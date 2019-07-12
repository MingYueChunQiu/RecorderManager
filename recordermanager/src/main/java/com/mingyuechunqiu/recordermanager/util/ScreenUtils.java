package com.mingyuechunqiu.recordermanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * <pre>
 *     author : xyj
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2018/03/27
 *     desc   : 屏幕相关的工具类
 *     version: 1.0
 * </pre>
 */

public class ScreenUtils {

    /**
     * 获取item平均宽度
     *
     * @param context 上下文
     * @param radio   截取的屏幕宽度
     * @param count   item个数
     * @return 返回平分后的item宽度
     */
    public static int getItemWidth(@NonNull Context context, float radio, int count) {
        return (int) (context.getResources().getDisplayMetrics().widthPixels * radio / count);
    }

    /**
     * 将dp转换为px
     *
     * @param resources 资源管理器
     * @param dpVal     dp值
     * @return 返回px值
     */
    public static float getPxFromDp(@NonNull Resources resources, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.getDisplayMetrics());
    }

    /**
     * 将sp转换成px
     *
     * @param resources 资源管理器
     * @param spVal     sp值
     * @return 返回px值
     */
    public static float getPxFromSp(@NonNull Resources resources, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, resources.getDisplayMetrics());
    }

    /**
     * 设置文本控件字体大小
     *
     * @param textView 控件
     * @param spVal    文本的sp大小
     */
    public static void setTextSize(@NonNull TextView textView, int spVal) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, spVal);
    }

    /**
     * 设置控件的外边距
     *
     * @param layoutParams 控件的布局参数
     * @param resources    资源管理器
     * @param left         左边距
     * @param top          上边距
     * @param right        右边距
     * @param bottom       下边距
     */
    public static void setMargins(@NonNull ViewGroup.MarginLayoutParams layoutParams, Resources resources,
                                  int left, int top, int right, int bottom) {
        layoutParams.setMargins((int) ScreenUtils.getPxFromDp(resources, left),
                (int) ScreenUtils.getPxFromDp(resources, top),
                (int) ScreenUtils.getPxFromDp(resources, right),
                (int) ScreenUtils.getPxFromDp(resources, bottom));
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 返回状态栏高度
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 获取底部虚拟导航栏高度
     *
     * @param context 上下文
     * @return 返回底部虚拟导航栏高度
     */
    public static int getNavigationBarHeight(@NonNull Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * 隐藏当前获取焦点view的软键盘
     *
     * @param activity 软键盘所在界面
     */
    public static void hideFocusedSoftInput(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        hideViewSoftInput(activity, view);
    }

    /**
     * 隐藏指定view的软键盘
     *
     * @param activity 软键盘所在界面
     * @param view     指定的view
     */
    public static void hideViewSoftInput(@NonNull Activity activity, @NonNull View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
