package gameClient;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneDirector.getInstance().setPrimaryStage(primaryStage);//What happens on stage is directed by SceneDirector
        SceneDirector.getInstance().showLogin();//Shows login page
        //SceneDirector.getInstance().showGame();//Shows game page
    }

    public static void main(String[] args) {
        launch(args);
    }
}
