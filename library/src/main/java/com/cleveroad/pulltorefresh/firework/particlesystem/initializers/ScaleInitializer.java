package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class ScaleInitializer implements ParticleInitializer {

    private float mMaxScale;
    private float mMinScale;

    public ScaleInitializer(float minScale, float maxScale) {
        mMinScale = minScale;
        mMaxScale = maxScale;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        particle.setScale(random.nextFloat() * (mMaxScale - mMinScale) + mMinScale);
    }

}
