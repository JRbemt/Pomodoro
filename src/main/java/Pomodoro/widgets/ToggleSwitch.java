package Pomodoro.widgets;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ToggleSwitch extends Pane {

    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);

    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);


    public double ratio = 2;

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    private Rectangle background;
    private Circle trigger;

    public ToggleSwitch(double width){
        this(width, 2);
    }
    public ToggleSwitch(double width, double ratio) {
        this.ratio = ratio;
        background = new Rectangle();
        background.setFill(Color.WHITE);
        background.setStroke(Color.LIGHTGRAY);

        trigger = new Circle();
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            if (has_width) {
                translateAnimation.setToX(newState ? background.getWidth() - trigger.getRadius() * 2.0 : 0);
                fillAnimation.setFromValue(newState ? Color.WHITE : Color.LIGHTGREEN);
                fillAnimation.setToValue(newState ? Color.LIGHTGREEN : Color.WHITE);

                animation.play();
            }
        });

        setOnMouseClicked(event -> switchedOn.set(!switchedOn.get()));
        prefWidthProperty().addListener((observable, oldValue, newValue) -> {
            double h = newValue.doubleValue() / ratio;
            setPrefHeight(h);
        });
        setPrefWidth(width);
    }

    private boolean has_width = false;
    public boolean layout_ready = false;

    private void layoutBackground(){
        double w = getPrefWidth();
        double h = getPrefHeight();
        background.setWidth(w);
        background.setHeight(h);
        background.setArcHeight(h);
        background.setArcWidth(h);
        double radius = w/4.0;
        trigger.setCenterX(radius);
        trigger.setCenterY(radius);
        trigger.setRadius(radius);
    }

    @Override
    protected void layoutChildren() {
        layoutBackground();
        super.layoutChildren();
        if (!has_width){
            has_width = true;
            if (switchedOn.get()) {
                switchedOnProperty().set(false);
                switchedOnProperty().set(true);
            }
            layout_ready = true;
        }
    }
}