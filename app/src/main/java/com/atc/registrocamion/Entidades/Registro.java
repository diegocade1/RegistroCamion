package com.atc.registrocamion.Entidades;

public class Registro {
    private String ID;
    private String Cliente_id;
    private String Terminal_id;
    private String Anden_id;
    private String Patente;
    private String Chofer;
    private String Hora_llegada_camion;
    private String Hora_ingreso_terminal;
    private String Hora_apertura_camion;
    private String Fecha_creacion;
    private String Usuario_id_responsable;
    //private boolean Terminado;

    public Registro(){

    }

    public Registro(String id,String cliente_id,String terminal_id,String anden_id
            ,String patente, String chofer, String hora_llegada_camion, String hora_ingreso_terminal
            ,String hora_apertura_camion, String fecha_creacion, String usuario_id_responsable)//,boolean terminado
    {
        this.Anden_id = anden_id;
        this.Chofer = chofer;
        this.Cliente_id = cliente_id;
        this.Fecha_creacion = fecha_creacion;
        this.Hora_apertura_camion = hora_apertura_camion;
        this.Hora_ingreso_terminal = hora_ingreso_terminal;
        this.Hora_llegada_camion = hora_llegada_camion;
        this.ID = id;
        this.Terminal_id = terminal_id;
        this.Patente = patente;
        this.Usuario_id_responsable = usuario_id_responsable;
        //this.Terminado = terminado;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCliente_id() {
        return Cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        Cliente_id = cliente_id;
    }

    public String getTerminal_id() {
        return Terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        Terminal_id = terminal_id;
    }

    public String getAnden_id() {
        return Anden_id;
    }

    public void setAnden_id(String anden_id) {
        Anden_id = anden_id;
    }

    public String getPatente() {
        return Patente;
    }

    public void setPatente(String patente) {
        Patente = patente;
    }

    public String getChofer() {
        return Chofer;
    }

    public void setChofer(String chofer) {
        Chofer = chofer;
    }

    public String getHora_llegada_camion() {
        return Hora_llegada_camion;
    }

    public void setHora_llegada_camion(String hora_llegada_camion) {
        Hora_llegada_camion = hora_llegada_camion;
    }

    public String getHora_ingreso_terminal() {
        return Hora_ingreso_terminal;
    }

    public void setHora_ingreso_terminal(String hora_ingreso_terminal) {
        Hora_ingreso_terminal = hora_ingreso_terminal;
    }

    public String getHora_apertura_camion() {
        return Hora_apertura_camion;
    }

    public void setHora_apertura_camion(String hora_apertura_camion) {
        Hora_apertura_camion = hora_apertura_camion;
    }

    public String getFecha_creacion() {
        return Fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        Fecha_creacion = fecha_creacion;
    }

    public String getUsuario_id_responsable() {
        return Usuario_id_responsable;
    }

    public void setUsuario_id_responsable(String usuario_id_responsable) {
        Usuario_id_responsable = usuario_id_responsable;
    }

//    public boolean isTerminado() {
//        return Terminado;
//    }
//
//    public void setTerminado(boolean terminado) {
//        Terminado = terminado;
//    }
}
