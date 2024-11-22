package gestores;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class GestorAlmacenAdapter implements JsonSerializer<GestorAlmacen<?>>, JsonDeserializer<GestorAlmacen<?>> {

    @Override
    public JsonElement serialize(GestorAlmacen<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getAlmacen()); // Serializa solo los elementos
    }

    @Override
    public GestorAlmacen<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        List<?> elementos = context.deserialize(json, new TypeToken<List<?>>() {}.getType());
        GestorAlmacen<Object> almacen = new GestorAlmacen<>();
        for (Object elemento : elementos) {
            almacen.agregar(elemento);
        }
        return almacen;
    }
}
