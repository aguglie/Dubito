package game.action;

import game.exception.ActionException;
import game.model.Match;
import game.model.User;
import server.controller.GameLogic;

/**
 * Created by andrea on 26/10/16.
 */

/**
 * Client asks server to start game
 */
public class StartGame extends Action {
    /**
     * Creates payload
     */
    public StartGame() {
    }

    @Override
    public void doAction(User user) throws ActionException {
        try {
            Match match = ((server.model.User) user).getMatch();
            GameLogic.getInstance().startMatch((server.model.Match) match);
        } catch (Exception e) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, e.getMessage());//Smth bad happened here...
            return;
        }
    }
}
