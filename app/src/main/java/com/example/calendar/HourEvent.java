package com.example.calendar;

import java.time.LocalTime;
import java.util.ArrayList;

class HourEvent
{
    private LocalTime time;
    private ArrayList<Event> events;

    public HourEvent(int hour, ArrayList<Event> events)
    {
        this.time = LocalTime.of(hour, 0);
        this.events = events;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public ArrayList<Event> getEvents()
    {
        return events;
    }

    public void setEvents(ArrayList<Event> events)
    {
        this.events = events;
    }
}
