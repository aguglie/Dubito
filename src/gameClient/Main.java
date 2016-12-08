package gameClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController.getInstance().setPrimaryStage(primaryStage);//What happens on stage is directed by SceneController
        SceneController.getInstance().showLogin();//Shows login page
    }

    public static void main(String[] args) {
        launch(args);
    }
}
