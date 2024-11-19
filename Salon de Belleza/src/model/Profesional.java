package model;

import enumeraciones.TipoDeProfesional;

import java.util.ArrayList;
import java.util.List;

public class Profesional extends Persona {

    private List<String> cod_servicios;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Profesional(String nombre, String apellido, String dni, String genero, String telefono) {
        super(nombre, apellido, dni, genero, telefono);
        this.cod_servicios = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////GET ////////////////////////////////////////////////////
    public List<String> getProfesiones() {
        return cod_servicios;
    }

    public void agregarProfesion(String cod_servicio) {
        cod_servicios.add(cod_servicio);
    }

    public boolean verificarProfesion(String cod_servicio) {
        return cod_servicios.contains(cod_servicio);
    }


}


