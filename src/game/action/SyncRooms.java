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
    protected List rooms = new ArrayList<Room>(4);
    @Override
    public void doAction(Lobby lobby, Game game, User user) throws ActionException {
        lobby.setRooms(rooms);
    }

    @Override
    public void prepareAction(Lobby lobby, Game game, User user) throws ActionException {
        rooms = new ArrayList<>(lobby.getRooms());
    }
}