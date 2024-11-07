package gestores;

import enumeraciones.*;
import model.Depilacion;
import model.Pestanias;
import model.Servicio;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.util.Scanner;

public class GestorServicio {

    private static Scanner scanner = new Scanner(System.in);
    private AlmacenGenerico<Servicio> almacenServicios= new AlmacenGenerico<>();

    public Servicio crearServicio() {

        TipoServicio tipoService = pedirTipoServicio();
        double precio = pedirPrecio();
        double duracion = pedirDuracion();
        Disponibilidad disponibilidad = pedirDisponibilidad();

        Servicio servicio = new Servicio(tipoService, precio, duracion, disponibilidad);
        System.out.println("Servicio cargado exitosamente:");
        System.out.println(servicio);

        System.out.println("¿Deseas modificar algo del servicio?");
        System.out.println("1. Sí");
        System.out.println("2. No");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        if (opcion == 1) {
            modificarServicio(servicio);
        }

        if(tipoService == TipoServicio.DEPILACION)
        {
            TipoDepilacion tipoDepilacion= pedirTipoDepilacion();
            Depilacion depilacion= new Depilacion(tipoService,precio,duracion,disponibilidad,tipoDepilacion);
            almacenServicios.agregar(depilacion);
        }
        else if ( tipoService== TipoServicio.PESTANIAS )
        {
            TipoPestanias tipoPestanias=pedirTipoPestanias();
            Pestanias pestanias= new Pestanias(tipoService,precio,duracion,disponibilidad,tipoPestanias);
            almacenServicios.agregar(pestanias);
        }
        else if (tipoService== TipoServicio.MANICURA)
        {

        }

        return servicio;
    }

