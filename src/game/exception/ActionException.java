package game.exception;

/**
 * Created by andrea on 18/10/16.
 */
public class ActionException extends RuntimeException {

    public ActionException() {
        super();
    }

    public ActionException(String message) {
        super(message);
    }
}