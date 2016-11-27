package server.model;

/**
 * Created by andrea on 28/10/16.
 */

import game.exception.ActionException;
import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Deck is only server side
 */
public class Deck {
    private List cards = new ArrayList<Card>(40);

    public Deck() {
        for (CardType type : CardType.values()) {
            for (CardSuit suit : CardSuit.values()) {
                cards.add(new Card(type, suit));
            }
        }
        Collections.shuffle(cards);
        if (cards.size() != 40) throw new RuntimeException("You messed up cards");//this stops server
    }

    public Card getCard() throws ActionException {
        if (!cards.isEmpty()) {
            Card card = (Card) cards.get(0);//Pick a card from deck
            cards.remove(card);//Remove it from deck
            return card;
        } else {
            throw new ActionException("No more cards left");
        }
    }

    /**
     * Gives every user 40/#users cards
     *
     * @param users
     * @throws ActionException
     */
    public void giveCards(List<User> users) throws ActionException {
        if (cards.isEmpty()) throw new ActionException("No more cards left");
        if (users.isEmpty()) throw new ActionException("No users passed");
        int card_to_user = cards.size() / users.size();//numbers of cards per user

        for (User user : users) {
            MyLogger.println("Giving card to " + user.toString());
            for (int i = 1; i <= card_to_user; i++) {
                user.getCards().add(this.getCard());//gives a card to selected user.
            }
        }
    }
}