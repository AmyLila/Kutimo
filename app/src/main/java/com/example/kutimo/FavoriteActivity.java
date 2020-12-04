package com.example.kutimo;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        for (int i = 0; i < 100; i++) {
            ScriptureItemView scriptureItemView = new ScriptureItemView(this);
            scriptureItemView.setArtistText("Bob");
            scriptureItemView.setButton("PRESS ME");
            scriptureItemView.setTrackText("MUSIC time!");
            linearLayout.addView(scriptureItemView);
        }
    }
}