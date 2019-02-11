package Pomodoro.settings.control;

import Pomodoro.Utils.FieldUtils;

public class NumberRestrictor implements StringRestrictor<Number> {
    private final Class<? extends Number> fieldType;
    private final int type;
    public NumberRestrictor(Class<? extends Number> fieldType){
        this.type = FieldUtils.getType(fieldType);
        this.fieldType = fieldType;
    }
    @Override
    public Class<? extends Number> getType() {
        return fieldType;
    }

    @Override
    public Number parse(String str) throws IllegalArgumentException {
        if (str.equals(""))
            str = "0";

        switch (type) {
            case FieldUtils.DOUBLE:
                return Double.parseDouble(str);
            case FieldUtils.INT:
                return Integer.parseInt(str);
            case FieldUtils.SHORT:
                return Short.parseShort(str);
            case FieldUtils.FLOAT:
                return Float.parseFloat(str);
        }
        return null;
    }
}
