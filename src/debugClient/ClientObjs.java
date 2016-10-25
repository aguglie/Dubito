package debugClient;

import game.model.Match;
import game.model.User;
import utils.MyLogger;

/**
 * Created by andrea on 25/10/16.
 */

/**
 * Main objects used by client to show GUI. These objects are signletons
 */
public class ClientObjs {
    private static Match match;
    private static User user;

    /**
     * Get the match showed by client
     *
     * @return
     */
    public static Match getMatch() {
        if (match == null) {
            match = new Match("Fake");
        }
        return match;
    }

    /**
     * Get user's instance
     *
     * @return
     */
    public static User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void debug() {
        MyLogger.println(getUser().toString());
        MyLogger.println(getMatch().toString());
    }
}
