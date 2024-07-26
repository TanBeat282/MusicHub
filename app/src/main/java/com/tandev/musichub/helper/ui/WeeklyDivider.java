package com.tandev.musichub.helper.ui;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeeklyDivider {
    private int year;
    private int month;

    public WeeklyDivider(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> getWeeksInMonth() {
        List<String> weeks = new ArrayList<>();
        YearMonth currentMonth = YearMonth.of(year, month);
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();
        LocalDate nextMonthStartDate = currentMonth.plusMonths(1).atDay(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        LocalDate currentStartDate = startDate;
        while (currentStartDate.isBefore(nextMonthStartDate)) {
            // Xác định bắt đầu và kết thúc của tuần
            LocalDate weekStart = currentStartDate.with(weekFields.dayOfWeek(), 1);
            LocalDate weekEnd = weekStart.plusDays(6);

            // Nếu tuần bắt đầu trước ngày bắt đầu của tháng, đặt lại tuần bắt đầu bằng ngày bắt đầu của tháng
            if (weekStart.isBefore(startDate)) {
                weekStart = startDate;
            }
            // Nếu tuần kết thúc sau ngày kết thúc của tháng hiện tại, đặt lại tuần kết thúc bằng ngày kết thúc của tháng hiện tại
            if (weekEnd.isAfter(endDate)) {
                weekEnd = endDate;
            }

            int weekNumber = currentStartDate.get(weekFields.weekOfWeekBasedYear());
            String weekRange = "Week " + weekNumber + ": " + weekStart.format(formatter) + " - " + weekEnd.format(formatter);
            weeks.add(weekRange);

            // Nếu tuần kết thúc sau ngày kết thúc của tháng hiện tại, xử lý tuần cho tháng tiếp theo
            if (weekEnd.isAfter(endDate)) {
                LocalDate nextMonthWeekStart = weekEnd.plusDays(1);
                LocalDate nextMonthWeekEnd = nextMonthWeekStart.plusDays(6);

                // Đảm bảo rằng tuần không vượt quá ngày cuối của tháng tiếp theo
                if (nextMonthWeekEnd.isAfter(nextMonthStartDate.withDayOfMonth(nextMonthStartDate.lengthOfMonth()))) {
                    nextMonthWeekEnd = nextMonthStartDate.withDayOfMonth(nextMonthStartDate.lengthOfMonth());
                }

                String nextMonthWeekRange = "Week " + weekNumber + ": " + nextMonthWeekStart.format(formatter) + " - " + nextMonthWeekEnd.format(formatter);
                weeks.add(nextMonthWeekRange);
            }

            // Chuyển đến tuần tiếp theo
            currentStartDate = weekEnd.plusDays(1);
        }

        return weeks;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<String> getWeeksForCurrentMonth() {
        LocalDate today = LocalDate.now();
        this.year = today.getYear();
        this.month = today.getMonthValue();
        return getWeeksInMonth();
    }
}
