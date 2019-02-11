package Pomodoro;

import Pomodoro.controllers.PomodoroStack;
import Pomodoro.keystrokelistener.NativeKeyCode;
import Pomodoro.keystrokelistener.NativeKeyCodeCombination;
import Pomodoro.keystrokelistener.NativeKeyCombination;
import Pomodoro.settings.OptionFactory;
import Pomodoro.settings.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PomodoroApp extends Application implements NativeKeyListener{
    public static final String OS_NAME = System.getProperty("pomodoro.os.name", System.getProperty("os.name"));
    public static final String OS_ARCH = System.getProperty("pomodoro.os.arch", System.getProperty("os.arch"));
    public static final String O_VERSION = System.getProperty("os.O_VERSION");

    public static final boolean IS_IPHONE = false;
    public static final boolean IS_IOS = "iOS".equals(OS_NAME) || "iOS Simulator".equals(OS_NAME);
    public static final boolean IS_ANDROID = "android".equals(System.getProperty("javafx.platform")) || "Dalvik".equals(System.getProperty("java.vm.name"));
    public static final boolean IS_EMBEDDED = "arm".equals(OS_ARCH) && !IS_IOS && !IS_ANDROID;
    public static final boolean IS_DESKTOP = !IS_EMBEDDED && !IS_IOS && !IS_ANDROID;
    public static final boolean IS_MAC = OS_NAME.startsWith("Mac");
    public static final boolean IS_JAR = PomodoroApp.class.getResource("PomodoroApp.class").toString().startsWith("jar");
    public static final boolean IS_LINUX = OS_NAME.startsWith("Linux") && !IS_ANDROID;
    public static final boolean IS_SOLARIS = OS_NAME.startsWith("SunOS");
    public static final boolean IS_WINDOWS = OS_NAME.startsWith("Windows");
    public static final boolean IS_WINDOWS_VISTA_OR_LATER = IS_WINDOWS && versionNumberGreaterThanOrEqualTo(6.0f);
    public static final boolean IS_WINDOWS_7_OR_LATER = IS_WINDOWS && versionNumberGreaterThanOrEqualTo(6.1f);

    /**
     * Utility method used to determine whether the O_VERSION number as
     * reported by system properties is greater than or equal to a given
     * value.
     *
     * @param value The value to test against.
     * @return false if the O_VERSION number cannot be parsed as a float,
     *         otherwise the comparison against value.
     */
    public static boolean versionNumberGreaterThanOrEqualTo(float value) {
        try {
            return Float.parseFloat(O_VERSION) >= value;
        } catch (Exception e) {
            return false;
        }
    }

    private final Settings settings = new Settings();
    private ResourceBundle bundle;

    @Override
    public void init() throws Exception {
        super.init();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        bundle = ResourceBundle.getBundle("Pomodoro.bundles.strings", new Locale("en", "US"));
        OptionFactory.createOptions(bundle);

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this);
    }

    private PomodoroStack stack = null;
    private Stage primaryStage = null;
    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        primaryStage.setAlwaysOnTop(settings.ALWAYS_ON_TOP);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Font.loadFont(PomodoroApp.class.getResource("fa-solid-900.ttf").
                toExternalForm(), 12);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout/main.fxml"));
        loader.setResources(bundle);
        Parent root = loader.load();
        this.stack = loader.getController();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll("/Pomodoro/stylesheets/main.css", "/Pomodoro/stylesheets/overlay.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static PomodoroApp instance;
    public static PomodoroApp instance(){
        if (instance == null)
            throw new RuntimeException("Not yet initialized");
        return instance;
    }

    public Settings getSettings(){
        return this.settings;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public String getString(String key, String defaultValue){
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex){}
        return defaultValue;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stopped correctly");
    }

    private void shutdown(){
        settings.save();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        System.exit(0);
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        if (stack == null)
            return;
        PomodoroStack.PomodoroEvent event = null;
        if(stack.isFinished() && settings.SH_PAUSE.match(nativeEvent)){
            event = PomodoroStack.PomodoroEvent.QUIT_ALARM;
        } else if (primaryStage.isFocused()){
            if (settings.SH_POMODORO.match(nativeEvent)) {
                System.out.println(primaryStage.isFocused());
                event = PomodoroStack.PomodoroEvent.WORK_DEFAULT;
            } else if(settings.SH_PAUSE.match(nativeEvent)){
                event = PomodoroStack.PomodoroEvent.PAUSE;
            } if (settings.SH_RESET.match(nativeEvent)) {
                event = PomodoroStack.PomodoroEvent.RESET;
            } else if (settings.SH_LONG_BREAK.match(nativeEvent)) {
                event = PomodoroStack.PomodoroEvent.BREAK_LONG;
            } else if (settings.SH_SHORT_BREAK.match(nativeEvent)) {
                event = PomodoroStack.PomodoroEvent.BREAK_SHORT;
            }
        }
        if (event != null){
            final PomodoroStack.PomodoroEvent finalEvent = event;
            Platform.runLater(() -> stack.processEvent(finalEvent));
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {

    }
}
