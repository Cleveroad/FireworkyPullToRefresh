package com.cleveroad.pulltorefresh.firework;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.cleveroad.pulltorefresh.firework.particlesystem.Particle;
import com.cleveroad.pulltorefresh.firework.particlesystem.ParticleSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


class FireworkRefreshDrawable extends BaseRefreshDrawable {
    private static final float SCALE_START_PERCENT = 0.5f;
    private static final Random RND = new Random();

    private final Configuration mConfig;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();

    private FireworkyPullToRefreshLayout mParent;

    private boolean mIsAnimationStarted = false;
    private boolean mSkipRocketAnimation = false;

    private float mPercent;
    private float mPointerPositionX, mPointerPositionY;
    private int   mScreenWidth;
    private int   mTop;

    /**
     * Background
     */
    private static final float BACKGROUND_RATIO = 1.5f;
    private static final float BACKGROUND_INITIAL_SCALE = 1.35f;
    private int mBackgroundHeight;

    /**
     * Firework rocket
     */
    private static final float ROCKET_INITIAL_SCALE = 1.1f;
    private static final float ROCKET_FINAL_SCALE   = 0.8f;
    private static final float ROCKET_MAX_DEVIATION_ANGLE = 45;
    private final ValueAnimator mRocketAnimator = new ValueAnimator();
    private float mRocketAnimationPercent;
    private float mIgnoredRocketXOffset = 0;
    private float mRocketTopOffset;
    private double mLastRocketAngle;
    private boolean mIsRocketAnimationFinished;

    /**
     * Rocket smoke
     */
    private final List<Bubble> mRocketSmokeBubbles = new LinkedList<>();
    private final Bubble.Builder mRocketSmokeBuilder = Bubble.newBuilder().alpha(80).dAlpha(-0.5f).dRadius(-0.05f);
    private float mPointCache[] = new float[2];

    /**
     * Flame
     */
    private static final long FLAME_BLINKING_DURATION = 300;
    private final ValueAnimator mFlameAnimator = new ValueAnimator();
    private float mFlameScale = 1;

    /**
     * Firework
     */
    private static final int MAX_FIREWORKS_COUNT = 3;
    private int mFireworkBubbleRadius;
    private List<ParticleSystem> mParticleSystems = new LinkedList<>();

    /**
     * Curve
     */
    private static final float CURVE_TARGET_POINT_VALUE_NOT_ANIMATED = Float.MAX_VALUE;
    private static final float CURVE_VERTICAL_POINT_PERCENT = 0.7f;
    private float mCurveTargetPointAnimValue = CURVE_TARGET_POINT_VALUE_NOT_ANIMATED;
    private final ValueAnimator mCurveAnimator = new ValueAnimator();
    /**
     * Constructor
     */
    FireworkRefreshDrawable(final FireworkyPullToRefreshLayout layout) {
        super(layout);
        mParent = layout;
        mConfig = new Configuration(getContext());

        layout.post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    @Override
    protected void init() {
        int viewWidth = mParent.getWidth();
        if (viewWidth <= 0 || viewWidth == mScreenWidth) {
            return;
        }

        setupAnimations();

        mScreenWidth = viewWidth;
        mBackgroundHeight = (int) (BACKGROUND_RATIO * mScreenWidth);

        mRocketTopOffset = mParent.getTotalDragDistance()
                - mConfig.getRocketDrawable().getIntrinsicHeight()
                - mConfig.getFlameDrawable().getIntrinsicHeight() / 2f
                + mConfig.getRocketDrawable().getIntrinsicHeight() / 10f;

        mTop = -mParent.getTotalDragDistance();

        mFireworkBubbleRadius = (int) (mParent.getTotalDragDistance() * 0.065f);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawCurve(canvas);
        drawBackground(canvas);
        if(!mSkipRocketAnimation) {
            drawRocketSmoke(canvas);
            drawRocket(canvas);
        }
        drawFireworks(canvas);

        canvas.restoreToCount(saveCount);
    }

    /**
     * Curve
     * *********************************************************************************************
     */
    private void drawCurve(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, getCurveYStart());
        mPath.lineTo(getCurveXStart(), getCurveYStart());
        mPath.quadTo(getCurveTargetPointX(), getCurveTargetPointY(), getCurveXEnd(), getCurveYEnd());
        mPath.lineTo(getCurveXEnd(), canvas.getWidth());
        mPath.lineTo(canvas.getWidth(), canvas.getHeight());
        mPath.lineTo(0, canvas.getHeight());
        mPath.close();
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
    }

    private float getCurveXStart() {
        return 0f;
    }

