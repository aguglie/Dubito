package debugClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.model.*;
import utils.MyLogger;
import utils.MySerializer;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by andrea on 20/10/16.
 */
public class Reader implements Runnable {
    private Socket socket;
    private Scanner in;
    private User user;
    private Match match;


    public Reader(Socket socket) {
        this.socket = socket;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setMatch(Match match) { this.match = match; }

    @Override
    public void run() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();
        MyLogger.println("Reader is UP");
        while (true) {
            try {
                String socketLine = in.nextLine();
                MyLogger.println("Ricevuto "+socketLine);
                Action obj = gson.fromJson(socketLine, Action.class);
                obj.doAction(user);
            } catch (Exception e){
                System.out.println("Bah boh beh errore");
                e.printStackTrace(System.err);
                break;
            }
        }
        //socketIn.close();
    }
}