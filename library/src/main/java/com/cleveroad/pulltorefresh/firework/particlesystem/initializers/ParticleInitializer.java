package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public interface ParticleInitializer {
    void initParticle(Particle particle, Random random);
}
