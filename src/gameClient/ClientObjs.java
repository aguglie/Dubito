package gameClient;

import game.model.*;
import gameClient.network.SocketWriter;
import gameClient.utils.TreeTableRowObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MyLogger;

import java.util.ArrayList;
import java.util.Collections;

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
            /*
            ArrayList<Card> cards = new ArrayList<Card>(40);
            for (CardType type : CardType.values()) {
                for (CardSuit suit : CardSuit.values()) {
                    cards.add(new Card(type, suit));
                }
            }
            Collections.shuffle(cards);////Debug debug debug
            for (int i = 0; i < 10; i++) {
                cards.remove(i);
            }
            user.setCards(cards);
            */
        }
        return user;
    }

    public static void debug(){
        System.out.println(getMatch().toString());
    }
    public static SocketWriter getSocketWriter() {
        return socketWriter;
    }

    public static void setSocketWriter(SocketWriter socketWriter) {
        ClientObjs.socketWriter = socketWriter;
    }

    public static ObservableList<Match> getMatchList() {
        return matchList;
    }

    public static void setMatchList(ObservableList<Match> matchList) {
        ClientObjs.matchList = matchList;
    }
}
