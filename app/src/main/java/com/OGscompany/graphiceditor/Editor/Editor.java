package com.OGscompany.graphiceditor.Editor;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.*;
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
                        int angle0 = Integer.parseInt(A);
                        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        while (angle0 < 0) {
                            angle0 += 360;
                        }
                        while (angle0 >= 360) {
                            angle0 -= 360;
                        }
                        int ANG = angle0;
                        int angle;
                        if (((angle0 / 90) & 1) == 1){
                            bmp = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
                            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);
                        }
                        for (; ANG > 0; ANG -= 45) {
                            if (ANG >= 45)
                                angle = 45;
                            else
                                angle = ANG;
                            for (int i = 0; i < bitmap.getWidth(); i++) {
                                for (int j = 0; j < bitmap.getHeight(); j++) {
                                    int a = i, b = j;
                                    if (((angle / 90) & 1) == 1) {
                                        a = bitmap.getHeight() - j;
                                        b = i;
                                    }
                                    if (((angle / 90) & 2) == 2) {
                                        a = bitmap.getHeight() - j;
                                        b = bitmap.getWidth() - i;
                                    }
                                    if (a < 0 || a >= bitmap.getWidth()) {
                                        continue;
                                    }
                                    if (b < 0 || b >= bitmap.getHeight()) {
                                        continue;
                                    }
                                    bmp.setPixel(a, b, bitmap.getPixel(i, j));
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
                            for (int i = 0; i <= (int) AB; i++) {
                                for (int j = 0; j <= (int) AD; j++) {
                                    int wh1 = (int) (i * sinAngle - j * cosAngle + X * cosAngle * cosAngle);
                                    int ht1 = (int) (i * cosAngle + j * sinAngle - X * sinAngle * cosAngle);

                                    if (wh1 < 0 || wh1 >= bitmap.getWidth()) {
                                        continue;
                                    }
                                    if (ht1 < 0 || ht1 >= bitmap.getHeight()) {
                                        continue;
                                    }
                                    bmp.setPixel(i, j, bitmap.getPixel(wh1, ht1));
                                }
                            }
                            bitmap = bmp;
                        }
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
            AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(Editor.this);

            View view1 = getLayoutInflater().inflate(R.layout.activity_filter, null);

            Button green = (Button) view1.findViewById(R.id.button6);
            Button blue = (Button) view1.findViewById(R.id.button7);
            Button sepia = (Button) view1.findViewById(R.id.button8);
            Button buttonNo1  = (Button) view1.findViewById(R.id.button9);

            mBuilder1.setView(view1);
            final AlertDialog dialog1 = mBuilder1.create();

            green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
                    for(int i = 0; i < bitmap.getWidth(); i++) {
                        for (int j = 0; j < bitmap.getHeight(); j++) {
                            int p = bitmap.getPixel(i, j);

                            int r = Color.red(p);
                            int g = Color.green(p);
                            int b = Color.blue(p);

                            r = (int) (r * 0.21);
                            g = (int) (g * 0.71);
                            b = (int) (b * 0.07);
                            bmp.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                        }
                    }
                    bitmap = bmp;
                    imageView.setImageBitmap(bitmap);
                    dialog1.dismiss();
                }
            });
            blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
                    for(int i = 0; i < bitmap.getWidth(); i++) {
                        for(int j = 0; j < bitmap.getHeight(); j++) {
                            int p = bitmap.getPixel(i, j);

                            int r = Color.red(p);
                            int g = Color.green(p);
                            int b = Color.blue(p);

                            r = (int) (r * 0.92);
                            b = (int) (b * 1.25);
                            if (b > 255) {
                                b = 255;
                            }
                            bmp.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                        }
                    }
                    bitmap = bmp;
                    imageView.setImageBitmap(bitmap);
                    dialog1.dismiss();
                }
            });
            sepia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    int r, g, b, count;
                    int depth = 18;
                    Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),bitmap.getConfig());
                    Canvas canvas = new Canvas(bmp);
                    Paint paint = new Paint();
                    ColorMatrix cmt = new ColorMatrix();
                    cmt.setScale(.4f, .4f, .4f, 1.0f);
                    ColorMatrixColorFilter ft = new ColorMatrixColorFilter(cmt);
                    paint.setColorFilter(ft);
                    canvas.drawBitmap(bitmap, 0, 0, paint);
                    for(int i = 0; i < bitmap.getWidth(); i++) {
                        for(int j = 0; j < bitmap.getHeight(); j++) {
                            int p = bitmap.getPixel(i, j);
                            r = Color.red(p);
                            g = Color.green(p);
                            b = Color.blue(p);
                            count = (r + g + b) / 3;
                            r = g = b = count;
                            r = r + (depth * 2);
                            g = g + depth;
                            if (r > 255) {
                                r = 255;
                            }
                            if (g > 255) {
                                g = 255;
                            }
                            bmp.setPixel(i, j, Color.rgb(r, g, b));
                        }
                    }
                    bitmap = bmp;
                    imageView.setImageBitmap(bitmap);
                    dialog1.dismiss();
                }
            });
            buttonNo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    dialog1.dismiss();
                }
            });
            dialog1.show();

        } else if (id == R.id.nav_scale) {

        } else if (id == R.id.nav_segment) {

        } else if (id == R.id.nav_splane) {

        } else if (id == R.id.nav_retush) {

        } else if (id == R.id.nav_mask) {

        } else if (id == R.id.nav_btf) {

        } else if (id == R.id.nav_save) {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "вашаФотка.jpg");

            try {
                FileOutputStream fOS = null;
                try {
                    fOS = new FileOutputStream(file);
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOS);
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
