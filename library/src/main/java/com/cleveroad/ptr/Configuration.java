package com.cleveroad.ptr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.cleveroad.library.R;

/**
 * Class {@link Configuration} contains {@link FireworkyPullToRefreshLayout} instance configuration
 * For getting {@link FireworkyPullToRefreshLayout} configuration use {@link FireworkyPullToRefreshLayout#getConfig()} method
 */
public class Configuration {
    private final Context mContext;
    private int[] mFireworkColors;
    private long mRocketAnimDuration = 1000L;

    private Drawable mRocketDrawable;
    private Drawable mFlameDrawable;
    private Drawable mBackgroundDrawable;

    Configuration(Context context) {
        mContext = context;
    }

    /**
     * Use this method to set rocket drawable
     * @param rocketDrawable
     */
    public void setRocket(Drawable rocketDrawable) {
        mRocketDrawable = rocketDrawable;
    }

    /**
     * Use this method to set rocket bitmap
     * @param rocketBitmap
     */
    public void setRocket(Bitmap rocketBitmap) {
        setRocket(new BitmapDrawable(mContext.getResources(), rocketBitmap));
    }

    /**
     * Use this method to set rocket drawable from resources
     * @param fireworkDrawableRes
     */
    public void setRocket(@DrawableRes int fireworkDrawableRes) {
        mRocketDrawable = ContextCompat.getDrawable(mContext, fireworkDrawableRes);
    }

    Drawable getRocketDrawable() {
        return mRocketDrawable;
    }

    /**
     * Use this method to set flame drawable
     * @param flameDrawable
     */
    public void setFlame(Drawable flameDrawable) {
        mFlameDrawable = flameDrawable;
    }

    /**
     * Use this method to set flame bitmap
     * @param flameBitmap
     */
    public void setFlame(Bitmap flameBitmap) {
        setFlame(new BitmapDrawable(mContext.getResources(), flameBitmap));
    }

    /**
     * Use this method to set flame drawable from resources
     * @param flameDrawableRes
     */
    public void setFlame(@DrawableRes int flameDrawableRes) {
        mFlameDrawable = ContextCompat.getDrawable(mContext, flameDrawableRes);
    }

    Drawable getFlameDrawable() {
        return mFlameDrawable;
    }

    /**
     * Use this method to set background drawable
     * @param backgroundDrawable
     */
    public void setBackground(Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
    }

    /**
     * Use this method to set background bitmap
     * @param backgroundBitmap
     */
    public void setBackground(Bitmap backgroundBitmap) {
        setBackground(new BitmapDrawable(mContext.getResources(), backgroundBitmap));
    }

    /**
     * Use this method to set background drawable from resources
     * @param backgroundDrawableRes
     */
    public void setBackground(@DrawableRes int backgroundDrawableRes) {
        mBackgroundDrawable = ContextCompat.getDrawable(mContext, backgroundDrawableRes);
    }

    Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    int[] getFireworkColors() {
        if (mFireworkColors == null) {
            mFireworkColors = mContext.getResources().getIntArray(R.array.ptr_defColorSet);
        }
        return mFireworkColors;
    }

    /**
     * Use this method to set firework colors
     * @param fireworkColors
     */
    public void setFireworkColors(int fireworkColors[]) {
        mFireworkColors = fireworkColors;
    }

    /**
     * Use this method to set firework color set from resources
     * @param fireworkColorsResources
     */
    public void setFireworkColors(@ArrayRes int fireworkColorsResources) {
        mFireworkColors = mContext.getResources().getIntArray(fireworkColorsResources);
    }

    long getRocketAnimDuration() {
        return mRocketAnimDuration;
    }

    /**
     * Use this method to set rocket animation duration
     * @param rocketAnimDuration
     */
    public void setRocketAnimDuration(long rocketAnimDuration) {
        mRocketAnimDuration = rocketAnimDuration;
    }
}
