package game.action;

import game.exception.ActionException;
import game.model.Match;
import game.model.User;

/**
 * Created by Andrea on 10/24/2016.
 */
public class UpdateMatch extends Action {
    private Match match;
    public UpdateMatch(Match match){
        this.match = match;
    }

    @Override
    public void doAction(User user) throws ActionException {
        System.out.println("NOT IMPLEMENTED, UPDATE LOCAL MATCH WITH RECEIVED");
    }
}
