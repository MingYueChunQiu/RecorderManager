package com.mingyuechunqiu.recordermanager.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mingyuechunqiu.recordermanager.R;

import java.util.Map;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 录制权限工具类
 *     version: 1.0
 * </pre>
 */
public class RecordPermissionUtils {

    private static final String[] sPermissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @NonNull
    public static String[] getRecordVideoPermissions() {
        return sPermissions;
    }

    /**
     * 检测是否已经获取到权限
     *
     * @param activity 界面
     * @param callback 请求权限回调
     * @return 如果获取到权限返回true，否则返回false
     */
    public static boolean checkOrRequestRecordVideoPermissions(@NonNull FragmentActivity activity, @NonNull RequestPermissionCallback callback) {
        boolean hasPermissions = checkHasRecordVideoPermissions(activity);
        if (!hasPermissions) {
            activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> handleRequestPermissionsResult(callback, activity, result))
                    .launch(RecordPermissionUtils.getRecordVideoPermissions());
        }
        return hasPermissions;
    }

    /**
     * 检测是否已经获取到权限
     *
     * @param fragment 界面
     * @param callback 请求权限回调
     * @return 如果获取到权限返回true，否则返回false
     */
    public static boolean checkOrRequestRecordVideoPermissions(@NonNull Fragment fragment, @Nullable RequestPermissionCallback callback) {
        Context context = fragment.getContext();
        if (context == null) {
            return false;
        }
        boolean hasPermissions = checkHasRecordVideoPermissions(context);
        if (!hasPermissions) {
            fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> handleRequestPermissionsResult(callback, context, result))
                    .launch(RecordPermissionUtils.getRecordVideoPermissions());
        } else {
            if (callback != null) {
                callback.onPermissionsGranted();
            }
        }
        return hasPermissions;
    }

    public static boolean checkHasRecordVideoPermissions(@NonNull Context context) {
        boolean hasPermissions = true;
        for (String permission : RecordPermissionUtils.getRecordVideoPermissions()) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                hasPermissions = false;
                break;
            }
        }
        return hasPermissions;
    }

    private static void handleRequestPermissionsResult(@Nullable RequestPermissionCallback callback, @NonNull Context context, @NonNull Map<String, Boolean> result) {
        boolean isAllGranted = true;
        for (Map.Entry<String, Boolean> entry : result.entrySet()) {
            if (!entry.getValue()) {
                isAllGranted = false;
                break;
            }
        }
        if (!isAllGranted) {
            showAppSettingsDialog(context);
            if (callback != null) {
                callback.onPermissionsDenied();
            }
        } else {
            if (callback != null) {
                callback.onPermissionsGranted();
            }
        }
    }

    private static void showAppSettingsDialog(@NonNull Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.rm_set_permission)
                .setMessage(R.string.rm_warn_allow_record_video_permissions)
                .setPositiveButton(R.string.rm_set, (dialog, which) -> startDetailSettingsActivity(context))
                .setNegativeButton(R.string.rm_cancel, (dialog, which) -> {
                })
                .create()
                .show();
    }

    private static void startDetailSettingsActivity(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }


    public interface RequestPermissionCallback {

        void onPermissionsGranted();

        void onPermissionsDenied();
    }
}
