package dev.haschish.stupidbirds;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private Bitmap bitmap;
    private List<Rect> frames;
    private int frameWidth;
    private int frameHeight;
    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;

    private double x;
    private double y;

    private double vX;
    private double vY;

    private int padding;

    public Sprite (double x, double y, double vX, double vY, Rect initFrame, Bitmap bitmap) {
        this.x = x;
        this.y = y;
        this.vX = vX;
        this.vY = vY;
        this.bitmap = bitmap;
        this.frames = new ArrayList<Rect>();
        this.frames.add(initFrame);
        this.bitmap = bitmap;
        this.timeForCurrentFrame = 0;
        this.frameTime = 0.1;
        this.currentFrame = 0;
        this.frameWidth = initFrame.width();
        this.frameHeight = initFrame.height();
        this.padding = 20;
    }

    public void addFrame (Rect frame) {
        frames.add(frame);
    }

    public void update (int ms) {
        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }

        x = x + vX * ms / 1000;
        y = y + vY * ms / 1000;
    }

    public void draw (Canvas canvas) {
        Paint p = new Paint();
        Rect destination = new Rect((int)x, (int)y, (int)x + frameWidth, (int)y + frameHeight);
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, p);
    }

    public Rect getBoundingBoxRect () {
        int left = (int)x + padding;
        int top = (int)y + padding;
        int right = (int)(x + frameWidth - 2 * padding);
        int bottom = (int)(y + frameHeight - 2 * padding);

        return new Rect(left, top, right, bottom);
    }

    public boolean intersect (Sprite s) {
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect());
    }
}
