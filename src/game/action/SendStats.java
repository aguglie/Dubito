package game.action;

import game.exception.ActionException;
import game.model.User;
import gameClient.SceneDirector;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to generates JSON stat
 */
public class SendStats extends Action {
    private List<Math> matchList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    public SendStats(List<Math> mathList, List<User> userList) {
        this.matchList = mathList;
        this.userList = userList;
    }

    @Override
    public void doAction(User user) throws ActionException {
    }
}
