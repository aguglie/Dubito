package server.network;

import game.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Andrea on 10/18/2016.
 */
public class SocketsListener {

    private static SocketsListener socketsListener;
    private int port;

    private SocketsListener(int port) {
        this.port = port;
    }

    public static SocketsListener getInstance(int port) {
        if (socketsListener == null) {
            socketsListener = new SocketsListener(port);
        }
        return socketsListener;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server started");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketListener socketListener = new SocketListener(socket);
                executor.submit(socketListener);//Starts a new thread to handle user's command on his socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        */
    }
}