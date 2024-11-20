package gestores;

import com.google.gson.Gson;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GestorTurno {

    private MapaGenerico<LocalDate, List<Turno>> listaTurnos;
    private static Scanner scanner = new Scanner(System.in);


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public GestorTurno() {
        this.listaTurnos = new MapaGenerico<>();
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public boolean agregarTurno(GestorPersona gestorPersona, GestorServicio gestorServicio) {
        String dniCliente = pedirDNIcliente(gestorPersona);//METODO pedir dni cliente

        if (dniCliente == null) {
            return false;
        }

        String codServicio = pedirCodServicio(gestorServicio);//METODO pedir cod servicio

        if (codServicio == null) {
            return false;
        }

        Turno turno = elegirFechaYhorario(codServicio, gestorServicio);

        if (turno == null) {
            return false;
        }

        while (verificarClienteXHorario(dniCliente, turno.getHorario(), turno.getFecha())) {
            System.out.println("El cliente ya tiene reservado un turno en la misma fecha y horario!!");
            turno = elegirFechaYhorario(codServicio,gestorServicio);
        }

        String dniProfesional = pedirDNIprofesionalXservicio(codServicio, turno.getHorario(), turno.getFecha(), gestorPersona);//METODO pedir dni profesional

        if (dniProfesional == null) {
            return false;
        }

        turno.setDni_cliente(dniCliente);
        turno.setCodigo_servicio(codServicio);
        turno.setDni_profesional(dniProfesional);


        System.out.println(turno);

        if (listaTurnos.contiene(turno.getFecha())) {
            listaTurnos.getMapa().get(turno.getFecha()).add(turno);
        } else {
            List<Turno> turnos = new ArrayList<>();
            turnos.add(turno);

            listaTurnos.agregar(turno.getFecha(), turnos);
        }
        return true;
    }

    ///mirar
    public boolean eliminarTurno(String codTurno) {
        for (LocalDate fecha : listaTurnos.getMapa().keySet()) {
            for (List<Turno> e : listaTurnos.getMapa().values()) {
                for (Turno t : e) {
                    if (t.getCodigo_servicio().equals(codTurno)) {
                        return e.remove(t);
                    }
                }
            }
        }
        return false;
    }

    ///mirar
    public Turno buscarTurnoPorCodigo(String codTurno) throws CodigoNoEncontradoException {
        for (LocalDate fecha : listaTurnos.getMapa().keySet()) {
            for (List<Turno> e : listaTurnos.getMapa().values()) {
                for (Turno t : e) {
                    if (t.getCodigo_servicio().equals(codTurno)) {
                        return t;
                    }
                }
            }
        }
        throw new CodigoNoEncontradoException("El código del turno no existe: " + codTurno);
    }

    public boolean modificarTurno(String codTurno) {

      try{
          Turno t = buscarTurnoPorCodigo(codTurno);
          System.out.println("DATOS ACTUALES DEL TURNO: " + t.toString());
          System.out.println("| Ingrese la opcion que desea modificar:");
          System.out.println("1- Fecha");
          System.out.println("2- Horario");
          System.out.println("3- Profesional");
          System.out.println("4- Cliente");

    /*
        private LocalDate fecha;         // Fecha del turno
        private LocalTime horario;       // Hora del turno
        private String codigo_servicio;       // El servicio a realizar
        private String dni_profesional; // Profesional que atenderá el servicio
        private String dni_cliente;
    */

      } catch (CodigoNoEncontradoException e) {
         e.getMessage();
      }
      return false;
    }

    /////////////////////////////////////////////MANEJO DE SERVICIOS!!!!////////////////////////////////////////
    public String pedirCodServicio(GestorServicio gestorServicio) {
        int opc = 0;
        while (true) {
            for (int i = 0; i < gestorServicio.getAlmacenServicios().getAlmacen().size(); i++) {
                System.out.println(i + "- " + gestorServicio.getAlmacenServicios().getAlmacen().get(i).toString());
            }

            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }

            try {
                opc = Integer.parseInt(opcElegida); // Convierte el String a entero
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }

            if (opc < 0 || opc >= gestorServicio.getAlmacenServicios().getAlmacen().size()) {
                System.out.println("Opcion no valida");
            } else {
                return gestorServicio.getAlmacenServicios().getAlmacen().get(opc).getCodigo_servicio();
            }
        }
    }

    /////////////////////////////////////////////MANEJO DE CLIENTES!!!!////////////////////////////////////////

   public String pedirDNIcliente(GestorPersona gestorCliente) {

        String dniCliente;
        while (true) {
            ///para ponerle que presione ESC hay que usar libreras
            System.out.println("Ingrese el DNI del cliente (o escriba 'salir' para cancelar):");
            dniCliente = scanner.nextLine();

            if (dniCliente.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }

            try {
                gestorCliente.verificarSiExiste(dniCliente);
                return dniCliente;

            } catch (DNInoEncontradoException e) {
                System.out.println(e.getMessage());
            }
        }

        ///tendriamos que verificar que el cliente no tenga más turnos en el mismo horario y dia?? o podriamos decir que puede reservar turnos para otra persona
    }

    public boolean verificarClienteXHorario(String dni, LocalTime horario, LocalDate fecha) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        for (Turno t : turnos) {
            if (t.getDni_cliente().equals(dni) && t.getHorario().equals(horario)) {
                return false; ///retorna falso si el cliente tiene un turno con el mismo horario solicitado
            }
        }
        return true;//si no hay ningun turno con el mismo horario y cliente retorna true

    }

    /////////////////////////////////////////////MANEJO DE FECHAS!!!!////////////////////////////////////////

    ///retorna un turno con la fecha y el horario elegido
    public Turno elegirFechaYhorario(String cod_servicio, GestorServicio gestorServicio) {

        LocalDate fecha = pedirFecha();
        if (fecha == null) {
            return null;
        }

        ///guarda los horarios disponibles y los muestra
        List<LocalTime> horariosDisponibles = mostrarTurnosDisponiblesXfecha(fecha, cod_servicio, gestorServicio );

        int indiceHorario;
        ///seleccion de horario
        while (true) {
            try {

                if (horariosDisponibles.isEmpty()) {
                    System.out.println("No hay turnos disponibles en la fecha seleccionada");
                    ///NO HAY HORARIOS DISPONIBLES
                } else {

                    System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
                    String opcElegida = scanner.nextLine();

                    if (opcElegida.equalsIgnoreCase("salir")) {
                        System.out.println("Operación cancelada por el usuario.");
                        return null;
                    }

                    try {
                        indiceHorario = Integer.parseInt(opcElegida); // Convierte el String a entero
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Debe ingresar un número.");
                        return null;
                    }

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

    ///retorna la fecha ingresada por el usuario
    public LocalDate pedirFecha() {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            /// agregar filtros
            System.out.println("Ingrese la fecha del turno (YYYY-MM-DD): (o escriba 'salir' para cancelar)");

            ///lo guarda en un string para verificar que no haya escrito salir
            String fechaIngresada = scanner.nextLine();

            if (fechaIngresada.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }

            try {

                fecha = LocalDate.parse(fechaIngresada);

                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("Error: La fecha debe ser en el futuro.");
                } else {
                    valido = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Por favor, use el formato YYYY-MM-DD");
            }
        }
        return fecha;
    }

    ///retorna una lista con los horarios disponibles x fecha especifica y servicio
    public List<LocalTime> mostrarTurnosDisponiblesXfecha(LocalDate fecha, String cod_servicio,GestorServicio gestorServicio) {
        List<Turno> turnosReservados = obtenerTurnosReservadosXfecha(fecha);///retorna la lista de turnos reservados para una fecha

        List<LocalTime> horariosDisponibles = new ArrayList<>();///en esta lista guardamos horarios disponibles

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(18, 0);    // 6 p.m.

        ////agregar metodo que busque el servicio por codigo
        int i = 0; ///para el indice de horarios disponibles
        long hora = (long) servicio.getDuracion().getHour();
        long minutos = (long) servicio.getDuracion().getMinute();

        System.out.println("Turnos disponibles del dia: " + fecha);
        while (horaInicio.isBefore(horaFin)) {///recorre todos los horarios

            if (!siEstaHorario(turnosReservados, horaInicio, cod_servicio)) {
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

    /// retorna lista de turnos RESERVADOS por fecha específica
    public List<Turno> obtenerTurnosReservadosXfecha(LocalDate fecha) {
        return listaTurnos.obtener(fecha);
    }

    ///retorna true si el horario está ocupado por un turno del mismo servicio
    public boolean siEstaHorario(List<Turno> turnos, LocalTime horario, String cod_servicio) {
        if (turnos == null || turnos.isEmpty()) {
            return false;
        }

        for (Turno t : turnos) {
            if (t.getHorario().equals(horario) && t.getCodigo_servicio().equals(cod_servicio)) {
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



   /* public List<Profesional> pedirDNIprofesionalXservicio(TipoDeProfesional tipo) {
        List<Profesional> profesionales = LeerArchivoProfesionales("profesionales.json");///no es mejor que el metodo ya tenga el nombre antes que pasarselo?

        if (profesionales == null || profesionales.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        int opc = -1;
        List<Profesional> disponibles = new ArrayList<>();
        do {
            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < profesionales.size(); i++) {
                if (profesionales.get(i).verificarProfesion(tipo)) {
                    System.out.println(i + "- " + profesionales.get(i).getNombre() + " " + profesionales.get(i).getApellido());
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
    }*/
///a este metodo le agregue las verificaciones de servicio, fecha y horario. Tambien retorne el dni en vez de retornar la lista completa

///Meli modifique
    ///devuelve el DNI del profesional
///filtra por servicio, por horario y fecha
    public String pedirDNIprofesionalXservicio(String codServicio, LocalTime horario, LocalDate fecha, GestorPersona gestorPersona) {

        List<Persona> personas = gestorPersona.LeerArchivo("profesionales.json");

        if (personas == null || personas.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

            List<Profesional> profesionales = personas.stream().filter(persona -> persona instanceof Profesional).map(persona -> (Profesional) persona).toList();


        int opc = 0;
        List<Profesional> disponibles = new ArrayList<>();

        while (true) {
            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < profesionales.size(); i++) {

                if (profesionales.get(i).verificarProfesion(codServicio) && verificarProfesionalXhorario(profesionales.get(i).getDni(), horario, fecha)) {
                    System.out.println(opc + "- " + profesionales.get(i).getNombre() + " " + profesionales.get(i).getApellido());
                    disponibles.add(profesionales.get(i));
                    opc++;
                }
            }
            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();


                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > profesionales.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
        return disponibles.get(opc).getDni();
    }


    public boolean verificarProfesionalXhorario(String dni, LocalTime horario, LocalDate fecha) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        for (Turno t : turnos) {
            if (t.getDni_profesional().equals(dni) && t.getHorario().equals(horario)) {
                return false; ///retorna falso si el profesional tiene un turno con el mismo horario solicitado
            }
        }
        return true;//si no hay ningun turno con el mismo horario y profesional retorna true

    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

   // aguss, lo hice para probar
    public List<Turno> obtenerTurnosPorCliente(String dni) {
        List<Turno> turnosProclien = (List<Turno>) listaTurnos.getMapa();
        return turnosProclien.stream()
                .filter(turno -> turno.getDni_cliente().equals(dni) &&
                        !turno.getFecha().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }


}