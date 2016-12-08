package gameClient.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import game.model.Match;
import gameClient.ClientObjs;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Select Room Page.
 */
public class SelectRoomController implements Initializable {
    @FXML
    private JFXTreeTableView<Match> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<Match, String> firstColumn = new JFXTreeTableColumn<>("Nome Stanza");
        firstColumn.setPrefWidth(350);
        firstColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Match, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Match, String> param) {
                //getValue() is a tree item, getValue() is Row OBJ,
                return param.getValue().getValue().pNameProperty();
            }
        });

        //Start listening for changes on displayed matches
        ClientObjs.getMatchList().addListener((ListChangeListener<Match>) (c -> {
            Platform.runLater(new Runnable() {
                public void run() {
                    //If matches has changed we have to update our list
                    System.out.println("Changed");
                    treeView.setRoot(new RecursiveTreeItem<Match>(ClientObjs.getMatchList(), RecursiveTreeObject::getChildren));
                }
            });
        }));

        //Create a root object and append all rows
        //final TreeItem<Match> root = new RecursiveTreeItem<Match>(new Match(), RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(firstColumn);
        treeView.setRoot(new RecursiveTreeItem<Match>(ClientObjs.getMatchList(), RecursiveTreeObject::getChildren));
        treeView.setShowRoot(false);//not show root element

    }
}
