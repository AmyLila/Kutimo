package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "Card Activity";
    private int faithPoints, level1, level2, level3;
    ImageView image_level1, image_level2, image_level3;
    Data data;
    Save save;
    MainActivity mainActivity;



    /**
     * Default constructor for AppCompatActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
     * {@link AppComponentFactory}.
     */
    public CardActivity() {

        //Need to pass faith points in.
        //this.faithPoints = faithPoints;
       data = new Data();
       //save = new Save(mainActivity);
       faithPoints = data.faithPoints;
//        faithPoints = mainActivity.save.retrieveFaithPoints();

        Log.i(TAG, "faith points: " + faithPoints);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        changeImage();
    }
}