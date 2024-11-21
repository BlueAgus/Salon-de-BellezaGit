package Menus;

import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import gestores.*;
import model.*;

import java.io.FileReader;
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

    /*
        public void inicioMenu(GestorPersona profesionales, GestorPersona clientes, GestorPersona administrador, GestorPersona recepcionistas) {
            List<Persona> profesionals = profesionales.leerArchivoPersona(archivoProfesionales);
            List<Profesional> p1 = new ArrayList<>();
            for (Persona p : profesionals) {
                p1.add((Profesional) p);
            }
            GestorAlmacen<Profesional> profesionalGestorAlmacen = new GestorAlmacen<>();
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
    */
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
                    if (primerIngreso(administradores)) {
                        llenarAdministrador(administradores,servicios);
                    } else {
                        System.out.println("Bienvenido administrador ");
                        if (iniciarSesion(administradores)) {
                            menuAdministrador.mostrarMenu(clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 2:
                    //recepcionista
                    if (primerIngreso(administradores)) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        if (iniciarSesion(recepcionistas)) {
                            System.out.println("Bienvenido Recepcionista !");
                            menuRecepcionista.menuRecepcionistas(clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 3:
                    //profesional
                    if (primerIngreso(administradores)) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        if (iniciarSesion(profesionales)) {
                            System.out.println("Bienvenido profesional! ");
                            menuRecepcionista.menuRecepcionistas(clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void llenarAdministrador(GestorPersona personas, GestorServicio servicios) {
        List<Administrador> aux = new ArrayList<>();
        aux.add(personas.cargarUnAdministrador(servicios));
        personas.guardarArchivoAdministradores(aux);
        System.out.println("Bienvenido administrador ! ");
    }

    public boolean pedirDatos(GestorPersona personaQueDeseaIngresar) {
        boolean tienecuenta = false;
        String dni = personaQueDeseaIngresar.pedirDNIsinVerificacion();
        String contra;
        String contrapedida;
        boolean valido = false;

        try {
            if (personaQueDeseaIngresar.buscarPersonas(dni)) {
                do {
                    contra = personaQueDeseaIngresar.buscarContraseña(dni);
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
                } while (!valido);
            }
        } catch (DNInoEncontradoException e) {
            System.out.println(e.getMessage());
        }
        return tienecuenta;
    }

    public boolean iniciarSesion(GestorPersona personas) {
        if (pedirDatos(personas)) {
            System.out.println("Entrando..");//si es true anda
            return true;
        } else {
            System.out.println("No tienes cuenta aun...");
            System.out.println("Verifica que el administrador te haya cargado correctamente..");
            return false;
        }
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

    public boolean primerIngreso(GestorPersona administradores) {
        List<Administrador> adminAux = administradores.leerArchivoAdministradores();
        boolean primeringreso = false;
        for (Administrador a : adminAux) {
            if (a.getContraseña().equals("12345678")) {
                primeringreso = true;
            }
        }
        return primeringreso;
    }


//no se que onda esto... NO LO BORREN POR LAS DUDAS.
    /*
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

