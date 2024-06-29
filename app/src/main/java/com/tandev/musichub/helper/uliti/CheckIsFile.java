package com.tandev.musichub.helper.uliti;

import android.os.Environment;

import java.io.File;

public class CheckIsFile {

    public static boolean isFileDownloaded(String fileName) {
        File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File destinationFile = new File(musicFolder, fileName);
        return destinationFile.exists();
    }

    // Xóa file nếu tồn tại
    public static boolean deleteFileIfExists(String fileName) {
        File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File destinationFile = new File(musicFolder, fileName);
        if (destinationFile.exists()) {
            boolean deleted = destinationFile.delete();
            if (deleted) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
