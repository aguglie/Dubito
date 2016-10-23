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
     * @param user property donor
     */
    public void updateFrom(User user){
        this.username = user.username;
        this.userState = user.userState;
    }

    @Override
    public String toString() {
        return username + "@" + userState.toString() + ":" + this.hashCode();
    }
}
