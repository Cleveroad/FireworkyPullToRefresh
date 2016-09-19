package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class RotationSpeedInitializer implements ParticleInitializer {

    private float mMinRotationSpeed;
    private float mMaxRotationSpeed;

    public RotationSpeedInitializer(float minRotationSpeed, float maxRotationSpeed) {
        mMinRotationSpeed = minRotationSpeed;
        mMaxRotationSpeed = maxRotationSpeed;
    }

    @Override
    public void initParticle(Particle particle, Random random) {
        particle.setRotationSpeed(random.nextFloat() * (mMaxRotationSpeed - mMinRotationSpeed) + mMinRotationSpeed);
    }

}
