package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatePicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.kutimo));

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);

    }
}