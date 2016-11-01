package game.model;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 18/10/16.
 */
public class User {
    private String username = "undefined";
    private UserState userState = UserState.LOGIN;
    private transient List<Card> cards = new ArrayList<>();// user's cards

    public User(String username) {
        this.username = username;
    }

    public User() {
    }


    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    /**
     * Updates the object's properties with donor's ones
     *
     * @param user property donor
     */
    public void updateFrom(User user) {
        this.username = user.username;
        this.userState = user.userState;
        if (!user.cards.isEmpty()) {
            this.cards = new ArrayList<Card>(user.cards);
        }
    }

    /**
     * debug...
     *
     * @return
     */
    @Override
    public String toString() {
        String cardString = "";
        if (!cards.isEmpty()) {
            for (Object card :
                    cards.toArray())
                cardString = cardString.concat("," + card.toString());
        }
        return "[username=" + username + ", userState=" + userState.toString() + ", cards=" + cardString + "]";
    }

    /**
     * Two users are the same user if the have same username. Username is primary key.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;
        if (object != null && object instanceof User) {
            sameSame = this.username.equals(((User) object).username);
        }
        return sameSame;
    }

    /**
     * BTW don't know if it's good...
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 31 * username.length();
    }
}
