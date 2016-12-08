package gameClient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import game.action.Action;
import game.action.JoinServer;
import gameClient.ClientObjs;
import gameClient.network.Connection;
import javafx.fxml.FXML;

public class LoginController {
    @FXML
    JFXButton loginButton;
    @FXML
    JFXTextField usernameField;

    public void loginButtonPressed() {
        new Connection("127.0.0.1", 1337);//Starts new listener for incoming messages and creates a SocketWriter
        // Prepare login Action
        Action action = new JoinServer(usernameField.textProperty().getValue());
        ClientObjs.getSocketWriter().sendAction(action);
        ClientObjs.getSceneController().showSelectRoom();
    }
}
