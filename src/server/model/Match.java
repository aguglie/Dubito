package server.model;

import game.exception.UserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class Match extends game.model.Match {
    private transient List users = new ArrayList<User>();//Users in this match

    public Match(String string){
        super(string);
    }

    /**
     * Adds user to this match
     * @param user
     * @throws UserException
     */
    public void addUser(User user) throws UserException{
        if (users.contains(user)){
            throw new UserException("Already in this match");
        }else{
            users.add(user);
        }
    }
}
