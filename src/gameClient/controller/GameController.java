package gameClient.controller;

import com.jfoenix.controls.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import game.action.Action;
import game.action.UserPlay;
import game.model.*;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import gameClient.utils.GuiCard;
import gameClient.utils.GuiHelper;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.net.URL;
import java.util.*;
import java.util.List;

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
    private JFXButton discardButton;

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

        //Appends card to gui
        for (int b = CardSuit.values().length - 1; b >= 0; b--) {//Reversed foreach CardSuit
            CardSuit cardSuit = CardSuit.values()[b];
            for (CardType cardType : CardType.values()) {//Foreach CardType

                Card card = new Card(cardType, cardSuit);//Creates new logical card object
                GuiCard guiCard = new GuiCard(card);//Creates new 'physical' card object
                pane.getChildren().add(guiCard.getImage());//Adds physical card to stager
                guiCards.put(card, guiCard);//Bind logical and physical card

                //appends every card to stage
                GuiHelper.hideCard(guiCard);

                //highlights on hover
                guiCard.getImage().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                   GuiHelper.highlightCard(guiCards.get(card));
                });

                //Cheats ;)
                guiCard.getImage().addEventHandler(MouseEvent.ANY, event -> {
                    if (guiCard.isCoveredCard() && event.isSecondaryButtonDown()){
                        guiCard.setCoveredCard(false);
                        Timeline timeline = new Timeline(new KeyFrame(
                                Duration.millis(200),
                                ae -> guiCard.setCoveredCard(true)));
                        timeline.play();
                    }
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

        //Append enemies to gui
        List<User> enemies = ClientObjs.getMatch().getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            VBox userAvatarVBox = enemies.get(i).getUserAvatarVBox();//retrieve user's avatar
            if (!GameController.getGameController().getPane().getChildren().contains(userAvatarVBox)){//if not displayed on gui append it now
                GameController.getGameController().getPane().getChildren().add(userAvatarVBox);
            }
        }


        playButton.setDisable(true);//it starts disabled
        dubitoButton.setOnAction((e) -> dubitoButtonPressed());
        playButton.setOnAction((e) -> showTypeBox());
        discardButton.setOnAction((e) -> GuiHelper.askServerDiscard(new ArrayList<Card>(selectedCards)));


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

        /////BUG?! root width/heigh are not ready here... we have to wait some time, is it JavaFX bug?
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> updatePositions()));
        timeline.play();
    }

    private void updatePositions() {
        buttonsHBox.setLayoutX((root.getWidth() - buttonsHBox.getWidth() - 30));
        buttonsHBox.setLayoutY(root.getHeight() * 2 / 5);

        GuiHelper.displaySelectedCards();
        displayUserHand(ClientObjs.getUser().getCards());
        GuiHelper.displayEnemies();
    }

    /**
     * Called by server when another user (NOT ME!) puts cards on table
     * @param user
     * @param cards
     * @param cardType
     */
    public void userPutsCards(User user, List<Card> cards, CardType cardType) {
        GuiHelper.showToast(user.getUsername() + " ha appena giocato " + cards.size() + " " + cardType.toStringPlurals(cards.size()));
        GuiHelper.throwCards(user, GuiHelper.cardListToGuiCardList(cards));
    }

    /**
     * Called by server when someone has to take cards
     * @param user
     * @param cardsPicked
     */
    public void userPicksCards(User user, List<Card> cardsPicked) {

        GuiHelper.pickCards(user, GuiHelper.cardListToGuiCardList(cardsPicked));//Who lost has to take cards

        if (user.equals(ClientObjs.getUser())) {//If playing user lost
            GuiHelper.showToast("Accidenti " + user.getUsername() + ", hai perso la mano e devi prendere "+cardsPicked.size()+" carte.");
        } else {
            GuiHelper.showToast(user.getUsername() + " perde la mano");
        }
    }

    /**
     * Called when UpdateMatch is received
     */
    public void onUpdateMatch() {
        buttonsHBox.setDisable(!isMyTurn());
    }

    /**
     * Called when UpdateUser is received
     * Clear user selected cards (called when my turns starts/ends)
     */
    public void clearSelectedCards() {
        selectedCards.clear();
    }


    /**
     * Disposes cards in ordered way on GUI
     *
     * @param userCards userCards
     */
    public void displayUserHand(List<Card> userCards) {
        GuiHelper.displayUserHand(userCards);
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
        //Throw Cards
        GuiHelper.throwCards(ClientObjs.getUser(), GuiHelper.cardListToGuiCardList(selectedCards));

        //Performs Action
        Action action = new UserPlay(UserPlay.PlayType.PLAYCARD, selectedCards, cardType);
        ClientObjs.getSocketWriter().sendAction(action);
    }

    /**
     * Called by server when someone wins
     * @param winner
     */
    public void gameOver(User winner){
        if (winner.equals(ClientObjs.getUser())){
            SceneDirector.showModal("Hai vinto!", "Complimenti, hai vinto!");
        }else {
            SceneDirector.showModal("Accidenti", "Accidenti "+ClientObjs.getUser().getUsername()+", "+winner.getUsername()+" ha terminato le carte prima di te!");
        }

        //Go back to home screen
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(10000),
                ae -> SceneDirector.getInstance().showSelectRoom()));
        timeline.play();
    }

    private boolean isMyTurn() {
        return ClientObjs.getUser().equals(ClientObjs.getMatch().getWhoseTurn());
    }

    public void discardCards(User user, List<Card> cards){
        GuiHelper.discardCards(user, new ArrayList<Card>(cards));
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