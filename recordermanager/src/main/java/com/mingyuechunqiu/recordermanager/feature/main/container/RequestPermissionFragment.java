package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerProvider;
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultObserver;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/6/10
 *     desc   : 请求权限Fragment，空界面，只用于权限申请
 *              继承自Fragment
 *     version: 1.0
 * </pre>
 */
public class RequestPermissionFragment extends Fragment {

    private RecordVideoRequestOption mOption;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mOption = args.getParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION);
        }
        if (RecordPermissionUtils.checkOrRequestRecordVideoPermissions(this, this::startRecordVideoPage)) {
            startRecordVideoPage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOption = null;
    }

    /**
     * 打开录制视频界面
     */
    private void startRecordVideoPage() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        RMRecordVideoResultObserver observer = RecorderManagerProvider.getRecordManagerGlobalDataStore().getRecordVideoResultObserver();
        if (observer == null) {
            throw new IllegalArgumentException("must set RMRecordVideoResultCallback first!");
        }
        Intent intent = new Intent(getContext(), RecordVideoActivity.class);
        intent.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, mOption);
        FragmentManager fragmentManager = null;
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            fragmentManager = parentFragment.getChildFragmentManager();
            parentFragment.getViewLifecycleOwner().getLifecycle().addObserver(observer);
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                fragmentManager = activity.getSupportFragmentManager();
                activity.getLifecycle().addObserver(observer);
            }
        }
        startActivity(intent);
        if (fragmentManager == null) {
            Toast.makeText(context, context.getString(R.string.rm_error_start_record_video_page), Toast.LENGTH_SHORT).show();
        }
        removeRequestPermissionPage(fragmentManager);
    }

    /**
     * 从父布局中移除申请权限界面
     *
     * @param fragmentManager 碎片管理器
     */
    private void removeRequestPermissionPage(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        fragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }

    @NonNull
    public static RequestPermissionFragment newInstance(@Nullable RecordVideoRequestOption option, @NonNull RMRecordVideoResultObserver.RMRecordVideoResultCallback callback) {
        RequestPermissionFragment permissionFragment = new RequestPermissionFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION, option);
        permissionFragment.setArguments(args);
        RecorderManagerProvider.getRecordManagerGlobalDataStore().setRecordVideoResultObserver(new RMRecordVideoResultObserver(callback));
        return permissionFragment;
    }
}
