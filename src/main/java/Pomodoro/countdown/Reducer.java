package Pomodoro.countdown;

public class Reducer {

    private int start;
    protected int ticks = 0;
    protected int difference = 0;

    public Reducer from(int start){
        this.start = start;
        reset();
        return this;
    }

    public void reset(){
        ticks = 0;
        update();
    }

    protected void update(){
        difference = start - ticks;
    }

    public final Reducer reduce(){
        ticks++;
        update();
        return this;
    }

    public boolean isFinished(){
        return difference <= 0;
    }

    public int getDifference() {
        return difference;
    }
}
