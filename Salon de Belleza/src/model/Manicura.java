package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.Disponibilidad;
import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;

public class Manicura extends Servicio implements MantenimientoMaquinas {
private TipoManicura tipoManicura;

//LE FALTAN MIL COSAS
    public Manicura(TipoServicio tipoService, double precio, double duracion, Disponibilidad disponibilidad, TipoManicura tipoManicura) {
        super(tipoService, precio, duracion, disponibilidad);
        this.tipoManicura = tipoManicura;
    }

    ///getter y setter ///////////////////////

    public TipoManicura getTipoManicura() {return tipoManicura;}
    public void setTipoManicura(TipoManicura tipoManicura) {this.tipoManicura = tipoManicura;}

    //implementacion interfaz
    @Override
    public void MantenimientoMaquinas(Servicio servicio) {
        System.out.println("aca ira el metodo ");
    }
}
