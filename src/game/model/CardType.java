package game.model;

/**
 * Created by andrea on 27/10/16.
 */

/**
 * Card type
 */
public enum CardType {
    ASSO(0),
    DUE(1),
    TRE(2),
    QUATTRO(3),
    CINQUE(4),
    SEI(5),
    SETTE(6),
    FANTE(7),
    CAVALLO(8),
    RE(9);

    public int value;

    CardType(int value) {
        this.value = value;
    }

    public int getNameAsInt() {
        return value+1;
    }

    public int getAsArrayKey() {
        return value;
    }
}
