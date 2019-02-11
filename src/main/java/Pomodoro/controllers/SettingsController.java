package Pomodoro.controllers;

import Pomodoro.FontAwesome;
import Pomodoro.PomodoroApp;
import Pomodoro.settings.Option;
import Pomodoro.settings.OptionFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable{

    @FXML
    private Label close_button, app_title;
    @FXML
    private VBox root;
    @FXML
    private ListView<Option> settings_list;
    private ObservableList<Option> data = FXCollections.observableArrayList(OptionFactory.getOptions());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    private void initialize(){
        close_button.setText(FontAwesome.TIMES);
        app_title.setMouseTransparent(true);
        settings_list.setItems(data);
        settings_list.setCellFactory(param -> new OptionCell());
        settings_list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> settings_list.getChildrenUnmodifiable());
    }

    @FXML
    public void close(){
        stage().hide();
        PomodoroApp.instance().getSettings().save();
    }

    private final Window stage(){
        return root.getScene().getWindow();
    }

    private double xOffset, yOffset;

    @FXML
    public void onAppBarDrag(MouseEvent event){
        stage().setX(event.getScreenX() + xOffset);
        stage().setY(event.getScreenY() + yOffset);
        event.consume();
    }
    @FXML
    public void appBarDragStarted(MouseEvent event){
        xOffset = stage().getX() - event.getScreenX();
        yOffset = stage().getY() - event.getScreenY();
        event.consume();
    }

    public class OptionCell extends ListCell<Option>{
        @Override
        protected void updateItem(Option item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
               setGraphic(OptionFactory.inflateOption(item));
            }
        }
    }
}
