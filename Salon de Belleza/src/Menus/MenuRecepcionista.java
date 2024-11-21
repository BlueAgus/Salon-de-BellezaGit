package Menus;

import gestores.GestorFactura;
import gestores.GestorPersona;
import gestores.GestorServicio;
import gestores.GestorTurno;

import java.util.Scanner;

public class MenuRecepcionista extends MenuAdministrador {


    public void menuRecepcionistas(GestorPersona clientes, GestorPersona profesionales,GestorPersona recepcionista,GestorPersona administrador,GestorServicio servicios,GestorTurno turnos,GestorFactura facturas) {

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
                    subMenuClientes(clientes,servicios);
                    break;
                case 2:
                    menuTurnos(turnos,clientes,profesionales,servicios);
                    break;
                case 3:
                    menuFacturas(facturas,clientes, turnos);
                    break;
                case 4:
                    servicios.mostrarServicios();
                    break;
                case 5:
                    mostrarProfesionales(profesionales);
                    break;
                case 6:

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void mostrarProfesionales(GestorPersona profesionales) {

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
                    profesionales.

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
