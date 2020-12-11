package com.example.kutimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
 * @author Megan and Timothy
 */
public class DatePicker extends AppCompatActivity {
    Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Data(this);
        setContentView(R.layout.activity_calendar);

        // Utilize the list of dates stored through the chronometer to add the dates user reads
        List<String> list = data.loadStringList(StorageKeys.DATE);
        List<EventDay> events = new ArrayList<>();

        // Preset to false
        boolean is_today_in_list = false;

        for (String each : list) {
            Calendar each_calendar = Calendar.getInstance();

            // If it today is on the list add a checked mark image
            try {
                each_calendar.setTime(new SimpleDateFormat("MM-dd-yyyy").parse(each));
                if (Calendar.getInstance().getTime() == each_calendar.getTime()) {
                    is_today_in_list = true;
                }
                events.add(new EventDay(each_calendar, R.drawable.checkmark));

            } catch (ParseException ignored) {
            }
        }
        // Otherwise keep K logo image on today's date.
        if (!is_today_in_list) {
            events.add(new EventDay(Calendar.getInstance(), R.drawable.kutimo));
        }

        // Displays dates clicked
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);
    }
}
