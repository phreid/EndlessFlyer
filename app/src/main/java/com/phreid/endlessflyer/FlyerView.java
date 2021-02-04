package com.phreid.endlessflyer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.phreid.endlessflyer.sprite.AbstractSprite;
import com.phreid.endlessflyer.sprite.AnimatedSprite;
import com.phreid.endlessflyer.sprite.CircleSprite;
import com.phreid.endlessflyer.sprite.Explosion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlyerView extends SurfaceView implements Runnable {
    private boolean initialized = false;
    private CircleSprite player;
    private int frameCount = 0;
    private List<CircleSprite> enemyList = new ArrayList<>();
    private List<AnimatedSprite> starList = new ArrayList<>();
    private List<AbstractSprite> toRemove = new ArrayList<>();
    private Random random = new Random();
    private List<Bitmap> starImageList = new ArrayList<>();
    private int score = 0;
    private Paint textPaint = new Paint();
    private Paint floorPaint = new Paint();
    private RectF floorRect;
    private RectF ceilingRect;
    private float circleSize;
    private float starSize;
    private Context context;
    private float defaultOnTouchAccel;
    private float defaultAccelY;
    private int maxEnemyVelocity;
    private int minEnemyVelocity;
    private int maxPlayerVelocity;
    private boolean gameOver = false;
    private Explosion explosion;

    private SurfaceHolder holder = getHolder();
    private Thread gameThread;
    private boolean isRunning;

    private static final int ENEMY_CREATE_INTERVAL = 20;
    private static final int STAR_CREATE_INTERVAL = 240;
    private static final int CEILING_MARGIN = 75;
    private static final int FLOOR_MARGIN = 200;
    private static final int INCREASE_NUM_CIRCLE_INTERVAL = 720;

    public FlyerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void drawSprites(Canvas canvas) {
        if (! gameOver) {
            player.draw(canvas);

            for (CircleSprite sprite : enemyList) {
                sprite.draw(canvas);
            }

            for (AnimatedSprite star : starList) {
                star.draw(canvas);
            }
        } else {
            explosion.update(canvas);
        }

        canvas.drawText("Score: " + score, getWidth() / 2f - 50, 40 + CEILING_MARGIN,
                textPaint);
        canvas.drawRect(floorRect, floorPaint);
        canvas.drawRect(ceilingRect, floorPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (! initialized) {
            init();
        }
    }

    void init() {
        initialized = true;

        circleSize = getWidth() * 0.05f;
        starSize = getWidth() * 0.1f;
        minEnemyVelocity = getWidth() / 150;
        maxEnemyVelocity = getWidth() / 75;
        defaultAccelY = getHeight() * 0.0025f;
        defaultOnTouchAccel = getHeight() * -0.0015f;
        maxPlayerVelocity = getHeight() / 100;

        starImageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.bluestar0));
        starImageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.bluestar1));
        starImageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.bluestar2));
        starImageList.add(BitmapFactory.decodeResource(getResources(), R.drawable.bluestar3));

        player = new CircleSprite(
                getWidth() * 0.1f,
                (getHeight() - (FLOOR_MARGIN + CEILING_MARGIN)) / 2f,
                circleSize
        );
        player.setMaxVelocityY(maxPlayerVelocity);
        Paint paint = new Paint();
        paint.setARGB(255, 0, 255, 0);
        player.setPaint(paint);

        textPaint.setARGB(255, 0, 0, 0);
        textPaint.setTextSize(40);
        textPaint.setAntiAlias(true);

        ceilingRect = new RectF(0, 0, getWidth(), CEILING_MARGIN);
        floorRect = new RectF(0, getHeight() - FLOOR_MARGIN,
                getWidth(), getHeight());
        floorPaint.setARGB(255,84, 84, 84);
    }

    void onTick(Canvas canvas) {
        frameCount++;

        if (! gameOver) {
            if (frameCount == 30) {
                player.setAccelerationY(defaultAccelY);
            }

            if (frameCount % ENEMY_CREATE_INTERVAL == 0) {
                addRandomEnemies(1);
            }

            if (frameCount % STAR_CREATE_INTERVAL == 0) {
                addRandomStar();
            }

            doPlayerBehavior();
            doEnemyBehavior();
            doStarBehavior();

            for (AbstractSprite sprite : toRemove) {
                enemyList.remove(sprite);
                starList.remove(sprite);
            }
            toRemove.clear();
        } else {
            if (! explosion.isAlive()) {
                endGame();
            }
        }


        drawSprites(canvas);
    }

    private void doPlayerBehavior() {
        player.move();

        if ((player.getY() + circleSize > getHeight() - FLOOR_MARGIN ||
                player.getY() - circleSize < CEILING_MARGIN) && (! gameOver)) {
            setGameOver();
        }
    }

    private void doStarBehavior() {
        for (AnimatedSprite star : starList) {
            if (star.collidesWith(player)) {
                toRemove.add(star);
                score++;
            }

            if (star.getRightBound() < 0) {
                toRemove.add(star);
            }

            if (star.getBottomBound() > getHeight() - FLOOR_MARGIN ||
                    star.getTopBound() < CEILING_MARGIN) {
                star.setVelocityY(-star.getVelocityY());
                star.setAccelerationY(-star.getAccelerationY());
            }

            star.move();
            star.updateBitmap(frameCount);
        }
    }

    private void addRandomStar() {
        int dx = -1 * (minEnemyVelocity + random.nextInt(maxEnemyVelocity));
        int dy = random.nextInt(2) == 1 ? 2 : -2;
        int xPos = getWidth();
        int yPos = random
                .nextInt(getHeight() - FLOOR_MARGIN - ((int) starSize * 2))
                + CEILING_MARGIN;

        AnimatedSprite star = new AnimatedSprite(starImageList, xPos, yPos, starSize);
        star.setVelocityX(dx);
        star.setVelocityY(dy);

        star.setCollisionDebug();

        starList.add(star);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            player.setAccelerationY(defaultOnTouchAccel);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setAccelerationY(defaultAccelY);
        }

        return super.onTouchEvent(event);
    }

    private void addRandomEnemies(int numSprites) {
        for (int i = 0; i < numSprites; i++) {
            int dx = -1 * (random.nextInt(maxEnemyVelocity) + minEnemyVelocity);
            float xPos = getWidth();
            float yPos = random.nextInt(getHeight() -
                        FLOOR_MARGIN - CEILING_MARGIN - ((int) circleSize * 2))
                    + CEILING_MARGIN + circleSize;
            Paint paint = new Paint();
            paint.setARGB(255, 255, 0, 0);

            CircleSprite sprite = new CircleSprite(xPos, yPos, circleSize);
            sprite.setPaint(paint);
            sprite.setVelocityX(dx);
            enemyList.add(sprite);
        }
    }

    private void doEnemyBehavior() {
        for (CircleSprite sprite : enemyList) {
            if (sprite.collidesWith(player)) {
                setGameOver();
            }

            if (sprite.getX() + circleSize < 0) {
                toRemove.add(sprite);
            }

            sprite.move();
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void setGameOver() {
        gameOver = true;
        explosion = new Explosion(player.getX(), player.getY(), circleSize, 5,
                3, player.getPaint(), 1000, 50, 200);
    }

    private void endGame() {
        GameActivity game = (GameActivity) context;
        game.onGameOver();
    }

    public int getScore() {
        return score;
    }

    @Override
    public void run() {
        Canvas canvas;

        while (isRunning) {
            if (holder.getSurface().isValid()) {
                canvas = holder.lockCanvas();
                long startTime = System.currentTimeMillis();
                canvas.drawColor(Color.WHITE);

                onTick(canvas);

                holder.unlockCanvasAndPost(canvas);
                long frameTime = System.currentTimeMillis() - startTime;
//                Log.d("ZZZ", String.valueOf(frameTime));
            }
        }
    }

    public void pause() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
