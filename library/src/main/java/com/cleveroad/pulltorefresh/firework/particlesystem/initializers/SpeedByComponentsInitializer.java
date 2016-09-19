package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class SpeedByComponentsInitializer implements ParticleInitializer {

    private float mMinSpeedX;
    private float mMaxSpeedX;
    private float mMinSpeedY;
    private float mMaxSpeedY;

    public SpeedByComponentsInitializer(float speedMinX, float speedMaxX, float speedMinY, float speedMaxY) {
        mMinSpeedX = speedMinX;
        mMaxSpeedX = speedMaxX;
        mMinSpeedY = speedMinY;
        mMaxSpeedY = speedMaxY;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        particle.setSpeedX(random.nextFloat() * (mMaxSpeedX - mMinSpeedX) + mMinSpeedX);
        particle.setSpeedY(random.nextFloat() * (mMaxSpeedY - mMinSpeedY) + mMinSpeedY);
    }

}
