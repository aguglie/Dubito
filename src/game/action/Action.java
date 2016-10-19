package game.action;

import game.exception.ActionException;
import game.model.Game;
import game.model.Lobby;
import game.model.Room;
import game.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Base action which is parent of every single action
 * Actions MUST alter only client or server model
 * Created by andrea on 18/10/16.
 */
public class Action{
    private List rooms;
    private Game game;

    public void setRooms (List<Room> rooms){
        this.rooms = new ArrayList<Room>(rooms);
    }

    public List getRooms() {
        return rooms;
    }

    public void doAction(Lobby lobby, Game game, User user) throws ActionException{}
}
