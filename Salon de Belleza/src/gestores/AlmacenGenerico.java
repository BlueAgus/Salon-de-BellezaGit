package gestores;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

///solo el gestor Almacen usa esta clase
public class AlmacenGenerico<T> {

    private List<T> lista= new ArrayList<>();

    public AlmacenGenerico(){
    }

    public boolean agregar(T elemento){
        return lista.add(elemento);
    }

    public boolean eliminar(T elemento){
        return lista.remove(elemento);
    }

    public List<T> getLista() {
        return lista;
    }

    public ArrayList<T> filtrarPorCondicion(Predicate<T> condicion){
        ArrayList<T> resultado = new ArrayList<>();

        for(T elemento : lista){
            if(condicion.test(elemento)){
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    public void mostrar() {
        if (lista.isEmpty()) {
            System.out.println("No hay servicios en el almacén.");
        } else {
            for (T item : lista) {
                System.out.println(item);
            }
        }
    }
}
