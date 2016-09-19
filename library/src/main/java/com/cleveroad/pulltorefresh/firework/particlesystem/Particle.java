package com.cleveroad.pulltorefresh.firework.particlesystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.cleveroad.pulltorefresh.firework.particlesystem.modifiers.ParticleModifier;

import java.util.List;

public class Particle {

    public float mCurrentX;
    public float mCurrentY;
    public float mScale = 1f;
    public int mAlpha = 255;
    public float mInitialRotation = 0f;
    public float mRotationSpeed = 0f;
    public float mSpeedX = 0f;
    public float mSpeedY = 0f;
    public float mAccelerationX;
    public float mAccelerationY;
    long mStartingMilliseconds;
    Bitmap mImage;
    private Matrix mMatrix;
    private Paint mPaint;
    private float mInitialX;
    private float mInitialY;
    private float mRotation;
    private long mTimeToLive;
    private int mBitmapHalfWidth;
    private int mBitmapHalfHeight;

    private List<ParticleModifier> mModifiers;

    private Integer mTintColor;

    protected Particle() {
        mMatrix = new Matrix();
        mPaint = new Paint();
    }

    public Particle(Bitmap bitmap) {
        this();
        mImage = bitmap;
    }

    public void init() {
        mScale = 1;
        mAlpha = 255;
    }

    public void configure(long timeToLive, float emiterX, float emiterY) {
        mBitmapHalfWidth = mImage.getWidth() / 2;
        mBitmapHalfHeight = mImage.getHeight() / 2;

        mInitialX = emiterX - mBitmapHalfWidth;
        mInitialY = emiterY - mBitmapHalfHeight;
        mCurrentX = mInitialX;
        mCurrentY = mInitialY;

        mTimeToLive = timeToLive;
    }

    public boolean update(long milliseconds) {
        long realMilliseconds = milliseconds - mStartingMilliseconds;
        if (realMilliseconds > mTimeToLive) {
            return false;
        }
        mCurrentX = mInitialX + mSpeedX * realMilliseconds + mAccelerationX * realMilliseconds * realMilliseconds;
        mCurrentY = mInitialY + mSpeedY * realMilliseconds + mAccelerationY * realMilliseconds * realMilliseconds;
        mRotation = mInitialRotation + mRotationSpeed * realMilliseconds / 1000;
        for (int i = 0; i < mModifiers.size(); i++) {
            mModifiers.get(i).apply(this, realMilliseconds);
        }
        return true;
    }

    public void draw(Canvas c) {
        mMatrix.reset();
        mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postTranslate(mCurrentX, mCurrentY);

        if (mTintColor != null) {
            mPaint.setColorFilter(new PorterDuffColorFilter(mTintColor, PorterDuff.Mode.MULTIPLY));
        }
        mPaint.setAlpha(mAlpha);
        c.drawBitmap(mImage, mMatrix, mPaint);
    }

    public Particle activate(long startingMilliseconds, List<ParticleModifier> modifiers) {
        mStartingMilliseconds = startingMilliseconds;
        // We do store a reference to the list, there is no need to copy, since the modifiers do not carte about states
        mModifiers = modifiers;
        return this;
    }

    public void setTintColor(Integer tintColor) {
        mTintColor = tintColor;
    }
}
