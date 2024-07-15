package classes;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

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
    

    //constructor
    public Autor(int id, String nombre, String apellido, String email, String institucion, String CampoInvestigacion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.institucion = institucion;
        this.CampoInvestigacion = CampoInvestigacion;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getInstitucion() {
        return institucion;
    }
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
    public String getCampoInvestigacion() {
        return CampoInvestigacion;
    }
    public void setCampoInvestigacion(String CampoInvestigacion) {
        this.CampoInvestigacion = CampoInvestigacion;
    }

    //metodos de Autor
    public static int generateUniqueID() {
        int newID;
        do {
            newID = 100000000 + random.nextInt(900000000); // Genera un número entre 100,000,000 y 999,999,999
        } while (existingIDs.contains(newID));
        existingIDs.add(newID);
        return newID;
    }

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

    public static void GuardarAutor(Autor autor,String path) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            String autorInfo = String.format("%d,%s,%s,%s,%s,%s\n", autor.getId(), autor.getNombre(), autor.getApellido(), autor.getEmail(), autor.getInstitucion(), autor.getCampoInvestigacion());
            writer.write(autorInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cargarAutores(String path) {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length == 6){
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
        }catch(IOException e){

        }
    }
    public static ArrayList<Autor> getAutores(){
        return autores;
    }
}
