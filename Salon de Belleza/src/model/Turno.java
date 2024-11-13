package model;

import enumeraciones.TipoDePago;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public class Turno {

    private LocalDate fecha;         // Fecha del turno
    private LocalTime horario;       // Hora del turno
    private String codigo_servicio;       // El servicio a realizar
    private String dni_profesional; // Profesional que atenderá el servicio
    private String dni_cliente;         // Cliente que recibirá el servicio

    // Constructor de la clase Turno

    public Turno(LocalDate fecha, LocalTime horario, String codigo_servicio, String dni_profesional, String dni_cliente) {
        this.fecha = fecha;
        this.horario = horario;
        this.codigo_servicio = codigo_servicio;
        this.dni_profesional = dni_profesional;
        this.dni_cliente = dni_cliente;
    }

    // Constructor simplificado para crear turnos de disponibilidad sin servicio, profesional y cliente
    public Turno(LocalDate fecha, LocalTime horario) {
        this.fecha = fecha;
        this.horario = horario;
        this.codigo_servicio = null;
        this.dni_profesional = null;
        this.dni_cliente = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turno turno = (Turno) o;
        return Objects.equals(fecha, turno.fecha) && Objects.equals(horario, turno.horario) && Objects.equals(servicio, turno.servicio);
    }



    // Métodos Getters y Setters


    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getCodigo_servicio() {
        return codigo_servicio;
    }

    public void setCodigo_servicio(String codigo_servicio) {
        this.codigo_servicio = codigo_servicio;
    }

    public String getDni_profesional() {
        return dni_profesional;
    }

    public void setDni_profesional(String dni_profesional) {
        this.dni_profesional = dni_profesional;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    @Override
    public String toString() {
        return "\n        TURNO: " +
                "\n| FECHA : " + fecha +
                "\n| HORARIO : " + horario +
                "\n| SERVICIO : " + servicio.getTipoService() +
                "\n| PROFESIONAL :" + profesional.getNombre() +
                "\n| CLIENTE : " + cliente.getNombre() +
                " DNI : "+ cliente.getDni();
    }


}
