package Menus;

import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import excepciones.FacturaNoExistenteException;
import gestores.GestorFactura;
import gestores.GestorPersona;
import gestores.GestorServicio;
import gestores.GestorTurno;
import model.*;

import javax.imageio.stream.FileCacheImageInputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

public class MenuAdministrador {

    Scanner scanner = new Scanner(System.in);

    public void mostrarMenu(GestorPersona clientes, GestorPersona profesionales, GestorPersona recepcionista, GestorPersona administrador, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Menu de usuarios");
            System.out.println("2.Menu de servicios");
            System.out.println("3.Menu de turnos");
            System.out.println("4.Menu de facturas");
            System.out.println("5. ");
            System.out.println("6. ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    menuUsuarios(clientes, profesionales, recepcionista, administrador, servicios);
                    break;
                case 2:
                    menuServicio(servicios, clientes, turnos);
                    break;
                case 3:
                    menuTurnos(turnos, clientes, profesionales, servicios);
                    break;
                case 4:
                    menuFacturas(facturas, clientes);
                    break;
                case 5:

                    break;
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

    }

    public void menuUsuarios(GestorPersona clientes, GestorPersona profesionales, GestorPersona recepcionista, GestorPersona administrador, GestorServicio gestorServicio) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            ///esta bien no tener un admnistrador??
            System.out.println("1.Recepcionista");
            System.out.println("2.Profesionales");
            System.out.println("3.Clientes");
            System.out.println("4.");
            System.out.println("5.");
            System.out.println("6. ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    subMenuRecepcionista(recepcionista, gestorServicio);
                    break;
                case 2:
                    subMenuProfesionales(profesionales, gestorServicio);
                    break;
                case 3:
                    subMenuClientes(clientes, gestorServicio);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void subMenuRecepcionista(GestorPersona recepcionistas, GestorServicio gestorServicio) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("RECEPCIONISTA");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni ");
            System.out.println("4.Modificar datos ");
            System.out.println("5. ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    recepcionistas.agregarPersona(3, gestorServicio);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del Recepcionista que desea eliminar?");

                    String dni = recepcionistas.pedirDNIsinVerificacion();
                    recepcionistas.eliminarPersona(dni);
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del Recepcionista que desea buscar?");

                    String dni1 = recepcionistas.pedirDNIsinVerificacion();

                    try {
                        Recepcionista recepcionista = (Recepcionista) recepcionistas.buscarPersona(dni1);
                        System.out.println(recepcionista);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("¿Cual es el DNI del Recepcionista al que le desea modificar los datos");

                    String dni2 = recepcionistas.pedirDNIsinVerificacion();
                    try {
                        Recepcionista recepcionista = (Recepcionista) recepcionistas.buscarPersona(dni2);
                        System.out.println(recepcionista);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 5:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void subMenuProfesionales(GestorPersona profesionales, GestorServicio servicios) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("PROFESIONALES");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Modificar datos");
            System.out.println("4.Buscar por DNI");
            ///LISTAR O MOSTRAR O VER
            System.out.println("5.Lista de todos los profesionales del salon ");
            ///o 3 listar, 1 listra manicura etc
            System.out.println("6.Lista de profesionales que realicen determinado servicio");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    profesionales.agregarPersona(2, servicios);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del profesional que desea eliminar?");

                    String dni = profesionales.pedirDNIsinVerificacion();
                    profesionales.eliminarPersona(dni);
                    break;

                case 3:
                    System.out.println("¿Cual es el DNI del profesional al que le desea modificar los datos");

                    String dni2 = profesionales.pedirDNIsinVerificacion();

                    try {
                        Profesional profesional = (Profesional) profesionales.buscarPersona(dni2);
                        profesionales.modificarProfesional(profesional, servicios);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }

                    break;
                case 4:

                    System.out.println("¿Cual es el dni del profesional que desea buscar?");

                    String dni1 = profesionales.pedirDNIsinVerificacion();

                    try {
                        Persona persona = profesionales.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 5:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void subMenuClientes(GestorPersona clientes, GestorServicio gestorServicio) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("CLIENTES");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni ");
            System.out.println("4.Modificar datos ");
            System.out.println("5.Mostrar todos lo clientes del salon");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    clientes.agregarPersona(1, gestorServicio);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del cliente que desea eliminar?");

                    String dni = clientes.pedirDNIsinVerificacion();
                    clientes.eliminarPersona(dni);
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del cliente que desea buscar?");

                    String dni1 = clientes.pedirDNIsinVerificacion();

                    try {
                        Persona persona = clientes.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("¿Cual es el DNI del cliente al que le desea modificar los datos");

                    String dni2 = clientes.pedirDNIsinVerificacion();
                    try {
                        Persona persona = clientes.buscarPersona(dni2);
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 5:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuServicio(GestorServicio servicios, GestorPersona cliente, GestorTurno turnos) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar ");
            System.out.println("3.Modificar ");
            System.out.println("4.Buscar servicio por tipo ");
            System.out.println("5.Mostrar todos los servicios del salon ");
            System.out.println("6.Reportar falla de un servicio");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    servicios.agregarServicio();
                    break;
                case 2:
                    servicios.eliminarServicio();
                    break;
                case 3:
                    servicios.modificarServicio();
                    break;
                case 4:
                    servicios.mostrarServicioXtipo();
                    break;
                case 5:
                    servicios.mostrarServicios();
                    break;
                case 6:
                    servicios.reportarFalla(cliente, turnos);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuTurnos(GestorTurno turnos, GestorPersona clientes, GestorPersona profesionales, GestorServicio servicios) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("TURNOS");
            System.out.println("1.Agregar ");
            System.out.println("2.Eliminar ");
            System.out.println("3.Modificar datos ");
            System.out.println("4.Buscar un turno");
            System.out.println("5.Listar turnos proximos ");
            System.out.println("6.Listar historial de turnos");
            System.out.println("7.Turnos segun profesional");
            System.out.println("8.Turnos segun cliente");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    if (turnos.agregarTurno(clientes, profesionales, servicios)) {
                        System.out.println("Turno agregado exitosamente!");
                    } else {
                        System.out.println("Error al agregar turno");
                    }
                    break;
                case 2:
                    turnos.eliminarTurno(clientes);
                    break;
                case 3:
                    turnos.modificarTurno(servicios, profesionales, clientes);
                    break;
                case 4:
                    Turno turno = turnos.buscarTurnoXclienteFechaHorario(clientes);
                    if (turno == null) {
                        System.out.println("Turno buscado no encontrado");
                    } else {
                        System.out.println("Turno buscado:");
                        System.out.println(turno);
                    }
                    break;
                case 5:
                    List<Turno> turnosProximos = turnos.mostrarTurnosVigentes();
                    int contador = 0;

                    for (Turno turno1 : turnosProximos) {
                        System.out.println(contador + " " + turno1);
                        contador++;
                    }
                    break;
                case 6:
                    turnos.mostrarHistorialTurnos();
                    break;
                case 7:
                    turnosXprofesional(turnos, profesionales);
                    break;
                case 8:
                    turnosXcliente(turnos, clientes);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void turnosXprofesional(GestorTurno turnos, GestorPersona profesionales) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("6.Turnos proximos de un profesional");
            System.out.println("7.Historial de turnos de un profesional");

            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    String dni = profesionales.pedirDNIsinVerificacion();
                    List<Turno> turnosProximos = turnos.buscarTurnosXdniProfesionalVigentes(dni);

                    int contador = 0;
                    if (turnosProximos.isEmpty()) {
                        System.out.println("El profesional no tiene turnos agendados proximamente");
                    } else {
                        for (Turno turno : turnosProximos) {
                            System.out.println(contador + " " + turno);
                            contador++;
                        }
                    }
                    break;
                case 2:
                    String dni1 = profesionales.pedirDNIsinVerificacion();
                    List<Turno> historialTurnos = turnos.historialTurnosXprofesional(dni1);

                    int contador1 = 0;
                    if (historialTurnos.isEmpty()) {
                        System.out.println("El profesional no tiene un historial de turnos");
                    } else {
                        for (Turno turno : historialTurnos) {
                            System.out.println(contador1 + " " + turno);
                            contador1++;
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        while (opcion != 0);
    }

    public void turnosXcliente(GestorTurno turnos, GestorPersona clientes) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Turnos proximos de un cliente especifico");
            System.out.println("2.Historial de turnos de un cliente");


            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    String dni = clientes.pedirDNIsinVerificacion();
                    List<Turno> turnosVigentes = turnos.buscarTurnosXdniClienteVigentes(dni);
                    int contador1 = 0;
                    if (turnosVigentes.isEmpty()) {
                        System.out.println("El cliente no tiene agendado turnos proximamente");
                    } else {
                        for (Turno turno : turnosVigentes) {
                            System.out.println(contador1 + " " + turno);
                            contador1++;
                        }
                    }
                    break;
                case 2:
                    String dni1 = clientes.pedirDNIsinVerificacion();
                    List<Turno> historialTurnos = turnos.historialTurnosXcliente(dni1);
                    int contador = 0;
                    if (historialTurnos.isEmpty()) {
                        System.out.println("El cliente no tiene un historial de turnos");
                    } else {
                        for (Turno turno : historialTurnos) {
                            System.out.println(contador + " " + turno);
                            contador++;
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuFacturas(GestorFactura facturas, GestorPersona clientes) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Agregar ");
            System.out.println("2.Eliminar ");
            System.out.println("3.Modificar ");
            System.out.println("4.Buscar");
            System.out.println("5.Ver historial de facturas");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    facturas.crearFactura();
                    break;
                case 2:
                    System.out.println("Para eliminar una factura, se solicita el DNI del cliente");
                    while (true) {
                        String dni = clientes.pedirDNIsinVerificacion();
                        try {
                            facturas.historialFacturasPorCliente(dni);

                            while (true) {
                                System.out.println("Ingrese el codigo de la factura que quiere eliminar:");
                                String codigo = scanner.nextLine();

                                try {
                                    facturas.eliminarFactura(codigo);
                                    break;
                                } catch (FacturaNoExistenteException e) {
                                    System.out.println(e.getMessage());
                                    System.out.println("¿Desea intentar de nuevo? S/N");
                                    String respuesta = scanner.nextLine();
                                    if (!respuesta.equalsIgnoreCase("S")) {
                                        System.out.println("Operación cancelada.");
                                        return;
                                    }
                                }
                            }
                            break;
                        } catch (DNInoEncontradoException e) {
                            System.out.println(e.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Para modificar una factura, se solicita el DNI del cliente al que corresponda");
                    String dni1 = clientes.pedirDNIsinVerificacion();
                    try {
                        facturas.historialFacturasPorCliente(dni1);
                        facturas.modificarFactura();
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }

                    break;
                case 4:
                    buscarFacturas(facturas, clientes);
                    break;
                case 5:
                    System.out.println(facturas.getHistorial().getAlmacen());
                    break;
                case 6:
                    String dni = clientes.pedirDNIsinVerificacion();
                    try {
                        facturas.historialFacturasPorCliente(dni);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void buscarFacturas(GestorFactura facturas, GestorPersona clientes) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Buscar por codigo ");
            System.out.println("2.Buscar por fecha ");
            System.out.println("3.Buscar por cliente ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    while (true) {
                        System.out.println("Ingrese el codigo de la factura:");
                        String codigo = scanner.nextLine();
                        try {
                            Factura factura = facturas.buscarFacturaPorCodigo(codigo);
                            System.out.println(factura);
                            break;
                        } catch (CodigoNoEncontradoException a) {
                            System.out.println(a.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                case 2:
                    while (true) {
                        System.out.println("Ingrese la fecha (YYYY-MM-DD):");
                        String fecha = scanner.nextLine();

                        List<Factura> facturasXfecha = facturas.verHistorialPorFecha(fecha);

                        if (facturasXfecha == null || facturasXfecha.isEmpty()) {
                            System.out.println("Intente nuevamente.");
                            continue;
                        }
                        System.out.println(facturasXfecha);
                        break;
                    }
                    break;
                case 3:
                    while (true) {
                        String dni = clientes.pedirDNIsinVerificacion();
                        try {
                            facturas.historialFacturasPorCliente(dni);
                            break;
                        } catch (DNInoEncontradoException e) {
                            System.out.println(e.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuPrecios() {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1. ");
            System.out.println("2. ");

            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:

                    break;
                case 2:

                    break;
               
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

    }
}
