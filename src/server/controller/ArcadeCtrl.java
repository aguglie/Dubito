package server.controller;

import game.exception.UserException;
import server.model.Room;
import server.network.SocketListener;
import utils.Logga;

import java.util.ArrayList;
import java.util.List;

/**
 * ArcadeCtrl(room) where users have to join/create a room to play in
 */
public class ArcadeCtrl {
    private static ArcadeCtrl arcadeCtrl;
    private List socketsList = new ArrayList<SocketListener>(4);
    private List rooms = new ArrayList<Room>(4);

    private ArcadeCtrl() {
        rooms.add(new Room("First Room"));
        rooms.add(new Room("Second Room"));
    }

    /**
     * Singleton pattern
     *
     * @return
     */
    public static ArcadeCtrl getInstance() {
        if (arcadeCtrl == null) {
            arcadeCtrl = new ArcadeCtrl();
            Logga.print("ArcadeCtrl created right now");
        }
        return arcadeCtrl;
    }

    /**
     * Includes a new socketListener (user) in ArcadeCtrl RoomCtrl
     * Every socketListener is a connected user looking forward to join a room
     *
     * @param socketListener
     */
    public void addSocketListener(SocketListener socketListener) {
        socketsList.add(socketListener);
    }

    public List getRooms(){
        return rooms;
    }
    /**
     * Deletes socketListener(user) entry from ArcadeCtrl RoomCtrl
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

}
