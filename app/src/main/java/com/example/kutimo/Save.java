package com.example.kutimo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Save {
    private MainActivity mainActivity;
    private int faithPoints;
    private static final String TAG = "Shared Preferences";
    SharedPreferences sharedPreferences;

    /** Constructor **/
    public Save(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
        sharedPreferences = this.mainActivity.getSharedPreferences("com.example.sharedpreferencesprototype", Context.MODE_PRIVATE);
        faithPoints = 0;
    }



    void saveFaithPoints(int faithPoints){
        sharedPreferences.edit().putInt("faithPoints", faithPoints).apply();
        Log.i(TAG, "faith points: " + faithPoints);

    }

    int retrieveFaithPoints(){
        faithPoints = sharedPreferences.getInt("faithPoints", faithPoints);
        Log.i(TAG, "faith points: " + faithPoints);
        return faithPoints;


    }

    // To save the scriptures I need to parse the arraylist to a json file and then read it to the saver as a string
    // To return the scriptures we need to get the string from saved preferences and parse it to the array list in the data class.




}
