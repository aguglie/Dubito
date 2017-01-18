package gameClient.controller;

import gameClient.SceneDirector;
import gameClient.utils.GuiHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseAvatarController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private ImageView rightButton;

    @FXML
    private ImageView leftButton;

    @FXML
    private Pane pane;

    private ImageView[] avatars = new ImageView[28];

    private int selectedAvatarKey = 0;

    private boolean isTimelinePlaying = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneDirector.getInstance().setRoot(root);

        //Loads all avatars
        for (int i=0; i<28; i++) {
            avatars[i] = new ImageView();
            avatars[i].setImage(new Image("/game/resources/avatars/avatar"+String.format("%05d", i)+".png", 200, 200, false, true));
            pane.getChildren().add(avatars[i]);
            avatars[i].setX(-500);
            avatars[i].setY(root.getHeight());

            final int key = i;
            avatars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> placeAvatars(key));
        }

        rightButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            moveAvatars(+1);
        });

        leftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            moveAvatars(-1);
        });

        /////BUG?! root width/heigh are not ready here... we have to wait some time, is it JavaFX bug?
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> moveAvatars(0)));
        timeline.play();
    }

    private void moveAvatars(int i){
        if (isTimelinePlaying) return;//if we are playing animation, abort
        selectedAvatarKey = selectedAvatarKey+i;
        placeAvatars(selectedAvatarKey);
    }

    private void placeAvatars(int centralKey){
        if (isTimelinePlaying) return;

        //prepare next objects
        avatars[calculateKey(centralKey-2)].setX(-200);
        avatars[calculateKey(centralKey-2)].setY(root.getHeight());
        avatars[calculateKey(centralKey+2)].setX(root.getWidth()+150);
        avatars[calculateKey(centralKey+2)].setY(root.getHeight());

        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        GuiHelper.moveImageView(timeline, avatars[calculateKey(centralKey-1)], 100, root.getHeight()/2-100, 0, 1d, 200);
        GuiHelper.moveImageView(timeline, avatars[calculateKey(centralKey)], root.getWidth()/2-100, root.getHeight()/2-100, 0, 1.6d, 200);
        GuiHelper.moveImageView(timeline, avatars[calculateKey(centralKey+1)], root.getWidth()-100-170, root.getHeight()/2-100, 0, 1d, 200);

        selectedAvatarKey = centralKey;//We have to update the selected/central item
        isTimelinePlaying = true;
        timeline.play();
        timeline.setOnFinished((event)-> isTimelinePlaying = false);

    }

    private int calculateKey(int i){
        if (i<0){
            return avatars.length-Math.abs(i)%avatars.length;
        }
        return Math.abs(i)%avatars.length;
    }
}