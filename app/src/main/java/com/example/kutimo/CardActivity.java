package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "Card Activity";
    private int faithPoints, level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12;
    ImageView image_level1, image_level2, image_level3, image_level4, image_level5, image_level6, image_level7, image_level8, image_level9, image_level10, image_level11, image_level12;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        data = new Data(this);

        //Amy's Card Activity

        faithPoints = data.load(StorageKeys.FAITH_POINTS);
        Log.i(TAG, "faith points: " + faithPoints);

        int level = 500;


        // Levels
        level1 = level;
        level2 = level * 2;
        level3 = level * 3;
        level4 = level * 4;
        level5 = level * 5;
        level6 = level * 6;
        level7 = level * 7;
        level8 = level * 8;
        level9 = level * 9;
        level10 = level * 10;
        level11 = level * 11;
        level12 = level * 12;

        changeImage();
    }

    /** Change the UI according to faith points Method **/
    public void changeImage(){

        image_level1 = (ImageView) findViewById(R.id.image_level1);
        image_level2 = (ImageView) findViewById(R.id.image_level2);
        image_level3 = (ImageView) findViewById(R.id.image_level3);
        image_level4 = (ImageView) findViewById(R.id.image_level4);
        image_level5 = (ImageView) findViewById(R.id.image_level5);
        image_level6 = (ImageView) findViewById(R.id.image_level6);
        image_level7 = (ImageView) findViewById(R.id.image_level7);
        image_level8 = (ImageView) findViewById(R.id.image_level8);
        image_level9 = (ImageView) findViewById(R.id.image_level9);
        image_level10 = (ImageView) findViewById(R.id.image_level10);
        image_level11 = (ImageView) findViewById(R.id.image_level11);
        image_level12 = (ImageView) findViewById(R.id.image_level12);

        //Trial images aren't working in drawable
        if (faithPoints >= level1){
            image_level1.setImageResource(R.drawable.abel);
        }
        if (faithPoints >= level2){
            image_level2.setImageResource(R.drawable.abinadi);
        }
        if (faithPoints >= level3){
            image_level3.setImageResource(R.drawable.abraham);
        }
        if (faithPoints >= level4){
            image_level4.setImageResource(R.drawable.ammon);
        }
        if (faithPoints >= level5){
            image_level5.setImageResource(R.drawable.anne);
        }
        if (faithPoints >= level6){
            image_level6.setImageResource(R.drawable.daniel);
        }
        if (faithPoints >= level7){
            image_level7.setImageResource(R.drawable.esther);
        }
        if (faithPoints >= level8){
            image_level8.setImageResource(R.drawable.mormon);
        }
        if (faithPoints >= level9){
            image_level9.setImageResource(R.drawable.moroni);
        }
        if (faithPoints >= level10){
            image_level10.setImageResource(R.drawable.mosiah);
        }
        if (faithPoints >= level11){
            image_level11.setImageResource(R.drawable.jonas);
        }
        if (faithPoints >= level12){
            image_level12.setImageResource(R.drawable.king_benjamin);
        }

        //Log.i(TAG, "changeImage: The button has been tapped");

    }
}