package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BaseCircle {
    private float radius;
    private float x;
    private float y;
    private Paint paint;

    public BaseCircle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public boolean collidesWith(BaseCircle other) {
        float deltaX = x - other.x;
        float deltaY = y - other.y;
        float dist = (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

        return dist <= radius + other.radius;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getRadius() {
        return radius;
    }

    public Paint getPaint() {
        return paint;
    }
}
