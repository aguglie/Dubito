package game.action;

import game.exception.ActionException;
import game.model.*;
import game.model.User;
import server.controller.GameLogic;
import server.model.*;
import server.model.Match;
import utils.MyLogger;

import java.util.ArrayList;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Asks server to join a match room
 */
public class UserPlay extends Action {
    public enum PlayType {
        DUBITO,
        PLAYCARD
    }

    private PlayType playType;//Dubito or Playing cards
    private CardType cardsType;//Pretended type of cards
    private ArrayList<Card> cards;//Cards played
    private server.model.User performer;//User who performed this action; filled by server when receives this req.


    /**
     * Only used to prepare 'DUBITO' action
     *
     * @param playType must be DUBITO
     */
    public UserPlay(PlayType playType) {
        this.playType = playType;
    }

    /**
     * Prepares move payload
     *
     * @param playType must be PLAYCARD
     * @param cards    cards you want to play
     * @param cardsType pretended suit
     */
    public UserPlay(PlayType playType, ArrayList<Card> cards, CardType cardsType) {
        this.playType = playType;
        this.cards = new ArrayList<>(cards);
        this.cardsType = cardsType;
    }

    /**
     * Tries to add user to specified Match Room.
     *
     * @param user
     * @throws ActionException
     */
    @Override
    public void doAction(User user) throws ActionException {
        GameLogic gameLogic = GameLogic.getInstance();
        if (user.getUserState() != UserState.PLAYING) {
            gameLogic.sendDangerMessageTo((server.model.User) user, "You must be in lobby to perform this!");
            return;
        }
        server.model.Match userMatch = ((server.model.User) user).getMatch();

        if (!userMatch.getWhoseTurn().equals(user)) {//if it's not user's turn
            gameLogic.sendDangerMessageTo((server.model.User) user, "It's not your turn!");
            return;
        }
        MyLogger.println("User " + user.getUsername() + " plays " + playType.toString());

        if (playType.equals(PlayType.DUBITO)) {//Handles 'dubito' moves.
            if (userMatch.getLastMove() == null || userMatch.getTableCardsList().size() == 0) {
                gameLogic.sendDangerMessageTo((server.model.User) user, "Non puoi dubitare se non ci sono carte sul piatto");
                return;
            }
            gameLogic.executeDubitoPlay((server.model.User) user, userMatch.getLastMove());//'DUBITO' action logic is here

        }

        if (playType.equals(PlayType.PLAYCARD)) {// Handles 'playcard' moves.
            this.performer = (server.model.User) user;
            try {
                gameLogic.moveCardsToTable(cards, this.performer);//Move selected cards from user deck to table
            } catch (Exception e) {
                gameLogic.sendDangerMessageTo((server.model.User) user, e.getMessage());
                return;
            }
            userMatch.setLastMove(this);//If everything is ok save this move as last happened
        }


        //Anyway turn ends and we send updated data back...
        userMatch.nextTurn();
        gameLogic.sendUpdateMatchTo(userMatch);
        gameLogic.sendUpdateUserTo(userMatch);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public CardType getCardsType() {
        return cardsType;
    }

    public server.model.User getPerformer() {
        return performer;
    }
}