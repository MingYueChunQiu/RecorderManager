package com.mingyuechunqiu.recordermanager.record;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制接口，约束要实现的方法
 *     version: 1.0
 * </pre>
 */
public interface Recorderable {

    boolean recordAudio(String path);

    boolean recordAudio(RecorderOption bean);

    boolean recordVideo(Camera camera, Surface surface, String path);

    boolean recordVideo(Camera camera, Surface surface, RecorderOption bean);

    void release();

    MediaRecorder getMediaRecorder();
}
