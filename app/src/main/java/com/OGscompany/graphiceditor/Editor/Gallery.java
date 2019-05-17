package com.OGscompany.graphiceditor.Editor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.OGscompany.graphiceditor.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Gallery extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_GALLERY = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GALLERY: {
                        // если пользователь закрыл запрос на разрешение, не дав ответа, массив grantResults будет пустым
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // разрешение было предоставлено
                            // выполните здесь необходимые операции для включения функциональности приложения, связанной с запрашиваемым разрешением
                } else {
                            // разрешение не было предоставлено
                            // выполните здесь необходимые операции для выключения функциональности приложения, связанной с запрашиваемым разрешением
                }
                return;
            }
        }
    }
    public void onClick(View v) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bmp = null;
        ImageView imageView = findViewById(R.id.imageView);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bmp);
                    /*BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    int inSampleSize = 1, height = bmp.getHeight(), width = bmp.getWidth();
                    if (height > 2000 || width > 2000) {

                        final int halfHeight = height / 2;
                        final int halfWidth = width / 2;

                        while ((halfHeight / inSampleSize) >= 2000 && (halfWidth / inSampleSize) >= 2000) {
                            inSampleSize *= 2;
                        }
                    }
                    options.inSampleSize = inSampleSize;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.imageView, options);
                    String imageType = options.outMimeType;*/
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] array = stream.toByteArray();
                    Intent intent1 = new Intent(this, Editor.class);
                    intent1.putExtra("image",array);
                    startActivity(intent1);
                }
        }
    }
}
