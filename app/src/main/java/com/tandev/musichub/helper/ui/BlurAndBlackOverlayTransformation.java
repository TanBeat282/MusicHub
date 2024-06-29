package com.tandev.musichub.helper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class BlurAndBlackOverlayTransformation extends BitmapTransformation {

    private static final String ID = "com.example.BlurAndBlackOverlayTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final Context context;
    private final int blurRadius;
    private final int overlayOpacity;

    public BlurAndBlackOverlayTransformation(Context context, int blurRadius, int overlayOpacity) {
        this.context = context;
        this.blurRadius = blurRadius;
        this.overlayOpacity = overlayOpacity;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap blurredBitmap = blurBitmap(pool, toTransform, blurRadius);
        return addBlackOverlay(blurredBitmap, overlayOpacity);
    }

    private Bitmap blurBitmap(BitmapPool pool, Bitmap bitmap, int radius) {
        // Sử dụng lớp BlurTransformation để làm mờ bitmap
        return new BlurTransformation(context, radius).transform(pool, bitmap, bitmap.getWidth(), bitmap.getHeight());
    }

    private Bitmap addBlackOverlay(Bitmap bitmap, int opacity) {
        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAlpha(opacity);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        return overlayBitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BlurAndBlackOverlayTransformation) {
            BlurAndBlackOverlayTransformation other = (BlurAndBlackOverlayTransformation) o;
            return blurRadius == other.blurRadius && overlayOpacity == other.overlayOpacity;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + blurRadius * 1000 + overlayOpacity * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(blurRadius).array());
        messageDigest.update(ByteBuffer.allocate(4).putInt(overlayOpacity).array());
    }
}
