package Menus;

import excepciones.CodigoNoEncontradoException;
import gestores.GestorFactura;
import gestores.GestorPersona;
import gestores.GestorServicio;
import gestores.GestorTurno;
import model.Profesional;
import model.Servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuRecepcionista extends MenuAdministrador {


    public void menuRecepcionistas(GestorPersona clientes, GestorPersona profesionales, GestorPersona recepcionista, GestorPersona administrador, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Gestion de clientes");
            System.out.println("2.Gestion de turnos");
            System.out.println("3.Gestion de facturas");
            System.out.println("4.Mostrar nuestros servicios");
            System.out.println("5.Ver profesionales en el salón");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    subMenuClientes(clientes, servicios);
                    break;
                case 2:
                    menuTurnos(turnos, clientes, profesionales, servicios);
                    break;
                case 3:
                    menuFacturas(facturas, clientes, turnos);
                    break;
                case 4:
                    servicios.mostrarServicios();
                    break;
                case 5:
                    mostrarProfesionales(profesionales, servicios);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void mostrarProfesionales(GestorPersona profesionales, GestorServicio servicios) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Mostrar profesionales del salón ");
            System.out.println("2.Mostrar profesionales por servicio específico ");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    profesionales.mostrarTodos();
                    break;
                case 2:
                    Servicio servicio = null;
                    while (true) {
                        try {
                            servicio = servicios.buscarServicio();
                        } catch (CodigoNoEncontradoException e) {
                            System.out.println(e.getMessage());
                        }

                        for (Profesional p : (List<Profesional>) profesionales.getAlmacenPersonas()) {
                            if (p.verificarProfesion(servicio.getCodigo_servicio())) {
                                System.out.println(p.toString());
                            }
                        }
                        break;
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

    }

}
