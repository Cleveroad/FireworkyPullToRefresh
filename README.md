#FireworkyPullToRefresh [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="20"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="20"></a>
![Header image](/images/header_.jpg)

## Meet Fireworky Pull To Refresh for Android

It’s time to add some creativity to good old pull to refresh view. You can now literally launch your feed content or search results like a firework! Entertain your app users while they are waiting for refreshment — it’s easy with Fireworky Pull To Refresh library for Android apps.

![Demo image](/images/demo_.gif)
#####Check out the animation on the <strong><a target="_blank" href="https://youtu.be/sJhBKyvF7i4?list=PLi-FH7__aeiydOwY_1q5I8P2EUSseqUCj">Fireworky Pull To Refresh for Android on YouTube</a></strong> in HD quality.

What will you receive by using the Fireworky Pull To Refresh component in your app? The answer is pretty obvious: original and inspiring design element to generate well-disposed users. So, here you go!
 

[![Awesome](/images/logo-footer.png)](https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts)

## Setup and usage ##
### Installation ###
by Gradle:
```groovy
    compile 'com.cleveroad:fireworkypulltorefresh:1.0.3'
```
or just download zip and import module "fireworky-pull-to-refresh" to be able to modify the sources.
### Supported Views ###

* RecyclerView
* ListView
* ScrollView
* NestedScrollView

### How do I get set up? ###
Just wrap your view:

```XML

<com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout
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

<com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout
        xmlns:app="http://schemas.android.com/apk/res-auto"
        ...
        app:ptr_fireworkColors="@array/fireworkColors"
        app:ptr_background="@drawable/background"
        app:ptr_rocketAnimDuration="1000">
```
|  attribute name | description |
|---|---|
| ptr_fireworkColors  | An array of colors which will be used for firework animation |
| ptr_background  | Background drawable |
| ptr_backgroundColor | Background color |
| ptr_rocketAnimDuration  | Rocket flight duration |
| ptr_fireworkStyle | Fireworks animation style - `classic` (by default) or `modern` |
### Via Java code ###

```Java
//use .config() methods:

mPullToRefresh.getConfig().setBackground(backgroundDrawable);
mPullToRefresh.getConfig().setBackground(backgroundBitmap);
mPullToRefresh.getConfig().setBackground(R.drawable.background);
mPullToRefresh.getConfig().setBackgroundColor(Color.BLACK);
mPullToRefresh.getConfig().setBackgroundColorFromResources(R.color.background);

mPullToRefresh.getConfig().setFireworkColors(colorsIntArray);
mPullToRefresh.getConfig().setFireworkColors(R.array.fireworkColors);

mPullRefreshView.getConfig().setFireworkStyle(Configuration.FireworkStyle.MODERN);

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
## Support
If you have any questions regarding the use of this tutorial, please contact us for support
at info@cleveroad.com (email subject: «FireworkyPullToRefresh for Android. Support request.»)
<br>or
<br>Use our contacts:
<br><a href="https://www.cleveroad.com/?utm_source=github&utm_medium=link&utm_campaign=contacts">Cleveroad.com</a>
<br><a href="https://www.facebook.com/cleveroadinc">Facebook account</a>
<br><a href="https://twitter.com/CleveroadInc">Twitter account</a>
<br><a href="https://plus.google.com/+CleveroadInc/">Google+ account</a>

## License


        The MIT License (MIT)

        Copyright (c) 2015-2016 Cleveroad

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
