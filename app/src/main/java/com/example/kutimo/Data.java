package com.example.kutimo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class Data {
    Activity activity;
    SharedPreferences sharedPreferences;
    protected String LIST_NAME = "list";

    Data(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com.storage", Context.MODE_PRIVATE);
    }

    Data(Activity activity, String page) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com." + page, Context.MODE_PRIVATE);
    }

    public void saveInt(String shared_preference, int number) {
        sharedPreferences.edit().putInt(shared_preference, number).apply();
    }

    public void saveString(String shared_preference, String variable) {
        sharedPreferences.edit().putString(shared_preference, variable).apply();
    }

    public void saveJSON(String shared_preference, JSONObject json_value) {
        saveString(shared_preference, json_value.toJSONString());
    }

    public void appendItemToJSON(String shared_preference, JSONObject json_value) {
        try {
            JSONObject scripture_json = loadJSON(shared_preference);

            JSONArray json_array = loadListItemsFromJSON(shared_preference);
            json_array.add(json_value);
            scripture_json.put(LIST_NAME, json_array);

            saveJSON(shared_preference, scripture_json);
        } catch (Exception ignored) {
        }
    }

    public JSONArray loadListItemsFromJSON(String shared_preference) throws ParseException{
        JSONArray json_array = (JSONArray) loadJSON(shared_preference).get(LIST_NAME);
        return json_array == null ? new JSONArray() : json_array;
    }

    public int loadInt(String shared_preference) {
        return sharedPreferences.getInt(shared_preference, 0);
    }

    public int loadInt(String shared_preference, int default_value) {
        return sharedPreferences.getInt(shared_preference, default_value);
    }


    public String loadString(String shared_preference) {
        return sharedPreferences.getString(shared_preference, "");
    }

    public String loadString(String shared_preference, String default_value) {
        return sharedPreferences.getString(shared_preference, default_value);
    }

    public JSONObject loadJSON(String shared_preference) throws ParseException {
        return (JSONObject) new JSONParser().parse(loadString(shared_preference));
    }
}