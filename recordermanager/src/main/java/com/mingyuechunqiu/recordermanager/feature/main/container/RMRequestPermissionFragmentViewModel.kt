package com.mingyuechunqiu.recordermanager.feature.main.container

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mingyuechunqiu.recordermanager.framework.RMRecordVideoResultCallback

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/26 10:05 下午
 *      Desc:       请求权限Fragment所持有ViewModel
 *                  继承自ViewModel
 *      Version:    1.0
 * </pre>
 */
class RMRequestPermissionFragmentViewModel : ViewModel() {

    private val mResultCallback = MutableLiveData<RMRecordVideoResultCallback>()

    fun getRecordVideoResultCallback(): RMRecordVideoResultCallback? {
        return mResultCallback.value
    }

    fun updateRecordVideoResultCallback(callback: RMRecordVideoResultCallback) {
        mResultCallback.value = callback
    }
}