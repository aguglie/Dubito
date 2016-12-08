package gameClient;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import gameClient.utils.TreeTableRowObj;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectRoom implements Initializable {
    @FXML
    private JFXTreeTableView<TreeTableRowObj> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXTreeTableColumn<TreeTableRowObj, String> firstColumn = new JFXTreeTableColumn<>("First Column");
        firstColumn.setPrefWidth(150);
        firstColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TreeTableRowObj, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TreeTableRowObj, String> param) {
                //getValue() is a tree item, getValue() is Row OBJ,
                return param.getValue().getValue().firstProperty();
            }
        });


        ClientObjs.getRows().add(new TreeTableRowObj("abcd","deeeeef","ghiii"));

        //Create a root object and append all rows
        final TreeItem<TreeTableRowObj> root = new RecursiveTreeItem<TreeTableRowObj>(ClientObjs.getRows(), RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(firstColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);//not show root element

    }
}
