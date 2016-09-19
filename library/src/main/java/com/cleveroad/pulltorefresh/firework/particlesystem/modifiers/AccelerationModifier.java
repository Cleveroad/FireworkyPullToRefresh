package com.cleveroad.pulltorefresh.firework.particlesystem.modifiers;


import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

public class AccelerationModifier implements ParticleModifier {

    private float mVelocityX;
    private float mVelocityY;

    public AccelerationModifier(float velocity, float angle) {
        float velocityAngleInRads = (float) (angle * Math.PI / 180f);
        mVelocityX = (float) (velocity * Math.cos(velocityAngleInRads));
        mVelocityY = (float) (velocity * Math.sin(velocityAngleInRads));
    }

    @Override
    public void apply(Particle particle, long milliseconds) {
        particle.setCurrentX(mVelocityX * milliseconds * milliseconds + particle.getCurrentX());
        particle.setCurrentY(mVelocityY * milliseconds * milliseconds + particle.getCurrentY());
    }
}
