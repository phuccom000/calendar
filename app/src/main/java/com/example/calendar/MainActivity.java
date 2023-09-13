package com.example.calendar;

import static com.example.calendar.CalendarUtils.daysInMonthArray;
import static com.example.calendar.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private int currentFocus = 0;

    private int[] focuses = {R.id.monthViewButton, R.id.weekViewButton, R.id.dayViewButton};

    private int[][] fragmentNavigations = {
            {0, R.id.month_view_to_week_view, R.id.month_view_to_day_view},
            {R.id.week_view_to_month_view, 0, R.id.week_view_to_day_view},
            {R.id.day_view_to_month_view, R.id.day_view_to_week_view, 0}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarUtils.selectedDate = LocalDate.now();

        for (int i = 0; i < focuses.length; ++i) {
            final int val = i;
            ((TextView) findViewById(focuses[i])).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchFocusTo(val);
                }
            });
        }

        Activity _this = this;
        ((TextView) findViewById(R.id.createEventButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_this, EventEditActivity.class));
            }
        });

        selectFocus(0);
    }

    private void highlightFocus (int oldI, int newI) {
        TextView oldItem = findViewById(focuses[oldI]);
        oldItem.setBackgroundResource(0);
        oldItem.setTypeface(null, Typeface.NORMAL);

        selectFocus(newI);
    }

    private void selectFocus (int index) {
        TextView item = (TextView) findViewById(focuses[index]);
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(3, 0xFF000000); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            item.setBackgroundDrawable(border);
        } else {
            item.setBackground(border);
        }

        item.setTypeface(null, Typeface.BOLD);
    }

    private void switchFocusTo(int index) {
        if (currentFocus == index) return;

        int oldIndex = currentFocus;

        // highlight buttons
        highlightFocus(currentFocus, index);

        currentFocus = index;

        ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.main_app)).getNavController().navigate(fragmentNavigations[oldIndex][currentFocus]);
    }
}








