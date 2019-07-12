package com.mingyuechunqiu.recordermanager.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.util.ScreenUtils;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/11/18
 *     desc   : 可长按的圆形进度按钮
 *              继承自View
 *     version: 1.0
 * </pre>
 */
public class CircleProgressButton extends View {

    private final int DEFAULT_IDLE_CIRCLE_COLOR = getResources().getColor(android.R.color.holo_red_light);
    private final int DEFAULT_PRESSED_CIRCLE_COLOR = getResources().getColor(android.R.color.holo_red_dark);
    private final int DEFAULT_RELEASED_CIRCLE_COLOR = DEFAULT_IDLE_CIRCLE_COLOR;

    private final int DEFAULT_IDLE_RING_COLOR = getResources().getColor(android.R.color.darker_gray);
    private final int DEFAULT_PRESSED_RING_COLOR = DEFAULT_IDLE_RING_COLOR;
    private final int DEFAULT_RELEASED_RING_COLOR = DEFAULT_IDLE_RING_COLOR;

    private final int DEFAULT_PROGRESS_DURATION = 400;
    private final int DEFAULT_INNER_PADDING = 0;//默认圆环距离内圆距离

    private final int DEFAULT_IDLE_RING_WIDTH = (int) ScreenUtils.getPxFromDp(getResources(), 6);
    private final int DEFAULT_PRESSED_RING_WIDTH = DEFAULT_IDLE_RING_WIDTH;
    private final int DEFAULT_RELEASED_RING_WIDTH = DEFAULT_IDLE_RING_WIDTH;

    private int mIdleCircleColor, mPressedCircleColor, mReleasedCircleColor;
    private int mIdleRingColor, mPressedRingColor, mReleasedRingColor;
    private int mIdleRingWidth, mPressedRingWidth, mReleasedRingWidth;
    private int mIdleInnerPadding, mPressedInnerPadding, mReleasedInnerPadding;//圆环距离内圆的边距
    private boolean mIdleRingVisible, mPressedRingVisible, mReleasedRingVisible;
    private int mMinProgress, mMaxProgress, mCurrentProgress;
    private boolean mTimerMode;
    private int mProgressDuration;

    private Paint mCirclePaint, mRingPaint;
    private int mInnerPadding;
    private RectF mRectF;
    private float mEndAngle;
    private ValueAnimator mProgressAnimator;
    private OnCircleProgressButtonListener mListener;

    private State mState = State.IDLE;

    public CircleProgressButton(Context context) {
        this(context, null);
    }

