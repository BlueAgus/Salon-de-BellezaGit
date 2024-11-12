import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;
import gestores.PrototipoGestorTurnos;
import model.Cliente;
import model.Manicura;
import model.Profesional;
import model.Servicio;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
       /* System.out.println("HOLA HICE UN CAMBIO ");
        System.out.println("Holi soy agus jeje");*/

        PrototipoGestorTurnos turnos = new PrototipoGestorTurnos();
        Manicura manicura= new Manicura(200, 1, TipoManicura.ESCULPIDAS, true);
        Profesional profesional= new Profesional("Daniela", "Vega", "45131280", "F", 223456346);
        Cliente cliente= new Cliente("Martina", "Rios", "44365444", "F", 223654789);

        turnos.agregarTurno(manicura, profesional, cliente);
        }
    }
