package gestores;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GestorAlmacen<T> {

    private List<T> almacen = new ArrayList<>();

    private Gson gson = new Gson();
    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR////////////////////////////////////////////////////

    public boolean agregar(T elemento) {

        return almacen.add(elemento);

    }

    public boolean eliminar(T elemento) {
        return almacen.remove(elemento);
    }

    //public T buscarElemento(int indice){return almacen.get(indice);}

    public void mostrar() {
        if (almacen.isEmpty()) {
            System.out.println("No hay elementos en el almacén.");
        } else {
            for (T item : almacen) {
                System.out.println(item);
            }
        }
        ///quedaria mejor personalizado en cada gestor me parece
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public void guardarServicioAJSON(String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(almacen, writer);
            System.out.println("Datos guardados en el archivo " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar datos al archivo" + e.getMessage());
        }
    }

    public ArrayList<T> filtrarPorCondicion(Predicate<T> condicion) {
        ArrayList<T> resultado = new ArrayList<>();

        for (T elemento : almacen) {
            if (condicion.test(elemento)) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public List<T> getAlmacen() {
        return almacen;
    }

    public void setAlmacen(List<T> almacen) {this.almacen = almacen;}


}
