package gameClient.controller;

import com.jfoenix.controls.*;
import game.action.Action;
import game.action.UserPlay;
import game.model.*;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import gameClient.utils.GuiCard;
import gameClient.utils.GuiHelper;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import utils.MyLogger;


import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andrea on 10/12/16.
 */

public class GameController implements Initializable {
    private static GameController gameController;//UpdateUser UpdateMatch and Croupier use this variable to reach GameController
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
        this.gameController = this;

        pane.setStyle("-fx-background-image: url('/game/resources/table.png');");//Sets page background

        //loads fake data
        List<User> enemies = new ArrayList<>();
        enemies.add(new User("Luciano"));
        enemies.add(new User("Computer"));


        ClientObjs.getMatch().setEnemies(enemies);

        //Appends card to gui
        for (int b = CardSuit.values().length - 1; b >= 0; b--) {//Reversed foreach CardSuit
            CardSuit cardSuit = CardSuit.values()[b];
            for (CardType cardType : CardType.values()) {//Foreach CardType

                Card card = new Card(cardType, cardSuit);//Creates new logical card object
                GuiCard guiCard = new GuiCard(card);//Creates new 'physical' card object
                pane.getChildren().add(guiCard.getImage());//Adds physical card to stager
                guiCards.put(card, guiCard);//Bind logical and physical card

                //loads fake data
                Random rn = new Random();
                int answer = rn.nextInt(3);
                if (answer == 2) {
                    ClientObjs.getUser().getCards().add(card);
                }

                //appends every card to stage
                GuiHelper.hideCard(guiCard);

                //highlights on hover
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                   GuiHelper.highlightCard(guiCards.get(card));
                });

                //selects/deselects a card when clicked
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if (guiCard.isCoveredCard()) return;//User cannot click on covered cards
                    if (selectedCards.contains(card)) {
                        selectedCards.remove(card);//if already selected, deselect it.
                    } else {
                        if (isMyTurn()) {
                            if (selectedCards.size() >= 4) {
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
                        GuiHelper.removeEffects(guiCards.get(card));
                    }
                });
            }
        }

        buttonsHBox.setDisable(!isMyTurn());//Enables/Disables play and dubito buttons
        playButton.setDisable(true);//it starts disabled
        dubitoButton.setOnAction((e) -> dubitoButtonPressed());
        playButton.setOnAction((e) -> showTypeBox());


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
            GuiHelper.displaySelectedCards();//moves card from user deck to selected list
            displayUserHand(ClientObjs.getUser().getCards());//Updates user deck
        });
    }

    private void updatePositions() {
        buttonsHBox.setLayoutX((root.getWidth() - buttonsHBox.getWidth()));
        buttonsHBox.setLayoutY(root.getHeight() * 2 / 5);
        GuiHelper.displaySelectedCards();
        displayUserHand(ClientObjs.getUser().getCards());
        GuiHelper.displayEnemies();
    }

    public void userPutsCards(User user, int howMany, CardType cardType) {
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(() -> userPutsCards(user, howMany, cardType));
            return;
        }
        GuiHelper.showToast(user.getUsername() + " ha appena giocato " + howMany + " " + cardType.toStringPlurals(howMany));


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
                        //////// ---------------------------------------------------------------------------------------------------->>><<<<<
                        //moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() / 9, random.nextInt(200), 1000);
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
                        //////// ---------------------------------------------------------------------------------------------------->>><<<<<
                        //moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), -200, root.getHeight() + 200, 0, 4000);
                    }
                }
            }));


        } else {
            jfxSnackbar.show(user.getUsername() + " perde la mano", 6000);

            //If user is not loosing, cards are brought to top of screen
            guiCards.forEach(((card, guiCard) -> {
                if (guiCard.getImage().getX() > 0 && guiCard.getImage().getY() > 0) {//if it's on stage
                    if (guiCard.isCoveredCard()) {
                        //////// ---------------------------------------------------------------------------------------------------->>><<<<<
                        //moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), root.getWidth()/2, -200, 0, 1000);
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
        GuiHelper.displayUserHand();
    }


    /**
     * Disposes cards in ordered way on GUI
     *
     * @param userCards     userCards
     * @param animationTime animation duration
     */

    /**
     * Sends dubito Action
     */
    public void dubitoButtonPressed() {
        Action action = new UserPlay(UserPlay.PlayType.DUBITO);
        ClientObjs.getSocketWriter().sendAction(action);
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
            //////// ---------------------------------------------------------------------------------------------------->>><<<<<
            //moveImageView(moveCloseTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() * 2 / 9, 0, 1000);
        });


        //throw timeline
        Timeline throwCardsTimeline = new Timeline();
        throwCardsTimeline.setCycleCount(1);
        throwCardsTimeline.setAutoReverse(false);

        Random random = new Random();
        selectedCards.forEach((card) -> {
            guiCards.get(card).setCoveredCard(true);//Set cards to covered
            //////// ---------------------------------------------------------------------------------------------------->>><<<<<
            //moveImageView(throwCardsTimeline, guiCards.get(card).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2, root.getHeight() / 9, random.nextInt(200), 1000);
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
        return true;
        //////// ---------------------------------------------------------------------------------------------------->>><<<<<
        //return ClientObjs.getUser().equals(ClientObjs.getMatch().getWhoseTurn());
    }

    public void onUpdateMatch() {
        buttonsHBox.setDisable(!isMyTurn());
    }


    public static GameController getGameController() {
        return gameController;
    }

    public StackPane getRoot() {
        return root;
    }

    public ObservableList<Card> getSelectedCards() {
        return selectedCards;
    }

    public HashMap<Card, GuiCard> getGuiCards() {
        return guiCards;
    }

    public Pane getPane() {
        return pane;
    }
}