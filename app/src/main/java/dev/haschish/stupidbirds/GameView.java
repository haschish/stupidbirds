package dev.haschish.stupidbirds;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    private int viewWeight;
    private int viewHeight;
    private int points;
    private final int timerInterval = 30;

    private Sprite playerBird;
    private Sprite enemyBird;

    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {
        }
    };

    public GameView(Context context) {
        super(context);


        createPlayer();
        createEnemy();
        Timer t = new Timer();
        t.start();
    }

    private void createPlayer () {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        int w = b.getWidth() / 5;
        int h = b.getHeight() / 3;

        Rect firstFrame = new Rect(0, 0, w, h);

        playerBird = new Sprite(10 ,0, 0, 100, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 2 && j == 3) {
                    continue;
                }

                int left = j * w;
                int top = i * h;
                int right = j * w + w;
                int bottom = i * h + h;
                playerBird.addFrame(new Rect(left, top, right, bottom));
            }
        }
    };

    private void createEnemy () {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        int w = b.getWidth() / 5;
        int h = b.getHeight() / 3;

        Rect firstFrame = new Rect(4*w, 0, 5*w, h);

        enemyBird = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i == 0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWeight = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(250, 127, 199, 255);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);
        canvas.drawText(points+"", viewWeight - 100, 70, p);

        playerBird.draw(canvas);
        enemyBird.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN)  {
            if (event.getY() < playerBird.getBoundingBoxRect().top) {
                playerBird.setVY(-100);
                points--;
            }
            else if (event.getY() > (playerBird.getBoundingBoxRect().bottom)) {
                playerBird.setVY(100);
                points--;
            }
        }
        return super.onTouchEvent(event);
    }

    protected void update () {
        playerBird.update(timerInterval);
        enemyBird.update(timerInterval);
        invalidate();
    }

}
