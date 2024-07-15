package com.example.clases;

public Autor extends Usuario{
    private String codigo, institucion, campoInvestigacion;
    publi Autor(String nombre, String apellido, String correo, String institucion, String campoInvestigacion,'a'){
        super(nombre,apellido,correo);
        this.institucion=institucion;
        this.campoInvestigacion=campoInvestigacion;
        

    }
    public String getCodigo(){
        return codigo;
    }
    public String getInstitucion(){
        return institucion;
    }
     public String getCampoInvestigacion(){
        return campoInvestigacion;
    }
    public setCodigo(codigo){
        this.codigo=codigo;
    }
    public setInstitucion(institucion){
        this.institucion=institucion;
    }
    public setCampoInvestigacion(campoInvestigacion){
        this.campoInvestigacion=campoInvestigacion;
    }

}