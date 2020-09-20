# RecorderManager





因为在项目中经常需要使用音视频录制，所以写了一个公共库RecorderManager，欢迎大家使用。

最新0.3-beta.2版本更新：详情见文档</br>
1.重构项目代码，kotlin改写部分功能
2.移除rxjava库，减少依赖
3.升级最新SDK
4.新增闪光灯功能，增加计时前提示文本设置
5.修复已知问题，优化代码
6.对外用户调用API改动较少，主要为内部调整，见下方文档，欢迎大家测试反馈完善功能

0.2.29版本更新：
1.新增圆形进度按钮配置功能</br>
2.新增指定前后置摄像头功能</br>
3.优化代码，调整启动视频录制配置项</br>

0.2.28版本更新：</br>
1.优化视频录制结果获取方式</br>
2.优化代码</br>

0.2.27版本更新：</br>
1.视频录制界面RecordVideoRequestOption新增RecorderOption和hideFlipCameraButton配置</br>
2.优化代码</br>

0.2.26版本更新：</br> 
1.项目迁移至AndroidX， 引入Kotlin</br>

0.2.25版本更新：</br>
1.优化权限自动申请，可自动调起视频录制界面</br>
2.规范图片资源命名</br>

## 一.效果展示
仿微信界面视频录制
![在这里插入图片描述](https://user-gold-cdn.xitu.io/2019/2/12/168df9c38e4d10d3?w=1080&h=2280&f=jpeg&s=1065098)
![在这里插入图片描述](https://user-gold-cdn.xitu.io/2019/1/29/1689916e86ee15a3?w=1080&h=2280&f=jpeg&s=819744)
2.音频录制界面比较简单，就不放图了
## 二.引用
1.Add it in your root build.gradle at the end of repositories
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2.Add the dependency

```
dependencies {
		//0.3-beta.2
	        implementation 'com.github.MingYueChunQiu:RecorderManager:0.2.29'
	}
```
## 三.使用
### 1.音频录制
采用默认配置录制
```
mRecorderManager.recordAudio(mFilePath);
```
自定义配置参数录制

```
mRecorderManager.recordAudio(new RecorderOption.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    .setAudioSamplingRate(44100)
                    .setBitRate(96000)
                    .setFilePath(path)
                    .build());
```
### 2.视频录制
#### (1).可以直接使用RecordVideoActivity，实现了仿微信风格的录制界面
从0.2.18开始改为类似

```
RecorderManagerFactory.getRecordVideoRequest().startRecordVideo(MainActivity.this, 0);
```
RecorderManagerFactory中可以拿到RequestRecordVideoPageable，在RequestRecordVideoPageable接口中

```
/**
     * 以默认配置打开录制视频界面
     *
     * @param activity    Activity
     * @param requestCode 请求码
     */
    void startRecordVideo(@NonNull FragmentActivity activity, int requestCode);

    /**
     * 以默认配置打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     */
    void startRecordVideo(@NonNull Fragment fragment, int requestCode);

    /**
     * 打开录制视频界面
     *
     * @param activity    Activity
     * @param requestCode 请求码
     * @param option      视频录制请求配置信息类
     */
    void startRecordVideo(@NonNull FragmentActivity activity, int requestCode, @Nullable RecordVideoRequestOption option);

    /**
     * 打开录制视频界面
     *
     * @param fragment    Fragment
     * @param requestCode 请求码
     * @param option      视频录制请求配置信息类
     */
    void startRecordVideo(@NonNull Fragment fragment, int requestCode, @Nullable RecordVideoRequestOption option);
```
RecordVideoRequestOption可配置最大时长（秒）和文件保存路径

```
public class RecordVideoRequestOption implements Parcelable {

        private String filePath;//文件保存路径
        private int maxDuration;//最大录制时间（秒数）
        private RecordVideoOption recordVideoOption;//录制视频配置信息类（里面配置的filePath和maxDuration会覆盖外面的）
}
```

RecordVideoActivity里已经配置好了默认参数，可以直接使用，然后在onActivityResult里拿到视频路径的返回值
返回值为RecordVideoResultInfo
```
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
        RecordVideoResultInfo info = data.getParcelableExtra(EXTRA_RECORD_VIDEO_RESULT_INFO);
	  
	//从0.2.28版本开始可以使用下面这种方式，更安全更灵活，兼容性强
	RecordVideoResultInfo info = RecorderManagerFactory.getRecordVideoResult(data);
	//从0.3版本开始
	RecordVideoResultInfo info = RecorderManagerFactory.getRecordVideoResultParser().parseRecordVideoResult(data);
	
	if (info != null) {
                Log.e("MainActivity", "onActivityResult: " + " "
                        + info.getDuration() + " " + info.getFilePath());
            }
        }
    }
```

#### (2).如果想要界面一些控件的样式，可以继承RecordVideoActivity，里面提供了几个protected方法，可以拿到界面的一些控件

```
/**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    protected AppCompatTextView getTimingView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getTimingView();
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    protected CircleProgressButton getCircleProgressButton() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCircleProgressButton();
    }

	/**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    public AppCompatImageView getFlipCameraView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getFlipCameraView();
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    protected AppCompatImageView getPlayView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getPlayView();
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    protected AppCompatImageView getCancelView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCancelView();
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    protected AppCompatImageView getConfirmView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getConfirmView();
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    protected AppCompatImageView getBackView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getBackView();
    }
```
想要替换图标资源的话，提供下列名称图片

```
rm_record_video_flip_camera.png
rm_record_video_cancel.png
rm_record_video_confirm.png
rm_record_video_play.png
rm_record_video_pull_down.png
rm_record_video_flashlight_turn_off.png
rm_record_video_flashlight_turn_on.png
```

#### (3).同时提供了对应的RecordVideoFragment，实现与RecordVideoActivity同样的功能，实际RecordVideoActivity就是包裹了一个RecordVideoFragment
1.创建RecordVideoFragment

```
/**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(@Nullable String filePath) {
        return newInstance(filePath, 30);
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath    存储文件路径
     * @param maxDuration 最大时长（秒数）
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(@Nullable String filePath, int maxDuration) {
        return newInstance(new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(filePath))
                .setMaxDuration(maxDuration)
                .build());
    }

    /**
     * 获取录制视频Fragment实例
     *
     * @param option 录制配置信息对象
     * @return 返回RecordVideoFragment
     */
    public static RecordVideoFragment newInstance(@Nullable RecordVideoOption option) {
        RecordVideoFragment fragment = new RecordVideoFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA_RECORD_VIDEO_OPTION, option == null ? new RecordVideoOption() : option);
        fragment.setArguments(args);
        return fragment;
    }
```
2.然后添加RecordVideoFragment到自己想要的地方就可以了
3.可以设置OnRecordVideoListener，拿到各个事件的回调

```
public class RecordVideoOption：

	private RecorderOption recorderOption;//录制配置信息
        private RecordVideoButtonOption recordVideoButtonOption;//录制视频按钮配置信息类
        private int maxDuration;//最大录制时间（秒数）
        private RecorderManagerConstants.CameraType cameraType;//摄像头类型
        private boolean hideFlipCameraButton;//隐藏返回翻转摄像头按钮
        private OnRecordVideoListener listener;//录制视频监听器(直接使用RecorderManagerFactory启动视频录制界面，不需要设置此项)

	/**
     * 录制视频监听器
     */
    public interface OnRecordVideoListener {
		/**
         * 当完成一次录制时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onCompleteRecordVideo(String filePath, int videoDuration);

        /**
         * 当点击确认录制结果按钮时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onClickConfirm(String filePath, int videoDuration);

  /**
         * 当点击取消按钮时回调
         *
         * @param filePath      视频文件路径
         * @param videoDuration 视频时长（毫秒）
         */
        void onClickCancel(String filePath, int videoDuration);

        /**
         * 当点击返回按钮时回调
         */
        void onClickBack();
    }
```
原OnRecordVideoListener现已改为RMOnRecordVideoListener，并从RecordVideoOption中移除，主要用于用户自己activity或fragment实现此接口，用于承载RecordVideoFragment，获取相关步骤回调
```
interface RMOnRecordVideoListener {

    /**
     * 当完成一次录制时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onCompleteRecordVideo(filePath: String?, videoDuration: Int)

    /**
     * 当点击确认录制结果按钮时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onClickConfirm(filePath: String?, videoDuration: Int)

    /**
     * 当点击取消按钮时回调
     *
     * @param filePath      视频文件路径
     * @param videoDuration 视频时长（毫秒）
     */
    fun onClickCancel(filePath: String?, videoDuration: Int)

    /**
     * 当点击返回按钮时回调
     */
    fun onClickBack()
}
```
4.RecordVideoButtonOption是圆形进度按钮配置类
```
	private @ColorInt
        int idleCircleColor;//空闲状态内部圆形颜色
        private @ColorInt
        int pressedCircleColor;//按下状态内部圆形颜色
        private @ColorInt
        int releasedCircleColor;//释放状态内部圆形颜色
        private @ColorInt
        int idleRingColor;//空闲状态外部圆环颜色
        private @ColorInt
        int pressedRingColor;//按下状态外部圆环颜色
        private @ColorInt
        int releasedRingColor;//释放状态外部圆环颜色
        private int idleRingWidth;//空闲状态外部圆环宽度
        private int pressedRingWidth;//按下状态外部圆环宽度
        private int releasedRingWidth;//释放状态外部圆环宽度
        private int idleInnerPadding;//空闲状态外部圆环与内部圆形之间边距
        private int pressedInnerPadding;//按下状态外部圆环与内部圆形之间边距
        private int releasedInnerPadding;//释放状态外部圆环与内部圆形之间边距
        private boolean idleRingVisible;//空闲状态下外部圆环是否可见
        private boolean pressedRingVisible;//按下状态下外部圆环是否可见
        private boolean releasedRingVisible;//释放状态下外部圆环是否可见
```
5.RecorderOption是具体的录制参数配置类
```
	private int audioSource;//音频源
        private int videoSource;//视频源
        private int outputFormat;//输出格式
        private int audioEncoder;//音频编码格式
        private int videoEncoder;//视频编码格式
        private int audioSamplingRate;//音频采样频率（一般44100）
        private int bitRate;//视频编码比特率
        private int frameRate;//视频帧率
        private int videoWidth, videoHeight;//视频宽高
        private int maxDuration;//最大时长
        private long maxFileSize;//文件最大大小
        private String filePath;//文件存储路径
        private int orientationHint;//视频录制角度方向
```

#### (4).如果想自定义自己的界面，可以直接使用RecorderManagerable类
1.通过RecorderManagerFactory获取RecorderManagerable
```
public class RecorderManagerFactory {

    private RecorderManagerFactory() {
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance() {
        return newInstance(new RecorderHelper());
    }

    /**
     * 创建录制管理类实例（使用默认录制类）
     *
     * @param intercept 录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderManagerInterceptor intercept) {
        return newInstance(new RecorderHelper(), intercept);
    }

    /**
     * 创建录制管理类实例
     *
     * @param helper 实际录制类
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderHelper helper) {
        return newInstance(helper, null);
    }

    /**
     * 创建录制管理类实例
     *
     * @param helper    实际录制类
     * @param intercept 录制管理器拦截器
     * @return 返回录制管理类实例
     */
    @NonNull
    public static IRecorderManager newInstance(@NonNull IRecorderHelper helper, @Nullable IRecorderManagerInterceptor intercept) {
        return new RecorderManager(helper, intercept);
    }

    @NonNull
    public static IRecordVideoRequest getRecordVideoRequest() {
        return new RecordVideoPageRequest();
    }

    //0.3之后版本通过解析器来进行处理数据
    @NonNull
    public static IRecordVideoResultParser getRecordVideoResultParser() {
        return new RecordVideoResultParser();
    }
}
```
它们返回的都是IRecorderManager 接口类型，RecorderManager 是默认的实现类，RecorderManager 内持有一个真正进行操作的RecorderHelper。

```
public interface IRecorderManager extends IRecorderHelper {

    /**
     * 设置录制对象
     *
     * @param helper 录制对象实例
     */
    void setRecorderHelper(@NonNull IRecorderHelper helper);

    /**
     * 获取录制对象
     *
     * @return 返回录制对象实例
     */
    @NonNull
    IRecorderHelper getRecorderHelper();

    /**
     * 初始化相机对象
     *
     * @param holder Surface持有者
     * @return 返回初始化好的相机对象
     */
    @Nullable
    Camera initCamera(@NonNull SurfaceHolder holder);

    /**
     * 初始化相机对象
     *
     * @param cameraType 指定的摄像头类型
     * @param holder     Surface持有者
     * @return 返回初始化好的相机对象
     */
    @Nullable
    Camera initCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder);

    /**
     * 打开或关闭闪光灯
     *
     * @param turnOn true表示打开，false关闭
     */
    boolean switchFlashlight(boolean turnOn);

    /**
     * 翻转摄像头
     *
     * @param holder Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    @Nullable
    Camera flipCamera(@NonNull SurfaceHolder holder);

    /**
     * 翻转到指定类型摄像头
     *
     * @param cameraType 摄像头类型
     * @param holder     Surface持有者
     * @return 返回翻转并初始化好的相机对象
     */
    @Nullable
    Camera flipCamera(@NonNull RecorderManagerConstants.CameraType cameraType, @NonNull SurfaceHolder holder);

    /**
     * 获取当前摄像头类型
     *
     * @return 返回摄像头类型
     */
    @NonNull
    RecorderManagerConstants.CameraType getCameraType();

    /**
     * 释放相机资源
     */
    void releaseCamera();

}
```
RecorderManagerIntercept实现IRecorderManagerInterceptor接口，用户可以直接继承RecorderManagerIntercept，它里面所有方法都是空实现，可以自己改写需要的方法

```
public interface IRecorderManagerInterceptor extends ICameraInterceptor {
}
```
IRecorderHelper是一个接口类型，由实现IRecorderHelper的子类来进行录制操作，默认提供的是RecorderHelper，RecorderHelper实现了IRecorderHelper。

```
public interface IRecorderHelper {

    /**
     * 录制音频
     *
     * @param path 文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordAudio(@NonNull String path);

    /**
     * 录制音频
     *
     * @param option 存储录制信息的对象
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordAudio(@NonNull RecorderOption option);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param path    文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable String path);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param option  存储录制信息的对象
     * @return 返回是否成功开启视频录制，成功返回true，否则返回false
     */
    boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable RecorderOption option);

    /**
     * 释放资源
     */
    void release();

    /**
     * 获取录制器
     *
     * @return 返回实例对象
     */
    @NonNull
    MediaRecorder getMediaRecorder();

    /**
     * 获取配置信息对象
     *
     * @return 返回实例对象
     */
    @Nullable
    RecorderOption getRecorderOption();
}
```
2.拿到后创建相机对象

```
		if (mCamera == null) {
            mCamera = mManager.initCamera(mCameraType, svVideoRef.get().getHolder());
            mCameraType = mManager.getCameraType();
        }
```
3.录制

```
isRecording = mManager.recordVideo(mCamera, svVideoRef.get().getHolder().getSurface(), mOption.getRecorderOption());
```
4.释放

```
	    mManager.release();
            mManager = null;
            mCamera = null;
```
## 四.总结
目前来说，大体流程就是这样，更详细的信息请到Github上查看， 后期将添加闪光灯等更多功能，敬请关注，github地址为 https://github.com/MingYueChunQiu/RecorderManager ，码云地址为 https://gitee.com/MingYueChunQiu/RecorderManager ，如果它能对你有所帮助，请帮忙点个star，有什么建议或意见欢迎反馈。
