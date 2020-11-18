package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //comment
    }

    public void openCalendar(View view) {
        Intent intent = new Intent (this, Calendar_checkmarks.class);
        TextView textView = findViewById(R.id.textView);
        String message = textView.getText().toString();
        intent.putExtra("Extra Message", message);
        startActivity(intent);

    }
}