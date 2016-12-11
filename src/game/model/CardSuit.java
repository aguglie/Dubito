package game.model;

/**
 * Created by andrea on 27/10/16.
 */
public enum CardSuit {
    BASTONI(0, "basto"),
    COPPE(1, "coppe"),
    SPADE(2, "spade"),
    DENARI(3, "denar");

    private int value;
    private String resource;

    CardSuit(int value, String resource) {
        this.value = value;
        this.resource = resource;
    }

    public int getValue() {
        return value;
    }

    public String getResource() {
        return resource;
    }
}
