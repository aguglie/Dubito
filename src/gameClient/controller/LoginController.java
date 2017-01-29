package gameClient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import game.action.Action;
import game.action.JoinServer;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import gameClient.network.Connection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


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
        if (usernameField.textProperty().getValue()==""){
            return;
        }
        JFXButton button = new JFXButton("Bene per me");
        button.setStyle("-fx-background-color: limegreen");
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Con chi vuoi giocare?"));
        VBox vBox = new VBox();
        JFXTextField jfxTextField = new JFXTextField("server.guglio.net");
        vBox.getChildren().addAll(new Label("A che server vuoi connetterti?"), jfxTextField);
        content.setBody(vBox);
        content.setActions(button);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER, false);
        dialog.show();
        button.setOnAction(action -> {
            if (jfxTextField.textProperty().getValue()!="") {
                ClientObjs.setServerAddress(jfxTextField.getText());

                new Connection(jfxTextField.textProperty().getValue(), 8080);//Starts new listener for incoming messages and creates a SocketWriter

                Action loginAction = new JoinServer(usernameField.textProperty().getValue());
                ClientObjs.getSocketWriter().sendAction(loginAction);

                dialog.close();
            }
        });
    }
}
