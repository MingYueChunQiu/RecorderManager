package com.mingyuechunqiu.recordermanager.util;

import android.Manifest;
import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.mingyuechunqiu.recordermanager.R;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

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

    /**
     * 检查是否已经获取相机权限
     *
     * @param activity Activity
     * @return 如果已经获取返回true，否则返回false
     */
    public static boolean checkRecordPermissions(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (!EasyPermissions.hasPermissions(activity, sPermissions)) {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.rm_warn_allow_record_video_permissions), 1, sPermissions);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查是否已经获取相机权限
     *
     * @param fragment Fragment
     * @return 如果已经获取返回true，否则返回false
     */
    public static boolean checkRecordPermissions(Fragment fragment) {
        if (fragment == null || fragment.getContext() == null) {
            return false;
        }
        if (!EasyPermissions.hasPermissions(fragment.getContext(), sPermissions)) {
            EasyPermissions.requestPermissions(fragment, fragment.getString(R.string.rm_warn_allow_record_video_permissions), 1, sPermissions);
            return false;
        } else {
            return true;
        }
    }

    public static void handleOnPermissionDenied(Activity activity) {
        if (activity == null) {
            return;
        }
        showAppSettingsDialog(new AppSettingsDialog.Builder(activity));
    }

    public static void handleOnPermissionDenied(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        showAppSettingsDialog(new AppSettingsDialog.Builder(fragment));
    }

    private static void showAppSettingsDialog(AppSettingsDialog.Builder builder) {
        if (builder == null) {
            return;
        }
        builder.setTitle(R.string.rm_set_permission)
                .setRationale(R.string.rm_warn_allow_record_video_permissions)
                .setPositiveButton(R.string.rm_set)
                .setNegativeButton(R.string.rm_cancel)
                .build().show();
    }
}
