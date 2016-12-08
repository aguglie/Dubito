package gameClient.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import game.model.Match;
import gameClient.ClientObjs;
import gameClient.model.ObservableMatch;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Select Room Page.
 */
public class SelectRoomController implements Initializable {
    @FXML
    private JFXTreeTableView<ObservableMatch> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<ObservableMatch, String> firstColumn = new JFXTreeTableColumn<>("Nome Stanza");
        firstColumn.setPrefWidth(350);
        firstColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ObservableMatch, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ObservableMatch, String> param) {
                //getValue() is a tree item, getValue() is Row OBJ,
                return param.getValue().getValue().nameProperty();
            }
        });

        //Start listening for changes on displayed matches
        ClientObjs.getMatchList().addListener((ListChangeListener<Match>) (c -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    //If matches has changed we have to update our displayed list
                    ObservableList<ObservableMatch> matches = FXCollections.observableArrayList();
                    ClientObjs.getMatchList().forEach(m -> matches.add(new ObservableMatch(m.getName())));
                    treeView.setRoot(new RecursiveTreeItem<ObservableMatch>(matches, RecursiveTreeObject::getChildren));
                }
            });
        }));


        treeView.getColumns().setAll(firstColumn);
        //Create a root object and append all rows
        ObservableList<ObservableMatch> matches = FXCollections.observableArrayList();
        ClientObjs.getMatchList().forEach(m -> matches.add(new ObservableMatch(m.getName())));
        treeView.setRoot(new RecursiveTreeItem<ObservableMatch>(matches, RecursiveTreeObject::getChildren));
        treeView.setShowRoot(false);//not show root element

    }
}
