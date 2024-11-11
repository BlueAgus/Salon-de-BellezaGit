import gestores.CalendarioReservas;
import gestores.PrototipoGestorTurnos;

import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
       /* System.out.println("HOLA HICE UN CAMBIO ");
        System.out.println("Holi soy agus jeje");*/

        PrototipoGestorTurnos turnos = new PrototipoGestorTurnos();
        turnos.reservarTurno(LocalDate.now());
        turnos.obtenerTurnosDisponibles(LocalDate.now());

        }
    }
