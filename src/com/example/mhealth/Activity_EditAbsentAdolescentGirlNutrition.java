package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.text.Spannable;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.AdolescentMonitoring;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.helper.Views;
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

/**
 * Created by Admin on 7/3/2017.
 */

public class Activity_EditAbsentAdolescentGirlNutrition extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    long server_pw_id = 0;
    AdolescentMonitoring adolescentMonitoring;
    ArrayList<AdolescentMonitoring> list;
    TextView txtAdolescentGirlMonitor, txtAdolscentName, txtAdolescentWeight,
            txtAdolescentHeight, txtAdolescentHb, txtDateOfRecord,
            txtPregnantNutritionHistory, txtFooter;
    EditText etxtSearchByHhid, etxtAdolescentWeight, etxtAdolescentHeight,
            etxtAdolescentHb;
    Button btnAdolescentGo, btnAdlMonSubmit;
    TextView spnAdolescentName;
    WebView wvAdlNutritionHistory;
    int intAdolescentId;
    String strAdolescentGirlMonitor, strAdolscentName, strAdolescentWeight,
            strAdolescentHeight, strAdolescentHb, strAdolescentBMI, strDateOfRecord,
            strAdolescentGo, strAdlMonSubmit, strlanguageId, strId, strName,
            strData, strHistory, strFooter, strMandatory, strYes, strNo,
            strCancel, strTryAgain, strDone;
    RadioButton adolescent, mother, married;
    RadioGroup current_status;
    int intId;
    ImageView imgAdolscentMon;
    byte[] image;
    String image64 = "";
    String name, current_date;
    int adolescent_girl_id;
    String fdate;
    private int user_id;
    private int flag = 0;
    private Bitmap bitmap;
    private String mCurrentPhotoPath;
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
        setContentView(R.layout.activity_absent_adolesect_monitoring);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthy_Monitoring/ Adolescent Monitoring");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        setLanguage();
        user_id = sph.getInt("user_id", 0);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                adolescent_girl_id = bundle.getInt("index");
                name = bundle.getString("name");
                current_date = bundle.getString("currDate");
                spnAdolescentName.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            list = new ArrayList<AdolescentMonitoring>();
            list.clear();
            wvAdlNutritionHistory.loadUrl("about:blank");
            list = sqliteHelper
                    .getAdolescentMonitorDataBy(adolescent_girl_id);

            if (list.get(0) != null) {
                wvAdlNutritionHistory
                        .setVisibility(View.VISIBLE);

            }

            getData(list);

        } catch (Exception e) {
            // TODO Auto-generated catch block

            wvAdlNutritionHistory.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    public void click_Image(View v) {
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
                        bitmap = imageLatLogHeper.compressImage(file + "", name);
                        imgAdolscentMon.setImageBitmap(bitmap);
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

                            try {

                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", name);
                                imgAdolscentMon.setImageBitmap(bitmap);

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

    private void getData(ArrayList<AdolescentMonitoring> list2) {
        // TODO Auto-generated method stub
        strData = " ";
        for (int i = 0; i < list.size(); i++) {
            adolescentMonitoring = list.get(i);
            adolescentMonitoring.getAdolescentWeight();
            adolescentMonitoring.getAdolescentHeight();
            adolescentMonitoring.getAdolescentHb();
            adolescentMonitoring.getDateOfRecord();
            String adol_BMI = "NA";
            try {
                if (!adolescentMonitoring.getAdolescentWeight().equalsIgnoreCase("")) {
                    double height = Double.parseDouble(adolescentMonitoring.getAdolescentHeight()) / 100;
                    double sq_height = height * height;
                    double bmi = Double.parseDouble(adolescentMonitoring.getAdolescentWeight()) / sq_height;
                    adol_BMI = String.valueOf((int) bmi);
                }
            } catch (Exception e) {
            }

            strData = strData + "<tr>" + "<td>"
                    + adolescentMonitoring.getDateOfRecord() + "</td>" + "<td>"
                    + adolescentMonitoring.getAdolescentWeight() + "</td>"
                    + "<td>" + adolescentMonitoring.getAdolescentHeight()
                    + "</td>" + "<td>" + adolescentMonitoring.getAdolescentHb()
                    + "</td>" + "<td>" + adol_BMI
                    + "</td>" + "</tr>";

        }
        showWebView(strData);
    }

    private void showWebView(String data2) {
        // TODO Auto-generated method stub

        String[] util = Utility.split(strAdolescentWeight);
        strAdolescentWeight = util[0];
        util = Utility.split(strAdolescentHeight);
        strAdolescentHeight = util[0];
        util = Utility.split(strAdolescentHb);
        strAdolescentHb = util[0];

        String myvar = "<html>" + "" + "" + "<script type=\"text/javascript\">"
                + "function altRows(id){"
                + "	if(document.getElementsByTagName){  " + "		"
                + "		var table = document.getElementById(id);  "
                + "		var rows = table.getElementsByTagName(\"tr\"); " + "		 "
                + "		for(i = 0; i < rows.length; i++){          "
                + "			if(i % 2 == 0){"
                + "				rows[i].className = \"evenrowcolor\";" + "			}else{"
                + "				rows[i].className = \"oddrowcolor\";" + "			}      "
                + "		}" + "	}" + "}" + "window.onload=function(){"
                + "	altRows('alternatecolor');" + "}" + "</script>" + "" + ""
                + "" + "" + "<style type=\"text/css\">"
                + "table.altrowstable {"
                + "	font-family: verdana,arial,sans-serif;"
                + "	font-size:16px;" + "	color:#333333;"
                + "	border-width: 1px;" + "	border-color: #a9c6c9;"
                + "	border-collapse: collapse;" + "}"
                + "table.altrowstable th {" + "	border-width: 1px;"
                + "	padding: 8px;" + "	border-style: solid;"
                + "  text-align:center;" + "	border-color: #a9c6c9;" + "}"
                + "table.altrowstable td {" + "	border-width: 1px;"
                + "	padding: 8px;" + "	border-style: solid;"
                + "	border-color: #a9c6c9;" + "}" + ".oddrowcolor{"
                + "	background-color:#d4e3e5;" + "}" + ".evenrowcolor{"
                + "	background-color:#c3dde0;" + "}" + "</style>" + "" + ""
                + "" + "" + "<body>" + "" + "" + "" + ""
                + "<table class=\"altrowstable\" id=\"alternatecolor\">" + ""
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + strDateOfRecord
                + "</th>" + "<th>" + strAdolescentWeight + "</th>" + "<th>"
                + strAdolescentHeight + "</th>" + "<th>" + strAdolescentHb
                + "</th>" + "<th>" + strAdolescentBMI
                + "</th>" + "</tr>" +

                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";

        wvAdlNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);

    }

    public void btnSave(View v) {

        try {

            ArrayList<Views> arr = new ArrayList<Views>();

            arr = sqliteHelper.monitoringDateSearch("adolescent_monitoring", "girl_id", Integer.toString(adolescent_girl_id));
            Log.e("....................", Integer.toString(adolescent_girl_id));

            for (int i = 0; i < arr.size(); i++) {

                final Views viewObj = arr.get(i);
                String s = viewObj.getDate();
                String dateOfRecording = fdate;
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                java.util.Date dateDb = null;
                java.util.Date dateInsert = null;
                try {
                    dateDb = form.parse(s);
                    dateInsert = form.parse(dateOfRecording);
                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                c.setTime(dateDb);

                c.setTime(dateInsert);

            }

            adolescentMonitoring.setAdolescentGirlID(adolescent_girl_id);
            adolescentMonitoring.setAdolscentName(name);
            adolescentMonitoring.setAdolescentWeight(etxtAdolescentWeight
                    .getText().toString().trim());
            adolescentMonitoring.setAdolescentHeight(etxtAdolescentHeight
                    .getText().toString().trim());
            adolescentMonitoring.setAdolescentHb(etxtAdolescentHb.getText()
                    .toString().trim());
            if (etxtAdolescentHb.getText().toString().trim().equals("")) {
                adolescentMonitoring.setAdolescentHb("0");
            } else {
                adolescentMonitoring.setAdolescentHb(etxtAdolescentHb.getText().toString().trim());
            }

            Time time = new Time();
            time.setToNow();
            String strDate = fdate + " " + time.hour + ":" + time.minute + ":" + time.second;

            adolescentMonitoring.setDateOfRecord(strDate);


            if (adolescentMonitoring.getAdolscentName().equals("")
                    || adolescentMonitoring.getAdolescentWeight().equals("")
                    || Float.parseFloat(adolescentMonitoring.getAdolescentWeight()) > 90.000
                    || adolescentMonitoring.getAdolescentHeight().equals("")
                    || Float.parseFloat(adolescentMonitoring.getAdolescentHeight()) > 185.00
                    || adolescentMonitoring.getDateOfRecord().equals("")) {

                if (adolescentMonitoring.getAdolscentName().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            strAdolscentName + " " + strMandatory, Toast.LENGTH_LONG).show();

                }
                if (adolescentMonitoring.getAdolescentWeight().equals("")) {
                    String[] util = Utility.split(strAdolescentWeight);
                    String weight = util[0];
                    etxtAdolescentWeight.setError(weight + " " + strMandatory);

                }
                if (Float.parseFloat(adolescentMonitoring.getAdolescentWeight()) > 90.000) {
                    String[] util = Utility.split(strAdolescentWeight);
                    String weight = util[0];
                    etxtAdolescentWeight.setError(weight + " should be less than 90 kg.");
                    etxtAdolescentWeight.setFocusable(true);
                }
                if (adolescentMonitoring.getAdolescentHeight().equals("")) {
                    String[] util = Utility.split(strAdolescentHeight);
                    String height = util[0];
                    etxtAdolescentHeight.setError(height + " " + strMandatory);

                }
                if (Float.parseFloat(adolescentMonitoring.getAdolescentHeight()) > 185.00) {
                    String[] util = Utility.split(strAdolescentHeight);
                    String height = util[0];
                    etxtAdolescentHeight.setError(height + " should be less than 185 cm.");
                    etxtAdolescentHeight.setFocusable(true);
                }

            } else {
                adolescentMonitoring.setImage(image64);

                final long local_pw_id = sqliteHelper.editAbsentadolescentNutrition(adolescentMonitoring, current_date);

                if (local_pw_id > 0) {
                    sqliteHelper.updateViewAbsentStatus(adolescentMonitoring.getAdolescentGirlID(), "adolescent_monitoring");
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");

                    Toast.makeText(getApplicationContext(),
                            strAdolescentGirlMonitor + " " + strDone, Toast.LENGTH_LONG)
                            .show();

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), strTryAgain,
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void setLanguage() {

        strlanguageId = sph.getString("Language", "1");// getting languageId

        strAdolescentGirlMonitor = sqliteHelper.LanguageChanges(
                ConstantValue.LANAdolscentMonitoring, strlanguageId);
        strAdolscentName = sqliteHelper.LanguageChanges(
                ConstantValue.LANAdolescent, strlanguageId);
        strAdolescentWeight = sqliteHelper.LanguageChanges(
                ConstantValue.LANWeight, strlanguageId);
        strAdolescentHeight = sqliteHelper.LanguageChanges(
                ConstantValue.LANHeight, strlanguageId);
        strAdolescentHb = sqliteHelper.LanguageChanges(ConstantValue.LANHB,
                strlanguageId);
        strAdolescentBMI = sqliteHelper.LanguageChanges(ConstantValue.LANBMI,
                strlanguageId);
        strDateOfRecord = sqliteHelper.LanguageChanges(
                ConstantValue.LANRecordDate, strlanguageId);
        strAdolescentGo = sqliteHelper.LanguageChanges(ConstantValue.LANGo,
                strlanguageId);
        strAdlMonSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave,
                strlanguageId);
        strHistory = sqliteHelper.LanguageChanges(ConstantValue.LANHistory,
                strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                strlanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory,
                strlanguageId);
        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone,
                strlanguageId);
        strTryAgain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain,
                strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel,
                strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes,
                strlanguageId);
        strNo = sqliteHelper
                .LanguageChanges(ConstantValue.LANNo, strlanguageId);

        txtAdolescentGirlMonitor.setText(strAdolescentGirlMonitor);
        txtAdolscentName.setText(strAdolscentName);
        txtAdolescentWeight.setText(strAdolescentWeight);
        txtAdolescentHeight.setText(strAdolescentHeight);
        txtAdolescentHb.setText(strAdolescentHb);
        txtDateOfRecord.setText(strDateOfRecord);
        txtPregnantNutritionHistory.setText(strAdolescentGirlMonitor + " "
                + strHistory);
        //btnAdolescentGo.setText(strAdolescentGo);
        btnAdlMonSubmit.setText(strAdlMonSubmit);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        fdate = df.format(c.getTime());
    }

    void initialization() {

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);
        adolescentMonitoring = new AdolescentMonitoring();

        txtAdolescentGirlMonitor = (TextView) findViewById(R.id.txtAdolescentGirlMonitor);
        txtAdolscentName = (TextView) findViewById(R.id.txtAdolscentName);
        txtAdolescentWeight = (TextView) findViewById(R.id.txtAdolescentWeight);
        txtAdolescentHeight = (TextView) findViewById(R.id.txtAdolescentHeight);
        txtAdolescentHb = (TextView) findViewById(R.id.txtAdolescentHb);
        txtDateOfRecord = (TextView) findViewById(R.id.txtDateOfRecord);
        txtPregnantNutritionHistory = (TextView) findViewById(R.id.txtPregnantNutritionHistory);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        // etxtSearchByHhid = (EditText) findViewById(R.id.etxtSearchByHhid);
        etxtAdolescentWeight = (EditText) findViewById(R.id.etxtAdolescentWeight);
        etxtAdolescentHeight = (EditText) findViewById(R.id.etxtAdolescentHeight);
        etxtAdolescentHb = (EditText) findViewById(R.id.etxtAdolescentHb);

        //btnAdolescentGo = (Button) findViewById(R.id.btnAdolescentGo);
        btnAdlMonSubmit = (Button) findViewById(R.id.btnAdlMonSubmit);

        spnAdolescentName = (TextView) findViewById(R.id.spnAdolescentName);
        wvAdlNutritionHistory = (WebView) findViewById(R.id.wvAdlNutritionHistory);

        //rgMGR = (RadioGroup)findViewById(R.id.rgMGR);

        imgAdolscentMon = (ImageView) findViewById(R.id.imgAdolscentMon);

        adolescent = (RadioButton) findViewById(R.id.adolescent);
        married = (RadioButton) findViewById(R.id.married);
        mother = (RadioButton) findViewById(R.id.mother);
        current_status = (RadioGroup) findViewById(R.id.radioGroup);
    }

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new AdolescentMonitoringActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strAdolescentGirlMonitor + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adolescent_monitoring, menu);
        return true;
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
            try {
                String dt = day + "-" + month + "-" + year;
                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());


            } catch (Exception e) {
                Log.d("", "");
            }
        }
    }
}
