package gestores;


import com.google.gson.reflect.TypeToken;
import enumeraciones.*;
import excepciones.CodigoNoEncontradoException;
import model.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.InputMismatchException;

import java.util.Scanner;

public class GestorServicio {

    private static Scanner scanner = new Scanner(System.in);
    public GestorAlmacen<Servicio> listaServicios = new GestorAlmacen<>();
    public static final String archivoServicios = "servicios.json";

    public GestorServicio() {

        this.listaServicios = cargarServiciosDesdeArchivo();

        if (this.listaServicios == null) {
            this.listaServicios = new GestorAlmacen<>();
        }
    }
    ////////////////////////////////////////////////////////jesonA ////////////////////////////////////////////////////

    // Guardar turnos en archivo
    public void guardarServiciosEnArchivo() {
        GestorJson.guardarEnArchivo(archivoServicios, listaServicios);
    }

    // Cargar servicios desde archivo
   private GestorAlmacen<Servicio> cargarServiciosDesdeArchivo() {
        Type type = new TypeToken<GestorAlmacen<Servicio>>() {}.getType();
        return GestorJson.cargarDeArchivoType(archivoServicios, type);
        //usamos el que pide Type en caso de que usemos gestor generico
    }

   /* private GestorAlmacen<Servicio> cargarServiciosDesdeArchivo() {
        Type type = new TypeToken<GestorAlmacen<Servicio>>() {}.getType();
        GestorAlmacen<Servicio> almacen = GestorJson.cargarDeArchivoType(archivoServicios, type);

        if (almacen == null) {
            System.out.println("Creando un archivo nuevo: " + archivoServicios);
            guardarServiciosEnArchivo(); // Crear un archivo vacío
            almacen = new GestorAlmacen<>();
        }

        return almacen;
    }*/

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public void agregarServicio() {

        TipoServicio tipoService = pedirTipoServicio();
        double precio = pedirPrecio();
        LocalTime duracion = pedirDuracion();

        if (tipoService == TipoServicio.DEPILACION) {
            TipoDepilacion tipoDepilacion = pedirTipoDepilacion();
            Depilacion depilacion = new Depilacion(duracion, tipoDepilacion);
            GestorPrecios.modificarPrecio(Depilacion.class, tipoDepilacion, precio);
            listaServicios.agregar(depilacion);
            System.out.println(depilacion);
            verificarCarga(depilacion);

        } else if (tipoService == TipoServicio.PESTANIAS) {
            TipoPestanias tipoPestanias = pedirTipoPestanias();
            Pestanias pestanias = new Pestanias(duracion, tipoPestanias);
            GestorPrecios.modificarPrecio(Pestanias.class, tipoPestanias, precio); // ponemos el precio en el gestor
            listaServicios.agregar(pestanias);
            System.out.println(pestanias);
            verificarCarga(pestanias);

        } else if (tipoService == TipoServicio.MANICURA) {
            boolean disenio = pedirDisenio();
            TipoManicura tipoManicura = pedirTipoManicura();
            Manicura manicura = new Manicura(duracion, tipoManicura);
            manicura.setDisenio(disenio);
            GestorPrecios.modificarPrecio(Manicura.class, tipoManicura, precio);
            listaServicios.agregar(manicura);
            System.out.println(manicura);
            verificarCarga(manicura);
        }
    }

    public boolean eliminarServicio() {

        String cod_servicio = pedirParaBuscar();

        for (Servicio servicio : listaServicios.getAlmacen()) {
            if (servicio.getCodigo_servicio().equals(cod_servicio)) {
                return listaServicios.eliminar(servicio);
            }
        }
        return false;
    }

    public Servicio buscarServicio() throws CodigoNoEncontradoException {

        mostrarServicios();

        while (true) {
            System.out.println("Ingrese el código('salir' si quiere cancelar la operacion): ");
            String cod_Servicio = scanner.nextLine();

            if (cod_Servicio.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }


            for (Servicio s : listaServicios.getAlmacen()) {
                if (s.getCodigo_servicio().equals(cod_Servicio)) {
                    return s;
                }
            }
            throw new CodigoNoEncontradoException("El código de servicio no existe: " + cod_Servicio);
        }

    }

    public Servicio buscarServicioCodigo(String codServicio)throws CodigoNoEncontradoException {
        Servicio servicio = null;
        for (Servicio s : listaServicios.getAlmacen()) {
            if (s.getCodigo_servicio().equals(codServicio)) {
                servicio = s;
            }
        }
        if(servicio == null){
            throw new CodigoNoEncontradoException("El codigo ingresado no existe..");
        }
        return servicio;
    }

    public void mostrarServicioXtipo() {
        TipoServicio tipoServicio = pedirTipoServicio();
        for (Servicio s : listaServicios.getAlmacen()) {
            if (s.getTipoService().equals(tipoServicio)) {
                System.out.println(s.toString());
            }
        }
    }

