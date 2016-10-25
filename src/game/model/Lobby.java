package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 20/10/16.
 */
public class Lobby {

    private List rooms;

    public void setRooms(List rooms) {
        this.rooms = new ArrayList<Match>(rooms);
    }

    public List getRooms() {
        return rooms;
    }

    public void addRoom(Match match) {
        this.rooms.add(match);
    }

    public void removeRoom(Match match) {
        this.rooms.remove(match);
    }
}
