package com.atc.registrocamion.Entidades;

import android.os.Parcelable;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String ID;
    private String Nombre;
    private String Apellido;

    public Usuario()
    {

    }

    public Usuario(String id,String nombre,String apellido)
    {
        this.ID = id;
        this.Nombre = nombre;
        this.Apellido = apellido;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

}
