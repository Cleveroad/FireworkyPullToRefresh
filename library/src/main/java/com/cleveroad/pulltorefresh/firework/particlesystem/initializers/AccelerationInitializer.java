package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class AccelerationInitializer implements ParticleInitializer {

    private float mMinValue;
    private float mMaxValue;
    private int mMinAngle;
    private int mMaxAngle;

    public AccelerationInitializer(float minAcceleration, float maxAcceleration, int minAngle, int maxAngle) {
        mMinValue = minAcceleration;
        mMaxValue = maxAcceleration;
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        float angle = mMinAngle;
        if (mMaxAngle != mMinAngle) {
            angle = random.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
        }
        float angleInRads = (float) (angle * Math.PI / 180f);
        float value = random.nextFloat() * (mMaxValue - mMinValue) + mMinValue;
        particle.setAccelerationX((float) (value * Math.cos(angleInRads)));
        particle.setAccelerationY((float) (value * Math.sin(angleInRads)));
    }

}
