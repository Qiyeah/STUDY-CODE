#  **Android ijkplayer的使用解析** 
---
ijkplayer是Bilibili基于ffmpeg开发并开源的轻量级视频播放器，支持播放本地网络视频，也支持流媒体播放。支持Android&iOS。

ijkplayer的编译这里不多阐述，我也是直接获取别人编译完成的so库文件，直接使用的。如果你对ijkplayer的编译感兴趣，可以百度一下，有很多文章。

## 使用ijkplayer

###* **1.导包**

ijkplayer源码官方下载地址：[https://github.com/Bilibili/ijkplayer](https://github.com/Bilibili/ijkplayer )

上面是官方提供的ijkplayer的源码地址，但是它是没有编译过的。下面我给大家分享一份编译好的ijkplayer源码，由于比较大，分了三个包才上传完成，需要三个包都下载后才能一起解压： 

**[编译好的ijkplayer.part1 ](http://download.csdn.net/detail/huaxun66/9705290)**

**[编译好的ijkplayer.part2 ](http://download.csdn.net/detail/huaxun66/9705293)**

**[编译好的ijkplayer.part3 ](http://download.csdn.net/detail/huaxun66/9705297 )**

我们下载完成，进入android/ijkplayer目录：

![alt text](http://img.blog.csdn.net/20161129220820865)

ijkplayer-java：ijkplayer的一些操作封装及定义。这里面是通用的API接口，里面最主要的是IMediaPlayer，它是用来渲染显示多媒体的。

ijkplayer-exo：google开源的一个新的播放器ExoPlayer，在Demo中和ijkplayer对比用的。通过安装ijkplayer可以发现setting里面可以选择不同player来渲染多媒体显示，该模块下面就是一个MediaPlayer。  

ijkplayer-example：测试程序	  
	
ijkplayer-{arch}：编译出来的各个版本的.so文件。		  

官方提供的Demo的代码还是挺多的，甚至还用了otto,需要对官方的demo进行精简，去除一些用不到的代码。 		  

首先需要的是ijkplayer-{arch}、ijkplayer-java两个库。exo是Google提供的新的播放器，这里不需要，直接砍掉。其次是ijkplayer-example里的，我们需要的只有tv.danmaku.ijk.media.example.widget.media包下的部分类。	
	   
**注：** 

链接库ijkplayer-arm64，ijkplayer-armv5，ijkplayer-armv7a，ijkplayer-x86，ijkplayer-x86_64是不同体系架构的动态链接库，在当前工程结构里面作为一个模块，如果不想做兼容多平台问题，可以删除其他目录结构，单独保留自己需要的平台目录。

新建一个工程： 

（1）把ijkplayer-armv7a/src/main/libs下的文件拷贝到新工程app目录的libs下。 

（2）把ijkplayer-java/build/outputs/aar/ijkplayer-java-release.aar复制到新工程app目录的libs下。 

（3）修改APP下的build.gradle, 主要设置.so及.aar的位置:
		
	apply plugin: 'com.android.application'

	android {
	    compileSdkVersion 24
	    buildToolsVersion "25.0.0"
	    defaultConfig {
	        applicationId "com.hx.ijkplayer_demo"
	        minSdkVersion 14
	        targetSdkVersion 24
	        versionCode 1
	        versionName "1.0"
	        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
	    }
	    buildTypes {
	        release {
	            minifyEnabled false
	            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
	        }
	    }
	    sourceSets {
	        main {
	            jniLibs.srcDirs = ['libs']  /**在libs文件夹下找so文件*/
	        }
	    }
	}

	repositories {
	    mavenCentral()
	    flatDir {
	        dirs 'libs' /**在libs文件夹下找aar文件*/
	    }
	}
	
	dependencies {
	    compile fileTree(dir: 'libs', include: ['*.jar'])
	    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
	        exclude group: 'com.android.support', module: 'support-annotations'
	    })
	    compile 'com.android.support:appcompat-v7:24.2.1'
	    testCompile 'junit:junit:4.12'
	    compile(name: 'ijkplayer-java-release', ext: 'aar') /**编译ijkplayer-java-release.aar文件*/
	}

（4）复制ijkplayer-example下面的tv.danmaku.ijk.media.example.widget.media到新的工程，删掉一些不需要类。 

![alt text](http://img.blog.csdn.net/20161130171122997)

（5）IjkVideoView里面还是有很多如exo等没用的东西，删！具体可以参见我后面的Demo。

（6）Manifest

	...
	<activity android:name=".MainActivity"
	          android:screenOrientation="sensorLandscape"
	          android:configChanges="orientation|keyboardHidden">
	...
	</activity>
	...
	<uses-permission android:name="android.permission.INTERNET"/>

**播放实现**

#### *Xml代码

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context=".MainActivity">
	
	    <com.hx.ijkplayer_demo.widget.media.IjkVideoView
	        android:id="@+id/video_view"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"/>
	</RelativeLayout>
	
#### *Java代码
	public class MainActivity extends AppCompatActivity {
	
	    private IjkVideoView videoView;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        videoView = (IjkVideoView) findViewById(R.id.video_view);
	        videoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
	        videoView.setVideoURI(Uri.parse("http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8"));
	        videoView.start();
	    }
	}

**常用函数**

	/**
	 * 参数aspectRatio(缩放参数)参见IRenderView的常量：IRenderView.AR_ASPECT_FIT_PARENT,
	IRenderView.AR_ASPECT_FILL_PARENT,
	IRenderView.AR_ASPECT_WRAP_CONTENT,
	IRenderView.AR_MATCH_PARENT,
	IRenderView.AR_16_9_FIT_PARENT,
	IRenderView.AR_4_3_FIT_PARENT
	 */
	public void setAspectRatio(int aspectRatio);
	//改变视频缩放状态。
	public int toggleAspectRatio();
	//设置视频路径。
	public void setVideoPath(String path);
	//设置视频URI。（可以是网络视频地址）
	public void setVideoURI(Uri uri);
	//停止视频播放，并释放资源。
	public void stopPlayback();
	/**
	 * 设置媒体控制器。
	 * 参数controller:媒体控制器，注意是com.hx.ijkplayer_demo.widget.media.IMediaController。
	 */
	public void setMediaController(IMediaController controller);
	//改变媒体控制器显隐
	private void toggleMediaControlsVisiblity();
	//注册一个回调函数，在视频预处理完成后调用。在视频预处理完成后被调用。此时视频的宽度、高度、宽高比信息已经获取到，此时可调用seekTo让视频从指定位置开始播放。
	public void setOnPreparedListener(OnPreparedListener l);
	//播放完成回调
	public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l);
	//播放错误回调
	public void setOnErrorListener(IMediaPlayer.OnErrorListener l);
	//事件发生回调
	public void setOnInfoListener(IMediaPlayer.OnInfoListener l);
	//获取总长度
	public int getDuration();
	//获取当前播放位置。
	public long getCurrentPosition();
	//设置播放位置。单位毫秒
	public void seekTo(long msec);
	//是否正在播放。
	public boolean isPlaying();
	//获取缓冲百分比。
	public int getBufferPercentage();

**封装ijkplayer**

上面只是ijkplayer的一个基本用法。我们可以对ijkplayer进行一次封装，让ijkplayer使用起来更加简单。

功能：
> 使用Vitamio的VideoView进行视频播放
> 
> 视频左侧界面（左1/2以内）上下滑动调节亮度
> 
> 视频右侧界面（右1/2以外）上下滑动调节声音
> 
> 双击切换视频窗口布局
> 
> 非直播状态，可以左右滑动调节当前播放进度

	public class PlayerManager {
	    /**
	     * 可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
	     */
	    public static final String SCALETYPE_FITPARENT="fitParent";
	    /**
	     * 可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
	     */
	    public static final String SCALETYPE_FILLPARENT="fillParent";
	    /**
	     * 将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
	     */
	    public static final String SCALETYPE_WRAPCONTENT="wrapContent";
	    /**
	     * 不剪裁,非等比例拉伸画面填满整个View
	     */
	    public static final String SCALETYPE_FITXY="fitXY";
	    /**
	     * 不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
	     */
	    public static final String SCALETYPE_16_9="16:9";
	    /**
	     * 不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
	     */
	    public static final String SCALETYPE_4_3="4:3";
	
	    /**
	     * 状态常量
	     */
	    private final int STATUS_ERROR=-1;
	    private final int STATUS_IDLE=0;
	    private final int STATUS_LOADING=1;
	    private final int STATUS_PLAYING=2;
	    private final int STATUS_PAUSE=3;
	    private final int STATUS_COMPLETED=4;
	
	    private final Activity activity;
	    private final IjkVideoView videoView;
	    private final AudioManager audioManager;
	    public GestureDetector gestureDetector;
	
	    private boolean playerSupport;
	    private boolean isLive = false;//是否为直播
	    private boolean fullScreenOnly;
	    private boolean portrait;
	
	    private final int mMaxVolume;
	    private int screenWidthPixels;
	    private int currentPosition;
	    private int status=STATUS_IDLE;
	    private long pauseTime;
	    private String url;
	
	    private float brightness=-1;
	    private int volume=-1;
	    private long newPosition = -1;
	    private long defaultRetryTime=5000;
	
	    private OrientationEventListener orientationEventListener;
	    private PlayerStateListener playerStateListener;
	
	    public void setPlayerStateListener(PlayerStateListener playerStateListener) {
	        this.playerStateListener = playerStateListener;
	    }
	
	    private OnErrorListener onErrorListener=new OnErrorListener() {
	        @Override
	        public void onError(int what, int extra) {
	        }
	    };
	
	    private OnCompleteListener onCompleteListener=new OnCompleteListener() {
	        @Override
	        public void onComplete() {
	        }
	    };
	
	    private OnInfoListener onInfoListener=new OnInfoListener(){
	        @Override
	        public void onInfo(int what, int extra) {
	
	        }
	    };
	    private OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener=new OnControlPanelVisibilityChangeListener() {
	        @Override
	        public void change(boolean isShowing) {
	        }
	    };
	
	    /**
	     * try to play when error(only for live video)
	     * @param defaultRetryTime millisecond,0 will stop retry,default is 5000 millisecond
	     */
	    public void setDefaultRetryTime(long defaultRetryTime) {
	        this.defaultRetryTime = defaultRetryTime;
	    }
	
	    public PlayerManager(final Activity activity) {
	        try {
	            IjkMediaPlayer.loadLibrariesOnce(null);
	            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
	            playerSupport=true;
	        } catch (Throwable e) {
	            Log.e("GiraffePlayer", "loadLibraries error", e);
	        }
	        this.activity=activity;
	        screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
	
	        videoView = (IjkVideoView) activity.findViewById(R.id.video_view);
	        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
	            @Override
	            public void onCompletion(IMediaPlayer mp) {
	                statusChange(STATUS_COMPLETED);
	                onCompleteListener.onComplete();
	            }
	        });
	        videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
	            @Override
	            public boolean onError(IMediaPlayer mp, int what, int extra) {
	                statusChange(STATUS_ERROR);
	                onErrorListener.onError(what,extra);
	                return true;
	            }
	        });
	        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
	            @Override
	            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
	                switch (what) {
	                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
	                        statusChange(STATUS_LOADING);
	                        break;
	                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
	                        statusChange(STATUS_PLAYING);
	                        break;
	                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
	                        //显示下载速度
	//                      Toast.show("download rate:" + extra);
	                        break;
	                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
	                        statusChange(STATUS_PLAYING);
	                        break;
	                }
	                onInfoListener.onInfo(what,extra);
	                return false;
	            }
	        });
	
	        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
	        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	        gestureDetector = new GestureDetector(activity, new PlayerGestureListener());
	
	        if (fullScreenOnly) {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        }
	        portrait=getScreenOrientation()== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	
	        if (!playerSupport) {
	            DebugLog.e("播放器不支持此设备");
	        }
	    }
	
	    private void statusChange(int newStatus) {
	        status = newStatus;
	        if (!isLive && newStatus==STATUS_COMPLETED) {
	            DebugLog.d("statusChange STATUS_COMPLETED...");
	            if (playerStateListener != null){
	                playerStateListener.onComplete();
	            }
	        }else if (newStatus == STATUS_ERROR) {
	            DebugLog.d("statusChange STATUS_ERROR...");
	            if (playerStateListener != null){
	                playerStateListener.onError();
	            }
	        } else if(newStatus==STATUS_LOADING){
	//            $.id(R.id.app_video_loading).visible();
	            if (playerStateListener != null){
	                playerStateListener.onLoading();
	            }
	            DebugLog.d("statusChange STATUS_LOADING...");
	        } else if (newStatus == STATUS_PLAYING) {
	            DebugLog.d("statusChange STATUS_PLAYING...");
	            if (playerStateListener != null){
	                playerStateListener.onPlay();
	            }
	        }
	    }
	
	    public void onPause() {
	        pauseTime= System.currentTimeMillis();
	        if (status==STATUS_PLAYING) {
	            videoView.pause();
	            if (!isLive) {
	                currentPosition = videoView.getCurrentPosition();
	            }
	        }
	    }
	
	    public void onResume() {
	        pauseTime=0;
	        if (status==STATUS_PLAYING) {
	            if (isLive) {
	                videoView.seekTo(0);
	            } else {
	                if (currentPosition>0) {
	                    videoView.seekTo(currentPosition);
	                }
	            }
	            videoView.start();
	        }
	    }
	
	    public void onDestroy() {
	        orientationEventListener.disable();
	        videoView.stopPlayback();
	    }
	
	    public void play(String url) {
	        this.url = url;
	        if (playerSupport) {
	            videoView.setVideoPath(url);
	            videoView.start();
	        }
	    }
	
	    private String generateTime(long time) {
	        int totalSeconds = (int) (time / 1000);
	        int seconds = totalSeconds % 60;
	        int minutes = (totalSeconds / 60) % 60;
	        int hours = totalSeconds / 3600;
	        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	    }
	
	    private int getScreenOrientation() {
	        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	        DisplayMetrics dm = new DisplayMetrics();
	        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	        int width = dm.widthPixels;
	        int height = dm.heightPixels;
	        int orientation;
	        // if the device's natural orientation is portrait:
	        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width ||
	                (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height) {
	            switch (rotation) {
	                case Surface.ROTATION_0:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                    break;
	                case Surface.ROTATION_90:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                    break;
	                case Surface.ROTATION_180:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
	                    break;
	                case Surface.ROTATION_270:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
	                    break;
	                default:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                    break;
	            }
	        }
	        // if the device's natural orientation is landscape or if the device
	        // is square:
	        else {
	            switch (rotation) {
	                case Surface.ROTATION_0:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                    break;
	                case Surface.ROTATION_90:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	                    break;
	                case Surface.ROTATION_180:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
	                    break;
	                case Surface.ROTATION_270:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
	                    break;
	                default:
	                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	                    break;
	            }
	        }
	        return orientation;
	    }
	
	    /**
	     * 滑动改变声音大小
	     *
	     * @param percent
	     */
	    private void onVolumeSlide(float percent) {
	        if (volume == -1) {
	            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	            if (volume < 0)
	                volume = 0;
	        }
	        int index = (int) (percent * mMaxVolume) + volume;
	        if (index > mMaxVolume) {
	            index = mMaxVolume;
	        } else if (index < 0){
	            index = 0;
	        }
	        // 变更声音
	        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
	        // 变更进度条
	        int i = (int) (index * 1.0 / mMaxVolume * 100);
	        String s = i + "%";
	        if (i == 0) {
	            s = "off";
	        }
	        DebugLog.d("onVolumeSlide:"+s);
	    }
	
	    private void onProgressSlide(float percent) {
	        long position = videoView.getCurrentPosition();
	        long duration = videoView.getDuration();
	        long deltaMax = Math.min(100 * 1000, duration - position);
	        long delta = (long) (deltaMax * percent);
	
	        newPosition = delta + position;
	        if (newPosition > duration) {
	            newPosition = duration;
	        } else if (newPosition <= 0) {
	            newPosition=0;
	            delta=-position;
	        }
	        int showDelta = (int) delta / 1000;
	        if (showDelta != 0) {
	            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
	            DebugLog.d("onProgressSlide:" + text);
	        }
	    }
	
	    /**
	     * 滑动改变亮度
	     *
	     * @param percent
	     */
	    private void onBrightnessSlide(float percent) {
	        if (brightness < 0) {
	            brightness = activity.getWindow().getAttributes().screenBrightness;
	            if (brightness <= 0.00f){
	                brightness = 0.50f;
	            }else if (brightness < 0.01f){
	                brightness = 0.01f;
	            }
	        }
	        DebugLog.d("brightness:"+brightness+",percent:"+ percent);
	        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
	        lpa.screenBrightness = brightness + percent;
	        if (lpa.screenBrightness > 1.0f){
	            lpa.screenBrightness = 1.0f;
	        }else if (lpa.screenBrightness < 0.01f){
	            lpa.screenBrightness = 0.01f;
	        }
	        activity.getWindow().setAttributes(lpa);
	    }
	
	    public void setFullScreenOnly(boolean fullScreenOnly) {
	        this.fullScreenOnly = fullScreenOnly;
	        tryFullScreen(fullScreenOnly);
	        if (fullScreenOnly) {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        } else {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	        }
	    }
	
	    private void tryFullScreen(boolean fullScreen) {
	        if (activity instanceof AppCompatActivity) {
	            ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
	            if (supportActionBar != null) {
	                if (fullScreen) {
	                    supportActionBar.hide();
	                } else {
	                    supportActionBar.show();
	                }
	            }
	        }
	        setFullScreen(fullScreen);
	    }
	
	    private void setFullScreen(boolean fullScreen) {
	        if (activity != null) {
	            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
	            if (fullScreen) {
	                attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	                activity.getWindow().setAttributes(attrs);
	                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	            } else {
	                attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	                activity.getWindow().setAttributes(attrs);
	                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	            }
	        }
	    }
	
	    /**
	     * <pre>
	     *     fitParent:可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
	     *     fillParent:可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
	     *     wrapContent:将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
	     *     fitXY:不剪裁,非等比例拉伸画面填满整个View
	     *     16:9:不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
	     *     4:3:不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
	     * </pre>
	     * @param scaleType
	     */
	    public void setScaleType(String scaleType) {
	        if (SCALETYPE_FITPARENT.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
	        }else if (SCALETYPE_FILLPARENT.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
	        }else if (SCALETYPE_WRAPCONTENT.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_ASPECT_WRAP_CONTENT);
	        }else if (SCALETYPE_FITXY.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_MATCH_PARENT);
	        }else if (SCALETYPE_16_9.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
	        }else if (SCALETYPE_4_3.equals(scaleType)) {
	            videoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
	        }
	    }
	
	    public void start() {
	        videoView.start();
	    }
	
	    public void pause() {
	        videoView.pause();
	    }
	
	    public boolean onBackPressed() {
	        if (!fullScreenOnly && getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	            return true;
	        }
	        return false;
	    }
	
	    class Query {
	        private final Activity activity;
	        private View view;
	
	        public Query(Activity activity) {
	            this.activity=activity;
	        }
	
	        public Query id(int id) {
	            view = activity.findViewById(id);
	            return this;
	        }
	
	        public Query image(int resId) {
	            if (view instanceof ImageView) {
	                ((ImageView) view).setImageResource(resId);
	            }
	            return this;
	        }
	
	        public Query visible() {
	            if (view != null) {
	                view.setVisibility(View.VISIBLE);
	            }
	            return this;
	        }
	
	        public Query gone() {
	            if (view != null) {
	                view.setVisibility(View.GONE);
	            }
	            return this;
	        }
	
	        public Query invisible() {
	            if (view != null) {
	                view.setVisibility(View.INVISIBLE);
	            }
	            return this;
	        }
	
	        public Query clicked(View.OnClickListener handler) {
	            if (view != null) {
	                view.setOnClickListener(handler);
	            }
	            return this;
	        }
	
	        public Query text(CharSequence text) {
	            if (view!=null && view instanceof TextView) {
	                ((TextView) view).setText(text);
	            }
	            return this;
	        }
	
	        public Query visibility(int visible) {
	            if (view != null) {
	                view.setVisibility(visible);
	            }
	            return this;
	        }
	
	        private void size(boolean width, int n, boolean dip){
	            if(view != null){
	                ViewGroup.LayoutParams lp = view.getLayoutParams();
	                if(n > 0 && dip){
	                    n = dip2pixel(activity, n);
	                }
	                if(width){
	                    lp.width = n;
	                }else{
	                    lp.height = n;
	                }
	                view.setLayoutParams(lp);
	            }
	        }
	
	        public void height(int height, boolean dip) {
	            size(false,height,dip);
	        }
	
	        public int dip2pixel(Context context, float n){
	            int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n, context.getResources().getDisplayMetrics());
	            return value;
	        }
	
	        public float pixel2dip(Context context, float n){
	            Resources resources = context.getResources();
	            DisplayMetrics metrics = resources.getDisplayMetrics();
	            float dp = n / (metrics.densityDpi / 160f);
	            return dp;
	        }
	    }
	
	    public class PlayerGestureListener extends GestureDetector.SimpleOnGestureListener {
	        private boolean firstTouch;
	        private boolean volumeControl;
	        private boolean toSeek;
	
	        /**
	         * 双击
	         */
	        @Override
	        public boolean onDoubleTap(MotionEvent e) {
	            videoView.toggleAspectRatio();
	            return true;
	        }
	
	        @Override
	        public boolean onDown(MotionEvent e) {
	            firstTouch = true;
	            return super.onDown(e);
	        }
	
	        /**
	         * 滑动
	         */
	        @Override
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	            float mOldX = e1.getX(), mOldY = e1.getY();
	            float deltaY = mOldY - e2.getY();
	            float deltaX = mOldX - e2.getX();
	            if (firstTouch) {
	                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
	                volumeControl=mOldX > screenWidthPixels * 0.5f;
	                firstTouch = false;
	            }
	
	            if (toSeek) {
	                if (!isLive) {
	                    onProgressSlide(-deltaX / videoView.getWidth());
	                }
	            } else {
	                float percent = deltaY / videoView.getHeight();
	                if (volumeControl) {
	                    onVolumeSlide(percent);
	                } else {
	                    onBrightnessSlide(percent);
	                }
	            }
	
	            return super.onScroll(e1, e2, distanceX, distanceY);
	        }
	
	        @Override
	        public boolean onSingleTapUp(MotionEvent e) {
	            return true;
	        }
	    }
	
	    /**
	     * is player support this device
	     * @return
	     */
	    public boolean isPlayerSupport() {
	        return playerSupport;
	    }
	
	    /**
	     * 是否正在播放
	     * @return
	     */
	    public boolean isPlaying() {
	        return videoView!=null?videoView.isPlaying():false;
	    }
	
	    public void stop(){
	        videoView.stopPlayback();
	    }
	
	    public int getCurrentPosition(){
	        return videoView.getCurrentPosition();
	    }
	
	    /**
	     * get video duration
	     * @return
	     */
	    public int getDuration(){
	        return videoView.getDuration();
	    }
	
	    public PlayerManager playInFullScreen(boolean fullScreen){
	        if (fullScreen) {
	            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        }
	        return this;
	    }
	
	    public PlayerManager onError(OnErrorListener onErrorListener) {
	        this.onErrorListener = onErrorListener;
	        return this;
	    }
	
	    public PlayerManager onComplete(OnCompleteListener onCompleteListener) {
	        this.onCompleteListener = onCompleteListener;
	        return this;
	    }
	
	    public PlayerManager onInfo(OnInfoListener onInfoListener) {
	        this.onInfoListener = onInfoListener;
	        return this;
	    }
	
	    public PlayerManager onControlPanelVisibilityChange(OnControlPanelVisibilityChangeListener listener){
	        this.onControlPanelVisibilityChangeListener = listener;
	        return this;
	    }
	
	    /**
	     * set is live (can't seek forward)
	     * @param isLive
	     * @return
	     */
	    public PlayerManager live(boolean isLive) {
	        this.isLive = isLive;
	        return this;
	    }
	
	    public PlayerManager toggleAspectRatio(){
	        if (videoView != null) {
	            videoView.toggleAspectRatio();
	        }
	        return this;
	    }
	
	    public interface PlayerStateListener{
	        void onComplete();
	        void onError();
	        void onLoading();
	        void onPlay();
	    }
	
	    public interface OnErrorListener{
	        void onError(int what, int extra);
	    }
	
	    public interface OnCompleteListener{
	        void onComplete();
	    }
	
	    public interface OnControlPanelVisibilityChangeListener{
	        void change(boolean isShowing);
	    }
	
	    public interface OnInfoListener{
	        void onInfo(int what, int extra);
	    }
	}

使用封装后的PlayerManager播放视频：

	public class MainActivity extends AppCompatActivity implements PlayerManager.PlayerStateListener{
	    private String url1 = "rtmp://203.207.99.19:1935/live/CCTV5";
	    private String url2 = "http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8";
	    private String url3 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
	    private String url4 = "http://42.96.249.166/live/24035.m3u8";
	    private PlayerManager player;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        initPlayer();
	    }
	
	    private void initPlayer() {
	        player = new PlayerManager(this);
	        player.setFullScreenOnly(true);
	        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
	        player.playInFullScreen(true);
	        player.setPlayerStateListener(this);
	        player.play(url1);
	    }
	
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        if (player.gestureDetector.onTouchEvent(event))
	            return true;
	        return super.onTouchEvent(event);
	    }
	
	    @Override
	    public void onComplete() {
	    }
	
	    @Override
	    public void onError() {
	    }
	
	    @Override
	    public void onLoading() {
	    }
	
	    @Override
	    public void onPlay() {
	    }
	}

# **[Demo下载地址](http://download.csdn.net/detail/huaxun66/9698723)**
