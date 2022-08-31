package com.example.game1;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class ObstacleManager {
    //higher index = lower on screen = higher y value
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(Constants.CURRENT_CONTEXT);
    SharedPreferences.Editor myEditor = myPreferences.edit();
    public int highScore = myPreferences.getInt("BEST", 0);

    //Typeface font = Typeface.createFromAsset(Constants.CURRENT_CONTEXT.getAssets(), "emulogic.ttf");

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for(Obstacle ob : obstacles) {
            if(ob.playerScores(player) && !ob.getAlreadyScored()) {
                score++;
                if(score >= highScore) {
                    highScore = score;

                }
                ob.scored();
            }
            if (ob.playerCollide(player)) {
                myEditor.putInt("BEST", highScore);
                myEditor.commit();
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while(currY < 0) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public void update() {
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;

        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1 + (startTime - initTime)/2000.0))*Constants.SCREEN_HEIGHT/(10000.0f);
        for(Obstacle ob : obstacles) {
            ob.incrementY(speed * elapsedTime);
        }
        if(obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xStart,
                    obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
            obstacles.remove(obstacles.size() - 1);
            //score++;
        }
    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles)
            ob.draw(canvas);
        Paint paint = new Paint();
        //paint.setTypeface(MainActivity.font);
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("" + score, 50, 250 + paint.ascent() - paint.descent(), paint);


        Paint paint2 = new Paint();
        //paint2.setTypeface(font);
        paint2.setTextSize(100);
        paint2.setColor(Color.MAGENTA);
        paint2.setTextAlign(Paint.Align.RIGHT);


        canvas.drawText("BEST " + highScore, Constants.SCREEN_WIDTH - 50, 250 + paint.ascent() - paint.descent(), paint2);
    }
}
