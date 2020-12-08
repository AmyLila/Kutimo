package com.example.kutimo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        data = new Data(this);

        setupIntent();

        try {
            JSONArray scriptures = data.loadListItemsFromJSON(StorageKeys.SCRIPTURES);
            for (Object each : scriptures)
                linearLayout.addView(new ScriptureItemView(this, (JSONObject) each));
        } catch (ParseException ignored) {
        }

    }

    private void setupIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
            Toast.makeText(this, "Not working.", Toast.LENGTH_SHORT).show();
        }
    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            getContent(sharedText);

            JSONObject scripture = new JSONObject();

            scripture.put("title", getTitle(sharedText));
            scripture.put("content", getContent(sharedText));
            scripture.put("link", getGospelUrl(sharedText));

            data.appendItemToJSON(StorageKeys.SCRIPTURES, scripture);

            Toast.makeText(this, sharedText, Toast.LENGTH_SHORT).show();
        }
    }

    private String getGospelUrl(String text) {
        String[] items = text.split("\n");
        return items[items.length - 1];
    }

    private String getTitle(String text) {
        String[] items = text.split("\n");
        return items[items.length - 3];
    }

    private String getContent(String text) {
        List<String> items = new ArrayList<String>(Arrays.asList(text.split("\n")));

        // remove ending part of the items list to have beginning only
        for (int i = 0; i < 4; i++) {
            items.remove(items.size() - 1);
        }

        // remove excesses space
        for (int i = 0; i < items.size(); i++) {
            items.set(i, items.get(i).trim());
        }

        return String.join("\n", items);
    }
}