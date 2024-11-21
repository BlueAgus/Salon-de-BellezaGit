package model;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;

public class LocalTimeAdapter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        // Convierte LocalTime a String en formato ISO-8601
        return new JsonPrimitive(src.toString());
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // Convierte un String en formato ISO-8601 a LocalTime
        return LocalTime.parse(json.getAsString());
    }
}