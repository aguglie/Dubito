package game.action;

import game.exception.ActionException;
import game.model.MatchState;
import game.model.User;
import game.model.UserState;
import gameClient.ClientObjs;
import gameClient.controller.GameController;
import server.controller.GameLogic;
import server.model.Match;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Asks server to join a match room
 */
public class GameOver extends Action {
    private User winner;

    public GameOver(User winner) {
        this.winner = winner;
    }

    @Override
    public void doAction(User user) throws ActionException {
        GameController.getGameController().gameOver(winner);
    }
}
