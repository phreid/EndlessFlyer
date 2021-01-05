package com.phreid.endlessflyer.sprite;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.List;

public class AnimatedSprite extends AbstractSprite {
    private RectF positionRect;
    private BaseCircle collisionCircle;
    private final List<Bitmap> bitmapList;
    private int currentBitmap;
    private boolean debug = false;
    private Paint debugPaint;

    private final int updateBitmapInterval = 4;

    public AnimatedSprite(List<Bitmap> bitmapList, float x, float y, float length) {
        this.bitmapList = bitmapList;
        this.positionRect = new RectF(x, y, x + length, y + length);
        this.collisionCircle = new BaseCircle(
                x + (length / 2),
                y + (length / 2),
                length / 2);
        currentBitmap = 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public void draw(Canvas canvas) {
        if (debug) {
            if (debugPaint == null) {
                debugPaint = new Paint();
                debugPaint.setARGB(100, 0, 255, 0);
                collisionCircle.setPaint(debugPaint);
            }

            collisionCircle.draw(canvas);
        }

        canvas.drawBitmap(bitmapList.get(currentBitmap), null, positionRect, null);
    }

    public void updateBitmap(int frame) {
        if (frame % updateBitmapInterval == 0) {
            if (currentBitmap == bitmapList.size() - 1) {
                currentBitmap = 0;
            } else {
                currentBitmap++;
            }
        }
    }

    @Override
    public void move() {
        positionRect.offset(dx, dy);
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

    public void setCollisionDebug() {
        debug = true;
    }

    public float getRightBound() {
        return positionRect.right;
    }

    public float getLeftBound() {
        return positionRect.left;
    }

    public float getBottomBound() {
        return positionRect.bottom;
    }

    public float getTopBound() {
        return positionRect.top;
    }
}
