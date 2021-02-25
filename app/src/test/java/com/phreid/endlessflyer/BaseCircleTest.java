package com.phreid.endlessflyer;

import com.phreid.endlessflyer.sprite.BaseCircle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseCircleTest {

    @Test
    public void testMove() {
        BaseCircle circle = new BaseCircle(0, 0, 1);
        circle.move(1, 2);
        assertEquals(1f, circle.getX(), 0.01);
        assertEquals(2f, circle.getY(), 0.01);
    }

    @Test
    public void testCollidesWith() {
        BaseCircle circle = new BaseCircle(0, 0, 1);
        BaseCircle otherCollides = new BaseCircle(0, 1, 1);
        BaseCircle otherNotCollides = new BaseCircle(2, 2, 1);

        assertTrue(circle.collidesWith(otherCollides));
        assertFalse(circle.collidesWith(otherNotCollides));
    }
}
