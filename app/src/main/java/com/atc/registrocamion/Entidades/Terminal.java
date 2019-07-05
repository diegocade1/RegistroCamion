package com.atc.registrocamion.Entidades;

public class Terminal {
    private int ID;
    private String Nombre;

    public Terminal()
    {

    }

    public Terminal(int id,String nombre)
    {
        this.ID = id;
        this.Nombre = nombre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
