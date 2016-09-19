package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class RotationInitiazer implements ParticleInitializer {

    private int mMinAngle;
    private int mMaxAngle;

    public RotationInitiazer(int minAngle, int maxAngle) {
        mMinAngle = minAngle;
        mMaxAngle = maxAngle;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        p.mInitialRotation = mMinAngle == mMaxAngle ? mMinAngle : r.nextInt(mMaxAngle - mMinAngle) + mMinAngle;
    }
}
