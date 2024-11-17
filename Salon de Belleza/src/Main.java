import enumeraciones.TipoManicura;

import gestores.GestorPersona;

import gestores.GestorTurnos;
import model.Cliente;
import model.Manicura;
import model.Profesional;

import java.time.LocalTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
       /* System.out.println("HOLA HICE UN CAMBIO ");
        System.out.println("Holi soy agus jeje");*/

        GestorPersona profesionales=new GestorPersona();
        profesionales.agregarPersona(2);
        System.out.println(profesionales);

        GestorTurnos turnos = new GestorTurnos();
        Manicura manicura= new Manicura(LocalTime.of(1,30),TipoManicura.ESCULPIDAS);
        Profesional profesional= new Profesional("Daniela", "Vega", "45131280", "F", "223456346");
        Cliente cliente= new Cliente("Martina", "Rios", "44365444", "F", "223654789");

       // turnos.agregarTurno(manicura, profesional, cliente);
        }
    }

