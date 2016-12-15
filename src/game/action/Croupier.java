package game.action;

import game.exception.ActionException;
import game.model.CardType;
import game.model.User;
import gameClient.SceneDirector;
import gameClient.controller.GameController;

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


    public Croupier(ActionType actionType, User user, int number, CardType cardType) {
        this.actionType = actionType;
        this.number = number;
        this.serverUser = user;
        this.cardType = cardType;
    }

    public Croupier(ActionType actionType, User user) {
        this.actionType = actionType;
        this.serverUser = user;
    }

    @Override
    public void doAction(User user) throws ActionException {
        switch (actionType){
            case PUT_CARDS:
                GameController.getGameController().userPutsCards(serverUser, number, cardType);
                break;
            case PICK_CARDS:
                GameController.getGameController().userPicksCards(serverUser);
        }
    }
}