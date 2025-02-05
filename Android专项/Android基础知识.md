[TOC]


# 内存泄漏

## Handler 造成的内存泄漏 (19.6.25日被骂，超级尴尬)

**问题描述**
Handler 中的 Message 对象会持有 Handler 的引用，而 Handler 如果是一个匿名内部类的方式使用：

```java
mHandler = new Handler(Looper.getMiainLooper()) {
    @Override
    public void handleMessage(Message msg) {
    }
}
```
Handler则会持有Activity的强引用，在Activity需要销毁的时候，Looper还在运行中，则内存泄漏。

**处理方案**

1. 在Activity `onDestory`、View `onDetachedFromWindos`中调用 `removeCallAndMessage(null);`,**但是Activity`onDestory`在异常情况下不会被调用**，所以需要加上第二种方式。
2. 创建Handler匿名内部类、如果需要依赖外部类，可以传递一个弱引用进去
```java
private static class MyHandler extend Handler {
    private WeakReference mReference;
    MyHandler(Actvity activity) {
        new WeakReference(actvity);
    }
    @Override
    public void handleMessage(Message msg) {
        if (mReference.get() == null ){
            removeCallAndMessage(null);
            return;
        }
        // to do sth.
        mReference.get().todo();
    }
}
```
## 线程造成的内存泄漏

**JVM 不会回收正在运行中的线程**，所有如果 Thread 中运行着一个长任务并且引用了 Activity，那么就会造成内存泄漏，例如 AsyncTask 造成内存泄漏的本质其实也是此原因，所以 AsyncTask 的内存泄漏并不是他自身的特点，而是所有线程的都会造成内存泄漏的风险。

但是，需要运行的任务时间是短暂的例如开发者设定了一个短暂的运行时间，那么其实可以忽略这种，当然可以使用 Activity 弱引用的方式使用线程。



# Activity（19.8.5）

## 监听所有Actvity的生命周期回调

```java
Applicaiton —> registerActivityLifecycleCallbacks();
```

## 设置进入和退出动画

* 【anim】in_from_up.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromYDelta="100%p"
        android:toYDelta="0%p" />

</set>
```

* 【anim】out_to_down.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromYDelta="0%p"
        android:toYDelta="100%p" />

</set>
```

* 【style.xml】

```java
 <!--自下而上进入 自上而下退出 -->
    <style name="AppAnimationTheme" parent="继承默认app主题即可">
        <!-- 将Activity的Theme设置成透明 -->
        <item name="android:windowBackground">@null</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:activityOpenEnterAnimation">@anim/in_from_up</item>
        <item name="android:activityOpenExitAnimation">@anim/in_from_up</item>
        <item name="android:activityCloseEnterAnimation">@anim/out_to_down</item>
        <item name="android:activityCloseExitAnimation">@anim/out_to_down</item>
    </style>

```

* 【AndroidManifest.xml】设置主题

```xml
<activity android:theme="@style/AppAnimationTheme" />
```

* 【Activity跳转页面】

```java
Intent intent = new Intent();
startActivity(intent);
// overridePendingTransition 是 Activity 的方法
overridePendingTransition(R.anim.in_from_up, android.R.anim.fade_out);
```
* 【Activity目标页面】重写finish

```java
@Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.out_to_down, R.anim.out_to_down);
    }
```

## 设置透明背景

* 【style.xml】

```xml
  <style name="Transparent" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
    </style>
```

*  【AndroidManifest.xml】设置主题

```xml
<activit
            android:name=".join.RequestJoinListActivity"
            android:screenOrientation="portrait"
            android:theme="@style/Transparent" />
```

## 设置状态栏颜色

```java
StatusBarUtil.setColor(this, Color.TRANSPARENT);
```
