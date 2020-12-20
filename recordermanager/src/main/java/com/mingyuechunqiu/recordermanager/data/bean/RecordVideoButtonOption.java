package com.mingyuechunqiu.recordermanager.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2019-11-10 19:31
 *      Desc:       录制视频按钮配置信息类
 *                  实现Parcelable
 *      Version:    1.0
 * </pre>
 */
public class RecordVideoButtonOption implements Parcelable {

    private final Builder mBuilder;

    public RecordVideoButtonOption() {
        this(new Builder());
    }

    public RecordVideoButtonOption(@NonNull Builder builder) {
        mBuilder = builder;
    }

    protected RecordVideoButtonOption(@NonNull Parcel in) {
        mBuilder = new Builder();
        mBuilder.idleCircleColor = in.readInt();
        mBuilder.pressedCircleColor = in.readInt();
        mBuilder.releasedCircleColor = in.readInt();
        mBuilder.idleRingColor = in.readInt();
        mBuilder.pressedRingColor = in.readInt();
        mBuilder.releasedRingColor = in.readInt();
        mBuilder.idleRingWidth = in.readInt();
        mBuilder.pressedRingWidth = in.readInt();
        mBuilder.releasedRingWidth = in.readInt();
        mBuilder.idleInnerPadding = in.readInt();
        mBuilder.pressedInnerPadding = in.readInt();
        mBuilder.releasedInnerPadding = in.readInt();
        mBuilder.idleRingVisible = in.readByte() != 0;
        mBuilder.pressedRingVisible = in.readByte() != 0;
        mBuilder.releasedRingVisible = in.readByte() != 0;
    }

    public static final Creator<RecordVideoButtonOption> CREATOR = new Creator<RecordVideoButtonOption>() {
        @Override
        public RecordVideoButtonOption createFromParcel(Parcel in) {
            return new RecordVideoButtonOption(in);
        }

        @Override
        public RecordVideoButtonOption[] newArray(int size) {
            return new RecordVideoButtonOption[size];
        }
    };

    public int getIdleCircleColor() {
        return mBuilder.idleCircleColor;
    }

    public void setIdleCircleColor(int idleCircleColor) {
        mBuilder.idleCircleColor = idleCircleColor;
    }

    public int getPressedCircleColor() {
        return mBuilder.pressedCircleColor;
    }

    public void setPressedCircleColor(int pressedCircleColor) {
        mBuilder.pressedCircleColor = pressedCircleColor;
    }

    public int getReleasedCircleColor() {
        return mBuilder.releasedCircleColor;
    }

    public void setReleasedCircleColor(int releasedCircleColor) {
        mBuilder.releasedCircleColor = releasedCircleColor;
    }

    public int getIdleRingColor() {
        return mBuilder.idleRingColor;
    }

    public void setIdleRingColor(int idleRingColor) {
        mBuilder.idleRingColor = idleRingColor;
    }

    public int getPressedRingColor() {
        return mBuilder.pressedRingColor;
    }

    public void setPressedRingColor(int pressedRingColor) {
        mBuilder.pressedRingColor = pressedRingColor;
    }

    public int getReleasedRingColor() {
        return mBuilder.releasedRingColor;
    }

    public void setReleasedRingColor(int releasedRingColor) {
        mBuilder.releasedRingColor = releasedRingColor;
    }

    public int getIdleRingWidth() {
        return mBuilder.idleRingWidth;
    }

    public void setIdleRingWidth(int idleRingWidth) {
        mBuilder.idleRingWidth = idleRingWidth;
    }

    public int getPressedRingWidth() {
        return mBuilder.pressedRingWidth;
    }

    public void setPressedRingWidth(int pressedRingWidth) {
        mBuilder.pressedRingWidth = pressedRingWidth;
    }

    public int getReleasedRingWidth() {
        return mBuilder.releasedRingWidth;
    }

    public void setReleasedRingWidth(int releasedRingWidth) {
        mBuilder.releasedRingWidth = releasedRingWidth;
    }

    public int getIdleInnerPadding() {
        return mBuilder.idleInnerPadding;
    }

    public void setIdleInnerPadding(int idleInnerPadding) {
        mBuilder.idleInnerPadding = idleInnerPadding;
    }

    public int getPressedInnerPadding() {
        return mBuilder.pressedInnerPadding;
    }

    public void setPressedInnerPadding(int pressedInnerPadding) {
        mBuilder.pressedInnerPadding = pressedInnerPadding;
    }

    public int getReleasedInnerPadding() {
        return mBuilder.releasedInnerPadding;
    }

    public void setReleasedInnerPadding(int releasedInnerPadding) {
        mBuilder.releasedInnerPadding = releasedInnerPadding;
    }

    public boolean isIdleRingVisible() {
        return mBuilder.idleRingVisible;
    }

    public void setIdleRingVisible(boolean idleRingVisible) {
        mBuilder.idleRingVisible = idleRingVisible;
    }

    public boolean isPressedRingVisible() {
        return mBuilder.pressedRingVisible;
    }

    public void setPressedRingVisible(boolean pressedRingVisible) {
        mBuilder.pressedRingVisible = pressedRingVisible;
    }

    public boolean isReleasedRingVisible() {
        return mBuilder.releasedRingVisible;
    }

