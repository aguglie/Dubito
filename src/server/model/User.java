package server.model;

import server.network.SocketHandler;

/**
 * Created by andrea on 19/10/16.
 */
public class User extends game.model.User {
    private transient SocketHandler socketHandler;//Riferimento alla socket utente

    public void setSocketHandler(SocketHandler socketHandler){
        this.socketHandler = socketHandler;
    }

    public SocketHandler getSocketHandler() {
        return socketHandler;
    }
}
