package com.cleveroad.pulltorefresh.firework;

import android.app.Activity;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.cleveroad.pulltorefresh.firework.particlesystem.ParticleSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class ModernFireworksDrawer implements FireworksDrawer {
    private static final Random RND = new Random();
    private final int mMaxFireworksCount;
    private final ViewGroup mParentView;
    private final Configuration mConfiguration;
    private final List<ParticleSystem> mParticleSystems = new LinkedList<>();

    ModernFireworksDrawer(@NonNull Configuration configuration, int maxFireworksCount, @NonNull ViewGroup parentView) {
        mConfiguration = configuration;
        mMaxFireworksCount = maxFireworksCount;
        mParentView = parentView;
    }

    private void emitFirework(int width, int height) {

        float fireworkWidth = width / mMaxFireworksCount;
        float fireworkHeight = height / mMaxFireworksCount;

        float x = RND.nextInt((int) (width - fireworkWidth)) + fireworkWidth / 2f;
        float y = RND.nextInt((int) (height - fireworkHeight)) + fireworkHeight;

        for(int i=0; i< mMaxFireworksCount; i++) {
            ParticleSystem particleSystem = new ParticleSystem(
                    (Activity) mParentView.getContext(),     //activity
                    20,                         //max particles
                    R.drawable.ptr_star_white,  //icon
                    800L,                       //time to live
                    mParentView);      //parent view
            particleSystem.setScaleRange(0.7f, 1.3f);
            particleSystem.setSpeedRange(0.03f, 0.07f);
            particleSystem.setRotationSpeedRange(90, 180);
            particleSystem.setFadeOut(500, new DecelerateInterpolator());
            particleSystem.setTintColor(getRandomBubbleColor());

            mParticleSystems.add(particleSystem);
            particleSystem.emit((int) x, (int) y, 70, 500);
        }
    }

    @Override
    public void draw(Canvas canvas, int width, int height) {
        if (mParticleSystems.isEmpty()) {
            emitFirework(width, height);
        }

        for (int i = 0; i < mParticleSystems.size(); i++) {
            ParticleSystem particleSystem = mParticleSystems.get(i);
            particleSystem.draw(canvas);

            if(!particleSystem.isRunning()) {
                mParticleSystems.remove(i);
                i--;
            }
        }
    }

    @Override
    public void reset() {
        for (ParticleSystem ps : mParticleSystems) {
            ps.stopEmitting();
            ps.cancel();
        }
        mParticleSystems.clear();
    }

    @ColorInt
    private int getRandomBubbleColor() {
        int fireworkColors[] = mConfiguration.getFireworkColors();
        return fireworkColors[RND.nextInt(fireworkColors.length)];
    }
}
