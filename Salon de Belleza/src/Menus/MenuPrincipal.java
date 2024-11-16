package Menus;

import java.util.Scanner;

public class MenuPrincipal {

    public void menuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("Bienvenido a (nombre de la estetica)");
            System.out.println("¿Quién está ingresando?");
            System.out.println("--------------------");
            System.out.println("1. Administrador ");
            System.out.println("2. Recepcionista");
            System.out.println("3. Profesional");
            System.out.println("0. Para salir ");
            System.out.println("--------------------");
            System.out.print("Ingrese una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    //administrador
                    break;
                case 2:
                    //recepcionista
                    break;
                case 3:
                    //profesional
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }
}
