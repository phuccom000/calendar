package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    private Activity activity;
    public EventAdapter(@NonNull Context context, Activity act, List<Event> events)
    {
        super(context, 0, events);
        this.activity = act;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        ((TextView)convertView.findViewById(R.id.eventTimeTV)).setText(CalendarUtils.formattedDateTime(event.getDateTime()));
        ((TextView)convertView.findViewById(R.id.eventNameTV)).setText(CalendarUtils.truncateEventName(event.getName(), 20));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EventEditActivity.class);
                intent.putExtra("chosenEventID", event.getID());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }
}
