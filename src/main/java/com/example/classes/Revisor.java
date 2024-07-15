package com.example.classes;

public class Revisor {
    //ATRIBUTOS
    private String accesoId;
    private String password;
    private String especialidad;
    private int numeroArticulo; 

    //CONSTRUCTOR
    public Revisor(String accesoId, String password, String especialidad, int numeroArticulo){
        this.accesoId = accesoId;
        this.password = password;
        this.especialidad = especialidad;
        this.numeroArticulo = numeroArticulo;
    
    //MÉTODOS GETTERS Y SETTERS 
    }
    public String getAccesoId(){
        return accesoId;
    }
    public void setAccesoId(String accesoId){
        this.accesoId = accesoId;
    }


    public String GetPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password  =password;
    }


    public String getEspecialidad(){
        return especialidad;
    }
    public void setEspecialidad(String especialidad){
        this.especialidad = especialidad;
    }


    public int getNumeroArticulo(){
        return numeroArticulo;
    }
    public void setNumeroEspecialidad(int numeroArticulo){
        this.numeroArticulo = numeroArticulo;
    }

}
