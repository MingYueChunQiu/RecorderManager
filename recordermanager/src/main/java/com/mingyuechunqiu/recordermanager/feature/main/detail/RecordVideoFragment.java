package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoButtonOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.feature.main.container.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.framework.KeyBackCallback;
import com.mingyuechunqiu.recordermanager.ui.fragment.BasePresenterFragment;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;
import com.mingyuechunqiu.recordermanager.util.FilePathUtils;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.DEFAULT_RECORD_VIDEO_DURATION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频界面
 *              继承自BasePresenterFragment
 *     version: 1.0
 * </pre>
 */
public class RecordVideoFragment extends BasePresenterFragment<RecordVideoContract.View<RecordVideoContract.Presenter<?>>, RecordVideoContract.Presenter<?>>
        implements RecordVideoContract.View<RecordVideoContract.Presenter<?>>, View.OnClickListener, SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks {

    private SurfaceView svVideo;
    private AppCompatTextView tvTiming;
    private CircleProgressButton cpbRecord;
    private AppCompatImageView ivFlipCamera, ivPlay, ivCancel, ivConfirm, ivBack;
    private AppCompatImageView ivFlashlight;

    private RecordVideoOption mOption;
    private boolean isSurfaceHolderDestroyed;//标记SurfaceHolder是否被销毁了

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_fragment_record_video, container, false);
        svVideo = view.findViewById(R.id.sv_record_video_screen);
        tvTiming = view.findViewById(R.id.tv_record_video_timing);
        cpbRecord = view.findViewById(R.id.cpb_record_video_record);
        ivFlipCamera = view.findViewById(R.id.iv_record_video_flip_camera);
        ivPlay = view.findViewById(R.id.iv_record_video_play);
        ivCancel = view.findViewById(R.id.iv_record_video_cancel);
        ivConfirm = view.findViewById(R.id.iv_record_video_confirm);
        ivBack = view.findViewById(R.id.iv_record_video_back);
        ivFlashlight = view.findViewById(R.id.iv_record_video_flashlight);

        svVideo.getHolder().addCallback(this);
        svVideo.getHolder().setKeepScreenOn(true);
        svVideo.setOnClickListener(this);

        initRecorderOption();
        initCircleProgressButton(mOption.getRecordVideoButtonOption());
        cpbRecord.setMaxProgress(mOption.getMaxDuration());
        cpbRecord.setOnCircleProgressButtonListener(new CircleProgressButton.OnCircleProgressButtonListener() {
            @Override
            public boolean onPreProgress(CircleProgressButton v) {
                if (!checkHasPermissions() || mPresenter == null || ivFlipCamera == null || ivBack == null) {
                    return false;
                }
                return mPresenter.pressToStartRecordVideo(svVideo.getHolder(), ivFlipCamera, ivBack);
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
        mPresenter.initView(mOption);

        ivFlipCamera.setVisibility(mOption.isHideFlipCameraButton() ? View.GONE : View.VISIBLE);
        tvTiming.setText(mPresenter == null ? "" : mPresenter.getTimingHint("00"));
        ivFlashlight.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在红米Note5A上，锁屏没有调用surfaceDestroyed方法，所以要加以判断
        if (!isSurfaceHolderDestroyed && mPresenter != null && ivPlay != null && svVideo != null) {
            mPresenter.resumePlayVideo(false, ivPlay, svVideo.getHolder());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null && ivPlay != null) {
            mPresenter.pausePlayVideo(false, ivPlay);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOption = null;
        isSurfaceHolderDestroyed = false;
    }

    @Override
    protected RecordVideoContract.Presenter<?> initPresenter() {
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
        //库需要使用if else，使用switch判断id会导致问题
        int id = v.getId();
        if (id == R.id.sv_record_video_screen) {
            if (mPresenter != null & ivPlay != null && svVideo != null) {
                mPresenter.controlPlayOrPauseVideo(ivPlay, svVideo.getHolder());
            }
        } else if (id == R.id.iv_record_video_flip_camera) {
            if (mPresenter != null) {
                mPresenter.flipCamera(svVideo.getHolder());
            }
        } else if (id == R.id.iv_record_video_play) {
            if (mPresenter != null && ivPlay != null && svVideo != null) {
                mPresenter.resumePlayVideo(true, ivPlay, svVideo.getHolder());
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
        } else if (id == R.id.iv_record_video_flashlight) {
            ivFlashlight.setSelected(!ivFlashlight.isSelected());
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mPresenter != null && holder != null && ivPlay != null) {
            mPresenter.onSurfaceCreated(holder, ivPlay);
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
    public void showTimingText(@NonNull String text) {
        if (tvTiming == null) {
            return;
        }
        tvTiming.setText(text);
    }

    @Nullable
    @Override
    public SurfaceHolder getSurfaceHolder() {
        return svVideo == null ? null : svVideo.getHolder();
    }

    @Override
    public void controlRecordOrPlayVisibility(boolean isInPlayingState) {
        if (tvTiming == null || ivFlipCamera == null || ivPlay == null || ivCancel == null ||
                ivConfirm == null || cpbRecord == null || ivBack == null) {
            return;
        }
        int playViewsVisibility, recordViewsVisibility;
        if (isInPlayingState) {
            playViewsVisibility = View.VISIBLE;
            recordViewsVisibility = View.GONE;
        } else {
            playViewsVisibility = View.GONE;
            recordViewsVisibility = View.VISIBLE;
            if (mPresenter != null) {
                showTimingText(mPresenter.getTimingHint("00"));
            }
            if (!mOption.isHideFlipCameraButton()) {
                ivFlipCamera.setVisibility(recordViewsVisibility);
            }
            ivPlay.setVisibility(playViewsVisibility);
        }
        ivCancel.setVisibility(playViewsVisibility);
        ivConfirm.setVisibility(playViewsVisibility);
        tvTiming.setVisibility(recordViewsVisibility);
        cpbRecord.setVisibility(recordViewsVisibility);
        ivBack.setVisibility(recordViewsVisibility);
    }

    @Override
    public void setPresenter(@NonNull RecordVideoContract.Presenter<?> presenter) {
        mPresenter = presenter;
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(@Nullable String filePath) {
        return newInstance(filePath, DEFAULT_RECORD_VIDEO_DURATION);
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath    存储文件路径
     * @param maxDuration 最大时长（秒数）
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(@Nullable String filePath, int maxDuration) {
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
    public static RecordVideoFragment newInstance(@Nullable RecordVideoOption option) {
        RecordVideoFragment fragment = new RecordVideoFragment();
        fragment.mOption = option;
        if (fragment.mOption == null) {
            fragment.mOption = new RecordVideoOption();
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

    /**
     * 初始化录制参数信息对象
     */
    private void initRecorderOption() {
        if (mOption.getRecorderOption() == null) {
            mOption.setRecorderOption(new RecorderOption.Builder()
                    .buildDefaultVideoBean(
                            FilePathUtils.getSaveFilePath(getContext())
                    ));
        }
        if (TextUtils.isEmpty(mOption.getRecorderOption().getFilePath())) {
            mOption.getRecorderOption().setFilePath(
                    FilePathUtils.getSaveFilePath(getContext()));
        }
    }

    /**
     * 初始化圆形进度按钮相关属性
     *
     * @param option 录制视频按钮配置信息类
     */
    private void initCircleProgressButton(@Nullable RecordVideoButtonOption option) {
        if (cpbRecord == null || option == null) {
            return;
        }
        if (option.getIdleCircleColor() != 0) {
            cpbRecord.setIdleCircleColor(option.getIdleCircleColor());
        }
        if (option.getPressedCircleColor() != 0) {
            cpbRecord.setPressedCircleColor(option.getPressedCircleColor());
        }
        if (option.getReleasedCircleColor() != 0) {
            cpbRecord.setReleasedCircleColor(option.getReleasedCircleColor());
        }
        if (option.getIdleRingColor() != 0) {
            cpbRecord.setIdleRingColor(option.getIdleRingColor());
        }
        if (option.getPressedRingColor() != 0) {
            cpbRecord.setPressedRingColor(option.getPressedRingColor());
        }
        if (option.getReleasedRingColor() != 0) {
            cpbRecord.setReleasedRingColor(option.getReleasedRingColor());
        }
        if (option.getIdleRingWidth() > 0) {
            cpbRecord.setIdleRingWidth(option.getIdleRingWidth());
        }
        if (option.getPressedRingWidth() > 0) {
            cpbRecord.setPressedRingWidth(option.getPressedRingWidth());
        }
        if (option.getReleasedRingWidth() > 0) {
            cpbRecord.setReleasedRingWidth(option.getReleasedRingWidth());
        }
        if (option.getIdleInnerPadding() > 0) {
            cpbRecord.setIdleInnerPadding(option.getIdleInnerPadding());
        }
        if (option.getPressedInnerPadding() > 0) {
            cpbRecord.setPressedInnerPadding(option.getPressedInnerPadding());
        }
        if (option.getReleasedInnerPadding() > 0) {
            cpbRecord.setReleasedInnerPadding(option.getReleasedInnerPadding());
        }
        cpbRecord.setIdleRingVisible(option.isIdleRingVisible());
        cpbRecord.setPressedRingVisible(option.isPressedRingVisible());
        cpbRecord.setReleasedRingVisible(option.isReleasedRingVisible());
    }
}
