package gameClient.controller;

import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;
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

public class ChooseCardsController implements Initializable{

    private ImageView[] cardsImageView = new ImageView[12];//Example cards
    private boolean isTimelinePlaying = false;
    private int selectedLocale = -1;


    @FXML
    private StackPane root;

    @FXML
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneDirector.getInstance().setRoot(root);


        //Populate example cards
        for (int x=0; x<CardLocale.values().length; x++){
            CardLocale cardLocale = CardLocale.values()[x];
            for (int i = 0; i < 4; i++) {
                CardType cardType = CardType.values()[i];
                CardSuit cardSuit = CardSuit.values()[i];

                Card card = new Card(cardType, cardSuit);

                String n;
                if ((card.getCardType().getNameAsInt()) < 10) {
                    n = "0" + card.getCardType().getNameAsInt();
                } else {
                    n = "" + card.getCardType().getNameAsInt();
                }
                final String url = "/game/resources/"+cardLocale.getResource()+"/" + n + "_" + card.getCardSuit().getResource() + ".png";

                final int index = x * 4 + i;
                cardsImageView[index] = new ImageView();
                cardsImageView[index].setImage(new Image(url));

                //i want to get rotation from bottom left margin so let's do a trick:
                cardsImageView[index].setScaleY(-1);
                cardsImageView[index].setRotate(180);


                pane.getChildren().add(cardsImageView[index]);//Append to stage
                cardsImageView[index].setX(-100);
                cardsImageView[index].setY(300);

                cardsImageView[index].addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    GuiHelper.highlightImageView(cardsImageView[index]);
                });
                cardsImageView[index].addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                    GuiHelper.removeEffects(cardsImageView[index]);
                });
                cardsImageView[index].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (isTimelinePlaying) return;
                    selectedLocale = index/4;
                    displayCards();
                });
            }
        }
        /////BUG?! root width/heigh are not ready here... we have to wait some time, is it JavaFX bug?
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> displayCards()));
        timeline.play();
    }

    /**
     * Displays cards in groups divived by Locale
     */
    private void displayCards(){
        if (isTimelinePlaying) return;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        for (int i=0; i<cardsImageView.length; i++){
            ImageView imageView = cardsImageView[i];
            int localeNumber = (i)/4;
            int cardNumber = i%4;

            if (localeNumber!=selectedLocale) {
                GuiHelper.moveImageView(timeline, imageView, 20 + root.getWidth() * (double) (localeNumber) / 5d + i * 35, root.getHeight() / 2 + 60 + cardNumber*5, cardNumber * 7, 1d, 200);
            }else{
                GuiHelper.moveImageView(timeline, imageView, 20 + root.getWidth()/2d + cardNumber * 60 - 150, root.getHeight() / 2 - 130, 0, 1.5d, 200);
                imageView.toFront();
            }
        }


        isTimelinePlaying = true;
        timeline.play();
        timeline.setOnFinished((event)-> isTimelinePlaying = false);
    }
}


enum CardLocale {
    PIACENTINE("piac"),
    BERGAMASCHE("bergamo"),
    NAPOLETANE("napoli");

    private String resource;

    CardLocale(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return Character.toUpperCase(super.toString().charAt(0)) + super.toString().substring(1);
    }

    public String getResource() {
        return resource;
    }
}