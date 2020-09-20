package com.mingyuechunqiu.recordermanager.feature.interpect;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.feature.record.IRecorderHelper;

import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   : 录制管理器拦截类
 *              实现IRecorderManagerInterceptor
 *     version: 1.0
 * </pre>
 */
public class RecorderManagerIntercept implements IRecorderManagerInterceptor {

    @NonNull
    @Override
    public IRecorderHelper setRecorderHelper(@NonNull IRecorderHelper helper) {
        return helper;
    }

    @NonNull
    @Override
    public IRecorderHelper getRecorderHelper(@NonNull IRecorderHelper helper) {
        return helper;
    }

    @Nullable
    @Override
    public Camera initCamera(@NonNull SurfaceHolder holder) {
        return null;
    }

    @Nullable
    @Override
    public Camera initCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder) {
        return null;
    }

    @Override
    public void switchFlashlight(boolean turnOn) {
    }

    @Nullable
    @Override
    public Camera flipCamera(@NonNull SurfaceHolder holder) {
        return null;
    }

    @Nullable
    @Override
    public Camera flipCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder) {
        return null;
    }

    @NonNull
    @Override
    public RecorderManagerConstants.CameraType getCameraType(@NonNull RecorderManagerConstants.CameraType cameraType) {
        return cameraType;
    }

    @Override
    public void releaseCamera() {
    }

    @Override
    public void recordAudio(@NonNull String path) {
    }

    @Override
    public void recordAudio(@NonNull RecorderOption option) {
    }

    @Override
    public void recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable String path) {
    }

    @Override
    public void recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable RecorderOption option) {
    }

    @Override
    public void release() {
    }

    @NonNull
    @Override
    public MediaRecorder getMediaRecorder(@NonNull MediaRecorder recorder) {
        return recorder;
    }

    @Nullable
    @Override
    public RecorderOption getRecorderOption(@Nullable RecorderOption option) {
        return option;
    }

    @Override
    public boolean interceptSettingPreviewSize(List<Camera.Size> list) {
        return false;
    }

    @Override
    public boolean interceptSettingPictureSize(List<Camera.Size> list) {
        return false;
    }

    @Override
    public int interceptCameraDisplayOrientation(int degrees) {
        return degrees;
    }
}
