package com.mingyuechunqiu.recordermanager;

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
public class RecorderBean {

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

    public static class Builder {

        private RecorderBean bean;

        public Builder() {
            bean = new RecorderBean();
        }

        public RecorderBean build() {
            return bean;
        }

        /**
         * 创建默认的音频录制对象
         *
         * @param path 文件地址
         * @return 返回生成的默认配置对象
         */
        public RecorderBean buildDefaultAudioBean(String path) {
            return this.setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    .setFrameRate(44100)
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
        public RecorderBean buildDefaultVideoBean(String path) {
            //华为荣耀10用640/480拍摄会很卡，且用MPEG_4_SP录制的视频会有问题，要么有时能有时不能放，
            // 要么视频会只剩下录制的一部分，用H263录制时会一卡一卡的，停止录制时直接崩溃
            return this.setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                    .setVideoSource(MediaRecorder.VideoSource.CAMERA)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    .setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                    .setBitRate(1024 * 1024)
                    .setFrameRate(20)
                    .setVideoWidth(320)
                    .setVideoHeight(240)
                    .setFilePath(path)
                    .build();
        }

        public int getAudioSource() {
            return bean.audioSource;
        }

        public Builder setAudioSource(int audioSource) {
            bean.audioSource = audioSource;
            return this;
        }

        public int getVideoSource() {
            return bean.videoSource;
        }

        public Builder setVideoSource(int videoSource) {
            bean.videoSource = videoSource;
            return this;
        }

        public int getOutputFormat() {
            return bean.outputFormat;
        }

        public Builder setOutputFormat(int outputFormat) {
            bean.outputFormat = outputFormat;
            return this;
        }

        public int getAudioEncoder() {
            return bean.getAudioEncoder();
        }

        public Builder setAudioEncoder(int audioEncoder) {
            bean.audioEncoder = audioEncoder;
            return this;
        }

        public int getVideoEncoder() {
            return bean.videoEncoder;
        }

        public Builder setVideoEncoder(int videoEncoder) {
            bean.videoEncoder = videoEncoder;
            return this;
        }

        public int getAudioSamplingRate() {
            return bean.audioSamplingRate;
        }

        public Builder setAudioSamplingRate(int audioSamplingRate) {
            bean.audioSamplingRate = audioSamplingRate;
            return this;
        }

        public int getBitRate() {
            return bean.bitRate;
        }

        public Builder setBitRate(int bitRate) {
            bean.bitRate = bitRate;
            return this;
        }

        public int getFrameRate() {
            return bean.frameRate;
        }

        public Builder setFrameRate(int frameRate) {
            bean.frameRate = frameRate;
            return this;
        }

        public int getVideoWidth() {
            return bean.getVideoWidth();
        }

        public Builder setVideoWidth(int videoWidth) {
            bean.videoWidth = videoWidth;
            return this;
        }

        public int getVideoHeight() {
            return bean.getVideoHeight();
        }

        public Builder setVideoHeight(int videoHeight) {
            bean.videoHeight = videoHeight;
            return this;
        }

        public long getMaxDuration() {
            return bean.maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            bean.maxDuration = maxDuration;
            return this;
        }

        public long getMaxFileSize() {
            return bean.maxFileSize;
        }

        public Builder setMaxFileSize(long maxFileSize) {
            bean.maxFileSize = maxFileSize;
            return this;
        }

        public String getFilePath() {
            return bean.filePath;
        }

        public Builder setFilePath(String filePath) {
            bean.filePath = filePath;
            return this;
        }
    }
}
