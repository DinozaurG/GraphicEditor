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
    int width;
    int height;
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
            width = bitmap.getWidth();
            height = bitmap.getHeight();
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
                        String a = input.getText().toString();
                        int A = Integer.parseInt(a);
                        Bitmap bmp = bitmap;
                        Matrix matrix = new Matrix();
                        matrix.postRotate(A);
                        bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
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
            final EditText input2 = (EditText) view1.findViewById(R.id.editText4);
            final EditText input3 = (EditText) view1.findViewById(R.id.editText5);
            final EditText input4 = (EditText) view1.findViewById(R.id.editText6);

            Button buttonYes1 = (Button) view1.findViewById(R.id.button4);
            Button buttonNo1 = (Button) view1.findViewById(R.id.button5);

            mBuilder1.setView(view1);
            final AlertDialog dialog1 = mBuilder1.create();

            buttonYes1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if (!input1.getText().toString().isEmpty()
                            || !input2.getText().toString().isEmpty()
                            || !input3.getText().toString().isEmpty()
                            || !input4.getText().toString().isEmpty()) {
                        Toast.makeText(Editor.this, "Масштабируем!!!", Toast.LENGTH_LONG).show();
                        String b = input1.getText().toString();
                        int A = Integer.parseInt(b);
                        b = input2.getText().toString();
                        int B = Integer.parseInt(b);
                        b = input3.getText().toString();
                        int C = Integer.parseInt(b);
                        b = input4.getText().toString();
                        int D = Integer.parseInt(b);
                        Bitmap bmp = bitmap;
                        Matrix matrix = new Matrix();
                        bitmap = Bitmap.createBitmap(bmp, A, B, C, D, matrix, true);
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
