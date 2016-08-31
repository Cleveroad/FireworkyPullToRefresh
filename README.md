# FireworkyPullToRefresh #

Let's try to refresh your data with our library!

<img src="/images/FireworkyPullToRefresh1.png" width="216" height="384" />
<img src="/images/FireworkyPullToRefresh2.png" width="216" height="384" />

## Setup and usage ##
### Installation ###
by Gradle:
```groovy
    //coming soon
```
or just download zip and import module "fireworky-pull-to-refresh to be able to modify the sources
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
| ptr_rocketAnimDuration  | rocket flight duration |
### Via Java code ###

```Java
//use .config() methods:
mPullToRefresh.getConfig().setRocketFromBitmap(fireworkRocketBitmap);
mPullToRefresh.getConfig().setRocketFromResources(R.drawable.fireworkRocket);

mPullToRefresh.getConfig().setBackgroundFromBitmap(backgroundBitmap);
mPullToRefresh.getConfig().setBackgroundFromResources(R.drawable.background);

mPullToRefresh.getConfig().setFlameFromBitmap(flameBitmap);
mPullToRefresh.getConfig().setFlameFromResources(R.drawable.flame);

mPullToRefresh.getConfig().setFireworkColors(colorsIntArray);
mPullToRefresh.getConfig().setFireworkColorsFromResources(R.array.fireworkColors);

mPullToRefresh.getConfig().setRocketAnimDuration(1000L);

```

## Animation ##
To start or stop animation:

```Java
mPullRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mPullRefreshView.setRefreshing(mIsRefreshing);
            }
});
```

### Refreshing callback ###
Just implement PullToRefreshView.OnRefreshListener:

```Java

mPullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                
            }
});
```
### Using custom views ###
For using custom views just implement FireworkyPullToRefreshLayout.OnChildScrollUpCallback:
```Java

mPullToRefresh.setOnChildScrollUpCallback(new FireworkyPullToRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(FireworkyPullToRefreshLayout parent, @Nullable View child) {
                //put your implementation here
            }
});
```
