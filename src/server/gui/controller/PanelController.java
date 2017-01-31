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

    private ObservableList<server.model.Match> matches;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.setPanelController(this);//give reference

        ////Room Tree:
        matches = FXCollections.observableArrayList(server.model.Match.getMatches());
        roomsList.setItems(matches);
        roomsList.setCellFactory(new Callback<ListView<server.model.Match>, ListCell<server.model.Match>>() {

            @Override
            public ListCell<server.model.Match> call(ListView<server.model.Match> p) {

                ListCell<server.model.Match> cell = new ListCell<server.model.Match>() {

                    @Override
                    protected void updateItem(server.model.Match t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName() + " ( " + t.getUsers().size() + " users)");
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
                }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }
}
