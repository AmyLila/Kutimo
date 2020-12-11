package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        // Utilize the list of dates stored through the chronometer to add dates read
        List<String> list = data.loadStringList(StorageKeys.DATE);
        List<Calendar> calendars = new ArrayList<>();
        List<EventDay> events = new ArrayList<>();

        //Magic trick
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.kutimo));

        for (String each : list) {
            calendar = Calendar.getInstance();

            try {
                calendar.setTime(new SimpleDateFormat("MM-dd-yyyy").parse(each));
                events.add(new EventDay(calendar, R.drawable.checkmark));
                calendars.add(calendar);
            } catch (ParseException ignored) {
            }
        }

        getSupportActionBar().hide();


        // Displays dates clicked
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);
    }
}
