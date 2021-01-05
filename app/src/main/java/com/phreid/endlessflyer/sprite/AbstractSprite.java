package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class AbstractSprite {
    protected BaseCircle collisionCircle;
    protected float dx = 0;
    protected float dy = 0;
    protected float maxVelocityX = -1;
    protected float maxVelocityY = -1;
    protected float accelerationX = 0;
    protected float accelerationY = 0;

    public abstract void draw(Canvas canvas);

    public abstract void move();

    public abstract boolean collidesWith(AbstractSprite other);

    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    public void setMaxVelocityX(float maxVelocityX) {
        this.maxVelocityX = maxVelocityX;
    }

    public void setMaxVelocityY(float maxVelocityY) {
        this.maxVelocityY = maxVelocityY;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setPaint(Paint paint) {
        collisionCircle.setPaint(paint);
    }

    public abstract float getY();

    public abstract float getX();

    public float getVelocityY() {
        return dy;
    }

    public float getAccelerationY() {
        return accelerationY;
    }
}
