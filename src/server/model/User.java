package server.model;

import server.network.SocketHandler;

/**
 * Created by andrea on 19/10/16.
 */

/**
 * User abstraction
 */
public class User extends game.model.User {
    private transient SocketHandler socketHandler;//Riferimento alla socket utente
    private transient Match match;
    private transient long lastHeartBeat;

    /**
     * Returns true if user is in a match
     * @return
     */
    public boolean hasMatch() {
        if (match != null) return true;
        else return false;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public SocketHandler getSocketHandler() {
        return socketHandler;
    }
}
