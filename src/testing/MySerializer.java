package testing;

import com.google.gson.*;

import java.lang.reflect.Type;


/**
 * Created by Andrea on 10/19/2016.
 */
public class MySerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {


    @Override
    public JsonElement serialize(final T o, final Type typeOfSrc, final JsonSerializationContext context) {
        // based on: http://www.javacreed.com/gson-serialiser-example/
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", o.getClass().getName());
        jsonObject.add("data", context.serialize(o, o.getClass()));
        return jsonObject;
    }

    @Override
    public T deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String type = jsonObject.get("type").getAsString();
        try {
            final Type casting = Class.forName(type);
            return context.deserialize(jsonObject.get("data"), casting);
        }catch (ClassNotFoundException e){
            throw new JsonParseException("Cannot find class "+type);
        }
    }
}