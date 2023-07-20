package io.vorbind.chat.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vorbind.chat.adapters.UserItem;

public class SharedPreferencesHelper {
    private static final String PREFS_NAME = "io.vorbind.chat.usersData";
    private static final String KEY_DICTIONARY = "UsersPhoneNumbers";
    private static final String KEY_USER = "UserKeyList";
    private final SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveDictionary(Context context, Map<String, String> dictionary) {
        // Convert the dictionary to a JSON string using Gson
        Gson gson = new Gson();
        String jsonString = gson.toJson(dictionary);

        // Save the JSON string in SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DICTIONARY, jsonString);
        editor.apply();
    }

    public static Map<String, String> getDictionary(Context context) {
        // Retrieve the JSON string from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonString = preferences.getString(KEY_DICTIONARY, "");

        // Convert the JSON string back to a dictionary using Gson
        Gson gson = new Gson();
        Map<String, String> dictionary = new HashMap<>();
        if (!jsonString.isEmpty()) {
            dictionary = gson.fromJson(jsonString, HashMap.class);
        }

        return dictionary;
    }

    public void addUser(UserItem user) {
        List<UserItem> userList = getUsers();
        if (userList == null) {
            userList = new ArrayList<>();
        }
        userList.add(user);
        saveUsers(userList);
    }

    public List<UserItem> getUsers() {
        String userJsonList = sharedPreferences.getString(KEY_USER, null);
        if (userJsonList != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserItem>>() {
            }.getType();
            return gson.fromJson(userJsonList, type);
        } else {
            return null;
        }
    }

    private void saveUsers(List<UserItem> userList) {
        Gson gson = new Gson();
        String userJsonList = gson.toJson(userList);
        sharedPreferences.edit().putString(KEY_USER, userJsonList).apply();
    }
}
