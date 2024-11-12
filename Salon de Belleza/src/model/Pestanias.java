package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;

public class Pestanias extends Servicio{
    private TipoPestanias tipoPestanias;

    public Pestanias( double precio, TipoPestanias tipoPestanias) {
        super(TipoServicio.PESTANIAS, precio, 1);
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
