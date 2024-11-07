package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.Disponibilidad;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;

public class Depilacion extends Servicio implements MantenimientoMaquinas {
    private TipoDepilacion tipoDepilacion;

    public Depilacion(TipoServicio tipoService, double precio, double duracion, Disponibilidad disponibilidad, TipoDepilacion tipoDepilacion) {
        super(tipoService, precio, duracion, disponibilidad);
        this.tipoDepilacion = tipoDepilacion;
    }

    //////////////////////////////////////getter y setter

    public TipoDepilacion getTipoDepilacion() {
        return tipoDepilacion;
    }

    public void setTipoDepilacion(TipoDepilacion tipoDepilacion) {
        this.tipoDepilacion = tipoDepilacion;
    }

    ///uso interfaz
    @Override
    public void MantenimientoMaquinas(Servicio servicio)
    {
        System.out.println("aca ira el metodo");
    }
}
