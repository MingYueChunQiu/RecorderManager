package com.mingyuechunqiu.recordermanager;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;

import java.io.IOException;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/30
 *     desc   : 调用系统API的录制具体实现类
 *              实现Recorderable
 *     version: 1.0
 * </pre>
 */
public class RecorderHelper implements Recorderable {

    private static MediaRecorder sRecorder;

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (sRecorder != null) {
            sRecorder.stop();
            sRecorder.reset();
            sRecorder.release();
            sRecorder = null;
        }
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        if (sRecorder == null) {
            synchronized (RecorderHelper.class) {
                if (sRecorder == null) {
                    sRecorder = new MediaRecorder();
                }
            }
        }
        return sRecorder;
    }

    /**
     * 录制音频
     *
     * @param path 文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordAudio(String path) {
        return recordAudio(new RecorderBean.Builder().buildDefaultAudioBean(path));
    }

    /**
     * 录制音频
     *
     * @param bean 存储录制信息的对象
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordAudio(RecorderBean bean) {
        if (bean == null) {
            return false;
        }
        resetRecorder();
        sRecorder.setAudioSource(bean.getAudioSource());
        sRecorder.setOutputFormat(bean.getOutputFormat());
        sRecorder.setAudioEncoder(bean.getAudioEncoder());
        sRecorder.setAudioEncodingBitRate(bean.getBitRate());
        if (bean.getMaxDuration() > 0) {
            sRecorder.setMaxDuration(bean.getMaxDuration());
        }
        if (bean.getMaxFileSize() > 0) {
            sRecorder.setMaxFileSize(bean.getMaxFileSize());
        }
        sRecorder.setOutputFile(bean.getFilePath());
        try {
            sRecorder.prepare();
            sRecorder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param path    文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordVideo(Camera camera, Surface surface, String path) {
        return recordVideo(camera, surface, new RecorderBean.Builder().buildDefaultVideoBean(path));
    }

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param bean    存储录制信息的对象
     * @return 返回是否成功开启视频录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderBean bean) {
        if (camera == null || surface == null || bean == null) {
            return false;
        }
        resetRecorder();
        sRecorder.setCamera(camera);
        sRecorder.setAudioSource(bean.getAudioSource());
        sRecorder.setVideoSource(bean.getVideoSource());
        sRecorder.setOutputFormat(bean.getOutputFormat());
        sRecorder.setAudioEncoder(bean.getAudioEncoder());
        sRecorder.setVideoEncoder(bean.getVideoEncoder());
        if (bean.getBitRate() > 0) {
            sRecorder.setVideoEncodingBitRate(bean.getBitRate());
        }
        if (bean.getFrameRate() > 0) {
            sRecorder.setVideoFrameRate(bean.getFrameRate());
        }
        if (bean.getVideoWidth() > 0 && bean.getVideoHeight() > 0) {
            sRecorder.setVideoSize(bean.getVideoWidth(), bean.getVideoHeight());
        }
        if (bean.getMaxDuration() > 0) {
            sRecorder.setMaxDuration(bean.getMaxDuration());
        }
        if (bean.getMaxFileSize() > 0) {
            sRecorder.setMaxFileSize(bean.getMaxFileSize());
        }
        sRecorder.setPreviewDisplay(surface);
        sRecorder.setOutputFile(bean.getFilePath());
        try {
            sRecorder.prepare();
            sRecorder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重置多媒体录制器
     */
    private void resetRecorder() {
        if (sRecorder == null) {
            sRecorder = getMediaRecorder();
        } else {
            sRecorder.reset();
        }
    }
}
