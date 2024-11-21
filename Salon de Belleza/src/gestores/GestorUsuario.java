package gestores;

import com.google.gson.Gson;
import model.Persona;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GestorUsuario<T extends Persona> {

    private List<T> almacen = new ArrayList<>();

    private Gson gson = new Gson();
    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR////////////////////////////////////////////////////

    public boolean agregar(T elemento) {

        return almacen.add(elemento);

    }
    public boolean eliminar(T elemento) {
        return almacen.remove(elemento);
    }

    public void mostrar() {
        if (almacen.isEmpty()) {
            System.out.println("No hay elementos en el almacén.");
        } else {
            for (T item : almacen) {
                System.out.println(item);
            }
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

