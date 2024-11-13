package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoServicio;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import model.*;

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
            genero = pedirGenero();
        }catch (GeneroInvalidoException e)
        {
            System.out.printf(e.getMessage());
        }

        switch (tipoPersona){
            case 1:
                Cliente cliente= new Cliente(nombre, apellido, dni, genero, telefono);
                cargado=true;
                if (almacenPersonas.agregar(cliente))
                {
                    System.out.printf("\nCLIENTE AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR CLIENTE\n");
                }
                System.out.println(cliente);
                verificarCarga(cliente);

                break;
            case 2:
                Profesional profesional= new Profesional(nombre,apellido,dni,genero, telefono);
                cargado=true;
                if (almacenPersonas.agregar(profesional))
                {
                    System.out.printf("\n PROFESIONAL AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR PROFESIONAL\n");
                }
                System.out.println(profesional);
                verificarCarga(profesional);
                ManejoArchivos a=new ManejoArchivos();
                a.EscribirUnProfesional(profesional);
                break;
            case 3:
                Recepcionista recepcionista= new Recepcionista(nombre, apellido, dni, genero, telefono);
                cargado=true;
                if (almacenPersonas.agregar(recepcionista))
                {
                    System.out.printf("\nRECEPCIONISTA AGREGADO EXITOSAMENTE \n");
                } else {
                    System.out.printf("\nERROR AL AGREGAR RECEPCIONISTA\n");
                }
                System.out.println(recepcionista);
                verificarCarga(recepcionista);
                break;
            case 4:
                Administrador administrador= new Administrador(nombre, apellido, dni, genero, telefono);
                cargado=true;
                if (almacenPersonas.agregar(administrador))
                {
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

    public void verificarCarga(Persona persona)
    {
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
            }
        } while(opcion !=2 &&opcion!=1);
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

    public String pedirGenero() throws GeneroInvalidoException{

        String genero;

        while(true) {
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
                throw new GeneroInvalidoException("GÉNERO INVÁLIDO");
            } else {
                break;
            }
        }
        return genero;  // Retornar el String que contiene el género válido
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

    public List<Persona> getAlmacenPersonas() {
        return getAlmacenPersonas();
    }

/*
    public void ActualizarArchivo(String nombreArchivo,List<T>){
        try{
            FileReader fileReader = new FileReader(nombreArchivo);
            Gson gson = new Gson();
            Type tipoListaProfesionales = new TypeToken<List<Profesional>>();
            fileReader.close();
        }catch (JsonSyntaxException e) {
                System.out.println(e.getMessage());
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

    }

    public List<Profesional> LeerArchivo(String nombreArchivo) {
        try {
            FileReader fileReader = new FileReader(nombreArchivo);
            Gson gson = new Gson();
            Type tipoListaProfesionales = new TypeToken<List<Profesional>>() {
            }.getType();
            List<Profesional> profesionales = gson.fromJson(fileReader, tipoListaProfesionales);

            return profesionales;
        }*/


}