    private float getCurveXEnd() {
        return mParent.getWidth();
    }

    private float getCurveYStart() {
        return mParent.getTotalDragDistance() * (1f + CURVE_VERTICAL_POINT_PERCENT - Math.min(mPercent, 1.0f));
    }

    private float getCurveYEnd() {
        return getCurveYStart();
    }

    private float getCurveTargetPointX() {
        return mParent.getWidth() / 2f;
    }

    private float getCurveTargetPointY() {
        return BezierCurveHelper.getQuadTargetPoint(
                getCurveYStart(),
                getCurveYEnd(),
                mCurveTargetPointAnimValue != CURVE_TARGET_POINT_VALUE_NOT_ANIMATED ?
                        getCurveYStart() + mCurveTargetPointAnimValue
                        :
                        mParent.getTotalDragDistance(),
                0.5f);
    }

    /**
     * Background
     * *********************************************************************************************
     */
    private void drawBackground(Canvas canvas) {
        canvas.save();

        float dragPercent = Math.min(1f, Math.abs(mPercent));
        float backgroundScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            backgroundScale = BACKGROUND_INITIAL_SCALE - (BACKGROUND_INITIAL_SCALE - 1.0f) * scalePercent;
        } else {
            backgroundScale = BACKGROUND_INITIAL_SCALE;
        }

        canvas.scale(backgroundScale, backgroundScale, canvas.getWidth() / 2f, mParent.getTotalDragDistance() / 2f);

