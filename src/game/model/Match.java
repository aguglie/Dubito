package game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 19/10/16.
 */
public class Match {
    private String name;
    private static List matches = new ArrayList<server.model.Match>();//All active matches
    private MatchState matchState = MatchState.WAITING;

    public Match(String name) {
        this.name = name;
        matches.add(this);//Add created match to list of matches
    }

    public Match() {
        this("Untitled");//Default name

    }

    /**
     * Retrives match name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets match name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * Sets matchState
     * @param matchState
     */
    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    /**
     * Gets matchState
     * @return
     */
    public MatchState getMatchState() {
        return matchState;
    }
}
