package server;

import server.network.SocketsListener;

/**
 * Created by andrea on 31/01/17.
 */
public class MainGuiOrJavaFX {
        public static void main(String[] args) throws Exception {
            Thread thread = new Thread(){
                public void run(){
                    int port = 8080;
                    SocketsListener socketsListener = SocketsListener.getInstance(port);
                    socketsListener.startServer();
                }
            };
            thread.start();

            if (args.length == 1 && args[0].equals("nogui")) {
                System.out.println("NOGUI SELECTED");
            } else {
                Main.launch(Main.class, args);
            }
        }

}
