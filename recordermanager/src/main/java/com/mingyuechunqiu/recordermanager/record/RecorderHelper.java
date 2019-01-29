package com.mingyuechunqiu.recordermanager.record;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
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
            try {
                sRecorder.stop();
                sRecorder.reset();
                sRecorder.release();
                sRecorder = null;
            } catch (RuntimeException stopException) {
                //录制时间过短stop，会有崩溃异常，所以进行捕获
                Log.d("RecordVideoDelegate", stopException.getMessage());
            }
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
        return recordAudio(new RecorderOption.Builder().buildDefaultAudioBean(path));
    }

    /**
     * 录制音频
     *
     * @param option 存储录制信息的对象
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordAudio(RecorderOption option) {
        if (option == null) {
            return false;
        }
        resetRecorder();
        sRecorder.setAudioSource(option.getAudioSource());
        sRecorder.setOutputFormat(option.getOutputFormat());
        sRecorder.setAudioEncoder(option.getAudioEncoder());
        if (option.getAudioSamplingRate() > 0) {
            sRecorder.setAudioSamplingRate(option.getAudioSamplingRate());
        }
        if (option.getBitRate() > 0) {
            sRecorder.setAudioEncodingBitRate(option.getBitRate());
        }
        if (option.getMaxDuration() > 0) {
            sRecorder.setMaxDuration(option.getMaxDuration());
        }
        if (option.getMaxFileSize() > 0) {
            sRecorder.setMaxFileSize(option.getMaxFileSize());
        }
        sRecorder.setOutputFile(option.getFilePath());
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
        return recordVideo(camera, surface, new RecorderOption.Builder().buildDefaultVideoBean(path));
    }

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param option  存储录制信息的对象
     * @return 返回是否成功开启视频录制，成功返回true，否则返回false
     */
    @Override
    public boolean recordVideo(Camera camera, Surface surface, RecorderOption option) {
        if (camera == null || surface == null || option == null) {
            return false;
        }
        resetRecorder();
        sRecorder.setCamera(camera);
        sRecorder.setAudioSource(option.getAudioSource());
        sRecorder.setVideoSource(option.getVideoSource());
        sRecorder.setOutputFormat(option.getOutputFormat());
        sRecorder.setAudioEncoder(option.getAudioEncoder());
        sRecorder.setVideoEncoder(option.getVideoEncoder());
        if (option.getBitRate() > 0) {
            sRecorder.setVideoEncodingBitRate(option.getBitRate());
        }
        if (option.getFrameRate() > 0) {
            sRecorder.setVideoFrameRate(option.getFrameRate());
        }
        if (option.getVideoWidth() > 0 && option.getVideoHeight() > 0) {
            sRecorder.setVideoSize(option.getVideoWidth(), option.getVideoHeight());
        }
        //（目前录制视频时设置此属性有崩溃风险，留待解决）
//        if (option.getMaxDuration() > 0) {
//            sRecorder.setMaxDuration(option.getMaxDuration());
//        }
        if (option.getMaxFileSize() > 0) {
            sRecorder.setMaxFileSize(option.getMaxFileSize());
        }
        sRecorder.setOrientationHint(option.getOrientationHint());
        sRecorder.setPreviewDisplay(surface);
        sRecorder.setOutputFile(option.getFilePath());
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
