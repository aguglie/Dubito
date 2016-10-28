package server.model;

/**
 * Created by andrea on 28/10/16.
 */

import game.exception.ActionException;
import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;

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
}