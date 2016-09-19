package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class RotationInitializer implements ParticleInitializer {

    private int mMinAngle;
    private int mMaxAngle;

    public RotationInitializer(int minAngle, int maxAngle) {
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        particle.setInitialRotation(mMinAngle == mMaxAngle ? mMinAngle : random.nextInt(mMaxAngle - mMinAngle) + mMinAngle);
    }
}
