package Pomodoro;

import javafx.util.Duration;

public class Attempt {
    public final static Attempt DEFAULT_WORK_TIME = new Attempt(Duration.minutes(25), TaskType.WORK);
    public final static Attempt LONG_BREAK = new Attempt(Duration.minutes(10), TaskType.BREAK);
    public final static Attempt SMALL_BREAK = new Attempt(Duration.minutes(5), TaskType.BREAK);
    private final Duration duration;
    private final TaskType type;

    public Attempt(Duration duration, TaskType type){
        this.duration = duration;
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public TaskType getType() {
        return type;
    }

    public enum TaskType{
        BREAK,
        WORK
    }
}
