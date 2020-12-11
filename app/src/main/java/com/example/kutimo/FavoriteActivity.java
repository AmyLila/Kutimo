package com.example.kutimo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * @author Timothy
 */
public class FavoriteActivity extends AppCompatActivity {
    Data data;
    boolean is_from_intent = false;
    String Tag = "FavoriteActivity";


    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);
        data = new Data(this);

        setupIntent();
        if (!is_from_intent) {
            this.setTitle("Favorite Scriptures");
        }

        JSONArray scriptures = data.loadListItemsFromJSON(StorageKeys.SCRIPTURES);
        for (Object each : scriptures)
            linearLayout.addView(new ScriptureItemView(this, (JSONObject) each));


    }

    /**
     *
     */
    private void setupIntent() {
        Intent intent = getIntent();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(intent.getAction()) && type != null) {
            if ("text/plain".equals(type)) {
                is_from_intent = true;
                handleSendText(intent);
            }
        }
    }

    /**
     *
     * @param intent
     */
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            getContent(sharedText);

            JSONObject scripture = new JSONObject();

            scripture.put("title", getTitle(sharedText));
            scripture.put("content", getContent(sharedText));
            scripture.put("link", getGospelUrl(sharedText));

            data.appendItemToJSON(StorageKeys.SCRIPTURES, scripture);
        }
    }

    /**
     *
     * @param text
     * @return
     */
    private String getGospelUrl(String text) {
        String[] items = text.split("\n");
        return items[items.length - 1];
    }

    /**
     *
     * @param text
     * @return
     */
    private String getTitle(String text) {
        String[] items = text.split("\n");
        return items[items.length - 3];
    }

    /**
     *
     * @param text
     * @return
     */
    private String getContent(String text) {
        List<String> items = new ArrayList<String>(Arrays.asList(text.split("\n")));

        // Remove ending part of the items list to have beginning only
        for (int i = 0; i < 4; i++) {
            items.remove(items.size() - 1);
        }

        // Remove excesses space
        for (int i = 0; i < items.size(); i++) {
            items.set(i, items.get(i).trim());
        }

        return String.join("\n", items);
    }
}