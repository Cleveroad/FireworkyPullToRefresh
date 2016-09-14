package com.cleveroad.pulltorefresh.firework;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;


abstract class BaseRefreshDrawable extends Drawable implements Drawable.Callback, Animatable {

    private FireworkyPullToRefreshLayout mRefreshLayout;

    BaseRefreshDrawable(FireworkyPullToRefreshLayout mRefreshLayout) {
        this.mRefreshLayout = mRefreshLayout;
    }

    Context getContext() {
        return mRefreshLayout == null ? null : mRefreshLayout.getContext();
    }

    public abstract void setPercent(float percent, boolean invalidate);

    public abstract void setPointerPosition(float x, float y);

    public abstract void offsetTopAndBottom(int offset);

    protected abstract void init();

    protected abstract void setupAnimations();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.invalidateDrawable(who);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.scheduleDrawable(who, what, when);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        final Callback callback = getCallback();
        if (null != callback) {
            callback.unscheduleDrawable(who, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public abstract void draw(@NonNull Canvas canvas);

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
