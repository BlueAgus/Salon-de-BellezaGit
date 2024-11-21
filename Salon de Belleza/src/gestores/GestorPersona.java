package gestores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import excepciones.TelefonoInvalidoException;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class GestorPersona<T extends Persona> {

    private static Scanner scanner = new Scanner(System.in);
    private GestorUsuario<T> almacenPersonas = new GestorUsuario<>();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
/*
    private <T extends Persona> GestorPersona crearGestorParaClase(Class<T> tipoPersona, String nombreArchivo) {
        GestorPersona gestor = new GestorPersona();

        // Leer personas desde un archivo específico para la clase
        List<Persona> personas = gestor.leerArchivoPersona(nombreArchivo); // Ajusta esta parte si el archivo depende de la clase
        List<T> personasFiltradas = filtrarPorClase(personas, tipoPersona);

        // Configurar el almacén
        GestorAlmacen<T> almacen = new GestorAlmacen<>();
        almacen.setAlmacen(personasFiltradas);
        gestor.setAlmacenUsuario((GestorUsuario<Persona>) almacen);

        return gestor;
    }
*/
    private <T> List<T> filtrarPorClase(List<T> personas, Class<T> tipoPersona) {
        List<T> filtradas = new ArrayList<>();
        for (T persona : personas) {
            if (tipoPersona.isInstance(persona)) {
                filtradas.add(tipoPersona.cast(persona));
            }
        }
        return filtradas;
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    //pasamos 1 si es cliente,
    // 2 si es profesional,
    // 3 si es recepcionista,
    // 4 si es administrador
    public boolean agregarPersona(int tipoPersona, GestorServicio gestorServicio) {
        boolean cargado = false;

        String dni = "";
        while (true) {
            try {
                dni = pedirDNI();
                break;
            } catch (DNIyaCargadoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String nombre = pedirNombre();
        String apellido = pedirApellido();

        String genero = "";
        while (true) {
            try {
                genero = pedirGenero();
                break;
            } catch (GeneroInvalidoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String telefono = "";
        while (true) {
            try {
                telefono = pedirTelefono();
                break;
            } catch (TelefonoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }

        switch (tipoPersona) {
            case 1:
                Cliente cliente = new Cliente(nombre, apellido, dni, genero, telefono);
                cargado = true;
                if (almacenPersonas.agregar(cliente)) {
                    System.out.printf("\nCLIENTE AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR CLIENTE\n");
                }
                System.out.println(cliente);
                verificarCarga(cliente, gestorServicio);
                almacenPersonas.agregar(cliente);
                break;
            case 2:
                int opcion = 0;
                String contra = pedirContraseña();
                Profesional profesional = new Profesional(nombre, apellido, dni, genero, telefono, contra);

                GestorTurno aux = new GestorTurno();
                String e = aux.pedirCodServicio(gestorServicio);
                profesional.agregarProfesion(e);//minimo una profesion.

                do {

                    System.out.println("Deseas agregar otra profesion?");
                    System.out.println("1. Si deseo.");
                    System.out.println("2. No deseo.");
                    opcion = scanner.nextInt();
                    scanner.nextLine();

                    if (opcion == 1) {
                        e = aux.pedirCodServicio(gestorServicio);
                        profesional.agregarProfesion(e);
                    } else if (opcion != 2) {
                        System.out.println("Ingresa una opcion valida por favor.");
                    }
                } while (opcion != 2);

                cargado = true;
                if (almacenPersonas.agregar(profesional)) {
                    System.out.printf("\n PROFESIONAL AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR PROFESIONAL\n");
                }
                System.out.println(profesional);
                verificarCarga(profesional, gestorServicio);
                break;
            case 3:
                String contra2 = pedirContraseña();
                Recepcionista recepcionista = new Recepcionista(nombre, apellido, dni, genero, telefono, contra2);
                cargado = true;
                if (almacenPersonas.agregar(recepcionista)) {
                    System.out.printf("\nRECEPCIONISTA AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR RECEPCIONISTA\n");
                }
                System.out.println(recepcionista);
                verificarCarga(recepcionista, gestorServicio);
                break;
            case 4:
                String contra3 = pedirContraseña();
                Administrador administrador = new Administrador(nombre, apellido, dni, genero, telefono, contra3);
                cargado = true;
                if (almacenPersonas.agregar((administrador)) {
                    System.out.printf("\nADMINISTRADOR AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR ADMINISTRADOR\n");
                }
                almacenPersonas.agregar(administrador);
                System.out.println(administrador);
                verificarCarga(administrador, gestorServicio);
                break;
        }
        return cargado;
    }

    //estos buscan en la list ano en el archivo a
    public boolean eliminarPersona(String dni) {
        try {
            T p = buscarPersona(dni);
            return almacenPersonas.eliminar(p);
        } catch (DNInoEncontradoException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }

    public T buscarPersona(String dni) throws DNInoEncontradoException {
        for (T p : almacenPersonas.getAlmacen()) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }

    public boolean buscarPersonas(String dni) throws DNInoEncontradoException {
        for (T p : almacenPersonas.getAlmacen()) {
            if (p.getDni().equals(dni)) {
                return true;
            }
        }
        throw new DNInoEncontradoException("DNI no encontrado!!");
    }

    public String buscarContraseña(String dni) {
        for (T p : almacenPersonas.getAlmacen()) {
            if (p.getDni().equals(dni)) {

                if (p instanceof Administrador) {
                    Administrador administrador = (Administrador) p;
                    return administrador.getContraseña();
                } else if (p instanceof Recepcionista) {
                    Recepcionista recepcionista = (Recepcionista) p;
                    return recepcionista.getContraseña();
                } else if (p instanceof Profesional) {
                    Profesional profesional = (Profesional) p;
                    return profesional.getContraseña();
                }
            }
        }
        return null;
    }

    public void modificarPersona(T persona, GestorServicio gestorServicio) {
        int opcion;
        boolean continuarModificando = true;

        if (persona instanceof Profesional) {
            modificarProfesional((Profesional) persona, gestorServicio);
        } else {
            while (continuarModificando) {
                System.out.println("¿Qué te gustaría modificar?");
                System.out.println("1. Nombre");
                System.out.println("2. Apellido");
                System.out.println("3. DNI");
                System.out.println("4. Genero");
                System.out.println("5. Telefono");
                System.out.println("7. Contraseña");
                System.out.println("8. Salir");
                try {
                    opcion = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcion) {
                        case 1:
                            persona.setNombre(pedirNombre());
                            break;
                        case 2:
                            persona.setApellido(pedirApellido());
                            break;
                        case 3:
                            try {
                                persona.setDni(pedirDNI());
                            } catch (DNIyaCargadoException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 4:
                            try {
                                persona.setGenero(pedirGenero());
                            } catch (GeneroInvalidoException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 5:
                            try {
                                persona.setTelefono(pedirTelefono());
                            } catch (TelefonoInvalidoException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 6:
                            continuarModificando = false;
                            break;
                        case 7:
                            if (persona instanceof Administrador) {
                                Administrador admin = (Administrador) persona;
                                admin.setContraseña(pedirContraseñaNueva(admin.getContraseña()));

                            } else if (persona instanceof Recepcionista) {
                                Recepcionista recep = (Recepcionista) persona;
                                recep.setContraseña(pedirContraseñaNueva(recep.getContraseña()));

                            } else {
                                System.out.println("El cliente no es un usuario en sistema.");
                            }
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Por favor, ingresa un número válido.");
                    scanner.nextLine(); // Limpiar el buffer
                }
            }
            System.out.println("¡MODIFICADO EXITOSAMENTE!");
            System.out.println(persona.toString());
        }
    }

    public Administrador cargarUnAdministrador(GestorServicio servicio) {
        String dni = "";
        while (true) {
            try {
                dni = pedirDNI();
                break;
            } catch (DNIyaCargadoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String nombre = pedirNombre();
        String apellido = pedirApellido();

        String genero = "";
        while (true) {
            try {
                genero = pedirGenero();
                break;
            } catch (GeneroInvalidoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String telefono = "";
        while (true) {
            try {
                telefono = pedirTelefono();
                break;
            } catch (TelefonoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }
        String contra3 = pedirContraseña();
        Administrador administrador = new Administrador(nombre, apellido, dni, genero, telefono, contra3);
        if (almacenPersonas.agregar(administrador)) {
            System.out.printf("\nADMINISTRADOR AGREGADO EXITOSAMENTE \n");
        } else {
            System.out.printf("\nERROR AL AGREGAR ADMINISTRADOR\n");
        }
        almacenPersonas.agregar(administrador);
        System.out.println(administrador);
        verificarCarga(administrador, servicio);

        return administrador;
    }

    public void modificarProfesional(Profesional profesional, GestorServicio servicios) {
        int opcion;
        boolean continuarModificando = true;
        while (continuarModificando) {

            System.out.println("¿Qué te gustaría modificar del profesional?");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. Genero");
            System.out.println("5. Telefono");
            System.out.println("6. Servicios que ofrece");
            System.out.println("7. Modificar contraseña");
            System.out.println("8. Salir");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        profesional.setNombre(pedirNombre());
                        break;
                    case 2:
                        profesional.setApellido(pedirApellido());
                        break;
                    case 3:
                        try {
                            profesional.setDni(pedirDNI());
                        } catch (DNIyaCargadoException e) {
                            System.out.printf(e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            profesional.setGenero(pedirGenero());
                        } catch (GeneroInvalidoException e) {
                            System.out.printf(e.getMessage());
                        }
                        break;
                    case 5:
                        try {
                            profesional.setTelefono((pedirTelefono()));
                        } catch (TelefonoInvalidoException e) {
                            System.out.println(e);
                        }
                        break;
                    case 6:
                        GestorTurno aux = new GestorTurno();
                        String e = aux.pedirCodServicio(servicios);
                        profesional.agregarProfesion(e);
                        break;
                    case 7:
                        profesional.setContraseña(pedirContraseñaNueva(profesional.getContraseña()));
                        break;
                    case 8:
                        continuarModificando = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine(); // Limpiar el buffer
            }
            System.out.println("MODIFICADO EXITOSAMENTE!");
            System.out.println(profesional.toString());
        }
    }

    public void mostrarTodos() {
        for (Persona p : almacenPersonas.getAlmacen()) {
            System.out.println(p.toString());
        }
    }

    public void verificarCarga(Persona persona, GestorServicio gestorServicio) {
        int opcion;
        do {
            System.out.println("¿Deseas modificar algo de la persona?");
            System.out.println("1. Sí");
            System.out.println("2. No");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    modificarPersona(persona, gestorServicio);
                    break;
                case 2:
                    System.out.println("....");
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
                    break;
            }
        } while (opcion != 2 && opcion != 1);
    }

    public String pedirTelefono() throws TelefonoInvalidoException {
        String telefono = "";
        boolean telefonoValido = false;

        while (!telefonoValido) {
            System.out.print("Ingrese el teléfono: ");
            scanner.nextLine();
            telefono = scanner.nextLine().trim();

            // Validar que el número tenga exactamente 10 dígitos y solo contenga números
            if (!telefono.matches("\\d{10}")) {
                throw new TelefonoInvalidoException("El número de teléfono debe tener  10 dígitos y solo contener números.");
            } else {
                // Si es válido, confirmamos y salimos del bucle
                telefonoValido = true;
            }
        }
        return telefono;
    }

    public String pedirNombre() {
        String nombre = "";
        boolean nombreValido = false;

        while (!nombreValido) {
            System.out.print("Ingrese el nombre: ");
            nombre = scanner.nextLine();

            // Validar que el nombre no esté vacío y que contenga solo letras y espacios
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío. Por favor ingresa un nombre válido.");
            } else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*")) {
                System.out.println("Error: El nombre solo puede contener letras y espacios entre palabras.");
            } else {
                // Si el nombre es válido, formatearlo con la primera letra en mayúscula
                nombre = PasarMayuscula(nombre);
                nombreValido = true;
            }
        }
        return nombre;
    }

    private String PasarMayuscula(String nombre) {
        String[] palabras = nombre.split(" "); // Separar las palabras por espacio
        StringBuilder nombreFormateado = new StringBuilder();

        for (String palabra : palabras) {
            // Poner la primera letra en mayúscula y las demás en minúscula
            if (palabra.length() > 0) {
                nombreFormateado.append(palabra.substring(0, 1).toUpperCase()) // Primera letra en mayúscula
                        .append(palabra.substring(1).toLowerCase()) // Resto de la palabra en minúscula
                        .append(" "); // Agregar espacio entre palabras
            }
        }
        // Eliminar el último espacio vacio
        return nombreFormateado.toString().trim();
    }

    public String pedirApellido() {
        String apellido = "";
        boolean apellidoValido = false;

        while (!apellidoValido) {
            System.out.print("Ingrese el apellido: ");
            apellido = scanner.nextLine();

            // Validar que el apellido no esté vacío y que contenga solo letras y espacios
            if (apellido.isEmpty()) {
                System.out.println("Error: El apellido no puede estar vacío. Por favor ingresa un apellido válido.");
            } else if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*")) {
                System.out.println("Error: El apellido solo puede contener letras y espacios entre palabras.");
            } else {
                // Si el apellido es válido, formatearlo con la primera letra en mayúscula
                apellido = PasarMayuscula(apellido);
                apellidoValido = true;
            }
        }
        return apellido;
    }

    public String pedirDNI() throws DNIyaCargadoException {
        String dni = "";
        boolean dnivalido = false;

        while (!dnivalido) {
            System.out.println("Ingrese el DNI: ");
            dni = scanner.nextLine();

            // no esté vacío
            if (dni.isEmpty()) {
                System.out.println("Error: El DNI no puede estar vacío.");
            }
            //  contenga números
            else if (!dni.matches("\\d+")) {
                System.out.println("Error: El DNI solo puede contener números.");
            }
            //  dígitos
            else if (dni.length() != 8) {
                System.out.println("Error: El DNI debe tener exactamente 8 dígitos.");
            }
            // Verificar si el DNI ya está cargado en el sistema
            else {
                boolean dniRepetido = false;
                for (Persona a : almacenPersonas.getAlmacen()) {
                    if (a.getDni().equals(dni)) {
                        dniRepetido = true;
                        break;
                    }
                }
                if (dniRepetido) {
                    throw new DNIyaCargadoException("DNI ya cargado en el sistema: " + dni);
                } else {
                    dnivalido = true;
                }
            }
        }
        return dni;
    }

    public String pedirDNIsinVerificacion() {
        String dni = "";
        boolean dnivalido = false;

        while (!dnivalido) {
            System.out.println("Ingrese el DNI: ");
            dni = scanner.nextLine();

            // no esté vacío
            if (dni.isEmpty()) {
                System.out.println("Error: El DNI no puede estar vacío.");
            }
            //  contenga números
            else if (!dni.matches("\\d+")) {
                System.out.println("Error: El DNI solo puede contener números.");
            }
            //  dígitos
            else if (dni.length() != 8) {
                System.out.println("Error: El DNI debe tener exactamente 8 dígitos.");
            }
        }
        return dni;
    }

    public String pedirGenero() throws GeneroInvalidoException {
        String genero;
        while (true) {
            System.out.println("Ingrese el GÉNERO (M, F, O): ");
            genero = scanner.next().toUpperCase();  // Capturamos la entrada como String

            // Verificar que la entrada tiene exactamente un carácter
            if (genero.length() != 1) {
                throw new GeneroInvalidoException("Debes ingresar solo un carácter para el género.");
            }

            // Convertimos el String a un carácter para la validación
            char generoChar = genero.charAt(0);

            // Verificar si el carácter es válido
            if (generoChar != 'M' && generoChar != 'F' && generoChar != 'O') {
                throw new GeneroInvalidoException("GÉNERO INVÁLIDO\n");
            } else {
                break;
            }
        }
        return genero;  // Retornar el String que contiene el género válido
    }

    //contraseña entre 6 y 12 caracteres!!
    public String pedirContraseña() {
        boolean confirmado = false;
        int opcion;
        String contraseña = "";

        do {

            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
                continue;  // Vuelve al principio del ciclo si la contraseña no es válida
            } else if (!contraseña.matches(".*\\d.*")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
                continue;
            }

            System.out.println("La contraseña ingresada es: " + contraseña);
            System.out.println("Deseas modificar la contraseña?");
            System.out.println("1. SI deseo");
            System.out.println("2. NO deseo");
            opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 2) {
                System.out.println("Contraseña guardada");
                confirmado = true;
            } else if (opcion != 1 && opcion != 2) {
                System.out.println("No has ingresado una opción correcta");
            }

        } while (!confirmado);

        return contraseña;
    }

    public String pedirContraseñaNueva(String contraseniaVieja) {
        String nuevaContrasenia = "";
        int opcion;

        do {
            System.out.println("La contraseña actual es: " + contraseniaVieja);
            pedirContraseña();

            // Validación: Contraseña no puede ser vacía y debe ser diferente
            if (nuevaContrasenia.equals(contraseniaVieja)) {
                System.out.println("Has ingresado la misma contraseña. Intenta de nuevo.");
            } else if (nuevaContrasenia.isEmpty()) {
                System.out.println("La contraseña no puede estar vacía. Intenta de nuevo.");
            } else if (!nuevaContrasenia.matches(".\\d.")) {//tiene al menos un num?
                System.out.println("La contraseña debe contener al menos un número. Intenta de nuevo.");
            } else if (nuevaContrasenia.length() < 6 || nuevaContrasenia.length() > 12) {
                System.out.println("La contraseña debe tener entre 6 y 12 caracteres. Intenta de nuevo.");
            } else {
                System.out.println("Has establecido la nueva contraseña: " + nuevaContrasenia);
                System.out.println("¿Deseas modificarla de nuevo?");
                System.out.println("1. Sí, deseo modificarla de nuevo.");
                System.out.println("2. No, estoy satisfecho.");

                // Validar entrada del usuario
                while (!scanner.hasNextInt()) {
                    System.out.println("Por favor, selecciona una opción válida (1 o 2):");
                    scanner.next();
                }
                opcion = scanner.nextInt();
                scanner.nextLine();
                if (opcion == 2) {
                    System.out.println("Contraseña definitiva establecida.");
                    break;
                } else if (opcion != 1) {
                    System.out.println("Opción no válida. Intenta de nuevo.");
                }
            }
        } while (true);

        return nuevaContrasenia;
    }

    ///////VERIFICACIONES
    public boolean verificarSiExisteAdministrador(String dni) throws DNInoEncontradoException {
        List<Administrador> aux = leerArchivoAdministradores("administrad.json");
        if (aux == null || aux.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de administradores..");
        }
        for (Persona p : aux) {
            if (p.getDni().equals(dni)) {
                return true;//alguien del archivo tiene ese dni.
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en administradores!!");
    }

    public boolean verificarSiExisteCliente(String dni) throws DNInoEncontradoException {
        List<Cliente> aux = leerArchivoClientes();
        if (aux == null || aux.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de clientes en el archivo especificado.");
        }
        for (Persona p : aux) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en clientes del archivo especificado.");
    }

    public boolean verificarSiExisteRecepcionista(String dni) throws DNInoEncontradoException {
        List<Recepcionista> aux = leerArchivoRecepcionistas();
        if (aux == null || aux.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de recepcionistas en el archivo especificado.");
        }
        for (Persona p : aux) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en recepcionistas del archivo especificado.");
    }

    public boolean verificarSiExisteProfesional(String dni) throws DNInoEncontradoException {
        List<Profesional> aux = leerArchivoProfesionales();
        if (aux == null || aux.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de profesionales en el archivo especificado.");
        }
        for (Persona p : aux) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en profesionales del archivo especificado.");
    }

    /////////////////////////MANEJO DE ARCHIVOS DE PERSONAS.//////////////////////////
    //ADMINISTRADOR
/*
    public void guardarArchivoAdministradores(List<Administrador> administradores) {
        Gson gson = new Gson();

        try (FileWriter fileWriter = new FileWriter("administradores.json")) {
            // Convertir la lista de administradores a formato JSON
            gson.toJson(administradores, fileWriter);
            System.out.println("Archivo guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo: " + e.getMessage());
        }
    }

    public List<Administrador> leerArchivoAdministradores() {
        Gson gson = new Gson();
        List<Administrador> listaAdministradores = new ArrayList<>();

        try (FileReader fileReader = new FileReader("administradores.json")) {
            // Leer el archivo JSON y convertirlo a una lista de administradores
            Type listType = new TypeToken<List<Administrador>>() {
            }.getType();
            listaAdministradores = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + e.getMessage());
        }

        return listaAdministradores;
    }

    ///Secretaria
    public List<Recepcionista> leerArchivoRecepcionistas() {
        Gson gson = new Gson();
        List<Recepcionista> listaRecepcionistas = new ArrayList<>();
        try (FileReader fileReader = new FileReader("recepcionistas.json")) {
            Type listType = new TypeToken<List<Recepcionista>>() {
            }.getType();
            listaRecepcionistas = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + e.getMessage());
        }
        return listaRecepcionistas;
    }

    public void guardarArchivoRecepcionistas(List<Recepcionista> recepcionistas) {
        Gson gson = new Gson();
        try (FileWriter fileWriter = new FileWriter("recepcionistas.json")) {
            gson.toJson(recepcionistas, fileWriter);
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo: " + e.getMessage());
        }
    }

    //profesional
    public List<Profesional> leerArchivoProfesionales() {
        Gson gson = new Gson();
        List<Profesional> listaProfesionales = new ArrayList<>();
        try (FileReader fileReader = new FileReader("profesionales.json")) {
            Type listType = new TypeToken<List<Profesional>>() {
            }.getType();
            listaProfesionales = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + e.getMessage());
        }
        return listaProfesionales;
    }

    public void guardarArchivoProfesionales(List<Profesional> profesionales) {
        Gson gson = new Gson();
        try (FileWriter fileWriter = new FileWriter("profesionales.json")) {
            gson.toJson(profesionales, fileWriter);
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo: " + e.getMessage());
        }
    }

    //Clientes
    public void guardarArchivoClientes(List<Cliente> clientes) {
        Gson gson = new Gson();
        try (FileWriter fileWriter = new FileWriter("clientes.json")) {
            gson.toJson(clientes, fileWriter);
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo: " + e.getMessage());
        }
    }

    public List<Cliente> leerArchivoClientes() {
        Gson gson = new Gson();
        List<Cliente> listaClientes = new ArrayList<>();
        try (FileReader fileReader = new FileReader("clientes.json")) {
            Type listType = new TypeToken<List<Cliente>>() {
            }.getType();
            listaClientes = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + e.getMessage());
        }
        return listaClientes;
    }

   /* public List<Persona> leerArchivoPersona() {
        Gson gson = new Gson();
        List<Persona> listaClientes = new ArrayList<>();
        try (FileReader fileReader = new FileReader("clientes.json")) {
            Type listType = new TypeToken<List<Persona>>() {
            }.getType();
            listaClientes = gson.fromJson(fileReader, listType);
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo: " + e.getMessage());
        }
        return listaClientes;
    }*/

    public List<Persona> leerArchivoPersona(String rutaArchivo) {
        List<Persona> personas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }

            // Convertir el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(contenido.toString());

            // Procesar cada objeto JSON en el array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPersona = jsonArray.getJSONObject(i);

                // Determinar el tipo de persona y crear la instancia adecuada
                String tipo = jsonPersona.getString("tipo");
                Persona persona;

                switch (tipo) {
                    case "Profesional":
                        Profesional profesional = new Profesional(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        // Agregar los códigos de servicio si existen
                        JSONArray codServiciosArray = jsonPersona.optJSONArray("cod_servicios");
                        if (codServiciosArray != null) {
                            for (int j = 0; j < codServiciosArray.length(); j++) {
                                profesional.agregarProfesion(codServiciosArray.getString(j));
                            }
                        }
                        persona = profesional;
                        break;

                    case "Cliente":
                        persona = new Cliente(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono")
                        );
                        break;

                    case "Recepcionista":
                        persona = new Recepcionista(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        break;

                    case "Administrador":
                        persona = new Administrador(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        break;

                    default:
                        throw new IllegalArgumentException("Tipo de Persona desconocido: " + tipo);
                }

                // Agregar la persona a la lista
                personas.add(persona);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return personas;
    }

    // Métodos genéricos para guardar y leer archivos JSON
    private <T> void guardarArchivo(String ruta, T objeto) {
        try (FileWriter fileWriter = new FileWriter(ruta)) {
            gson.toJson(objeto, fileWriter);
            System.out.println("Archivo guardado exitosamente: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + ruta + " - " + e.getMessage());
        }
    }

    private <T> T leerArchivo(String ruta, Type tipo) {
        try (FileReader fileReader = new FileReader(ruta)) {
            return gson.fromJson(fileReader, tipo);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + ruta + " - " + e.getMessage());
        }
        return null;
    }

    // Métodos para Administradores
    public void guardarAdministradores(List<Administrador> administradores) {
        guardarArchivo("administradores.json", administradores);
    }

    public List<Administrador> leerAdministradores() {
        Type listType = new TypeToken<List<Administrador>>() {
        }.getType();
        List<Administrador> lista = leerArchivo("administradores.json", listType);
        return lista != null ? lista : new ArrayList<>();
    }

    // Métodos para Recepcionistas
    public void guardarRecepcionistas(List<Recepcionista> recepcionistas) {
        guardarArchivo("recepcionistas.json", recepcionistas);
    }

    public List<Recepcionista> leerRecepcionistas() {
        Type listType = new TypeToken<List<Recepcionista>>() {
        }.getType();
        List<Recepcionista> lista = leerArchivo("recepcionistas.json", listType);
        return lista != null ? lista : new ArrayList<>();
    }

    // Métodos para Profesionales
    public void guardarProfesionales(List<Profesional> profesionales) {
        guardarArchivo("profesionales.json", profesionales);
    }

    public List<Profesional> leerProfesionales() {
        Type listType = new TypeToken<List<Profesional>>() {
        }.getType();
        List<Profesional> lista = leerArchivo("profesionales.json", listType);
        return lista != null ? lista : new ArrayList<>();
    }

    // Métodos para Clientes
    public void guardarClientes(List<Cliente> clientes) {
        guardarArchivo("clientes.json", clientes);
    }

    public List<Cliente> leerClientes() {
        Type listType = new TypeToken<List<Cliente>>() {
        }.getType();
        List<Cliente> lista = leerArchivo("clientes.json", listType);
        return lista != null ? lista : new ArrayList<>();
    }

    // Métodos para Persona con JSON manual (org.json)
    public void guardarPersonas(List<Persona> personas, String rutaArchivo) {
        JSONArray jsonArray = new JSONArray();

        for (Persona persona : personas) {
            JSONObject jsonPersona = new JSONObject();
            jsonPersona.put("nombre", persona.getNombre());
            jsonPersona.put("apellido", persona.getApellido());
            jsonPersona.put("dni", persona.getDni());
            jsonPersona.put("genero", persona.getGenero());
            jsonPersona.put("telefono", persona.getTelefono());

            if (persona instanceof Profesional) {
                jsonPersona.put("tipo", "Profesional");
                jsonPersona.put("contraseña", ((Profesional) persona).getContraseña());
                jsonPersona.put("profesiones", ((Profesional) persona).getProfesiones());
            } else if (persona instanceof Cliente) {
                jsonPersona.put("tipo", "Cliente");
            } else if (persona instanceof Recepcionista) {
                jsonPersona.put("tipo", "Recepcionista");
                jsonPersona.put("contraseña", ((Recepcionista) persona).getContraseña());
            } else if (persona instanceof Administrador) {
                jsonPersona.put("tipo", "Administrador");
                jsonPersona.put("contraseña", ((Administrador) persona).getContraseña());
            } else {
                throw new IllegalArgumentException("Tipo de Persona desconocido: " + persona.getClass().getSimpleName());
            }

            jsonArray.put(jsonPersona);
        }

        try (FileWriter fileWriter = new FileWriter(rutaArchivo)) {
            fileWriter.write(jsonArray.toString(4)); // Formato indentado
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de personas: " + e.getMessage());
        }
    }

    public List<Persona> leerPersonas(String rutaArchivo) {
        List<Persona> personas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }

            JSONArray jsonArray = new JSONArray(contenido.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPersona = jsonArray.getJSONObject(i);

                String tipo = jsonPersona.getString("tipo");
                Persona persona;
                switch (tipo) {
                    case "Profesional":
                        Profesional profesional = new Profesional(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        JSONArray profesiones = jsonPersona.optJSONArray("profesiones");
                        if (profesiones != null) {
                            for (int j = 0; j < profesiones.length(); j++) {
                                profesional.agregarProfesion(profesiones.getString(j));
                            }
                        }
                        persona = profesional;
                        break;

                    case "Cliente":
                        persona = new Cliente(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono")
                        );
                        break;

                    case "Recepcionista":
                        persona = new Recepcionista(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        break;

                    case "Administrador":
                        persona = new Administrador(
                                jsonPersona.getString("nombre"),
                                jsonPersona.getString("apellido"),
                                jsonPersona.getString("dni"),
                                jsonPersona.getString("genero"),
                                jsonPersona.getString("telefono"),
                                jsonPersona.getString("contraseña")
                        );
                        break;

                    default:
                        throw new IllegalArgumentException("Tipo de Persona desconocido: " + tipo);
                }
                personas.add(persona);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de personas: " + e.getMessage());
        }

        return personas;
    }

    public void guardarArchivoAdministradores(List<Administrador> administradores) {
        Gson gson = new Gson();

        try (FileWriter fileWriter = new FileWriter("administradores.json")) {
            gson.toJson(administradores, fileWriter); // Convertir la lista a JSON
            System.out.println("Archivo de administradores guardado exitosamente.");
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo de administradores: " + e.getMessage());
        }
    }

    public List<Administrador> leerArchivoAdministradores(String nombreArchivo) {
        Administrador[] administradorsArray ;
        List<Administrador> administradoresCargados=null;
        try (FileReader reader = new FileReader(nombreArchivo)) {
             administradorsArray = gson.fromJson(reader, Administrador[].class);
            administradoresCargados = Arrays.asList(administradorsArray);

            for (Administrador administrador : administradoresCargados) {
                almacenPersonas.agregar(administrador);

            }
        } catch (IOException e) {
            System.out.println("Error al cargar los archivos" + e.getMessage());
        }
        return administradoresCargados;
    }


// Recepcionistas
public void guardarArchivoRecepcionistas(List<Recepcionista> recepcionistas) {
    Gson gson = new Gson();

    try (FileWriter fileWriter = new FileWriter("recepcionistas.json")) {
        gson.toJson(recepcionistas, fileWriter); // Convertir la lista a JSON
        System.out.println("Archivo de recepcionistas guardado exitosamente.");
    } catch (IOException e) {
        System.out.println("No se puede guardar el archivo de recepcionistas: " + e.getMessage());
    }
}

public List<Recepcionista> leerArchivoRecepcionistas() {
    Gson gson = new Gson();
    List<Recepcionista> listaRecepcionistas = null;

    try (FileReader fileReader = new FileReader("recepcionistas.json")) {
        Type listType = new TypeToken<List<Recepcionista>>() {
        }.getType();
        listaRecepcionistas = gson.fromJson(fileReader, listType); // Leer y convertir desde JSON
    } catch (FileNotFoundException e) {
        System.out.println("Archivo de recepcionistas no encontrado: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error al leer el archivo de recepcionistas: " + e.getMessage());
    }

    return listaRecepcionistas;
}

// Profesionales
public void guardarArchivoProfesionales(List<Profesional> profesionales) {
    Gson gson = new Gson();

    try (FileWriter fileWriter = new FileWriter("profesionales.json")) {
        gson.toJson(profesionales, fileWriter); // Convertir la lista a JSON
        System.out.println("Archivo de profesionales guardado exitosamente.");
    } catch (IOException e) {
        System.out.println("No se puede guardar el archivo de profesionales: " + e.getMessage());
    }
}

public List<Profesional> leerArchivoProfesionales() {
    Gson gson = new Gson();
    List<Profesional> listaProfesionales = null;

    try (FileReader fileReader = new FileReader("profesionales.json")) {
        Type listType = new TypeToken<List<Profesional>>() {
        }.getType();
        listaProfesionales = gson.fromJson(fileReader, listType); // Leer y convertir desde JSON
    } catch (FileNotFoundException e) {
        System.out.println("Archivo de profesionales no encontrado: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error al leer el archivo de profesionales: " + e.getMessage());
    }

    return listaProfesionales;
}

// Clientes
public void guardarArchivoClientes(List<Cliente> clientes) {
    Gson gson = new Gson();

    try (FileWriter fileWriter = new FileWriter("clientes.json")) {
        gson.toJson(clientes, fileWriter); // Convertir la lista a JSON
        System.out.println("Archivo de clientes guardado exitosamente.");
    } catch (IOException e) {
        System.out.println("No se puede guardar el archivo de clientes: " + e.getMessage());
    }
}

public List<Cliente> leerArchivoClientes() {
    Gson gson = new Gson();
    List<Cliente> listaClientes = null;

    try (FileReader fileReader = new FileReader("clientes.json")) {
        Type listType = new TypeToken<List<Cliente>>() {
        }.getType();
        listaClientes = gson.fromJson(fileReader, listType); // Leer y convertir desde JSON
    } catch (FileNotFoundException e) {
        System.out.println("Archivo de clientes no encontrado: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error al leer el archivo de clientes: " + e.getMessage());
    }

    return listaClientes;
}

// Métodos para Persona (JSON manual)
public void guardarEnArchivoJSON(String rutaArchivo) {
    JSONArray jsonArray = new JSONArray();

    for (Persona persona : almacenPersonas.getAlmacen()) {
        JSONObject jsonPersona = new JSONObject();
        jsonPersona.put("nombre", persona.getNombre());
        jsonPersona.put("apellido", persona.getApellido());
        jsonPersona.put("dni", persona.getDni());
        jsonPersona.put("genero", persona.getGenero());
        jsonPersona.put("telefono", persona.getTelefono());

        if (persona instanceof Profesional) {
            jsonPersona.put("tipo", "Profesional");
            jsonPersona.put("contraseña", ((Profesional) persona).getContraseña());
            jsonPersona.put("profesiones", ((Profesional) persona).getProfesiones());
        } else if (persona instanceof Cliente) {
            jsonPersona.put("tipo", "Cliente");
        } else if (persona instanceof Recepcionista) {
            jsonPersona.put("tipo", "Recepcionista");
            jsonPersona.put("contraseña", ((Recepcionista) persona).getContraseña());
        } else if (persona instanceof Administrador) {
            jsonPersona.put("tipo", "Administrador");
            jsonPersona.put("contraseña", ((Administrador) persona).getContraseña());
        } else {
            throw new IllegalArgumentException("Tipo de Persona desconocido: " + persona.getClass().getSimpleName());
        }

        jsonArray.put(jsonPersona);
    }

    try (FileWriter fileWriter = new FileWriter(rutaArchivo)) {
        fileWriter.write(jsonArray.toString(4));
        System.out.println("Archivo de personas guardado exitosamente.");
    } catch (IOException e) {
        System.out.println("No se puede guardar el archivo de personas: " + e.getMessage());
    }
}

    public List<Persona> getAlmacenPersonas() {
        return almacenPersonas.getAlmacen();
    }

    public void setAlmacenPersonas(List<Persona> almacenPersonass) {
       this.almacenPersonas.setAlmacen(almacenPersonass);
    }

}