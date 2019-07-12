package com.mingyuechunqiu.recordermanager.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;

import com.mingyuechunqiu.recordermanager.framework.KeyBackCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/29
 *     desc   : 所有Activity基类
 *              继承自AppCompatActivity
 *     version: 1.0
 * </pre>
 */
public class BaseRecordVideoActivity extends AppCompatActivity implements KeyBackCallback {

    private List<OnKeyBackListener> mListeners;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListeners != null) {
            mListeners.clear();
            mListeners = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mListeners != null && mListeners.size() > 0) {
            for (KeyBackCallback.OnKeyBackListener listener : mListeners) {
                if (listener != null && listener.onClickKeyBack(event)) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 添加点击返回事件监听器
     *
     * @param listener 监听器
     */
    @Override
    public void addOnKeyBackListener(OnKeyBackListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }
}
