package com.phreid.endlessflyer;

import com.phreid.endlessflyer.sprite.AbstractSprite;
import com.phreid.endlessflyer.sprite.CircleSprite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractSpriteTest {
    private AbstractSprite sprite;

    @Before
    public void runBefore() {
        sprite = new CircleSprite(0, 0, 1);
    }

    @Test
    public void testMove() {
        long delta = (long) 1e9; // 1 second in nano-seconds
        sprite.setVelocityX(1f); // 1 unit per second
        sprite.setVelocityY(2f); // 2 units per second

        sprite.move(delta);
        assertEquals(1f, sprite.getX(), 0.01);
        assertEquals(2f, sprite.getY(), 0.01);
    }

    @Test
    public void testCollidesWith() {
        AbstractSprite otherCollides = new CircleSprite(0, 1, 1);
        AbstractSprite otherNotCollides = new CircleSprite(2, 2, 1);

        assertTrue(sprite.collidesWith(otherCollides));
        assertFalse(sprite.collidesWith(otherNotCollides));
    }
}
