package game.model;

/**
 * Created by andrea on 19/10/16.
 */
public class Match {
    private String name;
    private MatchState matchState = MatchState.WAITING;

    public Match(String name) {
        this.name = name;
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

    /**
     * Updates the object's properties with donor's ones
     * @param match donor
     */
    public void updateFrom(Match match){
        this.name = match.name;
        this.matchState = match.matchState;
    }
}
