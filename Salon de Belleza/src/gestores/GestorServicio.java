package gestores;

import enumeraciones.*;
import model.Depilacion;
import model.Manicura;
import model.Pestanias;
import model.Servicio;

import java.util.Scanner;

public class GestorServicio {

    private static Scanner scanner = new Scanner(System.in);
    private GestorAlmacen<Servicio> almacenServicios = new GestorAlmacen<>();


    public void crearServicio() {

        TipoServicio tipoService = pedirTipoServicio();
        double precio = pedirPrecio();
        double duracion = pedirDuracion();
        boolean disenio = false;

        if (tipoService == TipoServicio.DEPILACION) {
            TipoDepilacion tipoDepilacion = pedirTipoDepilacion();
            Depilacion depilacion = new Depilacion( precio, tipoDepilacion);
            almacenServicios.agregar(depilacion);
            System.out.println(depilacion);
            verificarCarga(depilacion);

        } else if (tipoService == TipoServicio.PESTANIAS) {
            TipoPestanias tipoPestanias = pedirTipoPestanias();
            Pestanias pestanias = new Pestanias( precio, tipoPestanias);
            almacenServicios.agregar(pestanias);
            System.out.println(pestanias);
            verificarCarga(pestanias);

        } else if (tipoService == TipoServicio.MANICURA) {
            TipoManicura tipoManicura = pedirTipoManicura();
            Manicura manicura = new Manicura(precio, tipoManicura);
            almacenServicios.agregar(manicura);
            System.out.println(manicura);
            verificarCarga(manicura);
        }

    }

    public void verificarCarga(Servicio servicio)
    {
    int opcion;
        do {
        System.out.println("¿Deseas modificar algo del servicio?");
        System.out.println("1. Sí");
        System.out.println("2. No");

        opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
            case 1:
                modificarServicio(servicio);
                break;
            case 2:
                System.out.println("....");
                break;
            default:
                System.out.println("Opción no válida, selecciona nuevamente.");
        }
    } while(opcion !=2 &&opcion!=1);
    }

    // Función que permite modificar un servicio existente
    public void modificarServicio(Servicio servicio) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Tipo de servicio");
            System.out.println("2. Precio");
            System.out.println("3. Duración");
            System.out.println("4. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    servicio.setTipoService(pedirTipoServicio());
                    break;
                case 2:
                    servicio.setPrecio(pedirPrecio());
                    break;
                case 3:
                    servicio.setDuracion(pedirDuracion());
                    break;
                case 4:
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
        }
        return precio;
    }

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

    public void mostrarServicios() {

        almacenServicios.mostrar();
    }

    public TipoDepilacion pedirTipoDepilacion() {

        TipoDepilacion tipo = null;

        /// EXEPCION LETRAS INAVLIDA
        int opcion;
        do {
            System.out.println("Selecciona el tipo de depilación:");
            System.out.println("1. Cera");
            System.out.println("2. Láser");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    tipo = TipoDepilacion.CERA;
                    break;
                case 2:
                    tipo = TipoDepilacion.LASER;
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
            }
        } while (opcion != 1 && opcion != 2);

        return tipo;
    }

    public TipoPestanias pedirTipoPestanias() {

        TipoPestanias tipo = null;

        /// EXEPCION LETRAS INAVLIDA
        int opcion;
        do {
            System.out.println("Selecciona el tipo de pestañas:");
            System.out.println("1. Clásicas");
            System.out.println("2. 2D");
            System.out.println("3. 3D");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    tipo = TipoPestanias.CLASICAS;
                    break;
                case 2:
                    tipo = TipoPestanias.DOS_D;
                    break;
                case 3:
                    tipo = TipoPestanias.TRES_D;
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
            }
        } while (opcion != 1 && opcion != 2 && opcion != 3);

        return tipo;
    }


    public TipoManicura pedirTipoManicura() {

        TipoManicura tipo = null;

        /// EXEPCION LETRAS INAVLIDA
        int opcion;
        do {
            System.out.println("Selecciona el tipo de manicura:");
            System.out.println("1. Esculpidas");
            System.out.println("2. Gel");
            System.out.println("3. Semipermanente");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    tipo = TipoManicura.SEMIPERMANENTE;
                    break;
                case 2:
                    tipo = TipoManicura.GEL;
                    break;
                case 3:
                    tipo = TipoManicura.ESCULPIDAS;
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
            }
        } while (opcion != 1 && opcion != 2 && opcion != 3);

        return tipo;
    }

    //atributo UNICO
    // public void eliminarServicio()

}










