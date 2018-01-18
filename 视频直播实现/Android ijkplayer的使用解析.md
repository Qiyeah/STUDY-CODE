# [CSDN Watson的博客](http://blog.csdn.net/huaxun66)

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

（4）复制
