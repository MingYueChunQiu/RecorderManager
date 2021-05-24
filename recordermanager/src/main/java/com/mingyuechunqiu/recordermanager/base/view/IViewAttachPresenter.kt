package com.mingyuechunqiu.recordermanager.base.view

import com.mingyuechunqiu.recordermanager.base.presenter.IBasePresenter

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/23 11:45 下午
 *      Desc:       View层关联Presenter层标准接口
 *      Version:    1.0
 * </pre>
 */
interface IViewAttachPresenter<P : IBasePresenter<out IBaseView>> {

    /**
     * 绑定Presenter
     *
     * @param presenter 外部传入的Presenter
     */
    fun bindPresenter(presenter: P)
}