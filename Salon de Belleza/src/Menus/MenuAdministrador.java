package Menus;

import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import gestores.GestorPersona;
import gestores.GestorServicio;
import model.Persona;

import java.util.Scanner;

public class MenuAdministrador {

    public void MenuAdministrador() {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Bienvenido al menu de Administrador ");
            System.out.println("1.Recepcionista");
            System.out.println("2.Profesionales");
            System.out.println("3.Servicios");
            System.out.println("4.Clientes");
            System.out.println("5.Turnos");
            System.out.println("6. ");
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
                case 3:

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


    public void mostrarMenu1(GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("RECEPCIONISTA");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni ");
            System.out.println("4.Modificar datos ");
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
                    gestorPersona.agregarPersona(3);
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
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void mostrarMenu2(GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("PROFESIONALES");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por DNI");
            System.out.println("4.Modificar datos");
            /// System.out.println("3.Ver Turnos de un profesional ");
            System.out.println("4. ");
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
                    gestorPersona.agregarPersona(2);
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del profesional que desea eliminar?");

                    String dni = gestorPersona.pedirDNIsinVerificacion();
                    gestorPersona.eliminarPersona(dni);
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del profesional que desea buscar?");

                    String dni1 = gestorPersona.pedirDNIsinVerificacion();

                    try {
                        Persona persona = gestorPersona.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("¿Cual es el DNI del profesional al que le desea modificar los datos");

                    String dni2 = gestorPersona.pedirDNIsinVerificacion();
                    try {
                        Persona persona = gestorPersona.buscarPersona(dni2);
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
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


    public void mostrarMenu3(GestorServicio gestorServicio) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Agregar un Servicio");
            System.out.println("2. ");
            System.out.println("3. ");
            System.out.println("4. ");
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

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void mostrarMenu4(GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("CLIENTES");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni ");
            System.out.println("4.Modificar datos ");
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
                    gestorPersona.agregarPersona(1);
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
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

    }

}
