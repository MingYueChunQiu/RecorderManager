package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/6/10
 *     desc   : 请求权限Fragment，空界面，只用于权限申请
 *              继承Fragment
 *     version: 1.0
 * </pre>
 */
public class RequestPermissionFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private RecordVideoRequestOption mOption;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (RecordPermissionUtils.checkRecordPermissions(this)) {
            startRecordVideoPage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        startRecordVideoPage();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    public static RequestPermissionFragment newInstance(RecordVideoRequestOption option) {
        RequestPermissionFragment fragment = new RequestPermissionFragment();
        fragment.mOption = option;
        return fragment;
    }

    /**
     * 打开录制视频界面
     */
    private void startRecordVideoPage() {
        if (getContext() == null) {
            return;
        }
        Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        intent.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, mOption);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }
    }
}
