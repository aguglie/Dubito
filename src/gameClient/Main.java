package gameClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientObjs.getSceneController().setPrimaryStage(primaryStage);//What happens on stage is directed by SceneController
        //ClientObjs.getSceneController().showLogin();//Shows login page
        ClientObjs.getSceneController().showSelectRoom();//Shows login page
    }

    public static void main(String[] args) {
        launch(args);
    }
}
