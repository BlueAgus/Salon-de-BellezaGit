package Menus;

import gestores.*;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    Scanner scanner = new Scanner(System.in);

    String archivoProfesionales = "profesionales.json";
    String archivoAdministradores = "administradores.json";
    String archivoRecepcionista = "recepcionistas.json";
    String archivoCliente = "clientes.json";

    String archivoServicios = "servicios.json";
    String archivoTurnos = "turnos.json";
    String archivoFacturas = "facturas.json";

    public void inicioMenu(GestorPersona profesionales, GestorPersona clientes, GestorPersona administrador, GestorPersona recepcionistas) {
        List<Persona> profesionals = profesionales.leerArchivoPersona(archivoProfesionales);
        List<Profesional> p1 = new ArrayList<>();
        for (Persona p : profesionals) {
            p1.add((Profesional) p);
        }
        GestorAlmacen<Profesional> profesionalGestorAlmacen= new GestorAlmacen<>();
        profesionalGestorAlmacen.setAlmacen(p1);
        profesionales.setAlmacenPersonas(profesionalGestorAlmacen);


        ///clientes
        List<Persona> clientes3 = profesionales.leerArchivoPersona(archivoCliente);
        List<Cliente> clientes1 = new ArrayList<>();
        for (Persona p : clientes3) {
            clientes1.add((Cliente) p);
        }
        GestorAlmacen<Cliente> clienteGestorAlmacen = new GestorAlmacen<>();
        profesionalGestorAlmacen.setAlmacen(clientes1);
        profesionales.setAlmacenPersonas(clienteGestorAlmacen);

        //administrador
        List<Administrador> admin = profesionales.leerArchivoPersona(archivoAdministradores);
        List<Administrador> admin2 = new ArrayList<>();
        for (Persona p : admin) {
            admin2.add((Administrador) p);
        }
        GestorAlmacen<Administrador> AdministradorGestorAlmacen = new GestorAlmacen<>();
        profesionalGestorAlmacen.setAlmacen(admin2);
        administrador.setAlmacenPersonas(clienteGestorAlmacen);

        //recepecionista
        List<Recepcionista> re1 = profesionales.leerArchivoPersona(archivoAdministradores);
        List<Recepcionista> re2 = new ArrayList<>();
        for (Persona p : re1) {
            re2.add((Administrador) p);
        }
        GestorAlmacen<Recepcionista> RecepcionistaGestorAlmacen = new GestorAlmacen<>();
        profesionalGestorAlmacen.setAlmacen(re2);
        recepcionistas.setAlmacenPersonas(clienteGestorAlmacen);


    }

    public void menuPrincipal() {
        GestorPersona profesionales = new GestorPersona();
        GestorPersona administradores = new GestorPersona();
        GestorPersona recepcionistas = new GestorPersona();
        GestorPersona clientes = new GestorPersona();

        GestorServicio servicios = new GestorServicio();
        GestorTurno turnos = new GestorTurno();
        GestorFactura facturas = new GestorFactura(archivoFacturas);


        MenuAdministrador menuAdministrador = new MenuAdministrador();
        MenuRecepcionista menuRecepcionista = new MenuRecepcionista();
        MenuProfesional menuProfesional = new MenuProfesional();

        int opcion;
        do {

            System.out.println("Bienvenido a Estetica Queens!\n");
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
                    System.out.println("Bienvenido administrador ");
                    if (iniciarSesion(usuarios, archivoUsuarioAdministradores))//si es true tiene su dni registrado
                    {
                        menuAdministrador.mostrarMenu(clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        if (usuarios.primerIngreso()) {
                            //crea un usuario.
                        }
                    }
                    break;
                case 2:
                    //recepcionista
                    menuRecepcionista.MenuRecepcionistas();
                    System.out.println("Bienvenido Recepcionista ");
                    iniciarSesion(usuarios, archivoUsuarioRecepcionista);

                    break;
                case 3:
                    //profesional
                    System.out.println("Bienvenido profesional ");
                    iniciarSesion(usuarios, archivoUsuarioProfesionales);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public boolean pedirDatos(GestorUsuarios usuarios, String nombrearchivo) {
        System.out.println("Ingresa tu dni");
        String dni = scanner.nextLine();
        String contra;
        String contrapedida;
        boolean valido = false;
        boolean tienecuenta = false;
        if (usuarios.verificarUsuario(dni, nombrearchivo)) {
            do {
                contra = usuarios.DevolverContrasenia(dni);
                System.out.println("Ingresa tu contraseña:");
                contrapedida = scanner.nextLine();

                if (contrapedida.equals(contra)) {
                    valido = true;
                    return tienecuenta; //
                } else {
                    System.out.println("Contraseña incorrecta. Inténtalo nuevamente.");
                }
            } while (!valido);

        } else {
            return tienecuenta;
        }
    }

    public boolean iniciarSesion(GestorUsuarios usuarios, String nombrearchivo) {
        if (pedirDatos(usuarios, nombrearchivo)) {
            System.out.println("Entrando..");//si es true anda todo ok.
            return true;
        } else {
            System.out.println("No tienes cuenta aun...");
            System.out.println("Verifica que el administrador te haya cargado correctamente..");
            return false;
        }
    }
}
}
