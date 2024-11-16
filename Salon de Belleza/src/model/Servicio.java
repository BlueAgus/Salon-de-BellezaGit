package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoServicio;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Servicio {

    private String codigo_servicio ; // me parece que esta al pedo
    protected TipoServicio tipoService;
    protected double precio;
    protected LocalTime duracion;
    protected Disponibilidad disponibilidad; // se refiere al puesto donde se realiza el servicio
    ///VER DISPONIBILIDAD!!!!!!!!!!!!!!!!!

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Servicio(TipoServicio tipoService, double precio, LocalTime duracion) {

        this.tipoService = tipoService;
        this.precio = precio;
        this.duracion = duracion;
        this.disponibilidad = Disponibilidad.DISPONIBLE;
        this.codigo_servicio = generarIDEunico();
    }
    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    public abstract double calcularPrecio();

    private String generarIDEunico() {
        long numeroUnico = (long) (Math.random() * 100L);  // Genera un número entre 0 y 100
        return String.valueOf(numeroUnico);
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////


    public String getCodigo_servicio() {return codigo_servicio;}

    public TipoServicio getTipoService() {
        return tipoService;
    }

    public void setTipoService(TipoServicio tipoService) {
        this.tipoService = tipoService;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

}

