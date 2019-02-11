package Pomodoro.keystrokelistener;

import Pomodoro.Utils.Util;
import javafx.scene.input.KeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.List;

public class NativeKeyCodeCombination extends NativeKeyCombination {
    private final NativeKeyCode keyCode;

    public NativeKeyCodeCombination(NativeKeyCode keyCode,
                                    NativeKeyModifiers... modifiers){
        super(modifiers);
        this.keyCode = keyCode;
        if (keyCode == null)
            throw new IllegalArgumentException("Keycode cannot be null");
    }
    public NativeKeyCodeCombination(String keyname,
                                    NativeKeyModifiers... modifiers) {
        this((NativeKeyCode) NativeKeyCode.valueOf(keyname, false), modifiers);
    }

    public NativeKeyCodeCombination(NativeKeyCode keyCode,
                                    List<NativeKeyModifiers> modifiers){
        this(keyCode, Util.toList(NativeKeyModifiers.class, modifiers));
    }

    public NativeKeyCode getKeyCode() {
        return keyCode;
    }

    @Override
    public String getName() {
        StringBuilder sb = new StringBuilder();
        super.addModifiersIntoString(sb);
        sb.append("+").append(keyCode.toString());
        return sb.toString();
    }

    @Override
    public boolean matchFXEvent(KeyEvent event){
        return keyCode.matchFXEvent(event, false)
                && super.matchFXEvent(event);
    }

    @Override
    public boolean match(NativeKeyEvent event) {
        return keyCode.match(event) && super.match(event);
    }

}
