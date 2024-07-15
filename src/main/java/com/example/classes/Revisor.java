package com.example.classes;

import com.example.enums.*;
import java.io.*;
import java.util.*;

/**
 * La clase Revisor representa a un revisor de artículos en el sistema.
 */
public class Revisor extends Usuario {
    static Scanner scanner = new Scanner(System.in);
    private String especialidad;
    private int numeroRevisiones;
    private static ArrayList<Revisor> revisores = new ArrayList<>();
    private static ArrayList<String> comentarios = new ArrayList<>();
    private List<Articulo> articulosAsignados = new ArrayList<>();

    /**
     * Constructor de la clase Revisor.
     *
     * @param nombre         El nombre del revisor
     * @param apellido       El apellido del revisor
     * @param email          El correo electrónico del revisor
     * @param username       El nombre de usuario del revisor
     * @param password       La contraseña del revisor
     * @param tipo           El tipo de usuario (REVISOR)
     * @param especialidad   La especialidad del revisor
     * @param numeroRevisiones El número de revisiones realizadas por el revisor
     */
    public Revisor(String nombre, String apellido, String email, String username, String password, Tipo tipo, String especialidad, int numeroRevisiones) {
        super(nombre, apellido, email, username, password, tipo);
        this.especialidad = especialidad;
        this.numeroRevisiones = numeroRevisiones;
    }

    // Getters and setters

    /**
     * Obtiene la especialidad del revisor.
     *
     * @return La especialidad del revisor
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad del revisor.
     *
     * @param especialidad La nueva especialidad del revisor
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Obtiene el número de revisiones realizadas por el revisor.
     *
     * @return El número de revisiones realizadas por el revisor
     */
    public int getNumeroRevisiones() {
        return numeroRevisiones;
    }

    /**
     * Establece el número de revisiones realizadas por el revisor.
     *
     * @param numeroRevisiones El nuevo número de revisiones
     */
    public void setNumeroRevisiones(int numeroRevisiones) {
        this.numeroRevisiones = numeroRevisiones;
    }

    /**
     * Obtiene la lista de artículos asignados al revisor.
     *
     * @return La lista de artículos asignados
     */
    public List<Articulo> getArticulosAsignados() {
        return articulosAsignados;
    }

    /**
     * Establece la lista de artículos asignados al revisor.
     *
     * @param articulosAsignados La nueva lista de artículos asignados
     */
    public void setArticulosAsignados(List<Articulo> articulosAsignados) {
        this.articulosAsignados = articulosAsignados;
    }

