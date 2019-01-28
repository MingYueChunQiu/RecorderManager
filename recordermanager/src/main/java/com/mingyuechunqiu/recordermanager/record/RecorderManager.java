package com.mingyuechunqiu.recordermanager.record;

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
public class RecorderManager implements RecorderManagerable {

    private Recorderable mRecorderable;

    private RecorderManager(Recorderable recorderable) {
        if (recorderable == null) {
            throw new IllegalArgumentException("recorderable can not be null!");
        }
        mRecorderable = recorderable;
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @return 返回录制管理类实例
     */
    public static RecorderManagerable newInstance() {
        return new RecorderManager(new RecorderHelper());
    }

    /**
     * 创建录制管理类实例
     *
     * @param recorderable 实际录制类
     * @return 返回录制管理类实例
     */
    public static RecorderManagerable newInstance(Recorderable recorderable) {
        return new RecorderManager(recorderable);
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
    public boolean recordAudio(RecorderOption bean) {
        return mRecorderable.recordAudio(bean);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        return mRecorderable.recordVideo(camera, surface, path);
    }

    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderOption bean) {
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
    @Override
    public Camera initCamera() {
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
    @Override
    public void releaseCamera(Camera camera) {
        if (camera == null) {
            return;
        }
        camera.stopPreview();
        camera.lock();
        camera.release();
    }
}
