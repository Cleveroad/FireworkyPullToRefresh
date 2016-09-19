package com.cleveroad.pulltorefresh.firework.particlesystem;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;


class AnimatedParticle extends Particle {

    private AnimationDrawable mAnimationDrawable;
    private int mTotalTime;

    AnimatedParticle(AnimationDrawable animationDrawable) {
        mAnimationDrawable = animationDrawable;
        mImage = ((BitmapDrawable) mAnimationDrawable.getFrame(0)).getBitmap();
        // If it is a repeating animation, calculate the time
        mTotalTime = 0;
        for (int i = 0; i < mAnimationDrawable.getNumberOfFrames(); i++) {
            mTotalTime += mAnimationDrawable.getDuration(i);
        }
    }

    @Override
    public boolean update(long milliseconds) {
        boolean active = super.update(milliseconds);
        if (active) {
            long animationElapsedTime = 0;
            long realMilliseconds = milliseconds - mStartingMilisecond;
            if (realMilliseconds > mTotalTime) {
                if (mAnimationDrawable.isOneShot()) {
                    return false;
                } else {
                    realMilliseconds = realMilliseconds % mTotalTime;
                }
            }
            for (int i = 0; i < mAnimationDrawable.getNumberOfFrames(); i++) {
                animationElapsedTime += mAnimationDrawable.getDuration(i);
                if (animationElapsedTime > realMilliseconds) {
                    mImage = ((BitmapDrawable) mAnimationDrawable.getFrame(i)).getBitmap();
                    break;
                }
            }
        }
        return active;
    }
}
