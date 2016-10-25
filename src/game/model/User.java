package game.model;

/**
 * Created by andrea on 18/10/16.
 */
public class User {
    private String username = "undefined";
    private UserState userState = UserState.LOGIN;

    public User(String username) {
        this.username = username;
    }

    public User() {
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
    }

    /**
     * debug...
     *
     * @return
     */
    @Override
    public String toString() {
        return "[username=" + username + ", userState=" + userState.toString() + "]";
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
