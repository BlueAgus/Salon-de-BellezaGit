package model;

import java.util.ArrayList;
import java.util.List;

public class Profesional extends Persona {
    private String contraseña;
    private List<String> cod_servicios;

   ///////////////////////////// CONSTRUCTOR //////////////////////////////////////////////

    public Profesional(String nombre, String apellido, String dni, String genero, String telefono,String contraseña) {
        super(nombre, apellido, dni, genero, telefono);
        this.cod_servicios = new ArrayList<>();
        this.contraseña=contraseña;
    }

  /////////////////////////////////GET ////////////////////////////////////////////////////

    public List<String> getProfesiones() {
        return cod_servicios;
    }

    public void agregarProfesion(String cod_servicio) {
        cod_servicios.add(cod_servicio);
    }

    public boolean verificarProfesion(String cod_servicio) {
        return cod_servicios.contains(cod_servicio);
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


