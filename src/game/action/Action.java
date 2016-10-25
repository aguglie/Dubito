package game.action;

import game.exception.*;
import game.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Base action which is parent of every single action
 * Actions MUST alter only client or server model
 * Created by andrea on 18/10/16.
 */
public abstract class Action {
    public void doAction(User user) throws ActionException {
    }
}
