package gestores;

import com.google.gson.Gson;
import model.Servicio;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GestorAlmacen <T>{

    private List<T> almacen= new ArrayList<>();

    private Gson gson = new Gson();

    public GestorAlmacen() {
    }
// por qué boleados? no es mejor void 
    public boolean agregar(T elemento) {

        return  almacen.add(elemento);

    }

    public boolean eliminar(T elemento)
    {
        return almacen.remove(elemento);
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

    public void setAlmacen(List<T> almacen) {
        this.almacen = almacen;
    }


    public ArrayList<T> filtrarPorCondicion(Predicate<T> condicion){
        ArrayList<T> resultado = new ArrayList<>();

        for(T elemento : almacen){
            if(condicion.test(elemento)){
                resultado.add(elemento);
            }
        }
        return resultado;
    }
//Esto me parece que no va a funcionar porque este clase no tiene toString 
    public void mostrar() {
        if (almacen.isEmpty()) {
            System.out.println("No hay elementos en el almacén.");
        } else {
            for (T item : almacen) {
                System.out.println(item);
            }
        }
    }
}
