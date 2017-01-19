package game.model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 18/10/16.
 */
public class User {
    private String username = "undefined";
    private UserState userState = UserState.LOGIN;
    private String avatarURL = "avatar00000.png";//Default avatar
    private transient List<Card> cards = new ArrayList<>();// user's cards
    private transient ImageView userAvatarImageView;
    private transient VBox userAvatarVBox;

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

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    /**
     * Returns a VBox with user's avatar and username
     * @return
     */
    public VBox getUserAvatarVBox(){
        if (userAvatarImageView==null){
            this.userAvatarImageView = new ImageView();
            this.userAvatarImageView.setImage(new Image("/game/resources/avatars/"+ avatarURL, 100, 100, true, true));
        }
        if (userAvatarVBox==null){
            userAvatarVBox = new VBox();
            Label usernameLabel = new Label(getUsername());
            usernameLabel.setTextFill(Color.WHITE);
            usernameLabel.setFont(new Font("Arial", 20));
            userAvatarVBox.getChildren().addAll(userAvatarImageView, usernameLabel);
            userAvatarVBox.setAlignment(Pos.CENTER);
        }
        return userAvatarVBox;
    }

    /**
     * Updates the object's properties with donor's ones
     *
     * @param user property donor
     */
    public void updateFrom(User user) {
        this.username = user.username;
        this.userState = user.userState;
        this.avatarURL = user.avatarURL;
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
