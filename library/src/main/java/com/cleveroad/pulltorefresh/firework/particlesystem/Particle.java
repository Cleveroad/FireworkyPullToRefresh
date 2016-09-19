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

    private float mCurrentX;
    private float mCurrentY;
    private float mScale = 1f;
    private int mAlpha = 255;
    private float mInitialRotation = 0f;
    private float mRotationSpeed = 0f;
    private float mSpeedX = 0f;
    private float mSpeedY = 0f;
    private float mAccelerationX;
    private float mAccelerationY;
    private long mStartingMilliseconds;
    private Bitmap mImage;
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

    Particle(Bitmap bitmap) {
        this();
        mImage = bitmap;
    }

    void init() {
        mScale = 1;
        mAlpha = 255;
    }

    void configure(long timeToLive, float emiterX, float emiterY) {
        mBitmapHalfWidth = mImage.getWidth() / 2;
        mBitmapHalfHeight = mImage.getHeight() / 2;

        mInitialX = emiterX - mBitmapHalfWidth;
        mInitialY = emiterY - mBitmapHalfHeight;
        mCurrentX = mInitialX;
        mCurrentY = mInitialY;

        mTimeToLive = timeToLive;
    }

    boolean update(long milliseconds) {
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

    void draw(Canvas canvas) {
        mMatrix.reset();
        mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postTranslate(mCurrentX, mCurrentY);

        if (mTintColor != null) {
            mPaint.setColorFilter(new PorterDuffColorFilter(mTintColor, PorterDuff.Mode.MULTIPLY));
        }
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mImage, mMatrix, mPaint);
    }

    Particle activate(long startingMilliseconds, List<ParticleModifier> modifiers) {
        mStartingMilliseconds = startingMilliseconds;
        // We do store a reference to the list, there is no need to copy, since the modifiers do not carte about states
        mModifiers = modifiers;
        return this;
    }

    void setTintColor(Integer tintColor) {
        mTintColor = tintColor;
    }

    public float getCurrentX() {
        return mCurrentX;
    }

    public void setCurrentX(float currentX) {
        mCurrentX = currentX;
    }

    public float getCurrentY() {
        return mCurrentY;
    }

    public void setCurrentY(float currentY) {
        mCurrentY = currentY;
    }

    public float getScale() {
        return mScale;
    }

    public void setScale(float scale) {
        mScale = scale;
    }

    public int getAlpha() {
        return mAlpha;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public float getInitialRotation() {
        return mInitialRotation;
    }

    public void setInitialRotation(float initialRotation) {
        mInitialRotation = initialRotation;
    }

    public float getRotationSpeed() {
        return mRotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        mRotationSpeed = rotationSpeed;
    }

    public float getSpeedX() {
        return mSpeedX;
    }

    public void setSpeedX(float speedX) {
        mSpeedX = speedX;
    }

    public float getSpeedY() {
        return mSpeedY;
    }

    public void setSpeedY(float speedY) {
        mSpeedY = speedY;
    }

    public float getAccelerationX() {
        return mAccelerationX;
    }

    public void setAccelerationX(float accelerationX) {
        mAccelerationX = accelerationX;
    }

    public float getAccelerationY() {
        return mAccelerationY;
    }

    public void setAccelerationY(float accelerationY) {
        mAccelerationY = accelerationY;
    }

    public long getStartingMilliseconds() {
        return mStartingMilliseconds;
    }

    public void setStartingMilliseconds(long startingMilliseconds) {
        mStartingMilliseconds = startingMilliseconds;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }

    public Matrix getMatrix() {
        return mMatrix;
    }

    public void setMatrix(Matrix matrix) {
        mMatrix = matrix;
    }
}
