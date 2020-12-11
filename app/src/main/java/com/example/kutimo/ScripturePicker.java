package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

/**
 *
 *
 * @author Timothy
 */
public class ScripturePicker extends AppCompatActivity {
    private static final String TAG = "ScripturePicker";

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
    private void setup_buttons() {
        // setup buttons actions for scriptures by using id name as part of link
        int[] scriptureIDs = {R.id.bofm, R.id.ot, R.id.nt, R.id.dc_testament, R.id.pgp};
        for (int each : scriptureIDs) {
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("launchScriptures", getResources().getResourceEntryName(v.getId()));
                    setResult(2, intent);
                    finish();
                }
            });
        }

        // setup buttons actions for Come, Follow Me by using id name as part of link
        int[] studyIDs = {R.id.new_testament_2019, R.id.book_of_mormon_2020, R.id.doctrine_and_covenants_2021}; // doctrine-and-covenants-2021
        for (int each : studyIDs) {
            findViewById(each).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("launchStudy", getResources().getResourceEntryName(v.getId()));
                    setResult(2, intent);
                    finish();
                }
            });
        }
    }

    public void current_week(View v) {
        setResult(3);
        finish();
    }
}