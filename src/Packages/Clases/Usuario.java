publi Usuario{
    private String nombre, apellido, correo;
    private char rol;
    public Usuario(String nombre, String apellido, String correo, char rol){
        this.nombre=nombre;
        this.apellido=apellido;
        this.correo=correo;
        this.rol=rol;

    }
    public String getNombre(){
        return nombre;
    }
    public String getApellido(){
        return apellido;
    }
    public String getCorreo(){
        return correo;
    }
    public char getRol(){
        return rol;
    }
    pulic setNombre(nombre){
        this.nombre=nombre;
    }
    pulic setApellido(apellido){
        this.apellido=apellido;
    }
    pulic setCorreo(correo){
        this.correo=correo;
    }
}
