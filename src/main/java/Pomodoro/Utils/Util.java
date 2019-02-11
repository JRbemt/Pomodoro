package Pomodoro.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static int[] toList(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    public static <T> T[] toList(Class<T> clazz, List<T> items){
        T[] arr = (T[])Array.newInstance(clazz, items.size());
        for (int i=0; i < items.size(); i++) {
            arr[i] = items.get(i);
        }
        return arr;
    }

    /**
     * Validate if all modifiers matchFXEvent
     *
     * @param method method
     * @param flags  modifier flags
     * @return true if they matchFXEvent
     * @see java.lang.reflect.Modifier
     */
    static public boolean validateModifiers(Method method, int flags) {
        return (method.getModifiers() & flags) == flags;
    }
}
