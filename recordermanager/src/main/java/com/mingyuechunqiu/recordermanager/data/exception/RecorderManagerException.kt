package com.mingyuechunqiu.recordermanager.data.exception

/**
 * <pre>
 *      Project:    RecorderManager
 *
 *      Author:     xiyujie
 *      Github:     https://github.com/MingYueChunQiu
 *      Email:      xiyujieit@163.com
 *      Time:       2021/5/26 9:37 下午
 *      Desc:       库异常类
 *                  继承自Exception
 *      Version:    1.0
 * </pre>
 */
class RecorderManagerException(val errorCode: Int, message: String?) : Exception(message) {
}