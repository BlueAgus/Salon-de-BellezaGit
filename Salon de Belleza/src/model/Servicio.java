package model;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoServicio;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Servicio {

   // private String codigo_servicio ; // me parece que esta al pedo
    protected TipoServicio tipoService;
    protected double precio;
    protected LocalTime duracion;
    protected Disponibilidad disponibilidad; // se refiere al puesto donde se realiza el servicio

    public Servicio(TipoServicio tipoService, double precio, LocalTime duracion) {

        this.tipoService = tipoService;
        this.precio = precio;
        this.duracion = duracion;
        this.disponibilidad = Disponibilidad.DISPONIBLE;
       // this.codigo_servicio = generarIDEunico();
    }

    public abstract double calcularPrecio();

    ////////////////////////////////////////////////////////get y set ////////////////////////////////////////////////////

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

    //////////////////////////////////////////////// Dudoso ////////////////////////////////////////////////////////

     /*  public void limpiezaDelLugar(long duracionMantenimientoMs){ // representa la duración en milisegundos durante la cual el servicio estará en mantenimiento.
       this.disponibilidad = Disponibilidad.MANTENIMIENTO;
        // Crea un temporizador para volver a poner el servicio disponible
        Timer timer = new Timer(); //timer es una clase de Java que permite programar tareas que se ejecutan en un hilo separado después de un retraso.
        timer.schedule(new TimerTask() { //se crea una nueva instancia de Timer, que se usará para gestionar la duración del mantenimiento.
            @Override //TimerTask es una clase abstracta que debe ser extendida para definir una tarea que se va a ejecutar por el Timer.
            public void run() { //Aquí se crea una nueva tarea (anónima) que se ejecutará después de un retraso especificado.
               // Mét odo sobrescrito run() de la clase TimerTask. Contiene el código que se ejecutará cuando el temporizador expire.
                // Cambia el estado a DISPONIBLE después de la duración especificada
                disponibilidad = Disponibilidad.DISPONIBLE;
                timer.cancel(); // Finaliza el temporizador. cancel() de la clase Timer. Detiene el temporizador y libera los recursos asociados.
            }
        }, duracionMantenimientoMs); //es el parámetro que determina cuánto tiempo, en milisegundos, el servicio estará en mantenimiento.
    }*/
    //long unaHoraEnMs = 60 * 60 * 1000; // 1 hora = 60 minutos * 60 segundos * 1000 milisegundos
    //limpiezaDelLugar(unaHoraEnMs); // Llama al mét odo con la duración de una hora
    // o si no pasarle directamente una hora en milisegundo que es 3600000
}

