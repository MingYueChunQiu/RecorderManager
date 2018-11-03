package com.mingyuechunqiu.recordermanager;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.view.Surface;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制管理类，用于调用具体的录制实现类，并提供公共方法
 *              实现Recorderable
 *     version: 1.0
 * </pre>
 */
public class RecorderManager implements Recorderable {

    private Recorderable mRecorderable;

    @NonNull
    public static RecorderManager getInstance() {
        return new RecorderManager();
    }

    @NonNull
    public static RecorderManager getInstance(Recorderable recorderable) {
        return new RecorderManager(recorderable);
    }

    public RecorderManager() {
        this(new RecorderHelper());
    }

    public RecorderManager(Recorderable recorderable) {
        if (recorderable == null) {
            throw new IllegalArgumentException("recorderable can not be null!");
        }
        mRecorderable = recorderable;
    }

    public Recorderable getRecorderable() {
        return mRecorderable;
    }

    public void setRecorderable(Recorderable recorderable) {
        if (recorderable == null) {
            return;
        }
        mRecorderable = recorderable;
    }

    @Override
    public boolean recordAudio(String path) {
        return mRecorderable.recordAudio(path);
    }

    @Override
    public boolean recordAudio(RecorderBean bean) {
        return mRecorderable.recordAudio(bean);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        return mRecorderable.recordVideo(camera, surface, path);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderBean bean) {
        return mRecorderable.recordVideo(camera, surface, bean);
    }

    @Override
    public void release() {
        mRecorderable.release();
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        return mRecorderable.getMediaRecorder();
    }

    /**
     * 初始化相机对象
     *
     * @return 返回设置好参数的相机
     */
    public static Camera initCamera() {
        Camera camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        //设置对焦模式
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        } else {
            parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
        }
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        return camera;
    }

    /**
     * 释放相机
     *
     * @param camera 相机
     */
    public static void releaseCamera(Camera camera) {
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.lock();
        camera.release();
    }
}
