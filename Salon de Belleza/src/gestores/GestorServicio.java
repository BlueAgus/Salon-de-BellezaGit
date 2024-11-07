package gestores;

import enumeraciones.Disponibilidad;
import enumeraciones.TipoServicio;
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

        almacenServicios.agregar(servicio);

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
}





