package com.mingyuechunqiu.recordermanager.feature.main.detail

import android.hardware.Camera
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.mingyuechunqiu.recordermanager.R
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption
import com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.CameraType
import com.mingyuechunqiu.recordermanager.feature.main.detail.RecordVideoContract.Presenter
import com.mingyuechunqiu.recordermanager.feature.record.IRecorderManager
import com.mingyuechunqiu.recordermanager.feature.record.RecorderManagerFactory
import java.io.File
import java.io.IOException
import java.util.*

/**
 * <pre>
 * author : xyj
 * Github : https://github.com/MingYueChunQiu
 * e-mail : xiyujieit@163.com
 * time   : 2019/4/29
 * desc   : 录制视频MVP中P层
 * 继承自RecordVideoContract.Presenter
 * version: 1.0
</pre> *
 */
internal class RecordVideoPresenter : Presenter<RecordVideoContract.View<*>>() {

    private val mManager: IRecorderManager by lazy { RecorderManagerFactory.newInstance() }
    private var mOption: RecordVideoOption? = null
    private var mCamera: Camera? = null
    private var isRecording = false//标记是否正在录制中
    private var isReleaseRecord = false//标记是否已经释放了资源
    private var mTiming: Long = 0 //计时数值
    private var needStopDelayed = false//标记是否需要延迟停止
    private var isInPlayingState = false//标记是否处于播放视频状态
    private var mMediaPlayer: MediaPlayer? = null
    private val mHandler: MyHandler by lazy { MyHandler(this) }
    private var hasHandledReleaseRecord = false //标记是否处理了录制释放事件
    private var mVideoDuration = 0 //录制视频时长（毫秒）
    private var mCameraType: CameraType = CameraType.CAMERA_NOT_SET//摄像头类型
    private var mTimer: Timer? = null//录制计时器

    public override fun initConfiguration(option: RecordVideoOption) {
        mOption = option
        mCameraType = mOption?.cameraType ?: CameraType.CAMERA_NOT_SET
    }

    /**
     * 开始图像预览
     */
    public override fun startPreview(holder: SurfaceHolder?) {
        holder?.let {
            if (mCamera == null) {
                mCamera = mManager.initCamera(mCameraType, it)
                mCameraType = mManager.cameraType
            }
        }
    }

