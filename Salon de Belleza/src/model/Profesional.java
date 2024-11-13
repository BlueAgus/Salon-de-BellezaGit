package model;

import enumeraciones.TipoDeProfesional;

import java.util.ArrayList;
import java.util.List;

public class Profesional extends Persona{

    private List<TipoDeProfesional> profesiones;


    public Profesional(String nombre, String apellido, String dni, String genero, String telefono) {
        super(nombre, apellido, dni, genero, telefono);
        this.profesiones = new ArrayList<>();
    }

    public List<TipoDeProfesional> getProfesiones() {
        return profesiones;
    }

    public void agregarProfesion(TipoDeProfesional tipo)
    {
        profesiones.add(tipo);
    }

    public boolean verificarProfesion(TipoDeProfesional tipo)
    {
        return profesiones.contains(tipo);
    }
}
