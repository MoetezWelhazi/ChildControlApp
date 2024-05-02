package com.IDL2.childcontrolapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LocalStorage {

    private static final String PREF_NAME = "LocalPrefs";
    private static final String KEY_APP_NAMES = "appNames";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_MAP = "map";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveAppNames(List<String> appNames) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_APP_NAMES, new HashSet<>(appNames));
        editor.apply();
    }

    public List<String> getAppNames() {
        return new ArrayList<>(sharedPreferences.getStringSet(KEY_APP_NAMES, new HashSet<>()));
    }

    public void saveUUID(String uuid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UUID, uuid);
        editor.apply();
    }

    public String getUUID() {
        return sharedPreferences.getString(KEY_UUID, null);
    }

    public void saveMap(Map<String, Long> map) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String mapJson = gson.toJson(map);
        editor.putString(KEY_MAP, mapJson);
        editor.apply();
    }

    public Map<String, Long> getMap() {
        String mapJson = sharedPreferences.getString(KEY_MAP, null);
        if (mapJson != null) {
            Type type = new TypeToken<Map<String, Long>>(){}.getType();
            return gson.fromJson(mapJson, type);
        }
        return new HashMap<>();
    }
}
