package Pomodoro.keystrokelistener;

import org.jnativehook.keyboard.NativeKeyEvent;

public interface IMatchable {
    boolean match(NativeKeyEvent event);
}
