package gestores;
//Capaz la logica de poner precio directamente en los enum no esta tan buena, entonces es mejor manejarlo de aca

import enumeraciones.TipoDepilacion;
import enumeraciones.TipoManicura;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
import model.Depilacion;
import model.Manicura;
import model.Pestanias;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class GestorPrecios {
    private static final double precioDepiCera = 10.0;
    private static final double precioDepiLaser = 10.0;

    private static final double precioManiGel = 10.0;
    private static final double precioManiEsculpidas = 10.0;
    private static final double precioManiSemi = 10.0;
    private static double precioDisenio = 10.0;

    private static final double precioPestanias3D = 10.0;
    private static final double precioPestanias2D = 10.0;
    private static final double precioPestaniasClasic = 10.0;

    // mapa anidado
    private static final Map<Class<?>, Map<Enum<?>, Double>> precios = new HashMap<>();

    static {
        // inicializa los mapas de cada clase vacios
        precios.put(Depilacion.class, new HashMap<>());
        precios.put(Manicura.class, new HashMap<>());
        precios.put(Pestanias.class, new HashMap<>());
                                                      //clave   vaaaaloooooor
        // aca le asignamos su otro mapa anidado Mapa(  clase, (enum, precio) );
        //depilacion                                           clave, valor
        precios.get(Depilacion.class).put(TipoDepilacion.CERA, precioDepiCera);
        precios.get(Depilacion.class).put(TipoDepilacion.LASER, precioDepiLaser);

        // manicura
        precios.get(Manicura.class).put(TipoManicura.GEL, precioManiGel);
        precios.get(Manicura.class).put(TipoManicura.ESCULPIDAS, precioManiEsculpidas);
        precios.get(Manicura.class).put(TipoManicura.SEMIPERMANENTE, precioManiSemi);

        //
        precios.get(Pestanias.class).put(TipoPestanias.TRES_D, precioPestanias3D);
        precios.get(Pestanias.class).put(TipoPestanias.DOS_D, precioPestanias2D);
        precios.get(Pestanias.class).put(TipoPestanias.CLASICAS, precioPestaniasClasic);
    }


    public static double obtenerPrecio(Class<?> claseServicio, Enum<?> tipo) {
        return precios.get(claseServicio).get(tipo);
        //sacamos primero el valor de la clave "clase" que el mapa anidado, y luego sacamos el valor del segundo mapa que es el precio
    }

    public static void modificarPrecio(Class<?> claseServicio, Enum<?> tipo, double nuevoPrecio) {
        Map<Enum<?>, Double> mapaPrecios = precios.get(claseServicio);
        if (mapaPrecios == null) {
            throw new IllegalArgumentException("No se encontró un mapa de precios para la clase: " + claseServicio.getName());
        }
        if (!mapaPrecios.containsKey(tipo)) {
            throw new IllegalArgumentException("El tipo " + tipo + " no pertenece a la clase " + claseServicio.getName());
        }
        mapaPrecios.put(tipo, nuevoPrecio);
    }


    // solo va a usarse para  manicura
    public static double agregarDisenio(Enum<?> tipo) {
        if (!(tipo instanceof TipoManicura)) {
            throw new IllegalArgumentException("El diseño solo aplica a servicios de manicura.");
        }
        double precioBase = obtenerPrecio(Manicura.class, tipo);
        return precioBase + precioDisenio;
    }

    public static double getPrecioDisenio() {
        return precioDisenio;
    }


    public static void setPrecioDisenio(double nuevoPrecioDisenio) {
        if (nuevoPrecioDisenio < 0) {
            throw new ArithmeticException("El precio de diseño no puede ser negativo.");
        }
        precioDisenio = nuevoPrecioDisenio;
    }



    public static void aumentarTodosLosPrecios(double porcentaje) {
        if (porcentaje < 0) {
            throw new ArithmeticException("El porcentaje de aumento no puede ser negativo.");
        }
        double aumento = 1 + (porcentaje/100);

        // se usa este en vez de un for porque no es facil de usar con un mapa anidado y no queda lindo

        //               clave,        valor(o sea el mapa anidado)
        precios.forEach((claseIndice, mapaPreciosIndice) -> {//itera sobre todos los pares clave-valor de precios
            // y aca iteramos sobre el mapa aniado que contiene el enum y el precio para actualizar
            mapaPreciosIndice.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
            //este metodo actualiza los valores del map, toma los valores actuales y modifica
        });
    }

    public static void aumentarPreciosPorClase(Class<?> servicio, double porcentaje) {
        if (porcentaje < 0) {
            throw new ArithmeticException("El porcentaje de aumento no puede ser negativo.");
        }
        double aumento = 1 + (porcentaje / 100);

        // Verificamos si la clase existe en el mapa de precios
        Map<Enum<?>, Double> mapaPrecios = precios.get(servicio); // obtenemos el valor de precios (la clase anidada)
        if (mapaPrecios != null) {
            // el metodo replaceAll reemplaza todos los valores de una
            mapaPrecios.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
        } else {
            throw new IllegalArgumentException("No se encontraron precios para la clase: " + servicio.getSimpleName());
        }
    }

}
