package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.Disponibilidad;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;

public class Depilacion implements MantenimientoMaquinas {
    private TipoDepilacion tipoDepilacion;

    public Depilacion(TipoDepilacion tipoDepilacion) {
        this.tipoDepilacion = tipoDepilacion;
    }

    public double calcularPrecio()
    {
        double precio=0.0;
        if(tipoDepilacion== TipoDepilacion.CERA)
        {
        precio=tipoDepilacion.getPrecio();
    }
        else if(tipoDepilacion== TipoDepilacion.LASER)
        {
            precio=tipoDepilacion.getPrecio();
        }
        return this.precio;
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
/*
    @Override
    public double calcularPrecio() {
        return this.precio + this.tipoDepilacion.getPrecio();
    }
    */

}
