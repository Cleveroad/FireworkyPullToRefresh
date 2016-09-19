package com.cleveroad.pulltorefresh.firework.particlesystem.modifiers;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;


public class ScaleModifier implements ParticleModifier {

    private float mInitialValue;
    private float mFinalValue;
    private long mEndTime;
    private long mStartTime;
    private long mDuration;
    private float mValueIncrement;
    private Interpolator mInterpolator;

    public ScaleModifier(float initialValue, float finalValue, long startMillis, long endMillis, Interpolator interpolator) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMillis;
        mEndTime = endMillis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue - mInitialValue;
        mInterpolator = interpolator;
    }

    public ScaleModifier(float initialValue, float finalValue, long startMillis, long endMillis) {
        this(initialValue, finalValue, startMillis, endMillis, new LinearInterpolator());
    }

    @Override
    public void apply(Particle particle, long milliseconds) {
        if (milliseconds < mStartTime) {
            particle.setScale(mInitialValue);
        } else if (milliseconds > mEndTime) {
            particle.setScale(mFinalValue);
        } else {
            float interpolatedValue = mInterpolator.getInterpolation((milliseconds - mStartTime) * 1f / mDuration);
            particle.setScale(mInitialValue + mValueIncrement * interpolatedValue);
        }
    }

}
