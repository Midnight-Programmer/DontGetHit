package com.jakefidler.dontgethit_jfidl3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.SoundPool;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    Bitmap background, ground, kitty;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float kittyX, kittyY;
    float oldX, oldKittyX;
    ArrayList<Water> water;
    ArrayList<Splash> splash;
    //SoundPool soundPool;
    int mySound = -1;

    public GameView(Context context){
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        kitty = BitmapFactory.decodeResource(getResources(), R.drawable.kitty);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.rgb(255,165,0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.kenpixel_blocks));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        kittyX = dWidth / 2 - kitty.getWidth() / 2;
        kittyY = dHeight - ground.getHeight() - kitty.getHeight();
        water = new ArrayList<>();
        splash = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            water.add(new Water(context));
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(kitty, kittyX, kittyY, null);
        for(int i = 0; i < water.size(); i++){
            canvas.drawBitmap(water.get(i).getWater(water.get(i).waterFrame), water.get(i).waterX, water.get(i).waterY, null);
            water.get(i).waterFrame++;
            if(water.get(i).waterFrame > 2){
                water.get(i).waterFrame = 0;
            }

            water.get(i).waterY += water.get(i).waterSpeed;
            if (water.get(i).waterY + water.get(i).getWaterHeight() - 50 >= dHeight - ground.getHeight()){
                points += 10;
                Splash oneSplash = new Splash(context);
                oneSplash.splashX = water.get(i).waterX - 100;
                oneSplash.splashY = water.get(i).waterY - 50;
                splash.add(oneSplash);
                //soundPool.play(mySound, 1, 1, 0, 0, 1);
                water.get(i).resetPosition();
            }
        }

        for (int i = 0; i < water.size(); i++){
            if (water.get(i).waterX + water.get(i).getWaterWidth() >= kittyX
                && water.get(i).waterX <= kittyX + kitty.getWidth()
                && water.get(i).waterY + water.get(i).getWaterWidth() >= kittyY
                && water.get(i).waterY + water.get(i).getWaterWidth() <= kittyY + kitty.getHeight()){
                life--;
                water.get(i).resetPosition();
                if (life == 0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i = 0; i < splash.size(); i++){
            canvas.drawBitmap(splash.get(i).getSplash(splash.get(i).splashFrame), splash.get(i).splashX, splash.get(i).splashY, null);
            splash.get(i).splashFrame++;
            if (splash.get(i).splashFrame > 3){
                splash.remove(i);
            }
        }

        if (life == 2){
            healthPaint.setColor(Color.YELLOW);
        } else if(life == 1){
            healthPaint.setColor(Color.RED);
        }

        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        canvas.drawText( "" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        if(touchY >= kittyY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldKittyX = kittyX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newKittyX = oldKittyX - shift;
                if(newKittyX <= 0)
                    kittyX = 0;
                else if(newKittyX >= dWidth - kitty.getWidth())
                    kittyX = dWidth - kitty.getWidth();
                else
                    kittyX = newKittyX;
            }
        }

        return true;
    }
}
