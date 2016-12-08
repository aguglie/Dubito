package gameClient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by andrea on 08/12/16.
 */
public class SceneController {
    Stage primaryStage;

    public SceneController() {
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Shows login scene
     */
    public void showLogin(){
        changeScene("Login", "login.fxml",300,275);
    }

    /**
     * Shows select room page
     */
    public void showSelectRoom(){
        changeScene("Seleziona Stanza", "selectRoom.fxml",300,275);
    }


    /**
     * Changes scene
     * @param title Title of windows
     * @param fxml URI to FXML file
     * @param w New page width
     * @param h New page height
     */
    private void changeScene(String title, String fxml, int w, int h){
        try {
            primaryStage.setTitle(title);
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            primaryStage.setScene(new Scene(root, w, h));
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
