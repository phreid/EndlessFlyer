package com.phreid.endlessflyer.sprite;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Explosion {
    private List<Particle> particles;

    public Explosion(float x, float y, float circleRadius, float particleRadius, float particleSpeed,
                     Paint paint, int numParticles, int minLifetime, int maxLifetime) {
        particles = new ArrayList<>();
        for (int i = 0; i < numParticles; i++) {
            Random random = new Random();
            int lifetime = random.nextInt(maxLifetime - minLifetime) + minLifetime;

            //generate random particle in the circle of radius circleRadius around (x,y)
            float radius = circleRadius * (float) Math.sqrt(random.nextFloat());
            float theta = random.nextFloat() * 2 * (float) Math.PI;
            float centerX = x + radius * (float) Math.cos(theta);
            float centerY = y + radius * (float) Math.sin(theta);

            Particle particle = new Particle(centerX, centerY, particleRadius, lifetime);
            float vX = (float) Math.cos(theta) * particleSpeed * random.nextFloat();
            float vY = (float) Math.sin(theta) * particleSpeed * random.nextFloat();
            particle.setVelocityX(vX);
            particle.setVelocityY(vY);
            particle.setPaint(paint);
            particles.add(particle);
        }
    }

    public void move(long deltaTime) {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.move(deltaTime);
            }
        }
    }

    public void draw(Canvas canvas) {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.draw(canvas);
            }
        }
    }

    public boolean isAlive() {
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                return true;
            }
        }

        return false;
    }
}
