package com.mingyuechunqiu.recordermanager.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/5/5
 *     desc   : 录制视频结果信息类
 *              实现Parcelable
 *     version: 1.0
 * </pre>
 */
public class RecordVideoResultInfo implements Parcelable {

    private int duration;//视频录制时长
    private String filePath;//保存文件地址

    public RecordVideoResultInfo() {
    }

    public RecordVideoResultInfo(@NonNull Parcel in) {
        duration = in.readInt();
        filePath = in.readString();
    }

    public static final Creator<RecordVideoResultInfo> CREATOR = new Creator<RecordVideoResultInfo>() {
        @Override
        public RecordVideoResultInfo createFromParcel(Parcel in) {
            return new RecordVideoResultInfo(in);
        }

        @Override
        public RecordVideoResultInfo[] newArray(int size) {
            return new RecordVideoResultInfo[size];
        }
    };

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
        dest.writeInt(duration);
        dest.writeString(filePath);
    }

    /**
     * 链式调用
     */
    public static class Builder {

        private RecordVideoResultInfo mInfo;

        public Builder() {
            mInfo = new RecordVideoResultInfo();
        }

        public RecordVideoResultInfo build() {
            return mInfo;
        }

        public int getDuration() {
            return mInfo.duration;
        }

        public Builder setDuration(int duration) {
            mInfo.duration = duration;
            return this;
        }

        public String getFilePath() {
            return mInfo.filePath;
        }

        public Builder setFilePath(String filePath) {
            mInfo.filePath = filePath;
            return this;
        }
    }
}
