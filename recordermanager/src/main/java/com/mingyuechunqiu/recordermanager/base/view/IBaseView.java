package com.mingyuechunqiu.recordermanager.base.view;

import androidx.annotation.NonNull;

import com.mingyuechunqiu.recordermanager.base.presenter.IBasePresenter;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/25
 *     desc   : 所有view视图接口的父接口
 *              这儿是简写的MVP，更详细的信息可以看我的
 *              https://github.com/MingYueChunQiu/AgileMVPFrame.git
 *     version: 1.0
 * </pre>
 */
public interface IBaseView<P extends IBasePresenter> {

    void setPresenter(@NonNull P presenter);
}
