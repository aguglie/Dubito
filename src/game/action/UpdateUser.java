package game.action;

import game.exception.ActionException;
import game.model.Card;
import game.model.User;
import gameClient.ClientObjs;
import gameClient.controller.GameController;

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
        ClientObjs.getUser().updateFrom(serverUser);//Updates client's user properties with server's
        ClientObjs.getUser().setCards(cards);
        if (GameController.getGameController() != null){
            GameController.getGameController().clearSelectedCards();//Reset selected cards //TODO check this perhaps not best location
            GameController.getGameController().uncoverCards((ArrayList<Card>)cards);//Make sure cards belonging to user are uncovered
            GameController.getGameController().displayUserHand(ClientObjs.getUser().getCards());//Display all cards in GUI
        }
    }
}
