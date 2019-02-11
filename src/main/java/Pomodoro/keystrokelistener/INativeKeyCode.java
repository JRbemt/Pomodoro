package Pomodoro.keystrokelistener;

import org.jnativehook.keyboard.NativeKeyEvent;

public interface INativeKeyCode {
    String NULL = Character.toString((char) 128);
    /**
     * Virtual keycode
     * */
    int keycode();
    /**
     * String representation of key
     * */
    String keyname();

    /**
     *
     **/
    default String keyCharacter(){
        if (isCharKey())
            return NativeKeyEvent.getKeyText(keycode());
        return NULL;
    }

    /**
     * True if code represents a single key such as 'A' or 'Ctrl_L'
     * False if it represents multiple such as 'Ctrl'
     * */
    default boolean isSingleKey(){
        return false;
    }

    default boolean isCharKey(){
        return false;
    }
    /**
     * Is Shift, Ctrl etc.
     **/
    default boolean isModifier(){
        return false;
    }
    /**
     * Is F1, F2 etc.
     * */
    default boolean isFuntionKey(){
        return NativeKeyCode.isFunctionKey(keycode());
    }

    default boolean isMediaKey(){
        return NativeKeyCode.isMediaKey(keycode());
    }
}
