package com.jakefidler.dontgethit_jfidl3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Water {
    Bitmap water[] = new Bitmap[3];
    int waterFrame = 0;
    int waterX, waterY, waterSpeed;
    Random random;

    public Water(Context context){
        water[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.waterdrop);
        water[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.waterdrop);
        water[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.waterdrop);
        random = new Random();
        resetPosition();
    }

    public Bitmap getWater(int waterFrame){
        return water[waterFrame];
    }

    public int getWaterWidth(){
        return water[0].getWidth();
    }

    public int getWaterHeight(){
        return water[0].getHeight();
    }

    public void resetPosition() {
        waterX = random.nextInt(GameView.dWidth - getWaterWidth());
        waterY = -200 + random.nextInt(600) * -1;
        waterSpeed = 35 + random.nextInt(16);
    }
}
