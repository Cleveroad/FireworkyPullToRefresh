##FireworkyPullToRefresh [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="20"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="20"></a>
![Header image](/images/header.jpg)

## Cleveroad introduces Fireworky Pull To Refresh for Android

Let's try to refresh your data with our library!

Hey guys, hope you haven’t started developing a tutorial for your Android app yet, as we have already completed a part of your job. Don’t worry, we act from good motives only. Our aim is to help you create a sliding tutorial in a fast and simple manner. So we’ve done some work and voila!. A simple Android Sliding Tutorial library is at your service.

![Demo image](/images/demo.gif)
###### Also you can watch the animation of the <strong><a target="_blank" href="https://www.youtube.com/watch?v=lJSGIk4Zh9s&feature=youtu.be">Sliding Tutorial for Android on YouTube</a></strong> in HD quality.

The invention is going to ease the problem of structural design but not to limit a stretch of your imagination at the same time. We took care of the suitability aspect. So, your app is not going to look alien among other Android elements. 

[![Awesome](/images/logo-footer.png)](https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts)

## Setup and usage ##
### Installation ###
by Gradle:
```groovy
    //coming soon
```
or just download zip and import module "fireworky-pull-to-refresh to be able to modify the sources.
### Supported Views ###

* RecyclerView
* ListView
* ScrollView
* NestedScrollView

### How do I get set up? ###
Just wrap your view:

```XML

<com.cleveroad.ptr.FireworkyPullToRefreshLayout
        android:id="@+id/pullToRefresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

</com.cleveroad.ptr.FireworkyPullToRefreshLayout>
```

## Configuration ##
### Via XML ###

```XML

<com.cleveroad.ptr.FireworkyPullToRefreshLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        ...
        app:ptr_fireworkColors="@array/fireworkColors"
        app:ptr_flame="@drawable/flame"
        app:ptr_background="@drawable/background"
        app:ptr_rocket="@drawable/rocket"
        app:ptr_rocketAnimDuration="1000">
```
|  attribute name | description |
|---|---|
| ptr_fireworkColors  | An array of colors which will be used for firework animation |
| ptr_flame  | An Drawable of rocket flame |
| ptr_background  | Background drawable |
| ptr_rocket  | Rocket drawable |
| ptr_rocketAnimDuration  | Rocket flight duration |
### Via Java code ###

```Java
//use .config() methods:
mPullToRefresh.getConfig().setRocket(fireworkRocketDrawable);
mPullToRefresh.getConfig().setRocket(fireworkRocketBitmap);
mPullToRefresh.getConfig().setRocket(R.drawable.fireworkRocket);

mPullToRefresh.getConfig().setBackground(backgroundDrawable);
mPullToRefresh.getConfig().setBackground(backgroundBitmap);
mPullToRefresh.getConfig().setBackground(R.drawable.background);

mPullToRefresh.getConfig().setFlame(flameDrawable);
mPullToRefresh.getConfig().setFlame(flameBitmap);
mPullToRefresh.getConfig().setFlame(R.drawable.flame);

mPullToRefresh.getConfig().setFireworkColors(colorsIntArray);
mPullToRefresh.getConfig().setFireworkColors(R.array.fireworkColors);

mPullToRefresh.getConfig().setRocketAnimDuration(1000L);

```

## Animation ##
### Refreshing callback ###
Just implement `PullToRefreshView.OnRefreshListener`:

```Java

mPullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
    @Override
    public void onRefresh() {
        //refresh your data here        
    }
});
```
###To start or stop animation:###

```Java
mPullRefreshView.setRefreshing(isRefreshing);
```

### Using custom views ###
For using custom views just implement `FireworkyPullToRefreshLayout.OnChildScrollUpCallback`:
```Java

mPullToRefresh.setOnChildScrollUpCallback(new FireworkyPullToRefreshLayout.OnChildScrollUpCallback() {
    @Override
    public boolean canChildScrollUp(@NonNull FireworkyPullToRefreshLayout parent, @Nullable View child) {
        //put your implementation here
    }
});
```
