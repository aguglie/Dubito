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
        CHOOSE_AVATAR,
        CHOOSE_CARDS,
        SELECT_ROOM,
        GAME
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
            case CHOOSE_AVATAR:
                SceneDirector.getInstance().showChooseAvatar();
                break;
            case CHOOSE_CARDS:
                SceneDirector.getInstance().showChooseCards();
                break;
            case SELECT_ROOM:
                SceneDirector.getInstance().showSelectRoom();
                break;
            case GAME:
                SceneDirector.getInstance().showGame();
                break;
        }
    }
}
