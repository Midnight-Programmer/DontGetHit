package com.jakefidler.dontgethit_jfidl3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Splash {

    Bitmap splash[] = new Bitmap[4];
    int splashFrame = 0;
    int splashX, splashY;

    public Splash(Context context){
        splash[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash0);
        splash[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash1);
        splash[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash2);
        splash[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash3);
    }

    public Bitmap getSplash(int splashFrame){
        return splash[splashFrame];
    }
}
