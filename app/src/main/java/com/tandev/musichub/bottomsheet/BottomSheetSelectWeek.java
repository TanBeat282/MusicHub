package com.tandev.musichub.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tandev.musichub.R;
import com.tandev.musichub.adapter.week_chart.WeekChartSelectAdapter;
import com.tandev.musichub.model.weekchart.WeekChartSelect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BottomSheetSelectWeek extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private int weekOfYear;
    private BottomSheetDialog bottomSheetDialog;
    private SelectWeekListener mListener;

    private Calendar calendar;
    private WeekChartSelectAdapter weekChartSelectAdapter;
    private ArrayList<WeekChartSelect> weekChartSelects;
    private RecyclerView rv_week_chart_select;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private final SimpleDateFormat sdfMonthYear = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    private ImageView buttonPreviousMonth;
    private ImageView buttonNextMonth;
    private TextView txt_current_month;


    public BottomSheetSelectWeek(Context context, Activity activity, int weekOfYear) {
        this.context = context;
        this.activity = activity;
        this.weekOfYear = weekOfYear;
    }

    public interface SelectWeekListener {
        void onWeekSelected(WeekChartSelect weekChartSelect);
    }

    public void setWeekListener(SelectWeekListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_select_week, null);
        bottomSheetDialog.setContentView(view);

        calendar = Calendar.getInstance();

        initViews(view);
        initAdapter();
        onCLick();
        updateWeekList();

        return bottomSheetDialog;
    }

    private void initViews(View view) {
        rv_week_chart_select = view.findViewById(R.id.rv_week_chart_select);
        buttonPreviousMonth = view.findViewById(R.id.buttonPreviousMonth);
        txt_current_month = view.findViewById(R.id.txt_current_month);
        buttonNextMonth = view.findViewById(R.id.buttonNextMonth);
    }

    private void initAdapter() {
        weekChartSelects = new ArrayList<>();
        rv_week_chart_select.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        weekChartSelectAdapter = new WeekChartSelectAdapter(weekChartSelects);
        rv_week_chart_select.setAdapter(weekChartSelectAdapter);
    }

    private void onCLick() {
        buttonPreviousMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            txt_current_month.setText(sdfMonthYear.format(calendar.getTime()));
            updateWeekList();
        });

        buttonNextMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            txt_current_month.setText(sdfMonthYear.format(calendar.getTime()));
            updateWeekList();
        });

        weekChartSelectAdapter.setOnItemClickListener(weekChart -> {
            if (mListener != null) {
                mListener.onWeekSelected(weekChart);
            }
            bottomSheetDialog.dismiss();
        });
    }

    private void updateWeekList() {
        ArrayList<WeekChartSelect> weekCharts = new ArrayList<>();
        Calendar tempCalendar = (Calendar) calendar.clone();

        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        while (true) {
            String startDate = formatDate(tempCalendar.getTime());
            tempCalendar.add(Calendar.DAY_OF_WEEK, 6); // Ngày cuối tuần của tuần hiện tại
            String endDate = formatDate(tempCalendar.getTime());
            int weekNumber = tempCalendar.get(Calendar.WEEK_OF_YEAR);

            if (tempCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) ||
                    tempCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) + 1) {
                weekCharts.add(new WeekChartSelect(weekNumber, startDate, endDate));
            }

            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);

            if (tempCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH) + 1 ||
                    tempCalendar.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
                break;
            }
        }
        weekChartSelects = weekCharts;
        weekChartSelectAdapter.setFilterList(weekChartSelects);

    }

    private String formatDate(java.util.Date date) {
        return sdf.format(date);
    }
}
