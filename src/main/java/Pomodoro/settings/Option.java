package Pomodoro.settings;

import java.lang.reflect.Field;

public class Option{
    public final Field field;
    public String key = "";
    public String name = "";
    public String description = "";
    public ControlType controlType;

    public Option(Field f){
        this.field = f;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Option)) {
            return false;
        } else return (this.field.equals(((Option) obj).field));
    }
}
