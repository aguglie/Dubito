package gameClient;

import game.model.Match;
import game.model.User;
import gameClient.network.SocketWriter;
import gameClient.utils.TreeTableRowObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private static SocketWriter socketWriter;
    private static ObservableList<Match> matchList = FXCollections.observableArrayList();

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

    public static SocketWriter getSocketWriter() {
        return socketWriter;
    }

    public static void setSocketWriter(SocketWriter socketWriter) {
        ClientObjs.socketWriter = socketWriter;
    }

    public static void debug() {
        MyLogger.println(getUser().toString());
        MyLogger.println(getMatch().toString());
    }

    public static ObservableList<Match> getMatchList() {
        return matchList;
    }

    public static void setMatchList(ObservableList<Match> matchList) {
        ClientObjs.matchList = matchList;
    }
}
