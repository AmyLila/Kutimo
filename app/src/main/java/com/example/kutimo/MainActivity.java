package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Entry point for Kutimo project.
 *
 * @author Amy, Megan, Ruben, Timothy, and Wanderson
 */

public class MainActivity extends AppCompatActivity {

   private static final String TAG = "MainActivity";

    // Calendar
    private ImageButton calendarButton;

    // Chronometer
    private Chronometer chronometer;
    private boolean isRunning;
    private long pauseOffset;
    private int hours;
    private int minutes;

    // Faith Points
    private float faithPoints;
    private float multiplier;

    // Progress Bar
    private TextView txtProgress;
    private TextView levelNumber;
    private TextView multiplierLevel;
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    // Progress bar values
    public float faith_point_status = 0;
    private float levelUpPoints;
    public int currentLevel;

    // Scripture Picker
    ArrayList<String> scriptures;
    Data data;

    // Lamp Image
    ImageView lampFrame;
    int IMAGE_NAMES_TOTAL = 12;
    int DAYS_UNTIL_LAMP_FULL = 365;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(this);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        // Load faith points from shared preferences
        faithPoints = data.loadFloat(StorageKeys.FAITH_POINTS, 0);

        // Load the multiplier from shared preferences
        multiplier = data.loadFloat(StorageKeys.MULTIPLIER, 1);
        multiplierLevel = (TextView) findViewById(R.id.multiplierLevel);
        multiplierLevel.setText(String.format("%.1f", multiplier));
        Log.d(TAG, String.format("Multiplier:%f", multiplier));

        chronometerFunction();
        progress_bar();
        updateVisualsWithFaithPoints();

