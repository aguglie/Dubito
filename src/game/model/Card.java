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

    /**
     * Two cards are the same if they have same suit and type
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;
        if (object != null && object instanceof Card) {
            sameSame = this.cardSuit.equals(((Card) object).cardSuit) && this.cardType.equals(((Card) object).cardType);
        }
        return sameSame;
    }

    /**
     * BTW don't know if it's good...
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 31 * (cardSuit.hashCode() + cardType.hashCode());
    }
}
