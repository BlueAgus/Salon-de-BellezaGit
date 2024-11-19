import Menus.MenuPrincipal;
import enumeraciones.TipoManicura;

import gestores.GestorPersona;

import gestores.GestorTurno;
import model.Cliente;
import model.Manicura;
import model.Profesional;

import java.time.LocalTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

       // GestorPersona profesionales=new GestorPersona();
        MenuPrincipal menuPrincipal=new MenuPrincipal();


       /* System.out.println("HOLA HICE UN CAMBIO ");
        System.out.println("Holi soy agus jeje");*/

        GestorPersona profesionales=new GestorPersona();
        profesionales.agregarPersona(2);
        System.out.println(profesionales);

        GestorTurno turnos = new GestorTurno();

      //  Manicura manicura= new Manicura(200, LocalTime.of(1,30),true,TipoManicura.ESCULPIDAS);
        Profesional profesional= new Profesional("Daniela", "Vega", "45131280", "F", "223456346");
        Cliente cliente= new Cliente("Martina", "Rios", "44365444", "F", "223654789");

       // turnos.agregarTurno(manicura, profesional, cliente);
        }
    }

