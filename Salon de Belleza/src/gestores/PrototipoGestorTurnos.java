package gestores;

import model.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PrototipoGestorTurnos {
    private MapaGenerico<LocalDate, List<Turno>> listaTurnos;


    public PrototipoGestorTurnos() {
        this.listaTurnos = new MapaGenerico<>();
    }

    public List<Turno> obtenerTurnosDisponibles(LocalDate fecha) {
        return listaTurnos.obtener(fecha);
    }

    public void reservarTurno(LocalDate fecha) {
        ///pedir fecha
        List<Turno> turnosReservados = obtenerTurnosDisponibles(fecha);

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(20, 0);    // 8 p.m.

        System.out.println("Turnos disponibles del dia: " + fecha);

        while (horaInicio.isBefore(horaFin)) {

            if (!siEstaHorario(turnosReservados, horaInicio)) {
                //si te retorna true es porque ese horaario ya esta reservado, si da false lo muestra como horario disponible
                System.out.println(horaInicio);
            }
            // Avanzamos una hora para el siguiente turno
            horaInicio = horaInicio.plusHours(1);
        }
    }

    public boolean siEstaHorario(List<Turno> turnos, LocalTime horario) {
        if (turnos == null) {
            return false;
        } else if (turnos.isEmpty()) {
            return false;
        } else {
            for (Turno t : turnos) {
                if (t.getHorario().equals(horario)) {
                    return true;
                }
            }
        }
        return false;
    }


}
