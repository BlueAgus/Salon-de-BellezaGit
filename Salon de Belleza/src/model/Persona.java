package model;

import java.util.Objects;
import java.util.UUID;

public abstract class Persona {

    private String nombre;
    private String apellido;
    private String dni;
    private char genero; // esto si no se hace con un enum

    public Persona(String nombre, String apellido, String dni, char genero) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.genero = genero;
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

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

}