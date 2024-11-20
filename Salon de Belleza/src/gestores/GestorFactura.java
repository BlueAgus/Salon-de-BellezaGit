package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDePago;
import enumeraciones.TipoManicura;
import excepciones.*;
import model.Cliente;
import model.Factura;
import model.Persona;
import model.Turno;

import javax.xml.transform.Source;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GestorFactura {

    private GestorAlmacen<Factura> historial;
    Gson gson;
    /// donde pondriamos el nombre del archivo?
    private final String nombreArchivoGson;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public GestorFactura(String nombreArchivoGson) {

        this.historial = new GestorAlmacen<>();
        this.gson = new Gson();
        this.nombreArchivoGson = nombreArchivoGson;
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    private Cliente pedirCliente(String dni) throws DNInoEncontradoException {//propago la excepcion
        GestorPersona persona = new GestorPersona();

        return (Cliente) persona.buscarPersona(dni);

    }

    public void crearFactura() {
        GestorServicio gestor = new GestorServicio();
        GestorTurno turnos = new GestorTurno();

        try {
            Scanner scan = new Scanner(System.in);
            //pedir cliente
            System.out.println("Ingrese el DNI del cliente:");
            String dni = scan.nextLine();
            Cliente cliente = pedirCliente(dni);

            //pedir tipo de pago
            TipoDePago tipo = pedirTipoPago();
            Factura factura = new Factura(tipo, cliente, gestor);

            //mostrarle los turnos del dia de la fecha y los proximos
            System.out.println("Estos son los turnos recientes y proximos del cliente: ");
            List<Turno> turnosCliente = turnos.buscarTurnosXdniClienteVigentes(dni);

            if (turnosCliente.isEmpty()) {
                System.out.println("No hay turnos reservados para este cliente.");
                return;
            }
            System.out.println(turnosCliente);

            //Pedimos que turnos se quieren pagar
            boolean continuar = true;
            while (continuar) {

                System.out.println("Ingrese el número del turno a pagar: ");
                int nroTurno = scan.nextInt();
                Turno turnoSeleccionado = turnosCliente.get(nroTurno - 1);

                if (!factura.getTurnosPorCliente().contains(turnoSeleccionado)) {
                    factura.agregarTurno(turnoSeleccionado);
                    System.out.println("El turno se agregó correctamente a la factura.");
                }

                System.out.println("¿Desea agregar otro turno? (SI/NO)");
                String rta = scan.next().toLowerCase();
                continuar = rta.equals("si");
            }
            //aplicar descuento
            System.out.println("Aplicar descuento:(Si/NO)");
            String desc = scan.nextLine().toLowerCase();
            if (desc == "si") {
                System.out.println("Ingrese el porcentaje del descuento: ");
                double percen = scan.nextDouble();
                GestorPrecios.aplicarDescuento(factura.getCodigoFactura(), percen, historial.getAlmacen());

            }

            // montrar factura y agregarla
            boolean seCargo = agregarFactura(factura);
            if (seCargo) {
                System.out.println("La factura fue ingresada correctamente, aqui tiene los detalles:");
                System.out.println(factura);
            }

        } catch (CodigoNoEncontradoException | TurnoExistenteException | DNInoEncontradoException |
                 FacturaYaExistenteException e) {
            e.getMessage();
        }
    }

    private TipoDePago pedirTipoPago() {
        TipoDePago tipo = null;
        Scanner scan = new Scanner(System.in);

        while (tipo == null) {
            System.out.println("Seleccione el tipo de pago:");
            TipoDePago[] opciones = TipoDePago.values();
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            int opcion = scan.nextInt();
            scan.nextLine(); // Limpiar buffer
            if (opcion > 0 && opcion <= opciones.length) {
                tipo = opciones[opcion - 1];
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        return tipo;
    }

    ///preguntar de quien es la factura a modificar
    public void modificarFactura() throws CodigoNoEncontradoException {
        GestorTurno turnos = new GestorTurno();
        Scanner scan = new Scanner(System.in);

        System.out.println("Ingrese el codigo de factura:");
        String codigo = scan.nextLine();

        Factura facturaModificada = buscarFacturaPorCodigo(codigo);

        if (facturaModificada == null) {
            throw new CodigoNoEncontradoException("El codigo ingresado no existe, ingrese de nuevo.");
            //poner bucle para que intente de nuevo
        }
        ///Elegir que se modifica
        int opcion;
        do {
            System.out.println("Ingrese una opcion...");
            System.out.println("1. Tipo de pago -");
            System.out.println("2. Modificar el cliente -");
            System.out.println("3. Turnos declarados en la factura-");
            System.out.println("4. Aplicar descuento-"); //Esto puede ser en algun descuento especial o flasheo yo
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = scan.nextInt();
            scan.nextLine();

            switch (opcion) {
                case 1:
                    TipoDePago tipo = pedirTipoPago();
                    facturaModificada.setTipoPago(tipo);
                    break;
                case 2:
                    try {
                        System.out.println("Ingrese el DNI del cliente: ");
                        String dni = scan.nextLine();
                        Cliente cliente = pedirCliente(dni);
                        facturaModificada.setCliente(cliente);
                    } catch (DNInoEncontradoException e)
                    {
                        System.out.println(e.getMessage());
                    }

                    break;
                case 3:
                    try {
                        int nro = 0;
                        System.out.println("Turnos a pagar/pagos del cliente " + facturaModificada.getCliente().getNombre());
                        System.out.println("------------------------------");
                        System.out.println((nro + 1) + " " + facturaModificada.getTurnosPorCliente());// la idea es que numere los turnos
                        System.out.println("------------------------------");
                        System.out.println("Ingrese un numero de la lista para eliminar o cero(0) para agregar un turno mas:");
                        int rta = scan.nextInt();
                        scan.nextLine();
                        if (rta > 0 && rta < facturaModificada.getTurnosPorCliente().size()) {
                            Turno turnoSeleccionado = facturaModificada.getTurnosPorCliente().get(rta - 1);
                            facturaModificada.eliminarTurno(turnoSeleccionado);
                        }
                        if (rta == 0) {
                            System.out.println("Ingrese el codigo del turno: ");
                            String codTurno = scan.nextLine();
                            Turno turnoNuevo = turnos.buscarTurno(codTurno);

                            facturaModificada.agregarTurno(turnoNuevo);

                        }

                    } catch (TurnoExistenteException | TurnoNoExistenteException | FacturaSinTurnosException |
                             CodigoNoEncontradoException e) {
                        e.getMessage();
                    }

                    break;
                case 4:
                    System.out.println("Precio final actual " + facturaModificada.getPrecioFinal());
                    // metodo de descuento en gestor precios
                    System.out.println("Ingrese el porcentaje de descuento");
                    double desc = scan.nextDouble();
                    GestorPrecios.aplicarDescuento(facturaModificada.getCodigoFactura(), desc, historial.getAlmacen());
                    //por que le paso la lista si ya se que factura es?? arreglar
                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }


    private Factura buscarFacturaPorCodigo(String codigo) {

        for (Factura factu : historial.getAlmacen()) {

            if (factu.getCodigoFactura().equals(codigo)) {
                return factu;
            }
        }
        return null;
    }


    public boolean agregarFactura(Factura factura) throws FacturaYaExistenteException {
        for (Factura f : historial.getAlmacen()) {
            if (f.getCodigoFactura().equals(factura.getCodigoFactura())) {
                throw new FacturaYaExistenteException("La factura ya existe en el historial.");
            }
        }
        return historial.agregar(factura);
    }


    public boolean eliminarFactura(String codigoFactura) throws FacturaNoExistenteException {
        Factura facturaAEliminar = null;
        Scanner scan = new Scanner(System.in);
        boolean seBorro = false;

        // Buscar la factura por código
        for (Factura f : historial.getAlmacen()) {
            if (f.getCodigoFactura().equals(codigoFactura)) {
                facturaAEliminar = f;
                break;
            }
        }

        if (facturaAEliminar == null) {
            throw new FacturaNoExistenteException("La factura que desea eliminar no existe.");
        }

        System.out.println("Esta seguro que desea eliminar la factura del cliente " + facturaAEliminar.getCliente().getNombre() + " ?(SI/NO)");
        String rta = scan.nextLine().toLowerCase();

        if (rta == "si") {
            historial.eliminar(facturaAEliminar);
            seBorro = true;
        } else {
            System.out.println("no se ahr ");
        }

        return seBorro;
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    ///Para mi seria por rango de fechas o otra funcion
    //esta muestra las facturas de cualquier cliente en ese dia, pero se podria hacer por rango tambien
    public List<Factura> verHistorialPorFecha(LocalDate fecha) {

        if (fecha == null) {
            System.err.println("La fecha proporcionada no registra facturas en la fecha proporcionada.");
            return new ArrayList<>();
        }

        // Verificación de la fecha futura
        if (fecha.isAfter(LocalDate.now())) {
            System.err.println("La fecha proporcionada no puede ser en el futuro.");
            return new ArrayList<>(); // Retorna lista vacía si la fecha es futura
        }

        Predicate<Factura> condicion = factura -> factura.getFecha().equals(fecha);
        return historial.filtrarPorCondicion(condicion);
    }

    //mirar
    public void guardarEnArchivo() {

        try {
            FileWriter file = new FileWriter(this.nombreArchivoGson);
            String json = gson.toJson(getHistorial());
            file.write(json);
            file.close();
            System.out.println("Historial de facturas cargados con exito!");

        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void leerDesdeGson() {
        try (FileReader file = new FileReader(nombreArchivoGson)) {
            // Deserializar el archivo JSON a un objeto de tipo GestorAlmacen<Factura>
            GestorAlmacen<Factura> historialGson = gson.fromJson(file, new TypeToken<GestorAlmacen<Factura>>() {
            }.getType());
            this.historial = historialGson;  // Guardar el resultado en el atributo historial
        } catch (IOException e) {
            // Manejo adecuado de la excepción: aquí podrías loggear o imprimir el error
            System.out.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace(); // Para obtener más detalles de la excepción MIRAR COMO SE MUESTRA SI NO LO SACAMOS
        } catch (JsonSyntaxException e) {
            // En caso de que haya un error en el formato del JSON
            System.out.println("Error de sintaxis en el JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void historialFacturasPorCliente(String dni) throws DNInoEncontradoException {
        // Validar si el DNI es null o está vacío
        if (dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("El DNI proporcionado es inválido.");
        }

        List<Factura> facturasEncontradas = new ArrayList<>();

        // Recorremos todas las facturas en el historial
        for (Factura factu : historial.getAlmacen()) {
            // Si el DNI del cliente de la factura coincide con el DNI del parametro
            if (factu.getCliente().getDni().equals(dni)) {
                facturasEncontradas.add(factu);  // se agrega a la lisya
            }
        }

        if (facturasEncontradas.isEmpty()) {
            throw new DNInoEncontradoException("El DNI ingresado no pertenece a ninguno de nuestros clientes, intente de nuevo.");
        }

        //para mostrar las facturas ordanadas por fecha
        Collections.sort(facturasEncontradas, new Comparator<Factura>() {
            @Override
            public int compare(Factura f1, Factura f2) {
                return f1.getFecha().compareTo(f2.getFecha());
            }
        });

        // info del cliente
        Cliente cliente = new Cliente(null, null, dni, null, null);
        GestorPersona persona = new GestorPersona();
        cliente = (Cliente) persona.buscarPersona(dni);

        // mostrar las facturas
        System.out.println("Historial de facturas para el cliente con DNI " + dni +
                " Nombre y apellido: " + cliente.getNombre() + " " + cliente.getApellido());
        for (Factura factura : facturasEncontradas) {
            System.out.println("----------------------------------");
            System.out.println(factura);
            System.out.println("----------------------------------");

        }
    }


    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////
    public GestorAlmacen<Factura> getHistorial() {
        return historial;
    }

    public String getNombreArchivoGson() {
        return nombreArchivoGson;
    }

}

