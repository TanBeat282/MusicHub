package com.tandev.musichub.helper.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Helper {

    public static void changeStatusBarColor(Activity activity, int colorResId) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, colorResId));
    }

    public static void changeNavigationColor(Activity activity, int colorResId, boolean lightIcons) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Change the navigation bar color
        window.setNavigationBarColor(ContextCompat.getColor(activity, colorResId));

        // If light icons are desired
        int flags = window.getDecorView().getSystemUiVisibility();
        if (lightIcons) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        window.getDecorView().setSystemUiVisibility(flags);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void changeStatusBarTransparent(Activity activity) {
        if (activity == null) return;
        Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
    }


    // convert number to string follow
    @SuppressLint("DefaultLocale")
    public static String convertToIntString(int number) {
        String numberStr = String.valueOf(number);
        int length = numberStr.length();

        if (length <= 3) {
            return numberStr;
        } else if (length <= 6) {
            int thousands = number / 1000;
            double decimal = (double) number % 1000 / 100;
            return String.format("%,d.%dK", thousands, Math.round(decimal));
        } else if (length <= 9) {
            int millions = number / 1000000;
            return String.format("%,dM", millions);
        } else {
            return String.format("%,d.##B", number);

        }
    }


    // convert time to text
    public static String convertLongToTime2(String timeInMillis) {
        long time = Long.parseLong(timeInMillis);
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Lấy thời gian hiện tại
        Calendar today = Calendar.getInstance();

        // Kiểm tra khoảng cách giữa thời gian hiện tại và thời gian được chuyển đổi
        long differenceInMillis = today.getTimeInMillis() - calendar.getTimeInMillis();

        // Chuyển đổi khoảng cách thành ngày
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

        // Định dạng lại ngày tháng năm
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(date);

        // Xác định văn bản tương ứng
        String text = "";
        if (differenceInDays == 0) {
            text = "Hôm nay";
        } else if (differenceInDays == 1) {
            text = "Hôm qua";
        } else if (differenceInDays > 1 && differenceInDays < 7) {
            text = differenceInDays + " ngày trước";
        } else if (differenceInDays >= 7 && differenceInDays < 14) {
            text = "1 tuần trước";
        } else if (differenceInDays >= 14 && differenceInDays < 21) {
            text = "2 tuần trước";
        } else {
            text = formattedDate;
        }

        // Trả về kết quả
        return text;
    }

    public static String convertLongToTime(String timeInMillis) {
        try {
            long time = Long.parseLong(timeInMillis);

            // Kiểm tra giá trị time để tránh lỗi thời gian Unix Epoch (năm 1970)
            if (time <= 0) {
                return "Invalid time";
            }

            Date date = new Date(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // Định dạng lại ngày tháng năm
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            // Trả về kết quả
            return sdf.format(date);
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    //check type of url search recommend
    public static String getType(String url) {
        if (url.contains("/album/")) {
            return "album";
        } else if (url.contains("/hub/")) {
            return "hub";
        } else {
            return "unknown";
        }
    }

    // get endCodeId form link
    public static String extractEndCodeID(String url) {
        int startIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf(".html");
        if (endIndex == -1 || startIndex >= endIndex) {
            return "Invalid URL";
        }
        return url.substring(startIndex, endIndex);
    }


    // convert quality image
    public static String changeQualityUrl(String originalUrl) {
        return originalUrl.replace("w240", "w1080");
    }

    // convert duration to minutes and seconds
    @SuppressLint("DefaultLocale")
    public static String convertDurationToMinutesAndSeconds(int durationInSeconds) {
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;

        // Sử dụng String.format để đảm bảo phút và giây đều có 2 chữ số
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String normalizeString(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }

    //check playlist user
    public static boolean isPlaylistUser(String endCodeId) {
        if (endCodeId == null) {
            return false;
        }
        String regex = "^playlist\\d+$";
        return endCodeId.matches(regex);
    }

    // convert timestamp to dd-MM-yyyy HH:mm
    public static String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(date);
    }
}
