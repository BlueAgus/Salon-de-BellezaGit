package model;

public class Cliente extends Persona{
    private String email; // yo estos los pondria en persona porque pr ej el jefe quiere llamar a alguna recepcionista
    private int telefono;// o profesional y no tiene los datos.

    public Cliente(String nombre, String apellido, String dni, char genero, String email, int telefono) {
        super(nombre, apellido, dni, genero);
        this.email = email;
        this.telefono = telefono;
    }



    //////////////////////////////////////GETTERS Y SETTERS//////////////////////////////////////////////////////////////////////////////////

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}
