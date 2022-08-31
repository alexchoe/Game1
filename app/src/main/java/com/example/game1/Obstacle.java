package com.example.game1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject{
    private Rect rectangle;
    private Rect rectangle2;
    private Rect scoreRectangle;
    private boolean alreadyScored = false;
    public boolean getAlreadyScored() {
        return alreadyScored;
    }
    public void scored() {
        alreadyScored = true;
    }

    private int color;


    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
        scoreRectangle.top += y;
        scoreRectangle.bottom += y;

    }

    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
        scoreRectangle = new Rect(startX, startY, startX + playerGap, startY + rectHeight);
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
    }

    public boolean playerScores(RectPlayer player) {
        return Rect.intersects(scoreRectangle, player.getRectangle());
    }


    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);

        Paint paint2 = new Paint();
        paint2.setColor(Color.TRANSPARENT);
        canvas.drawRect(scoreRectangle, paint2);
    }

    @Override
    public void update() {

    }
}
