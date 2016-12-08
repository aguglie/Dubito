package gameClient.utils;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by andrea on 08/12/16.
 */
public class TreeTableRowObj extends RecursiveTreeObject<TreeTableRowObj> {
    private StringProperty first;
    private StringProperty second;
    private StringProperty third;

    public TreeTableRowObj(String first, String second, String third) {
        this.first = new SimpleStringProperty(first);
        this.second = new SimpleStringProperty(second);
        this.third = new SimpleStringProperty(third);
    }

    public StringProperty firstProperty() {
        return first;
    }

    public StringProperty secondProperty() {
        return second;
    }

    public StringProperty thirdProperty() {
        return third;
    }

}
