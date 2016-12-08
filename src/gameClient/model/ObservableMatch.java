package gameClient.model;


import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Pivot class used to display matches in client
 */
public class ObservableMatch extends RecursiveTreeObject<ObservableMatch> {
    private StringProperty name;
    private int id;

    public ObservableMatch(String name, int id) {
        this.name = new SimpleStringProperty(name);
        this.id = id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getId() {
        return id;
    }
}
