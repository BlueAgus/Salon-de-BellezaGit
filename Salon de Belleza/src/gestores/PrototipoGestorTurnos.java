package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Profesional;
import model.Servicio;
import model.Turno;

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

    public PrototipoGestorTurnos() {
        this.listaTurnos = new MapaGenerico<>();
    }

    public List<Turno> obtenerTurnosDisponibles(LocalDate fecha) {
        return listaTurnos.obtener(fecha);
    }

    public void agregarTurno(Servicio servicio, Profesional profesional, Cliente cliente)
    {
        Turno turno= elegirFechaYhorario();

        turno.setServicio(servicio);
        turno.setProfesional(profesional);
        turno.setCliente(cliente);

        System.out.println(turno);

    }


    public Turno elegirFechaYhorario() {

        List<LocalDate> fechas = fechasAunaSemana();

        System.out.println("ELIJA UNA FECHA PARA VER LOS TURNOS DISPONIBLES: ");
        for (int i = 0; i < fechas.size(); i++) {//recorre la lista con las fechas de aca a una semana
            if (listaTurnos.obtener(fechas.get(i)) == null || listaTurnos.obtener(fechas.get(i)).size() < 11) {   ///si la fecha no existe en el mapa generico(es decir no hay turnos) o si tiene menos de 11 turnos lo printea
                ///es decir, si tiene horarios disponibles lo muestra
                System.out.println(i + "- " + fechas.get(i));
            }
        }


        int indiceDia = -1;

        ///seleccion de fecha
        while (true) {
            try {
                System.out.println("OPCION: ");
                indiceDia = scanner.nextInt();
                if (indiceDia < 0 || indiceDia >= fechas.size()) {
                    System.out.println("Opción no válida.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {///si ingresa
                System.out.println("Entrada no válida. Por favor ingrese un número.");
                scanner.nextLine();///limpia buffer
            }
        }

        ///guarda los horarios disponibles y los muestra
        List<LocalTime> horariosDisponibles = mostrarTurnosDisponiblesXfecha(fechas.get(indiceDia));

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


        Turno turno = new Turno(fechas.get(indiceDia), horariosDisponibles.get(indiceHorario));
        ///System.out.println(turno);
        return turno;
    }

    public List<LocalDate> fechasAunaSemana() {
        List<LocalDate> fechas = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            fechas.add(LocalDate.now().plusDays(i));
        }
        return fechas;
    }


    ///el metodo retorna una lista con los horarios disponibles
    public List<LocalTime> mostrarTurnosDisponiblesXfecha(LocalDate fecha ) {
        List<Turno> turnosReservados = obtenerTurnosDisponibles(fecha);///retorna el valor que es una lista de turnos, con la clave que es la fecha

        List<LocalTime> horariosDisponibles = new ArrayList<>();///en esta lista guardamos horarios disponibles

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(20, 0);    // 6 p.m.

        int i = 0; ///para el indice de horarios disponibles

        System.out.println("Turnos disponibles del dia: " + fecha);
        while (horaInicio.isBefore(horaFin)) {///recorre todos los horarios


            if (!siEstaHorario(turnosReservados, horaInicio)) {
                //si te retorna true es porque ese horario ya esta reservado, si da false lo muestra como horario disponible
                System.out.println(i + "- " + horaInicio);
                //i++;
                horariosDisponibles.add(horaInicio);
            }
            // Avanzamos una hora para el siguiente turno
            horaInicio = horaInicio.plusHours(1);
        }

        if (i == 0) {
            System.out.println("No hay turnos disponibles");
        }

        return horariosDisponibles;
    }

    public boolean siEstaHorario(List<Turno> turnos, LocalTime horario) {
        if (turnos == null || turnos.isEmpty()) {
            return false;
        }

        for (Turno t : turnos) {
            if (t.getHorario().equals(horario)) {
                return true;
            }
        }
        return false;
    }


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

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

    private Profesional pedirProfesionalXservicio(Servicio servicio) {
        List<Profesional> profesionales = LeerArchivo("profesionales.json");

        if (profesionales == null || profesionales.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        int opc = -1;
        do{
        List<Profesional> disponibles= new ArrayList<>();

            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < profesionales.size(); i++) {
                if(profesionales.get(i).getProfesiones().contains(servicio))
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
        return profesionales.get(opc - 1);
    }


}
