package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.framework.KeyBackCallback;
import com.mingyuechunqiu.recordermanager.ui.fragment.BasePresenterFragment;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.SUFFIX_MP4;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频界面
 *              继承自Fragment
 *     version: 1.0
 * </pre>
 */
public class RecordVideoFragment extends BasePresenterFragment<RecordVideoContract.View<RecordVideoContract.Presenter>, RecordVideoContract.Presenter>
        implements RecordVideoContract.View<RecordVideoContract.Presenter>, View.OnClickListener, SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks {

    private AppCompatTextView tvTiming;
    private CircleProgressButton cpbRecord;
    private AppCompatImageView ivFlipCamera, ivPlay, ivCancel, ivConfirm, ivBack;

    private RecordVideoOption mOption;
    private boolean isSurfaceHolderDestroyed;//标记SurfaceHolder是否被销毁了

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_fragment_record_video, container, false);
        SurfaceView svVideo = view.findViewById(R.id.sv_record_video_screen);
        tvTiming = view.findViewById(R.id.tv_record_video_timing);
        cpbRecord = view.findViewById(R.id.cpb_record_video_record);
        ivFlipCamera = view.findViewById(R.id.iv_record_video_flip_camera);
        ivPlay = view.findViewById(R.id.iv_record_video_play);
        ivCancel = view.findViewById(R.id.iv_record_video_cancel);
        ivConfirm = view.findViewById(R.id.iv_record_video_confirm);
        ivBack = view.findViewById(R.id.iv_record_video_back);
        svVideo.getHolder().addCallback(this);
        svVideo.getHolder().setKeepScreenOn(true);
        svVideo.setOnClickListener(this);
        tvTiming.setText(getString(R.string.fill_record_timing, "00"));
        cpbRecord.setMaxProgress(mOption.getMaxDuration());
        cpbRecord.setOnCircleProgressButtonListener(new CircleProgressButton.OnCircleProgressButtonListener() {
            @Override
            public boolean onPreProgress(CircleProgressButton v) {
                if (!checkHasPermissions() || mPresenter == null) {
                    return false;
                }
                return mPresenter.pressToStartRecordVideo();
            }

            @Override
            public void onProgress(CircleProgressButton v, float progress) {

            }

            @Override
            public int onReleaseProgress(CircleProgressButton v) {
                if (mPresenter != null) {
                    mPresenter.releaseToStopRecordVideo(false);
                }
                return 360;
            }

            @Override
            public boolean onCancelProgress(CircleProgressButton v) {
                if (mPresenter != null) {
                    mPresenter.releaseToStopRecordVideo(true);
                }
                return false;
            }
        });
        ivFlipCamera.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivConfirm.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        if (getActivity() instanceof KeyBackCallback) {
            ((KeyBackCallback) getActivity()).addOnKeyBackListener(new RecordVideoActivity.OnKeyBackListener() {
                @Override
                public boolean onClickKeyBack(KeyEvent event) {
                    if (mPresenter != null) {
                        mPresenter.onClickBack();
                        return true;
                    }
                    return false;
                }
            });
        }
        checkHasPermissions();
        mPresenter.initView(tvTiming, svVideo, cpbRecord, ivFlipCamera, ivPlay,
                ivCancel, ivConfirm, ivBack, mOption);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在红米Note5A上，锁屏没有调用surfaceDestroyed方法，所以要加以判断
        if (!isSurfaceHolderDestroyed && mPresenter != null) {
            mPresenter.resumePlayVideo(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pausePlayVideo(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOption = null;
        isSurfaceHolderDestroyed = false;
    }

    @Override
    protected RecordVideoContract.Presenter initPresenter() {
        return new RecordVideoPresenter();
    }

    @Override
    protected void releaseOnDestroyView() {

    }

    @Override
    protected void releaseOnDestroy() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sv_record_video_screen) {
            if (mPresenter != null) {
                mPresenter.controlPlayOrPauseVideo();
            }
        } else if (id == R.id.iv_record_video_flip_camera) {
            if (mPresenter != null) {
                mPresenter.flipCamera();
            }
        } else if (id == R.id.iv_record_video_play) {
            if (mPresenter != null) {
                mPresenter.resumePlayVideo(true);
            }
        } else if (id == R.id.iv_record_video_cancel) {
            if (mPresenter != null) {
                mPresenter.resetResource();
                mPresenter.onClickCancel();
            }
        } else if (id == R.id.iv_record_video_confirm) {
            if (mPresenter != null) {
                mPresenter.onClickConfirm();
            }
        } else if (id == R.id.iv_record_video_back) {
            if (mPresenter != null) {
                mPresenter.onClickBack();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mPresenter != null) {
            mPresenter.onSurfaceCreated(holder);
        }
        isSurfaceHolderDestroyed = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setKeepScreenOn(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPresenter != null) {
            mPresenter.releaseCamera();
        }
        isSurfaceHolderDestroyed = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void setPresenter(@NonNull RecordVideoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(String filePath) {
        return newInstance(filePath, 30);
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath    存储文件路径
     * @param maxDuration 最大时长（秒数）
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(String filePath, int maxDuration) {
        return newInstance(new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(filePath))
                .setMaxDuration(maxDuration)
                .build());
    }

    /**
     * 获取录制视频Fragment实例
     *
     * @param option 录制配置信息对象
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(RecordVideoOption option) {
        RecordVideoFragment fragment = new RecordVideoFragment();
        fragment.mOption = option;
        if (fragment.mOption == null) {
            fragment.mOption = new RecordVideoOption();
        }
        if (fragment.mOption.getRecorderOption() == null && fragment.getContext() != null) {
            File file = fragment.getContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (file != null) {
                fragment.mOption.setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(
                        file.getAbsolutePath() +
                                File.separator + System.currentTimeMillis() + SUFFIX_MP4));
            }
        }
        return fragment;
    }

    /**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    public AppCompatTextView getTimingView() {
        return tvTiming;
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    public CircleProgressButton getCircleProgressButton() {
        return cpbRecord;
    }

    /**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    public AppCompatImageView getFlipCameraView() {
        return ivFlipCamera;
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    public AppCompatImageView getPlayView() {
        return ivPlay;
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    public AppCompatImageView getCancelView() {
        return ivCancel;
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    public AppCompatImageView getConfirmView() {
        return ivConfirm;
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    public AppCompatImageView getBackView() {
        return ivBack;
    }

    /**
     * 检测是否已经获取到权限
     *
     * @return 如果获取到权限返回true，否则返回false
     */
    private boolean checkHasPermissions() {
        return RecordPermissionUtils.checkRecordPermissions(this);
    }
}
