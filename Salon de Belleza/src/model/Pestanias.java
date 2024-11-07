package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;

public class Pestanias extends Servicio{
    private TipoPestanias tipoPestanias;

    public Pestanias(TipoServicio tipoService, double precio, double duracion, Disponibilidad disponibilidad, TipoPestanias tipoPestanias) {
        super(tipoService, precio, duracion, disponibilidad);
        this.tipoPestanias = tipoPestanias;
    }

    /////////////////////////getter y setter

    public TipoPestanias getTipoPestanias() {return tipoPestanias;}

    public void setTipoPestanias(TipoPestanias tipoPestanias) {this.tipoPestanias = tipoPestanias;}

}
