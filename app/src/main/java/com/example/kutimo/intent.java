package com.example.kutimo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class intent extends AppCompatActivity {

    protected String TAG_NAME = "intent";
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        data = new Data(this);

        // Get intent, action and MIME type
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

    private String getGospelUrl(String text){
        String[] items = text.split("\n");
        return items[items.length - 1];
    }

    private String getTitle(String text){
        String[] items = text.split("\n");
        return items[items.length - 3];
    }

    private String getContent(String text){
        List<String> items = new ArrayList<String>(Arrays.asList(text.split("\n")));

        // remove ending part of the items list to have beginning only
        for (int i = 0; i < 4; i++){
            items.remove(items.size() - 1);
        }

        // remove excesses space
        for (int i = 0; i < items.size(); i++){
            items.set(i, items.get(i).trim());
        }

        return String.join("\n", items);
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
            EditText textView = (EditText) findViewById(R.id.intentResult);
            // textView.setText(sharedText);
            textView.setText(sharedText);
        }
    }
}