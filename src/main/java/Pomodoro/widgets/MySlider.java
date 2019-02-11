package Pomodoro.widgets;

import javafx.scene.control.Slider;

public class MySlider extends Slider{
    private double stepsize = 0.1d;

    public MySlider(){
        setShowTickLabels(true);
        valueProperty().addListener((observable, oldValue, newValue) -> {
            setValue(Math.round(newValue.doubleValue()/ stepsize) * stepsize);
        });
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
    }


    public void setRange(double min, double max, double stepsize){
        setMin(min);
        setMax(max);
        this.stepsize = stepsize;
        setMajorTickUnit((max-min)/2);
    }

    public void setStepsize(double stepsize) {
        this.stepsize = stepsize;
    }
}
