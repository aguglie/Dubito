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
public class SceneDirector {
    private static Stage primaryStage;
    private static StackPane root;
    private static SceneDirector sceneDirector;

    private SceneDirector() {
    }

    public static SceneDirector getInstance() {
        if (sceneDirector == null) {
            sceneDirector = new SceneDirector();
        }
        return sceneDirector;
    }

    /**
     * Sets current root stackpane
     *
     * @param root
     */
    public void setRoot(StackPane root) {
        SceneDirector.root = root;
    }

    public void setPrimaryStage(Stage primaryStage) {
        SceneDirector.primaryStage = primaryStage;
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
     * Shows select avatar page
     */
    public void showChooseAvatar() {
        changeScene("Scegli un Avatar", "view/chooseAvatar.fxml", 900, 500);
        primaryStage.setResizable(false);
    }


    /**
     * Shows game page
     */
    public void showGame() {
        changeScene("Dubito", "view/game.fxml", 900, 760);
        primaryStage.setResizable(true);
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
        if (!Platform.isFxApplicationThread()){
            Platform.runLater(()-> changeScene(title, fxml, w, h));
            return;
        }
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
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER, false);
        dialog.show();
        button.setOnAction(action -> dialog.close());
    }
}
