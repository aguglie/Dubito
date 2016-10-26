package debugClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.action.JoinMatch;
import game.action.JoinServer;
import game.action.StartGame;
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
public class DebugClient {
    private String ip;
    private int port;

    public DebugClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        DebugClient client = new DebugClient("127.0.0.1", 1337);
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

        SocketReader socketReader = new SocketReader(socket);
        Thread reader_thread = new Thread(socketReader);
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
            //join socketReader thread
            stdin.close();
            socketOut.close();
            socket.close();
        }
    }
}