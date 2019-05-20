package com.mingyuechunqiu.recordermanager.data.bean;

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

    private RecorderOption option;//录制配置信息
    private int maxDuration;//最大录制时间（秒数）
    private OnRecordVideoListener listener;//录制视频监听器

    public RecordVideoOption() {
        maxDuration = 30;//默认30秒
    }

    public RecorderOption getRecorderOption() {
        return option;
    }

    public void setRecorderOption(RecorderOption option) {
        this.option = option;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public OnRecordVideoListener getOnRecordVideoListener() {
        return listener;
    }

    public void setOnRecordVideoListener(OnRecordVideoListener listener) {
        this.listener = listener;
    }

    /**
     * 链式调用
     */
    public static class Builder {
        RecordVideoOption mOption;

        public Builder() {
            mOption = new RecordVideoOption();
        }

        public RecordVideoOption build() {
            return mOption;
        }

        public RecorderOption getRecorderOption() {
            return mOption.option;
        }

        public Builder setRecorderOption(RecorderOption option) {
            mOption.option = option;
            return this;
        }

        public int getMaxDuration() {
            return mOption.maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            mOption.maxDuration = maxDuration;
            return this;
        }

        public OnRecordVideoListener getOnRecordVideoListener() {
            return mOption.listener;
        }

        public Builder setOnRecordVideoListener(OnRecordVideoListener listener) {
            mOption.listener = listener;
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
