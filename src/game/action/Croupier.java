package game.action;

import game.exception.ActionException;
import game.model.Card;
import game.model.CardType;
import game.model.User;
import gameClient.SceneDirector;
import gameClient.controller.GameController;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by server to move cards of other users on GUI
 */
public class Croupier extends Action {
    public enum ActionType {
        PUT_CARDS,
        PICK_CARDS,
        DISCARD_CARDS
    }
    private ActionType actionType;
    private User serverUser;
    private List<Card> cards;
    private CardType cardType;

    /**
     * Message sent to all clients to move cards.
     * @param actionType
     * @param user
     * @param cards
     */
    public Croupier(ActionType actionType, User user, List<Card> cards, CardType cardType) {
        this.actionType = actionType;
        this.serverUser = user;
        this.cards = cards;
        this.cardType = cardType;
    }

    @Override
    public void doAction(User user) throws ActionException {
        switch (actionType){
            case PUT_CARDS:
                GameController.getGameController().userPutsCards(serverUser, cards, cardType);
                break;
            case PICK_CARDS:
                GameController.getGameController().userPicksCards(serverUser, cards);
                break;
            case DISCARD_CARDS:
                GameController.getGameController().discardCards(serverUser, cards);
        }
    }
}