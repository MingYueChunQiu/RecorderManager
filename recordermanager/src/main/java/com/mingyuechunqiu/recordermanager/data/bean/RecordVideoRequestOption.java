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
        mBuilder.filePath = in.readString();
        mBuilder.maxDuration = in.readInt();
        mBuilder.recordVideoOption = in.readParcelable(RecordVideoOption.class.getClassLoader());
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

    public String getFilePath() {
        return mBuilder.filePath;
    }

    public void setFilePath(String filePath) {
        mBuilder.filePath = filePath;
    }

    public int getMaxDuration() {
        return mBuilder.maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        mBuilder.maxDuration = maxDuration;
    }

    public RecordVideoOption getRecordVideoOption() {
        return mBuilder.recordVideoOption;
    }

    public void setRecordVideoOption(RecordVideoOption recordVideoOption) {
        mBuilder.recordVideoOption = recordVideoOption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBuilder.filePath);
        dest.writeInt(mBuilder.maxDuration);
        dest.writeParcelable(mBuilder.recordVideoOption, flags);
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private String filePath;//文件保存路径
        private int maxDuration;//最大录制时间（秒数）
        private RecordVideoOption recordVideoOption;//录制视频配置信息类（里面配置的filePath和maxDuration会覆盖外面的）

        public RecordVideoRequestOption build() {
            return new RecordVideoRequestOption(this);
        }

        public String getFilePath() {
            return filePath;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public Builder setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        public RecordVideoOption getRecordVideoOption() {
            return recordVideoOption;
        }

        public Builder setRecordVideoOption(RecordVideoOption recordVideoOption) {
            this.recordVideoOption = recordVideoOption;
            return this;
        }
    }
}
