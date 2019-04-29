package com.mingyuechunqiu.recordermanager.framework;

import android.view.KeyEvent;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/29
 *     desc   : 返回按键回调接口
 *     version: 1.0
 * </pre>
 */
public interface KeyBackCallback {

    void addOnKeyBackListener(OnKeyBackListener listener);

    /**
     * 返回按键监听器
     */
    interface OnKeyBackListener {

        /**
         * 当点击返回按键时回调
         *
         * @param event 按键对象
         * @return 如果拦截返回键处理返回true，否则返回false
         */
        boolean onClickKeyBack(KeyEvent event);
    }
}
