package game.action;

import game.exception.ActionException;
import game.model.User;

/**
 * Created by andrea on 23/10/16.
 */
public class Ack extends Action {
    public enum MessageType {
        INFO,
        WARNING,
        DANGER;
    }

    private String message;
    private MessageType messageType;

    public Ack(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    @Override
    public void doAction(User user) throws ActionException {
        System.out.println("||" + messageType.toString() + "|||| " + message + "||");
    }
}
