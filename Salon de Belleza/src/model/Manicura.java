package model;

import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;

import java.time.LocalTime;

public class Manicura extends Servicio  {

    private TipoManicura tipoManicura;
    private static boolean disenio;
    private static double precioDisenio = GestorPrecios.getPrecioDisenio();

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Manicura(LocalTime duracion, TipoManicura tipoManicura) {
        super(TipoServicio.MANICURA, duracion);
        this.tipoManicura = tipoManicura;
        this.disenio = false;
        this.precioDisenio = precioDisenio;
        this.precio=calcularPrecio();
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public static double getPrecioDisenio() {
        return precioDisenio;
    }

    public static void setPrecioDisenio(double precioDisenio) {
        Manicura.precioDisenio = precioDisenio;
    }


    @Override
    public double calcularPrecio() {
        double precioBase = GestorPrecios.obtenerPrecio(Manicura.class, this.tipoManicura);

        if (disenio) {
            precioBase = GestorPrecios.agregarDisenio(this.tipoManicura);
        }

        return precioBase;
    }

   ////////////////////////////////////GET Y SET //////////////////////////////////////////////////

    public TipoManicura getTipoManicura() {return tipoManicura;}

    public void setTipoManicura(TipoManicura tipoManicura) {this.tipoManicura = tipoManicura;}

    public static boolean isDisenio() {
        return disenio;
    }

    public static void setDisenio(boolean disenio) {
        Manicura.disenio = disenio;
    }

   ////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "| MANICURA: " + tipoManicura +
                (disenio ? " con diseño "+ GestorPrecios.getPrecioDisenio() : " sin diseño ")+
                " \n| Precio: " + calcularPrecio() +
                " \n| Duracion: " + duracion ;
    }


}

