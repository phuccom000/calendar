package com.example.calendar;

import static com.example.calendar.CalendarUtils.selectedDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class DayView extends Fragment {

    private TextView monthDayText;
    private TextView dayOfWeekTV;
    private ListView hourListView;

    private EventManager manager;

    public DayView() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_view, container, false);
    }

    private void initButtons () {
        ((ImageButton) findViewById(R.id.nextDay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDayAction(v);
            }
        });

        ((ImageButton) findViewById(R.id.prevDay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousDayAction(v);
            }
        });
    }
    private View findViewById (int id) {
        View curView = getView();
        if (curView == null) return null;
        return curView.findViewById(id);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void restartFrag () {
        manager = new EventManager(getActivity().getSharedPreferences("APP_INFO", Context.MODE_PRIVATE), "Events");

        initButtons();
        initWidgets();

        setDayView();
    }

    private void initWidgets()
    {
        monthDayText = (TextView) findViewById(R.id.monthDayText);
        dayOfWeekTV = (TextView) findViewById(R.id.dayOfWeekTV);
        hourListView = (ListView) findViewById(R.id.hourListView);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        Activity activity = getActivity();
        HourAdapter hourAdapter = new HourAdapter(activity.getApplicationContext(), activity, hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();

        for (int hour = 0; hour < 24; ++hour) list.add(new HourEvent(hour, new ArrayList<>()));

        ArrayList<Event> events = manager.getEvents(selectedDate);

        for (Event event : events) {
            if (event != null) list.get(event.getDateTime().getHour()).getEvents().add(event);
        }

        return list;
    }

    public void previousDayAction(View view)
    {
        selectedDate = selectedDate.minusDays(1);
        setDayView();
    }

    public void nextDayAction(View view)
    {
        selectedDate = selectedDate.plusDays(1);
        setDayView();
    }

    @Override
    public void onStart () {
        super.onStart();
        restartFrag();
    }
}