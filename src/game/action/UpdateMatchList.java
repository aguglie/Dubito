package game.action;

import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import game.exception.ActionException;
import game.model.Match;
import game.model.User;
import gameClient.ClientObjs;
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
        ClientObjs.getMatchList().add(new Match("Porcoddio"));
        //ClientObjs.getMatchList().clear();
        //ClientObjs.getMatchList().addAll(match);
    }
}
