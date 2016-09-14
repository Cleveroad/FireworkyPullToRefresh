package com.cleveroad.pulltorefresh.firework;

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

    static Builder newBuilder() {
        return new Builder();
    }


    //--------------------------------------- bubble mPosition --------------------------------------
    Point getPosition() {
        return mPosition;
    }

    void setPosition(Point position) {
        this.mPosition = position;
    }

    float incrementXAndGet() {
        mPosition.x += mDPosition.x;
        return getXPos();
    }

    float incrementYAndGet() {
        mPosition.y += mDPosition.y;
        return getYPos();
    }

    float getXPos() {
        if (mDRotationAngle != Double.MIN_VALUE) {
            float deltaX = mPosition.x - mInitialState.mPosition.x;
            float deltaY = mPosition.y - mInitialState.mPosition.y;
            double x = deltaX * Math.cos(mRotationAngle) - deltaY * Math.sin(mRotationAngle);
            return (float) (mPosition.x + x * mRotationMulCoefficient);
        } else return mPosition.x;
    }

    void setXPos(float xPos) {
        this.mPosition.x = xPos;
    }

    float getYPos() {
        if (mDRotationAngle != Double.MIN_VALUE) {
            float deltaX = mPosition.x - mInitialState.mPosition.x;
            float deltaY = mPosition.y - mInitialState.mPosition.y;
            double y = deltaY * Math.cos(mRotationAngle) + deltaX * Math.sin(mRotationAngle);
            return (float) (mPosition.y + y * mRotationMulCoefficient);
        } else return mPosition.y;
    }

    void setYPos(float yPos) {
        this.mPosition.y = yPos;
    }

    float getDx() {
        return mDPosition.x;
    }

    void setDx(float dx) {
        this.mDPosition.x = dx;
    }

    float getDy() {
        return mDPosition.y;
    }

    void setDy(float dy) {
        this.mDPosition.y = dy;
    }

    Point getDPosition() {
        return mDPosition;
    }

    void setDPosition(Point DPosition) {
        this.mDPosition = DPosition;
    }

    //------------------------------------- rotation angle -----------------------------------------
    void incrementRotationAngle() {
        this.mRotationAngle += mDRotationAngle;
    }

    double getRotationAngle() {
        return mRotationAngle;
    }

    void setRotationAngle(double rotationAngle) {
        this.mRotationAngle = rotationAngle;
    }

    double getDRotationAngle() {
        return mDRotationAngle;
    }

    void setDRotationAngle(double DRotationAngle) {
        this.mDRotationAngle = DRotationAngle;
    }

    double getRotationMulCoefficient() {
        return mRotationMulCoefficient;
    }

    void setRotationMulCoefficient(double rotationMulCoefficient) {
        this.mRotationMulCoefficient = rotationMulCoefficient;
    }

    //----------------------------------------- visibility -----------------------------------------
    int getColor() {
        return mColor;
    }

    void setColor(int color) {
        this.mColor = color;
    }

    @IntRange(from = 0, to = 255)
    int getAlpha() {
        mAlpha = mAlpha > 255 ? 255 : mAlpha;
        return (int) (mAlpha < 0 ? (mAlpha = 0) : mAlpha);
    }

    void setAlpha(@FloatRange(from = 0, to = 255) float alpha) {
        this.mAlpha = alpha;
    }

    float getDAlpha() {
        return mDAlpha;
    }

    void setDAlpha(@FloatRange(from = 0, to = 255) float DAlpha) {
        this.mDAlpha = DAlpha;
    }

    int incrementAlphaAndGet() {
        mAlpha += mDAlpha;
        return getAlpha();
    }

    boolean isInvisible() {
        return mAlpha <= 0 || mRadius <= 0;
    }

    //------------------------------------------- mRadius -------------------------------------------
    float getRadius() {
        return mRadius < 0 ? (mRadius = 0f) : mRadius;
    }

    void setRadius(float radius) {
        this.mRadius = radius < 0 ? 0 : radius;
    }

    float incrementRadiusAndGet() {
        this.mRadius += this.mDRadius;
        return getRadius();
    }

    float getDRadius() {
        return mDRadius;
    }

    void setDRadius(float DRadius) {
        this.mDRadius = DRadius;
    }

    //======================================= other methods ========================================
    void reset() {
        copyTo(mInitialState, this);
    }

    @FloatRange(from = 0, to = 1)
    float getPercent() {
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

    Bubble getInitialState() {
        return mInitialState;
    }

    void updateInitialState() {
        copyTo(this, this.mInitialState = new Bubble());
    }

    //==============================================================================================
    static class Point implements Serializable {
        float x;
        float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        Point(Point src) {
            this.x = src.x;
            this.y = src.y;
        }

        float getX() {
            return x;
        }

        float getY() {
            return y;
        }
    }

    static final class Builder {
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

        Builder position(Point position) {
            this.position = new Point(position);
            return this;
        }

        Builder position(float positionX, float positionY) {
            this.position = new Point(positionX, positionY);
            return this;
        }

        Builder dPosition(Point dPosition) {
            this.dPosition = new Point(dPosition);
            return this;
        }

        Builder dPosition(float positionDx, float positionDy) {
            this.dPosition = new Point(positionDx, positionDy);
            return this;
        }

        Builder color(int color) {
            this.color = color;
            return this;
        }

        Builder radius(float radius) {
            this.radius = radius;
            return this;
        }

        Builder dRadius(float dRadius) {
            this.dRadius = dRadius;
            return this;
        }

        Builder alpha(@FloatRange(from = 0, to = 255) float alpha) {
            this.alpha = alpha;
            return this;
        }

        Builder dAlpha(@FloatRange(from = -255, to = 255) float dAlpha) {
            this.dAlpha = dAlpha;
            return this;
        }

        Builder rotationAngle(double rotationAngle) {
            this.rotationAngle = rotationAngle;
            return this;
        }

        Builder dRotationAngle(double dRotationAngle) {
            this.dRotationAngle = dRotationAngle;
            return this;
        }

        Builder rotationMulCoefficient(double rotationMulCoefficient) {
            this.rotationMulCoefficient = rotationMulCoefficient;
            return this;
        }

        Bubble build() {
            if (position == null) {
                position = new Point(0, 0);
            } else {
                position = new Point(position);
            }

            if (dPosition == null) {
                dPosition = new Point(0, 0);
            } else {
                dPosition = new Point(dPosition);
            }
            return new Bubble(this);
        }
    }
}