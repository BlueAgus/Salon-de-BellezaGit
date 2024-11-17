package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;

import java.time.LocalTime;

public class Depilacion extends Servicio {
    private TipoDepilacion tipoDepilacion;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Depilacion(LocalTime duracion, TipoDepilacion tipoDepilacion) {
        super(TipoServicio.DEPILACION, duracion);
        this.tipoDepilacion = tipoDepilacion;
    }


    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public double calcularPrecio() {
        double precio = 0.0;
        if (tipoDepilacion == TipoDepilacion.CERA) {
            precio = tipoDepilacion.getPrecio();
        } else if (tipoDepilacion == TipoDepilacion.LASER) {
            precio = tipoDepilacion.getPrecio();
        }
        return this.precio;
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public TipoDepilacion getTipoDepilacion() {
        return tipoDepilacion;
    }

    public void setTipoDepilacion(TipoDepilacion tipoDepilacion) {
        this.tipoDepilacion = tipoDepilacion;
    }

    ///uso interfaz
  /*  @Override
    public void MantenimientoMaquinas(Servicio servicio) {
        System.out.println("aca ira el metodo");
    }*/


    //////////////////////////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "DEPILACIÓN " + tipoDepilacion +
                " \nPrecio= " + precio +
                " \nDuracion=" + duracion;
    }
}