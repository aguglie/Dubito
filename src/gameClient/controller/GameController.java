package gameClient.controller;

import com.jfoenix.controls.*;
import game.action.Action;
import game.action.UserPlay;
import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;
import game.model.User;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import utils.MyLogger;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andrea on 10/12/16.
 */
public class GameController implements Initializable {
    private static GameController gameController;
    private ObservableList<Card> selectedCards = FXCollections.observableArrayList();


    @FXML
    private StackPane root;

    @FXML
    private Pane pane;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private JFXButton playButton;

    @FXML
    private JFXButton dubitoButton;


    private HashMap<Card, GuiCard> guiCards = new HashMap<>(40);//Map between Cards and their gui representation

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneDirector.getInstance().setRoot(root);
        gameController = this;


        //Graphic setup:

        //Sets page background
        final String background = "/game/resources/table.png";
        pane.setStyle("-fx-background-image: url('" + background + "');");


        //Spawn all cards put them outside view and setup actions
        for (int b = CardSuit.values().length - 1; b >= 0; b--) {//Reversed foreach CardSuit
            CardSuit cardSuit = CardSuit.values()[b];
            for (CardType cardType : CardType.values()) {//Foreach CardType

                //Connects every card with a GuiCard Object
                Card card = new Card(cardType, cardSuit);
                GuiCard guiCard = new GuiCard(card);
                guiCards.put(card, guiCard);//Register them in hashmap

                //appends every card to stage
                pane.getChildren().add(guiCard.getImage());
                guiCard.getImage().setX(-100);
                guiCard.getImage().setY(-100);

                //highlights on hover
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    ColorAdjust colorAdjust = new ColorAdjust();
                    colorAdjust.setContrast(0.1);
                    colorAdjust.setHue(-0.05);
                    colorAdjust.setBrightness(0.2);
                    colorAdjust.setSaturation(0.3);
                    //apply new effect to card
                    guiCards.get(card).getImage().setEffect(colorAdjust);
                });

