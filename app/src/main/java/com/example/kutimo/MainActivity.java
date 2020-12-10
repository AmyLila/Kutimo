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

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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
    private int faithPoints;
    private int multiplier;

    //Progress Bar
    private TextView txtProgress;
    private TextView levelNumber;
    private TextView multiplierLevel;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    // Progress bar values
    public float faith_point_status = 0;
    private int levelUpPoints;
    public int currentLevel;

    //Scripture Picker
    ArrayList<String> scriptures;
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(this);
        falsify_data();
        setContentView(R.layout.activity_main);

        faithPoints = data.loadInt(StorageKeys.FAITH_POINTS, 0);
        multiplier = data.loadInt(StorageKeys.MULTIPLIER, 1);

        progress_bar();
        chronometer_function();
    }

    private void falsify_data() {
        data.clearJSONArray(StorageKeys.SCRIPTURES);
        JSONObject scripture = new JSONObject();

        scripture.put("title", "THIS IS THE TITLE");
        scripture.put("content", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sit amet vehicula metus. Pellentesque luctus libero ac sapien sagittis dapibus. Nam nec nunc massa. In.");
        scripture.put("link", "GOSPEL LINK");

        for (int i = 0; i < 10; i++) {
            data.appendItemToJSON(StorageKeys.SCRIPTURES, scripture);
            data.appendItemToJSON(StorageKeys.SCRIPTURES, scripture);
        }

    }

    /**
     * Each sessions' faith points and multiplier
     */
    public void updateFaithPoints() {
        //will not update continuously - the current value will be 1
        data.saveInt(StorageKeys.FAITH_POINTS, faithPoints * minutes * multiplier);
        Log.i(TAG, "faith points: " + faithPoints);
    }


    void progress_bar() {
        //Progress Bar and levels
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        multiplierLevel = (TextView) findViewById(R.id.multiplierLevel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setProgress((int) faith_point_status);
        txtProgress.setText(faith_point_status + " %");
        levelNumber.setText(currentLevel + " ");
        multiplierLevel.setText(multiplier + " ");

        //progress bar update in percentage
        faith_point_status = Math.round((float) faithPoints / levelUpPoints * 100);

        levelUpPoints = data.loadInt(StorageKeys.LEVEL_UP_POINTS, 500);
        currentLevel = data.loadInt(StorageKeys.CURRENT_LEVEL, 0);

        Log.d(TAG, "progress_bar faithPoints " + faithPoints);
        Log.d(TAG, "progress_bar faith_point_status " + faith_point_status);
        Log.d(TAG, "progress_bar levelUpPoints " + levelUpPoints);
    }

    private boolean is_time_range(Chronometer chronometer, long start_second, long end_second) {
        boolean start = SystemClock.elapsedRealtime() - chronometer.getBase() >= start_second * 1_000L;
        boolean end = SystemClock.elapsedRealtime() - chronometer.getBase() <= end_second * 1_000L;
        return start && end;
    }

    private void long_toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void chronometer_function() {
        //Chronometer, toasts
        chronometer = findViewById(R.id.chronometer);

        AtomicLong number = new AtomicLong();

        chronometer.setOnChronometerTickListener(chronometer -> {
            if (is_time_range(chronometer, 10, 11)) { // reduce by 1 second to prevent double toast
                long_toast("It's been 10sec! Feel the Spirit yet?");
            }
            if (is_time_range(chronometer, 60, 61)) {
                long_toast("First FP of the day!");
            }
            if (is_time_range(chronometer, 300, 301)) { // 5 minutes
                long_toast("These are times of faith, these are times of perseverance.");
            }
            if (is_time_range(chronometer, 600, 601)) { // 10 minutes
                long_toast("He who reads it oftenest will like it best!");
            }
            if (is_time_range(chronometer, 900, 901)) { // 15 minutes
                long_toast("First I obey, then I understand!");
            }
            if (is_time_range(chronometer, 1800, 1801)) { // 30 minutes
                long_toast("Believing requires action.");
            }
            if (is_time_range(chronometer, 2700, 2701)) { // 45 minutes
                long_toast("We did not come this far to only come this far!");
            }
            if (is_time_range(chronometer, 3600, 3601)) { // 1 hour
                long_toast("Decisions determine destiny!");
            }
            if (is_time_range(chronometer, 7200, 7201)) { // 2 hours
                long_toast("Joy comes from and because of Him.");
            }
            if (is_time_range(chronometer, 10800, 10801)) { // 3 hours
                long_toast("No one is destined to fail.");
            }
            if (is_time_range(chronometer, 18000, 18001)) { // 5 hours
                long_toast("Well done, thou good and faithful servant.");
            }


            // if (is_time_range(chronometer, number.get() + 60L, number.get() + 61L)) {
            if (is_time_range(chronometer, number.get() + 30L, number.get() + 31L)) {
                number.addAndGet(30L);
                faithPoints += 1;
                data.saveInt(StorageKeys.FAITH_POINTS, faithPoints);

                faith_point_status = Math.round((float) faithPoints / levelUpPoints * 100);

                progressBar.setProgress((int) faith_point_status);
                txtProgress.setText(faith_point_status + " %");
                //levelNumber.setText(currentLevel + " ");
                multiplierLevel.setText(multiplier + " ");


            }
            if (faith_point_status >= 100) {
                faith_point_status = 0;
                levelUpPoints *= 2;
                currentLevel++;

                data.saveInt(StorageKeys.LEVEL_UP_POINTS, levelUpPoints);
                data.saveInt(StorageKeys.CURRENT_LEVEL, currentLevel);
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
        Scripture_picker scripture_picker = new Scripture_picker();
        //scripture_picker.show();
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
        //This method starts the DatePicker Class
        Intent intent = new Intent(this, DatePicker.class);
        startActivity(intent);

    }
}