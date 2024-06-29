package com.tandev.musichub.helper.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class BlurTransformation extends BitmapTransformation {

    private static final String ID = "com.example.BlurTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final Context context;
    private final int radius;

    public BlurTransformation(Context context, int radius) {
        this.context = context;
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return blurBitmap(toTransform, radius);
    }

    private Bitmap blurBitmap(Bitmap bitmap, int radius) {
        Bitmap blurredBitmap = bitmap.copy(bitmap.getConfig(), true);

        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, input.getElement());
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        rs.destroy();
        return blurredBitmap;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlurTransformation && ((BlurTransformation) o).radius == radius;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 1000;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        messageDigest.update(ByteBuffer.allocate(4).putInt(radius).array());
    }
}
