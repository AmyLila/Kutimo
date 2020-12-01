package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Entry point for Kutimo project.
 *
 * @author Amy, Megan, Ruben, Timothy, and Wanderson
 */

public class MainActivity extends AppCompatActivity {
    // ******
    // Fields
    // ******
    private static final String TAG = "Main Activity";
    //Chronometer
    private Chronometer chronometer;
    private boolean isRunning;
    private long pauseOffset;
    private long hours;
    private long minutes;
    private int intMinutes = (int) (minutes);

    //Faith Points
    private int faithPoints = 0;
    private int multiplier = 1;

    //Progress Bar and progress levels
    private TextView txtProgress;
    private TextView levelNumber;
    private TextView multiplierLevel;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private int levelUpPoints = 500;
    private int currentLevel = 0;
    private int multiplierView = multiplier;
    private Handler handler = new Handler();

    //Scripture Picker
    Data data;
    ArrayList<String> scriptures;
    Save save;

    /**
     * Each sessions' faith points and multiplier
     */
    public void updateFaithPoints() {
        //will not update continuously - the current value will be 1
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
        //save = new Save(this);

        progress_bar();

        chronometer_function();
    } // end onCreate

    void progress_bar() {
        //Progress Bar and levels
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        multiplierLevel = (TextView) findViewById(R.id.multiplierLevel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(() -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(pStatus);
                    txtProgress.setText(pStatus + " %");
                    levelNumber.setText(currentLevel + " ");
                    multiplierLevel.setText(multiplierView + " ");
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pStatus = (faithPoints / levelUpPoints) * 100;
            if (pStatus == 100) {
                pStatus = 0;
                levelUpPoints *= 2;
                currentLevel++;
            }
        }).start();
    }

    private boolean is_time_range(Chronometer chronometer, int start_second, int end_second){
        boolean start = SystemClock.elapsedRealtime() - chronometer.getBase() >= start_second * 1000;
        boolean end = SystemClock.elapsedRealtime() - chronometer.getBase() <= end_second * 1000;
        return start && end;
    }

    private void short_toast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    void chronometer_function() {
        //Chronometer, toasts
        chronometer = findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(chronometer -> {
            if (is_time_range(chronometer, 10, 11)) { // reduce by 1 second to prevent double toast
                short_toast("It's been 10sec! Felt the Spirit yet?");
            }
            if (is_time_range(chronometer, 30, 31)) { // reduce by 1 second to prevent double toast
                short_toast("Halfway through your first FP!");
            }
        });
    }

    /**
     * *****
     * Public Methods
     * *****
     * Calendar, Cards, Chronometer, and Scripture Picker
     *
     * @param view
     */
    //Chronometer, start
    public void toggleChronometer(View view) {
        if (isRunning) {
            isRunning = false;
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        } else {
            isRunning = true;
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }
    }

    //Chronometer, saves time and resets
    public void resetChronometer(View view) {
        // TODO: update pauseOffset when user press resetChronometer while isRunning is set to true
        save = new Save(this);
        hours = (pauseOffset / 3_600_000);
        minutes = (pauseOffset - hours * 3_600_000) / 60_000;

        updateFaithPoints();
        Log.d(TAG, "resetChronometer (faithPoints): " + faithPoints);
        Log.d(TAG, "resetChronometer (intMinutes): " + intMinutes);
        Log.d(TAG, "resetChronometer (minutes): " + minutes);

        chronometer.stop();
        isRunning = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    //Scripture Picker
    public void openScripturePicker(View view) {

        Log.i(TAG, "Opening Scripture Picker");

        Intent intent = new Intent(this, Scripture_picker.class);
        startActivity(intent);
    }

    public void openFavorites(View view){
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    //Cards
    public void openCards(View view) {
        //TODO need to pass faith points in when the button is pushed.
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        Log.i(TAG, "open cards button tapped");
    }

    //Calendar
    public void openCalendar(View view) {
        Intent intent = new Intent(this, Calendar_checkmarks.class);
        startActivity(intent);
    }
}