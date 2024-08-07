package com.tandev.musichub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.BuildConfig;
import com.tandev.musichub.helper.ui.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
//                    new CheckVersionTask().execute();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
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
    private class CheckVersionTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(GITHUB_API_URL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    return jsonObject.getString("tag_name");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String latestVersion) {
            super.onPostExecute(latestVersion);

            if (latestVersion != null) {
                String currentVersion = BuildConfig.VERSION_NAME;

                if (!currentVersion.equals(latestVersion)) {
                    showUpdateDialog(latestVersion);
                } else {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
                }
            }
        }
    }

    private void showUpdateDialog(String latestVersion) {
        new AlertDialog.Builder(this)
                .setTitle("Update Available")
                .setMessage("A new version (" + latestVersion + ") is available. Please update to the latest version.")
                .setPositiveButton("Update", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TanBeat282/MusicHub/releases"));
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}