package com.example.kutimo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        for (int i = 0; i < 100; i++) {
            linearLayout.addView(new ScriptureItemView(this, "Esther 8:9", "Then were the king’s scribes called at that time in the third month, that is, the month Sivan, on the three and twentieth day thereof; and it was written according to all that Mordecai commanded unto the Jews, and to the lieutenants, and the deputies and rulers of the provinces which are from India unto Ethiopia, an hundred twenty and seven provinces, unto every province according to the writing thereof, and unto every people after their language, and to the Jews according to their writing, and according to their language.", "SHARE"););
        }
    }
}