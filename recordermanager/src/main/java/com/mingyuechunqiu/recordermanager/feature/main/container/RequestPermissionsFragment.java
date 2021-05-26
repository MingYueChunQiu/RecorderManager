package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.mingyuechunqiu.recordermanager.BuildConfig;
import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.data.exception.RecorderManagerException;
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerProvider;
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultCallback;
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
public class RequestPermissionsFragment extends Fragment {

    private RecordVideoRequestOption mOption;
    private RMRecordVideoResultCallback mResultCallback;
    @Nullable
    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mResultCallback == null) {
            removeRequestPermissionPage();
            throw new IllegalArgumentException("RMRecordVideoResultCallback must be set firstly!");
        }
        new ViewModelProvider(this).get(RMRequestPermissionFragmentViewModel.class).updateRecordVideoResultCallback(mResultCallback);
        Bundle args = getArguments();
        if (args != null) {
            mOption = args.getParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION);
        }
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContract<Intent, RecordVideoResultInfo>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Intent input) {
                return input.putExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION, mOption);
            }

            @Nullable
            @Override
            public RecordVideoResultInfo parseResult(int resultCode, @Nullable Intent intent) {
                if (resultCode != Activity.RESULT_OK) {
                    return null;
                }
                if (intent == null) {
                    return null;
                }
                return RecorderManagerProvider.getRecordVideoResultParser().parseRecordVideoResult(intent);
            }
        }, result -> {
            RMRecordVideoResultCallback callback = new ViewModelProvider(RequestPermissionsFragment.this).get(RMRequestPermissionFragmentViewModel.class).getRecordVideoResultCallback();
            if (callback == null) {
                if (BuildConfig.DEBUG) {
                    Log.e("RecorderManager", "RequestPermissionFragment onActivityResult: RMRecordVideoResultCallback == null");
                }
                removeRequestPermissionPage();
                return;
            }
            if (result == null) {
                callback.onFailure(new RecorderManagerException(RecorderManagerConstants.ErrorCode.CODE_RECORD_VIDEO_RESULT_IS_EMPTY, "RecordVideoResultInfo == null"));
            } else {
                callback.onResponseRecordVideoResult(result);
            }
            //处理完回调事件后，再将自身移除
            removeRequestPermissionPage();
        });
        RecordPermissionUtils.checkOrRequestRecordVideoPermissions(this, new RecordPermissionUtils.RequestPermissionCallback() {
            @Override
            public void onPermissionsGranted() {
                startRecordVideoPage();
            }

            @Override
            public void onPermissionsDenied() {
                removeRequestPermissionPage();
            }
        });
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
        if (mActivityResultLauncher == null) {
            if (BuildConfig.DEBUG) {
                Log.e("RecorderManager", "RequestPermissionFragment startRecordVideoPage: mActivityResultLauncher == null");
            }
            return;
        }
        Context context = getContext();
        if (context == null) {
            return;
        }
        mActivityResultLauncher.launch(new Intent(context, RecordVideoActivity.class));
    }

    /**
     * 从父布局中移除申请权限界面
     */
    private void removeRequestPermissionPage() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        FragmentManager fragmentManager = null;
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            fragmentManager = parentFragment.getChildFragmentManager();
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                fragmentManager = activity.getSupportFragmentManager();
            }
        }
        if (fragmentManager == null) {
            Toast.makeText(context, context.getString(R.string.rm_error_start_record_video_page), Toast.LENGTH_SHORT).show();
            return;
        }
        fragmentManager
                .beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }

    @NonNull
    public static RequestPermissionsFragment newInstance(@Nullable RecordVideoRequestOption option, @NonNull RMRecordVideoResultCallback callback) {
        RequestPermissionsFragment fragment = new RequestPermissionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECORD_VIDEO_REQUEST_OPTION, option);
        fragment.setArguments(args);
        fragment.mResultCallback = callback;
        return fragment;
    }
}
