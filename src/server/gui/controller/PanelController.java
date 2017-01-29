package server.gui.controller;

/**
 * Created by andrea on 26/01/17.
 */
import com.jfoenix.controls.*;
import game.model.Match;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.Duration;
import server.Main;
import server.model.User;

import java.net.URL;
import java.util.ResourceBundle;
public class PanelController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private ListView<server.model.Match> roomsList;

    @FXML
    private ListView<server.model.User> roomUsersList;

    @FXML
    private Label selectedUserState;

    @FXML
    private Label selectedUsername;

    @FXML
    private Label selectedCardsNumber;

    private ObservableList<server.model.Match> matches;
    private ObservableList<server.model.User> users;
    private server.model.Match selectedMatch = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setPanelController(this);//give reference

        ////Room Tree:
        matches = FXCollections.observableArrayList (server.model.Match.getMatches());
        roomsList.setItems(matches);
        roomsList.setCellFactory(new Callback<ListView<server.model.Match>, ListCell<server.model.Match>>(){

            @Override
            public ListCell<server.model.Match> call(ListView<server.model.Match> p) {

                ListCell<server.model.Match> cell = new ListCell<server.model.Match>(){

                    @Override
                    protected void updateItem(server.model.Match t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName() + " ( "+t.getUsers().size()+" users)");
                        }
                    }

                };

                return cell;
            }
        });

        /////Users Tree
        users = FXCollections.observableArrayList();
        roomUsersList.setItems(users);
        roomUsersList.setCellFactory(new Callback<ListView<server.model.User>, ListCell<server.model.User>>(){

            @Override
            public ListCell<server.model.User> call(ListView<server.model.User> p) {

                ListCell<server.model.User> cell = new ListCell<User>(){

                    @Override
                    protected void updateItem(server.model.User t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getUsername());
                        }
                    }

                };

                return cell;
            }
        });

        //Update Lists:
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> {
                    matches.clear();
                    matches.addAll(server.model.Match.getMatches());
                    if (selectedMatch!=null){
                        try{
                            users.clear();
                            users.addAll(selectedMatch.getUsers());
                        }catch (Exception e){
                            e.printStackTrace();
                            users.clear();
                            selectedMatch = null;
                        }
                    }

                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    @FXML
    void kickUser(ActionEvent event) {

    }

    @FXML
    void showUserCards(ActionEvent event) {

    }

    public void showModal(String title, String text) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showModal(title, text));
            return;
        }
        JFXButton button = new JFXButton("Ok");
        button.setStyle("-fx-background-color: limegreen");
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(title));
        content.setBody(new Label(text));
        content.setActions(button);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER, false);
        dialog.show();
        button.setOnAction(action -> dialog.close());
    }

}
