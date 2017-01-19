package gameClient.utils;

import com.jfoenix.controls.JFXSnackbar;
import game.action.Action;
import game.action.DiscardCards;
import game.model.Card;
import game.model.CardSuit;
import game.model.CardType;
import game.model.User;
import gameClient.ClientObjs;
import gameClient.controller.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import utils.MyLogger;

import java.util.*;

/**
 * Created by andrea on 17/01/17.
 */
public class GuiHelper {

    private static volatile boolean isDiscarding = false;
    private static volatile boolean isAnimatingCards = false;

    /**
     * Hides passed card and sets it to covered
     * @param guiCard
     */
    public static void hideCard(GuiCard guiCard){
        guiCard.getImage().setX(-200);
        guiCard.getImage().setY(-200);
    }

    /**
     * Highlights passed card
     * @param guiCard
     */
    public static void highlightCard(GuiCard guiCard){
        highlightImageView(guiCard.getImage());
    }


    /**
     * Highlights passed imageView
     * @param imageView
     */
    public static void highlightImageView(ImageView imageView){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.1);
        colorAdjust.setHue(-0.05);
        colorAdjust.setBrightness(0.2);
        colorAdjust.setSaturation(0.3);
        //apply new effect to card
        imageView.setEffect(colorAdjust);
    }

    /**
     * Removes effects from cards (ex: removes highlight effect)
     * @param guiCard
     */
    public static void removeEffects(GuiCard guiCard){
        removeEffects(guiCard.getImage());
    }

    public static void removeEffects(ImageView imageView){
        imageView.setEffect(null);
    }


    /**
     * Display user's selected cards on gui
     */
    public static void displaySelectedCards() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displaySelectedCards());
            return;
        }

        //Retrieve objects:
        ObservableList<Card>selectedCards = GameController.getGameController().getSelectedCards();
        StackPane root = GameController.getGameController().getRoot();
        HashMap<Card, GuiCard> guiCards = GameController.getGameController().getGuiCards();

        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        //Show every selected image above UserHand
        for (int i = 0; i < selectedCards.size(); i++) {
            moveImageView(timeline, guiCards.get(selectedCards.get(i)).getImage(), (root.getWidth() - selectedCards.size() * 45) / 2 + i * 45, root.getHeight() * 2 / 9, 0, 1000);
            guiCards.get(selectedCards.get(i)).setCoveredCard(false);
        }

        //Corrects Z Position
        for (int b = 0; b < selectedCards.size(); b++) {//foreach deck
            Card card = selectedCards.get(b);
            guiCards.get(card).getImage().toFront();
        }

        timeline.play();
    }

    /**
     * Asks server to discard cardType becase we have 4 of it
     * @param cardsToThrow
     */
    public static void askServerDiscard(ArrayList<Card> cardsToThrow) {
        try{
            Action action = new DiscardCards(cardsToThrow);
            ClientObjs.getSocketWriter().sendAction(action);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Discard user's cards
     * @param cards
     */
    public static void discardCards(User user, ArrayList<Card> cards){
        if (isDiscarding) return;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> discardCards(user, cards));
            return;
        }

        if (isAnimatingCards){//if cards are moving try later
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> discardCards(user, cards)));
            timeline.play();
            return;
        }

        HashMap<Card, GuiCard> guiCards = GameController.getGameController().getGuiCards();
        StackPane root = GameController.getGameController().getRoot();

        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        cards.forEach(card -> {

            GuiCard guiCard = guiCards.get(card);
            guiCard.setCoveredCard(true);

            if (!user.equals(ClientObjs.getUser())) {
                guiCard.getImage().setX(getUserXPosition(user));
                guiCard.getImage().setY(getUserYPosition(user));
            }
            moveImageView(timeline, guiCard.getImage(), 20, root.getWidth()/2-130, 0, 0.8d, 1000);
        });
        isDiscarding = true;
        timeline.play();
        timeline.setOnFinished((e)->{ isDiscarding = false; });
    }

    /**
     * Magic method which displays user cards (which are not selected or covered) in ellipse shape
     */
    public static void displayUserHand(List<Card> userCards) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displayUserHand(userCards));
            return;
        }

        //Retrieve objects:
        ObservableList<Card>selectedCards = GameController.getGameController().getSelectedCards();
        StackPane root = GameController.getGameController().getRoot();
        HashMap<Card, GuiCard> guiCards = GameController.getGameController().getGuiCards();
        List<Card> deck = new ArrayList<>(userCards);//We take a copy of it

        //We have to remove from deck all selected cards, they'll be shown in selection box
        deck.removeAll(selectedCards);

        //We first need to hide cards which are not in our deck
        guiCards.forEach((card, guiCard) -> {
            if (!deck.contains(card)) {
                if (!guiCard.isCoveredCard()) {//If it's covered on table it can stay there
                    hideCard(guiCard);
                }
            }
        });

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


            if (!ClientObjs.getUser().equals(ClientObjs.getMatch().getWhoseTurn())) {
                depth = depth - 0.20;//If it's not my turn we can collpase cards a bit more
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
            guiCards.get(card).setCoveredCard(false);
            moveImageView(timeline, guiCards.get(card).getImage(), x + root.getWidth() / 2.2, root.getHeight() - y, angle, 1000);
        }

        //Corrects Z positions
        for (int b = 0; b < deck.size(); b++) {//foreach deck
            Card card = deck.get(b);
            guiCards.get(card).getImage().toBack();
        }
        isAnimatingCards = true;
        timeline.play();
        timeline.setOnFinished(e -> isAnimatingCards=false);
    }

    /**
     * Displays user's avatars and info on gui
     */
    public static void displayEnemies(){
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> displayEnemies());
            return;
        }


        //Retrieve objects:
        List<User> enemies = ClientObjs.getMatch().getEnemies();

        //Creates new timeline Object
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        //Show every user on top of screen

        for (int i = 0; i < enemies.size(); i++) {
            VBox userAvatarVBox = enemies.get(i).getUserAvatarVBox();//retrieve user's avatar
            if (GameController.getGameController().getPane().getChildren().contains(userAvatarVBox)) {//if displayed on gui update pos.
                userAvatarVBox.setLayoutX(getUserXPosition(enemies.get(i)));
                userAvatarVBox.setLayoutY(getUserYPosition(enemies.get(i)));
            }
        }
        timeline.play();
    }

    /**
     * Plays 'throw cards' animation.
     * @param from cards thrower
     * @param howMany how many cards to throw
     */
    public static void throwCards(User from, int howMany){
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> throwCards(from, howMany));
            return;
        }
        List<GuiCard> cards = new ArrayList<>();

        //Generates a list of throwable cards
        for (int i=0; i<howMany; i++) {
            GuiCard guiCard = findFreeGuiCard(cards);//Finds a free card not owned by user
            cards.add(guiCard);
            if (guiCard == null) {
                MyLogger.println("Covered cards are over");
                return;
            }
        }
        throwCards(from, cards);

    }

    /**
     * Plays 'throw cards' animation
     * @param from cards thrower
     * @param cards cards to throw
     */
    public static void throwCards(User from, List<GuiCard> cards){
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> throwCards(from, cards));
            return;
        }

        StackPane root = GameController.getGameController().getRoot();
        Timeline throwCardsTimeline = new Timeline();
        throwCardsTimeline.setCycleCount(1);
        throwCardsTimeline.setAutoReverse(false);
        Random random = new Random();

        for (GuiCard guiCard: cards) {
            guiCard.setCoveredCard(true);//sets this cards to covered
            guiCard.getImage().setX(getUserXPosition(from));//card starts his movement where thrower is
            guiCard.getImage().setY(getUserYPosition(from));
            guiCard.getImage().toFront();//it should be on top of all other cards
            moveImageView(throwCardsTimeline, guiCard.getImage(), root.getWidth()/2, root.getHeight() / 9, random.nextInt(200), 500);
        }
        throwCardsTimeline.play();
    }

    /**
     * Moves table cards to selected users and then hides them
     * @param user
     * @param cards
     */
    public static void pickCards(User user, List<GuiCard> cards){
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> pickCards(user, cards));
            return;
        }

        StackPane root = GameController.getGameController().getRoot();
        Timeline pickCardsTimeline = new Timeline();
        pickCardsTimeline.setCycleCount(1);
        pickCardsTimeline.setAutoReverse(false);

        for (GuiCard guiCard: cards) {
            moveImageView(pickCardsTimeline, guiCard.getImage(), getUserXPosition(user), getUserYPosition(user), 0, 500);
        }

        pickCardsTimeline.play();
        pickCardsTimeline.setOnFinished((event -> {
            if (!user.equals(ClientObjs.getUser())){
                cards.forEach((card) -> hideCard(card));
            }
        }));

    }

    /**
     * Shows message to user
     * @param message
     */
    public static void showToast(String message){
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(() -> showToast(message));
            return;
        }
        StackPane root = GameController.getGameController().getRoot();
        JFXSnackbar jfxSnackbar = new JFXSnackbar(root);
        jfxSnackbar.show(message, 6000);
    }

    /**
     * Returns user X position on screen
     * @param user
     * @return
     */
    public static double getUserXPosition(User user){
        StackPane root = GameController.getGameController().getRoot();
        if (!ClientObjs.getUser().equals(user)) {
            List<User> enemies = ClientObjs.getMatch().getEnemies();
            return (root.getWidth() - enemies.size() * root.getWidth()/ (enemies.size()+1)) / 2 + enemies.indexOf(user) * root.getWidth()/ enemies.size();
        }else{
            return root.getWidth()/2;
        }
    }

    /**
     * Returns user Y position on screen
     * @param user
     * @return
     */
    public static double getUserYPosition(User user){
        StackPane root = GameController.getGameController().getRoot();
        if (!ClientObjs.getUser().equals(user)) {
            return 20;
        }else{
            return root.getHeight()/2;
        }
    }

    /**
     * Moves ImageView using a Timeline animation
     * @param timeline
     * @param obj
     * @param endX
     * @param endY
     * @param endRotate
     * @param animationTime
     */
    public static void moveImageView(Timeline timeline, ImageView obj, double endX, double endY, double endRotate, double scale, int animationTime) {
        KeyValue keyValueRotate = new KeyValue(obj.rotateProperty(), endRotate);
        KeyValue keyValueX = new KeyValue(obj.xProperty(), endX);
        KeyValue keyValueY = new KeyValue(obj.yProperty(), endY);
        KeyValue keyValueScaleX = new KeyValue(obj.scaleXProperty(), scale);
        KeyValue keyValueScaleY = new KeyValue(obj.scaleYProperty(), scale);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(animationTime), keyValueRotate, keyValueX, keyValueY, keyValueScaleX, keyValueScaleY);
        timeline.getKeyFrames().add(keyFrame);
    }
    public static void moveImageView(Timeline timeline, ImageView obj, double endX, double endY, double endRotate, int animationTime) {
        moveImageView(timeline, obj, endX, endY, endRotate, 1d, animationTime);
    }

    /**
     * Converts a List of Card to a List of GuiCard
     * @param cardList
     * @return
     */
    public static List<GuiCard> cardListToGuiCardList(List<Card> cardList){
        HashMap<Card, GuiCard> guiCards = GameController.getGameController().getGuiCards();
        List<GuiCard> guiCardList = new ArrayList<>();
        cardList.forEach((card -> {
            guiCardList.add(guiCards.get(card));
        }));
        return guiCardList;
    }

    /**
     * Finds a card which not belongs to user and is not shown on table not in notIn List
     * @param notIn
     * @return
     */
    private static GuiCard findFreeGuiCard(List<GuiCard> notIn){
        final List<GuiCard> notInFinal;
        if (notIn==null){
            notInFinal = new ArrayList<>(0);
        }else {
            notInFinal = new ArrayList<>(notIn);
        }

        //Retrieve objects:
        HashMap<Card, GuiCard> guiCards = GameController.getGameController().getGuiCards();
        Optional<GuiCard> freeGuiCard = guiCards.values().stream()
                .filter(guiCard -> (!GuiHelper.isOnTable(guiCard) && !ClientObjs.getUser().getCards().contains(guiCard.getCardObject()) && !notInFinal.contains(guiCard))).findFirst();
        if(freeGuiCard.isPresent()){
            return freeGuiCard.get();
        }
        return null;
    }

    /**
     * Returns true when passed card is showed on table
     * @param card
     * @return
     */
    public static boolean isOnTable(GuiCard card){
        return isOnTable(card.getImage());
    }

    /**
     * Returns true when passed card is showed on table
     * @param card
     * @return
     */
    public static boolean isOnTable(ImageView card){
        if (card.getX()<0 && card.getY()<0){
            return false;
        }
        return true;
    }

    private static void moveImageView(ImageView obj, double endX, double endY, double endRotate){
        obj.setX(endX);
        obj.setY(endY);
        obj.setRotate(endRotate);
    }
}
