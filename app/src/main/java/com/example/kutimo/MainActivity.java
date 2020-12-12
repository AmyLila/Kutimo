package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Entry point for Kutimo project.
 *
 * @author Amy, Megan, Ruben, Timothy, and Wanderson
 */

public class MainActivity extends AppCompatActivity {

    // Mandatory values
    private static final String TAG = "MainActivity";
    Data data;

    // Chronometer
    private Chronometer chronometer;
    private boolean is_chronometer_running;
    private long chronometer_pause_offset;

    // Faith Points
    private float faith_points;
    private float streak_multiplier;

    // Progress Bar
    private TextView text_progress_TextView, level_number_TextView, multiplier_Level_TextView;
    private ProgressBar level_ProgressBar;
    public float faith_point_percent = 0;

    // Lamp Image
    ImageView lamp_ImageView;
    int TOTAL_LAMP_IMAGES = 12;
    int DAYS_UNTIL_LAMP_FULL = 365;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new Data(this);

        chronometer = findViewById(R.id.chronometer);
        multiplier_Level_TextView = (TextView) findViewById(R.id.multiplierLevel);
        text_progress_TextView = (TextView) findViewById(R.id.txtProgress);
        level_number_TextView = (TextView) findViewById(R.id.levelNumber);
        level_ProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Setup multiplier
        streak_multiplier = data.loadFloat(StorageKeys.MULTIPLIER, 1);
        multiplier_Level_TextView.setText(String.format("%.1f", streak_multiplier));

        faith_points = data.loadFloat(StorageKeys.FAITH_POINTS, 0);

        updateVisualsWithFaithPoints();
        chronometerFunction();

