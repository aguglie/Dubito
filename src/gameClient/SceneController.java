package gameClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by andrea on 08/12/16.
 */
public class SceneController {
    private static Stage primaryStage;
    private static StackPane root;
    private static SceneController sceneController;

    private SceneController() {
    }

    public static SceneController getInstance() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    /**
     * Sets current root stackpane
     *
     * @param root
     */
    public void setRoot(StackPane root) {
        this.root = root;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Shows login scene
     */
    public void showLogin() {
        changeScene("Login", "view/login.fxml", 500, 500);
    }

    /**
     * Shows select room page
     */
    public void showSelectRoom() {
        changeScene("Seleziona Stanza", "view/selectRoom.fxml", 500, 500);
    }


    /**
     * Changes scene
     *
     * @param title Title of windows
     * @param fxml  URI to FXML file
     * @param w     New page width
     * @param h     New page height
     */
    private void changeScene(String title, String fxml, int w, int h) {
        try {
            primaryStage.setTitle(title);
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            primaryStage.setScene(new Scene(root, w, h));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a modal on scene
     * @param title modal title
     * @param text modal content
     */
    public static void showModal(String title, String text) {

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showModal(title, text));
            return;
        }

        JFXButton button = new JFXButton("Bene per me");
        button.setStyle("-fx-background-color: limegreen");
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(title));
        content.setBody(new Label(text));
        content.setActions(button);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        dialog.show();
        button.setOnAction(action -> dialog.close());
    }
}
