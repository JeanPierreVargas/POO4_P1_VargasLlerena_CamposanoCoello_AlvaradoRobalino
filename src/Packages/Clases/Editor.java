public class Editor {
    //atributos
    private String trabajo, accessID, password;

    //constructores
    public Editor(String trabajo, String accessID, String password){
        this.trabajo = trabajo;
        this.accessID = accessID;
        this.password = password;
    }

    //metodos getters y setters
    public String getTrabajo(){
        return trabajo;
    }
    public void setTrabajo(String trabajo){
        this.trabajo = trabajo;
    }

    public String getAccessID(){
        return accessID;
    }
    public void setAccessID(String accessID){
        this.accessID = accessID;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
