package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;

public class CircleSprite extends AbstractSprite {

    public CircleSprite(float x, float y, float radius) {
        collisionCircle = new BaseCircle(x, y, radius);
    }

    @Override
    public void draw(Canvas canvas) {
        collisionCircle.draw(canvas);
    }

    @Override
    public void move() {
        collisionCircle.move(dx, dy);

        if (maxVelocityX > 0 && Math.abs(dx + accelerationX) < maxVelocityX) {
            dx += accelerationX;
        }

        if (maxVelocityY > 0 && Math.abs(dy + accelerationY) < maxVelocityY) {
            dy += accelerationY;
        }
    }

    @Override
    public boolean collidesWith(AbstractSprite other) {
        return collisionCircle.collidesWith(other.collisionCircle);
    }

    @Override
    public float getY() {
        return collisionCircle.getY();
    }

    @Override
    public float getX() {
        return collisionCircle.getX();
    }
}
