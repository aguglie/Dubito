package server;

import server.controller.Lobby;
import server.network.SocketsListener;

/**
 * This main file starts a SocketsListener
 * Created by andrea on 18/10/16.
 */
public class Main {
    public static void main(String[] args) {
        int port = 1337;
        SocketsListener socketsListener = SocketsListener.getInstance(port);
        socketsListener.startServer();//blocking function
        /* no code below */
    }
}
