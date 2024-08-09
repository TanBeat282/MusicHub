package com.tandev.musichub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.BuildConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tandev.musichub.helper.ui.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private ProgressBar progress_bar;
    private static final String GITHUB_API_URL = "https://api.github.com/repos/TanBeat282/MusicHub/releases/latest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Helper.changeStatusBarColor(this, R.color.bg);
        Helper.changeNavigationColor(this, R.color.bg, true);

        progress_bar = findViewById(R.id.progress_bar);

        new Handler().postDelayed(() -> {
            progress_bar.setVisibility(View.VISIBLE);
            if (isNetworkAvailable()) {
                new android.os.Handler().postDelayed(() -> {
                    // Kiểm tra phiên bản ứng dụng
                    new CheckVersionTask().execute();
                }, 1000);
            } else {
                showNetworkErrorDialog();
            }
        }, 1000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNetworkErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Không có kết nối mạng")
                .setMessage("Hãy kiểm tra lại mạng và thử lại!")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private class CheckVersionTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(GITHUB_API_URL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject releaseJson) {
            super.onPostExecute(releaseJson);

            if (releaseJson != null) {
                try {
                    // Lấy tag_name từ JSON
                    String latestVersion = releaseJson.getString("tag_name");
                    // Lấy version hiện tại của ứng dụng
                    String currentVersion = BuildConfig.VERSION_NAME;
                    Log.d(">>>>>>>>>>>>>>>>", "latestVersion: "+latestVersion);
                    Log.d(">>>>>>>>>>>>>>>>", "currentVersion: "+currentVersion);

                    // So sánh phiên bản hiện tại với phiên bản mới nhất
                    if (!currentVersion.equals(latestVersion)) {
                        // Lấy nội dung body từ JSON
                        String releaseNotes = releaseJson.getString("body");

                        // Lấy URL tải xuống APK từ JSON
                        JSONArray assetsArray = releaseJson.getJSONArray("assets");
                        String downloadUrl = null;
                        if (assetsArray.length() > 0) {
                            downloadUrl = assetsArray.getJSONObject(0).getString("browser_download_url");
                        }

                        // Hiển thị dialog với nội dung và nút tải xuống
                        showUpdateDialog(releaseNotes, downloadUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showUpdateDialog(String releaseNotes, String downloadUrl) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cập nhật mới")
                .setMessage(releaseNotes) // Nội dung của dialog là phần body từ JSON
                .setPositiveButton("Tải xuống", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (downloadUrl != null) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Chuyển sang MainActivity khi nhấn Hủy
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
}