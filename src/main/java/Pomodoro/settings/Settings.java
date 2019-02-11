package Pomodoro.settings;

import Pomodoro.PomodoroApp;
import Pomodoro.Utils.FieldUtils;
import Pomodoro.keystrokelistener.NativeKeyCode;
import Pomodoro.keystrokelistener.NativeKeyCodeCombination;
import Pomodoro.keystrokelistener.NativeKeyCombination;
import Pomodoro.keystrokelistener.NativeKeyModifiers;
import Pomodoro.settings.control.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * TODO: Write a small object which handles reading json files into a class/ object
 * TODO: Make changing settings not require a restart
 * */
public class Settings implements Serializable{
    @SettingInfo(key = "option_freeze_on_hover", type = ControlType.TOGGLE)
    public boolean FREEZE_ON_HOVER = false;
    @SettingInfo(key = "option_always_on_top", type = ControlType.TOGGLE)
    public boolean ALWAYS_ON_TOP = true;
    public transient String NOTIFICATION_FILE = "let_me_love_you.mp3";
    @Range
    @SettingInfo(key = "option_volume", type = ControlType.SLIDER)
    public double VOLUME = 1.0;

    /**
     * SHORTCUTS
     * */
    private final transient NativeKeyModifiers PLATFORM_DOWN = PomodoroApp.IS_MAC ? NativeKeyModifiers.META : NativeKeyModifiers.CTRL;

    @StringRestriction(restrictor = KeyCodeRestrictor.class)
    @SettingInfo(key = "option_sh_pause", type = ControlType.STRING)
    public NativeKeyCode SH_PAUSE = (NativeKeyCode) NativeKeyCode.valueOf("SPACE", true);
    @SettingInfo(key = "option_sh_reset", type = ControlType.STRING)
    @StringRestriction(restrictor = KeyCodeCombinationRestrictor.class)
    public NativeKeyCombination SH_RESET = new NativeKeyCodeCombination("R", PLATFORM_DOWN);
    @SettingInfo(key = "option_sh_pomodoro", type = ControlType.STRING)
    @StringRestriction(restrictor = KeyCodeCombinationRestrictor.class)
    public NativeKeyCombination SH_POMODORO = new NativeKeyCodeCombination("P", PLATFORM_DOWN);
    @SettingInfo(key = "option_sh_long_break", type = ControlType.STRING)
    @StringRestriction(restrictor = KeyCodeCombinationRestrictor.class)
    public NativeKeyCombination SH_LONG_BREAK = new NativeKeyCodeCombination("L", PLATFORM_DOWN);
    @SettingInfo(key = "option_sh_short_break", type = ControlType.STRING)
    @StringRestriction(restrictor = KeyCodeCombinationRestrictor.class)
    public NativeKeyCombination SH_SHORT_BREAK = new NativeKeyCodeCombination("S", PLATFORM_DOWN);

    private transient final static String FILE_NAME = "settings.txt";

    private final static Field[] serializableFields;
    static {
        Field[] unf_fields = Settings.class.getDeclaredFields();
        ArrayList<Field> f_fields = new ArrayList<>(unf_fields.length);
        for (Field field : unf_fields){
            if (FieldUtils.validateModifiers(field, Modifier.PUBLIC)
                    && !FieldUtils.validateModifiers(field, Modifier.TRANSIENT)){
                f_fields.add(field);
            }
        }
        serializableFields = new Field[f_fields.size()];
        f_fields.toArray(serializableFields);
    }
    private final transient Gson gson;
    public Settings(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Settings.class, (InstanceCreator<Settings>) type -> Settings.this);
        builder.registerTypeAdapter(NativeKeyCombination.class, new KeyCodeTypeAdapter());
        gson = builder.create();
        restore();
    }
    private void restore(){
        File file = new File(Settings.FILE_NAME);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                gson.fromJson(reader, Settings.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else save();
    }

    public static Field[] getSerializableFields(){
        return serializableFields;
    }

    public void save(){
        System.out.println("Saving getSettings");
        try (Writer writer = new FileWriter(Settings.FILE_NAME)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}  