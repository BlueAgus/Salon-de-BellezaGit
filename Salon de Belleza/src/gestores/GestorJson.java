package gestores;


import com.google.gson.*;
import jdk.internal.access.JavaTemplateAccess;
import model.*;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalTime;

public class GestorJson {

   static GestorServicio servi = new GestorServicio();
    private static JavaTemplateAccess RuntimeTypeAdapterFactory;
    private static boolean adapterFactory;
    private static JavaTemplateAccess Servicio;
    // Configura Gson con adaptadores para LocalTime y subclases de Servicio
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                @Override
                public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalTime.parse(json.getAsString());
                }
            })
            .registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory<Servicio> adapterFactory = RuntimeTypeAdapterFactory.of(Servicio.class, "type")
                            .registerSubtype(Manicura.class, "manicura")
                            .registerSubtype(Pestanias.class, "pestañas")
                            .registerSubtype(Depilacion.class, "depilacion");

            )
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT) // Excluye campos estáticos/transitorios
            .setPrettyPrinting() // Opcional: formato legible
            .create();

    // Guarda cualquier objeto en un archivo JSON
    public static <T> void guardarEnArchivo(String nombreArchivo, T objeto) {
        try (FileWriter writer = new FileWriter(nombreArchivo)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.out.println("Error al guardar en archivo: " + e.getMessage());
        }
    }

    // Carga un objeto simple desde un archivo JSON
    public static <T> T cargarDeArchivo(String nombreArchivo, Class<T> clase) {
        try (FileReader reader = new FileReader(nombreArchivo)) {
            return gson.fromJson(reader, clase);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    // Carga objetos complejos, como listas o GestorAlmacen, desde un archivo JSON
    public static <T> T cargarDeArchivoType(String archivo, Type typeOfT) {
        try (Reader reader = new FileReader(archivo)) {
            return gson.fromJson(reader, typeOfT);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + archivo);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}