    /**
     * 按下按钮开始录制视频
     *
     * @return 如果成功开始录制返回true，否则返回false
     */
    public override fun pressToStartRecordVideo(holder: SurfaceHolder?, ivFlipCamera: AppCompatImageView,
                                                ivFlashlight: AppCompatImageView, ivBack: AppCompatImageView): Boolean {
        if (mCamera == null) {
            startPreview(holder)
        }
        if (isRecording) {
            return false
        }
        hasHandledReleaseRecord = false
        ivFlipCamera.visibility = View.GONE
        ivFlashlight.visibility = View.GONE
        ivBack.visibility = View.GONE
        isRecording = true
        isReleaseRecord = false
        releaseTiming()
        mTimer = Timer().apply {
            mTiming = -1
            schedule(object : TimerTask() {

                override fun run() {
                    mHandler.removeMessages(MSG_UPDATE_TIMING)
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMING)
                }
            }, 0, 1000)
        }
        startRecordVideo(holder)
        return true
    }

    /**
     * 释放按钮停止录制视频
     *
     * @param isCancel 是否是取消操作导致停止录制，true表示是，否则为false
     */
    public override fun releaseToStopRecordVideo(isCancel: Boolean) {
        //释放方法会执行两次，进行过滤
        if (hasHandledReleaseRecord) {
            return
        }
        hasHandledReleaseRecord = true
        mHandler.removeMessages(MSG_STOP_RECORD)
        if (checkIsRecordingDurationShort()) {
            needStopDelayed = true
        }
        val message = mHandler.obtainMessage()
        message.what = MSG_STOP_RECORD
        message.arg1 = if (isCancel) 1 else 0
        mHandler.sendMessageDelayed(message, if (needStopDelayed && isNeedDelayToStopWhenRecordingDurationShort()) 1200 else 0.toLong())
    }

    public override fun flipCamera(holder: SurfaceHolder?) {
        holder?.let {
            mCamera = mManager.flipCamera(it)
            mCameraType = mManager.cameraType

            //切换到前置摄像头时隐藏闪光灯设置
            mViewRef?.get()?.showFlashlightButton(mCameraType != CameraType.CAMERA_FRONT)
        }
    }

    /**
     * 开始录制视频
     */
    private fun startRecordVideo(holder: SurfaceHolder?) {
        holder?.let {
            val surface = it.surface ?: return
            if (mCamera == null) {
                mCamera = mManager.initCamera(mCameraType, it)
                mCameraType = mManager.cameraType
            }
            if (mCameraType == CameraType.CAMERA_FRONT) {
                mOption?.recorderOption?.orientationHint = 270
            } else {
                mOption?.recorderOption?.orientationHint = 90
            }
            isRecording = mManager.recordVideo(mCamera, surface, mOption?.recorderOption)
        }
    }

    /**
     * 停止录制视频，返回是否录制成功
     *
     * @return 录制成功返回true，否则返回false
     */
    public override fun stopRecordVideo(): Boolean {
        if (!isRecording) {
            return false
        }
        releaseRecorderManager()
        var isRecordSuccessful = true //标记记录录制是否成功
        if (checkIsRecordingDurationShort()) {
            showErrorToast()
            isRecordSuccessful = false
        }
        releaseTiming()
        if (isRecordSuccessful) {
            //停顿200毫秒，确保写入数据结束完成
            try {
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        isRecording = false
        return isRecordSuccessful
    }

    /**
     * 释放计时资源
     */
    public override fun releaseTiming() {
        mTimer?.cancel()
        mTimer = null
        mTiming = 0
    }

    /**
     * 播放录制好的视频
     */
    public override fun playVideo() {
        val holder = mViewRef?.get()?.surfaceHolder ?: return
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer().apply {
                setScreenOnWhilePlaying(true)
            }
        }
        mMediaPlayer?.let {
            try {
                it.setDataSource(mOption!!.recorderOption.filePath)
                it.isLooping = true
                it.setDisplay(holder)
                it.setOnPreparedListener { mp ->
                    mp.start()
                    mVideoDuration = mp.duration
                    onCompleteRecordVideo()
                }
                it.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            isInPlayingState = true
        }
    }

    public override fun pausePlayVideo(controlViews: Boolean, ivPlay: AppCompatImageView) {
        if (isInPlayingState && mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.pause()
            ivPlay.visibility = View.VISIBLE
        }
    }

    /**
     * 恢复播放视频
     */
    public override fun resumePlayVideo(controlViews: Boolean, ivPlay: AppCompatImageView, holder: SurfaceHolder?) {
        holder?.let {
            if (isInPlayingState) {
                mMediaPlayer?.setDisplay(it)
                mMediaPlayer?.start()
                ivPlay.visibility = View.GONE
            }
        }
    }

    public override fun controlPlayOrPauseVideo(ivPlay: AppCompatImageView, holder: SurfaceHolder?) {
        if (isInPlayingState) {
            if (mMediaPlayer?.isPlaying == true) {
                pausePlayVideo(true, ivPlay)
            } else {
                resumePlayVideo(true, ivPlay, holder)
            }
        }
    }

    /**
     * 释放播放资源
     */
    public override fun releaseMediaPlayer() {
        mMediaPlayer?.stop()
        mMediaPlayer?.reset()
        mMediaPlayer?.release()
        mMediaPlayer = null
        isInPlayingState = false
    }

    /**
     * 释放相机资源
     */
    public override fun releaseCamera() {
        mManager.releaseCamera()
        mCamera = null
    }

    /**
     * 重置资源进行下次拍摄
     */
    public override fun resetResource() {
        releaseMediaPlayer()
        startPreview(mViewRef?.get()?.surfaceHolder)
        mViewRef?.get()?.controlRecordOrPlayVisibility(false)
    }

    /**
     * 设置SurfaceHolder（在调用到onPause()方法后，SurfaceView会销毁重建，要重新设置）
     *
     * @param holder 图层控制
     */
    public override fun onSurfaceCreated(holder: SurfaceHolder, ivPlay: AppCompatImageView) {
        holder.setKeepScreenOn(true)
        if (!isInPlayingState) {
            startPreview(holder)
            return
        }
        mMediaPlayer?.setDisplay(holder)
        resumePlayVideo(false, ivPlay, holder)
    }

    /**
     * 点击确认录制视频事件
     */
    public override fun onClickConfirm() {
        mViewRef?.get()?.onClickConfirm(mOption?.recorderOption?.filePath, mVideoDuration)
    }

    /**
     * 点击取消按钮事件
     */
    public override fun onClickCancel() {
        mOption?.recorderOption?.filePath?.let {
            val file = File(it)
            if (file.exists()) {
                file.delete()
            }
        }
        mViewRef?.get()?.onClickCancel(mOption?.recorderOption?.filePath, mVideoDuration)
    }

    /**
     * 点击返回键事件
     */
    public override fun onClickBack() {
        if (isInPlayingState) {
            resetResource()
            onClickCancel()
        } else {
            mViewRef?.get()?.onClickBack()
        }
    }

    public override fun getTimingHint(timing: String, isInTiming: Boolean): String {
        val context = mViewRef?.get()?.currentContext ?: return ""
        return if (isInTiming) {
            context.getString(R.string.rm_fill_record_timing, timing)
        } else {
            mOption?.timingHint ?: context.getString(R.string.rm_fill_record_timing, "00")
        }
    }

    public override fun switchFlashlightState(turnOn: Boolean): Boolean {
        return mManager.switchFlashlight(turnOn)
    }

    override fun release() {
        mHandler.removeCallbacksAndMessages(null)
        stopRecordVideo()
        isRecording = false
        isReleaseRecord = false
        needStopDelayed = false
        mTiming = 0
        mVideoDuration = 0
        releaseMediaPlayer()
        mOption = null
        mCameraType = CameraType.CAMERA_NOT_SET
    }

    private fun updateTiming() {
        //如果已经结束录制，则退出计时
        if (isReleaseRecord) {
            return
        }
        mTiming++
        val sbTiming = StringBuilder(mTiming.toString() + "")
        if (mTiming < 10) {
            sbTiming.insert(0, "0")
        }
        mViewRef?.get()?.showTimingText(getTimingHint(sbTiming.toString(), true))
    }

    /**
     * 释放录制管理器
     */
    private fun releaseRecorderManager() {
        if (!isReleaseRecord) {
            mManager.release()
            mCamera = null
            isReleaseRecord = true
        }
    }

    /**
     * 当完成一次录制时回调
     */
    private fun onCompleteRecordVideo() {
        mViewRef?.get()?.onCompleteRecordVideo(mOption?.recorderOption?.filePath, mVideoDuration)
    }


    private fun checkIsRecordingDurationShort(): Boolean {
        return mTiming < getRecordingMinDuration()
    }

    private fun getRecordingMinDuration(): Int {
        return mOption?.minDuration ?: DEFAULT_RECORDING_MIN_DURATION
    }

    private fun isNeedDelayToStopWhenRecordingDurationShort(): Boolean {
        return mTiming < DEFAULT_RECORDING_MIN_DURATION
    }

    private fun showErrorToast() {
        val context = mViewRef?.get()?.currentContext ?: return
        var msg = mOption?.errorToastMsg
        if (TextUtils.isEmpty(msg)) {
            msg = context.getString(R.string.rm_fill_warn_record_time_too_short, getRecordingMinDuration())
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setRecordOrPlayVisible() {
        mViewRef?.get()?.controlRecordOrPlayVisibility(true)
    }

    private class MyHandler(private val mPresenter: RecordVideoPresenter) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_UPDATE_TIMING -> mPresenter.updateTiming()
                MSG_STOP_RECORD -> {
                    if (mPresenter.needStopDelayed) {
                        mPresenter.needStopDelayed = false
                        //小于规定时长，不进入播放环节，重置资源
                        mPresenter.showErrorToast()
                        mPresenter.releaseRecorderManager()
                        mPresenter.releaseTiming()
                        mPresenter.isRecording = false
                        mPresenter.resetResource()
                    } else {
                        if (mPresenter.stopRecordVideo() && msg.arg1 == 0) {
                            mPresenter.playVideo()
                            mPresenter.setRecordOrPlayVisible()
                        }
                    }
                }
            }
        }
    }

    companion object {

        //默认最小为1秒，防止用户按下就抬起，导致MediaRecorder初始化还没完成就release导致报错
        private const val DEFAULT_RECORDING_MIN_DURATION = 1

        //更新计时
        private const val MSG_UPDATE_TIMING = 0x01

        //停止录制
        private const val MSG_STOP_RECORD = 0x02
    }
}