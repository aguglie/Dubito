package gameClient.network;

import com.google.gson.Gson;
import game.action.Action;
import utils.MyLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by andrea on 08/12/16.
 */
public class SocketWriter {
    private PrintWriter socketOut;
    private Gson gson;

    public SocketWriter(Gson gson, Socket socket) {
        this.gson = gson;
        try {
            socketOut = new PrintWriter(socket.getOutputStream(), true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Serialize and send an Action object to server
     *
     * @param action
     */
    public void sendAction(Action action) {
        try {
            String json = gson.toJson(action, Action.class);
            socketOut.println(json);
            MyLogger.println("Sending " + json);
        } catch (Exception e) {
            MyLogger.println("Error sending message");
            e.printStackTrace();
        }
    }
}
