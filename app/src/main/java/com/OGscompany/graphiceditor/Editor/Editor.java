package com.OGscompany.graphiceditor.Editor;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import com.OGscompany.graphiceditor.R;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileOutputStream;


public class Editor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ImageView imageView;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent1 = getIntent();
        if (intent1 != null) {
            byte[] array = intent1.getByteArrayExtra("image");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 0;
            imageView = (ImageView) findViewById(R.id.imageView);
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length, options);
            imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rotate) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Editor.this);

            View view = getLayoutInflater().inflate(R.layout.activity_rotate, null);

            final EditText input = (EditText) view.findViewById(R.id.editText);
            Button buttonYes = (Button) view.findViewById(R.id.button2);
            Button buttonNo = (Button) view.findViewById(R.id.button3);

            mBuilder.setView(view);
            final AlertDialog dialog = mBuilder.create();

            buttonYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if (!input.getText().toString().isEmpty()) {
                        Toast.makeText(Editor.this, "Ура, поворот!!!", Toast.LENGTH_LONG).show();
                        String A = input.getText().toString();
                        int angle = Integer.parseInt(A);
                        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        if (angle < 0) {
                            angle += 90;
                        }
                        if (((angle / 90) & 1) == 1) {
                            bmp = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
                        }
                        for (int wh = 0; wh < bitmap.getWidth(); wh++) {
                            for (int ht = 0; ht < bitmap.getHeight(); ht++) {
                                int a = wh, b = ht;
                                if (((angle / 90) & 1) == 1) {
                                    a = bitmap.getHeight() - ht;
                                    b = wh;
                                }
                                if (((angle / 90) & 2) == 2) {
                                    a = bitmap.getHeight() - ht;
                                    b = bitmap.getWidth() - wh;
                                }
                                if (a < 0 || a >= bitmap.getWidth()) {
                                    continue;
                                }
                                if (b < 0 || b >= bitmap.getHeight()) {
                                    continue;
                                }
                                bmp.setPixel(a, b, bitmap.getPixel(wh, ht));
                            }
                        }
                        bitmap = bmp;
                        int X = bmp.getWidth();
                        int Y = bmp.getHeight();
                        double a = (double) (90 - angle % 90) * Math.PI / 180.0;
                        double cosAngle = Math.cos(a);
                        double sinAngle = Math.sin(a);
                        double AB = X * sinAngle + Y * cosAngle;
                        double AD = Y * sinAngle + X * cosAngle;
                        bmp = Bitmap.createBitmap((int) AB + 2, (int) AD + 2, Bitmap.Config.ARGB_8888);
                        bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
                        for (int nX = 0; nX <= (int) AB; nX++) {
                            for (int nY = 0; nY <= (int) AD; nY++) {
                                int wh1 = (int) (nX * sinAngle - nY * cosAngle + X * cosAngle * cosAngle);
                                int ht1 = (int) (nX * cosAngle + nY * sinAngle - X * sinAngle * cosAngle);

                                if(wh1 < 0 || wh1 >= bitmap.getWidth()) {
                                    continue;
                                }
                                if(ht1 < 0 || ht1 >= bitmap.getHeight()) {
                                    continue;
                                }
                                bmp.setPixel(nX, nY, bitmap.getPixel(wh1, ht1));
                            }
                        }
                        bitmap = bmp;
                        imageView.setImageBitmap(bitmap);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Editor.this, "Вы не ввели число!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            buttonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } else if (id == R.id.nav_filter) {

        } else if (id == R.id.nav_scale) {
            AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(Editor.this);

            View view1 = getLayoutInflater().inflate(R.layout.activity_scale, null);

            final EditText input1 = (EditText) view1.findViewById(R.id.editText2);

            Button buttonYes1 = (Button) view1.findViewById(R.id.button4);
            Button buttonNo1 = (Button) view1.findViewById(R.id.button5);

            mBuilder1.setView(view1);
            final AlertDialog dialog1 = mBuilder1.create();

            buttonYes1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if (!input1.getText().toString().isEmpty()) {
                        Toast.makeText(Editor.this, "Масштабируем!!!", Toast.LENGTH_LONG).show();
                        String A = input1.getText().toString();
                        double C = Double.parseDouble(A);
                        /*Bitmap bmp = bitmap;
                        Matrix matrix = new Matrix();
                        bitmap = Bitmap.createBitmap(bmp, A, B, C, D, matrix, true);
                        imageView.setImageBitmap(bitmap);*/
                        int a, b, c, d, x, y, whC, htC, wh = bitmap.getWidth(), ht = bitmap.getHeight();
                        whC = (int) (bitmap.getWidth() * C);
                        htC = (int) (bitmap.getHeight() * C);
                        Bitmap bmp = Bitmap.createBitmap(whC, htC, Bitmap.Config.ARGB_8888);
                        float x_ratio = ((float)(wh - 1)) / whC;
                        float y_ratio = ((float)(ht - 1)) / htC;
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
                        bitmap = bmp;
                        imageView.setImageBitmap(bitmap);
                        dialog1.dismiss();
                    } else {
                        Toast.makeText(Editor.this, "Вы не ввели число!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            buttonNo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    dialog1.dismiss();
                }
            });

            dialog1.show();

        } else if (id == R.id.nav_segment) {

        } else if (id == R.id.nav_splane) {

        } else if (id == R.id.nav_retush) {

        } else if (id == R.id.nav_mask) {

        } else if (id == R.id.nav_btf) {

        } else if (id == R.id.nav_save) {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "вашаФотка.png");

            try {
                FileOutputStream fOS = null;
                try {
                    fOS = new FileOutputStream(file);
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOS);
                } finally {
                    if (fOS != null) fOS.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
