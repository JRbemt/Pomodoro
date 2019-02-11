package Pomodoro.controllers;

import Pomodoro.FontAwesome;
import Pomodoro.PomodoroApp;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControlOverlay implements Initializable{

    @FXML
    private AnchorPane root;
    @FXML
    private Label close_button, settings_button, app_title;

    @FXML
    private HBox app_bar;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    private void initialize(){
        AnchorPane.setLeftAnchor(app_bar, 0.0);
        AnchorPane.setRightAnchor(app_bar, 0.0);
        app_title.setMouseTransparent(true);
        app_title.setText(PomodoroApp.instance().getString("app_name", "Pomodoro"));
        close_button.setText(FontAwesome.TIMES);
        settings_button.setText(FontAwesome.COG);
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

    private Window stage(){
        return root.getScene().getWindow();
    }
    private final Stage settingsStage = new Stage();
    @FXML
    public void openSettings(){
        if (settingsStage.getScene() != null){
            if (!settingsStage.isShowing()) {
                settingsStage.show();
            }
            return;
        }
        settingsStage.setAlwaysOnTop(PomodoroApp.instance().getSettings().ALWAYS_ON_TOP);
        settingsStage.initStyle(StageStyle.TRANSPARENT);
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initOwner(stage());
        FXMLLoader loader = new FXMLLoader(PomodoroApp.class.getResource("layout/settings.fxml"));
        loader.setResources(PomodoroApp.instance().getBundle());
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 500);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().addAll("Pomodoro/stylesheets/overlay.css", "Pomodoro/stylesheets/settings.css");
            settingsStage.setScene(scene);
            settingsStage.show();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void close(){
        root.getScene().getWindow().hide();
        Platform.exit();
    }
}
