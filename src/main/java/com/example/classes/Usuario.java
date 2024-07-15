package com.example.classes;


import com.example.enums.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;

/**
 * La clase Usuario representa un usuario del sistema.
 */
public class Usuario {
    protected String nombre;
    protected String apellido;
    protected String email;
    protected String username;
    protected String password;
    private Tipo tipo;
    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

    /**
     * Constructor de la clase Usuario.
     *
     * @param nombre    El nombre del usuario
     * @param apellido  El apellido del usuario
     * @param email     El correo electrónico del usuario
     * @param username  El nombre de usuario
     * @param password  La contraseña del usuario
     * @param tipo      El tipo de usuario (REVISOR o EDITOR)
     */
    public Usuario(String nombre, String apellido, String email, String username, String password, Tipo tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }
    
    // Getters y setters

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nuevo nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return El apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido El nuevo apellido del usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El nuevo correo electrónico del usuario
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return El nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username El nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el tipo de usuario.
     *
     * @return El tipo de usuario (REVISOR o EDITOR)
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de usuario.
     *
     * @param tipo El nuevo tipo de usuario (REVISOR o EDITOR)
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    /**
     * Carga los usuarios desde un archivo especificado.
     *
     * @param path La ruta del archivo desde donde se cargarán los usuarios
     */
    public static void cargarUsuarios(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String userName = data[0];
                String password = data[1];
                String email = data[2];
                String nombre = data[3];
                String apellido = data[4];
                String tipo = data[5];
                if ("R".equals(tipo)) {
                    Tipo obt_tipo = Tipo.REVISOR;
                    String especialidad = data[6];
                    int numeroRevisiones = Integer.parseInt(data[7]);
                    usuarios.add(new Revisor(nombre, apellido, email, userName, password, obt_tipo, especialidad, numeroRevisiones));
                } else if ("E".equals(tipo)) {
                    Tipo obt_tipo = Tipo.EDITOR;
                    String journal = data[6];
                    usuarios.add(new Editor(nombre, apellido, email, userName, password, obt_tipo, journal));
                }
            }
            System.out.println("Usuarios cargados");
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios");
        }
    }
    public static void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Archivos/usuarios.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String userName = data[0];
                String password = data[1];
                String email = data[2];
                String nombre = data[3];
                String apellido = data[4];
                String tipo = data[5];
                if ("R".equals(tipo)) {
                    Tipo obt_tipo = Tipo.REVISOR;
                    String especialidad = data[6];
                    int numeroRevisiones = Integer.parseInt(data[7]);
                    usuarios.add(new Revisor(nombre, apellido, email, userName, password, obt_tipo, especialidad, numeroRevisiones));
                } else if ("E".equals(tipo)) {
                    Tipo obt_tipo = Tipo.EDITOR;
                    String journal = data[6];
                    usuarios.add(new Editor(nombre, apellido, email, userName, password, obt_tipo, journal));
                }
            }
            System.out.println("Usuarios cargados");
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios");
        }
    }

    /**
     * Verifica las credenciales del usuario.
     *
     * @param username El nombre de usuario
     * @param password La contraseña del usuario
     * @return El usuario si las credenciales son correctas, de lo contrario, null
     */
    public static Usuario verificarUsuario(String username, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                System.out.println("Usuario encontrado");
                return usuario;  // Devuelve el usuario encontrado
            }
        }
        System.out.println("Usuario no encontrado");
        return null;  // Devuelve null si no se encuentra ningún usuario
    }

    /**
     * Obtiene la lista de usuarios.
     *
     * @return La lista de usuarios
     */
    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}