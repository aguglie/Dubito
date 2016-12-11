package gameClient.controller;

import com.sun.deploy.util.ArrayUtil;
import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;
import gameClient.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andrea on 10/12/16.
 */
public class GameController implements Initializable {
    @FXML
    private StackPane root;

    @FXML
    private Pane pane;

    private HashMap<Card, GuiCard> guiCards = new HashMap<>(40);//Map between Cards and their gui representation

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int b = CardSuit.values().length - 1; b >= 0; b--) {//Reversed foreach CardSuit
            CardSuit cardSuit = CardSuit.values()[b];
            for (CardType cardType : CardType.values()) {//Foreach CardType

                //Connects every card with a GuiCard Object
                Card card = new Card(cardType, cardSuit);
                GuiCard guiCard = new GuiCard(card);
                guiCards.put(card, guiCard);//Register them in hashmap

                //appends every card to stage
                pane.getChildren().add(guiCard.getImage());
                guiCard.getImage().setX(50);
                guiCard.getImage().setY(50);
            }
        }

        ArrayList test = new ArrayList(40);
        for (CardType type : CardType.values()) {
            for (CardSuit suit : CardSuit.values()) {
                test.add(new Card(type, suit));
            }
        }
        Collections.shuffle(test);////Debug debug debug
        updateDeck(test);//// Debug debug debug
    }

    public void updateDeck(List<Card> deck) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> updateDeck(deck));
        }

        //Debug debug debug starts here
         for (int i=0; i<20; i++){
             deck.remove(i);
         }
         //Debug debug debug ends here


        //Orders cards first by type then suit
        Collections.sort(deck, (o1, o2) -> {
            int hasho1 = o1.getCardType().getAsArrayKey() * 4 + o1.getCardSuit().getValue();
            int hasho2 = o2.getCardType().getAsArrayKey() * 4 + o2.getCardSuit().getValue();
            return hasho1 - hasho2;
        });


        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);


        //Counts how many types are in user's deck.
        int cardinOfTypes = new Object() {
            int count() {
                List<CardType> cardTypes = new ArrayList<CardType>();
                deck.forEach(c -> {
                    if (!cardTypes.contains(c.getCardType())) cardTypes.add(c.getCardType());
                });
                return cardTypes.size();
            }
        }.count();


        //Here layout magic starts...
        List<CardType> placedTypes = new ArrayList(4);
        double depth = 0.5;
        for (Card card :
                deck) {

            if (placedTypes.contains(card.getCardType())) {
                //ITALIAN: Se il tipo è già stato piazzato significa che questa carta va messa dietro ad una già esistente
                depth = depth + 0.35;
            } else {
                depth = 0.5;
                placedTypes.add(card.getCardType());
            }

            //Calculates x,y positions and rotat.
            int x = (int) (-35d * cardinOfTypes + (placedTypes.size() - 1) * cardinOfTypes * 70d / (cardinOfTypes - 1d));
            int y = (int) (Math.sqrt(1d - Math.pow(x, 2) / (250000d)) * (300d * Math.sqrt(depth)));
            double angle = 30d * (x / 350d);


            //!?!?!?! corrections
            if (depth > 0.5 && x < 0)
                x = (int) (x - depth * 15);
            if (depth > 0.5 && x > 0)
                x = (int) (x + depth * 15);

            //moves image
            moveImageView(timeline, guiCards.get(card).getImage(), x + 400, 500 - y, angle);
        }

        timeline.play();
    }

    private void moveImageView(Timeline timeline, ImageView obj, double endX, double endY, double endRotate) {
        KeyValue keyValueRotate = new KeyValue(obj.rotateProperty(), endRotate);
        KeyValue keyValueX = new KeyValue(obj.xProperty(), endX);
        KeyValue keyValueY = new KeyValue(obj.yProperty(), endY);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), keyValueRotate, keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);
    }

    /**
     * Object locally used to keep card image matched with its object
     */
    private class GuiCard {
        private ImageView image;
        private Card cardObject;

        public GuiCard(Card cardObject) {
            this.cardObject = cardObject;
            setImage();
        }

        public GuiCard(CardType cardType, CardSuit cardSuit) {
            this.cardObject = new Card(cardType, cardSuit);
            setImage();
        }

        /**
         * Loads image
         */
        private void setImage() {
            Image image = new Image(cardObject.getResourceURL());
            this.image = new ImageView(image);
        }

        public ImageView getImage() {
            return image;
        }

        public Card getCardObject() {
            return cardObject;
        }
    }
}
