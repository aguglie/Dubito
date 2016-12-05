package gameClient;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class Login {
    @FXML
    JFXButton loginButton;
    @FXML
    JFXTextField usernameField;

    public void loginButtonPressed() {
        System.out.println("Login con username: " + usernameField.textProperty().getValue());
    }
}
