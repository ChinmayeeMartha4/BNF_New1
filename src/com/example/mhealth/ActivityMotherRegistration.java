package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.Mother;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.utils.CommonMethods;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityMotherRegistration extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    byte[] image;
    String image64 = "";
    ImageView btnPhoto;
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    Mother mother;
    long server_pw_id = 0;
    String gps;

    Spinner spnHhId;
    RadioGroup eStatus, rgICDS;
    RadioButton getEStatus, rbICDSYes, rbICDSNo;
    Button btnMotherRegSubmit;
    TextView txtHHID, txtMotherName, txtGps, txtMotherHusbandName, txtMotherAge, txtNoOfChild, txtFooter, txtMotherReg;
    EditText etxtMotherName, etxtMotherHusbandName, etxtMotherAge, etxtNoOfChild;
    String strHhId, strlanguageId, strMotherName, strHusband, strChildNo,strLat,
            strLang, strYes, strNo, strTryagain, strFooter, strCancel, strSubmit,
            strMotherRegistration, strMandatory, strOnlyAlpha, strRegDone, strRegisteredICDS="";
    private Bitmap bitmap;
    private long local_m_id;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    public static EditText etxtDateOfScreening;

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_registration);


        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/ Mother Registration");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        uploadAttendance();

        strlanguageId = sph.getString("Language", "1");// getting languageId
        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave, strlanguageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strlanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        strMotherRegistration = sqliteHelper.LanguageChanges(ConstantValue.LANMotherRegistration, strlanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, strlanguageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, strlanguageId);
        strRegDone = sqliteHelper.LanguageChanges(ConstantValue.LANPWRegDone, strlanguageId);
        strHusband = sqliteHelper.LanguageChanges(ConstantValue.LANHusbandName, strlanguageId);
        strChildNo = sqliteHelper.LanguageChanges(ConstantValue.LANNumberOfChild, strlanguageId);
        strMotherName = sqliteHelper.LanguageChanges(ConstantValue.LANMother1, strlanguageId);
        strHhId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId, strlanguageId);

        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strlanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strlanguageId);
        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strlanguageId);

        txtGps.setText(gps);
        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        txtHHID.setText(strHhId);
        txtMotherReg.setText(strMotherRegistration);
        txtMotherHusbandName.setText(strHusband);
        txtNoOfChild.setText(strChildNo);
        txtMotherName.setText(strMotherName);
        //txtFooter.setText(strFooter);
        btnMotherRegSubmit.setText(strSubmit);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);


        int user_id = sph.getInt("user_id", 0);

        populateHHList(spnHhId, "eligible_family", "familyid", "house_number",
                "Select Household Id", "where anganwadi_center_id=" + user_id);

        tvTitleText.setText("Mother Registration");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strMotherRegistration + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivityMotherRegistration.this,
                                MainMenuRegistrationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //registered ICDS
        rgICDS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbICDSYes:
                        strRegisteredICDS="Yes";
                        break;
                    case R.id.rbICDSNo:
                        strRegisteredICDS="No";
                        break;
                }
            }
        });
    }

    public void click_Image(View vw) {

        if (Build.VERSION.SDK_INT > 19) {
            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                intentForCamera(getpic, 1);
            } catch (Exception e) {
                Log.d("exp_result:", e.getMessage().toString());
            }
        }
        if (Build.VERSION.SDK_INT < 20) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(),
                    "temp.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }

    }

    private void intentForCamera(Intent getpic, int requestCode) {
        try {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());
                getpic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(getpic, requestCode);
            }

        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "branding" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @SuppressLint("ResourceAsColor")
    public Bitmap compressImage(String filePath) {

        //String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 1024.0f;
        float maxWidth = 812.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        utils = new ImageLoadingUtils(this);
        options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;


        Matrix scaleMatrix = new Matrix();
//        Matrix scaleMatrix = {0.9846154, 0.0, 4.4307556; 0.0, 0.9846154, 7.8769226][0.0, 0.0, 1.0]};

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

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


        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        Paint tPaint = new Paint();
        tPaint.setTextSize(24);
        tPaint.setColor(Color.parseColor("#E51900"));
        tPaint.setStyle(Paint.Style.FILL);

        Paint mpaint = new Paint();
        mpaint.setColor(Color.parseColor("#40FFFFFF"));
        mpaint.setStyle(Paint.Style.FILL);
        if (ratioX < 1) {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 65f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 25f, scaledBitmap.getHeight() - 35f, tPaint);
            canvas.drawText(lat, 25f, scaledBitmap.getHeight() - 10f, tPaint);
        } else {
            canvas.drawRect(0f, scaledBitmap.getHeight() - 120f, scaledBitmap.getWidth(), scaledBitmap.getHeight(), mpaint);
            canvas.drawText(dateTime, 50f, scaledBitmap.getHeight() - 95f, tPaint);
            canvas.drawText(lat, 50f, scaledBitmap.getHeight() - 70f, tPaint);
        }
