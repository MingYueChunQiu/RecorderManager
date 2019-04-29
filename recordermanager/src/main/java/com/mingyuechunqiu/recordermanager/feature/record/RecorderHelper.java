package com.mingyuechunqiu.recordermanager.feature.record;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;

import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;

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

    private RecorderOption mOption;
    private MediaRecorder mRecorder;

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            } catch (RuntimeException stopException) {
                //录制时间过短stop，会有崩溃异常，所以进行捕获
                Log.d("RecordVideoDelegate", stopException.getMessage());
            }
        }
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        return mRecorder;
    }

    @Override
    public RecorderOption getRecorderOption() {
        return mOption;
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
        mOption = option;
        mRecorder.setAudioSource(mOption.getAudioSource());
        mRecorder.setOutputFormat(mOption.getOutputFormat());
        mRecorder.setAudioEncoder(mOption.getAudioEncoder());
        if (mOption.getAudioSamplingRate() > 0) {
            mRecorder.setAudioSamplingRate(mOption.getAudioSamplingRate());
        }
        if (mOption.getBitRate() > 0) {
            mRecorder.setAudioEncodingBitRate(mOption.getBitRate());
        }
        if (mOption.getMaxDuration() > 0) {
            mRecorder.setMaxDuration(mOption.getMaxDuration());
        }
        if (mOption.getMaxFileSize() > 0) {
            mRecorder.setMaxFileSize(mOption.getMaxFileSize());
        }
        mRecorder.setOutputFile(mOption.getFilePath());
        try {
            mRecorder.prepare();
            mRecorder.start();
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
        mOption = option;
        mRecorder.setCamera(camera);
        mRecorder.setAudioSource(mOption.getAudioSource());
        mRecorder.setVideoSource(mOption.getVideoSource());
        mRecorder.setOutputFormat(mOption.getOutputFormat());
        mRecorder.setAudioEncoder(mOption.getAudioEncoder());
        mRecorder.setVideoEncoder(mOption.getVideoEncoder());
        if (mOption.getBitRate() > 0) {
            mRecorder.setVideoEncodingBitRate(mOption.getBitRate());
        }
        if (mOption.getFrameRate() > 0) {
            mRecorder.setVideoFrameRate(mOption.getFrameRate());
        }
        if (mOption.getVideoWidth() > 0 && mOption.getVideoHeight() > 0) {
            mRecorder.setVideoSize(mOption.getVideoWidth(), mOption.getVideoHeight());
        }
        //（目前录制视频时设置此属性有崩溃风险，留待解决）
//        if (option.getMaxDuration() > 0) {
//            sRecorder.setMaxDuration(option.getMaxDuration());
//        }
        if (mOption.getMaxFileSize() > 0) {
            mRecorder.setMaxFileSize(mOption.getMaxFileSize());
        }
        mRecorder.setOrientationHint(mOption.getOrientationHint());
        mRecorder.setPreviewDisplay(surface);
        mRecorder.setOutputFile(mOption.getFilePath());
        try {
            mRecorder.prepare();
            mRecorder.start();
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
        if (mRecorder == null) {
            mRecorder = getMediaRecorder();
        } else {
            mRecorder.reset();
        }
    }
}
