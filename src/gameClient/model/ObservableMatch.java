package gameClient.model;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Pivot class used to display matches in client
 */
public class ObservableMatch extends RecursiveTreeObject<ObservableMatch> {
    private StringProperty name;

    public StringProperty nameProperty() {
        return name;
    }

    public ObservableMatch(String name) {
        this.name = new SimpleStringProperty(name);
    }
}
