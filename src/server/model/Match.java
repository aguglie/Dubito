package server.model;

import game.action.UserPlay;
import game.exception.UserException;
import game.model.Card;
import server.controller.GameLogic;
import utils.MyLogger;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class Match extends game.model.Match {
    private transient List<User> users = new ArrayList<User>();//Users in this match
    private transient static List matches = new ArrayList<Match>();//All active matches
    private transient List<Card> tableCardsList = new ArrayList<>();//All cards covered on table
    private transient UserPlay lastMove = null;//Last move happended on match


    public Match(String string) {
        super(string);
        matches.add(this);//Add created match to list of matches
    }

    /**
     * List all matches
     *
     * @return
     */
    public static List getMatches() {
        return matches;
    }

    /**
     * Adds user to this match
     *
     * @param user
     * @throws UserException
     */
    public void addUser(User user) throws UserException {
        if (users.contains(user)) {
            throw new UserException("Already in this match");
        } else {
            users.add(user);
        }
    }

    /**
     * Removes user from this match
     *
     * @param user
     * @throws UserException
     */
    public void removeUser(User user) throws UserException {
        if (users.contains(user)) {
            users.remove(user);//remove user from match
            if (users.isEmpty()) {//if match is empty, remove match too!
                matches.remove(this);
                MyLogger.println(this.getName() + " deleted");
                GameLogic.getInstance().getConnectedUsers().forEach(u -> GameLogic.getInstance().sendMatchesListTo(u));
            }
        } else {
            throw new UserException("User is not in this match");
        }
    }

    /**
     * Retrives all users who joined this match
     *
     * @return
     */
    public List getUsers() {
        return users;
    }

    /**
     * Ends current user turn and starts next one.
     */
    public void nextTurn() {
        if (whoseTurn == null) {//whoseTurn is null when we first start the match
            whoseTurn = users.get(0);
        } else {
            int i = users.indexOf(whoseTurn);
            try {
                whoseTurn = users.get((i + 1));
            } catch (Exception e) {
                whoseTurn = users.get(0);
            }
        }
        MyLogger.println("it's " + whoseTurn.getUsername() + " turn.");
        if (whoseTurn.getCards().size() == 0) {
            MyLogger.println(whoseTurn.getUsername() + "just won");
            GameLogic.getInstance().userWon(((server.model.User)whoseTurn));
            whoseTurn = null;
            return;
        }
    }

    public UserPlay getLastMove() {
        return lastMove;
    }

    public void setLastMove(UserPlay lastMove) {
        this.lastMove = lastMove;
    }

    public List<Card> getTableCardsList() {
        return tableCardsList;
    }
}