package com.example.classes;
import com.example.enums.*;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

/**
 * La clase Revision representa una revisión de un artículo realizada por un revisor.
 */
public class Revision {
    private Articulo articulo;
    private Revisor revisor; 
    private String[] comentarios;
    private Estado estado;
    private static ArrayList<Revision> revisiones = new ArrayList<Revision>();

    /**
     * Constructor de la clase Revision.
     *
     * @param articulo    El artículo que se está revisando
     * @param revisor     El revisor que realiza la revisión
     * @param comentarios Los comentarios del revisor sobre el artículo
     * @param estado      El estado de la revisión (aprobado/rechazado)
     */
    public Revision(Articulo articulo, Revisor revisor, String[] comentarios, Estado estado) {
        this.articulo = articulo;
        this.revisor = revisor;
        this.comentarios = comentarios;
        this.estado = estado;
    }

    // Getters y setters

    /**
     * Obtiene el artículo que se está revisando.
     *
     * @return El artículo que se está revisando
     */
    public Articulo getArticulo() {
        return articulo;
    }

    /**
     * Establece el artículo que se está revisando.
     *
     * @param articulo El nuevo artículo que se está revisando
     */
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    /**
     * Obtiene el revisor que realiza la revisión.
     *
     * @return El revisor que realiza la revisión
     */
    public Revisor getRevisor() {
        return revisor;
    }

    /**
     * Establece el revisor que realiza la revisión.
     *
     * @param revisor El nuevo revisor que realiza la revisión
     */
    public void setRevisor(Revisor revisor) {
        this.revisor = revisor;
    }

    /**
     * Obtiene los comentarios del revisor sobre el artículo.
     *
     * @return Los comentarios del revisor
     */
    public String[] getComentarios() {
        return comentarios;
    }

    /**
     * Establece los comentarios del revisor sobre el artículo.
     *
     * @param comentarios Los nuevos comentarios del revisor
     */
    public void setComentarios(String[] comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Obtiene el estado de la revisión (aprobado/rechazado).
     *
     * @return El estado de la revisión
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la revisión (aprobado/rechazado).
     *
     * @param estado El nuevo estado de la revisión
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Guarda una revisión en el archivo especificado.
     *
     * @param articulo    El artículo que se está revisando
     * @param revisor     El revisor que realiza la revisión
     * @param comentarios Los comentarios del revisor
     * @param estado      El estado de la revisión (aprobado/rechazado)
     * @param path        La ruta del archivo donde se guardará la revisión
     */
    public static void guardarRevisiones(Articulo articulo, Revisor revisor, String[] comentarios, String estado, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {  // Usar 'true' para añadir en lugar de sobrescribir
            // Construir la cadena de comentarios
            StringBuilder comentariosBuilder = new StringBuilder();
            for (int i = 0; i < comentarios.length; i++) {
                comentariosBuilder.append(comentarios[i]);
                if (i < comentarios.length - 1) {
                    comentariosBuilder.append(";");  // Añade un punto y coma entre comentarios
                }
            }
    
            // Preparar la línea completa para escribir en el archivo
            String registro = articulo.getId() + "," + revisor.getUsername() + "," + comentariosBuilder.toString() + "," + estado + "\n";
    
            // Escribir la línea en el archivo
            bw.write(registro);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga las revisiones desde el archivo especificado.
     *
     * @param path La ruta del archivo desde donde se cargarán las revisiones
     * @return Una lista de revisiones cargadas desde el archivo
     */
    public static ArrayList<Revision> cargarRevisiones(String path) {
        ArrayList<Revision> revisiones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
    
                int articuloId = Integer.parseInt(parts[0].trim());
                String revisorUsername = parts[1].trim();
                String[] comentarios = parts[2].split(";");
                Estado estado = Estado.fromString(parts[3].trim());
    
                Articulo articulo = Articulo.findArticuloById(articuloId);
                Revisor revisor = Revisor.findRevisorByUsername(revisorUsername);
                if (articulo != null && revisor != null) {
                    Revision revision = new Revision(articulo, revisor, comentarios, estado);
                    revisiones.add(revision);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar las revisiones: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error en el formato del estado: " + e.getMessage());
        }
        return revisiones;
    }

    /**
     * Obtiene la lista de todas las revisiones.
     *
     * @return La lista de revisiones
     */
    public static ArrayList<Revision> getRevisiones() {
        return revisiones;
    }
}
