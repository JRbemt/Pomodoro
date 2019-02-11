package Pomodoro.controllers;

import Pomodoro.Attempt;
import Pomodoro.PomodoroApp;
import Pomodoro.Utils.Deque;
import Pomodoro.countdown.TimeReducer;
import Pomodoro.keystrokelistener.NativeKeyCodeCombination;
import Pomodoro.keystrokelistener.NativeKeyCombination;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PomodoroStack implements Initializable{
    @FXML
    private StackPane root;
    @FXML
    private Pane transparant_overlay;
    @FXML
    private Label clock;

    private FadeTransition fade_out;

    private boolean paused = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    private void initialize(){
        transparant_overlay.setFocusTraversable(true);
        fade_out = new FadeTransition(Duration.millis(500), transparant_overlay);
        transparant_overlay.setCache(true);
        transparant_overlay.setCacheHint(CacheHint.SPEED);
        fade_out.setToValue(0.0);
        fade_out.setFromValue(1.0);
        transparant_overlay.setOpacity(0.0);
        timeline.setCycleCount(Animation.INDEFINITE);
        processEvent(PomodoroEvent.WORK_DEFAULT);
        root.sceneProperty().addListener((observable, oldValue, newValue) -> newValue.windowProperty()
                .addListener((winobs, oldWin, newWin) -> {
                    newWin.setOnHidden(event -> hideOverlay());
        }));
    }

    private final TimeReducer reducer = new TimeReducer();
    private final Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1.0),
            (event)->{
                reducer.reduce();
                updateTime();
            })
    );
    public final Deque<Attempt> queue = new Deque<Attempt>(true, 16, Attempt.class);
    private Attempt activeAttempt;

    public void processEvent(PomodoroEvent event){
        switch (event){
            case WORK_DEFAULT:
                startAttempt(Attempt.DEFAULT_WORK_TIME, true);
                break;
            case BREAK_LONG:
                startAttempt(Attempt.LONG_BREAK, true);
                break;
            case BREAK_SHORT:
                startAttempt(Attempt.SMALL_BREAK, true);
                break;
            case PAUSE:
                this.paused = !paused;
                if (paused) {
                    timeline.pause();
                } else if (activeAttempt != null && !isFinished())
                    timeline.play();
                break;
            case RESET:
                if (activeAttempt != null)
                    startAttempt(activeAttempt, true);
                break;
            case QUIT_ALARM:
                if (isNotificationShowing){
                    notification.dismiss();
                    root.getScene().getWindow().setFocused(true);
                    showOverlay();
                }
                break;
        }
    }
    public boolean isFinished(){
        return reducer.isFinished();
    }
    public void queueAttempt(Attempt attempt){
        startAttempt(attempt, false);
    }
    private void startAttempt(Attempt attempt, boolean overrideQueue){
        if (overrideQueue) {
            queue.clear();
            activeAttempt = null;
            if (isNotificationShowing)
                notification.dismiss();
        }
        queue.push(attempt);
        updateAttempt();
    }
    private void endAttempt(){
        activeAttempt = null;
        updateAttempt();
    }
    private void updateAttempt(){
        if (activeAttempt == null && !queue.isEmpty()) {
            activeAttempt = queue.pop(0);
            if (activeAttempt == null)
                return;
            reducer.from(activeAttempt.getDuration());
            reducer.reset();
            updateTime();
            if (!paused)
                timeline.play();
        }
    }

    private final TrayNotification notification = new TrayNotification();
    private boolean isNotificationShowing = false;

    private void updateTime(){
        if (reducer.getDifference() >= 0){
            clock.setText(reducer.format(":", true));
        }
        if (reducer.isFinished()) {
            timeline.stop();
            URL res = PomodoroApp.class.getResource(PomodoroApp.instance().getSettings().NOTIFICATION_FILE);
            Media sound = new Media(res.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(Integer.MAX_VALUE);

            if (notification.getMessage().equals("")) {
                notification.setTitle(PomodoroApp.instance().getString("attempt_finished", "attempt_finished"));
                String msg = activeAttempt.getType() == Attempt.TaskType.BREAK
                        ? PomodoroApp.instance().getString("back_to_work", "back_to_work")
                        : PomodoroApp.instance().getString("take_a_break", "take_a_break");
                notification.setMessage(msg);
                notification.setNotification(Notifications.SUCCESS);
                notification.setAnimation(Animations.POPUP);
                notification.setOnShown(event -> {
                    mediaPlayer.setVolume(PomodoroApp.instance().getSettings().VOLUME);
                    mediaPlayer.play();
                    root.requestFocus();
                });
                notification.setOnDismiss(event -> {
                    isNotificationShowing = false;
                    mediaPlayer.stop();
                    endAttempt();
                });
            }
            isNotificationShowing = true;
            notification.showAndWait();
        }
    }

    @FXML
    public void hideOverlay(){
        if (PomodoroApp.instance().getSettings().FREEZE_ON_HOVER) {
            if (!isFinished() && !paused)
                timeline.play();
        }
        fade_out.setDelay(Duration.seconds(1));
        fade_out.play();
    }
    @FXML
    public void showOverlay(){
        if (PomodoroApp.instance().getSettings().FREEZE_ON_HOVER)
            timeline.pause();
        fade_out.stop();
        transparant_overlay.requestFocus();
        transparant_overlay.setOpacity(1.0);
    }

    public enum PomodoroEvent{
        WORK_DEFAULT,
        BREAK_LONG,
        BREAK_SHORT,
        PAUSE,
        RESET,
        QUIT_ALARM
    }
}
