package Menus;

import gestores.GestorPersona;
import gestores.GestorTurno;

import java.util.Scanner;

public class MenuProfesional {

    public void menuProfesional(GestorPersona cliente, GestorTurno turnos, String dniProfesional) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Bienvenido al menu de Profesional ");
            System.out.println("1.Ver historial de turnos ");
            System.out.println("2.Ver turnos próximos");
            System.out.println("3. Ver turnos por cliente");
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
                    turnos.historialTurnosXprofesional(dniProfesional);
                    break;
                case 2:
                    turnos.buscarTurnosXdniProfesionalVigentes(dniProfesional);
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

}
