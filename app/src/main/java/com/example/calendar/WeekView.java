package com.example.calendar;

import static com.example.calendar.CalendarUtils.daysInWeekArray;
import static com.example.calendar.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekView extends Fragment {

    public WeekView() {
        // Required empty public constructor
    }
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initButtons();
        initWidgets();

        setWeekView();
    }

    private View findViewById (int id) {
        View curView = getView();
        if (curView == null) return null;
        return curView.findViewById(id);
    }

    private void initWidgets()
    {
        calendarRecyclerView = (RecyclerView) findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) findViewById(R.id.monthYearTV);
        eventListView = (ListView) findViewById(R.id.eventListView);
    }

    private void initButtons () {
        ((ImageButton) findViewById(R.id.nextWeek)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction(v);
            }
        });

        ((ImageButton) findViewById(R.id.prevWeek)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction(v);
            }
        });
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getActivity().getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }
}