        mConfig.getBackgroundDrawable().setBounds(0, 0, canvas.getWidth(), mParent.getTotalDragDistance());
        mConfig.getBackgroundDrawable().draw(canvas);
        canvas.restore();
    }

    /**
     * Rocket
     * *********************************************************************************************
     */
    private void drawRocket(Canvas canvas) {
        if(mIsRocketAnimationFinished) {
            return;
        }

        Drawable rocketDrawable = mConfig.getRocketDrawable();
        Drawable flameDrawable = mConfig.getFlameDrawable();

        canvas.save();

        float dragPercent = Math.min(1f, Math.abs(mPercent));
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;

        float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
        float rocketScale = ROCKET_INITIAL_SCALE + (ROCKET_FINAL_SCALE - ROCKET_INITIAL_SCALE) * scalePercent;
        canvas.scale(rocketScale, rocketScale, canvas.getWidth() / 2.f, canvas.getHeight() / 2.f);

        float offsetX = canvas.getWidth() / 2.f
                - rocketDrawable.getIntrinsicWidth() / 2.f
                + (1f - rocketScale) * rocketDrawable.getIntrinsicWidth() / 2.f;

        float offsetY = mRocketTopOffset
                + (1.0f - dragPercent) * mParent.getTotalDragDistance()
                - mTop;
        offsetY -= (
                Math.max(mParent.getTotalDragDistance(), mScreenWidth)
                        + rocketDrawable.getIntrinsicHeight()
        ) * mRocketAnimationPercent;

        canvas.rotate((float) getRocketAngle(),
                canvas.getWidth() / 2.f,
                offsetY + rocketDrawable.getIntrinsicHeight() / 2.f);

        float offsetXDelta = 0;
        if (mIsAnimationStarted) {
            int sign = -1;
            float rocketAngle = (float) getRocketAngle();
            if (mPointerPositionX < mScreenWidth / 2.) {
                sign = 1;
            } else {
                rocketAngle = 360 - rocketAngle;
            }

            double rocketAngleRadians = rocketAngle * (Math.PI / 180.);
            double tan = Math.tan(rocketAngleRadians);


            offsetXDelta = (float) ((mParent.getTotalDragDistance() - offsetY) * tan) * sign;
            if(mIgnoredRocketXOffset == 0) {
                mIgnoredRocketXOffset = offsetXDelta;
            }
            offsetXDelta -= mIgnoredRocketXOffset;

            //rocket smoke
            final Bubble lastSmokeBubble = mRocketSmokeBubbles.isEmpty() ? null : mRocketSmokeBubbles.get(mRocketSmokeBubbles.size() - 1);
            int rocketDPositionSign = lastSmokeBubble == null || lastSmokeBubble.getDPosition().getX() < 0 ? 1 : -1;

            float points[] = mapPoints(
                    canvas,
                    offsetX + offsetXDelta + rocketDrawable.getIntrinsicWidth() / 2f,
                    offsetY + rocketDrawable.getIntrinsicHeight());

            if (lastSmokeBubble == null || points[1] < (lastSmokeBubble.getYPos() - mFireworkBubbleRadius)) {
                mRocketSmokeBuilder
                        .position(points[0], points[1])
                        .dPosition(0.05f * rocketDPositionSign, 0.05f)
                        .radius(mFireworkBubbleRadius / 2f)
                        .color(Color.WHITE);

                mRocketSmokeBubbles.add(mRocketSmokeBuilder.build());
            }
        }

        //drawing rocket
        canvas.translate(offsetX + offsetXDelta, offsetY);
        rocketDrawable.setBounds(0, 0, rocketDrawable.getIntrinsicWidth(), rocketDrawable.getIntrinsicHeight());
        rocketDrawable.draw(canvas);


        //rocket flame
        canvas.translate(
                -rocketDrawable.getIntrinsicWidth() * rocketScale / 2f,
                rocketDrawable.getIntrinsicHeight() * rocketScale - flameDrawable.getIntrinsicHeight() / 4f);
        canvas.scale(mFlameScale, mFlameScale, flameDrawable.getIntrinsicWidth() / 2f, flameDrawable.getIntrinsicHeight() / 2f);


        flameDrawable.setBounds(0, 0, flameDrawable.getIntrinsicWidth(), flameDrawable.getIntrinsicHeight());
        flameDrawable.draw(canvas);

        canvas.restore();
    }

    /**
     * Rocket flying
     * *********************************************************************************************
     */
    @FloatRange(from = 0, to = 360)
    private double getRocketAngle() {
        double xTouch = mPointerPositionX;
        double yTouch = mPointerPositionY;
        if(xTouch == 0. && yTouch == 0.) {
            return mLastRocketAngle = 0;
        }

        int sign = 1;
        if (xTouch < (mScreenWidth / 2.)) {
            xTouch = mScreenWidth - xTouch;
            sign = -1;
        }
        double xLength = (mScreenWidth - 2. * xTouch) / 2.;
        double yLength = yTouch - mRocketTopOffset + mConfig.getRocketDrawable().getIntrinsicHeight() / 2.;
        double tgAlpha = yLength / xLength;
        double result = Math.atan(tgAlpha) * (180. / Math.PI);

        result =  (90. - result) * sign + 180.;
        if(result > 360 - ROCKET_MAX_DEVIATION_ANGLE || result < ROCKET_MAX_DEVIATION_ANGLE) {
            return mLastRocketAngle = result;
        } else {
            return mLastRocketAngle;
        }
    }

    /**
     * Fireworks
     * *********************************************************************************************
     */
    private void emitFirework(final Canvas canvas) {
        ParticleSystem particleSystem1 = new ParticleSystem((Activity)getContext(), 20, R.drawable.ptr_star_white, 800L, mParent.mRefreshView.getId());
        particleSystem1.setScaleRange(0.7f, 1.3f);
        particleSystem1.setSpeedRange(0.03f, 0.07f);
        particleSystem1.setRotationSpeedRange(90, 180);
        particleSystem1.setFadeOut(500, new DecelerateInterpolator());
        particleSystem1.setTintColor(getRandomBubbleColor());

        ParticleSystem particleSystem2 = new ParticleSystem((Activity)getContext(), 20, R.drawable.ptr_star_white, 800L, mParent.mRefreshView.getId());
        particleSystem2.setScaleRange(0.7f, 1.3f);
        particleSystem2.setSpeedRange(0.03f, 0.07f);
        particleSystem2.setRotationSpeedRange(90, 180);
        particleSystem2.setFadeOut(500, new DecelerateInterpolator());
        particleSystem2.setTintColor(getRandomBubbleColor());

        float width = canvas.getWidth();
        float height = getCurveYStart();

        float fireworkWidth = width / MAX_FIREWORKS_COUNT;
        float fireworkHeight = height / MAX_FIREWORKS_COUNT;

        float x = RND.nextInt((int) (width - fireworkWidth)) + fireworkWidth / 2f;
        float y = RND.nextInt((int) (height - fireworkHeight)) + fireworkHeight;

        particleSystem1.emit((int) x, (int) y, 70, 500);
        particleSystem2.emit((int) x, (int) y, 70, 500);

//        particleSystem1.emit((int) x, (int) y, 200, 200, 70, 500);
//        particleSystem2.emit((int) x, (int) y, 200, 200, 70, 500);

        mParticleSystems.add(particleSystem1);
        mParticleSystems.add(particleSystem2);
    }

    private void drawFireworks(final Canvas canvas) {
        if (!mIsAnimationStarted || mRocketAnimationPercent < 0.95f) {
            return;
        }

        if (mParticleSystems.isEmpty()) {
            emitFirework(canvas);
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

    private void drawRocketSmoke(Canvas canvas) {
        boolean isSmokeInvisible = true;
        for(Bubble b : mRocketSmokeBubbles) {
            mPaint.setColor(b.getColor());
            mPaint.setAlpha(b.incrementAlphaAndGet());
            canvas.drawCircle(b.incrementXAndGet(), b.incrementYAndGet(), b.incrementRadiusAndGet(), mPaint);
            isSmokeInvisible &= b.isInvisible();
        }

        if(isSmokeInvisible) {
            mRocketSmokeBubbles.clear();
        }
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) {
            invalidateSelf();
        }
    }

    @Override
    public void setPointerPosition(float x, float y) {
        if(!mIsAnimationStarted) {
            mPointerPositionX = setVariable(x);
            mPointerPositionY = setVariable(y);
        }
    }

    private void setPercent(float percent) {
        mPercent = percent;
        if(percent == 0f && mFlameAnimator.isRunning()) {
            mFlameAnimator.cancel();
        } else if(!mFlameAnimator.isRunning()) {
            mFlameAnimator.start();
        }
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void start() {
        resetOrigins();
        mIsAnimationStarted = true;
        mCurveAnimator.start();
        mRocketAnimator.start();
    }

    @Override
    public void stop() {
        mIsAnimationStarted = false;
        mSkipRocketAnimation = false;
        mRocketAnimator.cancel();
        mCurveAnimator.cancel();
        resetOrigins();
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mBackgroundHeight + top);
    }

    @Override
    public boolean isRunning() {
        return mIsAnimationStarted;
    }

    @Override
    protected void setupAnimations() {
        //flame animation
        mFlameAnimator.cancel();
        mFlameAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mFlameAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mFlameAnimator.setDuration(FLAME_BLINKING_DURATION);
        mFlameAnimator.setFloatValues(0f, 1f);
        mFlameAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFlameScale = setVariable((float) animation.getAnimatedValue());
            }
        });

        //rocket animation
        mRocketAnimator.cancel();
        mRocketAnimator.setDuration(mConfig.getRocketAnimDuration());
        mRocketAnimator.setFloatValues(0f, 1f);
        mRocketAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRocketAnimationPercent = !mSkipRocketAnimation ? (float) valueAnimator.getAnimatedValue() : 1f;
                if (mSkipRocketAnimation) {
                    valueAnimator.cancel();
                }
            }
        });
        mRocketAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIsRocketAnimationFinished = true;
            }
        });

        //curve animation
        mCurveAnimator.cancel();
        mCurveAnimator.setDuration(mConfig.getRocketAnimDuration() * 2);

        mCurveAnimator.setValues(
                PropertyValuesHolder.ofFloat("force", 1f, 0f),
                PropertyValuesHolder.ofFloat("value", (float) Math.PI, (float) (3f / 2f * Math.PI * 3f)));
        mCurveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(mPercent == 0f) return;
                float force = (float) valueAnimator.getAnimatedValue("force");
                float value = (float) valueAnimator.getAnimatedValue("value");

                float maxDy = mParent.getTotalDragDistance() * (2f - CURVE_VERTICAL_POINT_PERCENT - Math.min(mPercent, 1.0f));
                mCurveTargetPointAnimValue = !mSkipRocketAnimation ? -(float) (maxDy * Math.cos(value) * force) : 0f;
                if (mSkipRocketAnimation) {
                    valueAnimator.cancel();
                }
            }
        });
    }

    Configuration getConfig() {
        return mConfig;
    }

    private float setVariable(float value) {
        invalidateSelf();
        return value;
    }

    private void resetOrigins() {
        setPercent(0f);
        mRocketSmokeBubbles.clear();
        mParticleSystems.clear();

        mIgnoredRocketXOffset = 0;
        mRocketAnimationPercent = 0;
        mCurveTargetPointAnimValue = CURVE_TARGET_POINT_VALUE_NOT_ANIMATED;

        mIsRocketAnimationFinished = false;

        for (ParticleSystem ps : mParticleSystems) {
            ps.stopEmitting();
            ps.cancel();
        }
    }

    @ColorInt
    private int getRandomBubbleColor() {
        int fireworkColors[] = mConfig.getFireworkColors();
        return fireworkColors[RND.nextInt(fireworkColors.length)];
    }

    private float[] mapPoints(Canvas canvas, float x, float y) {
        mPointCache[0] = x;
        mPointCache[1] = y;
        canvas.getMatrix().mapPoints(mPointCache);
        return mPointCache;
    }

    void setSkipRocketAnimation(boolean skipRocketAnimation) {
        this.mSkipRocketAnimation = skipRocketAnimation;
    }
}