package server.controller;

import game.exception.UserException;
import server.network.SocketListener;
import utils.Logga;

import java.util.ArrayList;
import java.util.List;

/**
 * Lobby(room) where users have to join/create a room to play in
 */
public class Lobby {
    private static Lobby lobby;
    private List socketsList = new ArrayList<SocketListener>(2);//Sockets/Users waiting in lobby
    private List roomsList = new ArrayList<Room>(5);//All available roomsList

    private Lobby() {
    }

    /**
     * Singleton pattern
     *
     * @return
     */
    public static Lobby getInstance() {
        if (lobby == null) {
            lobby = new Lobby();
            Logga.print("Lobby created right now");
        }
        return lobby;
    }

    /**
     * Includes a new socketListener (user) in Lobby Room
     * Every socketListener is a connected user looking forward to join a room
     *
     * @param socketListener
     */
    public void addSocketListener(SocketListener socketListener) {
        socketsList.add(socketListener);
        Logga.print("User " + socketListener.getUser().toString() + " joins lobby");
    }

    /**
     * Deletes socketListener(user) entry from Lobby Room
     *
     * @param socketListener
     * @throws UserException
     */
    protected void removeSocketListener(SocketListener socketListener) throws UserException {
        if (!socketsList.contains(socketListener)) {
            throw new UserException("Unable to find user");
        } else {
            socketsList.remove(socketListener);
        }
    }

    /**
     * Returns socketsList ArrayList (should be used in read-mode only)
     *
     * @return
     */
    public List getSocketsList() {
        return socketsList;
    }

    /**
     * Creates a new game room
     *
     * @param title
     */
    public void createRoom(String title) {
        roomsList.add(new Room(title));
    }

    /**
     * (Safely) deletes a game room
     *
     * @param room
     */
    public void deleteRoom(Room room) {
        //After 'delete' method there should be no more references to the room
        room.housekeeping();//throws users to lobby etc
        roomsList.remove(room);
    }

    /**
     * Returns roomsList ArrayList (should be used in read-mode only)
     *
     * @return
     */
    public List getRoomsList() {
        return roomsList;
    }
}
