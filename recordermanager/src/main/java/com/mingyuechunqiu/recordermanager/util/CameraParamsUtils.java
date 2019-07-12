package com.mingyuechunqiu.recordermanager.util;

import android.hardware.Camera;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/3/8
 *     desc   : 相机参数工具类
 *     version: 1.0
 * </pre>
 */
public class CameraParamsUtils {

    private static volatile CameraParamsUtils sUtils;

    private SizeComparator mSizeComparator;

    public static CameraParamsUtils getInstance() {
        if (sUtils == null) {
            synchronized (CameraParamsUtils.class) {
                if (sUtils == null) {
                    sUtils = new CameraParamsUtils();
                }
            }
        }
        return sUtils;
    }

    private CameraParamsUtils() {
        mSizeComparator = new SizeComparator();
    }

    /**
     * 获取支持的合适大小
     *
     * @param list  相机大小集合
     * @param ratio 视频宽高比例
     * @return 如果获取成功返回宽高对，否则返回null
     */
    @Nullable
    public Pair<Integer, Integer> getSupportSize(List<Camera.Size> list, float ratio) {
        if (list == null || list.size() == 0) {
            return null;
        }
        //进行降序排序
        Collections.sort(list, mSizeComparator);
        int selectedIndex = 0;//选中分辨率索引位置
        float ratioDelta = 0;//选中的分辨率比例与视频比例之间的差值
        for (int i = 0, count = list.size(); i < count; i++) {
            Camera.Size size = list.get(i);
            float currentRatioDelta = Math.abs(size.width * 1.0f / size.height - ratio);
            //如果是起始位置或者当前比例差值小于前面比例差值，更新选中项
            if (i == 0 || (currentRatioDelta < ratioDelta)) {
                ratioDelta = currentRatioDelta;
                selectedIndex = i;
            }
        }
        if (ratio <= 0) {
            selectedIndex = 0;
        }
        return new Pair<>(list.get(selectedIndex).width, list.get(selectedIndex).height);
    }

    /**
     * 获取支持的合适大小
     *
     * @param list      相机大小集合
     * @param minWidth  最小宽度
     * @param minHeight 最小高度
     * @return 如果获取成功返回宽高对，否则返回null
     */
    @Nullable
    public Pair<Integer, Integer> getSupportSize(List<Camera.Size> list, int minWidth, int minHeight) {
        if (list == null || list.size() == 0) {
            return null;
        }
        //进行降序排序
        Collections.sort(list, mSizeComparator);
        for (Camera.Size size : list) {
            if (size.width >= minWidth && size.height >= minHeight) {
                return new Pair<>(size.width, size.height);
            }
        }
        return null;
    }

    public void release() {
        mSizeComparator = null;
        sUtils = null;
    }

    /**
     * 大小比较器，以宽度为准
     */
    private class SizeComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            //降序
            return (o2.width < o1.width) ? -1 : ((o2.width == o1.width) ? 0 : 1);
        }
    }
}
