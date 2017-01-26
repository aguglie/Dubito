package game.action;

import game.exception.ActionException;
import game.model.Card;
import game.model.CardType;
import game.model.User;
import game.model.UserState;
import server.controller.GameLogic;
import server.model.Match;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Asks server to join a match room
 */
public class DiscardCards extends Action {
    private ArrayList<Card> cards;//Cards played


    /**
     * Asks server to discard selected cards
     * @param cards
     */
    public DiscardCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Discards cards
     *
     * @param user
     * @throws ActionException
     */
    @Override
    public void doAction(User user) throws ActionException {
        GameLogic gameLogic = GameLogic.getInstance();
        if (cards==null) return;
        if (user.getUserState() != UserState.PLAYING) {
            gameLogic.sendDangerMessageTo((server.model.User) user, "You must be in game to perform this!");
            return;
        }
        if (cards.size()<4) {
            gameLogic.sendDangerMessageTo((server.model.User) user, "Puoi scartare solo quattro carte dello stesso tipo");
            return;
        }
        if (!user.getCards().containsAll(cards)){
            gameLogic.sendDangerMessageTo((server.model.User) user, "Non possiedi queste carte");
            return;
        }
        CardType cardType = cards.get(0).getCardType();
        for (Object card: cards.toArray()){
            if (((Card)card).getCardType() != cardType){
                gameLogic.sendDangerMessageTo((server.model.User) user, "Puoi scaricare solo carte dello stesso tipo");
                return;
            }
        }
        user.getCards().removeAll(cards);
        game.model.Match match = ((server.model.User) user).getMatch();
        gameLogic.sendDiscardCardsTo((server.model.Match)match, (server.model.User)user, cards);
        gameLogic.sendUpdateUserTo((server.model.User)user);

        if (user.getCards().size()==0){//User Just won!
            GameLogic.getInstance().userWon((server.model.User)user);
        }
    }
}