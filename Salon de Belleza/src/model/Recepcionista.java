package model;

public class Recepcionista extends Persona{
    private String contraseña;
    //////////////////////////////////////// CONSTRUCTOR //////////////////////////////////////////////

    public Recepcionista(String nombre, String apellido, String dni, String genero, String telefono, String contraseña) {
        super(nombre, apellido, dni, genero, telefono);
        this.contraseña = contraseña;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
