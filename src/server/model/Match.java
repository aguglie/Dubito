package server.model;

import game.exception.UserException;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class Match extends game.model.Match {
    private transient List users = new ArrayList<User>();//Users in this match
    private transient static List matches = new ArrayList<Match>();//All active matches

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
}
