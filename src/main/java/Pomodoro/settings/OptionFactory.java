package Pomodoro.settings;

import Pomodoro.PomodoroApp;
import Pomodoro.settings.control.Range;
import Pomodoro.settings.control.SettingInfo;
import Pomodoro.settings.control.StringRestriction;
import Pomodoro.settings.control.StringRestrictor;
import Pomodoro.widgets.MySlider;
import Pomodoro.widgets.RestrictiveTextField;
import Pomodoro.widgets.ToggleSwitch;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO Find a better alternative
 * */
public class OptionFactory {
    public static final String TEMPLATE = "layout/option.fxml";
    public static ArrayList<Option> options = new ArrayList<>();

    private OptionFactory(){
        throw new RuntimeException("Can't be instantiated");
    }

    public static void createOptions(ResourceBundle bundle){
        if (options.size() > 0){
            throw new RuntimeException("Already initialized");
        }

        for (Field f : Settings.getSerializableFields()) {
            Option option = new Option(f);
            if (f.isAnnotationPresent(SettingInfo.class)) {
                SettingInfo info = f.getAnnotation(SettingInfo.class);
                option.key = info.key();
                option.controlType = info.type();
                options.add(option);
            }
        }
        parseBundle(bundle);
    }

    public static void parseBundle(ResourceBundle bundle){
        for (Option o : options){
            try {
                String[] name_description = bundle.getString(o.key).split("; ");
                if (name_description.length >= 1) {
                    o.name = name_description[0];
                    if (name_description.length >= 2) {
                        o.description = name_description[1];
                    } else System.out.println("Option key \""+o.key+"\" has no description");
                }
            } catch (MissingResourceException ex){
                System.out.println("Option key \""+o.key+"\"  not found");
            }
        }
    }

    public static ArrayList<Option> getOptions() {
        return options;
    }

    public static Pane inflateOption(final Option option){
        VBox root = null;
        try {
            root = FXMLLoader.load(PomodoroApp.class.getResource(TEMPLATE));
            Label label = (Label) root.lookup("#option_label");
            label.setText(option.name);
            StackPane option_control = ((StackPane) root.lookup("#option_pane"));
            Node node = createOptionController(option);
            if (node != null) {
                option_control.getChildren().add(node);
                StackPane.setAlignment(node, Pos.CENTER_RIGHT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private static Node createOptionController(final Option option){
        if (option.controlType == null)
            return null;
        switch (option.controlType) {
           case TOGGLE:
               final ToggleSwitch toggleSwitch = new ToggleSwitch(50);
               try {
                   toggleSwitch.switchedOnProperty().setValue(option.field.getBoolean(PomodoroApp.instance().getSettings()));
               } catch (IllegalAccessException ex) {
                   ex.printStackTrace();
               }
               toggleSwitch.switchedOnProperty().addListener((observable, oldValue, newValue) -> {
                   if (toggleSwitch.layout_ready) {
                       setValue(option, oldValue, newValue);
                   }
               });
               Pane pane = new Pane();
               pane.setPrefWidth(50);
               pane.setMaxWidth(50);
               pane.setMinWidth(50);
               pane.getStyleClass().add("toggle_group");
               pane.getChildren().add(toggleSwitch);
               return pane;
            case SLIDER:
                MySlider slider = new MySlider();
                Range range = option.field.getAnnotation(Range.class);
                try {
                    slider.setValue(option.field.getDouble(PomodoroApp.instance().getSettings()));
                } catch (IllegalAccessException ex){
                    ex.printStackTrace();
                }
                slider.valueProperty().addListener((observable, oldValue, newValue) -> setValue(option, oldValue, newValue));
                slider.getStyleClass().add("slider");
                slider.setRange(range.min(), range.max(), range.stepsize());
                return slider;
            case COMBOBOX:
                Object[] values;
                if (option.field.getType().isEnum())
                    values = option.field.getType().getEnumConstants();
                else {
                    throw new RuntimeException("Unable to process option:\""+option.key +
                            "\" of type:\""+option.field.getType()+"\" as "+ControlType.COMBOBOX.name());
                }

                ComboBox<Object> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
                try {
                    comboBox.setValue(option.field.get(PomodoroApp.instance().getSettings()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                comboBox.setPrefWidth(200);
                comboBox.setPrefHeight(25);
                comboBox.getStyleClass().add("combobox");
                comboBox.valueProperty().addListener((observable, oldValue, newValue) -> setValue(option, oldValue, newValue));
                return comboBox;
            case STRING:
                RestrictiveTextField field;
                if (option.field.isAnnotationPresent(StringRestriction.class)){
                    Class<? extends StringRestrictor> restriction = option.field.getAnnotation(StringRestriction.class).restrictor();
                    StringRestrictor restrictor;
                    try {
                        restrictor = StringRestrictor.instantiate(restriction, option.field.getType());
                        field = RestrictiveTextField.withRestriction(restrictor);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("Incompatible String restriction :"+restriction.getName());
                    }
                } else field = RestrictiveTextField.withoutRestriction();
                field.setPrefWidth(160);
                field.setPrefHeight(25);
                try {
                    String value = field.getRestrictor().toString(option.field.get(PomodoroApp.instance().getSettings()));
                    field.textProperty().setValue(value);
                } catch (IllegalAccessException ex){
                    ex.printStackTrace();
                }
                field.parsedText().addListener((observable, oldValue, newValue) -> setValue(option, oldValue, newValue));
                field.getStyleClass().add("text_input");
                return field;
        }
        return null;
    }

    private static void setValue(Option o,Object oldvalu, Object newvalue){
        try {
            o.field.set(PomodoroApp.instance().getSettings(), newvalue);
        } catch (IllegalAccessException ex) {ex.printStackTrace();}
    }
}
