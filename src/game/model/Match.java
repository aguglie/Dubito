package game.model;

import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */

/**
 * Every match is a possible dubito play.
 */
public class Match {
    private String name;
    private MatchState matchState = MatchState.WAITING;
    private transient List enemies;


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
}
