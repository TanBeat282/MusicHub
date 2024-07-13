package com.tandev.musichub.helper.ui;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.tandev.musichub.service.MyService;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private final Context context;  // Tham chiếu đến Context của ứng dụng

    public SwipeGestureListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffX > 0) {
                // Swipe Right
                onSwipeRight();
            } else {
                // Swipe Left
                onSwipeLeft();
            }
            return true;
        }
        return false;
    }

    private void onSwipeRight() {
        sendActionToService(MyService.ACTION_PREVIOUS);
    }

    private void onSwipeLeft() {
        sendActionToService(MyService.ACTION_NEXT);
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("action_music_service", action);
        context.startService(intent);
    }
}
