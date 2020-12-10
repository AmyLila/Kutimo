package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * DatePicker Class implements a Material View Calendar from Applandeo.
 *
 * @author Megan De Leon
 */
public class DatePicker extends AppCompatActivity {
    Data data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(this);
        setContentView(R.layout.activity_calendar);
        List<String> list = data.loadStringList(StorageKeys.DATE);

        getSupportActionBar().hide();

        // Adding dates read with our app icon
        List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.kutimo));

        // Displaying dates clicked
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);

    }
}
