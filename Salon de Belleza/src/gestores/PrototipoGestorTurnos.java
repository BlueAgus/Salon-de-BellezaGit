package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDeProfesional;
import enumeraciones.TipoServicio;
import excepciones.DNInoEncontradoException;
import model.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PrototipoGestorTurnos {

    private MapaGenerico<LocalDate, List<Turno>> listaTurnos;
    private static Scanner scanner = new Scanner(System.in);

    //Constructor
    public PrototipoGestorTurnos() {
        this.listaTurnos = new MapaGenerico<>();
    }

    public List<Turno> obtenerTurnosReservadosXfecha(LocalDate fecha) {
        return listaTurnos.obtener(fecha);
    }

    public void agregarTurno()
    {
        String dniCliente="";//METODO pedir dni cliente
        String codServicio= "";//METODO pedir cod servicio
        Turno turno= elegirFechaYhorario(codServicio);
        String dniProfesional="";//METODO pedir dni profesional

        turno.setDni_cliente(dniCliente);
        turno.setCodigo_servicio(codServicio);
        turno.setDni_profesional(dniProfesional);


        System.out.println(turno);
/*
        Cliente cliente1;
        while (true)
        {
            try {
                cliente1= (Cliente) clientes.buscarPersona("55555");
                break;
            } catch (DNInoEncontradoException e) {
                System.out.println(e.getMessage());
            }
        }
*/

    }

    public Turno elegirFechaYhorario(String cod_servicio) {

        LocalDate fecha=pedirFecha();


        ///guarda los horarios disponibles y los muestra
        List<LocalTime> horariosDisponibles = mostrarTurnosDisponiblesXfecha(fecha, cod_servicio);

        int indiceHorario = -1;
        ///seleccion de horario
        while (true) {
            try {

                if (horariosDisponibles.isEmpty()) {
                    System.out.println("No hay turnos disponibles en la fecha seleccionada");
                    ///NO HAY HORARIOS DISPONIBLES
                } else {
                    System.out.println("OPCION:");
                    indiceHorario = scanner.nextInt();
                    if (indiceHorario < 0 || indiceHorario >= horariosDisponibles.size()) {
                        System.out.println("Opcion no valida");
                    } else {
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();///limpia buffer
            }
        }

        Turno turno = new Turno(fecha, horariosDisponibles.get(indiceHorario));
        ///System.out.println(turno);
        return turno;
    }


    private LocalDate pedirFecha() {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            /// agregar filtros
            System.out.println("Ingrese la fecha del turno (YYYY-MM-DD): ");
            try {
                fecha = LocalDate.parse(scanner.nextLine());
                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("Error: La fecha debe ser en el futuro.");
                } else {
                    valido = true;
                }
            } catch (Exception e) {
                System.out.println("Formato de fecha inválido.");
            }
        }
        return fecha;
    }


    ///el metodo retorna una lista con los horarios disponibles
    public List<LocalTime> mostrarTurnosDisponiblesXfecha(LocalDate fecha, String cod_servicio) {
        List<Turno> turnosReservados = obtenerTurnosReservadosXfecha(fecha);///retorna el valor que es una lista de turnos, con la clave que es la fecha

        List<LocalTime> horariosDisponibles = new ArrayList<>();///en esta lista guardamos horarios disponibles

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(18, 0);    // 6 p.m.

        ////agregar metodo que busque el servicio por codigo
        int i = 0; ///para el indice de horarios disponibles
        long hora = (long) servicio.getDuracion().getHour();
        long minutos = (long) servicio.getDuracion().getMinute();

        System.out.println("Turnos disponibles del dia: " + fecha);
        while (horaInicio.isBefore(horaFin)) {///recorre todos los horarios

            if (!siEstaHorario(turnosReservados, horaInicio,cod_servicio)) {
                //si te retorna true es porque ese horario ya esta reservado, si da false lo muestra como horario disponible
                System.out.println(i + "- " + horaInicio);
                i++;
                horariosDisponibles.add(horaInicio);
            }
            // Avanzamos la duracion del sevicio para el siguiente turno
            horaInicio = horaInicio.plusHours(hora).plusMinutes(minutos);
        }
        if (i == 0) {
            System.out.println("No hay turnos disponibles");
        }
        return horariosDisponibles;
    }

    public boolean siEstaHorario(List<Turno> turnos, LocalTime horario, String cod_servicio) {
        if (turnos == null || turnos.isEmpty()) {
            return false;
        }

        for (Turno t : turnos) {
            if (t.getHorario().equals(horario)&& t.getCodigo_servicio().equals(cod_servicio)) {
                return true;
            }
        }
        return false;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /////////////////////////////////////////////MANEJO DE PROFESIONALES!!!!////////////////////////////////////////


    public List<Profesional> LeerArchivo(String nombreArchivo) {
        try {
            FileReader fileReader = new FileReader(nombreArchivo);
            Gson gson = new Gson();
            Type tipoListaProfesionales = new TypeToken<List<Profesional>>() {
            }.getType();
            List<Profesional> profesionales = gson.fromJson(fileReader, tipoListaProfesionales);
            fileReader.close();
            return profesionales;
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String pedirPprofesional()
    {


    }

    public List<Profesional> pedirProfesionalXservicio(TipoDeProfesional tipo) {
        List<Profesional> profesionales = LeerArchivo("profesionales.json");

        if (profesionales == null || profesionales.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        int opc = -1;
        List<Profesional> disponibles= new ArrayList<>();
        do{


            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < profesionales.size(); i++) {
                if(profesionales.get(i).verificarProfesion(tipo))
                {
                    System.out.println(i + "- " + profesionales.get(i).getNombre()+" "+profesionales.get(i).getApellido());
                    disponibles.add(profesionales.get(i));
                }
            }

            System.out.println("Elija por favor el profesional (número):");
            opc = scanner.nextInt();
            scanner.nextLine();

            if (opc < 1 || opc > profesionales.size()) {
                System.out.println("Selección inválida. Inténtelo de nuevo.");
            }

        } while (opc < 0 || opc > profesionales.size());
        return disponibles;
    }



}
