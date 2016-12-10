package game.action;

import game.exception.ActionException;
import game.model.User;
import gameClient.SceneDirector;

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
                SceneDirector.getInstance().showLogin();
                break;
            case SELECT_ROOM:
                SceneDirector.getInstance().showSelectRoom();
                break;
        }
    }
}
