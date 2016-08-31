package com.cleveroad.ptr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
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
    public void setRocketFromDrawable(Drawable rocketDrawable) {
        mRocketDrawable = rocketDrawable;
    }

    /**
     * Use this method to set rocket drawable from resources
     * @param fireworkDrawableRes
     */
    public void setRocketFromResources(@DrawableRes int fireworkDrawableRes) {
        mRocketDrawable = ContextCompat.getDrawable(mContext, fireworkDrawableRes);
    }

    Drawable getRocketDrawable() {
        return mRocketDrawable;
    }

    /**
     * Use this method to set flame drawable
     * @param flameDrawable
     */
    public void setFlameFromDrawable(@Nullable Drawable flameDrawable) {
        mFlameDrawable = flameDrawable;
    }

    /**
     * Use this method to set flame drawable from resources
     * @param flameDrawableRes
     */
    public void setFlameFromResources(@DrawableRes int flameDrawableRes) {
        mFlameDrawable = ContextCompat.getDrawable(mContext, flameDrawableRes);
    }

    Drawable getFlameDrawable() {
        return mFlameDrawable;
    }

    /**
     * Use this method to set background drawable
     * @param backgroundDrawable
     */
    public void setBackgroundFromDrawable(@Nullable Drawable backgroundDrawable) {
        mBackgroundDrawable = backgroundDrawable;
    }

    /**
     * Use this method to set background drawable from resources
     * @param backgroundDrawableRes
     */
    public void setBackgroundFromResources(@DrawableRes int backgroundDrawableRes) {
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
    public void setFireworkColorsFromResources(@ArrayRes int fireworkColorsResources) {
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
