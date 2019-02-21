package com.mingyuechunqiu.recordermanager.record;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.mingyuechunqiu.recordermanager.constants.Constants;

import java.io.IOException;
import java.util.List;

import static com.mingyuechunqiu.recordermanager.constants.Constants.CameraType.CAMERA_BACK;
import static com.mingyuechunqiu.recordermanager.constants.Constants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.constants.Constants.CameraType.CAMERA_NOT_SET;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制管理类，用于调用具体的录制实现类，并提供公共方法
 *              实现RecorderManagerable
 *     version: 1.0
 * </pre>
 */
class RecorderManager implements RecorderManagerable {

    private Recorderable mRecorderable;
    private Camera mCamera;
    private Constants.CameraType mCameraType;

    RecorderManager(Recorderable recorderable) {
        mRecorderable = recorderable;
        if (mRecorderable == null) {
            mRecorderable = new RecorderHelper();
        }
        mCameraType = CAMERA_NOT_SET;
    }

    @Override
    public boolean recordAudio(String path) {
        return mRecorderable.recordAudio(path);
    }

    @Override
    public boolean recordAudio(RecorderOption option) {
        return mRecorderable.recordAudio(option);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        return mRecorderable.recordVideo(camera, surface, path);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderOption option) {
        return mRecorderable.recordVideo(camera, surface, option);
    }

    @Override
    public void release() {
        mRecorderable.release();
        mRecorderable = null;
        releaseCamera();
        mCameraType = CAMERA_NOT_SET;
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        return mRecorderable.getMediaRecorder();
    }

    @Override
    public RecorderOption getRecorderOption() {
        return mRecorderable.getRecorderOption();
    }

    @Override
    public void setRecorderable(Recorderable recorderable) {
        if (recorderable == null) {
            return;
        }
        mRecorderable = recorderable;
    }

    @Override
    public Recorderable getRecorderable() {
        return mRecorderable;
    }

    /**
     * 初始化相机对象（默认使用后置摄像头）
     *
     * @return 返回设置好参数的相机
     */
    @Override
    public Camera initCamera(SurfaceHolder holder) {
        return initCamera(CAMERA_BACK, holder);
    }

    @Override
    public Camera initCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            //如果是指定前置摄像头，翻转至前置，否则全部指定为后置摄像头
            if (cameraType == CAMERA_FRONT &&
                    cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                releaseCamera();
                mCamera = Camera.open(i);
                mCameraType = CAMERA_FRONT;
                initCameraParameters(holder);
                return mCamera;
            } else if ((cameraType == null || cameraType == CAMERA_NOT_SET ||
                    cameraType == CAMERA_BACK) &&
                    cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                releaseCamera();
                mCamera = Camera.open(i);
                mCameraType = CAMERA_BACK;
                initCameraParameters(holder);
                return mCamera;
            }
        }
        return null;
    }

    @Override
    public Camera flipCamera(SurfaceHolder holder) {
        return flipCamera(mCameraType == CAMERA_BACK ? CAMERA_FRONT : CAMERA_BACK, holder);
    }

    @Override
    public Camera flipCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        if (mCameraType == cameraType) {
            return null;
        }
        return initCamera(cameraType, holder);
    }

    @Override
    public Constants.CameraType getCameraType() {
        return mCameraType;
    }

    /**
     * 释放相机
     */
    @Override
    public void releaseCamera() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.lock();
        mCamera.release();
        mCamera = null;
    }

    /**
     * 初始化相机参数
     *
     * @param holder Surface持有者
     */
    private void initCameraParameters(SurfaceHolder holder) {
        Camera.Parameters parameters = mCamera.getParameters();
        /*有的手机前置摄像头可能不支持变焦，设置对焦模式会崩溃
        isSmoothZoomSupported()返回为false，则不支持变焦，设置zoom出错
        isZoomSupported()返回为true，isSmoothZoomSupported()返回为false，
        可能手机自带的摄像应用支持zoom变焦，只是我们自己的应用就不行了*/
        if (parameters.isSmoothZoomSupported()) {
            //设置对焦模式
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else {
                parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
        }
        //设置使用机器本身所支持的宽高
        int previewWidth = 0, previewHeight = 0;
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : previewSizes) {
            if (size.width > previewWidth && size.height > previewHeight) {
                previewWidth = size.width;
                previewHeight = size.height;
            }
        }
        parameters.setPreviewSize(previewWidth, previewHeight);
        parameters.setPictureSize(previewWidth, previewHeight);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
