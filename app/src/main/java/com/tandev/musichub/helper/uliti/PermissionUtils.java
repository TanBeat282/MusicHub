package com.tandev.musichub.helper.uliti;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtils implements EasyPermissions.PermissionCallbacks {

    private Context context;
    private PermissionCallback callback;

    public PermissionUtils(Context context, PermissionCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void checkAndRequestPermissions(int requestCode, String... perms) {
        if (EasyPermissions.hasPermissions(context, perms)) {
            callback.onPermissionsGranted(requestCode, List.of(perms));
        } else {
            EasyPermissions.requestPermissions((Activity) context, "Ứng dụng cần quyền truy cập bộ nhớ để tải bài hát!", requestCode, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        callback.onPermissionsGranted(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        callback.onPermissionsDenied(requestCode, perms);
    }

    public interface PermissionCallback {
        void onPermissionsGranted(int requestCode, List<String> perms);
        void onPermissionsDenied(int requestCode, List<String> perms);
    }
}
