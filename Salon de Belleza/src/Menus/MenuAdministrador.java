package Menus;

import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import gestores.GestorFactura;
import gestores.GestorPersona;
import gestores.GestorServicio;
import gestores.GestorTurno;
import model.Persona;

import java.util.Scanner;

public class MenuAdministrador {

    private GestorPersona gestorPersona;
    private GestorServicio gestorServicio;
    private GestorTurno gestorTurno;
    private GestorFactura gestorFactura;

    public void mostrarMenu() {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Bienvenido al menu de Administrador");
            System.out.println("1.Menu de usuarios ");
            System.out.println("2.Menu de servicios");
            System.out.println("3.Menu de turnos ");
            System.out.println("4.Menu de facturas ");
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
                    menuUsuarios();
                    break;
                case 2:
                    menuServicio(gestorServicio);
                    break;
                case 3:
                    menuTurnos(gestorTurno);
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

    public void menuUsuarios() {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
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
                    subMenuRecepcionista(gestorPersona);
                    break;
                case 2:
                    subMenuProfesionales(gestorPersona);
                    break;
                case 3:
                    subMenuClientes(gestorPersona);
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


    public void subMenuRecepcionista(GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("RECEPCIONISTA");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni ");
            System.out.println("4.Modificar datos ");
            ///y si esta de vacaciones?
            System.out.println("5. ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    gestorPersona.agregarPersona(3, gestorServicio);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del Recepcionista que desea eliminar?");

                    String dni = gestorPersona.pedirDNIsinVerificacion();
                    gestorPersona.eliminarPersona(dni);
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del Recepcionista que desea buscar?");

                    String dni1 = gestorPersona.pedirDNIsinVerificacion();

                    try {
                        Persona persona = gestorPersona.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("¿Cual es el DNI del Recepcionista al que le desea modificar los datos");

                    String dni2 = gestorPersona.pedirDNIsinVerificacion();
                    try {
                        Persona persona = gestorPersona.buscarPersona(dni2);
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


    public void subMenuProfesionales(GestorPersona gestorPersona) {

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
                    gestorPersona.agregarPersona(2, gestorServicio);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del profesional que desea eliminar?");

                    String dni = gestorPersona.pedirDNIsinVerificacion();
                    gestorPersona.eliminarPersona(dni);
                    break;

                case 3:
                    System.out.println("¿Cual es el DNI del profesional al que le desea modificar los datos");

                    String dni2 = gestorPersona.pedirDNIsinVerificacion();
                    try {
                        Persona persona = gestorPersona.buscarPersona(dni2);
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 4:

                    System.out.println("¿Cual es el dni del profesional que desea buscar?");

                    String dni1 = gestorPersona.pedirDNIsinVerificacion();

                    try {
                        Persona persona = gestorPersona.buscarPersona(dni1);
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

    public void subMenuClientes(GestorPersona gestorPersona) {

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
                    gestorPersona.agregarPersona(1, gestorServicio);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del cliente que desea eliminar?");

                    String dni = gestorPersona.pedirDNIsinVerificacion();
                    gestorPersona.eliminarPersona(dni);
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del cliente que desea buscar?");

                    String dni1 = gestorPersona.pedirDNIsinVerificacion();

                    try {
                        Persona persona = gestorPersona.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("¿Cual es el DNI del cliente al que le desea modificar los datos");

                    String dni2 = gestorPersona.pedirDNIsinVerificacion();
                    try {
                        Persona persona = gestorPersona.buscarPersona(dni2);
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

    public void menuServicio(GestorServicio gestorServicio) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Agregar");
            System.out.println("2. ");
            System.out.println("3. ");
            System.out.println("4. ");
            System.out.println("5.Cambiar la disponibilidad de un servicio ");
            System.out.println("6.Mostrar todos los servicios del salon ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:

                    gestorServicio.agregarServicio();

                    break;
                case 2:
                    gestorServicio.eliminarServicio();
                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:
                    gestorServicio.mostrarServicios();
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuTurnos(GestorTurno gestorTurno) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("TURNOS");
            System.out.println("1.Agregar ");
            System.out.println("2.Eliminar ");
            System.out.println("3.Modificar datos ");
            System.out.println("4.Buscar un turno");
            System.out.println("5.Listar turnos proximos ");
            System.out.println("6.Turnos segun profesional");
            System.out.println("7.Turnos segun cliente");

            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    gestorTurno.agregarTurno(gestorPersona, gestorServicio);
                    break;
                case 2:
                    gestorTurno.eliminarTurno();
                    break;
                case 3:
                    gestorTurno.modificarTurno();
                    break;
                case 4:
                    gestorTurno.buscarTurnoPorCodigo();
                    break;
                case 5:

                    break;
                case 6:
                    turnosXprofesional(gestorTurno);
                    break;
                case 7:
                    turnosXcliente(gestorTurno);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void turnosXprofesional(GestorTurno gestorTurno) {

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

                    break;
                case 2:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void turnosXcliente(GestorTurno gestorTurno) {

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

                    break;
                case 2:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuFacturas(GestorFactura gestorFactura, GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Agregar ");
            System.out.println("2.Eliminar ");
            ///estos no estan
            System.out.println("3.Modificar ");
            System.out.println("4.Buscar ");

            System.out.println("5.Ver historial de facturas");
            System.out.println("6.Historial de facturas por un cliente");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    gestorFactura.crearFactura();
                break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    String dni = gestorPersona.pedirDNIsinVerificacion();
                    try {
                        gestorFactura.historialFacturasPorCliente(dni);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);


    }

}
