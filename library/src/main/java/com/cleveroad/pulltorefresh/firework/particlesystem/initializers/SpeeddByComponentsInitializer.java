package com.cleveroad.pulltorefresh.firework.particlesystem.initializers;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;

import java.util.Random;


public class SpeeddByComponentsInitializer implements ParticleInitializer {

	private float mMinSpeedX;
	private float mMaxSpeedX;
	private float mMinSpeedY;
	private float mMaxSpeedY;

	public SpeeddByComponentsInitializer(float speedMinX, float speedMaxX, float speedMinY, float speedMaxY) {
		mMinSpeedX = speedMinX;
		mMaxSpeedX = speedMaxX;
		mMinSpeedY = speedMinY;
		mMaxSpeedY = speedMaxY;
	}

	@Override
	public void initParticle(Particle p, Random r) {
		p.mSpeedX = r.nextFloat()*(mMaxSpeedX-mMinSpeedX)+mMinSpeedX;
		p.mSpeedY = r.nextFloat()*(mMaxSpeedY-mMinSpeedY)+mMinSpeedY;
	}

}
