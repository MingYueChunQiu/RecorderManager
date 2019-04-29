package com.mingyuechunqiu.recordermanager.data.bean;

import android.media.MediaRecorder;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制参数信息类
 *     version: 1.0
 * </pre>
 */
public class RecorderOption {

    private int audioSource;//音频源
    private int videoSource;//视频源
    private int outputFormat;//输出格式
    private int audioEncoder;//音频编码格式
    private int videoEncoder;//视频编码格式
    private int audioSamplingRate;//音频采样频率（一般44100）
    private int bitRate;//视频编码比特率
    private int frameRate;//视频帧率
    private int videoWidth, videoHeight;//视频宽高
    private int maxDuration;//最大时长
    private long maxFileSize;//文件最大大小
    private String filePath;//文件存储路径
    private int orientationHint;//视频录制角度方向

    public int getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(int audioSource) {
        this.audioSource = audioSource;
    }

    public int getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(int videoSource) {
        this.videoSource = videoSource;
    }

    public int getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(int outputFormat) {
        this.outputFormat = outputFormat;
    }

    public int getAudioEncoder() {
        return audioEncoder;
    }

    public void setAudioEncoder(int audioEncoder) {
        this.audioEncoder = audioEncoder;
    }

    public int getVideoEncoder() {
        return videoEncoder;
    }

    public void setVideoEncoder(int videoEncoder) {
        this.videoEncoder = videoEncoder;
    }

    public int getAudioSamplingRate() {
        return audioSamplingRate;
    }

    public void setAudioSamplingRate(int audioSamplingRate) {
        this.audioSamplingRate = audioSamplingRate;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getOrientationHint() {
        return orientationHint;
    }

    public void setOrientationHint(int orientationHint) {
        this.orientationHint = orientationHint;
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private RecorderOption mOption;

        public Builder() {
            mOption = new RecorderOption();
        }

        public RecorderOption build() {
            return mOption;
        }

        /**
         * 创建默认的音频录制对象
         *
         * @param path 文件地址
         * @return 返回生成的默认配置对象
         */
        public RecorderOption buildDefaultAudioBean(String path) {
            return this.setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    .setAudioSamplingRate(44100)
                    .setBitRate(96000)
                    .setFilePath(path)
                    .build();
        }

        /**
         * 创建默认的视频录制对象
         *
         * @param path 文件地址
         * @return 返回生成的默认配置对象
         */
        public RecorderOption buildDefaultVideoBean(String path) {
            //用MPEG_4_SP录制的视频会有问题，在荣耀10上会崩溃和卡
            //用H263录制时会一卡一卡的，停止录制时直接崩溃
            return this.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                    .setVideoSource(MediaRecorder.VideoSource.CAMERA)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    .setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                    .setVideoWidth(640)
                    .setVideoHeight(480)
                    .setBitRate(3 * 1024 * 1024)
                    .setFrameRate(30)
                    .setOrientionHint(90)
                    .setFilePath(path)
                    .build();
        }

        public int getAudioSource() {
            return mOption.audioSource;
        }

        public Builder setAudioSource(int audioSource) {
            mOption.audioSource = audioSource;
            return this;
        }

        public int getVideoSource() {
            return mOption.videoSource;
        }

        public Builder setVideoSource(int videoSource) {
            mOption.videoSource = videoSource;
            return this;
        }

        public int getOutputFormat() {
            return mOption.outputFormat;
        }

        public Builder setOutputFormat(int outputFormat) {
            mOption.outputFormat = outputFormat;
            return this;
        }

        public int getAudioEncoder() {
            return mOption.getAudioEncoder();
        }

        public Builder setAudioEncoder(int audioEncoder) {
            mOption.audioEncoder = audioEncoder;
            return this;
        }

        public int getVideoEncoder() {
            return mOption.videoEncoder;
        }

        public Builder setVideoEncoder(int videoEncoder) {
            mOption.videoEncoder = videoEncoder;
            return this;
        }

        public int getAudioSamplingRate() {
            return mOption.audioSamplingRate;
        }

        public Builder setAudioSamplingRate(int audioSamplingRate) {
            mOption.audioSamplingRate = audioSamplingRate;
            return this;
        }

        public int getBitRate() {
            return mOption.bitRate;
        }

        public Builder setBitRate(int bitRate) {
            mOption.bitRate = bitRate;
            return this;
        }

        public int getFrameRate() {
            return mOption.frameRate;
        }

        public Builder setFrameRate(int frameRate) {
            mOption.frameRate = frameRate;
            return this;
        }

        public int getVideoWidth() {
            return mOption.getVideoWidth();
        }

        public Builder setVideoWidth(int videoWidth) {
            mOption.videoWidth = videoWidth;
            return this;
        }

        public int getVideoHeight() {
            return mOption.getVideoHeight();
        }

        public Builder setVideoHeight(int videoHeight) {
            mOption.videoHeight = videoHeight;
            return this;
        }

        public long getMaxDuration() {
            return mOption.maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            mOption.maxDuration = maxDuration;
            return this;
        }

        public long getMaxFileSize() {
            return mOption.maxFileSize;
        }

        public Builder setMaxFileSize(long maxFileSize) {
            mOption.maxFileSize = maxFileSize;
            return this;
        }

        public String getFilePath() {
            return mOption.filePath;
        }

        public Builder setFilePath(String filePath) {
            mOption.filePath = filePath;
            return this;
        }

        public int getOrientionHint() {
            return mOption.orientationHint;
        }

        public Builder setOrientionHint(int orientationHint) {
            mOption.orientationHint = orientationHint;
            return this;
        }
    }
}