    /**
     * Carga la lista de revisores desde los usuarios.
     */
    public static void cargarRevisores() {
        ArrayList<Usuario> usuarios = Usuario.getUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Revisor) {
                Revisor revisor = (Revisor) usuario;
                revisores.add(revisor);
            }
        }
        System.out.println(revisores.size() + " revisores cargados.");
    }

    /**
     * Obtiene la lista de revisores.
     *
     * @return La lista de revisores
     */
    public static ArrayList<Revisor> getRevisores() {
        return revisores;
    }

    /**
     * Permite al revisor revisar un artículo.
     *
     * @param revisor El revisor que realizará la revisión
     * @param path1   La ruta del archivo de asignaciones
     * @param path2   La ruta del archivo de revisiones
     */
    public static void RevisarArticulo(Revisor revisor, String path1, String path2) {
        ArrayList<Revision> revisionesExistentes = Revision.cargarRevisiones(path2);
        HashSet<Integer> articulosRevisadosIds = new HashSet<>();
        for (Revision revision : revisionesExistentes) {
            if (revision.getRevisor().getUsername().equals(revisor.getUsername())) {
                articulosRevisadosIds.add(revision.getArticulo().getId());
            }
        }

        ArrayList<Articulo> articulosAsignados = cargarAsignaciones(revisor, path1);
        articulosAsignados.removeIf(articulo -> articulosRevisadosIds.contains(articulo.getId()));

        if (articulosAsignados.isEmpty()) {
            System.out.println("No tiene artículos asignados para revisar o todos los asignados ya han sido revisados.");
        } else {
            mostrarArticulosYProcesar(revisor, articulosAsignados, path2);
        }
    }

    /**
     * Muestra los artículos asignados y permite al revisor seleccionar y revisar uno.
     *
     * @param revisor            El revisor que realizará la revisión
     * @param articulosAsignados La lista de artículos asignados
     * @param path2              La ruta del archivo de revisiones
     */
    private static void mostrarArticulosYProcesar(Revisor revisor, ArrayList<Articulo> articulosAsignados, String path2) {
        System.out.println("Artículos asignados para revisar:");
        for (Articulo articulo : articulosAsignados) {
            System.out.println("ID: " + articulo.getId() + ", Título: " + articulo.getTitulo());
        }

        System.out.print("Ingrese el ID del artículo que desea revisar: ");
        int articuloId = Integer.parseInt(scanner.nextLine());

        for (Articulo articulo : articulosAsignados) {
            if (articulo.getId() == articuloId) {
                comentarYDecidir(articulo, revisor, path2);
                break;
            }
        }
    }

    /**
     * Permite al revisor agregar comentarios y tomar una decisión sobre un artículo.
     *
     * @param articulo El artículo que se está revisando
     * @param revisor  El revisor que realiza la revisión
     * @param path2    La ruta del archivo de revisiones
     */
    private static void comentarYDecidir(Articulo articulo, Revisor revisor, String path2) {
        String[] comentarios = agregarComentarios();

        System.out.print("Aprueba (A) o Rechaza (R) el artículo?: ");
        String decision = scanner.nextLine().toUpperCase();

        switch (decision) {
            case "A":
                System.out.println("Ha aprobado el artículo.");
                break;
            case "R":
                System.out.println("Ha rechazado el artículo.");
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }
        Revision.guardarRevisiones(articulo, revisor, comentarios, decision, path2);
        revisor.incrementarRevisiones(); // Incrementar el número de revisiones
    }

    /**
     * Permite al revisor agregar comentarios a un artículo.
     *
     * @return Un array de comentarios
     */
    public static String[] agregarComentarios() {
        boolean seguirAgregando = true;

        while (seguirAgregando) {
            System.out.print("Ingrese su comentario: ");
            String comentario = scanner.nextLine();
            comentarios.add(comentario);

            System.out.print("¿Desea agregar otro comentario? (y/n): ");
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (!respuesta.equals("y")) {
                seguirAgregando = false;
            }
        }
        String[] arrayDeComentarios = new String[comentarios.size()];
        arrayDeComentarios = comentarios.toArray(arrayDeComentarios);
        return arrayDeComentarios;
    }

    /**
     * Carga las asignaciones de artículos para un revisor desde un archivo.
     *
     * @param revisor El revisor para quien se cargarán las asignaciones
     * @param path1   La ruta del archivo de asignaciones
     * @return La lista de artículos asignados
     */
    public static ArrayList<Articulo> cargarAsignaciones(Revisor revisor, String path1) {
        ArrayList<Articulo> articulos = Articulo.getArticulos();
        ArrayList<Articulo> articulosAsignados = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(revisor.getUsername())) {
                    int articuloId = Integer.parseInt(data[1].trim());
                    for (Articulo articulo : articulos) {
                        if (articulo.getId() == articuloId) {
                            articulosAsignados.add(articulo);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de asignaciones: " + e.getMessage());
        }
        return articulosAsignados;
    }

    /**
     * Carga las asignaciones de artículos para todos los revisores desde un archivo.
     *
     * @param path1 La ruta del archivo de asignaciones
     */
    public static void cargarAsignaciones(String path1) {
        for (Revisor revisor : revisores) {
            revisor.setArticulosAsignados(new ArrayList<>()); // Limpiar asignaciones previas
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path1))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String revisorUsername = data[0].trim();
                int articuloId = Integer.parseInt(data[1].trim());
                Articulo articulo = Articulo.findArticuloById(articuloId);
                if (articulo != null) {
                    Revisor revisor = Revisor.findRevisorByUsername(revisorUsername);
                    if (revisor != null) {
                        revisor.getArticulosAsignados().add(articulo);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de asignaciones: " + e.getMessage());
        }
    }

    /**
     * Busca un revisor por su nombre de usuario.
     *
     * @param username El nombre de usuario del revisor
     * @return El revisor encontrado o null si no se encuentra
     */
    public static Revisor findRevisorByUsername(String username) {
        for (Revisor revisor : revisores) {
            if (revisor.getUsername().equals(username)) {
                return revisor;
            }
        }
        return null; // Retorna null si no encuentra el revisor
    }

    /**
     * Incrementa el número de revisiones realizadas por el revisor y actualiza el archivo de usuarios.
     */
    public void incrementarRevisiones() {
        this.numeroRevisiones++;
        actualizarUsuarioArchivo();
    }

    /**
     * Actualiza el archivo de usuarios con el número actualizado de revisiones realizadas por el revisor.
     */
    private void actualizarUsuarioArchivo() {
        try {
            File inputFile = new File("src/main/resources/Archivos/usuarios.txt");
            File tempFile = new File("usuarios_temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data[0].equals(this.getUsername())) {
                    data[7] = String.valueOf(this.numeroRevisiones); // Actualizar el número de revisiones
                    writer.write(String.join(",", data) + System.lineSeparator());
                } else {
                    writer.write(currentLine + System.lineSeparator());
                }
            }

            writer.close();
            reader.close();

            if (!inputFile.delete()) {
                System.out.println("No se pudo eliminar el archivo original");
                return;
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("No se pudo renombrar el archivo temporal");
            }
        } catch (IOException e) {
            System.out.println("Error al actualizar el archivo de usuarios: " + e.getMessage());
        }
    }
}