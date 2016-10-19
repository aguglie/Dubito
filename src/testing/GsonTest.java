package testing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {
    public static void main(String[] args) {
        ExtendedAction action = new ExtendedAction();

        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();

        String json = gson.toJson(action ,Action.class);
        System.out.println(json);

        Action oggetto = gson.fromJson(json, Action.class);
    }
}
