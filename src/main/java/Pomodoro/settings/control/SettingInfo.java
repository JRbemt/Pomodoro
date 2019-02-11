package Pomodoro.settings.control;

import Pomodoro.settings.ControlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SettingInfo {
    String key() default "";
    ControlType type();
}
