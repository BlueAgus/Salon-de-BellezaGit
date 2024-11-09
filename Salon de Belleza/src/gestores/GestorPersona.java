package gestores;

import enumeraciones.TipoServicio;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import model.*;

import java.util.Scanner;

public class GestorPersona {

    private static Scanner scanner = new Scanner(System.in);
    private GestorAlmacen<Persona> almacenPersonas=new GestorAlmacen<>();

    //pasamos 1 si es cliente, 2 si es profesional, 3 si es recepcionista, 4 si es administrador
    public boolean agregarPersona(int tipoPersona)
    {
        boolean cargado=false;

        String dni = "";
        String genero= "";

        int telefono=0;
        String nombre=pedirNombre();
        String apellido= pedirApellido();
        telefono=pedirTelefono();

        try{
            dni=pedirDNI();
        }catch (DNIyaCargadoException e)
        {
            System.out.printf(e.getMessage());
        }

        try{
            genero=pedirGenero();
        }catch (GeneroInvalidoException e)
        {
            System.out.printf(e.getMessage());
        }

        switch (tipoPersona){
            case 1:
                Cliente cliente= new Cliente(nombre, apellido, dni, genero, telefono);
                cargado=true;
                almacenPersonas.agregar(cliente);
                break;
            case 2:
                Profesional profesional= new Profesional(nombre,apellido,dni,genero, telefono);
                cargado=true;
                almacenPersonas.agregar(profesional);
                break;
            case 3:
                Recepcionista recepcionista= new Recepcionista(nombre, apellido, dni, genero, telefono);
                cargado=true;
                almacenPersonas.agregar(recepcionista);
                break;
            case 4:
                Administrador administrador= new Administrador(nombre, apellido, dni, genero, telefono);
                cargado=true;
                almacenPersonas.agregar(administrador);
                break;
        }
        return cargado;
    }

    public int pedirTelefono()
    { int telefono;

        ///EXCEPCION POR SI NO SON NUMEROS;
        System.out.println(" Ingrese el telefono: ");
        telefono= scanner.nextInt();
        scanner.nextLine();
    return telefono;
    }

    public String pedirNombre()
    {
        String nombre;
        System.out.println("Ingrese el nombre: ");
        nombre=scanner.nextLine();

        return nombre;
    }

    public String pedirApellido()
    {
        String apellido;
        System.out.println("Ingrese el apellido: ");
        apellido=scanner.nextLine();

        return apellido;
    }

    ///metodo para agregar persona
    public String pedirDNI ()throws DNIyaCargadoException
    {
        String dni;

        System.out.println("Ingrese el DNI: ");
        dni=scanner.nextLine();

        for(Persona a: almacenPersonas.getAlmacen())
            if (a.getDni().equals(dni)) {
                throw new DNIyaCargadoException("DNI ya cargado en el sistema: " + a.toString());
            }

        return dni;
    }

    ///metodo para agregar persona
    public String pedirGenero() throws GeneroInvalidoException {

        String genero;

        System.out.println("Ingrese el GÉNERO (M, F, O): ");
        genero = scanner.next();  // Capturamos la entrada como String

        // Verificar que la entrada tiene exactamente un carácter
        if (genero.length() != 1) {
            throw new GeneroInvalidoException("Debes ingresar solo un carácter para el género.");
        }

        // Convertimos el String a un carácter para la validación
        char generoChar = genero.charAt(0);

        // Verificar si el carácter es válido
        if (generoChar != 'M' && generoChar != 'F' && generoChar != 'O') {
            throw new GeneroInvalidoException("GÉNERO INVÁLIDO");
        }

        return genero;  // Retornar el String que contiene el género válido

    }

    public boolean eliminarPersona(String dni)
    {
        try {
            Persona p= buscarPersona(dni);
            return almacenPersonas.eliminar(p);
        }catch (DNInoEncontradoException e)
        {
            System.out.printf(e.getMessage());
        }
        return false;
    }


    public Persona buscarPersona(String dni)throws DNInoEncontradoException
    {
        for(Persona p: almacenPersonas.getAlmacen())
        {
            if(p.getDni().equals(dni))
            {
                return p;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }


    public void modificarPersona(Persona persona)
    {
        int opcion;

        boolean continuarModificando = true;

        while (continuarModificando) {

            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. Genero");
            System.out.println("5. Salir");
            opcion= scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    persona.setNombre(pedirNombre());
                    break;
                case 2:
                    persona.setApellido(pedirApellido());
                    break;
                case 3:
                    try{
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
                    continuarModificando = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        System.out.println("MODIFICADO EXITOSAMENTE!");
        System.out.println(persona.toString());


    }





}
