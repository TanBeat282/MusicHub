package com.tandev.musichub;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private Calendar calendar;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = findViewById(R.id.listView);
        Button buttonPreviousMonth = findViewById(R.id.buttonPreviousMonth);
        Button buttonNextMonth = findViewById(R.id.buttonNextMonth);

        calendar = Calendar.getInstance();

        updateWeekList();

        buttonPreviousMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateWeekList();
        });

        buttonNextMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateWeekList();
        });
    }

    private void updateWeekList() {
        ArrayList<String> weeks = new ArrayList<>();
        Calendar tempCalendar = (Calendar) calendar.clone(); // Sử dụng một bản sao để không làm thay đổi calendar gốc

        // Xác định ngày đầu tiên của tháng hiện tại
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Xác định ngày cuối cùng của tháng hiện tại
        Calendar startOfMonth = (Calendar) tempCalendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Calendar endOfMonth = (Calendar) tempCalendar.clone();

        // Reset tempCalendar để tính toán tuần
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Tính tuần cho tháng hiện tại
        while (tempCalendar.get(Calendar.MONTH) <= calendar.get(Calendar.MONTH) + 1) {
            String startDate = formatDate(tempCalendar.getTime());
            tempCalendar.add(Calendar.DAY_OF_WEEK, 6); // Ngày cuối tuần của tuần hiện tại
            String endDate = formatDate(tempCalendar.getTime());
            int weekNumber = tempCalendar.get(Calendar.WEEK_OF_YEAR);

            if (tempCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) ||
                    tempCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) + 1) {
                weeks.add("Week: " + weekNumber + " : " + startDate + " - " + endDate);
            }

            // Tiến đến tuần kế tiếp
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Cập nhật ListView
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weeks);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(weeks);
            adapter.notifyDataSetChanged();
        }

        Log.d(">>>>>>>>>>>>>>>>>>", "Month: " + (calendar.get(Calendar.MONTH) + 1) + " Year: " + calendar.get(Calendar.YEAR));
    }


    private String formatDate(java.util.Date date) {
        return sdf.format(date);
    }
}
