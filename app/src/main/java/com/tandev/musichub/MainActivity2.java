package com.tandev.musichub;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tandev.musichub.helper.ui.BlurTransformation;

public class MainActivity2 extends AppCompatActivity {

    private static final String IMAGE_URL = "https://www.vietnamworks.com/hrinsider/wp-content/uploads/2023/12/anh-dep-thien-nhien-3d-003.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        ImageView foregroundImageView = findViewById(R.id.foregroundImageView);
        FrameLayout foregroundContainer = findViewById(R.id.foregroundContainer);

        // Load and blur the background image
        Glide.with(this)
                .asBitmap()
                .load(IMAGE_URL)
                .transform(new BlurTransformation(this, 25)) // Adjust blur radius as needed
                .into(backgroundImageView);

        // Load the foreground image
        Glide.with(this)
                .load(IMAGE_URL)
                .into(foregroundImageView);

        // Add rounded corners to the foreground image
        foregroundImageView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int radius = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp); // Define the corner radius in your dimens.xml
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        foregroundImageView.setClipToOutline(true);

        // Add a semi-transparent background to the FrameLayout to mimic the blurred edges effect
        foregroundContainer.setBackgroundResource(R.drawable.app); // Create a drawable with rounded corners and the desired semi-transparent color
    }
}