package com.cleveroad.pulltorefresh.firework;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.FrameLayout;


/**
 * The {@link FireworkyPullToRefreshLayout} should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that instantiates this view should
 * add an {@link FireworkyPullToRefreshLayout.OnRefreshListener} to be notified whenever the swipe
 * to refresh gesture is completed.
 * The {@link FireworkyPullToRefreshLayout} will notify the listener each and every time the gesture
 * is completed again; the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should not be a refresh, it
 * must call {@link FireworkyPullToRefreshLayout#setRefreshing(boolean)} with {@link Boolean#FALSE}
 * param to cancel any visual indication of a refresh.
 * If an activity wishes to show just the progress animation, it should call
 * {@link FireworkyPullToRefreshLayout#setRefreshing(boolean)} with {@link Boolean#TRUE} param.
 * To disable the gesture and progress animation, call
 * {@link FireworkyPullToRefreshLayout#setRefreshing(boolean)} with {@link Boolean#FALSE} param on the view.
 *
 * <p><b>Note!</b> To call the {@link FireworkyPullToRefreshLayout#setRefreshing(boolean)} method
 * you must wrap it in {@link FireworkyPullToRefreshLayout#post(Runnable)}</p>
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a result of the
 * gesture and can only support one direct child. This view will also be made the target of the
 * gesture and will be forced to match both the width and the height supplied in this layout.
 * The SwipeRefreshLayout does not provide accessibility events; instead, a menu item must be
 * provided to allow refresh of the content wherever this gesture is used.
 *
 * Supported child views: RecyclerView, ListView, ScrollView, NestedScrollView etc.
 * </p>
 */
public class FireworkyPullToRefreshLayout extends ViewGroup {
    private static final String EXTRA_SUPER_STATE = "EXTRA_SUPER_STATE";
    private static final String EXTRA_IS_REFRESHING = "EXTRA_IS_REFRESHING";
    private static final int INVALID_POINTER_ID = -1;

    private static final float DRAG_RATE = .85f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;

    private static final int MAX_OFFSET_ANIMATION_DURATION = 700;
    private static final int ROCKET_DRAG_MAX_DISTANCE = 230;

    @Nullable
    private OnChildScrollUpCallback mOnChildScrollUpCallback;

    private int mTotalDragDistance;
    private int mTouchSlop;

    private int mTargetPaddingTop;
    private int mTargetPaddingBottom;
    private int mTargetPaddingRight;
    private int mTargetPaddingLeft;

    private int   mFrom;
    private int   mCurrentOffsetTop;
    private int   mActivePointerId;
    private float mFromDragPercent;
    private float mCurrentDragPercent;
    private float mInitialMotionY;

    private boolean mIsRefreshing;
    private boolean mIsBeingDragged;

    private View mTarget;
    ViewGroup mRefreshView;
    private FireworkRefreshDrawable mRefreshDrawable;

    private final Configuration mConfig;

