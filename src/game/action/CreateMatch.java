package game.action;

import game.exception.ActionException;
import game.model.UserState;
import server.model.Match;
import game.model.User;
import server.controller.GameLogic;
import utils.MyLogger;

/**
 * This Action is used to create a new match
 */
public class CreateMatch extends Action {
    private String name;

    public CreateMatch(String name) {
        this.name = name;
    }

    @Override
    public void doAction(User user) throws ActionException {//Runs on client.
        if (user.getUserState() != UserState.LOBBY) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, "You must be in lobby to perform this!");
            return;
        }
        if (name != "") {
            new Match(name);
            MyLogger.println("New match "+ name + " created.");
        }
        GameLogic.getInstance().getConnectedUsers().forEach(u -> GameLogic.getInstance().sendMatchesListTo(u));
    }
}
