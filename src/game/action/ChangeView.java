package game.action;

import game.exception.ActionException;
import game.model.User;
import gameClient.SceneController;

/**
 * Used by server to change client's view
 */
public class ChangeView extends Action {
    public enum GoTo {
        LOGIN,
        SELECT_ROOM
    }

    private GoTo goTo;

    public ChangeView(GoTo goTo) {
        this.goTo = goTo;
    }

    @Override
    public void doAction(User user) throws ActionException {
        switch (goTo){
            case LOGIN:
                SceneController.getInstance().showLogin();
                break;
            case SELECT_ROOM:
                SceneController.getInstance().showSelectRoom();
                break;
        }
    }
}
