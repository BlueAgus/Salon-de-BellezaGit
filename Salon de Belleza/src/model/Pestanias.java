package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;

import java.time.LocalTime;

public class Pestanias extends Servicio{

    private TipoPestanias tipoPestanias;

  //////////////////////////////////// CONSTRUCTOR ///////////////////////////////////////////////

    public Pestanias(LocalTime duracion, TipoPestanias tipoPestanias) {
        super(TipoServicio.PESTANIAS, duracion);
        this.tipoPestanias = tipoPestanias;
    }

    ////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    @Override
    public double calcularPrecio() {
        return GestorPrecios.obtenerPrecio(Pestanias.class, this.tipoPestanias);
    }

   /////////////////////////////////GET Y SET ////////////////////////////////////////////////////
    public TipoPestanias getTipoPestanias() {return tipoPestanias;}

    public void setTipoPestanias(TipoPestanias tipoPestanias) {
        this.tipoPestanias = tipoPestanias;
    }

 /////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "PESTAÑAS " + this.tipoPestanias +
                " \nPrecio= " + calcularPrecio() +
                " \nDuracion=" + duracion ;
    }
}
