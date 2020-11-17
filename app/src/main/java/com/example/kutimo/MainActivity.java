package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private int faithPoints, level1, level2, level3;
    ImageView image_level1, image_level2, image_level3;
    Data data;
    ArrayList<String> scriptures;
    Save save;



    /**
     * Default constructor for AppCompatActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * {@link AppComponentFactory}.
     */
    public MainActivity() {
        faithPoints = 5;
        level1 = 10;
        level2 = 20;
        level3 = 30;
    }


    /** Change the UI according to faith points Method **/
    public void changeImage(){

        image_level1 = (ImageView) findViewById(R.id.image_level1);
        image_level2 = (ImageView) findViewById(R.id.image_level2);
        image_level3 = (ImageView) findViewById(R.id.image_level3);

        if (faithPoints >= level1){
            image_level1.setImageResource(R.drawable.unlocked_level1);
        }
        if (faithPoints >= level2){
            image_level1.setImageResource(R.drawable.unlocked_level3);
        }
        if (faithPoints >= level3){
            image_level1.setImageResource(R.drawable.unlocked_level3);
        }


        //Log.i(TAG, "changeImage: The button has been tapped");

    }

    public void updateFaithPoints(){
        faithPoints = save.retrieveFaithPoints();
        faithPoints += 5;
        save.saveFaithPoints(faithPoints);
        Log.i(TAG, "faith points: " + faithPoints);
        Log.i(TAG, "scriptures: " + scriptures);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        data = new Data();
        faithPoints = data.faithPoints;
        scriptures = data.scriptures;
        save = new Save(this);

        updateFaithPoints();

        changeImage();

        // Hello from Amy (SUPPORTED)

    }
}