package com.tandev.musichub.adapter.week_chart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tandev.musichub.R;
import com.tandev.musichub.helper.ui.Helper;
import com.tandev.musichub.model.chart.home.home_new.week_chart.HomeDataItemWeekChartItem;
import com.tandev.musichub.model.weekchart.WeekChartSelect;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeekChartSelectAdapter extends RecyclerView.Adapter<WeekChartSelectAdapter.WeekChartViewHolder> {

    private ArrayList<WeekChartSelect> weekChartSelects;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(WeekChartSelect weekChartSelect);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public WeekChartSelectAdapter(ArrayList<WeekChartSelect> weekChartSelects) {
        this.weekChartSelects = weekChartSelects;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(ArrayList<WeekChartSelect> weekChartSelects) {
        this.weekChartSelects = weekChartSelects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeekChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weekchart_select, parent, false);
        return new WeekChartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeekChartViewHolder holder, int position) {
        WeekChartSelect weekChartSelect = weekChartSelects.get(position);
        holder.txt_week.setText("Tuần: " + weekChartSelect.getWeek());
        holder.txt_day.setText("(" + Helper.formatToDayMonth(weekChartSelect.getStartDayWeek()) + " - " + Helper.formatToDayMonth(weekChartSelect.getEndDayWeek()) + ")");

        // Định dạng cho màu sắc dựa trên thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date endDate = sdf.parse(weekChartSelect.getEndDayWeek());
            Date currentDate = new Date();

            if (endDate != null && endDate.before(currentDate)) {
                holder.txt_week.setTextColor(Color.WHITE);
                holder.txt_day.setTextColor(Color.WHITE);
                holder.itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(weekChartSelects.get(position));
                    }
                });
            } else {
                holder.txt_week.setTextColor(Color.GRAY);
                holder.txt_day.setTextColor(Color.GRAY);
                holder.itemView.setOnClickListener(v -> {
                    Toast.makeText(holder.itemView.getContext(), "Tuần này chưa tới", Toast.LENGTH_SHORT).show();
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weekChartSelects.size();
    }

    public class WeekChartViewHolder extends RecyclerView.ViewHolder {
        TextView txt_week, txt_day;

        public WeekChartViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_week = itemView.findViewById(R.id.txt_week);
            txt_day = itemView.findViewById(R.id.txt_day);
        }
    }
}
