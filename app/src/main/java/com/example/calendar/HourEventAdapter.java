package com.example.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HourEventAdapter extends RecyclerView.Adapter<HourEventAdapter.ViewHolder> {
    private Context context;

    private Activity activity;
    private ArrayList<Event> events;

    public HourEventAdapter(Context ctx, Activity act, ArrayList<Event> evts) {
        this.context = ctx;
        this.events = evts;
        this.activity = act;
        if (events.size() == 0) events.add(null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View hourEventView = inflater.inflate(R.layout.hour_event_cell, parent, false);
        ViewHolder viewHolder = new ViewHolder(hourEventView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        if (event == null) {
            holder.view.setBackground(null);
            holder.eventName.setText("-");
            holder.eventTime.setText("-");
            return;
        }
        holder.eventName.setText(CalendarUtils.truncateEventName(event.getName(), 20));
        holder.eventTime.setText(CalendarUtils.formattedTime(event.getDateTime().toLocalTime()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EventEditActivity.class);
                intent.putExtra("chosenEventID", event.getID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName, eventTime;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            eventName = itemView.findViewById(R.id.hourEventName);
            eventTime = itemView.findViewById(R.id.hourEventTime);
        }
    }
}
