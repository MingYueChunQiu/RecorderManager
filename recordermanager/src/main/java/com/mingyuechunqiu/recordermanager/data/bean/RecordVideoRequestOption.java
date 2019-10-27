package com.mingyuechunqiu.recordermanager.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/9
 *     desc   : 视频录制请求配置信息类
 *              实现Parcelable
 *     version: 1.0
 * </pre>
 */
public class RecordVideoRequestOption implements Parcelable {

    private Builder mBuilder;

    public RecordVideoRequestOption() {
        this(new Builder());
    }

    public RecordVideoRequestOption(@NonNull Builder builder) {
        mBuilder = builder;
    }

    protected RecordVideoRequestOption(@NonNull Parcel in) {
        mBuilder = new Builder();
        mBuilder.maxDuration = in.readInt();
        mBuilder.filePath = in.readString();
        mBuilder.recorderOption = in.readParcelable(RecorderOption.class.getClassLoader());
        mBuilder.hideFlipCameraButton = in.readByte() != 0;
    }

    public static final Creator<RecordVideoRequestOption> CREATOR = new Creator<RecordVideoRequestOption>() {
        @Override
        public RecordVideoRequestOption createFromParcel(Parcel in) {
            return new RecordVideoRequestOption(in);
        }

        @Override
        public RecordVideoRequestOption[] newArray(int size) {
            return new RecordVideoRequestOption[size];
        }
    };

    public int getMaxDuration() {
        return mBuilder.maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        mBuilder.maxDuration = maxDuration;
    }

    public String getFilePath() {
        return mBuilder.filePath;
    }

    public void setFilePath(String filePath) {
        mBuilder.filePath = filePath;
    }

    public RecorderOption getRecorderOption() {
        return mBuilder.recorderOption;
    }

    public void setRecorderOption(RecorderOption recorderOption) {
        mBuilder.recorderOption = recorderOption;
    }

    public boolean isHideFlipCameraButton() {
        return mBuilder.hideFlipCameraButton;
    }

    public void setHideFlipCameraButton(boolean hideFlipCameraButton) {
        mBuilder.hideFlipCameraButton = hideFlipCameraButton;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBuilder.maxDuration);
        dest.writeString(mBuilder.filePath);
        dest.writeParcelable(mBuilder.recorderOption, flags);
        dest.writeByte((byte) (mBuilder.hideFlipCameraButton ? 1 : 0));
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private int maxDuration;//最大录制时长
        private String filePath;//文件保存路径
        private RecorderOption recorderOption;//录制参数信息类（在这里配置的文件路径会覆盖filePath）
        private boolean hideFlipCameraButton;//隐藏返回翻转摄像头按钮

        public RecordVideoRequestOption build() {
            return new RecordVideoRequestOption(this);
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public String getFilePath() {
            return filePath;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public RecorderOption getRecorderOption() {
            return recorderOption;
        }

        public Builder setRecorderOption(RecorderOption recorderOption) {
            this.recorderOption = recorderOption;
            return this;
        }

        public boolean isHideFlipCameraButton() {
            return hideFlipCameraButton;
        }

        public Builder setHideFlipCameraButton(boolean hideFlipCameraButton) {
            this.hideFlipCameraButton = hideFlipCameraButton;
            return this;
        }
    }
}
