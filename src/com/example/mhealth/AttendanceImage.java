package com.example.mhealth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;

import com.example.mhealth.model.AttendanceImagePojo;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttendanceImage extends Activity {
    TextView submit;
    ImageView image;
    private final static int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 2000;
    Bitmap bitmap;
    File destination;
    String imagePath;
    InputStream inputStream;
    String image64 = "";
    String encodedImage = "";
    String screen_type = "";
    AttendanceImagePojo attendanceImagePojo;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    String currentTimeStamp;
    String ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_take_image);
        setTitle(Html.fromHtml("<font color=\"#FFFFFFFF\">" + getString(R.string.take_image) + "</font>"));
        init();
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null) {
            screen_type = bundle.getString("screen_type", "");
            ids= sharedPrefHelper.getString("id","");

        }
        attendanceImagePojo=sqliteHelper.getAttendanceImageData();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        currentTimeStamp = dateFormat.format(new Date());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screen_type.equals("MainMenu")) {
                    attendanceImagePojo.setStart_image(image64);
                    attendanceImagePojo.setStart_time(currentTimeStamp);


//                    sqliteHelper.saveAttendanceImageData(attendanceImagePojo, sharedPrefHelper.getString("user_id", ""));
                    sqliteHelper.saveAttendanceImageData(attendanceImagePojo,ids);
                    Intent intent = new Intent(com.example.mhealth.AttendanceImage.this, MainMenuActivity.class);
                    intent.putExtra("screen_type", "AttendanceImage");
                    intent.putExtra("start_time", attendanceImagePojo.getStart_time());
                    startActivity(intent);
                }
                else
                {
                    attendanceImagePojo.setEnd_image(image64);
                    attendanceImagePojo.setEnd_time(currentTimeStamp);
//                    sqliteHelper.saveAttendanceImageData1(attendanceImagePojo, sharedPrefHelper.getString("user_id",""));
                    sqliteHelper.saveAttendanceImageData1(attendanceImagePojo,ids);
                    Intent intent=new Intent(AttendanceImage.this, MainMenuActivity.class);
                    intent.putExtra("start_time", attendanceImagePojo.getStart_time());
                    intent.putExtra("end_time", attendanceImagePojo.getEnd_time() );
                    startActivity(intent);
                }

            }
        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attendanceImagePojo.setStart_image(image64);
//                attendanceImagePojo.setEnd_image(image64);
//                sqliteHelper.saveAttendanceImageData1(attendanceImagePojo, sharedPrefHelper.getString("user_id",""));
//                Intent intent=new Intent(AttendanceImage.this, MainMenu.class);
//                intent.putExtra("screen_type","AttendanceImage");
//                startActivity(intent);

//            }
//        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                uploadImageOrDocuments(PICK_IMAGE_CAMERA, PICK_IMAGE_GALLERY);
                setProfilePhotoClick();

            }
        });
    }
    private void setProfilePhotoClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, PICK_IMAGE_CAMERA);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void uploadImageOrDocuments(int pickImageCamera, int pickImageGallery) {
        try {
            final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent1, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStream = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                image64 = encodeTobase64(bitmap);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                image.setImageBitmap(bitmap);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePath = destination.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            try {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    image64 = encodeTobase64(bitmap);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    destination = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                    image.setImageBitmap(bitmap);
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagePath = destination.getAbsolutePath();

                } else {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST) {

            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                image64 = encodeTobase64(bitmap);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                image.setImageBitmap(bitmap);
                FileOutputStream fo1;
                try {
                    destination.createNewFile();
                    fo1= new FileOutputStream(destination);
                    fo1.write(byteArrayOutputStream.toByteArray());
                    fo1.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePath = destination.getAbsolutePath();

            }

        }
    }

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 15, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    private void init()
    {
        submit=findViewById(R.id.submit);
        image=findViewById(R.id.image_att);
        attendanceImagePojo= new AttendanceImagePojo();
        sqliteHelper=new SqliteHelper(this);
        sharedPrefHelper=new SharedPrefHelper(this);
    }
}