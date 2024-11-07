package gestores;

import model.Servicio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;

public class GestorAlmacen {

    private AlmacenGenerico<Servicio> almacen = new AlmacenGenerico<>();
    private Gson gson = new Gson();

    public GestorAlmacen() {
    }

    public void agregarServicio(Servicio servicio) {

        this.almacen.agregar(servicio);

    }

    public void eliminarServicio(Servicio servicio)
    {
        this.eliminarServicio(servicio);

    }

    public void guardarServicioAJSON(String nombreArchivo)
    {
        try(FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(almacen, writer);
            System.out.println("Datos guardados en el archivo " + nombreArchivo);
        }
        catch (IOException e)
        {
            System.out.println("Error al guardar datos al archivo" + e.getMessage());
        }
    }


}
