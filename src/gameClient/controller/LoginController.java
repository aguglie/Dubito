package gameClient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import game.action.Action;
import game.action.JoinServer;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import gameClient.network.Connection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;


import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    @FXML
    private StackPane root;

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXButton loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneDirector.getInstance().setRoot(root);//update root element.
    }

    public void loginButtonPressed() {
        new Connection("79.7.0.125", 1337);//Starts new listener for incoming messages and creates a SocketWriter
        // Prepare login Action
        Action action = new JoinServer(usernameField.textProperty().getValue());
        ClientObjs.getSocketWriter().sendAction(action);
    }
}