        // Set the lamp image
        lampFrame = (ImageView) findViewById(R.id.imageView2);
        setLampImage();
    }

    private float MultiplierPercentage(){
        return multiplier / DAYS_UNTIL_LAMP_FULL;
    }

    /**
     * Each sessions' faith points and multiplier
     */
    public void updateFaithPoints() {
        //will not update continuously - the current value will be 1
        data.saveFloat(StorageKeys.FAITH_POINTS, faithPoints * (1 + MultiplierPercentage()));
        updateVisualsWithFaithPoints();
        Log.i(TAG, "faith points: " + faithPoints);
    }

    public void updateVisualsWithFaithPoints() {
        // refresh screen values because of faithPoints change
        faith_point_status = Math.round(GetLevel.getPercent(faithPoints) * 100) / 100;
        currentLevel = GetLevel.getCurrentLevel(faithPoints);
        levelUpPoints = GetLevel.getLevelUpPoints(faithPoints);

        // update visual
        progressBar.setProgress((int) faith_point_status);
        txtProgress.setText(String.format("%.2f", faith_point_status)+ " %");
        levelNumber.setText(currentLevel + " ");
    }

    /**
     * find the image name from the drawable folder
     * @param name
     * @return
     */
    private int getIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    /**
     * get the image id
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
        final int LAMP_IMAGE_INCREMENT = DAYS_UNTIL_LAMP_FULL / IMAGE_NAMES_TOTAL;
        int lamp_image_frame = Math.min(Math.floorDiv((int) multiplier, LAMP_IMAGE_INCREMENT), IMAGE_NAMES_TOTAL - 1);
        lampFrame.setImageResource(getIdByName("lamp_level" + (lamp_image_frame + 1)));
    }


    /**
     *  Calculates multiplier depending on consecutive reading streak.
     */
    private void setStreakMultiplier() {
        String current_date = data.loadString(StorageKeys.CURRENT_DATE, Now());
        try {
            long days_between = daysBetween(current_date, Now());
            if (days_between == 1){
                multiplier += 1;
                data.saveFloat(StorageKeys.MULTIPLIER, multiplier);
                multiplierLevel.setText(String.format("%2.2f", MultiplierPercentage() * 100) + '%');
            } else if (days_between != 0){
                multiplier = 1;
                data.saveFloat(StorageKeys.MULTIPLIER, multiplier);
            }
        } catch (ParseException parseException) {
        }
    }

    /**
     *
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
     * Sets the visuals related to game progress, such as the progress donut,
     * level number and multiplier number.
     */
    void progress_bar() {
        // Progress Bar and levels
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        updateVisualsWithFaithPoints();


        Log.d(TAG, "progress_bar faithPoints " + faithPoints);
        Log.d(TAG, "progress_bar faith_point_status " + faith_point_status);
        Log.d(TAG, "progress_bar levelUpPoints " + levelUpPoints);
        Log.d(TAG, "progress_bar multiplier " + MultiplierPercentage());
    }

    /**
     *
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
     *
     * @param message
     */
    private void long_toast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     *
     */
    void chronometerFunction() {
        //Chronometer, toasts
        chronometer = findViewById(R.id.chronometer);

        AtomicLong number = new AtomicLong();

        /**
         * Times in order
         * 1, 15, 30", 1', 2', 3', 5', 10', 15', 30', 45', 1 hour, 2 hours, 3 hours, 5 hours.
         */
        int[] times = {1, 15, 30, 60, 120, 180, 300, 600, 900, 1800, 2700, 3600, 7200, 10800, 18000};

        // Messages for the toasts
        String[] message = {"All efforts begin with desire.",
                "It's been 15 sec. Feel the Spirit yet?",
                "First Faith Point of the day!",
                "These are times of faith, these are times of perseverance.",
                "Believe. Love. Do.",
                "Heaven is cheering you on!",
                "He who reads it oftenest will like it best!",
                "First I obey, then I understand!",
                "Believing requires action.",
                "We did not come this far to only come this far!",
                "Decisions determine destiny!",
                "Joy comes from and because of Him.",
                "No one is destined to fail.",
                "Well done, thou good and faithful servant."};

        // Long toasts that will be displayed after certain time of reading
        chronometer.setOnChronometerTickListener(chronometer -> {
            for (int i = 0; i < message.length; i++)
                if (is_time_range(chronometer, times[i], times[i] + 1))
                    long_toast(message[i]);

            if (is_time_range(chronometer, number.get() + 30L, number.get() + 31L)) {
                number.addAndGet(30L);
                faithPoints += 1;
                updateFaithPoints();

                // TODO: check this out if it causes issue for not being here
                /*
                if(multiplier > 1) {
                    multiplierLevel.setText(String.format("%2.2f", MultiplierPercentage() * 100));
                }
                else
                    multiplierLevel.setText(String.format("%.1f", multiplier));
                 */

            }

        });
    }

    /**
     *
     * @return
     */
    private String Now() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format("%d-%d-%d", month, day, year);
    }

    /**
     * TODO
     * @param view
     */
    public void toggleChronometer(View view) {
        if (isRunning) {
            isRunning = false;
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        } else {
            isRunning = true;
            chronometer.start();
            setStreakMultiplier();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);

            data.saveString(StorageKeys.CURRENT_DATE, Now());
            data.appendUniqueStringItem(StorageKeys.DATE, Now());

            Log.d(TAG, "formatted " + Now());
        }
    }

    /**
     *
     */
    public void triggerTimer() {
        if (!isRunning) {
            isRunning = true;
            chronometer.start();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }
    }

    /**
     *
     * @param view
     */
    public void resetChronometer(View view) {
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        hours = (int) (pauseOffset / 3_600_000);
        minutes = (int) ((pauseOffset - hours * 3_600_000) / 60_000);

        // updateFaithPoints();
        Log.d(TAG, "resetChronometer (faithPoints): " + faithPoints);
        //Log.d(TAG, "resetChronometer (intMinutes): " + intMinutes);
        Log.d(TAG, "resetChronometer (minutes): " + minutes);
        Log.d(TAG, "resetChronometer (pause offset): " + pauseOffset);

        chronometer.stop();
        isRunning = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    /**
     *
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
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == 2 && requestCode == 2 ) {
            if (data.hasExtra("launchScriptures")){
                Log.d(TAG, "1!");
                triggerTimer();
                launchScriptures(data.getExtras().getString("launchScriptures"));
            } else if (data.hasExtra("launchStudy")){
                Log.d(TAG, "2!");
                triggerTimer();
                launchStudy(data.getExtras().getString("launchStudy"));
            } else if (data.hasExtra("currentWeek")){
                Log.d(TAG, "2!");
                triggerTimer();
                launchStudy(data.getExtras().getString("launchStudy"));
            }
        } else if (resultCode == 3 && requestCode == 2){
             currentWeek();
         }
    }


    /**
     *
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
     * @param view
     */
    public void openCards(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        Log.i(TAG, "open cards button tapped");
    }

    /**
     * Launches the calendar from DatePicker class
     * @param view
     */
    public void openCalendar(View view) {
        // Starts the DatePicker Class
        Intent intent = new Intent(this, DatePicker.class);
        startActivity(intent);

    }
}