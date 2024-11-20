package gestores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import excepciones.DNInoEncontradoException;
import model.Persona;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GestorUsuarios {

    private HashMap<String, String> usuarios = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    public Set<String> getClaves() {
        return usuarios.keySet();
    }

    public String obtenerUsuario(String dni) {
        return usuarios.get(dni);
    }

    public boolean isEmpty() {
        return usuarios.isEmpty();
    }

    public String getValor(String key) {
        return usuarios.get(key);
    }
///////////////////////////////////////////////METODOS COOL

    public void agregarUsuarioDeCero(String dni,String nombreArchivo){
        if(verificarUsuario(dni,nombreArchivo)){//se fija si existe ese usuario en el archivo.
            System.out.println("Ya existe ese usuario!. ");
        }else{
            HashMap<String,String> aux=LeerArchivoUsuarios(nombreArchivo);
            aux.put(dni,pedirContraseña());//guarda y pide la contra
            guardarEnArchivoUsuarios(nombreArchivo);//guarda en archi
        }
    }

    public void agregarUsuario(String dni,String nombreArchivo,String contrasenia) {
        if (verificarUsuario(dni,nombreArchivo)) {
            System.out.println("Ya existe ese usuario... ");
        } else {
            usuarios.put(dni, contrasenia);
        }
        guardarEnArchivoUsuarios(nombreArchivo);//pisa con el nuevo agregado
    }
    //cambia a un usario que ya existe
    public boolean cambiarContraseña(String dni, String nombreArchivo) {
        boolean cambiada = false;
        GestorUsuarios UsuariosAux = new GestorUsuarios();

        HashMap<String,String> aux=LeerArchivoUsuarios(nombreArchivo);

        if (UsuariosAux.getClaves().isEmpty()) {
            System.out.println("No hay usuarios disponibles. Agrega uno.");
            return false;
        }

        for (String clave : UsuariosAux.getClaves()) {
            if (clave.equals(dni)) {
                String nuevaContraseña = pedirContraseñaNueva(clave);
                UsuariosAux.agregarUsuario(clave,nombreArchivo, nuevaContraseña);
                cambiada = true;
                break;
            }
        }
        if (cambiada) {
            UsuariosAux.guardarEnArchivoUsuarios(nombreArchivo);
            System.out.println("Contraseña cambiada con éxito.");
        } else {
            System.out.println("No se encontró el usuario con el DNI especificado.");
        }
        return cambiada;
    }

    public boolean primerIngreso(){
        HashMap<String,String> aux=LeerArchivoUsuarios("usuariosadministradores.json");
        for(String valor : aux.values()){
            if(valor.equals("zapallo"))
            {
                return true;
            }
        }
        return false;
    }

    public boolean eliminarUsuario(String dni, String nombreArchivo){
        if(verificarUsuario(dni,nombreArchivo)==true){
          HashMap<String,String> aux=LeerArchivoUsuarios(nombreArchivo);
          aux.remove(dni);
          guardarEnArchivoUsuarios(nombreArchivo);
          return true;
        }else{
            System.out.println("Ese usuario no existe ! ");
            return false;
        }
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
            } else if (!nuevaContrasenia.matches(".*\\d.*")) {//tiene al menos un num?
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
                opcion = scanner.nextInt();scanner.nextLine();
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

    public String pedirContraseña() {
        String contraseña = "";
        do {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
            } else if (!contraseña.matches(".*\\d.*")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
            }
        } while (contraseña.length() < 6 || contraseña.length() > 12 || !contraseña.matches(".*\\d.*")); // Bucle sigue hasta que la contraseña sea válida

        return contraseña;
    }

    //para cambiar la contra hay que saber si ya esta en el archi primero
    public boolean verificarUsuario( String dni,String nombreArchivo) {
        HashMap<String,String> aux=LeerArchivoUsuarios(nombreArchivo);
        // Verificar si no hay usuarios
        if (aux == null || aux.isEmpty()) {
            System.out.println("No hay usuarios disponibles.. ¡Agrega uno!");
            return false;
        }
        for (String clave : aux.keySet()) {
            if (clave.equals(dni)) {
                return true;//eso quiere decir que ya existe este usuario, y tiee su contrasenia
            }
        }
        return false;
    }

    public void guardarEnArchivoUsuarios(String nombreArchivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();//esto es para que se lea mejor
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(usuarios, writer);
            System.out.println("Usuarios guardados en el archivo JSON: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo JSON: " + e.getMessage());
        }
    }

    public HashMap<String,String> LeerArchivoUsuarios(String nombreArchivo) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(nombreArchivo)) {
            Type tipoMapa = new TypeToken<HashMap<String, String>>() {}.getType();
            usuarios = gson.fromJson(reader, tipoMapa);
            return usuarios;
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo JSON: " + e.getMessage());
            return new HashMap<>();//devuelve vacio !!!
        }
    }
}














