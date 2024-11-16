package model;

import java.util.Objects;
import java.util.UUID;

public abstract class Persona {
    /// lo cambio aca y en servicio por portected para que las clases hijas accedan directamente
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String genero; // esto si no se hace con un enum
    protected String telefono;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Persona(String nombre, String apellido, String dni, String genero, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.genero = genero;
        this.telefono = telefono;
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(dni, persona.dni);
    }

//////////////////////////////////////GETTERS Y SETTERS//////////////////////////////////////////////////////////////////////////////////

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) {  this.telefono = telefono; }


    //////////////////////////////////////////////////////// TO STRING ////////////////////////////////////////////////////
    @Override
    public String toString() {
        return
                "| Nombre : " + nombre + "\n" +
                        "| Apellido : " + apellido + "\n" +
                        "| DNI : " + dni + "\n" +
                        "| Genero : " + genero + "\n" +
                        "| Telefono : " + telefono + "\n" +
                        "=========================================\n";
    }
}