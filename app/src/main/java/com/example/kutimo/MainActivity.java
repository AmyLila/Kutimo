package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private int faithPoints, level1, level2, level3;
    ImageView image_level1, image_level2, image_level3;
    Data data;
    ArrayList<String> scriptures;
    Save save;

    //those variables are for the progress bar graphics
    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler handler = new Handler();

    /**
     * Default constructor for AppCompatActivity. All Activities must have a default constructor
     * for API 27 and lower devices or when using the default
>>>>>>> 77aaa5270584b91811e658ed56362199fc525cc6
     */
    public MainActivity() {
        faithPoints = 5;
        level1 = 10;
        level2 = 20;
        level3 = 30;
    }



    public void updateFaithPoints() {

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

        //those will find the view for the progress bar
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //this block will run the progress bar
        new Thread(new Runnable() {
            @Override
            public void run() {
                //while (pStatus < 101) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(pStatus);
                        txtProgress.setText(pStatus + " %");
                    }
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pStatus++;
                if(pStatus == 100)
                    pStatus = 0;
                //}
            }
        }).start();


    }

    public void openScripturePicker(View view){
        //debug message
        Log.i(TAG, "Opening Scripture Picker");

        Intent intent = new Intent(this, Scripture_picker.class);
        startActivity(intent);
    }

    public void openCards(View view) {
        // Do something in response to button
        //need to pass faith points in when the button is pushed.

        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        //debug message
        Log.i(TAG, "open cards button tapped");
    }



    public void openCalendar(View view) {
        Intent intent = new Intent(this, Calendar_checkmarks.class);
        TextView textView = findViewById(R.id.textView);
        String message = textView.getText().toString();
        intent.putExtra("Extra Message", message);
        startActivity(intent);

    }

}