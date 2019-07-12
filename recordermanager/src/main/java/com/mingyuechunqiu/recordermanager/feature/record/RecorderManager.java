package com.mingyuechunqiu.recordermanager.feature.record;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import androidx.core.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.data.constants.Constants;
import com.mingyuechunqiu.recordermanager.feature.interpect.RecorderManagerIntercept;
import com.mingyuechunqiu.recordermanager.feature.interpect.RecorderManagerInterceptable;
import com.mingyuechunqiu.recordermanager.util.CameraParamsUtils;

import java.io.IOException;

import static com.mingyuechunqiu.recordermanager.data.constants.Constants.CameraType.CAMERA_BACK;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.data.constants.Constants.CameraType.CAMERA_NOT_SET;

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
    private RecorderManagerInterceptable mIntercept;

    RecorderManager(Recorderable recorderable) {
        this(recorderable, null);
    }

    RecorderManager(Recorderable recorderable, RecorderManagerInterceptable intercept) {
        mRecorderable = recorderable;
        checkOrCreateDefaultRecorderable();
        mCameraType = CAMERA_NOT_SET;
        mIntercept = intercept;
        if (mIntercept == null) {
            mIntercept = new RecorderManagerIntercept();
        }
    }

    @Override
    public boolean recordAudio(String path) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordAudio(path);
        }
        return mRecorderable.recordAudio(path);
    }

    @Override
    public boolean recordAudio(RecorderOption option) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordAudio(option);
        }
        return mRecorderable.recordAudio(option);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordVideo(camera, surface, path);
        }
        return mRecorderable.recordVideo(camera, surface, path);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderOption option) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordVideo(camera, surface, option);
        }
        return mRecorderable.recordVideo(camera, surface, option);
    }

    @Override
    public void release() {
        if (mIntercept != null) {
            mIntercept.release();
            mIntercept = null;
        }
        if (mRecorderable != null) {
            mRecorderable.release();
            mRecorderable = null;
        }
        releaseCamera();
        mCameraType = CAMERA_NOT_SET;
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.getMediaRecorder();
        }
        return mRecorderable.getMediaRecorder();
    }

    @Override
    public RecorderOption getRecorderOption() {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.getRecorderOption();
        }
        return mRecorderable.getRecorderOption();
    }

    @Override
    public void setRecorderable(Recorderable recorderable) {
        if (mIntercept != null) {
            mIntercept.setRecorderable(recorderable);
        }
        if (recorderable == null) {
            return;
        }
        mRecorderable = recorderable;
    }

    @Override
    public Recorderable getRecorderable() {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.getRecorderable();
        }
        return mRecorderable;
    }

    /**
     * 初始化相机对象（默认使用后置摄像头）
     *
     * @return 返回设置好参数的相机
     */
    @Override
    public Camera initCamera(SurfaceHolder holder) {
        if (mIntercept != null) {
            mIntercept.initCamera(holder);
        }
        return initCamera(CAMERA_BACK, holder);
    }

    @Override
    public Camera initCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        if (mIntercept != null) {
            mIntercept.initCamera(cameraType, holder);
        }
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
                initCameraParameters(holder, i);
                return mCamera;
            } else if ((cameraType == null || cameraType == CAMERA_NOT_SET ||
                    cameraType == CAMERA_BACK) &&
                    cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                releaseCamera();
                mCamera = Camera.open(i);
                mCameraType = CAMERA_BACK;
                initCameraParameters(holder, i);
                return mCamera;
            }
        }
        return null;
    }

    @Override
    public Camera flipCamera(SurfaceHolder holder) {
        if (mIntercept != null) {
            mIntercept.flipCamera(holder);
        }
        return flipCamera(mCameraType == CAMERA_BACK ? CAMERA_FRONT : CAMERA_BACK, holder);
    }

    @Override
    public Camera flipCamera(Constants.CameraType cameraType, SurfaceHolder holder) {
        if (mIntercept != null) {
            mIntercept.flipCamera(cameraType, holder);
        }
        if (mCameraType == cameraType) {
            return null;
        }
        return initCamera(cameraType, holder);
    }

    @Override
    public Constants.CameraType getCameraType() {
        if (mIntercept != null) {
            mIntercept.getCameraType();
        }
        return mCameraType;
    }

    /**
     * 释放相机资源
     */
    @Override
    public void releaseCamera() {
        if (mIntercept != null) {
            mIntercept.releaseCamera();
        }
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        //没有这句翻转前后摄像头时会崩溃，或者切换前置后不调用下次再进，都不能打开相机服务
        try {
            mCamera.reconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.release();
        mCamera = null;
        CameraParamsUtils.getInstance().release();
    }

    /**
     * 初始化相机参数
     *
     * @param holder Surface持有者
     */
    private void initCameraParameters(SurfaceHolder holder, int cameraId) {
        Camera.Parameters parameters = mCamera.getParameters();
        /*有的手机前置摄像头可能不支持变焦，设置对焦模式会崩溃
        isSmoothZoomSupported()返回为false，则不支持变焦，设置zoom出错
        isZoomSupported()返回为true，isSmoothZoomSupported()返回为false，
        可能手机自带的摄像应用支持zoom变焦，只是我们自己的应用就不行了*/
        boolean isCanAutoFocus = false;
        if ((mCameraType == CAMERA_FRONT && parameters.isSmoothZoomSupported()) ||
                (mCameraType == CAMERA_BACK &&
                        (parameters.isSmoothZoomSupported() || parameters.isZoomSupported()))) {
            isCanAutoFocus = true;
        }
        if (isCanAutoFocus) {
            //设置对焦模式
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else {
                parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
        }
        float videoRatio = -1;//视频宽高比
        if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_HIGH)) {
            CamcorderProfile profile = CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            if (profile != null) {
                videoRatio = profile.videoFrameWidth * 1.0f / profile.videoFrameHeight;
            }
        }
        //设置使用机器本身所支持的宽高
        if (mIntercept == null || !mIntercept.interceptSettingPreviewSize(parameters.getSupportedPreviewSizes())) {
            Pair<Integer, Integer> mPreviewSize = CameraParamsUtils.getInstance().getSupportSize(
                    parameters.getSupportedPreviewSizes(), videoRatio);
            if (mPreviewSize != null && mPreviewSize.first != null && mPreviewSize.second != null) {
                parameters.setPreviewSize(mPreviewSize.first, mPreviewSize.second);
            }
        }
        if (mIntercept == null || !mIntercept.interceptSettingPictureSize(parameters.getSupportedPictureSizes())) {
            Pair<Integer, Integer> mPictureSize = CameraParamsUtils.getInstance().getSupportSize(
                    parameters.getSupportedPictureSizes(), 0, 0);
            if (mPictureSize != null && mPictureSize.first != null && mPictureSize.second != null) {
                parameters.setPictureSize(mPictureSize.first, mPictureSize.second);
            }
        }
        mCamera.setParameters(parameters);
        int degrees = 90;//默认竖屏旋转
        if (mIntercept != null) {
            degrees = mIntercept.interceptCameraDisplayOrientation(degrees);
        }
        mCamera.setDisplayOrientation(degrees);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.unlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查或者创建默认的录制对象
     */
    private void checkOrCreateDefaultRecorderable() {
        if (mRecorderable == null) {
            mRecorderable = new RecorderHelper();
        }
    }
}
