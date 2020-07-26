package com.mingyuechunqiu.recordermanager.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants;

/**
 * <pre>
 *     author : 明月春秋
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频配置信息类
 *     version: 1.0
 * </pre>
 */
public class RecordVideoOption implements Parcelable {

    private Builder mBuilder;

    public RecordVideoOption() {
        this(new Builder());
    }

    public RecordVideoOption(@NonNull Builder builder) {
        mBuilder = builder;
    }

    protected RecordVideoOption(@NonNull Parcel in) {
        mBuilder = new Builder();
        mBuilder.recorderOption = in.readParcelable(RecorderOption.class.getClassLoader());
        mBuilder.recordVideoButtonOption = in.readParcelable(RecordVideoButtonOption.class.getClassLoader());
        mBuilder.maxDuration = in.readInt();
        mBuilder.cameraType = RecorderManagerConstants.CameraType.values()[in.readInt()];
        mBuilder.hideFlipCameraButton = in.readByte() != 0;
        mBuilder.timingHint = in.readString();
        mBuilder.errorToastMsg = in.readString();
    }

    public static final Creator<RecordVideoOption> CREATOR = new Creator<RecordVideoOption>() {
        @Override
        public RecordVideoOption createFromParcel(Parcel in) {
            return new RecordVideoOption(in);
        }

        @Override
        public RecordVideoOption[] newArray(int size) {
            return new RecordVideoOption[size];
        }
    };

    public RecorderOption getRecorderOption() {
        return mBuilder.recorderOption;
    }

    public void setRecorderOption(RecorderOption option) {
        mBuilder.recorderOption = option;
    }

    public RecordVideoButtonOption getRecordVideoButtonOption() {
        return mBuilder.recordVideoButtonOption;
    }

    public void setRecordVideoButtonOption(RecordVideoButtonOption recordVideoButtonOption) {
        mBuilder.recordVideoButtonOption = recordVideoButtonOption;
    }

    public int getMaxDuration() {
        return mBuilder.maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        mBuilder.maxDuration = maxDuration;
    }

    public RecorderManagerConstants.CameraType getCameraType() {
        return mBuilder.cameraType;
    }

    public void setCameraType(RecorderManagerConstants.CameraType cameraType) {
        mBuilder.cameraType = cameraType;
    }

    public boolean isHideFlipCameraButton() {
        return mBuilder.hideFlipCameraButton;
    }

    public void setHideFlipCameraButton(boolean hideFlipCameraButton) {
        mBuilder.hideFlipCameraButton = hideFlipCameraButton;
    }

    public OnRecordVideoListener getOnRecordVideoListener() {
        return mBuilder.listener;
    }

    public void setOnRecordVideoListener(OnRecordVideoListener listener) {
        mBuilder.listener = listener;
    }

    @Nullable
    public String getTimingHint() {
        return mBuilder.timingHint;
    }

    public void setTimingHint(@Nullable String timingHint) {
        mBuilder.timingHint = timingHint;
    }

    @Nullable
    public String getErrorToastMsg() {
        return mBuilder.errorToastMsg;
    }

    public void setErrorToastMsg(@Nullable String errorToastMsg) {
        mBuilder.errorToastMsg = errorToastMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mBuilder.recorderOption, flags);
        dest.writeParcelable(mBuilder.recordVideoButtonOption, flags);
        dest.writeInt(mBuilder.maxDuration);
        dest.writeInt(mBuilder.cameraType.ordinal());
        dest.writeByte((byte) (mBuilder.hideFlipCameraButton ? 1 : 0));
        dest.writeString(mBuilder.timingHint);
        dest.writeString(mBuilder.errorToastMsg);
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private RecorderOption recorderOption;//录制配置信息
        private RecordVideoButtonOption recordVideoButtonOption;//录制视频按钮配置信息类
        private int maxDuration;//最大录制时间（秒数）
        private RecorderManagerConstants.CameraType cameraType;//摄像头类型
        private boolean hideFlipCameraButton;//隐藏返回翻转摄像头按钮
        private OnRecordVideoListener listener;//录制视频监听器
        private String timingHint;//录制按钮上方提示语句（默认：0：%s）
        private String errorToastMsg;//录制发生错误Toast（默认：录制时间小于1秒，请重试）

        public Builder() {
            maxDuration = 30;//默认30秒
            cameraType = RecorderManagerConstants.CameraType.CAMERA_NOT_SET;
        }

        public RecordVideoOption build() {
            return new RecordVideoOption(this);
        }

        public RecorderOption getRecorderOption() {
            return recorderOption;
        }

        public Builder setRecorderOption(RecorderOption option) {
            this.recorderOption = option;
            return this;
        }

        public RecordVideoButtonOption getRecordVideoButtonOption() {
            return recordVideoButtonOption;
        }

        public Builder setRecordVideoButtonOption(RecordVideoButtonOption recordVideoButtonOption) {
            this.recordVideoButtonOption = recordVideoButtonOption;
            return this;
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public RecorderManagerConstants.CameraType getCameraType() {
            return cameraType;
        }

        public Builder setCameraType(RecorderManagerConstants.CameraType cameraType) {
            this.cameraType = cameraType;
            return this;
        }

        public boolean isHideFlipCameraButton() {
            return hideFlipCameraButton;
        }

        public Builder setHideFlipCameraButton(boolean hideFlipCameraButton) {
            this.hideFlipCameraButton = hideFlipCameraButton;
            return this;
        }

        public OnRecordVideoListener getOnRecordVideoListener() {
            return listener;
        }

        public Builder setOnRecordVideoListener(OnRecordVideoListener listener) {
            this.listener = listener;
            return this;
        }

        @Nullable
        public String getTimingHint() {
            return timingHint;
        }

        public Builder setTimingHint(@Nullable String timingHint) {
            this.timingHint = timingHint;
            return this;
        }

        @Nullable
        public String getErrorToastMsg() {
            return errorToastMsg;
        }

        public Builder setErrorToastMsg(@Nullable String errorToastMsg) {
            this.errorToastMsg = errorToastMsg;
            return this;
        }
    }

    /**
     * 录制视频监听器
     */
    public interface OnRecordVideoListener {

        /**
         * 当完成一次录制时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onCompleteRecordVideo(String filePath, int videoDuration);

        /**
         * 当点击确认录制结果按钮时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onClickConfirm(String filePath, int videoDuration);

        /**
         * 当点击取消按钮时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onClickCancel(String filePath, int videoDuration);

        /**
         * 当点击返回按钮时回调
         */
        void onClickBack();
    }
}
