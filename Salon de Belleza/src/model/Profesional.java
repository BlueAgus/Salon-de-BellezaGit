package model;

import enumeraciones.TipoDeProfesional;

import java.util.ArrayList;
import java.util.List;

public class Profesional extends Persona{

    private List<TipoDeProfesional> profesiones;


    public Profesional(String nombre, String apellido, String dni, String genero, int telefono) {
        super(nombre, apellido, dni, genero, telefono);
        this.profesiones = new ArrayList<>();
    }

    public List<TipoDeProfesional> getProfesiones() {
        return profesiones;
    }

    public void setProfesiones(List<TipoDeProfesional> profesiones) {
        this.profesiones = profesiones;
    }
}
