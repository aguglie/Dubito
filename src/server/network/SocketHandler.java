package server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.action.SendStats;
import server.controller.GameLogic;
import server.model.Match;
import server.model.User;
import utils.MyLogger;
import utils.MySerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Andrea on 10/18/2016.
 */
public class SocketHandler implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Gson gson;
    private User user;

    public SocketHandler(Socket socket) {
        this.socket = socket;//Set references
        user = new User();//every socket is connected to a user.
        user.setSocketHandler(this);
        gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();//Gson Builder to serialize communication

        //Try to create in/out paths
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        GameLogic.getInstance().onUserConnect(user);//Notify to GameLogic user connection
    }

    /**
     * Serialize and send an Action object to this socket
     *
     * @param action
     */
    public void sendAction(Action action) {
        try {
            String json = gson.toJson(action, Action.class);
            MyLogger.println("Sending " + json);
            out.println(json);
        } catch (Exception e) {
            MyLogger.println("Error sending message");
        }
    }

    /**
     * Waits for data on socket and handles it
     */
    public void run() {
        try {
            while (true) {
                String line = in.nextLine();
                try {
                    //Execute action recived
                    Action oggetto = gson.fromJson(line, Action.class);
                    oggetto.doAction(this.user);
                } catch (Exception e) {
                    if (line.equals("")){ // last line of request header is blank
                        MyLogger.println("Http Request Found");

                        out.println("HTTP/1.1 200 OK");
                        out.println("Connection: close");
                        out.println("");
                        Action stats = new SendStats(Match.getMatches(), new ArrayList<game.model.User>(GameLogic.getInstance().getConnectedUsers()));
                        out.println(gson.toJson(stats, Action.class));
                        break;
                    }
                    //MyLogger.println("Deleting client, Invalid JSON: " + line);
                }
            }
        } catch (Exception e) {
            //Usually we get here if user disconnects
        } finally {
            GameLogic.getInstance().onUserDisconnect(this.user);
            //Close all open streams
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) try {
                socket.close();
            } catch (Exception e) {
                //prosit
            }
        }
    }
}