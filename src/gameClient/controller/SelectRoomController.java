package gameClient.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import game.action.Action;
import game.action.CreateMatch;
import game.action.JoinMatch;
import game.action.StartGame;
import game.model.Match;
import gameClient.ClientObjs;
import gameClient.SceneDirector;
import gameClient.model.ObservableMatch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Select Room Page.
 */
public class SelectRoomController implements Initializable {
    @FXML
    private StackPane root;

    @FXML
    private JFXTreeTableView<ObservableMatch> treeView;//Box where matches are listed

    @FXML
    private JFXTextField roomName;//Text field where new match name is typed

    @FXML
    private JFXButton addButton;//Add button

    @FXML
    private JFXButton startButton;//Inizia a giocare button


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SceneDirector.getInstance().setRoot(root);//update root element.

        JFXTreeTableColumn<ObservableMatch, String> firstColumn = new JFXTreeTableColumn<>("Nome Stanza");
        firstColumn.setPrefWidth(270);
        firstColumn.setCellValueFactory(param -> {
            //getValue() is a tree item, getValue() is Row OBJ,
            return param.getValue().getValue().nameProperty();
        });

        //Start listening for changes on displayed matches
        ClientObjs.getMatchList().addListener((ListChangeListener<Match>) c -> {
            Platform.runLater(() -> {
                //If matches has changed we have to update our displayed list
                ObservableList<ObservableMatch> matches = FXCollections.observableArrayList();
                ClientObjs.getMatchList().forEach(m -> matches.add(new ObservableMatch(m.getName(), ClientObjs.getMatchList().indexOf(m))));//o(n^2) here...
                treeView.setRoot(new RecursiveTreeItem<ObservableMatch>(matches, RecursiveTreeObject::getChildren));
            });
        });


        treeView.getColumns().setAll(firstColumn);
        //Create a root object and append all rows
        ObservableList<ObservableMatch> matches = FXCollections.observableArrayList();
        ClientObjs.getMatchList().forEach(m -> matches.add(new ObservableMatch(m.getName(), ClientObjs.getMatchList().indexOf(m))));//o(n^2) here..
        treeView.setRoot(new RecursiveTreeItem<ObservableMatch>(matches, RecursiveTreeObject::getChildren));
        treeView.setShowRoot(false);//not show root element


        //Setup events
        treeView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2)
            {
                TreeItem<ObservableMatch> item = treeView.getSelectionModel().getSelectedItem();
                Action action = new JoinMatch(item.getValue().getId());
                ClientObjs.getSocketWriter().sendAction(action);
                //treeView.setDisable(true);
                startButton.setDisable(false);

                startButton.setOnAction((e) -> {
                    Action startAction = new StartGame();
                    ClientObjs.getSocketWriter().sendAction(startAction);
                });
            }
        });

    }

    public void addButtonPressed(){
        Action action = new CreateMatch(roomName.textProperty().getValue());
        ClientObjs.getSocketWriter().sendAction(action);
        roomName.setText("");
    }
}
