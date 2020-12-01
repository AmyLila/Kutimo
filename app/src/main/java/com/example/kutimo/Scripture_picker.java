package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Scripture_picker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scripture_picker);
    }

    public void intent_example(View v) {
        Toast.makeText(this, "HELLO WORLD! how are you?", Toast.LENGTH_SHORT).show();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE, "Send message");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void bofm(View v) {
        launch_scriptures("bofm");
    }

    public void ot(View v) {
        launch_scriptures("ot");
    }

    public void nt(View v) {
        launch_scriptures("nt");
    }

    public void dc_testament(View v) {
        launch_scriptures("dc-testament");
    }

    public void pgp(View v) {
        launch_scriptures("pgp");
    }

    public void new_testament_2019(View v) {
        launch_study("new-testament-2019");
    }

    public void book_of_mormon_2020(View v) {
        launch_study("book-of-mormon-2020");
    }

    public void doctrine_and_covenant_2021(View v) {
        launch_study("doctrine-and-covenants-2021");
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
        Toast.makeText(this, "Receiving.", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/scriptures/%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    void launch_study(String url) {
        Toast.makeText(this, "Receiving.", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));
        Log.i("Scripture picker", String.format("https://www.churchofjesuschrist.org/study/manual/come-follow-me-for-individuals-and-families-%s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            stopwatch();
        }
    }

    void stopwatch(){

    }

}