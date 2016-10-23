package game.action;

import game.exception.ActionException;
import game.model.User;
import server.model.Match;
import utils.MyLogger;

import java.util.List;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * This Action is used to send to the client a List containing all active Matches(aka Game Rooms)
 */
public class UpdateMatchList extends Action {
    private List match;

    public UpdateMatchList(List match){
        this.match = match;
    }

    @Override
    public void doAction(User user) throws ActionException {//Runs on client.
        MyLogger.println("UpdateMatchList chiamata, non implementato");
    }
}
