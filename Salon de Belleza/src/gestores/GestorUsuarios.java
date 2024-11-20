package gestores;

import excepciones.DNInoEncontradoException;
import model.Persona;

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
    public void agregarUsuarioDeCero(String dni){
        if(verificarUsuario(dni)){
            System.out.println("Ya existe ese dni. ");
        }else{
            usuarios.put(dni,pedirContraseña());
        }
    }

    public void agregarUsuario(String dni,String contrasenia) {
        if (verificarUsuario(dni)) {
            System.out.println("Ya existe ese dni. ");
        } else {
            usuarios.put(dni, contrasenia);
        }
    }
    //cambia a un usario que ya existe
    public boolean cambiarContraseña(String dni, String nombreArchivo) {
        boolean cambiada = false;
        GestorUsuarios UsuariosAux = new GestorUsuarios();

        UsuariosAux.LeerArchivo(nombreArchivo);

        if (UsuariosAux.getClaves().isEmpty()) {
            System.out.println("No hay usuarios disponibles. Agrega uno.");
            return false;
        }

        for (String clave : UsuariosAux.getClaves()) {
            if (clave.equals(dni)) {
                String nuevaContraseña = pedirContraseñaNueva(clave);
                UsuariosAux.agregarUsuario(clave, nuevaContraseña);
                cambiada = true;
                break;
            }
        }

        if (cambiada) {
            UsuariosAux.EscribirArchivo(nombreArchivo);
            System.out.println("Contraseña cambiada con éxito.");
        } else {
            System.out.println("No se encontró el usuario con el DNI especificado.");
        }
        return cambiada;
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
            } else if (!nuevaContrasenia.matches(".*\\d.*")) {
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

    //para cambiar la contra hay que saber si ya esta en el gestor
    public boolean verificarUsuario( String dni) {
        // Verificar si no hay usuarios
        if (usuarios == null || usuarios.isEmpty()) {
            System.out.println("No hay usuarios disponibles.. ¡Agrega uno!");
            return false;
        }
        for (String clave : this.getClaves()) {
            if (clave.equals(dni)) {
                return true;
            }
        }
        return false;
    }
}














