package gestores;

import model.Servicio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.List;

public class GestorAlmacen <T>{

    private AlmacenGenerico<T> almacen = new AlmacenGenerico<>();
    private Gson gson = new Gson();

    public GestorAlmacen() {
    }

    public boolean agregar(T elemento) {

        return  almacen.agregar(elemento);

    }

    public boolean eliminar(T elemento)
    {
        return almacen.eliminar(elemento);

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

    public List<T> getAlmacen() {
        return almacen;
    }

    public void setAlmacen(AlmacenGenerico<T> almacen) {
        this.almacen = almacen;
    }
}
