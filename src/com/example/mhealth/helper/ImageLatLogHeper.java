package com.example.mhealth.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 3/3/2018.
 */

public class ImageLatLogHeper {
    Context context;

    public ImageLatLogHeper(Context context) {
        this.context = context;
    }


    public Bitmap compressImage(String filePath, String child_name) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(filePath);
        String lat = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        try {
            Location location = getLatitudeLogitude();
            lat = "Lat.: " + location.getLatitude();
            lat += " Long.:" + location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExifInterface exif;
        try {

            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);

            b = getResizedBitmap(b, 300);

            if (b.getHeight() > b.getWidth()) {
                Canvas canvas = new Canvas(b);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#40000000"));
                paint.setAntiAlias(true);
                int padding = 0;
                Rect rectangle = new Rect(
                        padding, // Left
                        b.getHeight() - (b.getHeight() / 10), // Top
                        b.getWidth() - padding,
                        b.getHeight() - padding // Right
                        // Bottom
                );
                Log.e("child_name", child_name);
                Paint tPaint = new Paint();
                tPaint.setTextSize(b.getHeight() / 40);
                tPaint.setColor(Color.parseColor("#E51900"));
                tPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(child_name, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 14), tPaint);
                canvas.drawText(dateTime, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 22), tPaint);
                canvas.drawText(lat, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 45), tPaint);

                canvas.drawRect(rectangle, paint);


            } else {

                Canvas canvas = new Canvas(b);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.parseColor("#40000000"));
                paint.setAntiAlias(true);
                int padding = 0;
                Rect rectangle = new Rect(
                        padding, // Left
                        b.getHeight() - (b.getHeight() / 5), // Top
                        b.getWidth() - padding,
                        b.getHeight() - padding // Right
                        // Bottom
                );

                Paint tPaint = new Paint();
                tPaint.setTextSize(b.getHeight() / 20);
                tPaint.setColor(Color.parseColor("#E51900"));
                tPaint.setStyle(Paint.Style.FILL);

                canvas.drawText(child_name, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 7), tPaint);

                canvas.drawText(dateTime, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 11), tPaint);
                canvas.drawText(lat, b.getWidth() / 20, b.getHeight() - (b.getHeight() / 20), tPaint);

                canvas.drawRect(rectangle, paint);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        b = getResizedBitmap(b, 300);
        return b;

    }

    public Location getLatitudeLogitude() {
        GPSTracker gpsTracker = new GPSTracker(context);
        return gpsTracker.getLocation();
    }


    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
