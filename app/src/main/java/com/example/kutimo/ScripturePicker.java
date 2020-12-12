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
        // setup buttons actions for scriptures
        int[] scriptureIDs = {R.id.bofm, R.id.ot, R.id.nt, R.id.dc_testament, R.id.pgp};
        for (int each : scriptureIDs)
            findViewById(each).setOnClickListener(v -> returnIntent(v,2));

        // setup buttons actions for Come, Follow Me
        int[] studyIDs = {R.id.new_testament_2019, R.id.book_of_mormon_2020, R.id.doctrine_and_covenants_2021};
        for (int each : studyIDs)
            findViewById(each).setOnClickListener(v -> returnIntent(v,3));
    }

    public void returnIntent(View view, int result_code) {
        Intent intent = new Intent();

        String id_name =  getResources().getResourceEntryName(view.getId());
        intent.putExtra("Link", id_name.replace('_', '-'));
        setResult(result_code, intent);
        finish();
    }

    public void currentWeek(View v) {
        setResult(3);
        finish();
    }
}