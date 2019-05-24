package com.OGscompany.graphiceditor.Editor;

import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

//https://www.fandroid.info/obnaruzhenie-litsa-na-foto-s-pomoshhyu-android-facedetector-face-api/
class Segmentation{
    private void scanFaces(){
		mDetector = new FaceDetector.Builder(mainActivity)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
		
        if (mDetector.isOperational() && bitmap != null) {
            Bitmap editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = mainActivity.getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(255, 90, 90));
            paint.setTextSize((int) (15 * scale));
            paint.setShadowLayer(1f, 1f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = mDetector.detect(frame);
            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);
            }

            imageView.setImageBitmap(editedBitmap);
            mainActivity.imageChanged = true;
            Toast.makeText(mainActivity.getApplicationContext(),
                    String.format("%s faces found", faces.size()),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
