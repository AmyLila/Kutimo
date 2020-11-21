package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "Card Activity";
    private int faithPoints, level1, level2, level3, level4, level5, level6, level7, level8, level9, level10, level11, level12;
    ImageView image_level1, image_level2, image_level3, image_level4, image_level5, image_level6, image_level7, image_level8, image_level9, image_level10;
    Save save;





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

        //Trial images aren't working in drawable
        if (faithPoints >= level1){
            image_level1.setImageResource(R.drawable.abel);
        }
        if (faithPoints >= level2){
            image_level1.setImageResource(R.drawable.abinadi);
        }
        if (faithPoints >= level3){
            image_level1.setImageResource(R.drawable.abraham);
        }
        if (faithPoints >= level4){
            image_level1.setImageResource(R.drawable.ammon);
        }
        if (faithPoints >= level5){
            image_level1.setImageResource(R.drawable.anne);
        }
        if (faithPoints >= level6){
            image_level1.setImageResource(R.drawable.daniel);
        }
        if (faithPoints >= level7){
            image_level1.setImageResource(R.drawable.esther);
        }
        if (faithPoints >= level8){
            image_level1.setImageResource(R.drawable.mormon);
        }
        if (faithPoints >= level9){
            image_level1.setImageResource(R.drawable.moroni);
        }
        if (faithPoints >= level10){
            image_level1.setImageResource(R.drawable.mosiah);
        }


        //Log.i(TAG, "changeImage: The button has been tapped");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

    //Amy's Card Activity

        faithPoints = 0;
        save = new Save(this);
        faithPoints = save.retrieveFaithPoints();
        Log.i(TAG, "faith points: " + faithPoints);


        // Levels
        level1 = 100;
        level2 = 200;
        level3 = 300;
        level4 = 400;
        level5 = 500;
        level6 = 600;
        level7 = 700;
        level8 = 800;
        level9 = 900;
        level10 = 1000;
        level11 = 1100;
        level12 = 1200;

        changeImage();
    }
}