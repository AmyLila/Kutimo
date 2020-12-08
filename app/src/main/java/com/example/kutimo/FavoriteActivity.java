package com.example.kutimo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        data = new Data(this);

        try {
            JSONArray scriptures = data.loadListItemsFromJSON(StorageKeys.SCRIPTURES);
            for (Object each : scriptures)
                linearLayout.addView(new ScriptureItemView(this, (JSONObject) each));
        } catch (ParseException ignored) {
        }

    }
}