    public CircleProgressButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mListener != null && !mListener.onPreProgress(this)) {
                    return true;
                }
                mState = State.PRESSED;
                startProgressAnimator();
                return true;
            case MotionEvent.ACTION_UP:
                releaseProgress(true);
                //消除警告
                performClick();
                return true;
            case MotionEvent.ACTION_CANCEL:
                boolean callRelease = true;
                if (mListener != null) {
                    callRelease = mListener.onCancelProgress(this);
                }
                releaseProgress(callRelease);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        //消除警告
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int ringWidth = getRingWidth();
        int realWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int size = realWidth < realHeight ? realWidth : realHeight;
        checkInnerPadding(ringWidth, size);
        int radius = (size - ringWidth * 2 - mInnerPadding * 2) / 2;
        int currentX = getWidth() / 2;
        int currentY = getHeight() / 2;
        canvas.drawCircle(currentX, currentY, radius, mCirclePaint);
        if (getRingVisible()) {
            mRectF.set(currentX - radius - ringWidth / 2 - mInnerPadding,
                    currentY - radius - ringWidth / 2 - mInnerPadding,
                    currentX + radius + ringWidth / 2 + mInnerPadding,
                    currentY + radius + ringWidth / 2 + mInnerPadding);
            canvas.save();
            canvas.rotate(-90, currentX, currentY);
            canvas.drawArc(mRectF, 0, mEndAngle, false, mRingPaint);
            canvas.restore();
        }
    }

    /**
     * 重置控件资源
     */
    public void reset() {
        mState = State.IDLE;
        mEndAngle = 360;
        invalidate();
    }

    public int getMinProgress() {
        return mMinProgress;
    }

    public void setMinProgress(int minProgress) {
        this.mMinProgress = minProgress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.mCurrentProgress = currentProgress;
    }

    public int getIdleCircleColor() {
        return mIdleCircleColor;
    }

    public void setIdleCircleColor(@ColorInt int idleCircleColor) {
        this.mIdleCircleColor = idleCircleColor;
        invalidate();
    }

    public int getPressedCircleColor() {
        return mPressedCircleColor;
    }

    public void setPressedCircleColor(@ColorInt int pressedCircleColor) {
        this.mPressedCircleColor = pressedCircleColor;
        invalidate();
    }

    public int getReleasedCircleColor() {
        return mReleasedCircleColor;
    }

    public void setReleasedCircleColor(@ColorInt int releasedCircleColor) {
        this.mReleasedCircleColor = releasedCircleColor;
        invalidate();
    }

    public int getIdleRingColor() {
        return mIdleRingColor;
    }

    public void setIdleRingColor(@ColorInt int idleRingColor) {
        this.mIdleRingColor = idleRingColor;
        invalidate();
    }

    public int getPressedRingColor() {
        return mPressedRingColor;
    }

    public void setPressedRingColor(@ColorInt int pressedRingColor) {
        this.mPressedRingColor = pressedRingColor;
        invalidate();
    }

    public int getReleasedRingColor() {
        return mReleasedRingColor;
    }

    public void setReleasedRingColor(@ColorInt int releasedRingColor) {
        this.mReleasedRingColor = releasedRingColor;
        invalidate();
    }

    public int getIdleRingWidth() {
        return mIdleRingWidth;
    }

    public void setIdleRingWidth(int idleRingWidth) {
        this.mIdleRingWidth = idleRingWidth;
        invalidate();
    }

    public int getPressedRingWidth() {
        return mPressedRingWidth;
    }

    public void setPressedRingWidth(int pressedRingWidth) {
        this.mPressedRingWidth = pressedRingWidth;
        invalidate();
    }

    public int getReleasedRingWidth() {
        return mReleasedRingWidth;
    }

    public void setReleasedRingWidth(int releasedRingWidth) {
        this.mReleasedRingWidth = releasedRingWidth;
        invalidate();
    }

    public int getIdleInnerPadding() {
        return mIdleInnerPadding;
    }

    public void setIdleInnerPadding(int idleInnerPadding) {
        mIdleInnerPadding = idleInnerPadding;
    }

    public int getPressedInnerPadding() {
        return mPressedInnerPadding;
    }

    public void setPressedInnerPadding(int pressedInnerPadding) {
        mPressedInnerPadding = pressedInnerPadding;
    }

    public int getReleasedInnerPadding() {
        return mReleasedInnerPadding;
    }

    public void setReleasedInnerPadding(int releasedInnerPadding) {
        mReleasedInnerPadding = releasedInnerPadding;
    }

    public boolean isIdleRingVisible() {
        return mIdleRingVisible;
    }

    public void setIdleRingVisible(boolean idleRingVisible) {
        this.mIdleRingVisible = idleRingVisible;
        invalidate();
    }

    public boolean isPressedRingVisible() {
        return mPressedRingVisible;
    }

    public void setPressedRingVisible(boolean pressedRingVisible) {
        this.mPressedRingVisible = pressedRingVisible;
        invalidate();
    }

    public boolean isReleasedRingVisible() {
        return mReleasedRingVisible;
    }

    public void setReleasedRingVisible(boolean releasedRingVisible) {
        this.mReleasedRingVisible = releasedRingVisible;
        invalidate();
    }

    public OnCircleProgressButtonListener getOnCircleProgressButtonListener() {
        return mListener;
    }

    public void setOnCircleProgressButtonListener(OnCircleProgressButtonListener listener) {
        mListener = listener;
    }

    public boolean isTimerMode() {
        return mTimerMode;
    }

    /**
     * 释放进度加载
     *
     * @param callRelease 是否调用释放回调
     */
    private void releaseProgress(boolean callRelease) {
        if (mProgressAnimator != null) {
            if (mProgressAnimator.isRunning()) {
                mProgressAnimator.cancel();
            }
            mProgressAnimator = null;
        }
        setReleasedState(callRelease);
    }

    /**
     * 设置控件释放状态
     *
     * @param callRelease 是否调用释放回调
     */
    private void setReleasedState(boolean callRelease) {
        mState = State.RELEASED;
        mEndAngle = 360;
        if (mListener != null) {
            int angle = callRelease ? mListener.onReleaseProgress(this) : 360;
            if (angle >= 0 && angle <= 360) {
                mEndAngle = angle;
            }
        }
        invalidate();
    }

    /**
     * 启动进度动画
     */
    private void startProgressAnimator() {
        if (mProgressAnimator != null) {
            mProgressAnimator.cancel();
            mProgressAnimator = null;
        }
        int end;
        if (mTimerMode) {
            end = mMaxProgress;
        } else {
            end = mCurrentProgress;
        }
        mProgressAnimator = ValueAnimator.ofFloat(0, end);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                mEndAngle = progress / mMaxProgress * 360;
                if (mListener != null) {
                    mListener.onProgress(CircleProgressButton.this, progress);
                }
                invalidate();
            }
        });
        mProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setReleasedState(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        if (mTimerMode) {
            mProgressAnimator.setDuration(mMaxProgress * 1000);
        } else {
            mProgressAnimator.setDuration(mProgressDuration);
        }
        mProgressAnimator.start();
    }

    /**
     * 初始化相关配置
     *
     * @param context 上下文
     * @param attrs   属性资源
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        initAttrs(context, attrs);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF();
        mEndAngle = 360;
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressButton);
        if (a != null) {
            mIdleCircleColor = a.getColor(R.styleable.CircleProgressButton_cpb_idle_circle_color,
                    DEFAULT_IDLE_CIRCLE_COLOR);
            mPressedCircleColor = a.getColor(R.styleable.CircleProgressButton_cpb_pressed_circle_color,
                    DEFAULT_PRESSED_CIRCLE_COLOR);
            mReleasedCircleColor = a.getColor(R.styleable.CircleProgressButton_cpb_released_circle_color,
                    DEFAULT_RELEASED_CIRCLE_COLOR);
            mIdleRingColor = a.getColor(R.styleable.CircleProgressButton_cpb_idle_ring_color,
                    DEFAULT_IDLE_RING_COLOR);
            mPressedRingColor = a.getColor(R.styleable.CircleProgressButton_cpb_pressed_ring_color,
                    DEFAULT_PRESSED_RING_COLOR);
            mReleasedRingColor = a.getColor(R.styleable.CircleProgressButton_cpb_released_ring_color,
                    DEFAULT_RELEASED_RING_COLOR);
            mIdleRingWidth = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_idle_ring_width,
                    DEFAULT_IDLE_RING_WIDTH);
            mPressedRingWidth = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_pressed_ring_width,
                    DEFAULT_PRESSED_RING_WIDTH);
            mReleasedRingWidth = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_pressed_ring_width,
                    DEFAULT_RELEASED_RING_WIDTH);
            mIdleInnerPadding = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_idle_inner_padding, DEFAULT_INNER_PADDING);
            mPressedInnerPadding = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_pressed_inner_padding, DEFAULT_INNER_PADDING);
            mReleasedInnerPadding = a.getDimensionPixelSize(R.styleable.CircleProgressButton_cpb_released_inner_padding, DEFAULT_INNER_PADDING);
            mIdleRingVisible = a.getBoolean(R.styleable.CircleProgressButton_cpb_idle_ring_visible,
                    true);
            mPressedRingVisible = a.getBoolean(R.styleable.CircleProgressButton_cpb_pressed_ring_visible,
                    true);
            mReleasedRingVisible = a.getBoolean(R.styleable.CircleProgressButton_cpb_released_ring_visible,
                    true);
            mMinProgress = a.getInt(R.styleable.CircleProgressButton_cpb_min_progress, 0);
            mMaxProgress = a.getInt(R.styleable.CircleProgressButton_cpb_max_progress, 100);
            mCurrentProgress = a.getInt(R.styleable.CircleProgressButton_cpb_current_progress, 0);
            mTimerMode = a.getBoolean(R.styleable.CircleProgressButton_cpb_is_timer_mode, true);
            mProgressDuration = a.getInt(R.styleable.CircleProgressButton_cpb_progress_duration, DEFAULT_PROGRESS_DURATION);
            a.recycle();
        }
    }

    /**
     * 根据控件状态判断当前外围圆环半径
     *
     * @return 返回圆环宽度
     */
    private int getRingWidth() {
        int ringWidth, circleColor, ringColor;
        switch (mState) {
            case PRESSED:
                ringWidth = mPressedRingWidth;
                circleColor = mPressedCircleColor;
                ringColor = mPressedRingColor;
                mInnerPadding = mPressedInnerPadding;
                break;
            case RELEASED:
                ringWidth = mReleasedRingWidth;
                circleColor = mReleasedCircleColor;
                ringColor = mReleasedRingColor;
                mInnerPadding = mReleasedInnerPadding;
                break;
            case IDLE:
            default:
                ringWidth = mIdleRingWidth;
                circleColor = mIdleCircleColor;
                ringColor = mIdleRingColor;
                mInnerPadding = mIdleInnerPadding;
                break;
        }
        mCirclePaint.setColor(circleColor);
        mRingPaint.setColor(ringColor);
        mRingPaint.setStrokeWidth(ringWidth);
        return ringWidth;
    }

    /**
     * 返回是否圆环可见
     *
     * @return 返回圆环的可见性
     */
    private boolean getRingVisible() {
        boolean isVisible;
        switch (mState) {
            case PRESSED:
                isVisible = mPressedRingVisible;
                break;
            case RELEASED:
                isVisible = mReleasedRingVisible;
                break;
            case IDLE:
            default:
                isVisible = mIdleRingVisible;
                break;
        }
        return isVisible;
    }

    /**
     * 检查内边距是否正常
     *
     * @param ringWidth 圆环宽度
     * @param size      控件大小
     */
    private void checkInnerPadding(int ringWidth, int size) {
        if (mInnerPadding < 0) {
            mInnerPadding = 0;
        }
        if (mInnerPadding * 2 > size - ringWidth * 2) {
            mInnerPadding = (size - ringWidth * 2) / 2;
        }
    }

    /**
     * 事件监听器
     */
    public interface OnCircleProgressButtonListener {

        /**
         * 当准备开始进度加载时回调
         *
         * @param v 控件本身
         * @return 返回是否开始进度加载，为true表示可以加载进度，否则不加载
         */
        boolean onPreProgress(CircleProgressButton v);

        /**
         * 进度回调
         *
         * @param v        控件本身
         * @param progress 当前加载进度
         */
        void onProgress(CircleProgressButton v, float progress);

        /**
         * 当释放进度时回调
         *
         * @param v 控件本身
         */
        int onReleaseProgress(CircleProgressButton v);

        /**
         * 当取消进度加载时回调
         *
         * @param v 控件本身
         * @return 返回true表示继续进行后续调用，否则返回false
         */
        boolean onCancelProgress(CircleProgressButton v);
    }

    /**
     * 控件状态
     */
    private enum State {
        IDLE, PRESSED, RELEASED
    }
}
