package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TurnoArchivo {
    private String cod_turno;
    private String fecha;           // Fecha en formato "yyyy-MM-dd"
    private String horario;         // Hora en formato "HH:mm"
    private String codigo_servicio;
    private String dni_profesional;
    private String dni_cliente;

    public TurnoArchivo(String cod_turno, String fecha, String horario, String codigo_servicio, String dni_profesional, String dni_cliente) {
        this.cod_turno = cod_turno;
        this.fecha = fecha;
        this.horario = horario;
        this.codigo_servicio = codigo_servicio;
        this.dni_profesional = dni_profesional;
        this.dni_cliente = dni_cliente;
    }

   /* public static TurnoArchivo convertirTurno(Turno turnoOriginal) {
        // Formateadores para fecha y hora
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        // Convertir LocalDate y LocalTime a String
        String fecha = turnoOriginal.getFecha().format(formatoFecha);
        String horario = turnoOriginal.getHorario().format(formatoHora);

        String fecha = Turno.convertirLocalDateAString(formatoFecha);
        String horario = turnoOriginal.getHorario().format(formatoHora);

        // Crear y retornar el nuevo objeto TurnoConStrings
        return new TurnoArchivo(
                turnoOriginal.getCod_turno(),
                fecha,
                horario,
                turnoOriginal.getCodigo_servicio(),
                turnoOriginal.getDni_profesional(),
                turnoOriginal.getDni_cliente()
        );
    }*/
}

