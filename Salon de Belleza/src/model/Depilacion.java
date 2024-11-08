package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.Disponibilidad;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;

public class Depilacion extends Servicio implements MantenimientoMaquinas {
    private TipoDepilacion tipoDepilacion;

    public Depilacion(TipoServicio tipoService, double precio, double duracion, TipoDepilacion tipoDepilacion) {
        super(TipoServicio.DEPILACION, precio, duracion);
        this.tipoDepilacion = tipoDepilacion;
    }


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

    @Override
    public double calcularPrecio() {
        return this.precio + this.tipoDepilacion.getPrecio();
    }
}