//        canvas.drawRect(middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,middleX - bmp.getWidth()/2, middleY - bmp.getHeight() / 2,tPaint);

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
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;

    }

    public Location getLatitudeLogitude() {
        GPSTracker gpsTracker = new GPSTracker(this);
        return gpsTracker.getLocation();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if (bitmap != null && !bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//        try {
//            if (resultCode == RESULT_OK) {
//
//                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//
//                    File f = new File(Environment.getExternalStorageDirectory().toString());
//
//                    Bundle extras = data.getExtras();
//                    // Get the returned image from extra
//                    bitmap = (Bitmap) extras.get("data");
////                    for (File temp : f.listFiles()) {
////                        if (temp.getName().equals("temp.jpg")) {
////                            f = temp;
////                            break;
////                        }
////                    }
//                    String selectedImagePath = "";
//                    try {
////                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
////                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                        //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//                        bitmap = getResizedBitmap(bitmap, 350);
//
//                        btnPhoto.setImageBitmap(bitmap);
//
//                        OutputStream fOut = null;
//
//                        try {
////                            fOut = new FileOutputStream(f);
////                            bitmap.compress(Bitmap.CompressFormat.PNG, 5, fOut);
////                            fOut.flush();
////                            fOut.close();
//                            image64 = encodeTobase64(bitmap);
//
//                        }
////                        catch (FileNotFoundException e) {
////                            e.printStackTrace();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (Build.VERSION.SDK_INT > 19) {
                //for camera intent
                if (requestCode == 1) {
                    try {
                        Uri imageUri = Uri.parse(mCurrentPhotoPath);
                        File file = new File(imageUri.getPath());

                        ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                        bitmap = imageLatLogHeper.compressImage(file + "", "");

                        btnPhoto.setImageBitmap(bitmap);
                        image64 = encodeTobase64(bitmap);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (Build.VERSION.SDK_INT < 20) {
                try {
                    if (resultCode == RESULT_OK) {

                        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

                            File f = new File(Environment.getExternalStorageDirectory().toString());
                            for (File temp : f.listFiles()) {
                                if (temp.getName().equals("temp.jpg")) {
                                    f = temp;
                                    break;
                                }
                            }
                            String selectedImagePath = "";
                            try {
                                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);

                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", "");

                                btnPhoto.setImageBitmap(bitmap);

                                OutputStream fOut = null;

                                try {
                                    fOut = new FileOutputStream(f);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 5, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    image64 = encodeTobase64(bitmap);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
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

    public void btnSaveMother_Click(View view) {

        try {
            if (spnHhId.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), "Select a House Number", Toast.LENGTH_LONG).show();
            } else {
                mother.setWidow("");
                mother.setMarried("");
                mother.setUnmarried("");
                getEStatus = (RadioButton) findViewById(eStatus.getCheckedRadioButtonId());
                String e_status = getEStatus.getText().toString().trim();
                if (e_status.equalsIgnoreCase("Married")) {
                    mother.setMarried("Married");
                }
                if (e_status.equalsIgnoreCase("Single")) {
                    mother.setUnmarried("Single");
                }
                if (e_status.equalsIgnoreCase("Widow")) {
                    mother.setWidow("Widow");
                }

                mother.setMotherName(etxtMotherName.getText().toString().trim());
                mother.setMotherhusbandName(etxtMotherHusbandName.getText().toString().trim());
                mother.setAge(etxtMotherAge.getText().toString().trim());
                mother.setNumber_of_child(etxtNoOfChild.getText().toString().trim());
                mother.setImage(image64);
                mother.setMobile_unique_id(CommonMethods.getUUID());
                mother.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                mother.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
                mother.setRegistred_icds(strRegisteredICDS);

                String strName = etxtMotherName.getText().toString().trim();
                String strHusbandName = etxtMotherHusbandName.getText().toString().trim();

                if (spnHhId.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Select a House Number", Toast.LENGTH_LONG).show();


                } else if (mother.getMotherName().equals("")
                        || mother.getAge().equals("")
                        || mother.getNumber_of_child().equals("")
                        || Integer.parseInt(mother.getAge()) < 12
                        || Integer.parseInt(mother.getAge()) > 70
                        || Integer.parseInt(mother.getNumber_of_child()) < 1
                        || Integer.parseInt(mother.getNumber_of_child()) > 20
                ) {

                    if (mother.getMotherName().equals(""))
                        etxtMotherName.setError("Name" + " " + strMandatory);

                    if (mother.getAge().equals("")) {
                        etxtMotherAge.setError("Age " + strMandatory);
                    }
                    if (Integer.parseInt(mother.getAge()) < 12) {
                        //String[] util = Utility.split(strHeight);
                        //String height = util[0];
                        etxtMotherAge.setError("Age should be greater than 12.");
                        etxtMotherAge.setFocusable(true);
                    }
                    if (Integer.parseInt(mother.getAge()) > 70) {
                        //String[] util = Utility.split(strHeight);
                        //String height = util[0];
                        etxtMotherAge.setError("Age should be less/equal to  70.");
                        etxtMotherAge.setFocusable(true);
                    }
                    if (mother.getNumber_of_child().equals("")) {
                        etxtNoOfChild.setError("Number of child " + strMandatory);
                    }
                    if (Integer.parseInt(mother.getNumber_of_child()) < 1) {
                        //String[] util = Utility.split(strHeight);
                        //String height = util[0];
                        etxtNoOfChild.setError("Number of child should be greater than 1.");
                        etxtNoOfChild.setFocusable(true);
                    }
                    if (Integer.parseInt(mother.getNumber_of_child()) > 20) {
                        //String[] util = Utility.split(strHeight);
                        //String height = util[0];
                        etxtNoOfChild.setError("Number of child should be less than 20.");
                        etxtNoOfChild.setFocusable(true);
                    }


                } else if (!strName.matches("[a-zA-Z ]+")) {
                    etxtMotherName.requestFocus();
                    etxtMotherName.setError(strOnlyAlpha);
                } else if (!strHusbandName.matches("[a-zA-Z ]+")) {
                    etxtMotherHusbandName.requestFocus();
                    etxtMotherHusbandName.setError(strOnlyAlpha);
                } else {
                    String familyid="";
                    String strHouseHoldId = (spnHhId.getSelectedItem().toString());
                    String[] houseHoldId = strHouseHoldId.split("\\(");
                    String hh = houseHoldId[0].trim();

                    familyid = getSelectedValue(spnHhId);
                    mother.setHhId(hh);
                    mother.setParent_id(Integer.parseInt(familyid));

                    etxtMotherName.setError(null);
                    etxtMotherAge.setError(null);
                    etxtNoOfChild.setError(null);

                    try {
                        local_m_id = sqliteHelper.MotherRegistration(mother);


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    etxtMotherName.setText("");
                    etxtMotherHusbandName.setText("");
                    etxtMotherAge.setText("");
                    etxtNoOfChild.setText("");


                    if (local_m_id > 0) {
                        Toast.makeText(getApplicationContext(), "Mother Registration done.",
                                Toast.LENGTH_LONG).show();

                        Intent intent1 = new Intent(this, MainMenuRegistrationActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);

                    } else {
                        Toast.makeText(getApplicationContext(), strTryagain,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data;
        data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        return data.getValue();
    }

    public void initialize() {

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);
        mother = new Mother();

        etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        btnPhoto = (ImageView) findViewById(R.id.btnPhoto);
        txtMotherReg = (TextView) findViewById(R.id.txtMotherReg);
        txtHHID = (TextView) findViewById(R.id.txtHHID);
        txtMotherName = (TextView) findViewById(R.id.txtMotherName);
        txtGps = (TextView) findViewById(R.id.txtGps);
        txtMotherHusbandName = (TextView) findViewById(R.id.txtMotherHusbandName);
        txtMotherAge = (TextView) findViewById(R.id.txtMotherAge);
        txtNoOfChild = (TextView) findViewById(R.id.txtNoOfChild);

        etxtMotherName = (EditText) findViewById(R.id.etxtMotherName);
        etxtMotherHusbandName = (EditText) findViewById(R.id.etxtMotherHusbandName);
        etxtMotherAge = (EditText) findViewById(R.id.etxtMotherAge);
        etxtNoOfChild = (EditText) findViewById(R.id.etxtNoOfChild);

        spnHhId = (Spinner) findViewById(R.id.spnHhId);
        btnMotherRegSubmit = (Button) findViewById(R.id.btnMotherRegSubmit);

        txtFooter = (TextView) findViewById(R.id.txtFooter);

        eStatus = (RadioGroup) findViewById(R.id.rgStatus);
        rgICDS=findViewById(R.id.rgICDS);
                rbICDSYes=findViewById(R.id.rbICDSYes);
        rbICDSNo=findViewById(R.id.rbICDSNo);
    }

    public void populateHHList(Spinner spinner, String tableName,
                               String col_id, String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerHHID(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityMotherRegistration.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt(label);
        spinner.setPrompt(" House Hold");
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_mother_registration, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strMotherRegistration + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(
                        ActivityMotherRegistration.this,
                        MainMenuRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }

    //date of recording click
    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dt = day + "-" + month + "-" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            etxtDateOfScreening.setText(formattedDate);
            etxtDateOfScreening.setError(null);
        }
    }
}