package server;

import javafx.application.Application;
import server.gui.controller.PanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.network.SocketsListener;

import static javafx.application.Application.launch;

/**
 * This main file starts a SocketsListener
 * Created by andrea on 18/10/16.
 */
public class Main  extends Application {


    private static PanelController panelController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Server Admin");
        Parent root = FXMLLoader.load(getClass().getResource("gui/view/panel.fxml"));
        primaryStage.setScene(new Scene(root, 710, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread thread = new Thread(){
            public void run(){
                int port = 8080;
                SocketsListener socketsListener = SocketsListener.getInstance(port);
                socketsListener.startServer();
            }
        };
        thread.start();
        launch(args);
    }


    public static PanelController getPanelController() {
        return panelController;
    }

    public static void setPanelController(PanelController panelController) {
        Main.panelController = panelController;
    }
}
