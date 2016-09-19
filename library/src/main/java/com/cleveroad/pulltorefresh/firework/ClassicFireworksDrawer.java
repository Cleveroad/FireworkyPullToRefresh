package com.cleveroad.pulltorefresh.firework;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class ClassicFireworksDrawer implements FireworksDrawer {
    private static final Random RND = new Random();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int mMaxFireworksCount;
    private final Configuration mConfiguration;
    private final List<List<Bubble>> mVisibleFireworksList = new LinkedList<>();
    private int mFireworkBubbleRadius;

    ClassicFireworksDrawer(@NonNull Configuration configuration, int maxFireworksCount, int bubbleRadius) {
        mConfiguration = configuration;
        mMaxFireworksCount = maxFireworksCount;
        mFireworkBubbleRadius = bubbleRadius;
    }

    private List<Bubble> getFirework(int width, int height) {
        List<Bubble> firework = new ArrayList<>(50);

        float fireworkWidth = width / mMaxFireworksCount;
        float fireworkHeight = height / mMaxFireworksCount;

        float x = RND.nextInt((int) (width - fireworkWidth)) + fireworkWidth / 2f;
        float y = RND.nextInt((int) (height - fireworkHeight)) + fireworkHeight / 2f;

        Bubble.Builder builder = Bubble.newBuilder()
                .position(new Bubble.Point(x, y))
                .dRotationAngle(0.01d);

        int color = getRandomBubbleColor();
        builder.dPosition(0.f, 0.f).color(color)
                .radius(mFireworkBubbleRadius * .2f).dRadius(.1f).alpha(255).dAlpha(-1.7f).build(); //center

        color = getRandomBubbleColor();
        for (int k = 360 / 45; k >= 0; k--) {
            firework.add(builder
                    .dPosition(Utils.rotateX(.7f, 0, 0, 0, k * 45), Utils.rotateY(.7f, 0, 0, 0, k * 45))
                    .radius(mFireworkBubbleRadius * .4f)
                    .dRadius(-.15f)
                    .dAlpha(-.8f)
                    .color(color)
                    .build());
        }

        color = getRandomBubbleColor();
        for (int k = 360 / 30; k >= 0; k--) {
            firework.add(builder
                    .dPosition(Utils.rotateX(.5f, 0, 0, 0, k * 30), Utils.rotateY(.5f, 0, 0, 0, k * 30))
                    .radius(mFireworkBubbleRadius * .2f)
                    .dRadius(-.1f)
                    .dAlpha(-.8f)
                    .color(color)
                    .build());
        }

        color = getRandomBubbleColor();
        for (int k = 360 / 30; k >= 0; k--) {
            firework.add(builder
                    .dPosition(Utils.rotateX(.3f, 0, 0, 0, k * 30), Utils.rotateY(.3f, 0, 0, 0, k * 30))
                    .radius(mFireworkBubbleRadius * .2f)
                    .dRadius(-.1f)
                    .dAlpha(-.8f)
                    .color(color)
                    .build());
        }

        return firework;
    }

    @Override
    public void draw(Canvas canvas, int width, int height) {
        if(mVisibleFireworksList.isEmpty()) {
            mVisibleFireworksList.add(getFirework(width, height));
        }

        for (int i = 0; i < mVisibleFireworksList.size(); i++) {
            List<Bubble> firework = mVisibleFireworksList.get(i);
            boolean isFireworkFinished = true;
            boolean isNeedToShowNextFirework = true;

            for (Bubble b : firework) {
                b.incrementRotationAngle();
                mPaint.setColor(b.getColor());
                mPaint.setAlpha(b.incrementAlphaAndGet());
                float radius = b.incrementRadiusAndGet();
                canvas.drawCircle(b.incrementXAndGet(), b.incrementYAndGet(), radius, mPaint);
                isFireworkFinished &= b.isInvisible();
                isNeedToShowNextFirework &= b.getPercent() > 0.65f;
            }

            if (isFireworkFinished) {
                mVisibleFireworksList.remove(i);
                i--;
                continue;
            }

            if (isNeedToShowNextFirework && mVisibleFireworksList.size() < mMaxFireworksCount) {
                mVisibleFireworksList.add(getFirework(width, height));
            }
        }
    }

    @Override
    public void reset() {
        mVisibleFireworksList.clear();
    }

    @ColorInt
    private int getRandomBubbleColor() {
        int fireworkColors[] = mConfiguration.getFireworkColors();
        return fireworkColors[RND.nextInt(fireworkColors.length)];
    }
}
