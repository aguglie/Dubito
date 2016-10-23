package game.action;

import game.exception.ActionException;
import game.model.User;
import game.model.UserState;
import server.controller.GameLogic;
import utils.MyLogger;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Action sent from client to server right after the connection to set the username
 */
public class JoinServer extends  Action{
    private String username;

    public JoinServer(String username){
        this.username = username;
    }

    @Override
    public void doAction(User user) throws ActionException {//Called on server when we login to it.
        user.setUsername(this.username);//Set username
        user.setUserState(UserState.LOBBY);//Set user state to (in)LOBBY, thiw will also update client GUI
        GameLogic.getInstance().sendMatchesListTo((server.model.User)user);//Send to user a list of all matches
        GameLogic.getInstance().sendUpdateUserTo((server.model.User)user);//Send to user an updated snapshot of himself
    }
}
