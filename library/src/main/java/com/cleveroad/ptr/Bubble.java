package com.cleveroad.ptr;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

import java.io.Serializable;

class Bubble implements Serializable {
    private Bubble mInitialState;
    private Point mPosition;
    private Point mDPosition;
    private int mColor;
    private float mRadius, mDRadius;
    private float mAlpha = 200, mDAlpha = 0;
    private double mRotationAngle, mDRotationAngle = Double.MIN_VALUE, mRotationMulCoefficient = 1;

    private Bubble() {
    }

    Bubble(Bubble src) {
        copyTo(src, this);
        copyTo(this, mInitialState = new Bubble());
    }

    private Bubble(Builder builder) {
        setPosition(builder.position);
        setDPosition(builder.dPosition);
        setColor(builder.color);
        setRadius(builder.radius);
        setDRadius(builder.dRadius);
        setAlpha(builder.alpha);
        setDAlpha(builder.dAlpha);
        setRotationAngle(builder.rotationAngle);
        setDRotationAngle(builder.dRotationAngle);
        setRotationMulCoefficient(builder.rotationMulCoefficient);

        copyTo(this, mInitialState = new Bubble());
    }

    private static void copyTo(Bubble src, Bubble dest) {
        dest.mPosition = new Point(src.mPosition);
        dest.mDPosition = new Point(src.mDPosition);
        dest.mColor = src.mColor;
        dest.mRadius = src.mRadius;
        dest.mDRadius = src.mDRadius;
        dest.mAlpha = src.mAlpha;
        dest.mDAlpha = src.mDAlpha;
        dest.mRotationAngle = src.mRotationAngle;
        dest.mDRotationAngle = src.mDRotationAngle;
        dest.mRotationMulCoefficient = src.mRotationMulCoefficient;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    //--------------------------------------- bubble mPosition --------------------------------------
    public Point getPosition() {
        return mPosition;
    }

    public void setPosition(Point position) {
        this.mPosition = position;
    }

    public float incrementXAndGet() {
        mPosition.x += mDPosition.x;
        return getXPos();
    }

    public float incrementYAndGet() {
        mPosition.y += mDPosition.y;
        return getYPos();
    }

    public float getXPos() {
        if (mRotationAngle != Double.MIN_VALUE) {
            float deltaX = mPosition.x - mInitialState.mPosition.x;
            float deltaY = mPosition.y - mInitialState.mPosition.y;
            double x = deltaX * Math.cos(mRotationAngle) - deltaY * Math.sin(mRotationAngle);
            return (float) (mPosition.x + x * mRotationMulCoefficient);
        } else return mPosition.x;
    }

    public void setXPos(float xPos) {
        this.mPosition.x = xPos;
    }

    public float getYPos() {
        if (mRotationAngle != Double.MIN_VALUE) {
            float deltaX = mPosition.x - mInitialState.mPosition.x;
            float deltaY = mPosition.y - mInitialState.mPosition.y;
            double y = deltaY * Math.cos(mRotationAngle) + deltaX * Math.sin(mRotationAngle);
            return (float) (mPosition.y + y * mRotationMulCoefficient);
        } else return mPosition.y;
    }

    public void setYPos(float yPos) {
        this.mPosition.y = yPos;
    }

    public float getDx() {
        return mDPosition.x;
    }

    public void setDx(float dx) {
        this.mDPosition.x = dx;
    }

    public float getDy() {
        return mDPosition.y;
    }

    public void setDy(float dy) {
        this.mDPosition.y = dy;
    }

    public Point getDPosition() {
        return mDPosition;
    }

    public void setDPosition(Point DPosition) {
        this.mDPosition = DPosition;
    }

    //------------------------------------- rotation angle -----------------------------------------
    public void incrementRotationAngle() {
        this.mRotationAngle += mDRotationAngle;
    }

    public double getRotationAngle() {
        return mRotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.mRotationAngle = rotationAngle;
    }

    public double getDRotationAngle() {
        return mDRotationAngle;
    }

    public void setDRotationAngle(double DRotationAngle) {
        this.mDRotationAngle = DRotationAngle;
    }

    public double getRotationMulCoefficient() {
        return mRotationMulCoefficient;
    }

    public void setRotationMulCoefficient(double rotationMulCoefficient) {
        this.mRotationMulCoefficient = rotationMulCoefficient;
    }

    //----------------------------------------- visibility -----------------------------------------
    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    @IntRange(from = 0, to = 255)
    public int getAlpha() {
        mAlpha = mAlpha > 255 ? 255 : mAlpha;
        return (int) (mAlpha < 0 ? (mAlpha = 0) : mAlpha);
    }

    public void setAlpha(@FloatRange(from = 0, to = 255) float alpha) {
        this.mAlpha = alpha;
    }

    public float getDAlpha() {
        return mDAlpha;
    }

    public void setDAlpha(@FloatRange(from = 0, to = 255) float DAlpha) {
        this.mDAlpha = DAlpha;
    }

    public int incrementAlphaAndGet() {
        mAlpha += mDAlpha;
        return getAlpha();
    }

    public boolean isInvisible() {
        return mAlpha <= 0 || mRadius <= 0;
    }

    //------------------------------------------- mRadius -------------------------------------------
    public float getRadius() {
        return mRadius < 0 ? (mRadius = 0f) : mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius < 0 ? 0 : radius;
    }

    public float incrementRadiusAndGet() {
        this.mRadius += this.mDRadius;
        return getRadius();
    }

    public float getDRadius() {
        return mDRadius;
    }

    public void setDRadius(float DRadius) {
        this.mDRadius = DRadius;
    }

    //======================================= other methods ========================================
    public void reset() {
        copyTo(mInitialState, this);
    }

    @FloatRange(from = 0, to = 1)
    public float getPercent() {
        if (mAlpha <= 0f && mDAlpha <= 0f) {
            return 1.f;
        }
        if (mRadius <= 0f && mDRadius <= 0f) {
            return 1.f;
        }

        float percent;

        if (mDAlpha >= 0f) {
            percent = mAlpha / 255.f;
        } else {
            percent = 1.f - mAlpha / mInitialState.mAlpha;
        }

        //noinspection StatementWithEmptyBody
        if (mDRadius >= 0f) {
            //incalculable, do nothing
        } else {
            percent = Math.max(1.f - mRadius / mInitialState.mRadius, percent);
        }

        return percent;
    }

    public Bubble getInitialState() {
        return mInitialState;
    }

    public void updateInitialState() {
        copyTo(this, this.mInitialState = new Bubble());
    }

    //==============================================================================================
    public static class Point {
        float x;
        float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        Point(Point src) {
            this.x = src.x;
            this.y = src.y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }

    public static final class Builder {
        private Point position;
        private Point dPosition;
        private int color;
        private float radius;
        private float dRadius;
        private float alpha;
        private float dAlpha;
        private double rotationAngle;
        private double dRotationAngle = Double.MIN_VALUE;
        private double rotationMulCoefficient = 1;

        private Builder() {
        }

        public Builder position(Point position) {
            this.position = new Point(position);
            return this;
        }

        public Builder position(float positionX, float positionY) {
            this.position = new Point(positionX, positionY);
            return this;
        }

        public Builder dPosition(Point dPosition) {
            this.dPosition = new Point(dPosition);
            return this;
        }

        public Builder dPosition(float positionDx, float positionDy) {
            this.dPosition = new Point(positionDx, positionDy);
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder dRadius(float dRadius) {
            this.dRadius = dRadius;
            return this;
        }

        public Builder alpha(@FloatRange(from = 0, to = 255) float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder dAlpha(@FloatRange(from = -255, to = 255) float dAlpha) {
            this.dAlpha = dAlpha;
            return this;
        }

        public Builder rotationAngle(double rotationAngle) {
            this.rotationAngle = rotationAngle;
            return this;
        }

        public Builder dRotationAngle(double dRotationAngle) {
            this.dRotationAngle = dRotationAngle;
            return this;
        }

        public Builder rotationMulCoefficient(double rotationMulCoefficient) {
            this.rotationMulCoefficient = rotationMulCoefficient;
            return this;
        }

        public Bubble build() {
            if (position == null) {
                position = new Point(0, 0);
            }

            if (dPosition == null) {
                dPosition = new Point(0, 0);
            }
            return new Bubble(this);
        }
    }
}