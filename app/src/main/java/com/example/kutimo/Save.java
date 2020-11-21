package com.example.kutimo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class Save {
    private Activity activity;
    private int faithPoints;
    private static final String TAG = "Save Class";
    SharedPreferences sharedPreferences;


    /** Constructor **/

    public Save(Activity activity) {

        this.activity = activity;
        sharedPreferences = this.activity.getSharedPreferences("com.example.sharedpreferencesprototype", Context.MODE_PRIVATE);

    }


    public void saveFaithPoints(int faithPoints) {

        sharedPreferences.edit().putInt("faithPoints", faithPoints).apply();
        Log.i(TAG, "faith points: " + faithPoints);

    }


    public int retrieveFaithPoints(){

        faithPoints = sharedPreferences.getInt("faithPoints", faithPoints);
        Log.i(TAG, "faith points: " + faithPoints);
        return faithPoints;


    }


    // To save the scriptures I need to parse the arraylist to a json file and then read it to the saver as a string
    // To return the scriptures we need to get the string from saved preferences and parse it to the array list in the data class.


}
