package server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.action.Action;
import game.action.SyncRooms;
import server.controller.ArcadeCtrl;
import utils.MySerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Andrea on 10/18/2016.
 */
public class SocketListener implements Runnable {
    private Socket socket;
    private ArcadeCtrl arcadeCtrl;

    public SocketListener(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new MySerializer<Action>())
                    .create();

            //We have just joined the game, we have report ourselves to the arcadeCtrl
            arcadeCtrl = ArcadeCtrl.getInstance();
            arcadeCtrl.addSocketListener(this);//Registers this socket to arcadeCtrl room

            while (true) {
                String line = in.nextLine();

                if (line.equals("exit")) {
                    break;
                } else if (line.equals("get rooms")) {
                    SyncRooms payload = new SyncRooms();
                    payload.setRooms(arcadeCtrl.getRooms());
                    String json = gson.toJson(payload, Action.class);
                    System.out.println(json);
                    out.println(json);
                    out.flush();
                } else {
                    out.println("what does '" + line + "' mean?");
                    out.flush();
                }
            }

            try {
                arcadeCtrl.getSocketsList().remove(this);
            } catch (Exception e) {
                //Amen
            }
            // chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}