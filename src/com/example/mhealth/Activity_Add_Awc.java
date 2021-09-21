package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity_Add_Awc extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE3 = 102;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4 = 103;

    private static final int CAMERA_PIC_REQUEST = 1;
    String image1 = "", image2 = "", image3 = "", image4 = "";
    byte[] image;


    RadioGroup rgAwcType, rgAwcBuildingStatus, rgAwcRentOrOwned, rgToiletYesNo, rgWaterYesNo, rgtlYesNo;
    TextView tvAwcHead, tvAwcType, tvbuildingStatus, tvAwcBuildingType, tvWaterType, tvWaterSafety,
            tvToilet, tvWaterAvailability, tvAwcHwf, tvAwcTT, tvAwcEquipment, tvAwcOutsideImg, tvAwcKitchenImg,
            tvAwcToiletImg, tvAwcHandWashImg, txtFooter,tvTitleText;
    RadioButton rbAwcTypeMain, rbAwcTypeMini, rbBuildingStatusKuchha, rbBuildingStatusPukka, rbown, rbrented,
            rbToiletYes, rbToiletNo, rbWaterYes, rbWaterNo, rbtlyes, rbtlno;
    Button tvSubmit;
    Spinner spnWaterType, spnAwcWaterSafety, spnHwf;
    ImageView img1, img2, img3, img4;
    CheckBox cbStadiometer, cbinfantometer, cbSpring, cbAdult;
    String equipment = "";
    SqliteHelper sqlitehelper;
    SharedPrefHelper sph;
    int awc_id;
    ArrayAdapter<String> adpWT, adpAWS, adpHWF;
    String mCurrentPhotoPath = "";
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    private ImageLoadingUtils utils;

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
        setContentView(R.layout.activity_add_awc);

        sqlitehelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        awc_id = sph.getInt("user_id", 0);
        String languageId = sph.getString("Language", "1");

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Add Anganwadi Details");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        tvAwcHead = (TextView) findViewById(R.id.tvAwcHead);
        tvAwcType = (TextView) findViewById(R.id.tvAwcType);
        tvbuildingStatus = (TextView) findViewById(R.id.tvbuildingStatus);
        tvAwcBuildingType = (TextView) findViewById(R.id.tvAwcBuildingType);
        tvWaterType = (TextView) findViewById(R.id.tvWaterType);
        tvWaterSafety = (TextView) findViewById(R.id.tvWaterSafety);
        tvToilet = (TextView) findViewById(R.id.tvToilet);
        tvWaterAvailability = (TextView) findViewById(R.id.tvWaterAvailability);
        tvAwcHwf = (TextView) findViewById(R.id.tvAwcHwf);
        tvAwcTT = (TextView) findViewById(R.id.tvAwcTT);
        tvAwcEquipment = (TextView) findViewById(R.id.tvAwcEquipment);
        tvAwcOutsideImg = (TextView) findViewById(R.id.tvAwcOutsideImg);
        tvAwcKitchenImg = (TextView) findViewById(R.id.tvAwcKitchenImg);
        tvAwcToiletImg = (TextView) findViewById(R.id.tvAwcToiletImg);
        tvAwcHandWashImg = (TextView) findViewById(R.id.tvAwcHandWashImg);
        tvSubmit = (Button) findViewById(R.id.tvSubmit);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        rbAwcTypeMain = (RadioButton) findViewById(R.id.rbAwcTypeMain);
        rbAwcTypeMini = (RadioButton) findViewById(R.id.rbAwcTypeMini);
        rbBuildingStatusKuchha = (RadioButton) findViewById(R.id.rbBuildingStatusKuchha);
        rbBuildingStatusPukka = (RadioButton) findViewById(R.id.rbBuildingStatusPukka);
        rbown = (RadioButton) findViewById(R.id.rbown);
        rbrented = (RadioButton) findViewById(R.id.rbrented);
        rbToiletYes = (RadioButton) findViewById(R.id.rbToiletYes);
        rbToiletNo = (RadioButton) findViewById(R.id.rbToiletNo);
        rbWaterYes = (RadioButton) findViewById(R.id.rbWaterYes);
        rbWaterNo = (RadioButton) findViewById(R.id.rbWaterNo);
        rbtlyes = (RadioButton) findViewById(R.id.rbtlyes);
        rbtlno = (RadioButton) findViewById(R.id.rbtlno);
        tvTitleText=findViewById(R.id.tvTitleText);
        tvTitleText.setText("Anganwadi Detail");

        tvAwcHead.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHead, languageId));
        tvAwcType.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcType, languageId));
        tvbuildingStatus.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcBuildingStatus, languageId));
        tvAwcBuildingType.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcBuildingType, languageId));
        tvWaterType.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterType, languageId));
        tvWaterSafety.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterSafety, languageId));
        tvToilet.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcToilet, languageId));
        tvWaterAvailability.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterAvailability, languageId));
        tvAwcHwf.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHwf, languageId));
        tvAwcTT.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcTL, languageId));
        tvAwcEquipment.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcEquipment, languageId));
        tvAwcOutsideImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcOutsideImg, languageId));
        tvAwcKitchenImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcKitchenImg, languageId));
        tvAwcToiletImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcToiletImg, languageId));
        tvAwcHandWashImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHandWashImg, languageId));
        tvSubmit.setText(sqlitehelper.LanguageChanges(ConstantValue.LANsv, languageId));

        String text = "<a href='http://indevjobs.org'>" + sqlitehelper.LanguageChanges(ConstantValue.LANTPIC, languageId) + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        rgAwcType = (RadioGroup) findViewById(R.id.rgAwcType);
        rgAwcBuildingStatus = (RadioGroup) findViewById(R.id.rgAwcBuildingStatus);
        rgAwcRentOrOwned = (RadioGroup) findViewById(R.id.rgAwcRentOrOwned);
        spnWaterType = (Spinner) findViewById(R.id.spnAwcWaterType);
        spnAwcWaterSafety = (Spinner) findViewById(R.id.spnAwcWaterSafety);
        rgToiletYesNo = (RadioGroup) findViewById(R.id.rgToiletYesNo);
        rgWaterYesNo = (RadioGroup) findViewById(R.id.rgWaterYesNo);
        rgtlYesNo = (RadioGroup) findViewById(R.id.rgtlYesNo);
        spnHwf = (Spinner) findViewById(R.id.spnHwf);
        cbStadiometer = (CheckBox) findViewById(R.id.cbStadiometer);
        cbStadiometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipment = equipment + "Stadiometer, ";
            }
        });
        cbinfantometer = (CheckBox) findViewById(R.id.cbinfantometer);
        cbinfantometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipment = equipment + "Infantometer, ";
            }
        });
        cbSpring = (CheckBox) findViewById(R.id.cbSpring);
        cbSpring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipment = equipment + "Spring weighing scale, ";
            }
        });
        cbAdult = (CheckBox) findViewById(R.id.cbAdult);
        cbAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                equipment = equipment + "Adult weighing scale, ";
            }
        });

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        String[] wt = sqlitehelper.GetManyData("drinking_water_source", "drinking_water_source", "drinking_water_source != ''");
        adpWT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wt);
        adpWT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWaterType.setAdapter(adpWT);

        String[] aws = {"Staining", "Chlorinate", "Water Filter", "None"};
        adpAWS = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aws);
        adpAWS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAwcWaterSafety.setAdapter(adpAWS);

        String[] hwf = {"Tap Water", "Bucket and Mug", "Bucket, Mug and Soap", "Any Other Innovation"};
        adpHWF = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hwf);
        adpHWF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHwf.setAdapter(adpHWF);

        SetDataIntoViews();

    }

    public void SetDataIntoViews() {
        String strA_Type = sqlitehelper.GetOneData("main_mini", "anganwadi_center", "center_id = " + awc_id);
        if (strA_Type.equals("Main")) {
            rbAwcTypeMain.setChecked(true);
        }
        if (strA_Type.equals("Mini")) {
            rbAwcTypeMini.setChecked(true);
        }

        String strA_BuildingStatus = sqlitehelper.GetOneData("kuchha_pukka", "anganwadi_center", "center_id = " + awc_id);
        if (strA_BuildingStatus.equals("Kuchha")) {
            rbBuildingStatusKuchha.setChecked(true);
        }
        if (strA_BuildingStatus.equals("Pukka")) {
            rbBuildingStatusPukka.setChecked(true);
        }

        String strA_RentOrOwned = sqlitehelper.GetOneData("owned_rented", "anganwadi_center", "center_id = " + awc_id);
        if (strA_RentOrOwned.equals("Owned")) {
            rbown.setChecked(true);
        }
        if (strA_RentOrOwned.equals("Rented")) {
            rbrented.setChecked(true);
        }

        String strA_WaterType = sqlitehelper.GetOneData("water_source", "anganwadi_center", "center_id = " + awc_id);
        if (!strA_WaterType.equals(null)) {
            if (!strA_WaterType.equals("")) {
                spnWaterType.setSelection(adpWT.getPosition(strA_WaterType));
            }
        }
        String strA_WaterSafety = sqlitehelper.GetOneData("water_safety", "anganwadi_center", "center_id = " + awc_id);
        if (!strA_WaterSafety.equals(null)) {
            if (!strA_WaterSafety.equals("")) {
                spnAwcWaterSafety.setSelection(adpAWS.getPosition(strA_WaterSafety));
            }
        }

        String strA_ToiletYesNo = sqlitehelper.GetOneData("toilet", "anganwadi_center", "center_id = " + awc_id);
        if (strA_ToiletYesNo.equals("Yes")) {
            rbToiletYes.setChecked(true);
        }
        if (strA_ToiletYesNo.equals("No")) {
            rbToiletNo.setChecked(true);
        }

        String strA_WaterYesNo = sqlitehelper.GetOneData("water", "anganwadi_center", "center_id = " + awc_id);
        if (strA_WaterYesNo.equals("Yes")) {
            rbWaterYes.setChecked(true);
        }
        if (strA_WaterYesNo.equals("No")) {
            rbWaterNo.setChecked(true);
        }

        String strA_tlYesNo = sqlitehelper.GetOneData("learning_teaching", "anganwadi_center", "center_id = " + awc_id);
        if (strA_tlYesNo.equals("Yes")) {
            rbtlyes.setChecked(true);
        }
        if (strA_tlYesNo.equals("No")) {
            rbtlno.setChecked(true);
        }
        String strA_Hwf = sqlitehelper.GetOneData("hand_washing_facility", "anganwadi_center", "center_id = " + awc_id);
        if (!strA_Hwf.equals(null)) {
            if (!strA_Hwf.equals("")) {
                spnHwf.setSelection(adpHWF.getPosition(strA_Hwf));
            }
        }
        String strEquipment = sqlitehelper.GetOneData("equipment", "anganwadi_center", "center_id = " + awc_id);

    }

    public void ShowWater(View v) {
        rgWaterYesNo.setVisibility(View.VISIBLE);
        tvWaterAvailability.setVisibility(View.VISIBLE);
    }

    public void HideWater(View v) {
        rgWaterYesNo.setVisibility(View.GONE);
        tvWaterAvailability.setVisibility(View.GONE);
    }

    public void click_Image1(View vw) {

        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 1);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }

