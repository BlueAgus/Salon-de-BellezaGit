package Menus;

import excepciones.DNInoEncontradoException;
import gestores.*;
import gestores.GestorProfesional;
import model.*;


import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    Scanner scanner = new Scanner(System.in);

    static String archivoPrecios = "precios.json";
    static String archivoServicios = "servicios.json";
    static String archivoTurnos = "turnos.json";
    static String archivoFacturas = "facturas.json";

    GestorProfesional profesionales = new GestorProfesional();
    GestorAdministrador administradores = new GestorAdministrador();
    GestorRecepcionista recepcionistas = new GestorRecepcionista();
    GestorCliente clientes = new GestorCliente();


    GestorServicio servicios = new GestorServicio();
    GestorTurno turnos = new GestorTurno();
    GestorFactura facturas = new GestorFactura();

/*
    public void menuPrincipal() {

        MenuAdministrador menuAdministrador = new MenuAdministrador();
        MenuRecepcionista menuRecepcionista = new MenuRecepcionista();
        MenuProfesional menuProfesional = new MenuProfesional();

        inicioSistema(administradores, clientes, recepcionistas, profesionales, servicios, turnos, facturas);

        int opcion;
        do {

            System.out.println("Bienvenido a Estetica Queens!\n");
            System.out.println("¿Quién está ingresando?");
            System.out.println("--------------------");
            System.out.println("1. Administrador ");
            System.out.println("2. Recepcionista");
            System.out.println("3. Profesional");
            System.out.println("0. Salir ");
            System.out.println("--------------------");
            System.out.print("Ingrese una opción: ");
            opcion = scanner.nextInt();


            switch (opcion) {
                case 1:
                    //administrador
                    if (primerIngreso()) {
                        llenarAdministrador();
                    } else {
                        System.out.println("Bienvenido administrador ");
                        String dni = iniciarSesion();
                        if (dni != null) {
                            menuAdministrador.mostrarMenu(dni, clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 2:
                    //recepcionista
                    if (primerIngreso()) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        String dni1 = iniciarSesion();
                        if (dni1 != null) {
                            System.out.println("Bienvenido Recepcionista !");
                            menuRecepcionista.menuRecepcionistas(clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 3:
                    //profesional
                    if (primerIngreso()) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        String dni3 = iniciarSesion();
                        if (dni3 != null) {
                            System.out.println("Bienvenido profesional! ");
                            menuProfesional.menuProfesional(clientes, turnos, dni3, servicios);
                        }
                    }
                    break;
                case 0:
                    boolean salir = false;
                    do {
                        System.out.println("Deseas guardar los cambios realizados?");
                        System.out.println("1.Salir sin guardar. ");
                        System.out.println("1.Salir y guardar todos los cambios.");

                        int opc = scanner.nextInt();
                        scanner.nextLine();
                        if (opcion == 1) {
                            cerrarSistema(administradores, clientes, recepcionistas, profesionales, servicios, turnos, facturas);
                            System.out.println("Se ha guardado con exito.");
                            salir = true;
                        } else if (opcion == 2) {
                            System.out.println("saliendo...");
                            salir = true;
                        }
                    } while (!salir);

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void inicioSistema(GestorAdministrador admin, GestorCliente clientes, GestorRecepcionista recepcionista, GestorProfesional profesionales, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas) {

        profesionales.setProfesionales(profesionales.leerArchivoProfesionales());
        admin.setAdministradores(administradores.leerArchivoAdministradores());
        recepcionista.setAlmacenPersonas(recepcionista.leerArchivoRecepcionistas());
        clientes.setClientes(clientes.leerArchivoClientes());
        //inicializar servicio
        servicios.LeerArchivo(archivoServicios);
        //turnos
        turnos.setListaTurnos(turnos.leerArchivoTurnos());

        //factura
        facturas.leerDesdeGson();

        // precios
        GestorPrecios.leerPreciosDesdeArchivo(archivoPrecios);
    }
    }

    public void cerrarSistema(GestorAdministrador admin, GestorCliente clientes, GestorRecepcionista recepcionista, GestorProfesional profesionales, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas) {
        //gaurda tooodo en archivos.
        profesionales.guardarArchivoProfesionales(profesionales.getProfesionales());
        admin.guardarArchivoAdministradores(admin.getAdministradores());
        recepcionista.guardarArchivoRecepcionistas(recepcionista.getAlmacenPersonas());
        clientes.guardarArchivoClientes(clientes.getClientes());

        /*servicios.EscribirServiciosEnArchivo(archivoServicios, servicios.getAlmacenServicios().getAlmacen());
        //turnos
        turnos.guardarEnArchivoTurnos(turnos.getListaTurnos());
        //factura
        facturas.guardarEnArchivo(facturas.getHistorial().getAlmacen());
        // precios
        GestorPrecios.guardarPreciosEnArchivo(archivoPrecios);
        System.out.println("Se ha cerrado el sistema. ");
    }

    public void llenarAdministrador() {

        administradores.guardarArchivoAdministradores(administradores.getAdministradores());
        System.out.println("Bienvenido administrador ! ");
    }

    public String pedirDatos() {
        boolean tienecuenta = false;
        String dni = administradores.pedirDNIsinVerificacion();
        String contra=null;
        String contrapedida;
        boolean valido = false;

        do {
            try {
                if (administradores.buscarPersonas(dni)) {
                    contra = administradores.buscarContraseña(dni);

                } else if (recepcionistas.buscarPersonas(dni)) {
                    contra = recepcionistas.buscarContraseña(dni);
                } else if (profesionales.buscarPersonas(dni)) {
                    contra = profesionales.buscarContraseña(dni);
                }

                if (contra == null) {
                    System.out.println("no tiene contrasenia..");
                    break;
                }
                contrapedida = pedirContraseña();

                if (contrapedida.equals(contra)) {
                    valido = true;
                    tienecuenta = true; //
                } else {
                    System.out.println("Contraseña incorrecta. Inténtalo nuevamente.");
                }
            } catch (DNInoEncontradoException e) {
                throw new RuntimeException(e);
            }
            if(!valido)
            {
                System.out.println("Vuelva a intentar");
            }
        } while (!valido);

        if (valido) {
            return dni;
        } else {
            return null;
        }
    }

    public String iniciarSesion() {

        String dni = pedirDatos();

        if (dni != null) {
            System.out.println("Entrando..");
        } else {
            System.out.println("No tienes cuenta aun...");
            System.out.println("Verifica que el administrador te haya cargado correctamente..");
        }
        return dni;
    }

    public String pedirContraseña() {
        String contraseña = "";
        do {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
            } else if (!contraseña.matches(".\\d.")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
            }
        } while (contraseña.length() < 6 || contraseña.length() > 12 || !contraseña.matches(".\\d.")); // Bucle sigue hasta que la contraseña sea válida

        return contraseña;
    }

    public boolean primerIngreso() {
        List<Administrador> adminAux = administradores.leerArchivoAdministradores();
        boolean primeringreso = false;
        for (Administrador a : adminAux) {
            if (a.getContraseña().equals("12345678") && a.getDni().equals("12345678")) {
                primeringreso = true;
            }
        }
        return primeringreso;
    }

//no se que onda esto... NO LO BORREN POR LAS DUDAS.

    public boolean verificarDniAdministradores(String dni, GestorPersona administradores) {
        List<Administrador> e = administradores.leerArchivoPersona("administradores.json");
        boolean a = false;
        for (Persona aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
            return a;
        }
    }

    public boolean verificarContraseniaAdministrador(String dni, String contrasenia, GestorPersona administradores) {
        List<Administrador> e = administradores.leerArchivoPersona("administradores.json");
        boolean a = false;
        for (Administrador aux : e) {
            if (aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarDniProfesionales(String dni, GestorPersona profesionales) {
        List<Profesional> e = profesionales.leerArchivoPersona("profesionales.json");
        boolean a = false;
        for (Profesional aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarContraseniaProfesional(String dni, String contrasenia, GestorPersona profesionales) {
        List<Profesional> e = profesionales.leerArchivoPersona("profesionales.json");
        boolean a = false;
        for (Profesional aux : e) {
            if (aux.getDni().equals(dni) && aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarDniRecepcionistas(String dni, GestorPersona recepcionistas) {
        List<Recepcionista> e = recepcionistas.leerArchivoPersona("recepcionistas.json");
        boolean a = false;
        for (Recepcionista aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarContraseniaRecepcionista(String dni, String contrasenia, GestorPersona recepcionistas) {
        List<Recepcionista> e = recepcionistas.leerArchivoPersona("recepcionistas.json");
        boolean a = false;
        for (Recepcionista aux : e) {
            if (aux.getDni().equals(dni) && aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }
*/

}

