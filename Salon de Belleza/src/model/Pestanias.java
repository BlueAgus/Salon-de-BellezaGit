package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;

public class Pestanias extends Servicio{
    private TipoPestanias tipoPestanias;

    public Pestanias(TipoServicio tipoService, double precio, double duracion, TipoPestanias tipoPestanias) {
        super(TipoServicio.PESTANIAS, precio, duracion);
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