    // Función que permite modificar un servicio existente
    public void modificarServicio(Servicio servicio) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Tipo de servicio");
            System.out.println("2. Precio");
            System.out.println("3. Duración");
            System.out.println("4. Disponibilidad");
            System.out.println("5. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    servicio.setTipoService( pedirTipoServicio() );
                    break;
                case 2:
                    servicio.setPrecio( pedirPrecio() ) ;
                    break;
                case 3:
                    servicio.setDuracion( pedirDuracion() );
                    break;
                case 4:
                    servicio.setDisponibilidad( pedirDisponibilidad() );
                    break;
                case 5:
                    continuarModificando = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println("Servicio modificado:");
            System.out.println(servicio);
        }
    }


    // Validación para el tipo de servicio
    private TipoServicio pedirTipoServicio() {

        TipoServicio tipo = null;
        while (tipo == null) {
            System.out.println("Selecciona el tipo de servicio:");
            System.out.println("1. Uñas");
            System.out.println("2. Pestañas");
            System.out.println("3. Depilación");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    tipo = TipoServicio.MANICURA;
                    break;
                case 2:
                    tipo = TipoServicio.PESTANIAS;
                    break;
                case 3:
                    tipo = TipoServicio.DEPILACION;
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
            }
        }
        return tipo;
    }

    // Validación del precio
    private double pedirPrecio() {
        double precio = 0;
        while (precio <= 0) {
            System.out.print("Introduce el precio del servicio: ");
            precio = scanner.nextDouble();
            scanner.nextLine();

            if (precio <= 0) {
                System.out.println("El precio debe ser mayor a 0.");
            }
        } return precio;
    }

    // Validación de duración
    private double pedirDuracion() {
        double duracion = 0;
        while (duracion <= 0) {
            System.out.print("Introduce la duración del servicio en minutos: ");
            duracion = scanner.nextDouble();
            scanner.nextLine();

            if (duracion <= 0) {
                System.out.println("La duración debe ser mayor a 0.");
            }
        } return duracion;
    }

    // Pedir disponibilidad
    private Disponibilidad pedirDisponibilidad() {

        Disponibilidad disponibilidad = null;
        while (disponibilidad == null) {
            System.out.println("Selecciona la disponibilidad del servicio:");
            System.out.println("1. Disponible");
            System.out.println("2. En Mantenimiento");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    disponibilidad = Disponibilidad.DISPONIBLE;
                    break;
                case 2:
                    disponibilidad = Disponibilidad.MANTENIMIENTO;
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
            }
        }
        return disponibilidad;
    }

    public void mostrarServicios() {

        almacenServicios.mostrar();
    }

    public TipoDepilacion pedirTipoDepilacion() {

        Scanner scanner = new Scanner(System.in);

        // Opciones para el tipo de depilación
        System.out.println("Selecciona el tipo de depilación:");
        System.out.println("1. Cera");
        System.out.println("2. Láser");

        // Pedir al usuario una opción
        int opcion = -1;  // Inicializamos con un valor no válido
        boolean opcionValida = false;

        // Bucle hasta que el usuario ingrese una opción válida
        while (!opcionValida) {
            System.out.print("Ingresa el número de la opción: ");

            // Comprobar si la entrada es un número entero
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                // Verificar si la opción está en el rango válido
                if (opcion >= 1 && opcion <= 2) {

                    opcionValida = true;// Opción válida, salimos del bucle

                } else {
                    System.out.println("Opción no válida. Por favor, selecciona un número entre 1 y 2.");
                }
            } else {
                // Si no es un número, mostrar un mensaje de error
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.nextLine();
            }
        }

        // Devolver el tipo de depilación correspondiente según la opción seleccionada
        switch (opcion) {
            case 1:
                return TipoDepilacion.CERA;
            case 2:
                return TipoDepilacion.LASER;
            default:
        }
    }

    public TipoPestanias pedirTipoPestanias() {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        boolean opcionValida = false;

        System.out.println("Selecciona el tipo de pestañas:");
        System.out.println("1. Clásicas");
        System.out.println("2. 2D");
        System.out.println("3. 3D");

        // Bucle hasta que el usuario ingrese una opción válida
        while (!opcionValida) {

            System.out.print("Ingresa el número de la opción: ");

            // Verificar si la entrada es un número entero
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                // Verificar si la opción está en el rango válido
                if (opcion >= 1 && opcion <= 3) {

                    opcionValida = true;

                } else {
                    System.out.println("Opción no válida. Por favor, selecciona un número entre 1 y 3.");
                }
            } else {
                // Si no es un número, mostrar un mensaje de error
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.nextLine();  // Limpiar el buffer para seguir pidiendo
            }
        }

        // Devolver el tipo de pestañas correspondiente según la opción seleccionada
        switch (opcion) {
            case 1: return TipoPestanias.CLASICAS;
            case 2: return TipoPestanias.DOS_D;  // 2D
            case 3: return TipoPestanias.TRES_D; // 3D
            default:

        }
    }
    public TipoManicura pedirTipoManicura() {
        Scanner scanner = new Scanner(System.in);

        // Mostrar opciones para el tipo de manicura
        System.out.println("Selecciona el tipo de manicura:");
        System.out.println("1. Esculpidas");
        System.out.println("2. Gel");
        System.out.println("3. Semipermanente");

        // Pedir al usuario que ingrese la opción
        int opcion = -1;
        boolean opcionValida = false;

        // Bucle hasta que el usuario ingrese una opción válida
        while (!opcionValida) {
            System.out.print("Ingresa el número de la opción: ");

            // Verificar si la entrada es un número entero
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();

                // Verificar si la opción está en el rango válido
                if (opcion >= 1 && opcion <= 3) {

                    opcionValida = true;

                } else {
                    System.out.println("Opción no válida. Por favor, selecciona un número entre 1 y 3.");
                }
            } else {
                // Si no es un número, mostrar un mensaje de error
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                scanner.nextLine();  // Limpiar el buffer para seguir pidiendo
            }
        }

        // Devolver el tipo de manicura correspondiente según la opción seleccionada
        switch (opcion) {
            case 1: return TipoManicura.ESCULPIDAS;
            case 2: return TipoManicura.GEL;
            case 3: return TipoManicura.SEMIPERMANENTE;
            default:

        }
    }

}





