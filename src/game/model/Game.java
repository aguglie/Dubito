package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class Game {
    private List rooms;

    public void setRooms(List<Room> rooms){
        rooms = new ArrayList<Room>(rooms);
    }
}
