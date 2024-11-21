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

    static String archivoProfesionales = "profesionales.json";
   static String archivoAdministradores = "administradores.json";
   static String archivoRecepcionista = "recepcionistas.json";
   static String archivoCliente = "clientes.json";
   static String archivoPrecios = "precios.json";
   static String archivoServicios = "servicios.json";
   static String archivoTurnos = "turnos.json";
   static String archivoFacturas = "facturas.json";

    public void menuPrincipal() {
        GestorPersona profesionales = new GestorPersona();
        GestorPersona administradores = new GestorPersona();
        GestorPersona recepcionistas = new GestorPersona();
        GestorPersona clientes = new GestorPersona();


        GestorServicio servicios = new GestorServicio();
        GestorTurno turnos = new GestorTurno();
        GestorFactura facturas = new GestorFactura();


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
                    if (primerIngreso(administradores)) {
                        llenarAdministrador(administradores,servicios);
                    } else {
                        System.out.println("Bienvenido administrador ");
                        String dni= iniciarSesion(administradores);
                        if (dni!=null) {
                            menuAdministrador.mostrarMenu(dni, clientes, profesionales, recepcionistas, administradores, servicios, turnos, facturas);
                        }
                    }
                    break;
                case 2:
                    //recepcionista
                    if (primerIngreso(administradores)) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        String dni1=iniciarSesion(recepcionistas);
                        if (dni1!=null) {
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
                        String dni3= iniciarSesion(profesionales);
                        if (dni3!=null) {
                            System.out.println("Bienvenido profesional! ");
                            menuProfesional.menuProfesional(clientes, turnos, dni3, servicios);
                        }
                    }
                    break;
                case 0:
                    boolean salir=false;
                    do {
                        System.out.println("Deseas guardar los cambios realizados?");
                        System.out.println("1.Salir sin guardar. ");
                        System.out.println("1.Salir y guardar todos los cambios.");

                        int opc = scanner.nextInt();
                        scanner.nextLine();
                        if (opcion == 1) {
                            cerrarSistema(administradores, clientes, recepcionistas, profesionales, servicios, turnos, facturas);
                            System.out.println("Se ha guardado con exito.");
                            salir=true;
                        } else if (opcion == 2) {
                            System.out.println("saliendo...");
                            salir=true;
                        }
                    }while(!salir);

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void inicioSistema(GestorPersona admin, GestorPersona clientes, GestorPersona recepcionista, GestorPersona profesionales, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas){

        //inicializar personas
        profesionales.leerArchivoProfesionales();
        admin.leerArchivoAdministradores();
        recepcionista.leerArchivoRecepcionistas();
        clientes.leerArchivoClientes();
        //inicializar servicio
        servicios.LeerArchivo(archivoServicios);
        //turnos
        turnos.leerArchivoTurnos();
        //factura
        facturas.leerDesdeGson();
        // precios
        GestorPrecios.leerPreciosDesdeArchivo(archivoPrecios);
    }

    public void cerrarSistema(GestorPersona admin, GestorPersona clientes, GestorPersona recepcionista, GestorPersona profesionales, GestorServicio servicios, GestorTurno turnos, GestorFactura facturas)
    {
        //gaurda tooodo en archivos.
        profesionales.guardarArchivoProfesionales(profesionales.getAlmacenPersonas().getAlmacen());
        admin.guardarArchivoAdministradores(admin.getAlmacenPersonas().getAlmacen());
        recepcionista.guardarArchivoAdministradores(recepcionista.getAlmacenPersonas().getAlmacen());
        clientes.guardarArchivoClientes(clientes.getAlmacenPersonas().getAlmacen());

        servicios.EscribirServiciosEnArchivo(archivoServicios,servicios.getAlmacenServicios().getAlmacen());
        //turnos
        turnos.guardarEnArchivoTurnos();
        //factura
        facturas.guardarEnArchivo();
        // precios
        GestorPrecios.guardarPreciosEnArchivo(archivoPrecios);
        System.out.println("Se ha cerrado el sistema. ");
    }
    public void llenarAdministrador(GestorPersona personas, GestorServicio servicios) {
        List<Administrador> aux = new ArrayList<>();
        aux.add(personas.cargarUnAdministrador(servicios));
        personas.guardarArchivoAdministradores(aux);
        System.out.println("Bienvenido administrador ! ");
    }

    public String pedirDatos(GestorPersona personaQueDeseaIngresar) {
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
        if(valido)
        {
            return dni;
        }
        else {
            return null;
        }
    }

    public String iniciarSesion(GestorPersona personas) {

        String dni=pedirDatos(personas);

        if (dni!=null) {
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

