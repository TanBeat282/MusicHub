package com.tandev.musichub;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.tandev.musichub.helper.ui.WeeklyDivider;

import java.time.LocalDate;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private int year;
    private int month;
    private WeeklyDivider weeklyDivider;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listView);
        Button buttonPreviousMonth = findViewById(R.id.buttonPreviousMonth);
        Button buttonNextMonth = findViewById(R.id.buttonNextMonth);

        LocalDate today = LocalDate.now();
        year = today.getYear();
        month = today.getMonthValue();

        weeklyDivider = new WeeklyDivider(year, month);
        updateWeekList();

        buttonPreviousMonth.setOnClickListener(v -> {
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            weeklyDivider = new WeeklyDivider(year, month);
            updateWeekList();
        });

        buttonNextMonth.setOnClickListener(v -> {
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
            weeklyDivider = new WeeklyDivider(year, month);
            updateWeekList();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateWeekList() {
        List<String> weeks = weeklyDivider.getWeeksInMonth();
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weeks);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(weeks);
            adapter.notifyDataSetChanged();
        }
    }
}

