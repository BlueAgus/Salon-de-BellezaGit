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
import java.io.FileNotFoundException;
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
    private final String nombreArchivoGson;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public GestorFactura(String nombreArchivoGson) {

        this.historial = new GestorAlmacen<>();
        this.gson = new Gson();
        this.nombreArchivoGson = nombreArchivoGson;
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public void crearFactura() {
        GestorServicio gestor = new GestorServicio();
        GestorTurno turnos = new GestorTurno();
        GestorPersona persona = new GestorPersona();
        Scanner scan = new Scanner(System.in);

        try {
            // Pedir cliente por DNI
            Cliente cliente = null;
            while (cliente == null) {
                try {
                    System.out.println("Ingrese el DNI del cliente:");
                    String dni = scan.nextLine();
                    cliente = (Cliente) persona.buscarPersona(dni);
                } catch (DNInoEncontradoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("¿Desea intentar nuevamente? (SI/NO):");
                    if (!scan.nextLine().equalsIgnoreCase("SI")) {// ese equals ignora si es en mayus o minus
                        return; //salir si el usuario ingresa NO
                    }
                }
            }

            //pedir tipo de pago
            TipoDePago tipo = pedirTipoPago();
            Factura factura = new Factura(tipo, cliente, gestor);

            //buscamos los turnos de hoy y futuros de ese cliente
            List<Turno> turnosCliente = turnos.buscarTurnosXdniClienteVigentes(cliente.getDni());

            if (turnosCliente.isEmpty()) {
                System.out.println("No hay turnos reservados para este cliente.");
                return;
            }

            //mostrarle los turnos del dia de la fecha y los proximos
            System.out.println("Estos son los turnos recientes del cliente: ");
            for (int i = 0; i < turnosCliente.size(); i++) {
                System.out.println((i + 1) + ". " + turnosCliente.get(i));
            }

            boolean continuar = true;
            while (continuar) {

                System.out.println(" ");// dejo un espacio
                System.out.print("Ingrese el número del turno a pagar: ");
                    int nroTurno = scan.nextInt();

                    if (nroTurno < 1 || nroTurno > turnosCliente.size()) { //corroboramos que el numero esye bien
                        System.out.println("Número de turno inválido. Intente nuevamente.");
                        continue;
                    }

                    Turno turnoSeleccionado = turnosCliente.get(nroTurno - 1);

                    if (!factura.getTurnosPorCliente().contains(turnoSeleccionado)) {
                        factura.agregarTurno(turnoSeleccionado);
                        System.out.println("El turno se agregó correctamente a la factura.");
                    } else {
                        System.out.println("El turno ya está en la factura.");
                    }


                System.out.print("¿Desea agregar otro turno? (SI/NO): ");
                String rta = scan.next().toLowerCase();
                continuar = rta.equals("si");
            }

            System.out.println("Aplicar descuento?(SI/NO)");
            String rta2 = scan.next().toLowerCase();

            if(rta2.equalsIgnoreCase("si")){
                aplicarDescuento(factura, scan);
            }
            else{
                return;
            }

            // montrar factura y agregarla
            boolean seCargo = agregarFactura(factura);
            if (seCargo) {
                System.out.println("La factura fue ingresada correctamente, aqui tiene los detalles:");
                System.out.println(factura);
                verificarCarga(factura, scan);
            }

        } catch (TurnoExistenteException | FacturaYaExistenteException e) {
            System.out.println(e.getMessage());
        }
    }


    ///aca se entiende que anterior a este metodo se muestran las facturas y de ahi se saca el codigo, todas las del dni de la persona que queremos
    public void modificarFactura() {
        Factura facturaModificada = null;
        Scanner scan = new Scanner(System.in);
        GestorTurno turnos = new GestorTurno();

        // Bucle para validar el código de la factura
        while (facturaModificada == null) {
            System.out.println("Ingrese el código de factura:");
            String codigo = scan.nextLine();

            try {
                facturaModificada = buscarFacturaPorCodigo(codigo);
            } catch (CodigoNoEncontradoException e) {
                System.out.println(e.getMessage());
                System.out.println("¿Desea intentarlo de nuevo? (S/N):");
                if (!scan.nextLine().equalsIgnoreCase("S")) {
                    return; // Salir del método
                }
            }
        }

        ///Elegir que se modifica
        int opcion;
        do {
            //no te olvides e actualizar la fecha!!
            System.out.println("Ingrese una opcion...");
            System.out.println("1. Tipo de pago -");
            System.out.println("2. Modificar el cliente -");
            System.out.println("3. Turnos declarados en la factura-");
            System.out.println("4. Aplicar descuento-"); //Esto puede ser en algun descuento especial o flasheo yo
            System.out.println("0. Salir");// tendria que haber una opcion para volver para atras
            System.out.print("Ingrese una opción: ");
            opcion = scan.nextInt();
            scan.nextLine();

            switch (opcion) {
                case 1:
                    TipoDePago tipo = pedirTipoPago();
                    facturaModificada.setTipoPago(tipo);
                    System.out.println("Tipo de pago actualizado.");

                    modificarFechaFactura(facturaModificada);
                    break;
                case 2:
                     modificarCliente(facturaModificada, scan);
                     //actualizar la fecha a la actual
                    modificarFechaFactura(facturaModificada);

                    break;
                case 3:

                     gestionarTurnos(facturaModificada, scan, turnos);
                     modificarFechaFactura(facturaModificada);

                    break;
                case 4:

                    aplicarDescuento(facturaModificada, scan);
                    modificarFechaFactura(facturaModificada);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private void aplicarDescuento(Factura factura, Scanner scan) {

        try{
            System.out.println("Precio final actual " + factura.getPrecioFinal());
            // metodo de descuento en gestor precios
            System.out.println("Ingrese el porcentaje de descuento");
            double desc = scan.nextDouble();
           GestorPrecios.aplicarDescuento(factura.getCodigoFactura(), desc, historial.getAlmacen());

        }catch (CodigoNoEncontradoException e){
            System.out.println(e.getMessage());
        }
    }

    public void verificarCarga(Factura factura, Scanner scanner) {
        int opcion;
        do {
            System.out.println("¿Deseas modificar algo de la factura?");
            System.out.println("1. Sí");
            System.out.println("2. No");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    modificarFactura();
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

    private void gestionarTurnos(Factura factura, Scanner scan, GestorTurno turnos) {
        try {
            System.out.println("Turnos actuales:");
            for (int i = 0; i < factura.getTurnosPorCliente().size(); i++) {
                System.out.println((i + 1) + ". " + factura.getTurnosPorCliente().get(i));
            }
            System.out.println("Seleccione un turno para eliminar o ingrese 0 para agregar uno nuevo:");
            int opcion = scan.nextInt();
            scan.nextLine();

            if (opcion > 0 && opcion <= factura.getTurnosPorCliente().size()) {
                Turno turno = factura.getTurnosPorCliente().get(opcion - 1);
                factura.eliminarTurno(turno);
                System.out.println("Turno eliminado.");
            } else if (opcion == 0) {
                System.out.println("Ingrese el código del turno:");
                String codTurno = scan.nextLine();
                Turno nuevoTurno = turnos.buscarTurnoXcodigo(codTurno);
                factura.agregarTurno(nuevoTurno);
                System.out.println("Turno agregado.");
            } else {
                System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void modificarCliente(Factura factura, Scanner scan) {
        GestorPersona persona = new GestorPersona();

        try {
            System.out.println("Ingrese el DNI del cliente:");
            String dni = scan.nextLine();
            Cliente cliente = (Cliente) persona.buscarPersona(dni);
            factura.setCliente(cliente);
            System.out.println("Cliente actualizado.");
        } catch (DNInoEncontradoException e) {
            System.out.println(e.getMessage());
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



    public void modificarFechaFactura(Factura factura) {
        LocalDate nuevaFecha = LocalDate.now();
        factura.setFecha(nuevaFecha);
    }

    private Factura buscarFacturaPorCodigo(String codigo) throws CodigoNoEncontradoException {


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

        System.out.println("¿Está seguro que desea eliminar la factura del cliente "
                + facturaAEliminar.getCliente().getNombre() + "? (SI/NO)");
        String rta = scan.nextLine().toLowerCase();

        if (rta.equals("si")) {
            historial.eliminar(facturaAEliminar);
            System.out.println("Factura eliminada exitosamente.");
            seBorro = true;
        } else {
            System.out.println("La factura no fue eliminada.");
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
            this.historial = gson.fromJson(file, new TypeToken<GestorAlmacen<Factura>>() {}.getType());
            if (this.historial == null) {
                this.historial = new GestorAlmacen<>();
            }
            System.out.println("Historial de facturas cargado exitosamente.");
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo. Se iniciará con un historial vacío.");
            this.historial = new GestorAlmacen<>();
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Ocurrió un error al cargar el archivo: " + e.getMessage());
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
        facturasEncontradas.sort(new Comparator<Factura>() {
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


