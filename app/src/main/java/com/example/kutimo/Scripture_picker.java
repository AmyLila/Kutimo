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

    private void setup_buttons(){
        int[] scriptureIDs = {R.id.bofm, R.id.ot, R.id.nt, R.id.dc_testament, R.id.pgp};
        for (int each : scriptureIDs){
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launch_scriptures(getResources().getResourceEntryName(v.getId()));
                }
            });
        }

        int[] studyIDs = {R.id.new_testament_2019, R.id.book_of_mormon_2020, R.id.doctrine_and_covenants_2021}; // doctrine-and-covenants-2021
        for (int each : studyIDs){
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    launch_study(getResources().getResourceEntryName(v.getId()));
                }
            });
        }
    }

    public void current_week(View v){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int week_of_year = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        week_of_year -= day_of_week == Calendar.SUNDAY ? 1 : 0; // if it's Sunday go back 1 week

        if (year == 2020){
            // minor adjustment for 2020
            week_of_year -= week_of_year  > 14 ? 1 : 0;
            week_of_year -= week_of_year  > 39 ? 1 : 0;
            launch_study("book-of-mormon-2020" + "/" + week_of_year);
        } else if (year == 2021){
            launch_study("doctrine-and-covenants-2021" + "/" + week_of_year);
        }
    }

    void launch_scriptures(String url) {
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/scriptures/%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    void launch_study(String url) {
        url = url.replace('_', '-');
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));
        Log.d(TAG, String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    void stopwatch(){

    }

}