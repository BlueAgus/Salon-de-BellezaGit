package gestores;
//Capaz la logica de poner precio directamente en los enum no esta tan buena, entonces es mejor manejarlo de aca

import enumeraciones.TipoDepilacion;
import enumeraciones.TipoManicura;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
import model.Manicura;

import java.util.Map;

public class GestorPrecios {
    private Map<TipoServicio, Double> preciosBase;
    private Map<TipoManicura, Double> preciosManicura;
    private Map<TipoDepilacion, Double> precioDepilacion;
    private Map<TipoPestanias, Double> precioPestañas;
    private static double precioDiseño;


    private void inicializarPreciosBase(){
        //Aca en teoria podriamos sacar precio de servicio y los valores de los enum.
        //no toque nada de lo demas porque no quiero romper todo
        preciosBase.put(TipoServicio.MANICURA, 100.0);
        preciosBase.put(TipoServicio.DEPILACION, 100.0);
        preciosBase.put(TipoServicio.PESTANIAS, 100.0);

    }
}
