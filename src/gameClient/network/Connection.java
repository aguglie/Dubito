package gameClient.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import gameClient.ClientObjs;
import utils.MySerializer;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by andrea on 08/12/16.
 */
public class Connection {
    private String ip;
    private int port;

    public Connection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        startClient();
    }

    public void startClient() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();//Gson Builder to serialize communication

        try {
            Socket socket = new Socket(ip, port);//Creates a new socket

            //Starts a new thread to listen and handle messages
            Thread reader_thread = new Thread(new SocketListener(gson, socket));
            reader_thread.start();

            //Creates a new object which takes care of writing on socket
            ClientObjs.setSocketWriter(new SocketWriter(gson, socket));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //socketOut.close();
        //socket.close();
    }
}
