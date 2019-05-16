package com.OGscompany.graphiceditor.Editor.Filters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class FilterLib{
	static public Bitmap BlackFilter(Bitmap inBitmap){
        Bitmap fBitmap = Bitmap.createBitmap(inBitmap.getWidth(),inBitmap.getHeight(),inBitmap.getConfig());
        for(int i=0; i<inBitmap.getWidth(); i++){
            for(int j=0; j<inBitmap.getHeight(); j++){
                int p = inBitmap.getPixel(i, j);
				
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) (r*0.21);
                g = (int) (g*0.71);
				b = (int) (b*0.07);
                fBitmap.setPixel(i,j,Color.argb(Color.alpha(p),r,g,b));
            }
        }
        return fBitmap;
    }
	
    static public Bitmap BlueFilter(Bitmap inBitmap){
        Bitmap fBitmap = Bitmap.createBitmap(inBitmap.getWidth(),inBitmap.getHeight(),inBitmap.getConfig());
        for(int i=0; i<inBitmap.getWidth(); i++){
            for(int j=0; j<inBitmap.getHeight(); j++){
                int p = inBitmap.getPixel(i, j);

                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) (r*0.92);
                b = (int) (b*1.25);
                if (b > 255){
                    b = 255;
                }
                fBitmap.setPixel(i,j,Color.argb(Color.alpha(p),r,g,b));
            }
        }
        return fBitmap;
    }

    static public Bitmap SephiaFilter(Bitmap inBitmap) {
        int r,g,b,count;
        int depth = 18;
        Bitmap fBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(),inBitmap.getConfig());
        Canvas canvas = new Canvas(fBitmap);
        Paint paint = new Paint();
        ColorMatrix cmt = new ColorMatrix();
        cmt.setScale(.4f, .4f, .4f, 1.0f);
        ColorMatrixColorFilter ft = new ColorMatrixColorFilter(cmt);
        paint.setColorFilter(ft);
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        for(int i=0; i < inBitmap.getWidth(); i++) {
            for(int j=0; j < inBitmap.getHeight(); j++) {
                int p = inBitmap.getPixel(i, j);
                r = Color.red(p);
                g = Color.green(p);
                b = Color.blue(p);
                count = (r + g + b) / 3;
                r = g = b = count;
                r = r + (depth * 2);
                g = g + depth;
                if(r > 255) {
                    r = 255;
                }
                if(g > 255) {
                    g = 255;
                }
                fBitmap.setPixel(i,j,Color.rgb(r,g,b));
            }
        }
        return fBitmap;
    }
}