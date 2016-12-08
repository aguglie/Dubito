package server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import server.controller.GameLogic;
import server.model.User;
import utils.MyLogger;
import utils.MySerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
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
                    MyLogger.println("Invalid JSON: " + line);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            GameLogic.getInstance().onUserDisconnect(this.user);
        } finally {
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