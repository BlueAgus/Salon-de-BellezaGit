package gestores;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AlmacenGenerico<T> {

    private List<T> lista;

    public AlmacenGenerico(List<T> lista) {
        this.lista = new ArrayList<>();
    }

    public void agregar(T elemento){
        lista.add(elemento);
    }

    public void eliminar(T elemento){
        lista.remove(elemento);
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

}
