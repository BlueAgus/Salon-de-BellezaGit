package gestores;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class GestorFactura {

    private GestorAlmacen<Factura> historial;
    private Scanner scan;
    Gson geson;
    private String nombreArchivoGson;


    public GestorFactura(String nombreArchivoGson) {
        this.historial = new GestorAlmacen<>();
        this.geson = new Gson();
        this.nombreArchivoGson = nombreArchivoGson;
        this.scan = new Scanner(System.in);
    }

    public boolean crearFactura (){
        boolean cargado = false;
        Cliente cliente;
        GestorPersona clientes = new GestorPersona();
        Factura facturaFinal;
        List<Turno> turnosCliente = new ArrayList<>();


        while (!cargado) {
            try {
                // Buscar cliente por DNI
                System.out.println("Ingrese el DNI del cliente:");
                String dni = scan.nextLine();
                cliente = (Cliente) clientes.buscarPersona(dni);

                // Crear instancia de factura para este cliente
                facturaFinal = new Factura(null, cliente);

                // Capturar servicios realizados
                System.out.println("Cuántos servicios se le realizaron al cliente:");
                int cantidad = scan.nextInt();
                scan.nextLine();

                for (int i = 0; i < cantidad; i++) {
                    System.out.println("Ingrese el código de turno " + (i + 1) + ":");
                    String codigo = scan.nextLine();

                    // Hya que hacer este metodo en Gestor Turno
                    GestorTurnos turno = buscarTurnoPorCodigo(codigo);
                    if (turno != null) {
                        facturaFinal.agregarTurno(turno);
                    } else {
                        System.out.println("Código de servicio no encontrado, intente de nuevo.");
                        i--;  // Repite la iteración si el código no es válido
                    }
                }

                // Definir el tipo de pago
                System.out.println("Seleccione el tipo de pago:");
                for (TipoDePago tipo : TipoDePago.values()) {
                    System.out.println(tipo.ordinal() + " - " + tipo.name());
                }
                int opcionPago = scan.nextInt();
                facturaFinal.setTipoPago(TipoDePago.values()[opcionPago]);

                // Calcular precio final
                facturaFinal.calcularPrecioFinal();
                System.out.println("Factura creada:\n" + facturaFinal);

                // Guardar la factura en el historial
                agregarFactura(facturaFinal);
                cargado = true;

            } catch (DNInoEncontradoException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (FacturaYaExistenteException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
        return cargado;
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


    public List<Factura> verHistorialPorFecha(LocalDate fecha) {

        if (fecha == null) {
            System.err.println("La fecha proporcionada es nula.");
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


    public GestorAlmacen<Factura> getHistorial() {
        return historial;
    }

    public void guardarEnArchivo() {

        try {
            FileWriter file = new FileWriter(this.nombreArchivoGson);
            String json = geson.toJson(getHistorial());
            file.write(json);
            file.close();
            System.out.println("Historial de facturas cargados con exito!");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void leerDesdeGson() {

        try {
            FileReader file = new FileReader(nombreArchivoGson);
            GestorAlmacen<Factura> historialGson = geson.fromJson(file, new TypeToken<GestorAlmacen<Factura>>() {
            }.getType());
            this.historial = historialGson;
            file.close();
        } catch (IOException e) {
            e.getMessage();

        }

    }

    public void historialFacturasPorCliente(String dni) throws DNInoEncontradoException {
        List<Factura> facturasEncontradas = new ArrayList<>();

        // Recorremos todas las facturas en el historial
        for (Factura factu : historial.getAlmacen()) {
            // Si el DNI del cliente de la factura coincide con el DNI proporcionado
            if (factu.getCliente().getDni().equals(dni)) {
                facturasEncontradas.add(factu);  // Agregamos la factura a la lista
            }
        }

        //aca la idea es desde afuera poner el while para que vuelva a intentar
        if (facturasEncontradas.isEmpty()) {
            throw new DNInoEncontradoException("El DNI ingresado no existe en la base de datos, intente de nuevo.");
        }

         Cliente cliente = new Cliente(null, null, dni, null, null);
         GestorPersona persona = new GestorPersona();
         cliente = (Cliente) persona.buscarPersona(dni);
        // Mostrar las facturas encontradas
        System.out.println("Historial de facturas para el cliente con DNI " + dni + " Nombre y apellido: "+cliente.getNombre()+" "+cliente.getApellido()+"");
        for (Factura factura : facturasEncontradas) {
            System.out.println(factura);  // Aquí puedes modificar la forma de mostrar la factura según lo necesites
        }
    }


    public String getNombreArchivoGson() {
        return nombreArchivoGson;
    }

}

