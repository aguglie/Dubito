package game.exception;

/**
 * Created by andrea on 18/10/16.
 */
public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }
}