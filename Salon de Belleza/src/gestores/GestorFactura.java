package gestores;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDePago;
import excepciones.DNInoEncontradoException;
import excepciones.FacturaNoExistenteException;
import excepciones.FacturaYaExistenteException;
import model.Cliente;
import model.Factura;
import model.Turno;

import javax.xml.transform.Source;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

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

        return historial.eliminar(facturaAEliminar);
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

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
            GestorAlmacen<Factura> historialGson = gson.fromJson(file, new TypeToken<GestorAlmacen<Factura>>() {}.getType());
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

