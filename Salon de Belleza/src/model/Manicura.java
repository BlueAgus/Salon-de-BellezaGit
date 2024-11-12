package model;

import Interfaces.MantenimientoMaquinas;
import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;

import java.time.LocalTime;

public class Manicura extends Servicio implements MantenimientoMaquinas {

    private TipoManicura tipoManicura;
    private boolean disenio;
    private static double precioDisenio = 10.0;

    ///si TipoServicio es por defecto no lo pedimos
    // hay un precio base? VER PRECIO DEPILACION


    public Manicura(double precio, LocalTime duracion, boolean disenio, TipoManicura tipoManicura) {
        super(TipoServicio.MANICURA , precio, duracion);
        this.disenio = disenio;
        this.tipoManicura = tipoManicura;
    }

    public TipoManicura getTipoManicura() {return tipoManicura;}

    public void setTipoManicura(TipoManicura tipoManicura) {this.tipoManicura = tipoManicura;}

    //implementacion interfaz, faltaaaaaaaaaaaa
    @Override
    public void MantenimientoMaquinas(Servicio servicio) {
        System.out.println("aca ira el metodo ");
    }


    @Override
    public double calcularPrecio() {
        double precioFinal =  this.precio + this.tipoManicura.getPrecio();
        if(disenio = true){
            precioFinal += precioDisenio;
        }
        return precioFinal;
    }


    public static double getPrecioDisenio() {
        return precioDisenio;
    }

    public static void setPrecioDisenio(double precioDisenio) {
        Manicura.precioDisenio = precioDisenio;
    }
}
