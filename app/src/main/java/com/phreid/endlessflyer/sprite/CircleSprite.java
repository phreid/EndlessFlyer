package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;
import android.util.Log;

public class CircleSprite extends AbstractSprite {

    public CircleSprite(float x, float y, float radius) {
        super(x, y, radius);
    }

    @Override
    public void draw(Canvas canvas) {
        collisionCircle.draw(canvas);
    }

    @Override
    public void move(long deltaTime) {
        float moveX = dx * deltaTime / 1e9f;
        float moveY = dy * deltaTime / 1e9f;

        collisionCircle.move(moveX, moveY);

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
}
