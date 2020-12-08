package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "Card Activity";
    private int faithPoints;
    Data data;
    ImageView[] image_views;
    int[] level_ranges;
    int[] image_ids;

    private int getIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    private int getIdByView(String name) {
        return getResources().getIdentifier(name, "id", this.getPackageName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        data = new Data(this);

        //Amy's Card Activity

        faithPoints = data.loadInt(StorageKeys.FAITH_POINTS);
        Log.i(TAG, "faith points: " + faithPoints);

        preload();

        for (int i = 0; i < image_ids.length; i++)
            if (faithPoints >= level_ranges[i])
                image_views[i].setImageResource(image_ids[i]);
    }

    private void preload(){
        int level = 500;
        int image_names_total = 15;
        String image_prefix_name = "image";

        image_ids = new int[image_names_total];

        for (int i = 0; i < image_names_total; i++)
            image_ids[i] = getIdByName(image_prefix_name + (i + 1));

        image_views = new ImageView[image_names_total];
        for (int i = 0; i < image_names_total; i++)
            image_views[i] = (ImageView) findViewById(getIdByView("image_level" + (i + 1)));


        // Levels
        level_ranges = new int[image_names_total];
        int power = 1;
        for (int i = 0; i < level_ranges.length; i++)
            level_ranges[i] = level * (int) Math.pow(2, power);
    }
}