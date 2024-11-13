package gestores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import excepciones.DNInoEncontradoException;
import excepciones.FacturaNoExistenteException;
import excepciones.FacturaYaExistenteException;
import model.Factura;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GestorFactura {

    private GestorAlmacen<Factura> historial;
    Gson geson;
    private String nombreArchivoGson;

    public GestorFactura(String nombreArchivoGson) {
        this.historial = new GestorAlmacen<>();
        this.geson = new Gson();
        this.nombreArchivoGson = nombreArchivoGson;
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


public List<Factura> verHistorialPorFecha (LocalDate fecha) {

        Predicate<Factura> condicion = factura -> factura.getFecha().equals(fecha);

        return historial.filtrarPorCondicion(condicion);
    }

    public GestorAlmacen<Factura> getHistorial() {
        return historial;
    }

public void guardarEnArchivo (){

        try{
            FileWriter file = new FileWriter(this.nombreArchivoGson);
            String json = geson.toJson(getHistorial());
            file.write(json);
            file.close();
            System.out.println("Historial de facturas cargados con exito!");
        }catch (IOException e){
            e.getMessage();
        }
    }

public void leerDesdeGson(){

        try{
            FileReader file = new FileReader(nombreArchivoGson);
            GestorAlmacen<Factura> historialGson = geson.fromJson(file,  new TypeToken<GestorAlmacen<Factura>>(){}.getType());
            this.historial = historialGson;
            file.close();
        }catch (IOException e){
            e.getMessage();

        }

    }

    public void buscarPorCliente(String dni) throws DNInoEncontradoException {


    }


    public String getNombreArchivoGson() {
        return nombreArchivoGson;
    }


}