package com.example.classes;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * La clase Articulo representa un artículo científico en el sistema de gestión.
 */
public class Articulo {
    static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    private int id_autor;
    private int id;
    private String titulo;
    private String resumen;
    private String contenido;
    private String palabrasClave;
    private static ArrayList<Articulo> articulos = new ArrayList<Articulo>();

    /**
     * Constructor de la clase Articulo.
     *
     * @param id_autor       El ID del autor del artículo
     * @param id             El ID del artículo
     * @param titulo         El título del artículo
     * @param resumen        El resumen del artículo
     * @param contenido      El contenido del artículo
     * @param palabrasClave  Las palabras clave del artículo
     */
    public Articulo(int id_autor, int id, String titulo, String resumen, String contenido, String palabrasClave) {
        this.id_autor = id_autor;
        this.id = id;
        this.titulo = titulo;
        this.resumen = resumen;
        this.contenido = contenido;
        this.palabrasClave = palabrasClave;
    }

    // Getters y setters

    /**
     * Obtiene el ID del autor del artículo.
     *
     * @return El ID del autor del artículo
     */
    public int getId_autor() {
        return id_autor;
    }

    /**
     * Establece el ID del autor del artículo.
     *
     * @param id_autor El nuevo ID del autor del artículo
     */
    public void setId_autor(int id_autor) {
        this.id_autor = id_autor;
    }

    /**
     * Obtiene el ID del artículo.
     *
     * @return El ID del artículo
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del artículo.
     *
     * @param id El nuevo ID del artículo
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el título del artículo.
     *
     * @return El título del artículo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del artículo.
     *
     * @param titulo El nuevo título del artículo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el resumen del artículo.
     *
     * @return El resumen del artículo
     */
    public String getResumen() {
        return resumen;
    }

    /**
     * Establece el resumen del artículo.
     *
     * @param resumen El nuevo resumen del artículo
     */
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    /**
     * Obtiene el contenido del artículo.
     *
     * @return El contenido del artículo
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido del artículo.
     *
     * @param contenido El nuevo contenido del artículo
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene las palabras clave del artículo.
     *
     * @return Las palabras clave del artículo
     */
    public String getPalabrasClave() {
        return palabrasClave;
    }

    /**
     * Establece las palabras clave del artículo.
     *
     * @param palabrasClave Las nuevas palabras clave del artículo
     */
    public void setPalabrasClave(String palabrasClave) {
        this.palabrasClave = palabrasClave;
    }

    /**
     * Crea un nuevo artículo solicitando los datos al usuario.
     *
     * @param id_autor El ID del autor del artículo
     * @return Un nuevo artículo con los datos ingresados
     */
    public static Articulo crearArticulo(Integer id_autor) {
        System.out.println("Ingrese los datos del artículo");
        int id_a = id_autor;
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Resumen: ");
        String resumen = scanner.nextLine();
        System.out.print("Contenido: ");
        String contenido = scanner.nextLine();
        System.out.print("Palabras clave: ");
        String palabrasClave = scanner.nextLine();
        return new Articulo(id_a, Autor.generateUniqueID(), titulo, resumen, contenido, palabrasClave);
    }

    /**
     * Guarda la información del artículo en un archivo.
     *
     * @param articulo El artículo a guardar
     * @param path     La ruta del archivo donde se guardará el artículo
     */
    public static void GuardarArticulo(Articulo articulo, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            String articuloInfo = String.format("%d,%d,%s,%s,%s,%s\n", articulo.getId_autor(), articulo.getId(), articulo.getTitulo(), articulo.getResumen(), articulo.getContenido(), articulo.getPalabrasClave());
            writer.write(articuloInfo);
        } catch (IOException e) {
            System.out.println("Error al guardar el artículo: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de artículos desde un archivo.
     *
     * @param path La ruta del archivo desde donde se cargarán los artículos
     */
    public static void cargarArticulos(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int id_autor = Integer.parseInt(data[0]);
                    int id = Integer.parseInt(data[1]);
                    String titulo = data[2];
                    String resumen = data[3];
                    String contenido = data[4];
                    String palabrasClave = data[5];
                    articulos.add(new Articulo(id_autor, id, titulo, resumen, contenido, palabrasClave));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los artículos: " + e.getMessage());
        }
    }

    /**
     * Obtiene la lista de artículos.
     *
     * @return La lista de artículos
     */
    public static ArrayList<Articulo> getArticulos() {
        return articulos;
    }

    /**
     * Asigna un artículo a dos revisores de manera aleatoria.
     *
     * @param articulo El artículo a asignar
     * @return Un array con los dos revisores asignados
     */
    public static Revisor[] asignarArticulo(Articulo articulo) {
        ArrayList<Revisor> revisores = Revisor.getRevisores();
        // Asegurarse de que hay al menos dos revisores
        if (revisores.size() < 2) {
            System.out.println("No hay suficientes revisores para asignar.");
            return null;
        }

        // Seleccionar dos revisores diferentes aleatoriamente
        int firstIndex = random.nextInt(revisores.size());
        int secondIndex;
        do {
            secondIndex = random.nextInt(revisores.size());
        } while (secondIndex == firstIndex);

        Revisor revisorUno = revisores.get(firstIndex);
        Revisor revisorDos = revisores.get(secondIndex);

        // Guardar las asignaciones en un archivo
        guardarAsignacion(revisorUno.getUsername(), articulo.getId());
        guardarAsignacion(revisorDos.getUsername(), articulo.getId());
        return new Revisor[]{revisorUno, revisorDos};
    }

    /**
     * Guarda una asignación de un artículo a un revisor en un archivo.
     *
     * @param usernameRevisor El nombre de usuario del revisor
     * @param idArticulo      El ID del artículo
     */
    private static void guardarAsignacion(String usernameRevisor, int idArticulo) {
        String filename = "src/main/resources/Archivos/asignaciones.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(usernameRevisor + "," + idArticulo + "\n");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de asignaciones: " + e.getMessage());
        }
    }

    /**
     * Busca un artículo por su ID.
     *
     * @param articuloId El ID del artículo a buscar
     * @return El artículo encontrado o null si no se encuentra
     */
    public static Articulo findArticuloById(int articuloId) {
        for (Articulo articulo : articulos) {
            if (articulo.getId() == articuloId) {
                return articulo;
            }
        }
        return null;  // Retorna null si no encuentra el artículo
    }
}