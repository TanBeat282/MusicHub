package com.tandev.musichub.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
import com.tandev.musichub.adapter.video.select_quality_video.SelectQualityVideoAdapter;
import com.tandev.musichub.model.video.ItemVideoStreaming;

import java.util.ArrayList;

public class BottomSheetSelectQualityVideo extends BottomSheetDialogFragment {
    private final Context context;
    private final Activity activity;
    private int quality;
    private ItemVideoStreaming videoStreamingLinks;
    private BottomSheetDialog bottomSheetDialog;
    private QualityVideoListener mListener;

    private TextView txt_quality;
    private LinearLayout linear_1080p, linear_720p, linear_480p, linear_360p;

    public BottomSheetSelectQualityVideo(Context context, Activity activity, int quality, ItemVideoStreaming videoStreamingLinks) {
        this.context = context;
        this.activity = activity;
        this.quality = quality;
        this.videoStreamingLinks = videoStreamingLinks;
    }

    public interface QualityVideoListener {
        void onQualityVideoSelected(int quality);
    }

    public void setQualityVideoListener(QualityVideoListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_sheet_select_quality_video, null);
        bottomSheetDialog.setContentView(view);

        txt_quality = bottomSheetDialog.findViewById(R.id.txt_quality);
        linear_1080p = bottomSheetDialog.findViewById(R.id.linear_1080p);
        linear_720p = bottomSheetDialog.findViewById(R.id.linear_720p);
        linear_480p = bottomSheetDialog.findViewById(R.id.linear_480p);
        linear_360p = bottomSheetDialog.findViewById(R.id.linear_360p);

        txt_quality.setText(currentQualityVideo(quality));

        linear_1080p.setVisibility(videoStreamingLinks.getP1080().isEmpty() ? View.GONE : View.VISIBLE);
        linear_720p.setVisibility(videoStreamingLinks.getP720().isEmpty() ? View.GONE : View.VISIBLE);
        linear_480p.setVisibility(videoStreamingLinks.getP480().isEmpty() ? View.GONE : View.VISIBLE);
        linear_360p.setVisibility(videoStreamingLinks.getP360().isEmpty() ? View.GONE : View.VISIBLE);


        linear_360p.setOnClickListener(view1 -> {
            if (mListener != null) {
                mListener.onQualityVideoSelected(0);
                dismiss();
            }
        });

        linear_480p.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onQualityVideoSelected(1);
                dismiss();
            }
        });
        linear_720p.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onQualityVideoSelected(2);
                dismiss();
            }
        });
        linear_1080p.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onQualityVideoSelected(3);
                dismiss();
            }
        });


        return bottomSheetDialog;
    }
    //0 360p
    //1 480p
    //2 720p
    //3 1080p

    private String currentQualityVideo(int quality) {
        if (quality == 1) {
            return "480p";
        } else if (quality == 2) {
            return "720p";
        } else if (quality == 3) {
            return "1080p";
        } else {
            return "360p";
        }
    }

}
