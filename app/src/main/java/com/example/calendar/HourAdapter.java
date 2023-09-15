package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HourAdapter extends ArrayAdapter<HourEvent>
{
    private Context ctx;
    private Activity activity;
    public HourAdapter(@NonNull Context context, @NonNull Activity act, List<HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
        ctx = context;
        activity = act;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        setHour(convertView, event.getTime());
        setEvents(convertView, event.getEvents());

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setEvents(View view, ArrayList<Event> events) {
        RecyclerView hourEventView = view.findViewById(R.id.hourEventView);
        HourEventAdapter adapter = new HourEventAdapter(ctx, activity, events);

        hourEventView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        hourEventView.setAdapter(adapter);
    }

}













