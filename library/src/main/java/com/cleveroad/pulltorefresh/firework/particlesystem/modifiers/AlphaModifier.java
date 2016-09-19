package com.cleveroad.pulltorefresh.firework.particlesystem.modifiers;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;


public class AlphaModifier implements ParticleModifier {

    private int mInitialValue;
    private int mFinalValue;
    private long mStartTime;
    private long mEndTime;
    private float mDuration;
    private float mValueIncrement;
    private Interpolator mInterpolator;

    public AlphaModifier(int initialValue, int finalValue, long startMillis, long endMillis, Interpolator interpolator) {
        mInitialValue = initialValue;
        mFinalValue = finalValue;
        mStartTime = startMillis;
        mEndTime = endMillis;
        mDuration = mEndTime - mStartTime;
        mValueIncrement = mFinalValue - mInitialValue;
        mInterpolator = interpolator;
    }

    public AlphaModifier(int initialValue, int finalValue, long startMillis, long endMillis) {
        this(initialValue, finalValue, startMillis, endMillis, new LinearInterpolator());
    }

    @Override
    public void apply(Particle particle, long milliseconds) {
        if (milliseconds < mStartTime) {
            particle.setAlpha(mInitialValue);
        } else if (milliseconds > mEndTime) {
            particle.setAlpha(mFinalValue);
        } else {
            float interpolatedValue = mInterpolator.getInterpolation((milliseconds - mStartTime) * 1f / mDuration);
            particle.setAlpha((int) (mInitialValue + mValueIncrement * interpolatedValue));
        }
    }

}
