package model;

import enumeraciones.TipoDePago;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Turno {

    private LocalDate fecha;         // Fecha del turno
    private LocalTime horario;       // Hora del turno
    private Servicio servicio;       // El servicio a realizar
    private Profesional profesional; // Profesional que atenderá el servicio
    private Cliente cliente;         // Cliente que recibirá el servicio
    private TipoDePago tipoDePago;   // Tipo de pago (Efectivo, Tarjeta, etc.)
    private double pagoTotal;        // Total del pago por el servicio

    // Constructor de la clase Turno
    public Turno(LocalDate fecha, LocalTime horario, Servicio servicio,
                 Profesional profesional, Cliente cliente,
                 TipoDePago tipoDePago, double pagoTotal) {
        this.fecha = fecha;
        this.horario = horario;
        this.servicio = servicio;
        this.profesional = profesional;
        this.cliente = cliente;
        this.tipoDePago = tipoDePago;


        // Calcular el total con el tipo de pago
        this.pagoTotal = tipoDePago.calcularPagoTotal(servicio.getPrecio());
    }

    // Métodos Getters y Setters
    public LocalDate getFecha() { return fecha; }

    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHorario() { return horario; }

    public void setHorario(LocalTime horario) { this.horario = horario; }

    public Servicio getServicio() { return servicio; }

    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Profesional getProfesional() { return profesional; }

    public void setProfesional(Profesional profesional) { this.profesional = profesional; }

    public Cliente getCliente() { return cliente; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public TipoDePago getTipoDePago() { return tipoDePago; }

    public void setTipoDePago(TipoDePago tipoDePago) { this.tipoDePago = tipoDePago; }

    public double getPagoTotal() { return pagoTotal; }

    public void setPagoTotal(double pagoTotal) { this.pagoTotal = pagoTotal; }

    @Override
    public String toString() {
        return "Turno{" +
                "fecha=" + fecha +
                ", horario=" + horario +
                ", servicio=" + servicio +
                ", profesional=" + profesional +
                ", cliente=" + cliente +
                ", tipoDePago=" + tipoDePago +
                ", pagoTotal=" + pagoTotal +
                '}';
    }


}
