package debugClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.action.JoinMatch;
import game.action.JoinServer;
import game.action.StartGame;
import game.model.Match;
import game.model.User;
import utils.MySerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Andrea on 10/18/2016.
 */
public class LineClient {
    private String ip;
    private int port;

    public LineClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        LineClient client = new LineClient("127.0.0.1", 1337);
        try {
            client.startClient();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() throws IOException {
        Socket socket = new Socket(ip, port);
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
        Scanner stdin = new Scanner(System.in);

        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                .create();//Gson Builder to serialize communication

        Reader reader = new Reader(socket);
        Thread reader_thread = new Thread(reader);
        reader_thread.start();
        try {
            while (true) {

                String inputLine = stdin.nextLine();

                if (inputLine.equals("join s")) {
                    Action action = new JoinServer("Guglio" + new Random().nextInt(100));
                    String json = gson.toJson(action, Action.class);
                    socketOut.println(json);
                } else if (inputLine.equals("join m")) {
                    Action action = new JoinMatch((int) 0);
                    String json = gson.toJson(action, Action.class);
                    socketOut.println(json);
                } else if (inputLine.equals("start")) {
                    Action action = new StartGame();
                    String json = gson.toJson(action, Action.class);
                    socketOut.println(json);
                } else {
                    System.out.println("Wrong");
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            //join reader thread
            stdin.close();
            socketOut.close();
            socket.close();
        }
    }
}