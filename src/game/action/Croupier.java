package game.action;

import game.exception.ActionException;
import game.model.Card;
import game.model.CardType;
import game.model.User;
import gameClient.SceneDirector;
import gameClient.controller.GameController;

import java.util.List;

/**
 * Used by server to move cards of other users on GUI
 */
public class Croupier extends Action {
    public enum ActionType {
        PUT_CARDS,
        PICK_CARDS
    }
    private ActionType actionType;
    private int number;
    private User serverUser;
    private CardType cardType;
    private List<Card> cardsPicked;


    /**
     * Message sent to all users except the one who puts cards
     * @param actionType
     * @param user
     * @param number
     * @param cardType
     */
    public Croupier(ActionType actionType, User user, int number, CardType cardType) {   //Used when ActionType = PUT_CARDS
        this.actionType = actionType;
        this.number = number;
        this.serverUser = user;
        this.cardType = cardType;
    }

    /**
     * Message sent to all users, only user who has to pick cards receives cardsPicked
     * @param actionType
     * @param user
     * @param cardsPicked
     */
    public Croupier(ActionType actionType, User user, List<Card> cardsPicked) {//Used when ActionType = PICK_CARDS
        this.actionType = actionType;
        this.serverUser = user;
        this.cardsPicked = cardsPicked;
    }

    @Override
    public void doAction(User user) throws ActionException {
        switch (actionType){
            case PUT_CARDS:
                GameController.getGameController().userPutsCards(serverUser, number, cardType);
                break;
            case PICK_CARDS:
                GameController.getGameController().userPicksCards(serverUser, cardsPicked);
        }
    }
}