package debugClient;

import game.model.Lobby;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
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
        System.out.println("Connection established");
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        Lobby lobby = new Lobby();
        Reader reader = new Reader(socket);
        reader.setLobby(lobby);
        Thread reader_thread = new Thread(reader);
        reader_thread.start();
        try {
            while (true) {

                String inputLine = stdin.nextLine();
                /*
                if (inputLine.equals("get rooms")) {
                    Action action = new SyncRooms();
                    String json = gson.toJson(action, Action.class);
                }else{
                    System.out.println("Wrong");
                    break;
                }*/
                socketOut.println(inputLine);
                socketOut.flush();
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