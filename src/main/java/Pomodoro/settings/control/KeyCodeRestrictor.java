package Pomodoro.settings.control;

import Pomodoro.keystrokelistener.NativeKeyCode;

public class KeyCodeRestrictor implements StringRestrictor<NativeKeyCode> {
    public KeyCodeRestrictor(Class<? extends NativeKeyCode> clazz){}

    @Override
    public Class<? extends NativeKeyCode> getType() {
        return NativeKeyCode.class;
    }

    @Override
    public String toString(NativeKeyCode object) {
        return object.toString();
    }

    @Override
    public NativeKeyCode parse(String str) throws IllegalArgumentException {
        if (str.equals(""))
            return null;
        return (NativeKeyCode) NativeKeyCode.valueOf(str, false);
    }
}
