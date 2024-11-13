package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona{

    public Cliente(String nombre, String apellido, String dni, String genero, String telefono) {
        super(nombre, apellido, dni, genero, telefono);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
