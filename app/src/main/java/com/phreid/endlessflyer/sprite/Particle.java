package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;

public class Particle extends AbstractSprite {
    private int lifetime;

    public Particle(float x, float y, float radius, int lifetime) {
        super(x, y, radius);
        this.lifetime = lifetime;
    }

    public boolean isAlive() {
        return lifetime > 0;
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
        lifetime--;
    }

    @Override
    public boolean collidesWith(AbstractSprite other) {
        return false;
    }
}
