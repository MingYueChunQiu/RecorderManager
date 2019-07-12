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

    private int maxDuration;//最大录制时长

    private String filePath;//文件保存路径

    public RecordVideoRequestOption() {
    }

    public RecordVideoRequestOption(@NonNull Parcel in) {
        maxDuration = in.readInt();
        filePath = in.readString();
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
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maxDuration);
        dest.writeString(filePath);
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private RecordVideoRequestOption mOption;

        public Builder() {
            mOption = new RecordVideoRequestOption();
        }

        public RecordVideoRequestOption build() {
            return mOption;
        }

        public int getMaxDuration() {
            return mOption.maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            mOption.maxDuration = maxDuration;
            return this;
        }

        public String getFilePath() {
            return mOption.filePath;
        }

        public Builder setFilePath(String filePath) {
            mOption.filePath = filePath;
            return this;
        }
    }
}