//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);
    }

    public void click_Image2(View vw) {
        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 2);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }

//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);
    }

    public void click_Image3(View vw) {

        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 3);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE3);
    }

    public void click_Image4(View vw) {

        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 4);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            //for camera intent
            if (requestCode == 1) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap1 = compressImage(file + "");
                    bitmap1 = getResizedBitmap(bitmap1, 350);
                    img1.setImageBitmap(bitmap1);

                    image1 = encodeTobase64(bitmap1);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 2) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap2 = compressImage(file + "");
                    bitmap2 = getResizedBitmap(bitmap2, 350);
                    img2.setImageBitmap(bitmap2);
                    image2 = encodeTobase64(bitmap2);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 3) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap3 = compressImage(file + "");
                    bitmap3 = getResizedBitmap(bitmap3, 350);
                    img3.setImageBitmap(bitmap3);
                    image3 = encodeTobase64(bitmap3);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 4) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap4 = compressImage(file + "");
                    bitmap4 = getResizedBitmap(bitmap4, 350);
                    img4.setImageBitmap(bitmap4);

                    image4 = encodeTobase64(bitmap4);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		try {
//			if (resultCode == RESULT_OK) {
//
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1) {
//
//					if (bitmap1 != null && !bitmap1.isRecycled()) {
//						bitmap1.recycle();
//						bitmap1 = null;
//					}
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap1 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap1 = getResizedBitmap(bitmap1, 350);
//
//						img1.setImageBitmap(bitmap1);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap1.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image1 = encodeTobase64(bitmap1);
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE2) {
//
//					if (bitmap2 != null && !bitmap2.isRecycled()) {
//						bitmap2.recycle();
//						bitmap2 = null;
//					}
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap2 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap2 = getResizedBitmap(bitmap2, 350);
//
//						img2.setImageBitmap(bitmap2);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap2.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image2 = encodeTobase64(bitmap2);
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE3) {
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap3 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap3 = getResizedBitmap(bitmap3, 350);
//
//						img3.setImageBitmap(bitmap3);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap3.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image3 = encodeTobase64(bitmap3);
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4) {
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap4 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap4 = getResizedBitmap(bitmap4, 350);
//
//						img4.setImageBitmap(bitmap4);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap4.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image4 = encodeTobase64(bitmap4);
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 15, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
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

    public void Add_My_Awc_Details(View v) {
        String AwcMainMini = "";
        String AwcPukkaKuchha = "";
        String AwcRentOwned = "";
        String toilet = "";
        String water = "";
        String learning = "";
        try {
            AwcMainMini = ((RadioButton) findViewById(rgAwcType.getCheckedRadioButtonId())).getText().toString();
            AwcPukkaKuchha = ((RadioButton) findViewById(rgAwcBuildingStatus.getCheckedRadioButtonId())).getText().toString();
            AwcRentOwned = ((RadioButton) findViewById(rgAwcRentOrOwned.getCheckedRadioButtonId())).getText().toString();
            toilet = ((RadioButton) findViewById(rgToiletYesNo.getCheckedRadioButtonId())).getText().toString();
            water = ((RadioButton) findViewById(rgWaterYesNo.getCheckedRadioButtonId())).getText().toString();
            learning = ((RadioButton) findViewById(rgtlYesNo.getCheckedRadioButtonId())).getText().toString();
        } catch (Exception e) {
        }
        String WaterType = spnWaterType.getSelectedItem().toString();
        String WaterSafety = spnAwcWaterSafety.getSelectedItem().toString();
        String hand_wash = spnHwf.getSelectedItem().toString();
        String[] awcarr = {AwcMainMini, AwcPukkaKuchha, AwcRentOwned, WaterType, WaterSafety, toilet, water, hand_wash, learning, equipment, image1, image2, image3, image4};
        sqlitehelper.SaveAwcDetails(awcarr);
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
    }

}
