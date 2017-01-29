package game.action;

import game.exception.ActionException;
import game.model.MatchState;
import game.model.User;
import game.model.UserState;
import server.controller.GameLogic;
import server.model.Match;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * Asks server to join a match room
 */
public class JoinMatch extends Action {
    private int match_index;

    /**
     * Prepares payload
     *
     * @param match_index index of match in server match list
     */
    public JoinMatch(int match_index) {
        this.match_index = match_index;
    }

    /**
     * Tries to add user to specified Match Room.
     *
     * @param user
     * @throws ActionException
     */
    @Override
    public void doAction(User user) throws ActionException {
        /*
        if (user.getUserState() != UserState.LOBBY) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, "You must be in lobby to perform this!");
            return;
        }
        */
        try {
            Match match = (Match) Match.getMatches().get(match_index);//Search the match
            if (match.getMatchState() != MatchState.WAITING_START) {
                GameLogic.getInstance().sendInfoMessageTo((server.model.User) user, "Accidenti, il match e' in corso!");//Match already started :(
                return;
            }
            if (((server.model.User)user).getMatch()!=null){
                ((server.model.User)user).getMatch().removeUser((server.model.User)user);
            }
            match.addUser((server.model.User) user);//Add user to that match
            ((server.model.User) user).setMatch(match);//Connect user to his match
            user.setUserState(UserState.WAITING_START);//Set user state as waiting (to start play Dubito)

            GameLogic.getInstance().sendUpdateUserTo((server.model.User)user);
            GameLogic.getInstance().sendUpdateMatchTo(match);//Send every user an updated snapshot of the match
            //GameLogic.getInstance().changeView((server.model.User)user, ChangeView.GoTo.SELECT_ROOM);//Send user to GAME_ROOM // TODO: 08/12/16 correct this!
            GameLogic.getInstance().sendInfoMessageTo((server.model.User) user, "Sei in " + match.getName()+" assieme ad altri "+(match.getUsers().size()-1)+" giocatori");//Send him an alert

        } catch (Exception e) {
            GameLogic.getInstance().sendDangerMessageTo((server.model.User) user, e.getMessage());//Smth bad happened here...
        }

    }
}
