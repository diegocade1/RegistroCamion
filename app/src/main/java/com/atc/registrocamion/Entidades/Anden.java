package com.atc.registrocamion.Entidades;

public class Anden {
    private int ID;
    private int Terminal_id;
    private String Nombre;

    public Anden()
    {

    }

    public Anden(int ID,int Anden_id,String Nombre)
    {
        this.Terminal_id = Anden_id;
        this.ID = ID;
        this.Nombre = Nombre;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTerminal_id() {
        return Terminal_id;
    }

    public void setTerminal_id(int anden_id) {
        Terminal_id = anden_id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
