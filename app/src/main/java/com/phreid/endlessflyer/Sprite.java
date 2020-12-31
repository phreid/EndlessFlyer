package com.phreid.endlessflyer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private float x;
    private float y;
    private float width;
    private float height;
    private float dx = 0;
    private float dy = 0;
    private float maxVelocityX = -1;
    private float maxVelocityY = -1;
    private float accelerationX = 0;
    private float accelerationY = 0;
    private RectF positionRect;
    private RectF collisionRect;
    private Bitmap bitmap;
    private List<Bitmap> bitmapList;
    private int currentBitmap;
    private Canvas canvas;
    private FlyerView view;
    private Paint paint = null;
    private Paint debugPaint = null;
    private boolean debug = false;
    private int updateBitmapInterval = 4;

    public Sprite(FlyerView view, Bitmap bitmap, float x, float y, float width, float height) {
        this.view = view;
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.positionRect = new RectF(x, y, x + width, y + height);
        this.collisionRect = new RectF(x, y, x + width, y + height);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setVelocityX(float dx) {
        this.dx = dx;
    }

    public void setVelocityY(float dy) {
        this.dy = dy;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void draw() {
        if (debug) {
            canvas.drawRect(collisionRect, debugPaint);
        }

        if (bitmapList != null) {
            updateCurrentBitmap();
        }

        canvas.drawBitmap(bitmap, null, positionRect, paint);
    }

    private void updateCurrentBitmap() {
        if (view.getFrameCount() % updateBitmapInterval == 0) {
            bitmap = bitmapList.get(currentBitmap);
        }

        if (currentBitmap == bitmapList.size() - 1) {
            currentBitmap = 0;
        } else {
            currentBitmap++;
        }
    }

    public void move() {
        positionRect.offset(dx, dy);
        collisionRect.offset(dx, dy);

        if (maxVelocityX > 0 && Math.abs(dx + accelerationX) < maxVelocityX) {
            dx += accelerationX;
        }

        if (maxVelocityY > 0 && Math.abs(dy + accelerationY) < maxVelocityY) {
            dy += accelerationY;
        }
    }

    public boolean collidesWith(Sprite other) {
        return collisionRect.intersect(other.getCollisionRect());
    }

    public void setCollisionMargin(float pixels) {
        collisionRect.left = positionRect.left + pixels;
        collisionRect.top =  positionRect.top + pixels;
        collisionRect.right =  positionRect.right - pixels;
        collisionRect.bottom =  positionRect.bottom - pixels;
    }

    public void setCollisionDebug() {
        debug = true;
        Paint paint = new Paint();
        paint.setARGB(100,0,255,0);
        debugPaint = paint;
    }

    public void addToBitmapList(Bitmap bitmap) {
        if (bitmapList == null) {
            bitmapList = new ArrayList<>();
        }

        bitmapList.add(bitmap);
    }

    private RectF getCollisionRect() {
        return collisionRect;
    }

    public void setMaxVelocityX(float maxVelocityX) {
        this.maxVelocityX = maxVelocityX;
    }

    public void setMaxVelocityY(float maxVelocityY) {
        this.maxVelocityY = maxVelocityY;
    }

    public float getVelocityX() {
        return dx;
    }

    public float getVelocityY() {
        return dy;
    }

    public float getRightBound() {
        return collisionRect.right;
    }

    public float getLeftBound() {
        return collisionRect.left;
    }

    public float getBottomBound() {
        return collisionRect.bottom;
    }

    public float getTopBound() {
        return collisionRect.top;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

}
