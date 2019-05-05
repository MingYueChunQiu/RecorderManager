package com.mingyuechunqiu.recordermanager.feature.interpect;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.mingyuechunqiu.recordermanager.data.constants.Constants;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.feature.record.Recorderable;

import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   : 录制管理器拦截类
 *              实现RecorderManagerInterceptable
 *     version: 1.0
 * </pre>
 */
public class RecorderManagerIntercept implements RecorderManagerInterceptable {

    @Override
    public void setRecorderable(Recorderable recorderable) {
    }

    @Override
    public Recorderable getRecorderable() {
        return null;
    }

    @Override
    public Camera initCamera(SurfaceHolder holder) {
        return null;
    }

    @Override
    public Camera initCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        return null;
    }

    @Override
    public Camera flipCamera(SurfaceHolder holder) {
        return null;
    }

    @Override
    public Camera flipCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        return null;
    }

    @Override
    public Constants.CameraType getCameraType() {
        return null;
    }

    @Override
    public void releaseCamera() {
    }

    @Override
    public boolean recordAudio(String path) {
        return false;
    }

    @Override
    public boolean recordAudio(RecorderOption option) {
        return false;
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        return false;
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderOption option) {
        return false;
    }

    @Override
    public void release() {
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        return null;
    }

    @Override
    public RecorderOption getRecorderOption() {
        return null;
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
