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

    /**
     * @param activity Pass in "this" from activity object.
     */
    Data(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com.storage", Context.MODE_PRIVATE);
    }

    /**
     * Enables multi-level storage as needed. (very easy to implement).
     *
     * @param activity Pass in "this" from activity object.
     * @param page     A page to save variables to.
     */
    Data(Activity activity, String page) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com." + page, Context.MODE_PRIVATE);
    }

    /**
     * @param shared_preference Key name for number variable
     * @param number            integer number to be saved under shared_preference
     */
    public void saveInt(String shared_preference, int number) {
        sharedPreferences.edit().putInt(shared_preference, number).apply();
    }

    /**
     * @param shared_preference Key name for String variable
     * @param variable          string variable to be saved under shared_preference
     */
    public void saveString(String shared_preference, String variable) {
        sharedPreferences.edit().putString(shared_preference, variable).apply();
    }

    /**
     * Converts JSONObject into JSON String to be saved as string under shared_preference key
     *
     * @param shared_preference Key name for JSONObject json_value
     * @param json_value        gets converted to string before saving under shared_preference
     */
    public void saveJSON(String shared_preference, JSONObject json_value) {
        saveString(shared_preference, json_value.toJSONString());
    }

    /**
     * Appends json_value to a JSONArray and gets saved under shared_preference
     * <p>
     * load JSONObject with shared_preference key then uses Data.LIST_ITEM as a JSONArray key to
     * append JSONObject json_value then save it.
     *
     * @param shared_preference Key name for JSONObject json_value
     * @param json_value        gets appended to JSONArray and JSONArray converts to String before saving under shared_preference
     */
    public void appendItemToJSON(String shared_preference, JSONObject json_value) {
        try {
            // load
            JSONObject main_json = loadJSON(shared_preference);

            JSONArray json_array = loadListItemsFromJSON(shared_preference);
            json_array.add(json_value);
            main_json.put(LIST_NAME, json_array);

            saveJSON(shared_preference, main_json);
        } catch (Exception ignored) {
        }
    }

    /**
     * @param shared_preference key for Data.LIST_NAME to be loaded as json_array
     * @return json_array loaded under Data.LIST_NAME found in shared_preference key returns empty JSONArray if empty/non-existance
     * @throws ParseException error when there's syntax error in JSON string.
     */
    public JSONArray loadListItemsFromJSON(String shared_preference) throws ParseException {
        JSONArray json_array = (JSONArray) loadJSON(shared_preference).get(LIST_NAME);
        return json_array == null ? new JSONArray() : json_array;
    }

    /**
     * @param shared_preference key to clear its JSONArray value
     */
    public void clearJSONArray(String shared_preference){
        try {
            JSONObject main_json = loadJSON(shared_preference);
            main_json.put(LIST_NAME, new JSONArray());
            saveJSON(shared_preference, main_json);
        } catch (Exception ignored) {
        }
    }

    /**
     * @param shared_preference key name for number variable
     * @return number stored under shared_preference key, returns 0 if non-existence
     */
    public int loadInt(String shared_preference) {
        return sharedPreferences.getInt(shared_preference, 0);
    }

    /**
     * @param shared_preference key name for number variable
     * @param default_value     sets the default value if number is non-existence
     * @return number stored under shared_preference
     */
    public int loadInt(String shared_preference, int default_value) {
        return sharedPreferences.getInt(shared_preference, default_value);
    }


    /**
     * @param shared_preference key name for string variable
     * @return string stored under shared_preference key, returns "" if non-existence
     */
    public String loadString(String shared_preference) {
        return sharedPreferences.getString(shared_preference, "");
    }

    /**
     * @param shared_preference key name for string variable
     * @param default_value     sets the default value if string is non-existence
     * @return string stored under shared_preference
     */
    public String loadString(String shared_preference, String default_value) {
        return sharedPreferences.getString(shared_preference, default_value);
    }

    /**
     * @param shared_preference key name for JSONObject variable
     * @return JSONObject stored under shared_preference
     * @throws ParseException error when there is JSON syntax error
     */
    public JSONObject loadJSON(String shared_preference) throws ParseException {
        return (JSONObject) new JSONParser().parse(loadString(shared_preference));
    }
}