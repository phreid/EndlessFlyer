package com.phreid.endlessflyer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;

public class AnimationLoop {
    private FlyerView view;
    private SurfaceHolder holder;
    private int fps;
    private Thread thread;
    private Handler handler;
    private volatile boolean isRunning = false;

    public AnimationLoop(FlyerView view, int fps) {
        if(view != null && fps > 0) {
            this.view = view;
            this.fps = fps;
            holder = view.getHolder();
            handler = new Handler(Looper.getMainLooper());
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean isRunning() {
        return thread != null;
    }

    public void start() {
        thread = new Thread(new MainRunner());
        thread.start();
    }

    public void stop() {
        if (isRunning()) {
            isRunning = false;

            try {
                thread.join();
            } catch (InterruptedException e) {
                ;
            }

            thread = null;
        }
    }

    private class Updater implements Runnable {

        @Override
        public void run() {
            if (! view.isInitialized()) {
                view.init();
            } else {
                Canvas canvas;
                if (holder.getSurface().isValid()) {
                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.WHITE);

                    view.onTick(canvas);

                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private class MainRunner implements Runnable {

        @Override
        public void run() {
            isRunning = true;

            while (isRunning) {
                try {
                    Thread.sleep((1000 / fps));
                } catch (InterruptedException e) {
                    isRunning = false;
                }

                if(! isRunning) {
                    break;
                }

                handler.post(new Updater());
            }
        }
    }
}
