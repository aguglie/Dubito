package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 20/10/16.
 */
public class Lobby {
    private List rooms = new ArrayList<Room>(4);

    public void setRooms(List rooms) {
        this.rooms = rooms;
    }

    public List getRooms() {
        return rooms;
    }

    public void addRoom(Room room){
        this.rooms.add(room);
    }

    public void removeRoom(Room room){
        this.rooms.remove(room);
    }
}
