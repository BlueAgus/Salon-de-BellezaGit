package Menus;

import gestores.GestorCliente;
import gestores.GestorServicio;
import gestores.GestorTurno;

import java.util.Scanner;

public class MenuProfesional {

    public void menuProfesional(GestorCliente cliente, GestorTurno turnos, String dniProfesional, GestorServicio servicios) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Bienvenido al menu de Profesional ");
            System.out.println("1.Ver historial de turnos ");
            System.out.println("2.Ver turnos próximos");
            System.out.println("3. Reportar falla de servicio");
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    turnos.historialTurnosXprofesional(dniProfesional);
                    break;
                case 2:
                    turnos.buscarTurnosXdniProfesionalVigentes(dniProfesional);
                    break;
                case 3:
                    servicios.reportarFalla(cliente, turnos);
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

}
