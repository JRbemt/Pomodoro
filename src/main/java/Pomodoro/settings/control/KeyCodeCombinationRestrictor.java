package Pomodoro.settings.control;

import Pomodoro.keystrokelistener.NativeKeyCombination;
import javafx.scene.input.KeyCombination;

public class KeyCodeCombinationRestrictor implements StringRestrictor<NativeKeyCombination> {
    public KeyCodeCombinationRestrictor(Class<NativeKeyCombination> clazz){

    }
    @Override
    public Class<? extends NativeKeyCombination> getType() {
        return null;
    }

    public String toString(KeyCombination combination){
        return combination.toString();
    }

    @Override
    public NativeKeyCombination parse(String str) throws IllegalArgumentException {
        if (str.length() == 0 || str.charAt(str.length()-1) == '+')
            return null;
        else return NativeKeyCombination.valueOf(str);
    }
}
