package debugClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.model.*;
import utils.MySerializer;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by andrea on 20/10/16.
 */
public class Reader implements Runnable {
    private Socket socket;
    private Scanner socketIn;
    private Lobby lobby;


    public Reader(Socket socket) {
        this.socket = socket;
        try {
            this.socketIn = new Scanner(socket.getInputStream());
        } catch (Exception e) {
            //@todo sistemare questo schifo
        }
    }

    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public void run() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();
        System.out.println("Reader is up");
        while (true) {
            String socketLine = socketIn.nextLine();
            System.out.println(socketLine);
            Action oggetto = gson.fromJson(socketLine, Action.class);
            System.out.println("Recived from server: " + socketLine);
            oggetto.doAction(lobby, null, null);
            System.out.println(lobby.getRooms().toString());
        }
        //socketIn.close();
    }
}