        // Set the lamp image
        lamp_ImageView = (ImageView) findViewById(R.id.imageView2);
        setLampImage();
    }

    private float MultiplierPercentage() {
        return streak_multiplier / DAYS_UNTIL_LAMP_FULL;
    }

    /**
     * Each sessions' faith points and multiplier
     */
    public void updateFaithPoints() {
        //will not update continuously - the current value will be 1
        data.saveFloat(StorageKeys.FAITH_POINTS, faith_points * (1 + MultiplierPercentage()));
        updateVisualsWithFaithPoints();
        Log.i(TAG, "faith points: " + faith_points);
    }

    public void updateVisualsWithFaithPoints() {
        // refresh screen values because of faithPoints change
        faith_point_percent = (float) Math.round(GetLevel.getPercent(faith_points) * 100) / 100;

        // update visual
        level_ProgressBar.setProgress((int) faith_point_percent);
        text_progress_TextView.setText(String.format("%.2f", faith_point_percent) + " %");
        level_number_TextView.setText(GetLevel.getCurrentLevel(faith_points) + " ");
    }

    /**
     * find the image name from the drawable folder
     *
     * @param name
     * @return
     */
    private int getIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    /**
     * get the image id
     *
     * @param name
     * @return
     */
    private int getIdByView(String name) {
        return getResources().getIdentifier(name, "id", this.getPackageName());
    }

    /**
     * Set the lamp image according to the streak multiplier
     */
    private void setLampImage() {
        final int LAMP_IMAGE_INCREMENT = DAYS_UNTIL_LAMP_FULL / TOTAL_LAMP_IMAGES;
        int lamp_image_frame = Math.min(Math.floorDiv((int) streak_multiplier, LAMP_IMAGE_INCREMENT), TOTAL_LAMP_IMAGES - 1);
        lamp_ImageView.setImageResource(getIdByName("lamp_level" + (lamp_image_frame + 1)));
    }


    /**
     * Calculates multiplier depending on consecutive reading streak.
     */
    private void setStreakMultiplier() {
        String current_date = data.loadString(StorageKeys.CURRENT_DATE, Now());
        try {
            long days_between = daysBetween(current_date, Now());
            if (days_between == 1) {
                streak_multiplier += 1;
                data.saveFloat(StorageKeys.MULTIPLIER, streak_multiplier);
                multiplier_Level_TextView.setText(String.format("%2.2f", MultiplierPercentage() * 100) + '%');
            } else if (days_between != 0) {
                streak_multiplier = 1;
                data.saveFloat(StorageKeys.MULTIPLIER, streak_multiplier);
            }
        } catch (ParseException parseException) {
        }
    }

    /**
     * @param one
     * @param two
     * @return
     * @throws ParseException
     */
    private long daysBetween(String one, String two) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date one_date = simpleDateFormat.parse(one);
        Date two_date = simpleDateFormat.parse(two);
        return Math.abs((one_date.getTime() - two_date.getTime()) / 86_400_000);
    }



    /**
     * @param chronometer
     * @param start_second
     * @param end_second
     * @return
     */
    private boolean is_time_range(Chronometer chronometer, long start_second, long end_second) {
        boolean start = SystemClock.elapsedRealtime() - chronometer.getBase() >= start_second * 1_000L;
        boolean end = SystemClock.elapsedRealtime() - chronometer.getBase() <= end_second * 1_000L;
        return start && end;
    }

    /**
     * @param message
     */
    private void long_toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Displays inspiring messages as the user reads.
     * Increments the faith points every 30 seconds.
     */
    void chronometerFunction() {
        //Chronometer, toasts

        AtomicLong number = new AtomicLong();
        int[] times = Constants.CHRONOMETER_TRIGGER_TOAST;

        // Long toasts that will be displayed after certain time of reading
        chronometer.setOnChronometerTickListener(chronometer -> {
            for (int i = 0; i < times.length; i++)
                if (is_time_range(chronometer, times[i], times[i] + 1))
                    long_toast(Constants.CHRONOMETER_TOASTS[i]);
            if (is_time_range(chronometer, number.get() + 30L, number.get() + 31L)) {
                number.addAndGet(30L);
                faith_points += 1;
                updateFaithPoints();
            }
        });
    }

    /**
     * @return
     */
    private String Now() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format("%d-%d-%d", month, day, year);
    }

    /**
     * Starts/pause the the chronometer.
     *
     * @param view
     */
    public void toggleChronometer(View view) {
        if (is_chronometer_running) {
            is_chronometer_running = false;
            chronometer.stop();
            chronometer_pause_offset = SystemClock.elapsedRealtime() - chronometer.getBase();
        } else {
            is_chronometer_running = true;
            chronometer.start();
            setStreakMultiplier();
            chronometer.setBase(SystemClock.elapsedRealtime() - chronometer_pause_offset);

            data.saveString(StorageKeys.CURRENT_DATE, Now());
            data.appendUniqueStringItem(StorageKeys.DATE, Now());

            Log.d(TAG, "formatted " + Now());
        }
    }

    /**
     * Starts the chronometer.
     */
    public void triggerTimer() {
        if (!is_chronometer_running) {
            is_chronometer_running = true;
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime() - chronometer_pause_offset);
        }
    }

    /**
     * Stops and resets chronometer to 0.
     *
     * @param view
     */
    public void resetChronometer(View view) {
        chronometer_pause_offset = SystemClock.elapsedRealtime() - chronometer.getBase();

        // updateFaithPoints();
        Log.d(TAG, "resetChronometer (faithPoints): " + faith_points);
        Log.d(TAG, "resetChronometer (pause offset): " + chronometer_pause_offset);

        chronometer.stop();
        is_chronometer_running = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer_pause_offset = 0;
    }

    /**
     * @param view
     */
    public void openScripturePicker(View view) {

        Log.i(TAG, "Opening Scripture Picker");

        Intent intent = new Intent(this, ScripturePicker.class);
        ScripturePicker scripture_picker = new ScripturePicker();
        //scripture_picker.show();
        startActivityForResult(intent, 2);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && requestCode == 2) {
            if (data.hasExtra("launchScriptures")) {
                Log.d(TAG, "1!");
                triggerTimer();
                launchScriptures(data.getExtras().getString("launchScriptures"));
            } else if (data.hasExtra("launchStudy")) {
                Log.d(TAG, "2!");
                triggerTimer();
                launchStudy(data.getExtras().getString("launchStudy"));
            } else if (data.hasExtra("currentWeek")) {
                Log.d(TAG, "2!");
                triggerTimer();
                launchStudy(data.getExtras().getString("launchStudy"));
            }
        } else if (resultCode == 3 && requestCode == 2) {
            currentWeek();
        }
    }


    /**
     * @param view
     */

    public void openFavorites(View view) {
        //TODO need to pass faith points in when the button is pushed.
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    /**
     * Launch the current week in Come, Follow Me for year 2020 then year 2021
     */
    public void currentWeek() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int week_of_year = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        week_of_year -= day_of_week == Calendar.SUNDAY ? 1 : 0; // if it's Sunday go back 1 week

        if (year == 2020) {
            // minor adjustment for 2020
            week_of_year -= week_of_year > 14 ? week_of_year > 39 ? 2 : 1 : 0;
            launchStudy("book-of-mormon-2020" + "/" + week_of_year);
        } else if (year == 2021)
            launchStudy("doctrine-and-covenants-2021" + "/" + week_of_year);
    }

    /**
     * Open an intent activity with an option to use Gospel Library or Website with scripture_book
     * as a parameter for link.
     *
     * @param scripture_book Any scripture found in the header from the website.
     */
    void launchScriptures(String scripture_book) {
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/scriptures/%s", scripture_book));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Open an intent activity with an option to use Gospel Library or Website with Come, Follow Me
     * book as a parameter for link.
     *
     * @param url any Come, Follow Me link from website
     */
    void launchStudy(String url) {
        url = url.replace('_', '-'); // convert id underscores as dashes
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * open an intent activity that views the reward cards
     *
     * @param view
     */
    public void openCards(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        Log.i(TAG, "open cards button tapped");
    }

    /**
     * Launches the calendar from DatePicker class
     *
     * @param view
     */
    public void openCalendar(View view) {
        // Starts the DatePicker Class
        Intent intent = new Intent(this, DatePicker.class);
        startActivity(intent);

    }
}