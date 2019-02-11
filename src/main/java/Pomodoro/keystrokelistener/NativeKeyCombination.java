package Pomodoro.keystrokelistener;

import Pomodoro.Utils.BitUtils;
import Pomodoro.Utils.BitVector;
import Pomodoro.Utils.Util;
import javafx.scene.input.KeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.ArrayList;

public class NativeKeyCombination implements IMatchable{
    private final BitVector downModifiers = new BitVector(NativeKeyModifiers.values().length);
    private final int specific_mask;   // such as Ctrl_L
    private final int[] generic_masks; // such as Ctrl
    private boolean strict; // if strict is true, no other modifiers can be pressed (Ctrl will not match Ctrl+Shift)

    protected NativeKeyCombination(NativeKeyModifiers... down) {
        this(true ,down);
    }
    private NativeKeyCombination(boolean strict, NativeKeyModifiers... down){
        this.strict = strict;
        int smask = 0;
        ArrayList<Integer> gmasks = new ArrayList<>();
        if (down.length == 0){
            throw new RuntimeException("No modifiers specified, match against NativeKeyCode instead");
        }
        for (int i = 0; i < down.length; i++) {
            NativeKeyModifiers mod = down[i];
            downModifiers.set(mod.ordinal());
            int kc = mod.keycode();
            if (mod.isSingleKey())
                smask |= kc;
            else {
                if (gmasks.contains(kc) || (smask & kc) > 0)
                    throw new RuntimeException("Duplicate modifier: "+mod.keyname());
                gmasks.add(kc);
            }
        }
        this.specific_mask = smask;
        this.generic_masks  = Util.toList(gmasks);
    }

    public String getName(){
        StringBuilder sb = new StringBuilder();
        addModifiersIntoString(sb);
        return sb.toString();
    }

    protected void addModifiersIntoString(StringBuilder sb){
        for (int i = 0; (i = downModifiers.nextSetBit(i)) >= 0; i++) {
            addModifierIntoString(sb, NativeKeyModifiers.values()[i]);
        }
    }

    private static void addModifierIntoString(StringBuilder sb,
                                              NativeKeyModifiers modifier){
        if (sb.length() > 0)
            sb.append("+");
        sb.append(modifier.keyname());
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    @Override
    public boolean match(NativeKeyEvent event){
        int mod = event.getModifiers();
        for (int generic_mask : generic_masks) {
            if ((mod & generic_mask) <= 0)
                return false;
            mod = BitUtils.lsXOR(mod, generic_mask);
        }
        int _smod = mod & specific_mask;            // works for specific_mask == 0!
        return (_smod == specific_mask              // check if our specific mask matches
                && (!strict || mod-_smod == 0));    // check for extra bits if strict
    }

    public boolean match(boolean shift, boolean ctrl, boolean meta, boolean alt){
        return requiresModifierDown(NativeKeyModifiers.SHIFT) == shift &&
                requiresModifierDown(NativeKeyModifiers.CTRL) == ctrl &&
                requiresModifierDown(NativeKeyModifiers.META) == meta &&
                requiresModifierDown(NativeKeyModifiers.ALT) == alt;
    }

    public boolean matchFXEvent(KeyEvent event){
        return match(event.isShiftDown(), event.isControlDown(), event.isMetaDown(), event.isAltDown());
    }

    public boolean requiresModifierDown(NativeKeyModifiers modifier){
        if (modifier.isSingleKey())
            return (specific_mask & modifier.keycode()) > 0;
        for (int mask : generic_masks)
            if ((mask & modifier.keycode()) > 0) return true;
        return false;
    }

    public static NativeKeyCombination valueOf(String str){
        String[] arr = str.split("\\+");
        ArrayList<NativeKeyModifiers> modifiers = new ArrayList<>(arr.length);
        NativeKeyCode code = null;
        for (String key : arr){
            INativeKeyCode keyCode = NativeKeyCode.valueOf(key, false);
            if (keyCode == null)
                throw new IllegalArgumentException("KeyCode : \""+key+ "\", could not be resolved");
            if (keyCode.isModifier())
                modifiers.add((NativeKeyModifiers) keyCode);
            else {
                if (code != null)
                    throw new IllegalArgumentException("Multiple keycodes are not allowed (\""+code.keyname()+
                            "\", and:\""+keyCode.keyname()+"\")");
                code = (NativeKeyCode) keyCode;
            }
        }

        return new NativeKeyCodeCombination(code, modifiers);
    }

    @Override
    public String toString() {
        return getName();
    }
}
