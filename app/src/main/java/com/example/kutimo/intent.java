package com.example.kutimo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class intent extends AppCompatActivity {

    protected String TAG_NAME = "intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

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
        String url_regex = "(https:\\/\\/www[.][-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|])";
        Pattern p = Pattern.compile(url_regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);

        // Get the last url in case potential of url in content text.
        String url_string = "";
        while(m.find()) {
            url_string = m.group();
        }
        return url_string;
    }

    private String getTitle(String text){
        String url_regex = "(https:\\/\\/www[.][-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|])";
        Pattern p = Pattern.compile(url_regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);

        // Get the last url in case potential of url in content text.
        String url_string = "";
        while(m.find()) {
            url_string = m.group();
        }
        return url_string;
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            String title = "";
            String content = getTitle(sharedText);
            String link = getGospelUrl(sharedText);
            Toast.makeText(this, sharedText, Toast.LENGTH_SHORT).show();
            EditText textView = (EditText) findViewById(R.id.intentResult);
            // textView.setText(sharedText);
            textView.setText(sharedText);
        }
    }
}