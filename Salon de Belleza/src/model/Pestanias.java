package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;

import java.time.LocalTime;

public class Pestanias extends Servicio{
    private TipoPestanias tipoPestanias;

    public Pestanias(double precio, TipoPestanias tipoPestanias, LocalTime duracion) {
        super(TipoServicio.PESTANIAS, precio,duracion );
        this.tipoPestanias = tipoPestanias;
    }

    public TipoPestanias getTipoPestanias() {return tipoPestanias;}

    public void setTipoPestanias(TipoPestanias tipoPestanias) {
        this.tipoPestanias = tipoPestanias;
    }

    @Override
    public double calcularPrecio() {
        return this.precio + this.tipoPestanias.getPrecio();
    }

}
