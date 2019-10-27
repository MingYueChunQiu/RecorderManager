package com.mingyuechunqiu.recordermanager.data.bean;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频配置信息类
 *     version: 1.0
 * </pre>
 */
public class RecordVideoOption {

    private Builder mBuilder;

    public RecordVideoOption() {
        this(new Builder());
    }

    public RecordVideoOption(@NonNull Builder builder) {
        mBuilder = builder;
    }

    public RecorderOption getRecorderOption() {
        return mBuilder.option;
    }

    public void setRecorderOption(RecorderOption option) {
        mBuilder.option = option;
    }

    public int getMaxDuration() {
        return mBuilder.maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        mBuilder.maxDuration = maxDuration;
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

    /**
     * 链式调用
     */
    public static class Builder {

        private RecorderOption option;//录制配置信息
        private int maxDuration;//最大录制时间（秒数）
        private boolean hideFlipCameraButton;//隐藏返回翻转摄像头按钮
        private OnRecordVideoListener listener;//录制视频监听器

        public Builder() {
            maxDuration = 30;//默认30秒
        }

        public RecordVideoOption build() {
            return new RecordVideoOption(this);
        }

        public RecorderOption getRecorderOption() {
            return option;
        }

        public Builder setRecorderOption(RecorderOption option) {
            this.option = option;
            return this;
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
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
