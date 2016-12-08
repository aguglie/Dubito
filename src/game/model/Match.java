package game.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */

/**
 * Every match is a possible dubito play.
 */
public class Match extends RecursiveTreeObject<Match> {
    private String name;
    private MatchState matchState = MatchState.WAITING_START;
    private transient List enemies;//this variable is used when match in sent to client
    protected User whoseTurn = null;
    private transient SimpleStringProperty pName = new SimpleStringProperty("");//name but as property

    public Match(String name) {
        this.name = name;
    }

    public Match() {
        this("Untitled");//Default name

    }

    /**
     * Sets enemies list (only used in client)
     *
     * @param enemies
     */
    public void setEnemies(List enemies) {
        this.enemies = enemies;
    }

    /**
     * Retrives match name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets match name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets matchState
     *
     * @param matchState
     */
    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    /**
     * Gets matchState
     *
     * @return
     */
    public MatchState getMatchState() {
        return matchState;
    }

    /**
     * Updates the object's properties with donor's ones
     *
     * @param match donor
     */
    public void updateFrom(Match match) {
        this.name = match.name;
        this.matchState = match.matchState;
        this.whoseTurn = match.whoseTurn;
    }

    /**
     * As name says...
     *
     * @return
     */
    public User getWhoseTurn() {
        return whoseTurn;
    }

    /**
     * debug...
     *
     * @return
     */
    @Override
    public String toString() {
        if (enemies != null) {
            return "[name=" + name + ", matchState=" + matchState.toString() + ", enemiesList=" + enemies.toString() + "]";
        } else {
            return "[name=" + name + ", matchState=" + matchState.toString() + "]";
        }
    }

    /**
     * Get name as string property
     * @return
     */
    public SimpleStringProperty pNameProperty() {
        if (name != pName.get()){
            pName = new SimpleStringProperty(name);
        }
        return pName;
    }
}
