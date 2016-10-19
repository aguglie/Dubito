package game.action;

import game.exception.ActionException;
import game.model.Game;
import game.model.Lobby;
import game.model.Room;
import game.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class SyncRooms extends Action {
    @Override
    public void doAction(Lobby lobby, Game game, User user)  throws ActionException {
        if (getRooms().isEmpty()){
            throw new ActionException("No rooms in here!");
        }else if (lobby==null){
            throw new ActionException("No lobby object");
        }else{
            lobby.setRooms(getRooms());
        }
    }
}
