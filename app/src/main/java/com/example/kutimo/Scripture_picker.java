package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

public class Scripture_picker extends AppCompatActivity {
    private static final String TAG = "Scripture Picker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_picker);

        setup_buttons();
    }

    /**
     * Smaller code by using for loop for several buttons performing similar scripture actions and
     * Come, Follow Me materials
     */
    private void setup_buttons(){
        // setup buttons actions for scriptures by using id name as part of link
        int[] scriptureIDs = {R.id.bofm, R.id.ot, R.id.nt, R.id.dc_testament, R.id.pgp};
        for (int each : scriptureIDs){
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launch_scriptures(getResources().getResourceEntryName(v.getId()));
                }
            });
        }

        // setup buttons actions for Come, Follow Me by using id name as part of link
        int[] studyIDs = {R.id.new_testament_2019, R.id.book_of_mormon_2020, R.id.doctrine_and_covenants_2021}; // doctrine-and-covenants-2021
        for (int each : studyIDs){
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launch_study(getResources().getResourceEntryName(v.getId()));
                }
            });
        }
    }

    /**
     * Launch the current week in Come, Follow Me for year 2020 then year 2021
     */
    public void current_week(View v){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int week_of_year = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        week_of_year -= day_of_week == Calendar.SUNDAY ? 1 : 0; // if it's Sunday go back 1 week

        if (year == 2020){
            // minor adjustment for 2020
            week_of_year -= week_of_year > 14 ? week_of_year > 39 ? 2 : 1 : 0;
            launch_study("book-of-mormon-2020" + "/" + week_of_year);
        } else if (year == 2021){
            launch_study("doctrine-and-covenants-2021" + "/" + week_of_year);
        }
    }

    /**
     * Open an intent activity with an option to use Gospel Library or Website with scripture_book
     * as a parameter for link.
     * @param scripture_book Any scripture found in the header from the website.
     */
    void launch_scriptures(String scripture_book) {
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/scriptures/%s", scripture_book));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    /**
     * Open an intent activity with an option to use Gospel Library or Website with Come, Follow Me
     * book as a parameter for link.
     * @param url
     */
    void launch_study(String url) {
        url = url.replace('_', '-'); // convert id underscores as dashes
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    void stopwatch(){

    }

}