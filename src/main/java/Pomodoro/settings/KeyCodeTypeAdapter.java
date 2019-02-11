package Pomodoro.settings;

import Pomodoro.keystrokelistener.NativeKeyCombination;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class KeyCodeTypeAdapter extends TypeAdapter<NativeKeyCombination> {
    @Override
    public void write(JsonWriter out, NativeKeyCombination value) throws IOException {
        if (value == null){
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    @Override
    public NativeKeyCombination read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL){
            in.nextNull();
            return null;
        }
        if (in.peek() == JsonToken.STRING){
            return NativeKeyCombination.valueOf(in.nextString());
        }
        return null;
    }
}
