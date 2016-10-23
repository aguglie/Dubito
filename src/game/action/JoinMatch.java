package game.action;

import game.exception.ActionException;
import game.model.User;
import game.model.UserState;
import server.controller.GameLogic;
import server.model.Match;

/**
 * Created by andrea on 23/10/16.
 */
public class JoinMatch extends Action {
    private int match_index;

    /**
     * Ask server to join a match room
     *
     * @param match_index index of match in server match list
     */
    public JoinMatch(int match_index) {
        this.match_index = match_index;
    }

    @Override
    public void doAction(User user) throws ActionException {
        if (user.getUserState() != UserState.LOBBY) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, "You must be in lobby to perform this!");
            return;
        }
        try {
            Match match = (Match) Match.getMatches().get(match_index);//Search the match
            match.addUser((server.model.User) user);//Add user to that match
            user.setUserState(UserState.WAITING_START);//Set user state as waiting (to start play Dubito)
            GameLogic.getInstance().sendUpdateUserTo((server.model.User) user);//Update his User Info
            //// TODO: 23/10/16 send user match data
            //// TODO: 23/10/16 update other user match data
            GameLogic.getInstance().sendInfoMessageTo((server.model.User) user, "You joined " + match.getName());//Send him an alert
        } catch (Exception e) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, e.getMessage());
        }

    }
}
