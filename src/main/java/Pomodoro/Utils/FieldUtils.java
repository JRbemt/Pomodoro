package Pomodoro.Utils;

import java.lang.reflect.Field;

public class FieldUtils {
    public final static int INT = 0x1<<1;
    public final static int BOOLEAN = 0x1<<2;
    public final static int FLOAT = 0x1<<3;
    public final static int DOUBLE = 0x1<<4;
    public final static int SHORT = 0x1<<5;
    public final static int STRING = 0x1<<6;
    public final static int ENUM = 0x1<<7;

    /**
     * Validate if all modifiers match
     *
     * @param field field
     * @param flags  modifier flags
     * @return true if they match
     * @see java.lang.reflect.Modifier
     */
    public static boolean validateModifiers(Field field, int flags) {
        return (field.getModifiers() & flags) == flags;
    }

    public static int getType(Class<?> type){
        if (type.isAssignableFrom(String.class)){
            return STRING;
        } else if (type.isEnum()){
            return ENUM;
        } else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
            return BOOLEAN;
        } else if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
            return INT;
        } else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)){
            return DOUBLE;
        } else if (type.isAssignableFrom(short.class) || type.isAssignableFrom(Short.class)) {
            return SHORT;
        } else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
            return FLOAT;
        }
        return -1;
    }
}
