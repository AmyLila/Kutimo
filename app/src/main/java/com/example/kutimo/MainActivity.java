package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Entry point for Kutimo project.
 *
 * @author Amy, Megan, Ruben, Timothy, and Wanderson
 */

public class MainActivity extends AppCompatActivity {

    // Mandatory values
    private static final String TAG = "MainActivity";
    Data storage_data;

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
        storage_data = new Data(this);

        chronometer = findViewById(R.id.chronometer);
        multiplier_Level_TextView = (TextView) findViewById(R.id.multiplierLevel);
        text_progress_TextView = (TextView) findViewById(R.id.txtProgress);
        level_number_TextView = (TextView) findViewById(R.id.levelNumber);
        level_ProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        lamp_ImageView = (ImageView) findViewById(R.id.imageView2);

        // Set the lamp image
        setLampImage();

        // Setup multiplier
        streak_multiplier = storage_data.loadFloat(StorageKeys.MULTIPLIER, 1);
        multiplier_Level_TextView.setText(String.format("%2.2f", MultiplierPercentage() * 100) + '%');

        faith_points = storage_data.loadFloat(StorageKeys.FAITH_POINTS, 0);

        updateVisualsWithFaithPoints();
        chronometerFunction();
    }

    // ***************************
    // *    Helper functions     *
    // ***************************

    /**
     * Easier typing this method than the longer way around.
     * @param message A String message to be displayed in long toast
     */
    private void longToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Get the image id by image name from the drawable folder.
     * @param name file_name found in drawable folder
     * @return image_id
     */
    private int getImageIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", this.getPackageName());
    }

    /**
     * Calculates multiplier depending on consecutive reading streak.
     */
    private void setStreakMultiplier() {
        String current_date = storage_data.loadString(StorageKeys.CURRENT_DATE, todayDate());
        try {
            long total_day_streak = daysBetween(current_date, todayDate());
            if (total_day_streak == 1) {
                streak_multiplier += 1;
                storage_data.saveFloat(StorageKeys.MULTIPLIER, streak_multiplier);
                multiplier_Level_TextView.setText(String.format("%2.2f", MultiplierPercentage() * 100) + '%');
            } else if (total_day_streak > 1) {
                streak_multiplier = 1;
                storage_data.saveFloat(StorageKeys.MULTIPLIER, streak_multiplier);
                multiplier_Level_TextView.setText(String.format("%2.2f", MultiplierPercentage() * 100) + '%');
            }
        } catch (ParseException parseException) {
        }
    }

    private float MultiplierPercentage() {
        return streak_multiplier / DAYS_UNTIL_LAMP_FULL;
    }

    /**
     * Returns the total days between start and end dates, regardless of order.
     *
     * @param start_date with month-day-year format (i.e. 03-04-2006)
     * @param end_date   with month-day-year format (i.e. 04-05-2007)
     * @return total days between start_date and end_date (always returns positive)
     * @throws ParseException for not following "MM-dd-yyyy" format
     */
    private long daysBetween(String start_date, String end_date) throws ParseException {
        java.text.SimpleDateFormat date_format = new java.text.SimpleDateFormat("MM-dd-yyyy");
        Long start_time = date_format.parse(start_date).getTime();
        Long end_time = date_format.parse(end_date).getTime();

        return Math.abs((start_time - end_time) / 86_400_000);
    }

    /**
     * @param chronometer
     * @param start_second
     * @param end_second
     * @return
     */
    private boolean isTimeRange(Chronometer chronometer, long start_second, long end_second) {
        boolean start = SystemClock.elapsedRealtime() - chronometer.getBase() >= start_second * 1_000L;
        boolean end = SystemClock.elapsedRealtime() - chronometer.getBase() <= end_second * 1_000L;
        return start && end;
    }

    /**
     * It returns the today's date.
     * @return a MM-dd-YYYY formatted String (i.e. 04-04-2004)
     */
    private String todayDate() {
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format("%d-%d-%d", month, day, year);
    }

    // ***********************
    // *    Update visuals   *
    // ***********************

    /**
     * Set the lamp image according to the streak multiplier
     */
    public void setLampImage() {
        final int LAMP_IMAGE_INCREMENT = DAYS_UNTIL_LAMP_FULL / TOTAL_LAMP_IMAGES;
        int lamp_image_frame = Math.min(Math.floorDiv((int) streak_multiplier, LAMP_IMAGE_INCREMENT), TOTAL_LAMP_IMAGES - 1);
        String lamp_filename = "lamp_level" + (lamp_image_frame + 1);
        lamp_ImageView.setImageResource(getImageIdByName(lamp_filename));
    }

    public void updateVisualsWithFaithPoints() {
        faith_point_percent = (float) Math.round(GetLevel.getPercent(faith_points) * 100) / 100;
        // refresh screen values because of faithPoints change
        level_ProgressBar.setProgress((int) faith_point_percent);
        text_progress_TextView.setText(String.format("%.2f", faith_point_percent) + " %");
        level_number_TextView.setText(GetLevel.getCurrentLevel(faith_points) + " ");
    }

    // ***************************
    // *    Handle chronometer   *
    // ***************************

    /**
     * Displays inspiring messages as the user reads.
     * Increments the faith points every 30 seconds.
     */
    void chronometerFunction() {
        // Chronometer, toasts
        AtomicLong time = new AtomicLong();
        int[] times = Constants.CHRONOMETER_TRIGGER_TOAST;

        chronometer.setOnChronometerTickListener(chronometer -> {
            // Long toasts that will be displayed after certain time of reading
            for (int i = 0; i < times.length; i++)
                if (isTimeRange(chronometer, times[i], times[i] + 1))
                    longToast(Constants.CHRONOMETER_TOASTS[i]);

            if (isTimeRange(chronometer, time.get() + 30L, time.get() + 31L)) {
                time.addAndGet(30L);
                faith_points += 1;
                storage_data.saveFloat(StorageKeys.FAITH_POINTS, faith_points * (1 + MultiplierPercentage()));
                updateVisualsWithFaithPoints();
            }
        });
    }

    /**
     * Starts/pause the the chronometer.
     */
    public void toggleChronometer(View view) {
        if (is_chronometer_running) {
            is_chronometer_running = false;
            chronometer.stop();
            chronometer_pause_offset = SystemClock.elapsedRealtime() - chronometer.getBase();
        } else
            startChronometer();
    }

    public void startChronometer() {
        is_chronometer_running = true;
        chronometer.start();
        setStreakMultiplier();
        chronometer.setBase(SystemClock.elapsedRealtime() - chronometer_pause_offset);

        storage_data.saveString(StorageKeys.CURRENT_DATE, todayDate());
        storage_data.appendUniqueStringItem(StorageKeys.DATE, todayDate());
    }

    /**
     * Stops and resets chronometer to 0.
     */
    public void resetChronometer(View view) {
        chronometer.stop();
        chronometer_pause_offset = 0;
        chronometer.setBase(SystemClock.elapsedRealtime() - chronometer.getBase());
        is_chronometer_running = false;
    }

    // ****************************
    // * Launch Scripture intents *
    // ****************************

    /**
     * Open an intent activity with an option to use Gospel Library or Website with scripture_book
     * or "Come, Follow Me" book as a parameter for link.
     *
     * @param id_name Any scripture or "Come, Follow Me" found in the header from the website.
     */
    void launchStudy(String id_name, String gospel_link) {
        Uri gospel_uri = Uri.parse(String.format(gospel_link, id_name));

        Intent uri_intent = new Intent(Intent.ACTION_VIEW, gospel_uri);
        if (uri_intent.resolveActivity(getPackageManager()) != null)
            startActivity(uri_intent);
    }

    // Launch the current week in Come, Follow Me for year 2020 then year 2021.
    public void currentWeek() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int week_of_year = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        // if it's Sunday go back 1 week
        week_of_year -= day_of_week == Calendar.SUNDAY ? 1 : 0;

        if (year == 2020) {
            // minor adjustment for 2020
            week_of_year -= week_of_year > 14 ? week_of_year > 39 ? 2 : 1 : 0;
            launchStudy("book-of-mormon-2020" + "/" + week_of_year, Constants.COME_FOLLOW_ME_LINK);
        } else if (year == 2021)
            launchStudy("doctrine-and-covenants-2021" + "/" + week_of_year, Constants.COME_FOLLOW_ME_LINK);
    }


    // ***************************
    // *      Button calls       *
    // ***************************

    public void openScriptureDialog(View view) {
        Intent intent = new Intent(this, ScripturePicker.class);
        startActivityForResult(intent, 1);
    }

    public void openFavoriteActivity(View view) {
        startActivity(new Intent(this, FavoriteActivity.class));
    }

    // Open an intent activity that views the reward cards.
    public void openCards(View view) {
        startActivity(new Intent(this, CardActivity.class));
    }

    // Launches the calendar from DatePicker class
    public void openCalendar(View view) {
        startActivity(new Intent(this, DatePicker.class));
    }

    // ***************************
    // *  Handle ActivityResult  *
    // ***************************

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!is_chronometer_running && resultCode >= 1 && resultCode <= 3)
                startChronometer();
            switch (resultCode) {
                case 1:
                    launchStudy(data.getExtras().getString("Link"), Constants.SCRIPTURE_LINK);
                    break;
                case 2:
                    launchStudy(data.getExtras().getString("Link"), Constants.COME_FOLLOW_ME_LINK);
                    break;
                case 3:
                    currentWeek();
                    break;
            }
        }
    }
}