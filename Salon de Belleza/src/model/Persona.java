package model;

import java.util.Objects;
import java.util.UUID;

public abstract class Persona {

    private String nombre;
    private String apellido;
    private String dni;
    private String genero; // esto si no se hace con un enum
    private int telefono;

    public Persona(String nombre, String apellido, String dni, String genero, int telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.genero = genero;
        this.telefono = telefono;
    }

    //////////////////////////////////////ToString e Equals//////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return
                "| Nombre : " + nombre + "\n" +
                        "| Apellido : " + apellido + "\n" +
                        "| DNI : " + dni + "\n" +
                        "| Genero : " + genero + "\n" +
                        "=========================================\n";
    }

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

    public int getTelefono() { return telefono; }

    public void setTelefono(int telefono) {  this.telefono = telefono; }
}