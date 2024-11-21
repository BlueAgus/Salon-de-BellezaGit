package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;
import gestores.GestorTurno;

import java.time.LocalDate;
import java.time.LocalTime;

public class Depilacion extends Servicio {

    private TipoDepilacion tipoDepilacion;

    //////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Depilacion(LocalTime duracion, TipoDepilacion tipoDepilacion) {
        super(TipoServicio.DEPILACION, duracion);
        this.tipoDepilacion = tipoDepilacion;
    }

   /////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
   public void programarMantenimiento(LocalDate fecha){
       GestorTurno turnos = new GestorTurno();


   }

    public double calcularPrecio() {
        return GestorPrecios.obtenerPrecio(Depilacion.class, this.tipoDepilacion);
    }

    /////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public TipoDepilacion getTipoDepilacion() {
        return tipoDepilacion;
    }

    public void setTipoDepilacion(TipoDepilacion tipoDepilacion) {
        this.tipoDepilacion = tipoDepilacion;
    }

   ////////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "DEPILACIÓN " + tipoDepilacion +
                " \nPrecio= " + calcularPrecio() +
                " \nDuracion=" + duracion;
    }
}