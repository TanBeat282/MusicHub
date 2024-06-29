package com.tandev.musichub.helper.uliti;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class DownloadAudio {
    private Context context;
    // Khai báo biến downloadID để lưu ID của quá trình tải xuống
    private long downloadID;

    public DownloadAudio(Context context) {
        this.context = context;
    }

    public long getDownloadID() {
        return downloadID;
    }

    public void downloadAudio(String url, String fileName) {
        File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        if (!musicFolder.exists()) {
            musicFolder.mkdirs();
        }

        fileName += ".mp3";
        File destinationFile = new File(musicFolder, fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("Đang lưu...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationUri(Uri.fromFile(destinationFile));

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // Lưu downloadID được trả về bởi enqueue
        downloadID = downloadManager.enqueue(request);
    }
}
