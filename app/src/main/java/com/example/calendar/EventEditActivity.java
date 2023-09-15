package com.example.calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private EditText dayEdit, monthEdit, yearEdit;
    private EditText hourEdit, minuteEdit;

    private LocalDateTime time;

    int parsedID = -1;
    String name = "";

    EventManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        manager = new EventManager(this.getSharedPreferences("APP_INFO", Context.MODE_PRIVATE), "Events");
        parsedID = getIntent().getIntExtra("chosenEventID", -1);

        if (parsedID > -1) {
            Event evt = manager.get(parsedID);
            if (evt != null) {
                name = evt.getName();
                time = evt.getDateTime();
            }
            else parsedID = -1;
        }
        else time = LocalDateTime.now();

        eventNameET.setText(name);

        dayEdit.setText(Integer.toString(time.getDayOfMonth()));
        monthEdit.setText(Integer.toString(time.getMonthValue()));
        yearEdit.setText(Integer.toString(time.getYear()));

        hourEdit.setText(Integer.toString(time.getHour()));
        String min = Integer.toString(time.getMinute());
        if (min.length() < 2) min = "0" + min;
        minuteEdit.setText(min);

        ((TextView)findViewById(R.id.editEventTitle)).setText(parsedID == -1 ? "Add Event" : "Modify Event");

        initButtons();
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        dayEdit = findViewById(R.id.dayEdit);
        monthEdit = findViewById(R.id.monthEdit);
        yearEdit = findViewById(R.id.yearEdit);

        hourEdit = findViewById(R.id.hourEdit);
        minuteEdit = findViewById(R.id.minuteEdit);
    }

    private void initButtons () {
        ((Button) findViewById(R.id.saveEventButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEventAction();
            }
        });

        ((Button) findViewById(R.id.cancelEventButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button delete = findViewById(R.id.deleteEventButton);
        if (parsedID == -1) {
            delete.setClickable(false);
            delete.setEnabled(false);
        }
        else delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });
    }

    private void saveEventAction()
    {
        String eventName = eventNameET.getText().toString();
        if (eventName.matches("")){
            Toast.makeText(this, "You did not enter any memo", Toast.LENGTH_SHORT).show();
            return;
        }

        String day = dayEdit.getText().toString(), month = monthEdit.getText().toString(), year = yearEdit.getText().toString();
        String hour = hourEdit.getText().toString(), minute = minuteEdit.getText().toString();

        LocalDateTime selectedDate = CalendarUtils.parseDate(year, month, day, hour, minute);

        if (selectedDate == null) {
            Toast.makeText(this, "Invalid Date or Time", Toast.LENGTH_SHORT).show();
            return;
        }

        manager.edit(parsedID, eventName, selectedDate, false);
        Toast.makeText(this, "Event successfully saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteEvent () {
        Activity _this = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Attention!");
        builder.setMessage("Do you want to delete this event?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.remove(parsedID, false);
                        Toast.makeText(_this, "Event successfully deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}