package Pomodoro.settings.control;

import java.lang.reflect.InvocationTargetException;

public interface StringRestrictor<T> {
    static <T> StringRestrictor instantiate(Class<? extends StringRestrictor> restrictor, Class<?> subtype) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return restrictor.getConstructor(Class.class).newInstance(subtype);
    }
    Class<? extends T> getType();
    /**
     * @return object from string, null when string is not parseable to type or
     * @throws IllegalArgumentException when string is not parseable to type
     * */
    T parse(String str) throws IllegalArgumentException;
    default String toString(T object){
        return object.toString();
    }
}
