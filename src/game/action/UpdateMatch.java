package game.action;


import game.exception.ActionException;
import game.model.Match;
import game.model.User;
import gameClient.ClientObjs;
import gameClient.controller.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrea on 10/24/2016.
 */

/**
 * Sends client an updated snapshot of the match he joined
 */
public class UpdateMatch extends Action {
    private Match match;
    private List<User> enemies;

    /**
     * Prepares payload
     *
     * @param match
     */
    public UpdateMatch(server.model.Match match) {
        this.match = match;
        this.enemies = new ArrayList<User>(match.getUsers());
    }

    /**
     * Updates client Match object with user's enemies, match status etc..
     *
     * @param user
     * @throws ActionException
     */
    @Override
    public void doAction(User user) throws ActionException {
        ClientObjs.getMatch().updateFrom(match);//This updates only superclass properties :(

        //creates the enemies list
        enemies.remove(user);//User is not enemy of himself!
        ClientObjs.getMatch().setEnemies(enemies);

        //gameClient.ClientObjs.debug();
        if (GameController.getGameController()!=null) {
            GameController.getGameController().onUpdateMatch();
        }
    }
}
