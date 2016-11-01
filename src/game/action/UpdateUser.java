package game.action;

import game.exception.ActionException;
import game.model.Card;
import game.model.User;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * This syncs server User model with client's one
 */
public class UpdateUser extends Action {
    private User serverUser;
    private List<Card> cards;

    public UpdateUser(User user) {
        this.serverUser = user;
        this.cards = new ArrayList<>(user.getCards());
    }

    @Override
    public void doAction(User user) throws ActionException {//doAction called on client
        user.updateFrom(serverUser);//Updates client's user properties with server's
        user.setCards(cards);
    }
}
