package game.action;

import game.exception.ActionException;
import game.model.User;
import game.model.UserState;
import server.controller.GameLogic;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Action sent from client to server to set preferences like avatar
 */
public class AvatarChosen extends Action {
    private String avatarURL;

    /**
     * Prepares payload
     *
     * @param avatarURL
     */
    public AvatarChosen(String avatarURL){
        this.avatarURL = avatarURL;
    }

    /**
     * Save them in user obj
     *
     * @param user
     * @throws ActionException
     */
    @Override
    public void doAction(User user) throws ActionException {//Called on server when we login to it.
        try {
            //@TODO check if avatar exists
            //Image image1 = new Image("/game/resources/avatars/" + avatarURL);
        }catch (Exception e){
            e.printStackTrace();
            GameLogic.getInstance().changeView((server.model.User)user, ChangeView.GoTo.CHOOSE_AVATAR);//Changes user's view to PREFERENCES
            GameLogic.getInstance().sendInfoMessageTo((server.model.User) user, "Avatar non valido");//Username already taken
            return;
        }
        user.setAvatarURL(avatarURL);
        user.setUserState(UserState.LOBBY);//Set user state to (in)LOBBY
        GameLogic.getInstance().changeView((server.model.User)user, ChangeView.GoTo.CHOOSE_CARDS);//Changes user's view to SELECT_ROOM
        GameLogic.getInstance().sendUpdateUserTo((server.model.User) user);//Sends user an updated snapshot of himself, including new username
        GameLogic.getInstance().sendMatchesListTo((server.model.User) user);//Sends user a list of all active matches
    }
}