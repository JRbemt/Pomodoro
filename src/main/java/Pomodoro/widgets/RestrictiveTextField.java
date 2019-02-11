package Pomodoro.widgets;

import Pomodoro.settings.control.StringRestrictor;
import javafx.beans.property.*;
import javafx.scene.control.TextField;

/**
 * TODO using regex would probably be way better
 * */
public class RestrictiveTextField<T> extends TextField {
    public final StringRestrictor<T> restrictor;

    private RestrictiveTextField(StringRestrictor<T> restrictor){
        this.restrictor = restrictor;
        textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                T result = this.restrictor.parse(newValue);
                if (result != null) {
                    restrictedText.set(newValue);
                    parsedText.set(result);
                    setValid(true);
                } else {
                    setValid(false);
                }
            } catch (IllegalArgumentException ex){
                setValid(false);
            }
        });
    }

    private void setValid(boolean valid){
        if (valid){
            getStyleClass().remove("wrong_input");
        } else {
            if (!getStyleClass().contains("wrong_input")){
                getStyleClass().add("wrong_input");
            }
        }
    }

    public static <T> RestrictiveTextField<T> withRestriction(StringRestrictor<T> restriction){
        return new RestrictiveTextField<>(restriction);
    }

    public static RestrictiveTextField<String> withoutRestriction(){
        return new RestrictiveTextField<>(new MockRestrictor());
    }

    public StringRestrictor<T> getRestrictor() {
        return restrictor;
    }

    private final StringPropertyBase restrictedText = new StringPropertyBase() {
        @Override
        public Object getBean() {
            return RestrictiveTextField.this;
        }

        @Override
        public String getName() {
            return "restrictedText";
        }
    };

    public final ReadOnlyStringProperty restrictedTextProperty(){ return restrictedText;}
    private final ObjectProperty<T> parsedText = new ObjectPropertyBase<T>() {
        @Override
        public Object getBean() {
            return RestrictiveTextField.this;
        }

        @Override
        public String getName() {
            return "parsedString";
        }
    };
    public final ReadOnlyObjectProperty<T> parsedText(){ return parsedText;}

    private static class MockRestrictor implements StringRestrictor<String>{
        @Override
        public Class<? extends String> getType() {
            return String.class;
        }

        @Override
        public String parse(String str) throws IllegalArgumentException {
            return str;
        }
    }
}
