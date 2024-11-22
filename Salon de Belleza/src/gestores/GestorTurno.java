package gestores;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import model.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;


public class GestorTurno {


    private MapaGenerico<String, List<Turno>> listaTurnos;
    private static final String archivoTurnos = "turnos.json";
    Scanner scanner = new Scanner(System.in);


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public GestorTurno() {

        this.listaTurnos = cargarTurnosDesdeArchivo();

        if (this.listaTurnos == null) {
            this.listaTurnos = new MapaGenerico<>();
        }
    }

    ////////////////////////////////////////////////////////Json ////////////////////////////////////////////////////

    // Guardar turnos en archivo
    public void guardarTurnosEnArchivo() {
        GestorJson.guardarEnArchivo(archivoTurnos, listaTurnos);
    }

    // Cargar turnos desde archivo
    private MapaGenerico<String, List<Turno>> cargarTurnosDesdeArchivo() {
        return GestorJson.cargarDeArchivo(archivoTurnos, MapaGenerico.class);
    }




    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public boolean agregarTurno(GestorCliente gestorCliente, GestorProfesional gestorProfesional, GestorServicio gestorServicio) {

        try{
            String dniCliente = pedirDNIcliente(gestorCliente);//METODO pedir dni cliente

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
            turno = elegirFechaYhorario(codServicio, gestorServicio);
        }

        String dniProfesional = pedirDNIprofesionalXservicio(codServicio, turno.getHorario(), turno.getFecha(), gestorProfesional);//METODO pedir dni profesional

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

        }catch (DNInoEncontradoException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean eliminarTurno(GestorCliente gestorCliente) {

        String codTurno = buscarCodigoTurno(gestorCliente);
        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codTurno)) {
                    return e.remove(t);
                }
            }
        }
        return false;
    }

    public String buscarCodigoTurno(GestorCliente gestorCliente) {

        String dniCliente = gestorCliente.pedirDNIsinVerificacion();

        int i = 0;
        List<Turno> turnosDelCliente = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                if (t.getDni_cliente().equals(dniCliente)) {
                    System.out.println(i + "-" + t.toString());
                    turnosDelCliente.add(t);
                    i++;
                }
            }
        }
        int opc = 0;
        while (true) {

            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();


                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > turnosDelCliente.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
        return turnosDelCliente.get(opc).getCod_turno();
    }

    public void cancelarTurnosXdia(String fecha, GestorCliente clientes, String codServicio) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        System.out.println("Avisar a los siguientes clientes que su turno del dia " + fecha + "ha sido cancelado");
        for (Turno t : turnos) {
            if (t.getCodigo_servicio().equals(codServicio)) {
                Cliente cliente = null;
                try {
                    // cliente = (Cliente) gestorPersona.buscarPersona(t.getDni_cliente());
                    cliente = clientes.buscarPersona(t.getDni_cliente());
                } catch (DNInoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("- " + cliente.getNombre() + " TELEFONO: " + cliente.getTelefono());
            }
        }
        turnos.clear();
    }

    public Turno buscarTurnoXclienteFechaHorario(GestorCliente gestorCliente) {

        String codigoTurno = buscarCodigoTurno(gestorCliente);
        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codigoTurno)) {
                    return t;
                }
            }
        }
        return null;
    }

    public Turno buscarTurnoXcodigo(String codTurno) {

        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codTurno)) {
                    return t;
                }
            }
        }
        return null;
    }

    public boolean modificarTurno(GestorServicio gestorServicio, GestorProfesional gestorProfesional, GestorCliente gestorCliente) {
        Turno t;

        List<Turno> turnosVigentes = mostrarTurnosVigentes();

        int opc = 0;
        ///eleccion de turno
        while (true) {

            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return false;
            }

            try {
                opc = Integer.parseInt(opcElegida); // Convierte el String a entero
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }

            if (opc < 0 || opc >= turnosVigentes.size()) {
                System.out.println("Opcion no valida");
            } else {
                t = turnosVigentes.get(opc);
                break;
            }
        }

        ///modificacion de turno
        while (true) {
            System.out.println("DATOS ACTUALES DEL TURNO: " + t.toString());
            System.out.println("| Ingrese la opcion que desea modificar:");
            System.out.println("1- Fecha y horario");
            System.out.println("2- Profesional");
            System.out.println("3- Cliente");


            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return false;
            }

            try {
                opc = Integer.parseInt(opcElegida); // Convierte el String a entero
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }

            if (opc < 0 || opc > 3) {
                System.out.println("Opcion no valida");
            } else {
                break;
            }
        }

        switch (opc) {
            case 1:
                Turno aux = elegirFechaYhorario(t.getCodigo_servicio(), gestorServicio);
                t.setFecha(aux.getFecha());
                t.setHorario(aux.getHorario());
                break;

            case 2:
                String dniProfesional = pedirDNIprofesionalXservicio(t.getCodigo_servicio(), t.getHorario(), t.getFecha(), gestorProfesional);

                if (dniProfesional == null) {
                    break;
                }

                t.setDni_profesional(dniProfesional);
                break;
            case 3:
                try{
                    String dniCliente = pedirDNIcliente(gestorCliente);
                    if (dniCliente == null) {
                        break;
                    }
                    t.setDni_cliente(dniCliente);
                    break;

                }catch (DNInoEncontradoException e){
                    System.out.println(e.getMessage());
                    break;
                }

        }
        return true;
    }

    public List<Turno> mostrarTurnosVigentes() {
        int i = 0;
        List<Turno> turnosVigentes = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalDate fecha=Turno.convertirStringALocalDate(t.getFecha());
                if (fecha.isAfter(LocalDate.now()) || fecha.isEqual(LocalDate.now())) {
                    System.out.println(i + "-" + t.toString());
                    turnosVigentes.add(t);
                    i++;
                }
            }
        }
        return turnosVigentes;
    }

    public void mostrarHistorialTurnos() {
        int i = 0;
        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalDate fecha=Turno.convertirStringALocalDate(t.getFecha());
                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println(i + "-" + t.toString());
                    i++;
                }
            }
        }
    }

    ///turnos vigentes del cliente
    public List<Turno> buscarTurnosXdniClienteVigentes(String dniCliente) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalTime horario=Turno.convertirStringALocalTime(t.getHorario());
                if (t.getDni_cliente().equals(dniCliente) && (horario.isAfter(LocalTime.now()) || horario.equals(LocalTime.now()))) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///turnos vigentes del profesional
    public List<Turno> buscarTurnosXdniProfesionalVigentes(String dniProfesional) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalDate fecha=Turno.convertirStringALocalDate(t.getFecha());
                if (t.getDni_profesional().equals(dniProfesional) && (fecha.isAfter(LocalDate.now()) || fecha.isEqual(LocalDate.now()))) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///historial de turnos por cliente
    public List<Turno> historialTurnosXcliente(String dniCliente) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalDate fecha=Turno.convertirStringALocalDate(t.getFecha());
                if (t.getDni_cliente().equals(dniCliente) && fecha.isBefore(LocalDate.now())) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///historial de turnos por profesional
    public List<Turno> historialTurnosXprofesional(String dniProfesional) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                LocalDate fecha=Turno.convertirStringALocalDate(t.getFecha());
                if (t.getDni_profesional().equals(dniProfesional) && fecha.isBefore(LocalDate.now())) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    /////////////////////////////////////////////MANEJO DE SERVICIOS!!!!////////////////////////////////////////

    public String pedirCodServicio(GestorServicio gestorServicio) {
        int opc = 0;
        while (true) {
            for (int i = 0; i < gestorServicio.getServicios().getAlmacen().size(); i++) {
                System.out.println(i + "- " + gestorServicio.getServicios().getAlmacen().get(i).toString());
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

            if (opc < 0 || opc >= gestorServicio.getServicios().getAlmacen().size()) {
                System.out.println("Opcion no valida");
            } else {
                return gestorServicio.getServicios().getAlmacen().get(opc).getCodigo_servicio();
            }
        }
    }

    /////////////////////////////////////////////MANEJO DE CLIENTES!!!!////////////////////////////////////////

    public String pedirDNIcliente(GestorCliente gestorCliente) throws DNInoEncontradoException {
        String dniCliente;
        while (true) {

            System.out.println("Ingrese el DNI del cliente (o escriba 'salir' para cancelar):");
            dniCliente = scanner.nextLine();

            if (dniCliente.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }


            boolean esta = gestorCliente.verificarSiExisteCliente(dniCliente);

              if(!esta){
                  throw new DNInoEncontradoException("El DNI no fue encontrado..");
              }

                return dniCliente;

        }

        ///tendriamos que verificar que el cliente no tenga más turnos en el mismo horario y dia?? o podriamos decir que puede reservar turnos para otra persona
    }

    public boolean verificarClienteXHorario(String dni, String horario, String fecha) {
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

        String fecha =Turno.convertirLocalDateAString(pedirFecha());
        if (fecha == null) {
            return null;
        }
        ///guarda los horarios disponibles y los muestra
        List<LocalTime> horariosDisponibles = mostrarTurnosDisponiblesXfecha(fecha, cod_servicio, gestorServicio);

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

        String horario= Turno.convertirLocalTimeAString(horariosDisponibles.get(indiceHorario));
        Turno turno = new Turno(fecha,horario );
        ///System.out.println(turno);
        return turno;
    }

    ///retorna la fecha ingresada por el usuario
    public LocalDate pedirFecha() {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            /// agregar filtros
            System.out.println("Ingrese la fecha (YYYY-MM-DD): (o escriba 'salir' para cancelar)");

            ///lo guarda en un string para verificar que no haya escrito salir
            String fechaIngresada = scanner.nextLine();

            if (fechaIngresada.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }

            try {

                 fecha=Turno.convertirStringALocalDate(fechaIngresada);

                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("Error: La fecha debe ser en el futuro.");
                } else if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("Error: No se pueden seleccionar turnos en domingo.");
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
    public List<LocalTime> mostrarTurnosDisponiblesXfecha(String fecha, String cod_servicio, GestorServicio gestorServicio) {
        List<Turno> turnosReservados = obtenerTurnosReservadosXfecha(fecha);///retorna la lista de turnos reservados para una fecha

        List<LocalTime> horariosDisponibles = new ArrayList<>();///en esta lista guardamos horarios disponibles

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(18, 0);    // 6 p.m.

        ////agregar metodo que busque el servicio por codigo
        int i = 0; ///para el indice de horarios disponibles
        long hora = 0;
        try {
            hora = (long) gestorServicio.buscarServicioCodigo(cod_servicio).getDuracion().getHour();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
        long minutos = 0;
        try {
            minutos = (long) gestorServicio.buscarServicioCodigo(cod_servicio).getDuracion().getMinute();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }

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
    public List<Turno> obtenerTurnosReservadosXfecha(String fecha) {
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

/////////////////////////////////////////////MANEJO DE PROFESIONALES!!!!////////////////////////////////////////

    ///devuelve el DNI del profesional
///filtra por servicio, por horario y fecha
    public String pedirDNIprofesionalXservicio(String codServicio, String horario, String fecha, GestorProfesional gestorProfesional) {
        List<Profesional> profesionales = gestorProfesional.leerArchivoProfesionales();

        if (profesionales == null || profesionales.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

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

    public boolean verificarProfesionalXhorario(String dniProfesional, String horario, String fecha) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        for (Turno t : turnos) {
            if (t.getDni_profesional().equals(dniProfesional) && t.getHorario().equals(horario)) {
                return false; ///retorna falso si el profesional tiene un turno con el mismo horario solicitado
            }
        }
        return true;//si no hay ningun turno con el mismo horario y profesional retorna true
    }

    /////////////////////////////////////////////MANEJO DE ARCHIVO TURNOS!!!!////////////////////////////////////////

/*
    public List<TurnoArchivo> convertirMapa(HashMap<LocalDate, List<Turno>> mapa) {
        List<TurnoArchivo> turnosConvertidos = new ArrayList<>();

        // Recorrer todo el mapa
        for (List<Turno> turnos : mapa.values()) {
            // Recorrer cada Turno en la lista y convertirlo
            for (Turno turno : turnos) {
                TurnoArchivo turnoConvertido = convertirTurno(turno);
                turnosConvertidos.add(turnoConvertido);
            }
        }
        return turnosConvertidos;
    }*/

   /* public TurnoArchivo convertirTurno(Turno turnoOriginal) {
        // Formateadores para fecha y hora
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        // Convertir LocalDate y LocalTime a String
        String fecha = turnoOriginal.getFecha().format(formatoFecha);
        String horario = turnoOriginal.getHorario().format(formatoHora);

        // Crear y retornar el nuevo objeto TurnoArchivo
        return new TurnoArchivo(
                turnoOriginal.getCod_turno(),
                fecha,
                horario,
                turnoOriginal.getCodigo_servicio(),
                turnoOriginal.getDni_profesional(),
                turnoOriginal.getDni_cliente()
        );
    }*/
    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public MapaGenerico<String, List<Turno>> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(HashMap<String, List<Turno>> listaTurnos) {
        this.listaTurnos.setMapa(listaTurnos);
    }
}
