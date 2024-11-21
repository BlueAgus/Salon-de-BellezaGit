package model;


import enumeraciones.TipoServicio;
import interfaces.CrearID;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Servicio implements CrearID {

    protected String codigo_servicio ; //
    protected TipoServicio tipoService;
    protected LocalTime duracion;
    protected double precio = 0.0;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Servicio(TipoServicio tipoService, LocalTime duracion) {

        this.tipoService = tipoService;
        this.duracion = duracion;
        this.codigo_servicio = this.generarIDEunico();
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    public abstract double calcularPrecio();

    @Override // aca modificamos el metodo de la interfaz
    public String generarIDEunico() {
        long numeroUnico = (long) (Math.random() * 100L);  // Genera un número entre 0 y 100
        return String.valueOf(numeroUnico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(codigo_servicio, servicio.codigo_servicio);
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////
    public String getCodigo_servicio() {return codigo_servicio;}

    public TipoServicio getTipoService() {
        return tipoService;
    }

    public void setTipoService(TipoServicio tipoService) {
        this.tipoService = tipoService;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

