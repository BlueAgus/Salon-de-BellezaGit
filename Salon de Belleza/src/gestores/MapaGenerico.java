package gestores;

import java.util.HashMap;
import java.util.Map;

public class MapaGenerico<K, V>{
    //Esto se puede usar para el manejo de turnos

    private Map<K, V> mapa;

    public MapaGenerico() {
        this.mapa = new HashMap<>();
    }
    //La clave es unica y el valor puede repetirse
    public void agregar(K clave, V valor){
        mapa.put(clave, valor);
    }

    public V obtener(K clave){
        return mapa.get(clave); // Devuelve el valor asociado a la clave
    }

    public void eliminar(K clave){
        mapa.remove(clave);
    }
}
