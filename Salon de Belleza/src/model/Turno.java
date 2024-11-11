package model;

import enumeraciones.TipoDePago;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Turno {

    private LocalDate fecha;         // Fecha del turno
    private LocalTime horario;       // Hora del turno
    private Servicio servicio;       // El servicio a realizar
    private Profesional profesional; // Profesional que atenderá el servicio
    private Cliente cliente;         // Cliente que recibirá el servicio


    // Constructor de la clase Turno
    public Turno(LocalDate fecha, LocalTime horario, Servicio servicio,
                 Profesional profesional, Cliente cliente) {
        this.fecha = fecha;
        this.horario = horario;
        this.servicio = servicio;
        this.profesional = profesional;
        this.cliente = cliente;
    }
    // Constructor simplificado para crear turnos de disponibilidad sin servicio, profesional y cliente
    public Turno(LocalDate fecha, LocalTime horario) {
        this.fecha = fecha;
        this.horario = horario;
        this.servicio = null;
        this.profesional = null;
        this.cliente = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turno turno = (Turno) o;
        return Objects.equals(fecha, turno.fecha) && Objects.equals(horario, turno.horario) && Objects.equals(servicio, turno.servicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, horario, servicio);
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

    @Override
    public String toString() {
        return "Turno{" +
                "fecha=" + fecha +
                ", horario=" + horario +
                ", servicio=" + servicio +
                ", profesional=" + profesional +
                ", cliente=" + cliente
                ;
    }


}
