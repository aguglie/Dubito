package game.action;

import game.exception.ActionException;
import game.model.User;
import utils.MyLogger;

/**
 * Created by andrea on 23/10/16.
 */
public class UpdateUser extends Action {
    private User serverUser;

    public UpdateUser(User user) {
        this.serverUser = user;
    }

    @Override
    public void doAction(User user) throws ActionException {//doAction called on client
        user.updateFrom(serverUser);//Updates client's user properties with server's
    }
}