                //set card as selected on click
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (guiCard.isCoveredCard()) return;//User cannot click on covered cards
                    if (selectedCards.contains(card)) {
                        selectedCards.remove(card);
                    } else {
                        if (isMyTurn()) {
                            if (selectedCards.size() >= 5) {
                                SceneDirector.showModal("Rallenta!", "Tieni un po' di carte anche per dopo!");
                            } else {
                                selectedCards.add(card);
                            }
                        }
                    }
                });

                //removes highlight if card is not selected
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                    if (!selectedCards.contains(card)) {
                        guiCards.get(card).getImage().setEffect(null);
                    }
                });
            }
        }

        buttonsHBox.setDisable(!isMyTurn());//Enables/Disables play and dubito buttons
        playButton.setDisable(true);//it starts disabled
        dubitoButton.setOnAction((e) -> dubitoButtonPressed());
        playButton.setOnAction((e) -> showTypeBox());
        updatePositions();
        //bind sizes to width property
        root.widthProperty().addListener(listener -> {
            updatePositions();
        });
        //bind sizes to height property
        root.heightProperty().addListener(listener -> {
            updatePositions();
        });


        //when a card is selected/deselected we need to draw everything
        selectedCards.addListener((ListChangeListener<Card>) change -> {
            playButton.setDisable((selectedCards.size() < 1));
            displaySelectedCards();//Updates selected cards
            displayUserHand(ClientObjs.getUser().getCards());//Updates bottom cards (aka UserHand)
        });
    }

    private void updatePositions() {
        buttonsHBox.setLayoutX((root.getWidth() - buttonsHBox.getWidth()));
        buttonsHBox.setLayoutY(root.getHeight() * 2 / 5);
        displaySelectedCards();
        displayUserHand(ClientObjs.getUser().getCards());
    }

    public void userPutsCards(User user, int howMany, CardType cardType) {
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(() -> userPutsCards(user, howMany, cardType));
            return;
        }

        JFXSnackbar jfxSnackbar = new JFXSnackbar(root);
        jfxSnackbar.show(user.getUsername() + " ha appena giocato " + howMany + " " + cardType.toStringPlurals(howMany), 6000);


        Timeline throwCardsTimeline = new Timeline();
        throwCardsTimeline.setCycleCount(1);
        throwCardsTimeline.setAutoReverse(false);
        Random random = new Random();


        final AtomicInteger atomicInteger = new AtomicInteger(1);
        guiCards.forEach(((card, guiCard) -> {
            if (!ClientObjs.getUser().getCards().contains(card)) {//If it's not in user deck
                if (guiCard.getImage().getX() < 0 || guiCard.getImage().getY() < 0) {//if it's not on stage
                    if (atomicInteger.get() <= howMany) {
                        guiCards.get(card).setCoveredCard(true);
                        guiCards.get(card).getImage().setX(-200);
                        guiCards.get(card).getImage().setY(-200);
                        moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() / 9, random.nextInt(200), 1000);
                        atomicInteger.incrementAndGet();
                    }
                }
            }
        }));

        throwCardsTimeline.play();
    }

    public void userPicksCards(User user, List<Card> cardsPicked) {
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(() -> userPicksCards(user, cardsPicked));
            return;
        }
        Timeline throwCardsTimeline = new Timeline();
        throwCardsTimeline.setCycleCount(1);
        throwCardsTimeline.setAutoReverse(false);

        JFXSnackbar jfxSnackbar = new JFXSnackbar(root);

        if (user.equals(ClientObjs.getUser())) {//If playing user just lost move cards to him
            jfxSnackbar.show("Accidenti " + user.getUsername() + ", hai perso la mano e devi prendere "+cardsPicked.size()+" carte.", 6000);

            //We have to update the random covered cards on screen with covered cards that actually user just picked.
            guiCards.forEach(((card, guiCard) -> {
                if (guiCard.getImage().getX() > 0 && guiCard.getImage().getY() > 0) {//if it's on stage
                    if (guiCard.isCoveredCard() && cardsPicked.size()>0) {//If it's covered
                        //if this card was put there randomly, we have to switch it with a user's one.
                        //Move correct card to displayed cards:
                        guiCards.get(cardsPicked.get(0)).setCoveredCard(false);
                        guiCards.get(cardsPicked.get(0)).getImage().setX(guiCard.getImage().getX());
                        guiCards.get(cardsPicked.get(0)).getImage().setY(guiCard.getImage().getY());
                        guiCards.get(cardsPicked.get(0)).getImage().setRotate(guiCard.getImage().getRotate());
                        guiCard.getImage().setY(-200);//Move wrong card away
                        cardsPicked.remove(0);//we corrected this card, remove it from list
                        moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), -200, root.getHeight() + 200, 0, 4000);
                    }
                }
            }));


        } else {
            jfxSnackbar.show(user.getUsername() + " perde la mano", 6000);

            //If user is not loosing, cards are brought to top of screen
            guiCards.forEach(((card, guiCard) -> {
                if (guiCard.getImage().getX() > 0 && guiCard.getImage().getY() > 0) {//if it's on stage
                    if (guiCard.isCoveredCard()) {
                        moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), root.getWidth()/2, -200, 0, 1000);
                    }
                }
            }));

        }

        throwCardsTimeline.play();
    }

    /**
     * Clear user selected cards (called when my turns starts/ends)
     */
    public void clearSelectedCards() {
        selectedCards.clear();
    }

    /**
     * Uncover passed cards
     *
     * @param cards cards to uncover
     */
    public void uncoverCards(ArrayList<Card> cards) {
        try {
            cards.forEach(card -> guiCards.get(card).setCoveredCard(false));
        } catch (Exception e) {

        }
    }

    /**
     * Disposes cards in ordered way on GUI
     *
     * @param userCards userCards
     */
    public void displayUserHand(List<Card> userCards) {
        displayUserHand(userCards, 500);
    }


    /**
     * Disposes cards in ordered way on GUI
     *
     * @param userCards     userCards
     * @param animationTime animation duration
     */
    public void displayUserHand(List<Card> userCards, int animationTime) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displayUserHand(userCards));
            return;
        }

        List<Card> deck = new ArrayList<>(userCards);//We take a copy of it

        //We have to put away cards which are in other users' hands
        guiCards.forEach((card, guiCard) -> {
            if (!deck.contains(card)) {
                if (!guiCard.isCoveredCard()) {//If it's covered on table it can stay there
                    guiCard.getImage().setX(-200);
                    guiCard.getImage().setY(-200);
                }
            }
        });

        //We have to remove from hand deck all selected cards, they'll be shown in selection box
        deck.removeAll(selectedCards);

        //Orders cards first by type then by suit
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
        List<CardType> placedTypes = new ArrayList(4);//Array containing already drew types
        double depth = 0.5;//level 0 card depth
        for (Card card :
                deck) {

            if (placedTypes.contains(card.getCardType())) {
                //ITALIAN: Se il tipo è già stato piazzato significa che questa carta va messa dietro ad una già esistente
                depth = depth + 0.35;
            } else {
                depth = 0.5;
                placedTypes.add(card.getCardType());
            }


            //Calculates x posit
            int x = (int) (-35d * cardinOfTypes + (placedTypes.size() - 1) * cardinOfTypes * 70d / (cardinOfTypes - 1d));

            //Correction of x pos
            if (x < 0) {
                x = (int) (x - depth * 20);
            }

            //calculates y and rotation
            int y = (int) (Math.sqrt(1d - Math.pow(x, 2) / (250000d)) * (300d * Math.sqrt(depth)));
            double angle = 25d * (x / 350d);

            //moves image
            moveImageView(timeline, guiCards.get(card).getImage(), x + root.getWidth() / 2.2, root.getHeight() - y, angle, animationTime);
        }

        //Corrects Z positions
        for (int b = 0; b < deck.size(); b++) {//foreach deck
            Card card = deck.get(b);
            guiCards.get(card).getImage().toBack();
        }

        timeline.play();
    }

    /**
     * Sends dubito Action
     */
    public void dubitoButtonPressed() {
        Action action = new UserPlay(UserPlay.PlayType.DUBITO);
        ClientObjs.getSocketWriter().sendAction(action);
    }

    /**
     * Displays selected cards on GUI
     */
    public void displaySelectedCards() {
        displaySelectedCards(500);
    }

    /**
     * Displays selected cards on GUI
     *
     * @param animationTime animation duration
     */
    public void displaySelectedCards(int animationTime) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displaySelectedCards());
            return;
        }
        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        //Show every selected image above UserHand
        for (int i = 0; i < selectedCards.size(); i++) {
            moveImageView(timeline, guiCards.get(selectedCards.get(i)).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2 + i * 45, root.getHeight() * 2 / 9, 0, animationTime);
        }

        //Corrects Z Position
        for (int b = 0; b < selectedCards.size(); b++) {//foreach deck
            Card card = selectedCards.get(b);
            guiCards.get(card).getImage().toFront();
        }

        timeline.play();
    }

    /**
     * Show box in which user chooses CardType for Play Action
     */
    public void showTypeBox() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showTypeBox());
            return;
        }

        if (selectedCards.size() == 0) {
            SceneDirector.showModal("Accidenti", "Devi almeno selezionare una carta da giocare!");
            return;
        }

        if (!ClientObjs.getMatch().isFirstTurn()){
        //If this is not the first turn, we don't have to show TypeBox, CardType was already choosen
            playActionAndAnimation(new ArrayList<Card>(selectedCards), null);
            return;
        }

        //Creates list of available CardTypes
        ListView<CardType> listView = new JFXListView<>();
        for (CardType cardType :
                CardType.values()) {
            listView.getItems().add(cardType);
        }
        VBox body = new VBox();//Vbox to hold Label and ListView
        body.getChildren().add(new Label("Come vuoi dichiarare le carte?"));
        body.getChildren().add(listView);

        JFXButton button = new JFXButton("Gioca");//Play Button
        button.setStyle("-fx-background-color: limegreen");//Button color
        button.setDisable(true);

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Dichiara le carte"));//Dialog Title
        content.setBody(body);
        content.setActions(button);
        JFXDialog dialog = new JFXDialog(this.root, content, JFXDialog.DialogTransition.CENTER);
        dialog.show();

        listView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            button.setDisable(false);
        }));
        button.setOnAction(action -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                dialog.close();
                playActionAndAnimation(new ArrayList<Card>(selectedCards), listView.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void playActionAndAnimation(ArrayList<Card> selectedCards, CardType cardType) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> playActionAndAnimation(selectedCards, cardType));
            return;
        }
        //Animation:

        //Creates new timeline Object

        //move closer timeline
        Timeline moveCloseTimeline = new Timeline();
        moveCloseTimeline.setCycleCount(1);
        moveCloseTimeline.setAutoReverse(false);

        selectedCards.forEach((card) -> {
            guiCards.get(card).setCoveredCard(true);//Set cards to covered
            moveImageView(moveCloseTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() * 2 / 9, 0, 1000);
        });


        //throw timeline
        Timeline throwCardsTimeline = new Timeline();
        throwCardsTimeline.setCycleCount(1);
        throwCardsTimeline.setAutoReverse(false);

        Random random = new Random();
        selectedCards.forEach((card) -> {
            guiCards.get(card).setCoveredCard(true);//Set cards to covered
            moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() / 9, random.nextInt(200), 1000);
        });

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(moveCloseTimeline, throwCardsTimeline);
        sequentialTransition.setCycleCount(1);
        sequentialTransition.setAutoReverse(false);
        sequentialTransition.play();

        //Performs Action
        Action action = new UserPlay(UserPlay.PlayType.PLAYCARD, selectedCards, cardType);
        ClientObjs.getSocketWriter().sendAction(action);
    }

    private boolean isMyTurn() {
        return ClientObjs.getUser().equals(ClientObjs.getMatch().getWhoseTurn());
    }

    public void onUpdateMatch() {
        buttonsHBox.setDisable(!isMyTurn());
    }

    private void moveImageView(Timeline timeline, ImageView obj, double endX, double endY, double endRotate, int animationTime) {
        KeyValue keyValueRotate = new KeyValue(obj.rotateProperty(), endRotate);
        KeyValue keyValueX = new KeyValue(obj.xProperty(), endX);
        KeyValue keyValueY = new KeyValue(obj.yProperty(), endY);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(animationTime), keyValueRotate, keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);
    }

    /**
     * Object locally used to keep card image matched with its object
     */
    private class GuiCard {
        private ImageView imageView;//Real card imageView
        private Image coveredCardImage;//Covered card image
        private Image realCardImage;//Real card image
        private Card cardObject;//Related cardObject

        public GuiCard(Card cardObject) {
            this.cardObject = cardObject;
            realCardImage = new Image(cardObject.getResourceURL());
            imageView = new ImageView(realCardImage);
        }

        /**
         * Loads covered card image
         */
        private void setCoveredCardImage() {
            coveredCardImage = new Image("/game/resources/coveredCard.jpg");
        }

        public ImageView getImage() {
            return imageView;
        }

        public boolean isCoveredCard() {
            return imageView.getImage() == coveredCardImage;
        }

        public void setCoveredCard(boolean coveredCard) {
            if (coveredCard) {
                if (coveredCardImage == null) {
                    setCoveredCardImage();
                }
                imageView.setEffect(null);//remove effects
                imageView.setImage(coveredCardImage);
            } else {
                imageView.setImage(realCardImage);
            }
        }
    }

    public static GameController getGameController() {
        return gameController;
    }
}