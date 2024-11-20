package Menus;

import gestores.GestorFactura;
import gestores.GestorPersona;
import gestores.GestorServicio;
import gestores.GestorTurno;

import java.util.Scanner;

public class MenuRecepcionista {

    private MenuAdministrador menuAdministrador;

    public void MenuRecepcionistas(GestorPersona gestorPersona,GestorServicio gestorServicio,GestorTurno gestorTurno,GestorFactura gestorFactura) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Bienvenido al menu de Recepcionista");
            System.out.println("1.Gestion de clientes");
            System.out.println("2.Gestion de turnos");
            System.out.println("3.Gestion de facturas");
            System.out.println("4.Ver servicios");
            System.out.println("4.Profesionales en el salon");
            System.out.println("5.Cambiar la disponibilidad de un servicio ");
            System.out.println("6. ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    menuAdministrador.subMenuClientes(gestorPersona,gestorServicio);
                    break;
                case 2:
                    menuAdministrador.menuTurnos(gestorTurno,gestorPersona,gestorServicio);
                    break;
                case 3:
                    menuAdministrador.menuFacturas(gestorFactura,gestorPersona);
                    break;
                case 4:
                    gestorServicio.mostrarServicios();
                    break;
                case 5:
                    mostrarProfesionales(gestorPersona);
                    break;
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void mostrarProfesionales(GestorPersona gestorPersona) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Todos los profesionales del salon ");
            System.out.println("2.Profesionales segun servicio ");
            System.out.println("3. ");
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

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

    }

}
