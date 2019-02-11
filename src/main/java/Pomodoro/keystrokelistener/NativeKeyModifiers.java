package Pomodoro.keystrokelistener;

import java.util.HashMap;

import static org.jnativehook.NativeInputEvent.*;

public enum NativeKeyModifiers implements INativeKeyCode {
    ALT(ALT_MASK, "Alt", false),
    ALT_L(ALT_L_MASK, "Alt_L"),
    ALT_R(ALT_R_MASK, "Alt_R"),
    META(META_MASK, "Meta", false),
    META_L(META_L_MASK, "Meta_L"),
    META_R(META_R_MASK, "Meta_R"),
    SHIFT(SHIFT_MASK, "Shift", false),
    SHIFT_L(SHIFT_L_MASK, "Shift_L"),
    SHIFT_R(SHIFT_R_MASK, "Shift_R"),
    CTRL(CTRL_MASK, "Ctrl", false),
    CTRL_L(CTRL_L_MASK, "Ctrl_L"),
    CTRL_R(CTRL_R_MASK, "Ctrl_R"),
    NUM_LOCK(NUM_LOCK_MASK, "NUM_LOCK"),
    CAPS_LOCK(CAPS_LOCK_MASK, "CAPS_LOCK"),
    SCROLL_LOCK(SCROLL_LOCK_MASK, "SCROLL_LOCK"),
    BUTTON1(BUTTON1_MASK, "Button1"),
    BUTTON2(BUTTON2_MASK, "Button2"),
    BUTTON3(BUTTON3_MASK, "Button3"),
    BUTTON4(BUTTON4_MASK, "Button4");

    private final int mask;
    private final String name;
    private final boolean isSingleKey;

    NativeKeyModifiers(int mask, String name){
        this(mask, name, true);
    }
    NativeKeyModifiers(int mask, String name, boolean singleKey){
        this.mask = mask;
        this.name = name;
        this.isSingleKey = singleKey;
    }

    private static final HashMap<String, NativeKeyModifiers> nameMap = new HashMap<>(values().length);
    static {
        for (NativeKeyModifiers mask : values()){
            nameMap.put(mask.name.toLowerCase(), mask);
        }
    }

    @Override
    public String keyname() {
        return name;
    }
    @Override
    public int keycode() {
        return mask;
    }

    @Override
    public boolean isSingleKey() {
        return isSingleKey;
    }

    @Override
    public boolean isModifier() {
        return true;
    }

    public static NativeKeyModifiers fromName(String name){
        return nameMap.get(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
