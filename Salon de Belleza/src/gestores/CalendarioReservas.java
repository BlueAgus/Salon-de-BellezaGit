package gestores;

import com.google.gson.Gson;
import model.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// revisar
public class CalendarioReservas {
    private MapaGenerico<LocalDate, List<Turno>> calendarioTurnos;
    private MapaGenerico<LocalDate, List<Turno>> turnosReservados;
    //private Gson geson;

    public CalendarioReservas() {
        this.calendarioTurnos = new MapaGenerico<>();
        this.turnosReservados = new MapaGenerico<>();
    }

// agregamos a la lista los turnos de a una hora disponibles
    public void inicializarDia(LocalDate fecha) {
        List<Turno> turnosDelDia = new ArrayList<>();
        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(20, 0);    // 8 p.m.

        // Crear turnos de 1 hora sin intervalos
        while (horaInicio.isBefore(horaFin)) {
            // Creamos un turno sin servicio, profesional, ni cliente (disponible para reserva)
            Turno turnoDisponible = new Turno(fecha, horaInicio);
            turnosDelDia.add(turnoDisponible);

            // Avanzamos una hora para el siguiente turno
            horaInicio = horaInicio.plusHours(1);
        }
        calendarioTurnos.agregar(fecha, turnosDelDia);
    }

    // Obtener los turnos disponibles para una fecha
    public List<Turno> obtenerTurnosDisponibles(LocalDate fecha) {
        return calendarioTurnos.obtener(fecha);
    }

    public void reservarTurno(LocalDate fecha, Turno turnoReservado) {
        List<Turno> turnos = calendarioTurnos.obtener(fecha);

        if (turnos != null) {
            // Removemos el turno disponible de calendarioTurnos
            turnos.removeIf(turno -> turno.getHorario().equals(turnoReservado.getHorario()));

            // Actualizamos el calendarioTurnos con la lista actualizada de turnos disponibles
            calendarioTurnos.agregar(fecha, turnos);

            // Agregamos el turno reservado en turnosReservados
            List<Turno> reservasDelDia = turnosReservados.obtener(fecha);
            if (reservasDelDia == null) {
                reservasDelDia = new ArrayList<>();
            }
            reservasDelDia.add(turnoReservado);
            turnosReservados.agregar(fecha, reservasDelDia);
        }
    }

    // Hay dos listas, una con los turnos disponibles y otra con los reservados, faltarian los mtodos gson, para hacer los archivos
    //no se si me copa tanto tener dos archivos separados, capaz podriamos ver la forma de unir n una sola lista pero todavia no veo como
    
}
