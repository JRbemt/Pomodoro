/* JNativeHook: Global keyboard and mouse hooking for Java.
 * Copyright (C) 2006-2016 Alexander Barker.  All Rights Received.
 * https://github.com/kwhat/jnativehook/
 *
 * JNativeHook is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JNativeHook is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Pomodoro.keystrokelistener.fxcompat;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Adapter to convert NativeKeyEvents to JavaFX KeyEvents.
 * The methods are empty so the super call is obsolete.
 *
 */
public class JavaFXKeyAdapter implements NativeKeyListener {

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        this.keyPressed(this.getJavaFXKeyEvent(nativeKeyEvent, KeyEvent.KEY_PRESSED));
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        this.keyReleased(this.getJavaFXKeyEvent(nativeKeyEvent, KeyEvent.KEY_RELEASED));
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        this.keyTyped(this.getJavaFXKeyEvent(nativeKeyEvent, KeyEvent.KEY_TYPED));
    }


    public void keyPressed(KeyEvent keyEvent){
        // Do Nothing
    }

    public void keyReleased(KeyEvent keyEvent){
        // Do Nothing
    }

    public void keyTyped(KeyEvent keyEvent){
        // Do Nothing
    }


    protected KeyEvent getJavaFXKeyEvent(NativeKeyEvent nativeKeyEvent, EventType<KeyEvent> eventType){

        KeyCode keyCode = convertToFXKeyCode(nativeKeyEvent.getKeyCode());

        String unicodeCharacter = KeyEvent.CHAR_UNDEFINED;
        if (eventType.equals(KeyEvent.KEY_TYPED)){
            unicodeCharacter = String.valueOf(nativeKeyEvent.getKeyChar());
        }

        return new KeyEvent(eventType,
                            unicodeCharacter,
                            "",
                            keyCode,
                            false,
                            false,
                            false,
                            false);

    }

    public static KeyCode convertToFXKeyCode(int keycode){
        KeyCode code = KeyCode.UNDEFINED;
        switch (keycode) {
            case NativeKeyEvent.VC_ESCAPE:
                code = KeyCode.ESCAPE;
                break;

            // Begin Function Keys
            case NativeKeyEvent.VC_F1:
                code = KeyCode.F1;
                break;

            case NativeKeyEvent.VC_F2:
                code = KeyCode.F2;
                break;

            case NativeKeyEvent.VC_F3:
                code = KeyCode.F3;
                break;

            case NativeKeyEvent.VC_F4:
                code = KeyCode.F4;
                break;

            case NativeKeyEvent.VC_F5:
                code = KeyCode.F5;
                break;

            case NativeKeyEvent.VC_F6:
                code = KeyCode.F6;
                break;

            case NativeKeyEvent.VC_F7:
                code = KeyCode.F7;
                break;

            case NativeKeyEvent.VC_F8:
                code = KeyCode.F8;
                break;

            case NativeKeyEvent.VC_F9:
                code = KeyCode.F9;
                break;

            case NativeKeyEvent.VC_F10:
                code = KeyCode.F10;
                break;

            case NativeKeyEvent.VC_F11:
                code = KeyCode.F11;
                break;

            case NativeKeyEvent.VC_F12:
                code = KeyCode.F12;
                break;

            case NativeKeyEvent.VC_F13:
                code = KeyCode.F13;
                break;

            case NativeKeyEvent.VC_F14:
                code = KeyCode.F14;
                break;

            case NativeKeyEvent.VC_F15:
                code = KeyCode.F15;
                break;

            case NativeKeyEvent.VC_F16:
                code = KeyCode.F16;
                break;

            case NativeKeyEvent.VC_F17:
                code = KeyCode.F17;
                break;

            case NativeKeyEvent.VC_F18:
                code = KeyCode.F18;
                break;

            case NativeKeyEvent.VC_F19:
                code = KeyCode.F19;
                break;
            case NativeKeyEvent.VC_F20:
                code = KeyCode.F20;
                break;

            case NativeKeyEvent.VC_F21:
                code = KeyCode.F21;
                break;

            case NativeKeyEvent.VC_F22:
                code = KeyCode.F22;
                break;

            case NativeKeyEvent.VC_F23:
                code = KeyCode.F23;
                break;

            case NativeKeyEvent.VC_F24:
                code = KeyCode.F24;
                break;
            // End Function Keys


            // Begin Alphanumeric Zone
            case NativeKeyEvent.VC_BACKQUOTE:
                code = KeyCode.BACK_QUOTE;
                break;

            case NativeKeyEvent.VC_1:
                code = KeyCode.DIGIT1;
                break;

            case NativeKeyEvent.VC_2:
                code = KeyCode.DIGIT2;
                break;

            case NativeKeyEvent.VC_3:
                code = KeyCode.DIGIT3;
                break;

            case NativeKeyEvent.VC_4:
                code = KeyCode.DIGIT4;
                break;

            case NativeKeyEvent.VC_5:
                code = KeyCode.DIGIT5;
                break;

            case NativeKeyEvent.VC_6:
                code = KeyCode.DIGIT6;
                break;

            case NativeKeyEvent.VC_7:
                code = KeyCode.DIGIT7;
                break;

            case NativeKeyEvent.VC_8:
                code = KeyCode.DIGIT8;
                break;

            case NativeKeyEvent.VC_9:
                code = KeyCode.DIGIT9;
                break;

            case NativeKeyEvent.VC_0:
                code = KeyCode.DIGIT0;
                break;


            case NativeKeyEvent.VC_MINUS:
                code = KeyCode.MINUS;
                break;

            case NativeKeyEvent.VC_EQUALS:
                code = KeyCode.EQUALS;
                break;

            case NativeKeyEvent.VC_BACKSPACE:
                code = KeyCode.BACK_SPACE;
                break;


            case NativeKeyEvent.VC_TAB:
                code = KeyCode.TAB;
                break;

            case NativeKeyEvent.VC_CAPS_LOCK:
                code = KeyCode.CAPS;
                break;


            case NativeKeyEvent.VC_A:
                code = KeyCode.A;
                break;

            case NativeKeyEvent.VC_B:
                code = KeyCode.B;
                break;

            case NativeKeyEvent.VC_C:
                code = KeyCode.C;
                break;

            case NativeKeyEvent.VC_D:
                code = KeyCode.D;
                break;

            case NativeKeyEvent.VC_E:
                code = KeyCode.E;
                break;

            case NativeKeyEvent.VC_F:
                code = KeyCode.F;
                break;

            case NativeKeyEvent.VC_G:
                code = KeyCode.G;
                break;

            case NativeKeyEvent.VC_H:
                code = KeyCode.H;
                break;

            case NativeKeyEvent.VC_I:
                code = KeyCode.I;
                break;

            case NativeKeyEvent.VC_J:
                code = KeyCode.J;
                break;

            case NativeKeyEvent.VC_K:
                code = KeyCode.K;
                break;

            case NativeKeyEvent.VC_L:
                code = KeyCode.L;
                break;

            case NativeKeyEvent.VC_M:
                code = KeyCode.M;
                break;

            case NativeKeyEvent.VC_N:
                code = KeyCode.N;
                break;

            case NativeKeyEvent.VC_O:
                code = KeyCode.O;
                break;

            case NativeKeyEvent.VC_P:
                code = KeyCode.P;
                break;

            case NativeKeyEvent.VC_Q:
                code = KeyCode.Q;
                break;

            case NativeKeyEvent.VC_R:
                code = KeyCode.R;
                break;

            case NativeKeyEvent.VC_S:
                code = KeyCode.S;
                break;

            case NativeKeyEvent.VC_T:
                code = KeyCode.T;
                break;

            case NativeKeyEvent.VC_U:
                code = KeyCode.U;
                break;

            case NativeKeyEvent.VC_V:
                code = KeyCode.V;
                break;

            case NativeKeyEvent.VC_W:
                code = KeyCode.W;
                break;

            case NativeKeyEvent.VC_X:
                code = KeyCode.X;
                break;

            case NativeKeyEvent.VC_Y:
                code = KeyCode.Y;
                break;

            case NativeKeyEvent.VC_Z:
                code = KeyCode.Z;
                break;


            case NativeKeyEvent.VC_OPEN_BRACKET:
                code = KeyCode.OPEN_BRACKET;
                break;

            case NativeKeyEvent.VC_CLOSE_BRACKET:
                code = KeyCode.CLOSE_BRACKET;
                break;

            case NativeKeyEvent.VC_BACK_SLASH:
                code = KeyCode.BACK_SLASH;
                break;


            case NativeKeyEvent.VC_SEMICOLON:
                code = KeyCode.SEMICOLON;
                break;

            case NativeKeyEvent.VC_QUOTE:
                code = KeyCode.QUOTE;
                break;

            case NativeKeyEvent.VC_ENTER:
                code = KeyCode.ENTER;
                break;


            case NativeKeyEvent.VC_COMMA:
                code = KeyCode.COMMA;
                break;

            case NativeKeyEvent.VC_PERIOD:
                code = KeyCode.PERIOD;
                break;

            case NativeKeyEvent.VC_SLASH:
                code = KeyCode.SLASH;
                break;

            case NativeKeyEvent.VC_SPACE:
                code = KeyCode.SPACE;
                break;
            // End Alphanumeric Zone


            case NativeKeyEvent.VC_PRINTSCREEN:
                code = KeyCode.PRINTSCREEN;
                break;

            case NativeKeyEvent.VC_SCROLL_LOCK:
                code = KeyCode.SCROLL_LOCK;
                break;

            case NativeKeyEvent.VC_PAUSE:
                code = KeyCode.PAUSE;
                break;


            // Begin Edit Key Zone
            case NativeKeyEvent.VC_INSERT:
                code = KeyCode.INSERT;
                break;

            case NativeKeyEvent.VC_DELETE:
                code = KeyCode.DELETE;
                break;

            case NativeKeyEvent.VC_HOME:
                code = KeyCode.HOME;
                break;

            case NativeKeyEvent.VC_END:
                code = KeyCode.END;
                break;

            case NativeKeyEvent.VC_PAGE_UP:
                code = KeyCode.PAGE_UP;
                break;

            case NativeKeyEvent.VC_PAGE_DOWN:
                code = KeyCode.PAGE_DOWN;
                break;
            // End Edit Key Zone


            // Begin Cursor Key Zone
            case NativeKeyEvent.VC_UP:
                code = KeyCode.UP;
                break;
            case NativeKeyEvent.VC_LEFT:
                code = KeyCode.LEFT;
                break;
            case NativeKeyEvent.VC_CLEAR:
                code = KeyCode.CLEAR;
                break;
            case NativeKeyEvent.VC_RIGHT:
                code = KeyCode.RIGHT;
                break;
            case NativeKeyEvent.VC_DOWN:
                code = KeyCode.DOWN;
                break;
            // End Cursor Key Zone


            // Begin Numeric Zone
            case NativeKeyEvent.VC_NUM_LOCK:
                code = KeyCode.NUM_LOCK;
                break;

            case NativeKeyEvent.VC_SEPARATOR:
                code = KeyCode.SEPARATOR;
                break;
            // End Numeric Zone


            // Begin Modifier and Control Keys
            case NativeKeyEvent.VC_SHIFT:
                code = KeyCode.SHIFT;
                break;

            case NativeKeyEvent.VC_CONTROL:
                code = KeyCode.CONTROL;
                break;

            case NativeKeyEvent.VC_ALT:
                code = KeyCode.ALT;
                break;

            case NativeKeyEvent.VC_META:
                code = KeyCode.META;
                break;

            case NativeKeyEvent.VC_CONTEXT_MENU:
                code = KeyCode.CONTEXT_MENU;
                break;
            // End Modifier and Control Keys


			/* Begin Media Control Keys
			case NativeKeyEvent.VC_POWER:
			case NativeKeyEvent.VC_SLEEP:
			case NativeKeyEvent.VC_WAKE:
			case NativeKeyEvent.VC_MEDIA_PLAY:
			case NativeKeyEvent.VC_MEDIA_STOP:
			case NativeKeyEvent.VC_MEDIA_PREVIOUS:
			case NativeKeyEvent.VC_MEDIA_NEXT:
			case NativeKeyEvent.VC_MEDIA_SELECT:
			case NativeKeyEvent.VC_MEDIA_EJECT:
			case NativeKeyEvent.VC_VOLUME_MUTE:
			case NativeKeyEvent.VC_VOLUME_UP:
			case NativeKeyEvent.VC_VOLUME_DOWN:
			case NativeKeyEvent.VC_APP_MAIL:
			case NativeKeyEvent.VC_APP_CALCULATOR:
			case NativeKeyEvent.VC_APP_MUSIC:
			case NativeKeyEvent.VC_APP_PICTURES:
			case NativeKeyEvent.VC_BROWSER_SEARCH:
			case NativeKeyEvent.VC_BROWSER_HOME:
			case NativeKeyEvent.VC_BROWSER_BACK:
			case NativeKeyEvent.VC_BROWSER_FORWARD:
			case NativeKeyEvent.VC_BROWSER_STOP:
			case NativeKeyEvent.VC_BROWSER_REFRESH:
			case NativeKeyEvent.VC_BROWSER_FAVORITES:
			// End Media Control Keys */


            // Begin Japanese Language Keys
            case NativeKeyEvent.VC_KATAKANA:
                code = KeyCode.KATAKANA;
                break;

            case NativeKeyEvent.VC_UNDERSCORE:
                code = KeyCode.UNDERSCORE;
                break;

            //case VC_FURIGANA:

            case NativeKeyEvent.VC_KANJI:
                code = KeyCode.KANJI;
                break;

            case NativeKeyEvent.VC_HIRAGANA:
                code = KeyCode.HIRAGANA;
                break;

            //case VC_YEN:
            // End Japanese Language Keys


            // Begin Sun keyboards
            case NativeKeyEvent.VC_SUN_HELP:
                code = KeyCode.HELP;
                break;

            case NativeKeyEvent.VC_SUN_STOP:
                code = KeyCode.STOP;
                break;

            //case VC_SUN_FRONT:

            //case VC_SUN_OPEN:

            case NativeKeyEvent.VC_SUN_PROPS:
                code = KeyCode.PROPS;
                break;

            case NativeKeyEvent.VC_SUN_FIND:
                code = KeyCode.FIND;
                break;

            case NativeKeyEvent.VC_SUN_AGAIN:
                code = KeyCode.AGAIN;
                break;

            //case NativeKeyEvent.VC_SUN_INSERT:

            case NativeKeyEvent.VC_SUN_COPY:
                code = KeyCode.COPY;
                break;

            case NativeKeyEvent.VC_SUN_CUT:
                code = KeyCode.CUT;
                break;
            // End Sun keyboards
        }
        return code;
    }

}
