package com.OGscompany.graphiceditor.Editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.OGscompany.graphiceditor.MainActivity;
import com.OGscompany.graphiceditor.R;

import java.io.ByteArrayOutputStream;

public class Camera extends AppCompatActivity {

    final int REQUEST_CODE_PHOTO = 1;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // разрешение было предоставлено
                    // выполните здесь необходимые операции для включения функциональности приложения, связанной с запрашиваемым разрешением
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_PHOTO);
                } else {
                    // разрешение не было предоставлено
                    // выполните здесь необходимые операции для выключения функциональности приложения, связанной с запрашиваемым разрешением
                    Intent intent0 = new Intent(this, MainActivity.class);
                    startActivity(intent0);
                }
                return;
            }
        }
    }
    public void onClickPhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_PHOTO);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    Bundle bndl = intent.getExtras();
                    if (bndl != null) {
                        Object obj = intent.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            imageView = findViewById(R.id.imageView);
                            imageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] array = stream.toByteArray();
                            Intent intent1 = new Intent(this, Editor.class);
                            intent1.putExtra("image",array);
                            startActivity(intent1);
                        }
                    }
                }
            }
        }
    }
}
