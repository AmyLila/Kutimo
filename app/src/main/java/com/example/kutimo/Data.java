package com.example.kutimo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.StringWriter;

public class Data {
    Activity activity;
    SharedPreferences sharedPreferences;

    Data(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com.storage", Context.MODE_PRIVATE);
    }

    Data(Activity activity, String page) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("com." + page, Context.MODE_PRIVATE);
    }

    public void save(String shared_preference, int number) {
        sharedPreferences.edit().putInt(shared_preference, number).apply();
    }

    public void save(String shared_preference, String variable) {
        sharedPreferences.edit().putString(shared_preference, variable).apply();
    }

    public void save(String shared_preference, JSONObject json_value) {
        StringWriter out = new StringWriter();
        try {
            json_value.writeJSONString(out);
            save(shared_preference, out.toString());
        } catch (IOException ignored) {
        }
    }

    public void append(String shared_preference, String named_key, JSONObject json_value) {
        StringWriter out = new StringWriter();
        JSONParser parser = new JSONParser();
        try {
            JSONObject main_json = main_json = (JSONObject) parser.parse(loadString(shared_preference));
            main_json.put(named_key, json_value);
            main_json.writeJSONString(out);
            save(shared_preference, out.toString());
        } catch (Exception ignored) {}
    }

    public int loadInt(String shared_preference) {
        return sharedPreferences.getInt(shared_preference, 0);
    }

    public String loadString(String shared_preference) {
        return sharedPreferences.getString(shared_preference, "");
    }

    public String loadString(String shared_preference, String default_value) {
        return sharedPreferences.getString(shared_preference, default_value);
    }

    public String loadFromJSON(String shared_preference){
        return "";
    }
}

    /*
    public int faithPoints;
    Save save;
    public ArrayList<String> scriptures = new ArrayList<>();


    public Data() {

        faithPoints = 0;
        faithPoints = save.retrieveFaithPoints();


        scriptures.add("Hello");
        scriptures.add("Hola");
        scriptures.add("Bonjour");
        scriptures.add("Ciao");
    }



    @Override
    public String toString() {
        return "Data{" +
                "faithPoints=" + faithPoints +
                ", scriptures=" + scriptures +
                '}';
    }
     */