    // Función que permite modificar un servicio existente
    public void modificarServicio() {

        Servicio servicio = null;
        try {
            servicio = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }

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

    public void modificarServicioParametro(Servicio servicio) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Tipo de servicio");
            System.out.println("2. Duración");
            System.out.println("0. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    servicio.setTipoService(pedirTipoServicio());
                    break;
                case 2:
                    servicio.setDuracion(pedirDuracion());
                    break;
                case 3:
                    continuarModificando = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println("Servicio modificado:");
            System.out.println(servicio);
        }
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public void verificarCarga(Servicio servicio) {
        int opcion = -1; // Valor inicial inválido
        do {
            try {
                System.out.println("¿Deseas modificar algo del servicio?");
                System.out.println("1. Sí");
                System.out.println("2. No");

                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpia el buffer

                switch (opcion) {
                    case 1:
                        modificarServicioParametro(servicio);
                        break;
                    case 2:
                        System.out.println("No se realizarán cambios.");
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, introduce un número (1 o 2).");
                scanner.nextLine(); // Limpia el buffer para evitar un bucle infinito
            }
        } while (opcion != 2);
    }

    public String pedirParaBuscar() {

        for (int i = 0; i < listaServicios.getAlmacen().size(); i++) {
            System.out.println(i + "- " + listaServicios.getAlmacen().get(i));
        }
        int opc = 0;
        while (true) {

            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();


                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > listaServicios.getAlmacen().size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
        return listaServicios.getAlmacen().get(opc).getCodigo_servicio();
    }

    // Validación para el tipo de servicio
    public TipoServicio pedirTipoServicio() {

        TipoServicio tipo = null;
        while (tipo == null) {
            System.out.println("Selecciona el tipo de servicio:");
            System.out.println("1. Uñas");
            System.out.println("2. Pestañas");
            System.out.println("3. Depilación");
            int opcion = scanner.nextInt();
            scanner.nextLine();

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
            try {
                System.out.print("Introduce el precio del servicio: ");
                precio = scanner.nextDouble();
                scanner.nextLine();

                if (precio <= 0) {
                    System.out.println("El precio debe ser mayor a 0.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Solo es posible ingresar numeros");
                scanner.nextLine();
            }
        }
        return precio;
    }

    private LocalTime pedirDuracion() {
        int h = -1;
        int m = -1;
        while (m < 0 || m > 59 || h > 23 || h < 0) {
            System.out.print("Introduce las horas que durara el servicio (0-23):");
            h = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Introduce los minutos que durara el servicio (0-59) ");
            m = scanner.nextInt();
            scanner.nextLine();
            if (m < 0 || m > 59 || h > 23 || h < 0) {
                System.out.println("La hora no es valida ! Volvamos a cargarla. ");
            }
        }
        LocalTime duracion = LocalTime.of(h, m);
        return duracion;
    }

    public TipoDepilacion pedirTipoDepilacion() {

        TipoDepilacion tipo = null;
        try {
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

        } catch (InputMismatchException a) {

            System.out.println("Opción no válida, selecciona nuevamenteeee");
            scanner.nextLine();
        }
        return tipo;
    }

    public TipoPestanias pedirTipoPestanias() {

        TipoPestanias tipo = null;

        try {
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
        } catch (InputMismatchException a) {
            System.out.println("Opción no válida, selecciona nuevamenteee");
            scanner.nextLine();
        }
        return tipo;
    }

    public TipoManicura pedirTipoManicura() {
        TipoManicura tipo = null;

        try {
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
        } catch (InputMismatchException a) {
            System.out.println("Opción no válida, selecciona nuevamente.");
            scanner.nextLine();
        }
        return tipo;
    }

    public boolean pedirDisenio() {
        int opcion = 0;
        boolean disenio = false;

        do {
            try {
                System.out.println("Desea agregar un diseño al servicio? El valor es .. "+ GestorPrecios.getPrecioDisenio());
                System.out.println("1. Si");
                System.out.println("2. No");
                opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion != 1 && opcion != 2) {
                    System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                }

            } catch (InputMismatchException a) {
                System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            disenio = true;
        } else {
            disenio = false;
        }
        return disenio;
    }


    public void reportarFalla(GestorCliente cliente, GestorTurno gestorTurno) {///
        Servicio servicio = null;
        try {
            servicio = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
        String ahora = Turno.convertirLocalDateAString(LocalDate.now());
        gestorTurno.cancelarTurnosXdia(ahora, cliente, servicio.getCodigo_servicio());
    }

   ////////////////////////////////////////GET ////////////////////////////////////////////////////

    public GestorAlmacen<Servicio> getServicios() {
        return listaServicios;
    }

    public void mostrarServicios() {
        listaServicios.mostrar();
    }


}










