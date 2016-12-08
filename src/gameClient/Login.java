package gameClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import game.action.Action;
import game.action.JoinServer;
import gameClient.network.Connection;
import javafx.fxml.FXML;

public class Login {
    @FXML
    JFXButton loginButton;
    @FXML
    JFXTextField usernameField;

    public void loginButtonPressed() {
        System.out.println("Login con username: " + usernameField.textProperty().getValue());
        new Connection("127.0.0.1", 1337);//Starts new listener for incoming messages and creates a SocketWriter
        // Prepare login Action
        Action action = new JoinServer("Bau");
        ClientObjs.getSocketWriter().sendAction(action);
    }
}
