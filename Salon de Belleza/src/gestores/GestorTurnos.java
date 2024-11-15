package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoManicura;
import excepciones.EntradaInvalidaException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class GestorTurnos {

    private GestorAlmacen<Turno> listaTurnos = new GestorAlmacen<>();
    // private GestorPersona clientes = new GestorPersona();
    private Scanner scanner = new Scanner(System.in);

    public void agregarTurno() {

        boolean turnoAgregado = false;

        while (!turnoAgregado) {
            LocalDate fecha = pedirFecha();
            LocalTime horario = pedirHorario();
            Servicio servicio = new Manicura(3, LocalTime.of(1, 30),true, TipoManicura.SEMIPERMANENTE);
            //    pedirServicio();
          //  Profesional profesional = pedirProfesional();
            Cliente cliente = new Cliente("S", "S", "3", "C", "3232");//Cliente cliente = pedirCliente();

            // Verificar si ya existe un turno para ese profesional en la misma fecha y hora
            boolean error = false;

            for (Turno turno : listaTurnos.getAlmacen()) {
                if (turno.getFecha().equals(fecha) &&
                        turno.getHorario().equals(horario)) {
                    System.out.println("Error: El profesional ya tiene un turno en esta fecha y hora.");
                    error = true;
                    break;
                }
            }
            // Si no hay conflicto, se agrega el turno y se sale del bucle
            if (!error) {
              //  Turno turno = new Turno(fecha, horario, servicio, profesional, cliente);
              //  listaTurnos.agregar(turno);
                System.out.println("Turno agregado con éxito.");
                turnoAgregado = true;

            } else {
                System.out.println("¿Desea intentar de nuevo?");
                System.out.println("1. Sí");
                System.out.println("2. No");
                int respuesta = scanner.nextInt();

                switch (respuesta) {
                    case 1:
                        System.out.println("Intentando con un nuevo horario...");
                        break;
                    case 2:
                        System.out.println("Operación cancelada.");
                        return; // Salir y no volver a intentar
                    default:
                        System.out.println("Respuesta no válida. Por favor, ingrese '1' para sí o '2' para no.");
                        break;
                }
            }
        }
    }


    public boolean eliminarTurno(LocalDate fecha, LocalTime horario, Profesional profesional) {
        Turno turno = buscarTurno(fecha, horario, profesional);
        if (turno != null) {
            listaTurnos.agregar(turno);
            System.out.println("Turno eliminado con éxito.");
            return true;
        } else {
            System.out.println("Error: No se encontró el turno.");
            return false;
        }
    }

    public Turno buscarTurno(LocalDate fecha, LocalTime horario, Profesional profesional) {
        for (Turno turno : listaTurnos.getAlmacen()) {
            if (turno.getFecha().equals(fecha) &&
                    turno.getHorario().equals(horario) &&
                    turno.getProfesional().equals(profesional)) {
                return turno;
            }
        }
        System.out.println("Turno no encontrado.");
        return null;
    }

    public void modificarTurno(Turno turno) {
        int opcion;
        boolean continuar = true;

        while (continuar) {
            System.out.println("¿Qué desea modificar?");
            System.out.println("1. Fecha");
            System.out.println("2. Horario");
            System.out.println("3. Servicio");
            System.out.println("4. Profesional");
            System.out.println("5. Cliente");
            System.out.println("6. Salir");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    turno.setFecha(pedirFecha());
                    break;
                case 2:
                    turno.setHorario(pedirHorario());
                    break;
                case 3:
                    //aca le pasas los servicios
                    //turno.setServicio(pedirServicio(serviciosDisponibles));
                    break;
                case 4:
                    ///turno.setProfesional(pedirProfesional());
                    break;
                case 5:
                    // turno.setCliente(pedirCliente());
                    break;
                case 6:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Métodos para pedir cada atributo con validaciones
/*
    private LocalDate pedirFecha() {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
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
*/
    public LocalTime pedirHorario() {

        LocalTime horario = null;
        boolean valido = false;

        while (!valido) {
            System.out.println("Ingrese la hora del turno en este formato (HH:MM):");
            try {
                horario = LocalTime.parse(scanner.nextLine());

                // Verificar que el horario esté en una hora exacta (minutos = 00)
                if (horario.getMinute() != 0) {
                    System.out.println("El turno debe ser en una hora exacta (por ejemplo, 10:00, 14:00). Intente de nuevo.");
                    continue;  // Volver a pedir la hora
                }

                // Verificar que el horario esté entre las 09:00 y las 18:00 inclusive
                LocalTime horaApertura = LocalTime.of(9, 0);
                LocalTime horaCierre = LocalTime.of(18, 0);
                if (horario.isBefore(horaApertura) || horario.isAfter(horaCierre)) {
                    System.out.println("El turno debe estar entre las 09:00 y las 18:00 inclusive. Intente de nuevo.");
                    continue;  // Volver a pedir la hora
                }
                // Si pasa ambas validaciones, salimos del bucle
                valido = true;

            } catch (EntradaInvalidaException e) {
                System.out.println("El formato de la hora no es válido. Asegúrese de usar el formato HH:MM.");
            }
        }
        return horario;
    }
}
/*
    private Servicio pedirServicio( GestorServicio servicios){
        boolean valido=false;
        int aux=0;
        try{
            do{
                System.out.println("Ingrese el tipo de servicio:");
                System.out.println("1. Manicura");
                System.out.println("2. Pestanias");
                System.out.println("3. Depilacion");
                aux= scanner.nextInt();
                if(aux!=1 && aux!=2 && aux!=3){
                    System.out.println("No haz elegido una opcion valida!! Vuelve a ingresar un servicio que si exista por favor.");
                }
            }while(aux!=1 && aux!=2 && aux!=3);
  //no se por que otr atributo buscar.. precio? duracion?
        }catch(ServicioNoExistenteException e){
            System.out.println("No existe el turno que buscas.");
        }
    }
/*
    private Persona pedirProfesional(GestorPersona profesionales) {
        boolean existe = false;
        Persona aux = null;
        do {
            System.out.println("Ingresa el nombre del profesional");
            String n = scanner.nextLine();
            System.out.println("ingresa el apellido del profesional");
            String a = scanner.nextLine();
            System.out.println("ingresa el dni del profesional");
            String d = scanner.nextLine();

            for (Persona e : profesionales.getAlmacenPersonas()) { //esto me devuelve la lista bien?
                if (e.getNombre().equals(n) && e.getApellido().equals(a) && e.getDni().equals(d)) {
                    existe = true;
                    aux = e;
                    break;
                }
            }
            if (aux == null) {
                System.out.println("No existe este profesional, volvamos a buscarlo.");
            }
        } while (!existe);

        return aux;
    }

//aca los clientes se deben leer de un archivo? creo yo,
    private Persona pedirCliente(GestorPersona clientes){
        boolean existe=false;
        Persona aux=null;
        do{
            System.out.println("Ingresa el nombre del cliente");
            String n=scanner.nextLine();
            System.out.println("Ingresa el apellido del cliente");
            String a=scanner.nextLine();
            System.out.println("Ingresa el dni del cliente");
            String d=scanner.nextLine();

            for(Persona e: clientes.getAlmacenPersonas()) {
                if (e.getNombre().equals(n) && e.getApellido().equals(a) && e.getDni().equals(d)) {
                    existe = true;
                    aux = e;
                    break;
                }
            }
            if(aux==null){
                System.out.println("No existe este cliente, volvamos a buscarlo.");
            }
        }while(!existe);

        return aux;
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

    private Profesional pedirProfesional(Servicio servicio) {
        List<Profesional> profesionales = LeerArchivo("profesionales.json");

        if (profesionales == null || profesionales.isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        int pro = -1;
        do {
            System.out.println("Profesionales disponibles:");

            for (int i = 0; i < profesionales.size(); i++) {
                System.out.println((i + 1) + ") " + profesionales.get(i).getNombre()+" "+profesionales.get(i).getApellido());
            }

            System.out.println("Elija por favor el profesional (número):");
            pro = scanner.nextInt();

            if (pro < 1 || pro > profesionales.size()) {
                System.out.println("Selección inválida. Inténtelo de nuevo.");
            }

        } while (pro < 0 || pro > profesionales.size());
        return profesionales.get(pro - 1);
    }

}

public boolean pedirDisenio() {
        int opcion = 0;
        boolean disenio = false;

        do {
            try {
                System.out.println("Desea agregar un diseño al servicio? El valor es .. " );
                System.out.println("1. Si");
                System.out.println("2. No");
                opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion != 1 && opcion != 2) {
                    System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                }

            } catch (InputMismatchException a) {
                System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            disenio = true;
        } else {
            disenio = false;
        }
        return disenio;
    }
}

*/