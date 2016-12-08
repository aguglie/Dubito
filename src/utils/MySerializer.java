package utils;

import com.google.gson.*;

import java.lang.reflect.Type;


/**
 * This is a support class for gson-library for serialize/deserialize objects keeping dynamic type unmodified.
 * Created by Andrea on 10/19/2016.
 */
public class MySerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {


    @Override
    public JsonElement serialize(final T o, final Type typeOfSrc, final JsonSerializationContext context) {
        // based on: http://www.javacreed.com/gson-serialiser-example/
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", o.getClass().getName());
        JsonObject dataJsonObject = context.serialize(o, o.getClass()).getAsJsonObject();
        try {
            //Used to serialize Match :(
            dataJsonObject.get("match").getAsJsonArray().forEach(e -> e.getAsJsonObject().remove("children"));
            dataJsonObject.get("match").getAsJsonArray().forEach(e -> e.getAsJsonObject().remove("groupedColumn"));
            dataJsonObject.get("match").getAsJsonArray().forEach(e -> e.getAsJsonObject().remove("groupedValue"));
        }catch (Exception e){}
        jsonObject.add("data", dataJsonObject);
        return jsonObject;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        // based on: http://www.javacreed.com/gson-deserialiser-example/
        final JsonObject jsonObject = json.getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();
        try {
            final Type casting = Class.forName(type);
            return context.deserialize(jsonObject.get("data"), casting);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Cannot find class " + type);
        }
    }
}