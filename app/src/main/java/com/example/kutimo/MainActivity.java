package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;

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

    //Calendar
    private ImageButton calendarButton;

    //Chronometer
    private Chronometer chronometer;
    private boolean isRunning;
    private long pauseOffset;
    private int hours;
    private int minutes;

    //Faith Points
    private int faithPoints = 300;
    private int multiplier = 1;

    //Progress Bar
    private TextView txtProgress;
    private TextView levelNumber;
    private TextView multiplierLevel;
    private ProgressBar progressBar;
    public float pStatus = 0;
    private int levelUpPoints = 500;
    public int currentLevel = 0;
    private Handler handler = new Handler();

    //Scripture Picker
    ArrayList<String> scriptures;
    Data data;

    /**
     * Each sessions' faith points and multiplier
     */
    public void updateFaithPoints() {
        //will not update continuously - the current value will be 1
        faithPoints = data.load(StorageKeys.FAITH_POINTS) * minutes * multiplier;
        data.save(StorageKeys.FAITH_POINTS, faithPoints);
        Log.i(TAG, "faith points: " + faithPoints);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(this);
        setContentView(R.layout.activity_main);

        //Create a new save object and update faith points
        //save = new Save(this);

        progress_bar();

        chronometer_function();
    }

    void progress_bar() {
        //Progress Bar and levels
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        multiplierLevel = (TextView) findViewById(R.id.multiplierLevel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //progress bar update in percentage
        pStatus = (float)faithPoints/levelUpPoints * 100;

        new Thread(() -> {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress((int)pStatus);
                    txtProgress.setText(pStatus + " %");
                    levelNumber.setText(currentLevel + " ");
                    multiplierLevel.setText(multiplier + " ");
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pStatus == 100) {
                pStatus = 0;
                levelUpPoints *= 2;
                currentLevel++;
            }
        }).start();
        System.out.println(faithPoints);
        System.out.println(pStatus);
        System.out.println(levelUpPoints);
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
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        hours = (int) (pauseOffset / 3_600_000);
        minutes = (int) ((pauseOffset - hours * 3_600_000) / 60_000);

        updateFaithPoints();
        Log.d(TAG, "resetChronometer (faithPoints): " + faithPoints);
        //Log.d(TAG, "resetChronometer (intMinutes): " + intMinutes);
        Log.d(TAG, "resetChronometer (minutes): " + minutes);
        Log.d(TAG, "resetChronometer (pause offset): " + pauseOffset);

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

    public void openFavorites(View view) {
        //TODO need to pass faith points in when the button is pushed.
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    //Cards
    public void openCards(View view) {
        //TODO need to pass faith points in when the button is pushed.
        // This doesn't need done because the card activity gets faithpoints from shared preferences through the save class.
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        Log.i(TAG, "open cards button tapped");
    }

    //Calendar
    public void openCalendar(View view) {
        //or try range picker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        //MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker(); // customDatePicker(). datePicker();
        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialdatepicker = builder.build();

        materialdatepicker.show(getSupportFragmentManager(), "CALENDAR_FRAGMENT");
        //Intent intent = new Intent(this, Calendar_checkmarks.class);
        //startActivity(intent);
    }
}