package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.Disponibilidad;
import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;

public class Manicura extends Servicio implements MantenimientoMaquinas {
private TipoManicura tipoManicura;

    public Manicura(TipoServicio tipoService, double precio, double duracion, TipoManicura tipoManicura) {
        super(tipoService, precio, duracion);
        this.tipoManicura = tipoManicura;
    }


    public TipoManicura getTipoManicura() {return tipoManicura;}

    public void setTipoManicura(TipoManicura tipoManicura) {this.tipoManicura = tipoManicura;}

    //implementacion interfaz, faltaaaaaaaaaaaa
    @Override
    public void MantenimientoMaquinas(Servicio servicio) {
        System.out.println("aca ira el metodo ");
    }

}
