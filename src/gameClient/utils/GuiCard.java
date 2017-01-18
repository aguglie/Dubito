package gameClient.utils;

/**
 * Created by andrea on 17/01/17.
 */

import game.model.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Object used to keep card image matched with its object on GUI
 */
public class GuiCard {
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