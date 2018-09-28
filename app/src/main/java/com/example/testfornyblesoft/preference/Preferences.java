package com.example.testfornyblesoft.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testfornyblesoft.adapter.SavedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Preferences {
    private static Preferences preferences;
    private SharedPreferences sharedPreferences;

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("savedData", Context.MODE_PRIVATE);
    }

    public static Preferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = new Preferences(context);
        }
        return preferences;
    }

    public void saveFile(SavedData savedData) {
        List<SavedData> listDate = loadFile();
        if (listDate == null) {
            listDate = new ArrayList<>();
        }
        listDate.add(savedData);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listDate);
        prefsEditor.putString("savedList", json);
        prefsEditor.apply();
    }

    public List<SavedData> loadFile() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("savedList", "");
        Type type = new TypeToken<ArrayList<SavedData>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
