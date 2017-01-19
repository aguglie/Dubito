package gameClient.network;

import com.google.gson.Gson;

import game.action.Action;
import gameClient.ClientObjs;
import utils.MyLogger;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by andrea on 08/12/16.
 */
public class SocketListener implements Runnable{
    private Gson gson;
    private Scanner in;

    SocketListener(Gson gson, Socket socket) {
        this.gson = gson;
        try {
            in = new Scanner(socket.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            String socketLine = in.nextLine();
            //MyLogger.println("Ricevuto " + socketLine);
            Action obj = gson.fromJson(socketLine, Action.class);
            obj.doAction(ClientObjs.getUser());
        }
    }
}
