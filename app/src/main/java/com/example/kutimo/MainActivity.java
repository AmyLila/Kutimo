package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private int faithPoints = 0;
    private int multiplier = 1;
    Data data;
    ArrayList<String> scriptures;
    Save save;

    //those variables are for the progress bar graphics
    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler handler = new Handler();

    //variables for the chronometer
    private Chronometer chronometer;
    private boolean isRunning;
    private long pauseOffset;
    private long hours;
    private long minutes;
    private  int intMinutes = (int) (minutes);



    // Faith points updater
    public void updateFaithPoints() {
        faithPoints = save.retrieveFaithPoints();
        faithPoints += intMinutes * multiplier;
        save.saveFaithPoints(faithPoints);
        Log.i(TAG, "faith points: " + faithPoints);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        //Create a new save object and update faith points
//        save = new Save(this);




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

        //Chronometer toasts
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000 && (SystemClock.elapsedRealtime() - chronometer.getBase()) <= 12000) {
                    Toast.makeText(MainActivity.this, "It's been 10sec! Felt the Spirit yet?", Toast.LENGTH_SHORT).show();
                }
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 30000 && (SystemClock.elapsedRealtime() - chronometer.getBase()) <= 32000) {
                    Toast.makeText(MainActivity.this, "Halfway through your first FP!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    } // end onCreate

    //Start the chronometer
    public void startChronometer(View view) {
        if (!isRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            isRunning = true;
        } else if (isRunning) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            isRunning = false;
        }
    }

    //Saves time and resets the chronometer
    public void resetChronometer(View view) {
        save = new Save(this);
        hours = (pauseOffset / 3600000);
        minutes = (pauseOffset - hours * 3600000) / 60000;
        // update faith points
        updateFaithPoints();
        Log.d(TAG, "resetChronometer: " + faithPoints);
        Log.d(TAG, "resetChronometer: " + intMinutes);
        Log.d(TAG, "resetChronometer: " + minutes);



        chronometer.stop();
        isRunning = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void openScripturePicker(View view){
        //debug message
        Log.i(TAG, "Opening Scripture Picker");

        Intent intent = new Intent(this, Scripture_picker.class);
        startActivity(intent);
    }

    public void openCards(View view) {
        //need to pass faith points in when the button is pushed.

        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        //debug message
        Log.i(TAG, "open cards button tapped");
    }

    public void openCalendar(View view) {
        Intent intent = new Intent(this, Calendar_checkmarks.class);
        startActivity(intent);

    }

}