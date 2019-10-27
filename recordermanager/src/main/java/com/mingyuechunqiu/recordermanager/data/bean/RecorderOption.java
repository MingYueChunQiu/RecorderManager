package com.mingyuechunqiu.recordermanager.data.bean;

import android.media.MediaRecorder;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     author : 明月春秋
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制参数信息类
 *              实现Parcelable
 *     version: 1.0
 * </pre>
 */
public class RecorderOption implements Parcelable {

    private Builder mBuilder;

    public RecorderOption() {
        this(new Builder());
    }

    public RecorderOption(@NonNull Builder builder) {
        mBuilder = builder;
    }

    protected RecorderOption(@NonNull Parcel in) {
        mBuilder = new Builder();
        mBuilder.audioSource = in.readInt();
        mBuilder.videoSource = in.readInt();
        mBuilder.outputFormat = in.readInt();
        mBuilder.audioEncoder = in.readInt();
        mBuilder.videoEncoder = in.readInt();
        mBuilder.audioSamplingRate = in.readInt();
        mBuilder.bitRate = in.readInt();
        mBuilder.frameRate = in.readInt();
        mBuilder.videoWidth = in.readInt();
        mBuilder.videoHeight = in.readInt();
        mBuilder.maxDuration = in.readInt();
        mBuilder.maxFileSize = in.readLong();
        mBuilder.filePath = in.readString();
        mBuilder.orientationHint = in.readInt();
    }

    public static final Creator<RecorderOption> CREATOR = new Creator<RecorderOption>() {
        @Override
        public RecorderOption createFromParcel(Parcel in) {
            return new RecorderOption(in);
        }

        @Override
        public RecorderOption[] newArray(int size) {
            return new RecorderOption[size];
        }
    };

    public int getAudioSource() {
        return mBuilder.audioSource;
    }

    public void setAudioSource(int audioSource) {
        mBuilder.audioSource = audioSource;
    }

    public int getVideoSource() {
        return mBuilder.videoSource;
    }

    public void setVideoSource(int videoSource) {
        mBuilder.videoSource = videoSource;
    }

    public int getOutputFormat() {
        return mBuilder.outputFormat;
    }

    public void setOutputFormat(int outputFormat) {
        mBuilder.outputFormat = outputFormat;
    }

    public int getAudioEncoder() {
        return mBuilder.audioEncoder;
    }

    public void setAudioEncoder(int audioEncoder) {
        mBuilder.audioEncoder = audioEncoder;
    }

    public int getVideoEncoder() {
        return mBuilder.videoEncoder;
    }

    public void setVideoEncoder(int videoEncoder) {
        mBuilder.videoEncoder = videoEncoder;
    }

    public int getAudioSamplingRate() {
        return mBuilder.audioSamplingRate;
    }

    public void setAudioSamplingRate(int audioSamplingRate) {
        mBuilder.audioSamplingRate = audioSamplingRate;
    }

    public int getBitRate() {
        return mBuilder.bitRate;
    }

    public void setBitRate(int bitRate) {
        mBuilder.bitRate = bitRate;
    }

    public int getFrameRate() {
        return mBuilder.frameRate;
    }

    public void setFrameRate(int frameRate) {
        mBuilder.frameRate = frameRate;
    }

    public int getVideoWidth() {
        return mBuilder.videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        mBuilder.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return mBuilder.videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        mBuilder.videoHeight = videoHeight;
    }

    public int getMaxDuration() {
        return mBuilder.maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        mBuilder.maxDuration = maxDuration;
    }

    public long getMaxFileSize() {
        return mBuilder.maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        mBuilder.maxFileSize = maxFileSize;
    }

    public String getFilePath() {
        return mBuilder.filePath;
    }

    public void setFilePath(String filePath) {
        mBuilder.filePath = filePath;
    }

    public int getOrientationHint() {
        return mBuilder.orientationHint;
    }

    public void setOrientationHint(int orientationHint) {
        mBuilder.orientationHint = orientationHint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBuilder.audioSource);
        dest.writeInt(mBuilder.videoSource);
        dest.writeInt(mBuilder.outputFormat);
        dest.writeInt(mBuilder.audioEncoder);
        dest.writeInt(mBuilder.videoEncoder);
        dest.writeInt(mBuilder.audioSamplingRate);
        dest.writeInt(mBuilder.bitRate);
        dest.writeInt(mBuilder.frameRate);
        dest.writeInt(mBuilder.videoWidth);
        dest.writeInt(mBuilder.videoHeight);
        dest.writeInt(mBuilder.maxDuration);
        dest.writeLong(mBuilder.maxFileSize);
        dest.writeString(mBuilder.filePath);
        dest.writeInt(mBuilder.orientationHint);
    }

    /**
     * 链式调用
     */
    public static class Builder {

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

        public RecorderOption build() {
            return new RecorderOption(this);
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
                    .setOrientationHint(90)
                    .setFilePath(path)
                    .build();
        }

        public int getAudioSource() {
            return audioSource;
        }

        public Builder setAudioSource(int audioSource) {
            this.audioSource = audioSource;
            return this;
        }

        public int getVideoSource() {
            return videoSource;
        }

        public Builder setVideoSource(int videoSource) {
            this.videoSource = videoSource;
            return this;
        }

        public int getOutputFormat() {
            return outputFormat;
        }

        public Builder setOutputFormat(int outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public int getAudioEncoder() {
            return getAudioEncoder();
        }

        public Builder setAudioEncoder(int audioEncoder) {
            this.audioEncoder = audioEncoder;
            return this;
        }

        public int getVideoEncoder() {
            return videoEncoder;
        }

        public Builder setVideoEncoder(int videoEncoder) {
            this.videoEncoder = videoEncoder;
            return this;
        }

        public int getAudioSamplingRate() {
            return audioSamplingRate;
        }

        public Builder setAudioSamplingRate(int audioSamplingRate) {
            this.audioSamplingRate = audioSamplingRate;
            return this;
        }

        public int getBitRate() {
            return bitRate;
        }

        public Builder setBitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
        }

        public int getFrameRate() {
            return frameRate;
        }

        public Builder setFrameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public int getVideoWidth() {
            return getVideoWidth();
        }

        public Builder setVideoWidth(int videoWidth) {
            this.videoWidth = videoWidth;
            return this;
        }

        public int getVideoHeight() {
            return getVideoHeight();
        }

        public Builder setVideoHeight(int videoHeight) {
            this.videoHeight = videoHeight;
            return this;
        }

        public long getMaxDuration() {
            return maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public Builder setMaxFileSize(long maxFileSize) {
            this.maxFileSize = maxFileSize;
            return this;
        }

        public String getFilePath() {
            return filePath;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public int getOrientationHint() {
            return orientationHint;
        }

        public Builder setOrientationHint(int orientationHint) {
            this.orientationHint = orientationHint;
            return this;
        }
    }
}
