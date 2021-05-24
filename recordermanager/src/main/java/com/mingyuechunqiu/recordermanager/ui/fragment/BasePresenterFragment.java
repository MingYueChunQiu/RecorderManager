package com.mingyuechunqiu.recordermanager.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mingyuechunqiu.recordermanager.base.presenter.IBasePresenter;
import com.mingyuechunqiu.recordermanager.base.view.IBaseView;
import com.mingyuechunqiu.recordermanager.base.view.IViewAttachPresenter;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/25
 *     desc   : 所有MVP层Fragment的基类
 *              继承自Fragment
 *     version: 1.0
 * </pre>
 */
public abstract class BasePresenterFragment<V extends IBaseView, P extends IBasePresenter<V>> extends Fragment implements IViewAttachPresenter<P> {

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            getLifecycle().removeObserver(mPresenter);
            //不能放在onDestroyView中执行，因为像输入框失去焦点这种事件会在onDestroyView之后才被调用
            mPresenter = null;
        }
    }

    /**
     * 添加Present相关
     */
    @SuppressWarnings("unchecked")
    protected void attachPresenter() {
        bindPresenter(initPresenter());
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
            getLifecycle().addObserver(mPresenter);
        }
    }

    @Override
    public void bindPresenter(@NonNull P presenter) {
        mPresenter = presenter;
    }

    protected abstract P initPresenter();

    /**
     * 释放资源（在onDestroyView时调用）
     */
    protected abstract void releaseOnDestroyView();

    /**
     * 释放资源（在onDestroy时调用）
     */
    protected abstract void releaseOnDestroy();

    /**
     * Fragment回调
     */
    public interface FragmentCallback {

        void onCall(Fragment fragment, Bundle bundle);
    }
}
