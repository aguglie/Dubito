package server.controller;

import game.exception.UserException;
import server.network.SocketListener;
import utils.Logga;

import java.util.ArrayList;
import java.util.List;

/**
 * In every room you can start a gaming session,
 * here there's the brain driving Dubito.
 * Created by andrea on 18/10/16.
 */
public class Room {
    private Lobby lobby = Lobby.getInstance();
    private String title;
    private List socketsList = new ArrayList<SocketListener>(2);//Sockets/Users currently in this room

    public Room(String title) {
        this.title = title;
    }

    public Room() {
        this("Untitled");
    }

    @Override
    public String toString() {
        return title;
    }


    /**
     * Moves socketListener(User) from Lobby to this game room
     *
     * @param socketListener
     * @throws UserException
     */
    public void join(SocketListener socketListener) throws UserException {
        if (!lobby.getSocketsList().contains(socketListener)) {
            throw new UserException("Unable to find user");
        } else {
            lobby.removeSocketListener(socketListener);//removes user from lobby
            socketsList.add(socketListener);//adds user to this room's list.
            Logga.print("User " + socketListener.getUser().toString() + " joins " + title);
        }
    }

    /**
     * Moves socketListener(User) from this game room to lobby
     *
     * @param socketListener
     * @throws UserException
     */
    public void leave(SocketListener socketListener) throws UserException {
        if (!socketsList.contains(socketListener)) {
            throw new UserException("Unable to find user");
        } else {
            socketsList.remove(socketListener);//removes user from this room
            lobby.addSocketListener(socketListener);//adds user to lobby's list.
            Logga.print("User " + socketListener.getUser().toString() + " goes back to lobby");
        }
    }

    /**
     * Returns true if socketListener(User) belongs to this room
     *
     * @param socketListener
     * @return
     */
    public boolean isMyRoom(SocketListener socketListener) {
        if (socketsList.contains(socketListener)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Called before closing the room
     * it brings users back to lobby etc..
     */
    public void housekeeping() {
        //We like dirty houses @todo implement
    }
}
