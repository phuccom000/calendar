package com.example.calendar;

import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventManager {
    private ArrayList<Event> events;

    private SharedPreferences storage;

    private String namespace;

    private String eventName = "name", eventDate = "date", eventCount = "count";

    private int MAX_ID_REG = 0;

    public EventManager (SharedPreferences prefs, String Namespace) {
        storage = prefs;
        namespace = Namespace;
        load();
    }

    private static class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event e1, Event e2) {
            if (!e1.getDateTime().equals(e2.getDateTime())) return e1.getDateTime().compareTo(e2.getDateTime());
            return e1.getID() - e2.getID();
        }
    }

    private ArrayList<Event> sort(ArrayList<Event> arr) {
        Collections.sort(arr, new EventComparator());
        return arr;
    }

    private boolean saveEvent (Event event, int newID) {
        if (event == null) return false;
        SharedPreferences.Editor editor = storage.edit();
        String ID = Integer.toString(newID);
        editor.putString(namespace + "_" + eventName + "_" + ID, event.getName());
        editor.putString(namespace + "_" + eventDate + "_" + ID, CalendarUtils.toDateString(event.getDateTime()));
        editor.commit();
        return true;
    }

    private Event loadSingle (int ID) {
        String id = Integer.toString(ID);
        String name = storage.getString(namespace + "_" + eventName + "_" + id, "");
        if (name.equals("")) return null;
        LocalDateTime date = CalendarUtils.parseDate(storage.getString(namespace + "_" + eventDate + "_" + id, ""));
        if (date == null) return null;
        return new Event(name, date, ID);
    }

    private void save (boolean reloadCache) {
        int ID = 0;
        for (Event event : events) {
            if (saveEvent(event, ID)) ++ID;
        }
        SharedPreferences.Editor editor = storage.edit();
        editor.putInt(namespace + "_" + eventCount, ID);
        editor.commit();
        if (reloadCache) load();
    }

    private void load () {
        events = new ArrayList<Event>();
        int eventsCount = storage.getInt(namespace + "_" + eventCount, 0);
        int UID = 0;
        for (int i = 0; i < eventsCount; ++i) {
            Event evt = loadSingle(i);
            if (evt != null) events.add(new Event(evt.getName(), evt.getDateTime(), UID++));
        }

        MAX_ID_REG = UID;
    }

    public void add (String name, LocalDateTime date, boolean reloadCache) {
        events.add(new Event(name, date, MAX_ID_REG++));
        save(reloadCache);
    }

    public Event get (int ID) {
        for (Event event : events) {
            if (event.getID() == ID) return event;
        }

        return null;
    }

    public Event remove (int id, boolean reloadCache) {
        Event evt = null;
        for (Event event : events) {
            if (event.getID() == id) {
                evt = event;
                events.remove(event);
                break;
            }
        }

        if (evt != null) save(reloadCache);

        return evt;
    }

    public void edit (int id, String newName, LocalDateTime newDate, boolean reloadCache) {
        if (id < 0) {
            add(newName, newDate, reloadCache);
            return;
        }

        boolean found = false;

        for (Event event : events) {
            if (event.getID() == id) {
                found = true;
                event.update(newName, newDate);
                break;
            }
        }

        if (found) save(reloadCache);
    }

    public ArrayList<Event> getEvents (LocalDate date) {
        ArrayList<Event> res = new ArrayList<Event>();
        for (Event event : events) {
            if (event.getDateTime().toLocalDate().equals(date)) res.add(event);
        }

        return sort(res);
    }

    public ArrayList<Event> getEvents (LocalDate start, LocalDate end) {
        ArrayList<Event> res = new ArrayList<Event>();
        for (Event event : events) {
            LocalDate date = event.getDateTime().toLocalDate();
            if ((date.equals(start) || date.isAfter(start)) && (date.equals(end) || date.isBefore(end))) res.add(event);
        }

        return sort(res);
    }

    public boolean hasEventOn (LocalDate date) {
        for (Event event : events) {
            if (event.getDateTime().toLocalDate().equals(date)) return true;
        }
        return false;
    }
}
