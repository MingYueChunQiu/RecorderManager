package com.mingyuechunqiu.recordermanager.feature.record;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;
import com.mingyuechunqiu.recordermanager.feature.interpect.RecorderManagerIntercept;
import com.mingyuechunqiu.recordermanager.feature.interpect.IRecorderManagerInterceptor;
import com.mingyuechunqiu.recordermanager.util.CameraParamsUtils;

import java.io.IOException;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType.CAMERA_BACK;
import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType.CAMERA_FRONT;
import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType.CAMERA_NOT_SET;

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
class RecorderManager implements IRecorderManager {

    private IRecorderHelper mRecorderHelper;
    private Camera mCamera;
    private RecorderManagerConstants.CameraType mCameraType;
    private IRecorderManagerInterceptor mIntercept;

    RecorderManager(@NonNull IRecorderHelper helper) {
        this(helper, null);
    }

    RecorderManager(@NonNull IRecorderHelper helper, @Nullable IRecorderManagerInterceptor intercept) {
        mRecorderHelper = helper;
        checkOrCreateDefaultRecorderable();
        mCameraType = CAMERA_NOT_SET;
        mIntercept = intercept;
        if (mIntercept == null) {
            mIntercept = new RecorderManagerIntercept();
        }
    }

    @Override
    public boolean recordAudio(@NonNull String path) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordAudio(path);
        }
        return mRecorderHelper.recordAudio(path);
    }

    @Override
    public boolean recordAudio(@NonNull RecorderOption option) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordAudio(option);
        }
        return mRecorderHelper.recordAudio(option);
    }

    @Override
    public boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable String path) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordVideo(camera, surface, path);
        }
        return mRecorderHelper.recordVideo(camera, surface, path);
    }

    @Override
    public boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable RecorderOption option) {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mIntercept.recordVideo(camera, surface, option);
        }
        return mRecorderHelper.recordVideo(camera, surface, option);
    }

    @Override
    public void release() {
        if (mIntercept != null) {
            mIntercept.release();
            mIntercept = null;
        }
        if (mRecorderHelper != null) {
            mRecorderHelper.release();
            mRecorderHelper = null;
        }
        releaseCamera();
        mCameraType = CAMERA_NOT_SET;
    }

    @NonNull
    @Override
    public MediaRecorder getMediaRecorder() {
        checkOrCreateDefaultRecorderable();
        MediaRecorder recorder = mRecorderHelper.getMediaRecorder();
        if (mIntercept != null) {
            recorder = mIntercept.getMediaRecorder(recorder);
        }
        return recorder;
    }

    @Nullable
    @Override
    public RecorderOption getRecorderOption() {
        checkOrCreateDefaultRecorderable();
        RecorderOption option = mRecorderHelper.getRecorderOption();
        if (mIntercept != null) {
            option = mIntercept.getRecorderOption(option);
        }
        return option;
    }

    @Override
    public void setRecorderable(@NonNull IRecorderHelper helper) {
        if (mIntercept != null) {
            mRecorderHelper = mIntercept.setRecorderable(helper);
        } else {
            mRecorderHelper = helper;
        }
    }

    @NonNull
    @Override
    public IRecorderHelper getRecorderable() {
        checkOrCreateDefaultRecorderable();
        if (mIntercept != null) {
            mRecorderHelper = mIntercept.getRecorderable(mRecorderHelper);
        }
        return mRecorderHelper;
    }

    /**
     * 初始化相机对象（默认使用后置摄像头）
     *
     * @return 返回设置好参数的相机
     */
    @Nullable
    @Override
    public Camera initCamera(@NonNull SurfaceHolder holder) {
        if (mIntercept != null) {
            Camera camera = mIntercept.initCamera(holder);
            if (camera != null) {
                mCamera = camera;
                return mCamera;
            }
        }
        return initCamera(CAMERA_BACK, holder);
    }

    @Nullable
    @Override
    public Camera initCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder) {
        if (mIntercept != null) {
            Camera camera = mIntercept.initCamera(cameraType, holder);
            if (camera != null) {
                mCamera = camera;
                return mCamera;
            }
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
            } else if ((cameraType == CAMERA_NOT_SET ||
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
    public boolean switchFlashlight(boolean turnOn) {
        if (mCamera == null) {
            return false;
        }
        if (mIntercept != null) {
            mIntercept.switchFlashlight(turnOn);
        }
        //getParameters failed (empty parameters) 需要加上锁保证每个时间最多只有一个线程访问Camera
        //声明Parameters前加lock，防止其他的线程访问，在 camera.setParameters(parameters); 后面设置unlock
        mCamera.lock();
        Camera.Parameters parameters = mCamera.getParameters();
        //如果不支持闪光灯设置，则直接返回
        if (parameters.getFlashMode() == null) {
            mCamera.unlock();
            return false;
        }
        parameters.setFlashMode(turnOn ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
        mCamera.unlock();
        return true;
    }

    @Nullable
    @Override
    public Camera flipCamera(@NonNull SurfaceHolder holder) {
        if (mIntercept != null) {
            Camera camera = mIntercept.flipCamera(holder);
            if (camera != null) {
                mCamera = camera;
                return mCamera;
            }
        }
        return flipCamera(mCameraType == CAMERA_BACK ? CAMERA_FRONT : CAMERA_BACK, holder);
    }

    @Nullable
    @Override
    public Camera flipCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder) {
        if (mCameraType == cameraType) {
            return null;
        }
        if (mIntercept != null) {
            Camera camera = mIntercept.flipCamera(cameraType, holder);
            if (camera != null) {
                mCamera = camera;
                return mCamera;
            }
        }
        return initCamera(cameraType, holder);
    }

    @NonNull
    @Override
    public RecorderManagerConstants.CameraType getCameraType() {
        if (mIntercept != null) {
            mCameraType = mIntercept.getCameraType(mCameraType);
        }
        return mCameraType == null ? CAMERA_NOT_SET : mCameraType;
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
    private void initCameraParameters(@NonNull SurfaceHolder holder, int cameraId) {
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
        if (mRecorderHelper == null) {
            mRecorderHelper = new RecorderHelper();
        }
    }
}
