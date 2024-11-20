package gestores;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDeProfesional;
import enumeraciones.TipoServicio;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import excepciones.TelefonoInvalidoException;
import model.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
public class GestorPersona {

    private static Scanner scanner = new Scanner(System.in);
    private GestorAlmacen<Persona> almacenPersonas = new GestorAlmacen<>();


    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    //pasamos 1 si es cliente,
    // 2 si es profesional,
    // 3 si es recepcionista,
    // 4 si es administrador
    public boolean agregarPersona(int tipoPersona, GestorServicio servicios) {
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
                verificarCarga(cliente);
                List<Cliente> clientesAux=(Cliente) LeerArchivo("clientes.json");
                clientesAux.add(cliente);
                EscribirPersonasEnArchivo("clientes.json",clientesAux);
                break;
            case 2:
                int opcion = 0;
                Profesional profesional = new Profesional(nombre, apellido, dni, genero, telefono);

                GestorTurno aux = new GestorTurno();
                String e = aux.pedirCodServicio(servicios);
                profesional.agregarProfesion(e);//minimo una profesion.

                do {
                    System.out.println("Deseas agregar otra profesion?");
                    System.out.println("Deseas agregar otra profesion?");
                    System.out.println("1. Si deseo.");
                    System.out.println("2. No deseo.");
                    opcion = scanner.nextInt();
                    scanner.nextLine();

                    if (opcion == 1) {
                        e = aux.pedirCodServicio(servicios);
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
                verificarCarga(profesional);
                ManejoArchivos a = new ManejoArchivos();
                a.EscribirUnProfesional(profesional);
                break;
            case 3:
                Recepcionista recepcionista = new Recepcionista(nombre, apellido, dni, genero, telefono);
                cargado = true;
                if (almacenPersonas.agregar(recepcionista)) {
                    System.out.printf("\nRECEPCIONISTA AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR RECEPCIONISTA\n");
                }
                System.out.println(recepcionista);
                verificarCarga(recepcionista);
                break;
            case 4:
                Administrador administrador = new Administrador(nombre, apellido, dni, genero, telefono);
                cargado = true;
                if (almacenPersonas.agregar(administrador)) {
                    System.out.printf("\nADMINISTRADOR AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR ADMINISTRADOR\n");
                }
                almacenPersonas.agregar(administrador);
                System.out.println(administrador);
                verificarCarga(administrador);
                break;
        }
        return cargado;
    }

    public boolean eliminarPersona(String dni) {
        try {
            Persona p = buscarPersona(dni);
            return almacenPersonas.eliminar(p);
        } catch (DNInoEncontradoException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }

    public Persona buscarPersona(String dni) throws DNInoEncontradoException {
        for (Persona p : almacenPersonas.getAlmacen()) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }

    public void modificarPersona(Persona persona) {
        int opcion;
        boolean continuarModificando = true;

        if (persona instanceof Profesional) {
            modificarProfesional(persona,GestorServicio gestorServicio);
        } else {
            while (continuarModificando) {

                System.out.println("¿Qué te gustaría modificar?");
                System.out.println("1. Nombre");
                System.out.println("2. Apellido");
                System.out.println("3. DNI");
                System.out.println("4. Genero");
                System.out.println("5. Telefono");
                System.out.println("6. Salir");
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
                                System.out.printf(e.getMessage());
                            }
                            break;
                        case 4:
                            try {
                                persona.setGenero(pedirGenero());
                            } catch (GeneroInvalidoException e) {
                                System.out.printf(e.getMessage());
                            }
                            break;
                        case 5:
                            try {
                                persona.setTelefono((pedirTelefono()));
                            } catch (TelefonoInvalidoException e) {
                                System.out.println(e);
                            }
                            break;
                        case 6:
                            continuarModificando = false;
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

    public Profesional modificarProfesional(Profesional profesional,GestorServicio servicios) {
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
            System.out.println("7. Salir");
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
            return profesional;
        }
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public void verificarCarga(Persona persona) {
        int opcion;
        do {
            System.out.println("¿Deseas modificar algo de la persona?");
            System.out.println("1. Sí");
            System.out.println("2. No");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    modificarPersona(persona);
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

    ///////nombre
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

    /////////PASA A MAYUSCULA LOS NOMBRES y apellidos  !
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

    /////////////apellido
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

    public String pedirDNIsinVerificacion()  {
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

    public boolean verificarSiExiste( String dni,String nombreArchivo) throws DNInoEncontradoException {
        List<Persona> aux=LeerArchivo(nombreArchivo);
        if (aux == null || aux.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros en el archivo.");
        }
        for (Persona p : aux) {
            if (p.getDni().equals(dni)) {
                return true;//alguien del archivo tiene ese dni.
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }

    ///////MANEJO DE ARCHIVOS DE PERSONAS.
    public void EscribirPersonasEnArchivo(String nombreArchivo, List<Persona> personas) {
        try(FileWriter fileWriter = new FileWriter(nombreArchivo)){
            Gson gson = new Gson();
            String json = gson.toJson(personas);
            fileWriter.write(json);
           // System.out.println("Archivo escrito correctamente.");

        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Persona> LeerArchivo(String nombreArchivo) {
        try (FileReader fileReader = new FileReader(nombreArchivo)){
            Gson gson = new Gson();
            Type ListaPersonas = new TypeToken<List<Persona>>() {}.getType();
            List<Persona> personas = gson.fromJson(fileReader, ListaPersonas);

            return personas;

        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void guardarPersonaEnArchivo(List<Persona> personas, String nombreArchivo) {
        Gson gson = configurarGsonConHerencia();
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(personas, writer);
            System.out.println("Archivo guardado correctamente: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo JSON: " + e.getMessage());
        }
    }
    /*
    public Gson configurarGsonConHerencia() {
        RuntimeTypeAdapterFactory<Persona> adapter =
                RuntimeTypeAdapterFactory.of(Persona.class, "tipo") // Campo "tipo" identifica la subclase
                        .registerSubtype(Recepcionista.class, "recepcionista")
                        .registerSubtype(Administrador.class, "administrador")
                        .registerSubtype(Cliente.class, "cliente");

        return new GsonBuilder().registerTypeAdapterFactory(adapter).create();
    }
*/
    /*
        public TipoDeProfesional pedirTipoProfesional() {
            int opcion = 0;
            TipoDeProfesional aux = null;
            do {
                System.out.println("Ingrese la profesion que posee este profesional:");
                System.out.println("1. Lashista");
                System.out.println("2. Manicura");
                System.out.println("3. Depiladora");
                opcion = scanner.nextInt();
                if (opcion == 1) {
                    aux = TipoDeProfesional.LASHISTA;
                } else if (opcion == 2) {
                    aux = TipoDeProfesional.MANICURA;
                } else if (opcion == 3) {
                    aux = TipoDeProfesional.DEPILADORA;
                } else {
                    System.out.println("No haz ingresado una opcion correcta, volvamos a intentar.");
                }
            } while (opcion != 1 && opcion != 2 && opcion != 3);
            return aux;
        }
    */
}


