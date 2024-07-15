package com.example;

import com.example.classes.*;
import com.example.enums.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Clase principal de la aplicación de gestión de artículos científicos.
 */
public class App {
    // Rutas de archivo
    public static final String RUTA_AUTORES = "src/main/resources/Archivos/investigadores.txt";
    public static final String RUTA_ARTICULOS = "src/main/resources/Archivos/articulos.txt";
    public static final String RUTA_USUARIO = "src/main/resources/Archivos/usuarios.txt";
    public static final String RUTA_REVISIONES = "src/main/resources/Archivos/revisiones.txt";
    public static final String RUTA_ASIGNACIONES = "src/main/resources/Archivos/asignaciones.txt";
    public static final String RUTA_REVISTA = "src/main/resources/Archivos/publicaciones.txt";

    private static Scanner scanner = new Scanner(System.in);
    private static boolean continuarEnApp = true;

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos
     * @throws Exception Si ocurre algún error durante la ejecución
     */
    public static void main(String[] args) throws Exception {
        Usuario.cargarUsuarios(RUTA_USUARIO);
        Articulo.cargarArticulos(RUTA_ARTICULOS);
        Revisor.cargarRevisores();
        Revision.cargarRevisiones(RUTA_REVISIONES);
        Autor.cargarAutores(RUTA_AUTORES);

        while (continuarEnApp) {
            menuInicio();
        }
    }

    /**
     * Muestra el menú de inicio y maneja la selección del usuario.
     */
    public static void menuInicio() {
        System.out.println();
        System.out.println("Bienvenido al sistema de gestión de revistas científicas");
        System.out.println("1. Someter articulo");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Mostrar revista");
        System.out.println("4. Salir");
        System.out.print("Ingrese su opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                System.out.println();
                System.out.println("Bienvenido, Autor. No es necesario iniciar sesión.");
                menuSometer();
                break;
            case "2":
                System.out.println();
                System.out.println("Ingrese sus credenciales para iniciar sesión.");
                menuIniciarSesion();
                break;
            case "3":
                Revista(RUTA_REVISTA);
                break;
            case "4":
                continuarEnApp = false;
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida, intente de nuevo.");
                break;
        }
    }

    /**
     * Muestra el menú para someter un artículo.
     */
    public static void menuSometer() {
        Autor autor = Autor.crearAutor();
        boolean continuarSometer = true;
        System.out.println();
        while (continuarSometer) {
            System.out.println("Desea escribir un articulo?");
            System.out.println("1. Si");
            System.out.println("2. No, volver al inicio");
            System.out.print("Ingrese su opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    Autor.GuardarAutor(autor, RUTA_AUTORES);
                    Articulo articulo = Articulo.crearArticulo(autor.getId());
                    Articulo.GuardarArticulo(articulo, RUTA_ARTICULOS);
                    Correo.enviarCorreo_Revisores(articulo);
                    System.out.println("Artículo guardado con éxito.");
                    // Recargar datos de artículos y asignaciones después de someter un artículo
                    Articulo.cargarArticulos(RUTA_ARTICULOS);
                    Revisor.cargarAsignaciones(RUTA_ASIGNACIONES);
                    break;
                case "2":
                    continuarSometer = false;
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
                    break;
            }
        }
    }

    /**
     * Muestra el menú para iniciar sesión.
     */
    public static void menuIniciarSesion() {
        System.out.println();
        System.out.print("Ingrese su nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String password = scanner.nextLine();
        Usuario usuarioEncontrado = Usuario.verificarUsuario(username, password);
        if (usuarioEncontrado != null) {
            System.out.println();
            System.out.println("Acceso concedido, bienvenido " + usuarioEncontrado.getNombre());
            if (usuarioEncontrado.getTipo() == Tipo.REVISOR) {
                Revisor revisor = (Revisor) usuarioEncontrado;
                Revisor.cargarAsignaciones(RUTA_ASIGNACIONES); // Asegurarse de que las asignaciones estén actualizadas
                menuRevisor(revisor);
            } else if (usuarioEncontrado.getTipo() == Tipo.EDITOR) {
                Editor editor = (Editor) usuarioEncontrado;
                menuEditor(editor);
            }
        } else {
            System.out.println("Credenciales incorrectas, por favor intente de nuevo.");
            menuIniciarSesion();
        }
    }

    /**
     * Muestra el menú para un revisor.
     *
     * @param usuarioEncontrado El revisor que ha iniciado sesión
     */
    public static void menuRevisor(Revisor usuarioEncontrado) {
        System.out.println("1. Revisar articulos");
        System.err.println("2. Verificar cuantos articulos ha revisado");
        System.out.println("3. Salir");

        System.out.print("Ingrese su opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                Revisor.RevisarArticulo(usuarioEncontrado, RUTA_ASIGNACIONES, RUTA_REVISIONES);
                break;
            case "2":
                System.out.println("Se han revisado:" + usuarioEncontrado.getNumeroRevisiones());
                break;
            case "3":
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida, intente de nuevo.");
                break;
        }
    }

    /**
     * Muestra el menú para un editor.
     *
     * @param usuarioEncontrado El editor que ha iniciado sesión
     */
    public static void menuEditor(Editor usuarioEncontrado) {
        System.out.println("1. Tomar decisiones sobre artículos");
        System.out.println("2. Salir");

        System.out.print("Ingrese su opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                System.out.println("Revisando articulos...");
                ArrayList<Revision> revisiones = Revision.cargarRevisiones(RUTA_REVISIONES);
                Editor.presentarRevisionesAEditor(revisiones);
                break;
            case "2":
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción no válida, intente de nuevo.");
                break;
        }
    }

    /**
     * Muestra la revista con los artículos publicados.
     *
     * @param path La ruta del archivo de publicaciones
     */
    public static void Revista(String path) {
        HashMap<Integer, Articulo> articulosPublicados = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                char estado = parts[1].charAt(0); // Asumiendo que el estado está en la posición 5

                if (estado == 'P') {
                    Articulo articulo = Articulo.findArticuloById(id);
                    if (articulo != null) {
                        articulosPublicados.put(id, articulo);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de publicaciones: " + e.getMessage());
        }

        if (!articulosPublicados.isEmpty()) {
            System.out.println("-----------------------------------------");
            System.out.println("Revista Científica miniCOLON");
            int contador = 1;
            for (Articulo articulo : articulosPublicados.values()) {
                System.out.println("-----------------------------------------");
                System.out.println("Artículo " + contador + ":");
                System.out.println("-----------------------------------------");
                System.out.println("Título: " + articulo.getTitulo());
                System.out.println("Resumen: " + articulo.getResumen());
                System.out.println("Contenido: " + articulo.getContenido());
                System.out.println("Palabras Clave: " + articulo.getPalabrasClave());
                System.out.println("-----------------------------------------\n");
                contador++;
            }
        } else {
            System.out.println("No hay artículos publicados para mostrar.");
        }
    }
}