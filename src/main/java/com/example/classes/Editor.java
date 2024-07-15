package com.example.classes;

import com.example.gestordearticulos.enums.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * La clase Editor representa un editor que puede revisar y tomar decisiones sobre artículos científicos.
 */
public class Editor extends Usuario {
    private static final String PATH_PUBLICACIONES = "src/main/resources/Archivos/publicaciones.txt";
    private static Scanner scanner = new Scanner(System.in);
    private String nombreJornal;

    /**
     * Constructor de la clase Editor.
     *
     * @param nombre       El nombre del editor
     * @param apellido     El apellido del editor
     * @param email        El correo electrónico del editor
     * @param username     El nombre de usuario del editor
     * @param password     La contraseña del editor
     * @param tipo         El tipo de usuario (EDITOR)
     * @param nombreJornal El nombre del jornal
     */
    public Editor(String nombre, String apellido, String email, String username, String password, Tipo tipo, String nombreJornal) {
        super(nombre, apellido, email, username, password, tipo);
        this.nombreJornal = nombreJornal;
    }

    // Getters y setters

    /**
     * Obtiene el nombre del jornal.
     *
     * @return El nombre del jornal
     */
    public String getNombreJornal() {
        return nombreJornal;
    }

    /**
     * Establece el nombre del jornal.
     *
     * @param nombreJornal El nuevo nombre del jornal
     */
    public void setNombreJornal(String nombreJornal) {
        this.nombreJornal = nombreJornal;
    }

    /**
     * Presenta las revisiones al editor para que tome una decisión sobre los artículos.
     *
     * @param revisiones La lista de revisiones a presentar
     */
    public static void presentarRevisionesAEditor(ArrayList<Revision> revisiones) {
        HashSet<Integer> articulosProcesados = cargarArticulosProcesados();
        HashMap<Integer, List<Revision>> revisionesPorArticulo = new HashMap<>();

        for (Revision revision : revisiones) {
            if (!articulosProcesados.contains(revision.getArticulo().getId())) {
                revisionesPorArticulo.computeIfAbsent(revision.getArticulo().getId(), k -> new ArrayList<>()).add(revision);
            }
        }

        if (revisionesPorArticulo.isEmpty()) {
            System.out.println("No hay artículos con revisiones completas para mostrar.");
            return;
        }

        System.out.println("Artículos con revisiones completas:");
        revisionesPorArticulo.entrySet().stream()
            .filter(entry -> entry.getValue().size() == 2)  // Asegurar que hay exactamente dos revisiones
            .forEach(entry -> System.out.println("Artículo ID: " + entry.getKey()));

        System.out.println("\nIngrese el ID del artículo que desea revisar o presione 'R' para regresar:");
        String input = scanner.nextLine().toUpperCase();

        if ("R".equals(input)) {
            System.out.println("Regresando al menú anterior...");
        } else {
            try {
                int articuloId = Integer.parseInt(input);
                if (revisionesPorArticulo.containsKey(articuloId) && revisionesPorArticulo.get(articuloId).size() == 2) {
                    mostrarDetallesArticulo(revisionesPorArticulo.get(articuloId), articuloId);
                } else {
                    System.out.println("ID no válido o artículo no tiene exactamente dos revisiones.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, introduzca un número válido.");
            }
        }
    }

    /**
     * Carga los artículos procesados desde el archivo de publicaciones.
     *
     * @return Un conjunto de IDs de artículos procesados
     */
    private static HashSet<Integer> cargarArticulosProcesados() {
        HashSet<Integer> procesados = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_PUBLICACIONES))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int id = Integer.parseInt(line.split(",")[0].trim());
                procesados.add(id);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de publicaciones: " + e.getMessage());
        }
        return procesados;
    }

    /**
     * Muestra los detalles de las revisiones de un artículo y permite al editor tomar una decisión.
     *
     * @param revisiones La lista de revisiones del artículo
     * @param articuloId El ID del artículo
     */
    private static void mostrarDetallesArticulo(List<Revision> revisiones, int articuloId) {
        System.out.println("Detalles del Artículo ID: " + articuloId);
        for (Revision rev : revisiones) {
            System.out.println("Revisor: " + rev.getRevisor().getUsername() +
                                ", Estado: " + rev.getEstado() +
                                ", Comentarios: " + Arrays.toString(rev.getComentarios()));
        }
        tomarDecision(articuloId, revisiones);
    }

    /**
     * Permite al editor tomar una decisión sobre un artículo (publicar o cancelar).
     *
     * @param articuloId El ID del artículo
     * @param revisiones La lista de revisiones del artículo
     */
    public static void tomarDecision(int articuloId, List<Revision> revisiones) {
        System.out.println("¿Desea publicar (P) o cancelar (C) el artículo " + articuloId + "? Presione 'R' para regresar a la selección de artículos");
        String decision = scanner.nextLine().toUpperCase();
        Estado estadoFinal = Estado.CANCELADO; // Estado predeterminado
        switch (decision) {
            case "P":
                escribirEnArchivoPublicaciones(articuloId, Estado.PUBLICADO);
                estadoFinal = Estado.PUBLICADO;
                break;
            case "C":
                escribirEnArchivoPublicaciones(articuloId, Estado.CANCELADO);
                estadoFinal = Estado.CANCELADO;
                break;
            case "R":
                System.out.println("Regresando...");
                return;
            default:
                System.out.println("Opción no válida.");
                return;
        }
        // Preparar los comentarios para el correo
        StringBuilder comentariosBuilder = new StringBuilder();
        for (Revision rev : revisiones) {
            comentariosBuilder.append("Revisor: ")
                            .append(rev.getRevisor().getUsername())
                            .append(", Comentarios: ")
                            .append(Arrays.toString(rev.getComentarios()))
                            .append("\n");
        }
        // Enviar correo al autor
        Correo.enviarCorreo_Autor(articuloId, estadoFinal.toString(), comentariosBuilder.toString());
    }

    /**
     * Escribe la decisión del editor en el archivo de publicaciones.
     *
     * @param articuloId El ID del artículo
     * @param estado     El estado final del artículo (publicado o cancelado)
     */
    private static void escribirEnArchivoPublicaciones(int articuloId, Estado estado) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_PUBLICACIONES, true))) {
            writer.write(articuloId + "," + estado.toString().substring(0, 1) + "\n"); // Escribir ID y estado
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de publicaciones: " + e.getMessage());
        }
    }
}