    public void setReleasedRingVisible(boolean releasedRingVisible) {
        mBuilder.releasedRingVisible = releasedRingVisible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mBuilder.idleCircleColor);
        dest.writeInt(mBuilder.pressedCircleColor);
        dest.writeInt(mBuilder.releasedCircleColor);
        dest.writeInt(mBuilder.idleRingColor);
        dest.writeInt(mBuilder.pressedRingColor);
        dest.writeInt(mBuilder.releasedRingColor);
        dest.writeInt(mBuilder.idleRingWidth);
        dest.writeInt(mBuilder.pressedRingWidth);
        dest.writeInt(mBuilder.releasedRingWidth);
        dest.writeInt(mBuilder.idleInnerPadding);
        dest.writeInt(mBuilder.pressedInnerPadding);
        dest.writeInt(mBuilder.releasedInnerPadding);
        dest.writeByte((byte) (mBuilder.idleRingVisible ? 1 : 0));
        dest.writeByte((byte) (mBuilder.pressedRingVisible ? 1 : 0));
        dest.writeByte((byte) (mBuilder.releasedRingVisible ? 1 : 0));
    }

    public static class Builder {
        private @ColorInt
        int idleCircleColor;//空闲状态内部圆形颜色
        private @ColorInt
        int pressedCircleColor;//按下状态内部圆形颜色
        private @ColorInt
        int releasedCircleColor;//释放状态内部圆形颜色
        private @ColorInt
        int idleRingColor;//空闲状态外部圆环颜色
        private @ColorInt
        int pressedRingColor;//按下状态外部圆环颜色
        private @ColorInt
        int releasedRingColor;//释放状态外部圆环颜色
        private int idleRingWidth;//空闲状态外部圆环宽度
        private int pressedRingWidth;//按下状态外部圆环宽度
        private int releasedRingWidth;//释放状态外部圆环宽度
        private int idleInnerPadding;//空闲状态外部圆环与内部圆形之间边距
        private int pressedInnerPadding;//按下状态外部圆环与内部圆形之间边距
        private int releasedInnerPadding;//释放状态外部圆环与内部圆形之间边距
        private boolean idleRingVisible;//空闲状态下外部圆环是否可见
        private boolean pressedRingVisible;//按下状态下外部圆环是否可见
        private boolean releasedRingVisible;//释放状态下外部圆环是否可见

        public Builder() {
            idleRingVisible = true;
            pressedRingVisible = true;
            releasedRingVisible = true;
        }

        public RecordVideoButtonOption build() {
            return new RecordVideoButtonOption(this);
        }

        public int getIdleCircleColor() {
            return idleCircleColor;
        }

        public Builder setIdleCircleColor(int idleCircleColor) {
            this.idleCircleColor = idleCircleColor;
            return this;
        }

        public int getPressedCircleColor() {
            return pressedCircleColor;
        }

        public Builder setPressedCircleColor(int pressedCircleColor) {
            this.pressedCircleColor = pressedCircleColor;
            return this;
        }

        public int getReleasedCircleColor() {
            return releasedCircleColor;
        }

        public Builder setReleasedCircleColor(int releasedCircleColor) {
            this.releasedCircleColor = releasedCircleColor;
            return this;
        }

        public int getIdleRingColor() {
            return idleRingColor;
        }

        public Builder setIdleRingColor(int idleRingColor) {
            this.idleRingColor = idleRingColor;
            return this;
        }

        public int getPressedRingColor() {
            return pressedRingColor;
        }

        public Builder setPressedRingColor(int pressedRingColor) {
            this.pressedRingColor = pressedRingColor;
            return this;
        }

        public int getReleasedRingColor() {
            return releasedRingColor;
        }

        public Builder setReleasedRingColor(int releasedRingColor) {
            this.releasedRingColor = releasedRingColor;
            return this;
        }

        public int getIdleRingWidth() {
            return idleRingWidth;
        }

        public Builder setIdleRingWidth(int idleRingWidth) {
            this.idleRingWidth = idleRingWidth;
            return this;
        }

        public int getPressedRingWidth() {
            return pressedRingWidth;
        }

        public Builder setPressedRingWidth(int pressedRingWidth) {
            this.pressedRingWidth = pressedRingWidth;
            return this;
        }

        public int getReleasedRingWidth() {
            return releasedRingWidth;
        }

        public Builder setReleasedRingWidth(int releasedRingWidth) {
            this.releasedRingWidth = releasedRingWidth;
            return this;
        }

        public int getIdleInnerPadding() {
            return idleInnerPadding;
        }

        public Builder setIdleInnerPadding(int idleInnerPadding) {
            this.idleInnerPadding = idleInnerPadding;
            return this;
        }

        public int getPressedInnerPadding() {
            return pressedInnerPadding;
        }

        public Builder setPressedInnerPadding(int pressedInnerPadding) {
            this.pressedInnerPadding = pressedInnerPadding;
            return this;
        }

        public int getReleasedInnerPadding() {
            return releasedInnerPadding;
        }

        public Builder setReleasedInnerPadding(int releasedInnerPadding) {
            this.releasedInnerPadding = releasedInnerPadding;
            return this;
        }

        public boolean isIdleRingVisible() {
            return idleRingVisible;
        }

        public Builder setIdleRingVisible(boolean idleRingVisible) {
            this.idleRingVisible = idleRingVisible;
            return this;
        }

        public boolean isPressedRingVisible() {
            return pressedRingVisible;
        }

        public Builder setPressedRingVisible(boolean pressedRingVisible) {
            this.pressedRingVisible = pressedRingVisible;
            return this;
        }

        public boolean isReleasedRingVisible() {
            return releasedRingVisible;
        }

        public Builder setReleasedRingVisible(boolean releasedRingVisible) {
            this.releasedRingVisible = releasedRingVisible;
            return this;
        }
    }

}
