package Pomodoro.keystrokelistener;

import Pomodoro.keystrokelistener.fxcompat.JavaFXKeyAdapter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import static org.jnativehook.keyboard.NativeKeyEvent.*;

public class NativeKeyCode implements INativeKeyCode, IMatchable{
    public final static int UNDEFINED_CODE = -1;
    private final int keycode;
    private final String keyname;
    private final boolean isCharKey;
    private final boolean isFunctionKey;
    private final boolean isMediaKey;

    public NativeKeyCode(int keycode, String keyname, boolean isCharKey, boolean isFunctionKey, boolean isMediaKey) {
        this.keycode = keycode;
        this.keyname = keyname;
        this.isCharKey = isCharKey;
        this.isFunctionKey = isFunctionKey;
        this.isMediaKey = isMediaKey;
    }

    public static INativeKeyCode charKey(int keycode, String keyname){
        return new NativeKeyCode(keycode, keyname, true, false, false);
    }

    public static INativeKeyCode undefinedCharKey(String name){
        return new NativeKeyCode(UNDEFINED_CODE, name, true, false, false);
    }

    public static INativeKeyCode functionKey(int keycode, String keyname){
        return new NativeKeyCode(keycode, keyname, false, true, false);
    }

    public static INativeKeyCode mediaKey(int keycode, String keyname){
        return new NativeKeyCode(keycode, keyname, false, false, true);
    }

    public static INativeKeyCode specialKey(int keycode, String keyname){
        return new NativeKeyCode(keycode, keyname, false, false, false);
    }

    public static INativeKeyCode modifierKey(String name){
        return NativeKeyModifiers.fromName(name);
    }

    @Override
    public int keycode() {
        return keycode;
    }

    @Override
    public String keyname() {
        return keyname;
    }

    @Override
    public boolean isCharKey() {
        return isCharKey;
    }

    @Override
    public boolean isFuntionKey() {
        return isFunctionKey;
    }

    @Override
    public String toString() {
        return keyname;
    }

    @Override
    public boolean match(NativeKeyEvent event) {
        if (keycode != UNDEFINED_CODE)
            return event.getKeyCode() == keycode;
        else {
            return NativeKeyEvent.getKeyText(event.getKeyCode()).equals(keyname);
        }
    }

    public static INativeKeyCode valueOf(String name, boolean requireKeyCode){
        if (name == null || name.equals(""))
            return null;
        if (name.startsWith("\'") && name.endsWith("\'")) {
            name = name.substring(1, name.length()-1);
        }
        NativeKeyModifiers mod;
        int len = name.length();
        boolean isChar = len == 1 && Character.isAlphabetic(name.charAt(0));
        if (!requireKeyCode && isChar){
            return NativeKeyCode.undefinedCharKey(name);
        } else if ((mod = NativeKeyModifiers.fromName(name)) != null) {
            return mod;
        } else {
            try {
                if (len == 1 && !isChar){
                    return null;
                } else {
                    String fname = name.startsWith("VC_") ? name : "VC_" + name.toUpperCase();
                    int keycode = NativeKeyEvent.class.getField(fname).getInt(null);
                    if (isFunctionKey(keycode)) {
                        return NativeKeyCode.functionKey(keycode, name);
                    } else if (isMediaKey(keycode)) {
                        return NativeKeyCode.mediaKey(keycode, name);
                    } else if (isChar) {
                        return NativeKeyCode.charKey(keycode, name);
                    } else return specialKey(keycode, name);
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                System.out.println("Field :"+name+", not found");
            }
        }
        return null;
    }
    public boolean matchFXEvent(KeyEvent event){
        return matchFXEvent(event, true);
    }
    private transient KeyCode FXCode = null;
    public boolean matchFXEvent(KeyEvent event, boolean noModifiers){
        if (FXCode == null)
            FXCode = JavaFXKeyAdapter.convertToFXKeyCode(keycode);
        return (!noModifiers || !(event.isShiftDown() || event.isAltDown() || event.isControlDown() || event.isMetaDown()))
                && ((keycode == NativeKeyCode.UNDEFINED_CODE)
                ? event.getCode().getName().equalsIgnoreCase(keyname)
                : event.getCode().equals(FXCode));
    }

    @Override
    public boolean isMediaKey() {
        return isMediaKey;
    }

    //region Keytype
    public static boolean isFunctionKey(int keycode){
        switch (keycode){
            case VC_F1:
            case VC_F2:
            case VC_F3:
            case VC_F4:
            case VC_F5:
            case VC_F6:
            case VC_F7:
            case VC_F8:
            case VC_F9:
            case VC_F10:
            case VC_F11:
            case VC_F12:

            case VC_F13:
            case VC_F14:
            case VC_F15:
            case VC_F16:
            case VC_F17:
            case VC_F18:
            case VC_F19:
            case VC_F20:
            case VC_F21:
            case VC_F22:
            case VC_F23:
            case VC_F24:
                return true;
        }
        return false;
    }

    public static boolean isMediaKey(int keycode){
        switch (keycode){
            case VC_POWER:
            case VC_SLEEP:
            case VC_WAKE:

            case VC_MEDIA_PLAY:
            case VC_MEDIA_STOP:
            case VC_MEDIA_PREVIOUS:
            case VC_MEDIA_NEXT:
            case VC_MEDIA_SELECT:
            case VC_MEDIA_EJECT:

            case VC_VOLUME_MUTE:
            case VC_VOLUME_UP:
            case VC_VOLUME_DOWN:

            case VC_APP_MAIL:
            case VC_APP_CALCULATOR:
            case VC_APP_MUSIC:
            case VC_APP_PICTURES:

            case VC_BROWSER_SEARCH:
            case VC_BROWSER_HOME:
            case VC_BROWSER_BACK:
            case VC_BROWSER_FORWARD:
            case VC_BROWSER_STOP:
            case VC_BROWSER_REFRESH:
            case VC_BROWSER_FAVORITES:
                return true;
        }
        return false;
    }

    //endregion
}