    private final Interpolator mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };
    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = mTotalDragDistance;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;
            mRefreshDrawable.setPercent(mCurrentDragPercent, false);
            setTargetOffsetTop(offset, false);
        }
    };
    private final Animation.AnimationListener mToStartListener = new SimpleAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            mRefreshDrawable.stop();
            mCurrentOffsetTop = mTarget.getTop();
        }
    };
    private OnRefreshListener mOnRefreshListener;

    /**
     * Simple constructor to use when creating a {@link FireworkyPullToRefreshLayout} from code.
     *
     * @param context
     */
    public FireworkyPullToRefreshLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating {@link FireworkyPullToRefreshLayout} from XML.
     *
     * @param context
     * @param attrs
     */
    public FireworkyPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (getChildCount() > 1) {
            throw new RuntimeException("You can attach only one child to the FireworkyPullToRefreshLayout!");
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mRefreshView = new FrameLayout(context);
        //noinspection ResourceType
        mRefreshView.setId(121122);     //TODO refactor here
        mTotalDragDistance = Utils.convertDpToPixel(context, ROCKET_DRAG_MAX_DISTANCE);
        mRefreshView.setBackgroundDrawable(mRefreshDrawable = new FireworkRefreshDrawable(this));
        mConfig =  mRefreshDrawable.getConfig();

        readAttributes(context, attrs);

        addView(mRefreshView);
        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    private void readAttributes(Context context, @Nullable AttributeSet attrs) {
        if(attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FireworkyPullToRefreshLayout);
        try {
            int backgroundId = array.getResourceId(R.styleable.FireworkyPullToRefreshLayout_ptr_background, -1);
            int backgroundColor = array.getColor(R.styleable.FireworkyPullToRefreshLayout_ptr_backgroundColor, Integer.MAX_VALUE);
            if(backgroundId != -1) {
                getConfig().setBackground(backgroundId);
            } else {
                if(backgroundColor != Integer.MAX_VALUE) {
                    getConfig().setBackgroundColor(backgroundColor);
                }
            }
            getConfig().setFireworkColors(array.getResourceId(R.styleable.FireworkyPullToRefreshLayout_ptr_fireworkColors, R.array.ptr_defColorSet));
            getConfig().setRocketAnimDuration(array.getInteger(R.styleable.FireworkyPullToRefreshLayout_ptr_rocketAnimDuration, 500));
        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View targetView = getTargetView();
        if (targetView != null) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingBottom() - getPaddingTop(), MeasureSpec.EXACTLY);

            targetView.measure(widthMeasureSpec, heightMeasureSpec);
            mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View targetView = getTargetView();
        if (targetView != null) {
            int height = getMeasuredHeight();
            int width = getMeasuredWidth();
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = getPaddingRight();
            int bottom = getPaddingBottom();

            targetView.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
            mRefreshView.layout(left, top, left + width - right, top + height - bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || canChildScrollUp() || mIsRefreshing) {
            return false;
        }

        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTop(0, true);
                mActivePointerId = motionEvent.getPointerId(0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(motionEvent, mActivePointerId);
                if (initialMotionY == -1f) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER_ID) {
                    return false;
                }

                final float y = getMotionEventY(motionEvent, mActivePointerId);
                if (y == -1f) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(motionEvent);
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SUPER_STATE, super.onSaveInstanceState());
        bundle.putBoolean(EXTRA_IS_REFRESHING, mIsRefreshing);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = ((Bundle) state);
            super.onRestoreInstanceState(bundle.getParcelable(EXTRA_SUPER_STATE));
            if(bundle.getBoolean(EXTRA_IS_REFRESHING)) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshDrawable.setSkipRocketAnimation(true);
                        setRefreshing(true, false);
                    }
                });
            }
        }
    }

    private float getMotionEventY(MotionEvent motionEvent, int activePointerId) {
        final int index = motionEvent.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1f;
        }
        return motionEvent.getY(index);
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        final int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
        final int pointerId = motionEvent.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            mActivePointerId = motionEvent.getPointerId(pointerIndex == 0 ? 1 : 0);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (!mIsBeingDragged) {
            return super.onTouchEvent(motionEvent);
        }

        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                if (pointerIndex != 0) {
                    return false;
                }
                final float x = motionEvent.getX(pointerIndex);
                final float y = motionEvent.getY(pointerIndex);
                final float yDiff = y - mInitialMotionY;
                final float scrollTop = yDiff * DRAG_RATE;
                mCurrentDragPercent = scrollTop / mTotalDragDistance;
                if (mCurrentDragPercent < 0) {
                    return false;
                }
                float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                float slingshotDist = mTotalDragDistance;
                int targetY = (int) ((slingshotDist * boundedDragPercent));

                mRefreshDrawable.setPointerPosition(x, y);
                mRefreshDrawable.setPercent(mCurrentDragPercent, true);
                setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mActivePointerId = motionEvent.getPointerId(MotionEventCompat.getActionIndex(motionEvent));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(motionEvent);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER_ID) {
                    return false;
                }
                final float y = motionEvent.getY(motionEvent.findPointerIndex(mActivePointerId));
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mIsRefreshing = false;
                    animateOffsetToStartPosition();
                }
                mActivePointerId = INVALID_POINTER_ID;
                return false;
            }
        }

        return true;
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mOnChildScrollUpCallback != null) {
            return mOnChildScrollUpCallback.canChildScrollUp(this, mTarget);
        }
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 ||
                        absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * @return max drag distance in pixels
     */
    public int getTotalDragDistance() {
        return mTotalDragDistance;
    }

    /**
     * @return Whether the {@link FireworkyPullToRefreshLayout} is actively showing refresh
     *         progress.
     */
    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     * <b>Note!</b> You must wrap calling this method in {@link FireworkyPullToRefreshLayout#post(Runnable)}
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        setRefreshing(refreshing, false);
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mIsRefreshing != refreshing) {

            mIsRefreshing = refreshing;
            if (mIsRefreshing) {
                mRefreshDrawable.setPercent(1f, true);
                mFrom = mCurrentOffsetTop;
                mFromDragPercent = mCurrentDragPercent;

                mAnimateToCorrectPosition.reset();
                mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
                mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);

                mRefreshView.clearAnimation();
                mRefreshView.startAnimation(mAnimateToCorrectPosition);

                if (mIsRefreshing) {
                    mRefreshDrawable.start();
                    if (notify && null != mOnRefreshListener) {
                        mOnRefreshListener.onRefresh();
                    }
                } else {
                    mRefreshDrawable.stop();
                    animateOffsetToStartPosition();
                }

                mCurrentOffsetTop = mTarget.getTop();
                mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom);
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    @Nullable
    private View getTargetView() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
                    mTargetPaddingBottom = mTarget.getPaddingBottom();
                    mTargetPaddingLeft = mTarget.getPaddingLeft();
                    mTargetPaddingRight = mTarget.getPaddingRight();
                    mTargetPaddingTop = mTarget.getPaddingTop();
                }
            }
        }
        return mTarget;
    }

    private void animateOffsetToStartPosition() {
        mFromDragPercent = mCurrentDragPercent;
        mFrom = mCurrentOffsetTop;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
        int offset = targetTop - mTarget.getTop();

        mCurrentDragPercent = targetPercent;
        mRefreshDrawable.setPercent(mCurrentDragPercent, true);
        mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom + targetTop);
        setTargetOffsetTop(offset, false);
    }

    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mRefreshDrawable.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        if (requiresUpdate && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            invalidate();
        }
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe gesture.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * Set a callback to override {@link FireworkyPullToRefreshLayout#canChildScrollUp()} method. Non-null
     * callback will return the value provided by the callback and ignore all internal logic.
     * @param callback Callback that should be called when canChildScrollUp() is called.
     */
    public void setOnChildScrollUpCallback(@Nullable FireworkyPullToRefreshLayout.OnChildScrollUpCallback callback) {
        mOnChildScrollUpCallback = callback;
    }

    /**
     * For changing {@link FireworkyPullToRefreshLayout} just call this method and set necessary parameters
     * @return Current {@link FireworkyPullToRefreshLayout} configuration
     */
    public Configuration getConfig() {
        return mConfig;
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        /**
         * Called when a swipe gesture triggers a refresh.
         */
        void onRefresh();
    }

    /**
     * Classes that wish to override {@link FireworkyPullToRefreshLayout#canChildScrollUp()} method
     * behavior should implement this interface.
     */
    public interface OnChildScrollUpCallback {
        /**
         * Callback that will be called when {@link FireworkyPullToRefreshLayout#canChildScrollUp()} method
         * is called to allow the implementer to override its behavior.
         *
         * @param parent {@link FireworkyPullToRefreshLayout} that this callback is overriding.
         * @param child The child view of {@link FireworkyPullToRefreshLayout}.
         *
         * @return Whether it is possible for the child view of parent layout to scroll up.
         */
        boolean canChildScrollUp(@NonNull FireworkyPullToRefreshLayout parent, @Nullable View child);
    }
}