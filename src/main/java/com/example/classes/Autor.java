package com.example.classes;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

/**
 * La clase Autor representa a un autor en el sistema de gestión de artículos científicos.
 */
public class Autor {
    private static Scanner scanner = new Scanner(System.in); // Scanner para entrada de datos
    private static ArrayList<Integer> existingIDs = new ArrayList<>(); // Lista compartida de IDs existentes
    private static Random random = new Random(); // Generador de números aleatorios
    private static ArrayList<Autor> autores = new ArrayList<Autor>(); // Lista de autores

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String institucion;
    private String CampoInvestigacion;

    /**
     * Constructor de la clase Autor.
     *
     * @param id                El ID del autor
     * @param nombre            El nombre del autor
     * @param apellido          El apellido del autor
     * @param email             El correo electrónico del autor
     * @param institucion       La institución del autor
     * @param CampoInvestigacion El campo de investigación del autor
     */
    public Autor(int id, String nombre, String apellido, String email, String institucion, String CampoInvestigacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.institucion = institucion;
        this.CampoInvestigacion = CampoInvestigacion;
    }

    // Getters y setters

    /**
     * Obtiene el ID del autor.
     *
     * @return El ID del autor
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del autor.
     *
     * @param id El nuevo ID del autor
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del autor.
     *
     * @return El nombre del autor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del autor.
     *
     * @param nombre El nuevo nombre del autor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del autor.
     *
     * @return El apellido del autor
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del autor.
     *
     * @param apellido El nuevo apellido del autor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el correo electrónico del autor.
     *
     * @return El correo electrónico del autor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del autor.
     *
     * @param email El nuevo correo electrónico del autor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene la institución del autor.
     *
     * @return La institución del autor
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Establece la institución del autor.
     *
     * @param institucion La nueva institución del autor
     */
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }

    /**
     * Obtiene el campo de investigación del autor.
     *
     * @return El campo de investigación del autor
     */
    public String getCampoInvestigacion() {
        return CampoInvestigacion;
    }

    /**
     * Establece el campo de investigación del autor.
     *
     * @param CampoInvestigacion El nuevo campo de investigación del autor
     */
    public void setCampoInvestigacion(String CampoInvestigacion) {
        this.CampoInvestigacion = CampoInvestigacion;
    }

    /**
     * Devuelve una representación en forma de cadena del autor.
     *
     * @return Una cadena que representa al autor
     */
    @Override
    public String toString() {
        return String.format("ID: %d\nNombre: %s %s\nEmail: %s\nInstitución: %s\nCampo de Investigación: %s", id, nombre, apellido, email, institucion, CampoInvestigacion);
    }

    // Métodos de Autor

    /**
     * Genera un ID único para el autor.
     *
     * @return Un ID único para el autor
     */
    public static int generateUniqueID() {
        int newID;
        do {
            newID = 100000000 + random.nextInt(900000000); // Genera un número entre 100,000,000 y 999,999,999
        } while (existingIDs.contains(newID));
        existingIDs.add(newID);
        return newID;
    }

    /**
     * Crea un nuevo autor solicitando los datos al usuario.
     *
     * @return Un nuevo autor con los datos ingresados
     */
    public static Autor crearAutor() {
        System.out.println("Ingrese sus datos");
        System.out.print("Nombres: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellidos: ");
        String apellido = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Institución: ");
        String institucion = scanner.nextLine();
        System.out.print("Campo de Investigación: ");
        String CampoInvestigacion = scanner.nextLine();
        return new Autor(generateUniqueID(), nombre, apellido, email, institucion, CampoInvestigacion);
    }

    /**
     * Guarda la información del autor en un archivo.
     *
     * @param autor El autor a guardar
     * @param path  La ruta del archivo donde se guardará el autor
     */
    public static void GuardarAutor(Autor autor, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            String autorInfo = String.format("%d,%s,%s,%s,%s,%s\n", autor.getId(), autor.getNombre(), autor.getApellido(), autor.getEmail(), autor.getInstitucion(), autor.getCampoInvestigacion());
            writer.write(autorInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de autores desde un archivo.
     *
     * @param path La ruta del archivo desde donde se cargarán los autores
     */
    public static void cargarAutores(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int id = Integer.parseInt(data[0]);
                    String nombre = data[1];
                    String apellido = data[2];
                    String email = data[3];
                    String institucion = data[4];
                    String CampoInvestigacion = data[5];
                    Autor autor = new Autor(id, nombre, apellido, email, institucion, CampoInvestigacion);
                    autores.add(autor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la lista de autores.
     *
     * @return La lista de autores
     */
    public static ArrayList<Autor> getAutores() {
        return autores;
    }

    /**
     * Busca un autor por su ID.
     *
     * @param autorId El ID del autor a buscar
     * @return El autor encontrado o null si no se encuentra
     */
    public static Autor findAutorById(int autorId) {
        for (Autor autor : autores) {
            if (autor.getId() == autorId) {
                return autor;
            }
        }
        return null;  // Retorna null si no encuentra el autor
    }
}
