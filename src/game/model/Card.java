package game.model;

/**
 * Created by andrea on 27/10/16.
 */

/**
 * Card object
 */
public class Card {
    private CardSuit cardSuit;
    private CardType cardType;

    private Card() {
    }

    public Card(CardType cardType, CardSuit cardSuit) {
        this.cardSuit = cardSuit;
        this.cardType = cardType;
    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public CardType getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return cardType.toString() + " di " + cardSuit.toString();
    }
}
