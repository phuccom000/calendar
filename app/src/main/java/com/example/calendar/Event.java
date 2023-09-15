package com.example.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    private String name;

    private LocalDateTime datetime;

    private int ID;

    public Event (String eventName, LocalDateTime time, int id) {
        this.name = eventName;
        this.datetime = time;
        this.ID = id;
    }

    public String getName () {
        return name;
    }

    public LocalDateTime getDateTime () {
        return datetime;
    }

    public int getID () {
        return ID;
    }

    public void update (String newName, LocalDateTime newDate) {
        name = newName;
        datetime = newDate;
    }
}
