package com.example.classes;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * La clase Correo se encarga de enviar correos electrónicos a revisores y autores.
 */
public class Correo {
    
    private static Properties loadEmailProperties() {
        Properties properties = new Properties();
        try (InputStream input = Correo.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Lo sentimos, no se pudo encontrar config.properties");
                return null;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    /**
     * Envía un correo electrónico a los revisores asignados para revisar un artículo.
     *
     * @param articulo El artículo que se va a revisar
     */
    public static void enviarCorreo_Revisores(Articulo articulo) {
        Properties emailProperties = loadEmailProperties();
        if (emailProperties == null) {
            System.out.println("No se pudieron cargar las propiedades del correo.");
            return;
        }

        Revisor[] revisores = Articulo.asignarArticulo(articulo);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación
        String username = emailProperties.getProperty("email");
        String password = emailProperties.getProperty("password");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        for (Revisor revisor : revisores) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(revisor.getEmail()));
                message.setSubject("Revisión del artículo: " + articulo.getTitulo());
                message.setText("Resumen del artículo: " + articulo.getResumen() + "\n\n" 
                                        + "Por favor revise el artículo y dé su opinión.\n\n" + "Gracias por su colaboración.");
    
                Transport.send(message);
                System.out.println("Correo enviado correctamente a : " + revisor.getEmail());
    
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Enviando correos a los revisores...");
    }

    /**
     * Envía un correo electrónico al autor de un artículo con la revisión realizada.
     *
     * @param articuloId   El ID del artículo revisado
     * @param estado       El estado de la revisión (por ejemplo, "Aprobado" o "Rechazado")
     * @param comentarios  Los comentarios de los revisores sobre el artículo
     */
    public static void enviarCorreo_Autor(int articuloId, String estado, String comentarios) {
        Properties emailProperties = loadEmailProperties();
        if (emailProperties == null) {
            System.out.println("No se pudieron cargar las propiedades del correo.");
            return;
        }

        Articulo articulo = Articulo.findArticuloById(articuloId);
        Autor autor = Autor.findAutorById(articulo.getId_autor());
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    
        String username = emailProperties.getProperty("email");
        String password = emailProperties.getProperty("password");
    
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(autor.getEmail()));
            message.setSubject("Revisión del artículo: " + estado);
            message.setText("Detalles del artículo:\n" + comentarios);
    
            Transport.send(message);
            System.out.println("Correo enviado correctamente al autor a: " + autor.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar correo al autor.");
        }
    }
}
