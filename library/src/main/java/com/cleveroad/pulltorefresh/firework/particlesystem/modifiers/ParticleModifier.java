package com.cleveroad.pulltorefresh.firework.particlesystem.modifiers;


import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

public interface ParticleModifier {

    /**
     * modifies the specific value of a particle given the current milliseconds
     *
     * @param particle
     * @param milliseconds
     */
    void apply(Particle particle, long milliseconds);

}
