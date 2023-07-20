package io.vorbind.chat.adapters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    public static String convertListToJson(List<String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.toJson(list, type);
    }

    public static List<String> convertJsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
