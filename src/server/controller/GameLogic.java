package server.controller;

import game.action.Ack;
import game.action.UpdateMatch;
import game.action.UpdateMatchList;
import game.action.UpdateUser;
import game.exception.ActionException;
import game.model.MatchState;
import game.model.UserState;
import server.model.Deck;
import server.model.Match;
import server.model.User;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * GameLogic(room) where users have to join/create a room to play in
 */
public class GameLogic {
    private static GameLogic gameLogic;

    private GameLogic() {
        new Match("Merda Room");
    }

    /**
     * Singleton pattern
     *
     * @return
     */
    public static GameLogic getInstance() {
        if (gameLogic == null) {
            gameLogic = new GameLogic();
        }
        return gameLogic;
    }

    /**
     * shows Warning Modal on client
     *
     * @param user
     * @param message
     */
    public void sendWarningMessageTo(User user, String message) {
        Ack ack = new Ack(message, Ack.MessageType.WARNING);
        user.getSocketHandler().sendAction(ack);
    }

    /**
     * shows Danger Modal on client
     *
     * @param user
     * @param message
     */
    public void sendDangerMessageTo(User user, String message) {
        Ack ack = new Ack(message, Ack.MessageType.DANGER);
        user.getSocketHandler().sendAction(ack);
    }

    /**
     * shows Info Modal on client
     *
     * @param user
     * @param message
     */
    public void sendInfoMessageTo(User user, String message) {
        Ack ack = new Ack(message, Ack.MessageType.INFO);
        user.getSocketHandler().sendAction(ack);
    }

    /**
     * Sends a list with all active matches to an user
     *
     * @param user receiver of all matches
     */
    public void sendMatchesListTo(User user) {
        UpdateMatchList updateMatchList = new UpdateMatchList(Match.getMatches());//Creates a new updateMatchList containing a list of all matches
        user.getSocketHandler().sendAction(updateMatchList);//Sends an updateMatchList to the specified user
    }

    /**
     * Syncs local user with server's one
     *
     * @param user
     */
    public void sendUpdateUserTo(User user) {
        UpdateUser updateUser = new UpdateUser(user);
        user.getSocketHandler().sendAction(updateUser);//Sync client's user data with server's
    }

    public void sendUpdateUserTo(Match match) {
        List<User> userList = match.getUsers();
        userList.forEach(u -> sendUpdateUserTo(u));
    }

    /**
     * Sends to specified user a personalized copy of server's match obj (with his enemies etc..)
     *
     * @param user
     */
    public void sendUpdateMatchTo(User user) {
        UpdateMatch updateUser = new UpdateMatch(user.getMatch());
        user.getSocketHandler().sendAction(updateUser);//Sync client's user data with server's
    }

    /**
     * Sends to every match's user a personalized copy of server's match obj (with his enemies etc..)
     *
     * @param match
     */
    public void sendUpdateMatchTo(Match match) {
        List<User> userList = match.getUsers();
        userList.forEach(u -> sendUpdateMatchTo(u));
    }

    /**
     * Carries user out of match and brings him to Lobby
     *
     * @param user
     * @throws ActionException
     */
    public void leaveMatch(User user) throws ActionException {
        if (user.hasMatch()) {
            //// TODO: 26/10/16 send chat message with byebye
            user.getMatch().removeUser(user);//Remove user from match
            this.sendUpdateMatchTo(user.getMatch());//Update other users
            user.setMatch(null);//remove user reference to match
            user.setUserState(UserState.LOBBY);//bring him back to lobby
        } else throw new ActionException(user.getUsername() + " is not in a match");
    }

    /**
     * Starts the specified match if possible
     *
     * @param match
     * @throws ActionException
     */
    public void startMatch(Match match) throws ActionException {
        if (match == null) throw new ActionException("Game dosn't exist");
        if (match.getUsers().size() < 2)
            throw new ActionException("You need at least two users to start a match");//A user can't play alone... it's sad!

        match.setMatchState(MatchState.PLAYING);//Set match as started
        match.getUsers().forEach((user) -> ((User) user).setUserState(UserState.PLAYING));//set each user as playing

        Deck deck = new Deck();//Creates a new deck.
        deck.giveCards(match.getUsers());//divides cards

        this.sendUpdateUserTo(match);//Send every match user the updated objects
        this.sendUpdateMatchTo(match);//Send every match user the updated objects
        MyLogger.println("Match started");
    }

    /**
     * Remove every reference to this user
     *
     * @param user
     */
    public void disconnectUser(User user) {
        try {
            this.leaveMatch(user);
        } catch (Exception e) {
        }
        user.destroyReferences();//Destroys user reference to game objs
        MyLogger.println(user.getUsername() + " left");
    }
}
