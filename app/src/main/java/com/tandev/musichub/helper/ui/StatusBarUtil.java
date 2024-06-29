package com.tandev.musichub.helper.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.Window;

public class StatusBarUtil {

    // Thiết lập thanh trạng thái với màu nền tùy chỉnh
    public static void setStatusBarColor(Activity activity, int colorResId) {
        if (activity == null) return;
        Window window = activity.getWindow();
        window.setStatusBarColor(activity.getResources().getColor(colorResId));
    }

    // Thiết lập thanh trạng thái trong suốt
    public static void setStatusBarTransparent(Activity activity) {
        if (activity == null) return;
        Window window = activity.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
    }
}
