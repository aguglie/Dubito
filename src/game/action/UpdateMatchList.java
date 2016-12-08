package game.action;

import game.exception.ActionException;
import game.model.User;
import gameClient.utils.TreeTableRowObj;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by andrea on 23/10/16.
 */

/**
 * This Action is used to send to the client a List containing all active Matches(aka Game Rooms)
 */
public class UpdateMatchList extends Action {
    private List<game.model.Match> match;

    public UpdateMatchList(List match) {
        this.match = match;
    }

    @Override
    public void doAction(User user) throws ActionException {//Runs on client.
        //ObservableList<TreeTableRowObj> rows = FXCollections.observableArrayList();
        //match.forEach(match1 -> {
        //    rows.add(new TreeTableRowObj())
        //});

        //// TODO: 23/10/16 directly fill GUI with Matches
        /*
        System.out.println("Stanze disponibili:");
        for (int i = 0; i < match.size(); i++) {
            System.out.println(i + ") " + match.get(i).getName());
        }
        */
    }
}
