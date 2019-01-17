# RecorderManager
Android录制音视频的管理工具，支持自定义，尚未完善，可以简单使用。

1.Add it in your root build.gradle at the end of repositories
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the dependency
```
 ependencies {
	        implementation 'com.github.MingYueChunQiu:RecorderManager:0.1.3'
	}
```
3.调用音频录制
采用默认配置录制
```mRecorderManager.recordAudio(mFilePath);```
自定义配置参数录制
```mRecorderManager.recordAudio(new RecorderBean.Builder()
                        .setAudioSource(MediaRecorder.AudioSource.MIC)
                        .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                        .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        .setAudioSamplingRate(44100)
                        .setBitRate(96000)
                        .setFilePath(mFilePath)
                        .build());
```
结束录制
```
if (mRecorderManager != null) {
            mRecorderManager.release();
            mRecorderManager = null;
}
```
4.调用视频录制（待完善）
	
