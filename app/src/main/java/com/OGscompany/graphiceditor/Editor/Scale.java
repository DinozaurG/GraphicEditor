package com.OGscompany.graphiceditor.Editor;

import android.graphics.Bitmap;

public class Scale {
    static public Bitmap scale(Bitmap bitmap, double C) {
        int a, b, c, d, x, y, whC, htC, wh = bitmap.getWidth(), ht = bitmap.getHeight();
        whC = (int) (bitmap.getWidth() * C);
        htC = (int) (bitmap.getHeight() * C);
        Bitmap bmp = Bitmap.createBitmap(whC, htC, Bitmap.Config.ARGB_8888);
        float x_ratio = ((float) (wh - 1)) / whC;
        float y_ratio = ((float) (ht - 1)) / htC;
        float x_diff, y_diff, blue, red, green;
        for (int i = 0; i < htC; i++) {
            for (int j = 0; j < whC; j++) {
                x = (int) (x_ratio * j);
                y = (int) (y_ratio * i);
                x_diff = (x_ratio * j) - x;
                y_diff = (y_ratio * i) - y;
                try {
                    a = bitmap.getPixel(x, y);
                    b = bitmap.getPixel(x + 1, y);
                    c = bitmap.getPixel(x, y + 1);
                    d = bitmap.getPixel(x + 1, y + 1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    a = bitmap.getPixel(x, y);
                    b = bitmap.getPixel(x, y);
                    c = bitmap.getPixel(x, y);
                    d = bitmap.getPixel(x, y);
                }

                // blue element
                // Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
                blue = (a & 0xff) * (1 - x_diff) * (1 - y_diff) + (b & 0xff) * (x_diff) * (1 - y_diff) +
                        (c & 0xff) * (y_diff) * (1 - x_diff) + (d & 0xff) * (x_diff * y_diff);

                // green element
                // Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
                green = ((a >> 8) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 8) & 0xff) * (x_diff) * (1 - y_diff) +
                        ((c >> 8) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 8) & 0xff) * (x_diff * y_diff);

                // red element
                // Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
                red = ((a >> 16) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 16) & 0xff) * (x_diff) * (1 - y_diff) +
                        ((c >> 16) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 16) & 0xff) * (x_diff * y_diff);


                bmp.setPixel(j, i, 0xff000000 |
                        ((((int) red) << 16) & 0xff0000) |
                        ((((int) green) << 8) & 0xff00) |
                        ((int) blue));
            }
        }
        return bmp;
    }
}
