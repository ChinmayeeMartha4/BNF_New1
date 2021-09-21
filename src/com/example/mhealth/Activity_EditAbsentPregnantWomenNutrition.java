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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.PregnantWomenMonitor;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
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

public class Activity_EditAbsentPregnantWomenNutrition extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    PregnantWomenMonitor pregnantWomenMonitor;
    EditText etxtWomenWeight, etxtHb, sysetxtBp, diasetxtBp;
    TextView spnPregnantWomen;
    TextView txtPregnentWomenMonitor, txtPregnentWomen, txtFooter, txtPregWomenWeightGain,
            txtWomenWeight, txtHb, txtBp, txtCurrentDate, txtPregnantNutritionHistory, systxtBp, diastxtBp;
    Button btnPreWomenMonitorSubmit;
    /*new code*/
    String strLanguageId, strPregnentWomenMonitor, strPregnentWomen, strPregnantWomenName, strWomenWeight, strHb, strBMI, strBp, strCurrentDate,
            strAdolescentGo, strAdlMonSubmit, strPregnantNutritionHistory, strId, strData, strFooter, strMandatory, strDone, strTryAgain, strCancel, strYes, strNo,
            strsysBp, strdiasBp, strPhotoInsHead, strPhotoIns;
    int intWomenId;
    double height;
    ImageView imgPregWomenMon;
    byte[] image;
    String image64 = "";
    String pregnant_women_id;
    String name;
    String current_date;
    String fdate;
    private ArrayList<PregnantWomenMonitor> list;
    private WebView wvNutritionHistory;
    private PregnantWomenMonitor womenMonitor;
    private int user_id;
    private Bitmap bitmap;
    private String mCurrentPhotoPath = "";
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
        setContentView(R.layout.activity_absent_pregnant_women_monitoring);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthly_Monitoring/ Pregnant Women Monitoring");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        setLanguage();
        uploadAttendance();

        try {
            user_id = sph.getInt("user_id", 0);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                pregnant_women_id = bundle.getString("index");
                name = bundle.getString("name");
                current_date = bundle.getString("currDate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        spnPregnantWomen.setText(name);

        intWomenId = Integer.parseInt(pregnant_women_id);

        try {
            list = new ArrayList<PregnantWomenMonitor>();
            list.clear();
            wvNutritionHistory.loadUrl("about:blank");
            list = sqliteHelper.getPregnentWomenMonitorDataBy(intWomenId);
            height = sqliteHelper.getHeightOFPW(intWomenId) / 100;

            if (list.get(0) != null) {
                wvNutritionHistory.setVisibility(View.VISIBLE);

            }

            getData(list);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            wvNutritionHistory.setVisibility(View.GONE);
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
//                    for (File temp : f.listFiles()) {
//                        if (temp.getName().equals("temp.jpg")) {
//                            f = temp;
//                            break;
//                        }
//                    }
//                    String selectedImagePath = "";
//                    try {
//                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                        //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//                        bitmap = getResizedBitmap(bitmap, 350);
//
//                        imgPregWomenMon.setImageBitmap(bitmap);
//
//                        OutputStream fOut = null;
//
//                        try {
//                            fOut = new FileOutputStream(f);
//                            bitmap.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//                            fOut.flush();
//                            fOut.close();
//                            image64 = encodeTobase64(bitmap);
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
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
                        bitmap = imageLatLogHeper.compressImage(file + "", name);
                        imgPregWomenMon.setImageBitmap(bitmap);
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
//
                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", name);
                                imgPregWomenMon.setImageBitmap(bitmap);

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

    //set language
    void setLanguage() {

        strLanguageId = sph.getString("Language", "1");// getting languageId

        strPregnentWomenMonitor = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomenMonitoring, strLanguageId);
        strPregnentWomen = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomen1, strLanguageId);
        strWomenWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight, strLanguageId);
        strHb = sqliteHelper.LanguageChanges(ConstantValue.LANHB, strLanguageId);
        strBMI = sqliteHelper.LanguageChanges(ConstantValue.LANBMI, strLanguageId);
        strsysBp = sqliteHelper.LanguageChanges(ConstantValue.LANSystolicBp, strLanguageId);
        strdiasBp = sqliteHelper.LanguageChanges(ConstantValue.LANDiastolicBp, strLanguageId);
        strCurrentDate = sqliteHelper.LanguageChanges(ConstantValue.LANRecordDate, strLanguageId);
        strAdolescentGo = sqliteHelper.LanguageChanges(ConstantValue.LANGo, strLanguageId);
        strAdlMonSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave, strLanguageId);
        strPregnantNutritionHistory = sqliteHelper.LanguageChanges(ConstantValue.LANHistory, strLanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strLanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, strLanguageId);
        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone, strLanguageId);
        strTryAgain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strLanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strLanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strLanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strLanguageId);

        strPhotoInsHead = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsHead, strLanguageId);
        strPhotoIns = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsPW, strLanguageId);

        txtPregnentWomenMonitor.setText(strPregnentWomenMonitor);
        txtPregnentWomen.setText(strPregnentWomen);
        txtWomenWeight.setText(strWomenWeight);
        txtHb.setText(strHb);
        systxtBp.setText(strsysBp);
        diastxtBp.setText(strdiasBp);
        txtCurrentDate.setText(strCurrentDate);
        btnPreWomenMonitorSubmit.setText(strAdlMonSubmit);
        txtPregnantNutritionHistory.setText(strPregnentWomenMonitor + " " + strPregnantNutritionHistory);
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

    private void getData(ArrayList<PregnantWomenMonitor> list2) {
        // TODO Auto-generated method stub
        strData = " ";
        double latest_weight = 0;
        for (int i = 0; i < list.size(); i++) {
            womenMonitor = list.get(i);

            womenMonitor.getWeight();

            String bmi = "NA";
            try {
                if (!womenMonitor.getWeight().equalsIgnoreCase("")) {
                    double height_pw = height * height;
                    double bmi_p = Double.parseDouble(womenMonitor.getWeight()) / height_pw;
                    bmi = String.valueOf((int) bmi_p);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

            if (!womenMonitor.getWeight().equalsIgnoreCase("")) {
                try {
                    latest_weight = Double.parseDouble(womenMonitor.getWeight());
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            womenMonitor.getHb();
            /*womenMonitor.getBp();*/
            womenMonitor.getSysbp();
            womenMonitor.getDiasbp();
            womenMonitor.getCurrent_date();


            strData = strData + "<tr>" + "<td>" + womenMonitor.getCurrent_date() + "</td>" +
                    "<td>" + womenMonitor.getWeight() + "</td>"
                    + "<td>" + womenMonitor.getHb() + "</td>"
                    + "<td>" + bmi + "</td>"
                    + "<td>" + womenMonitor.getSysBp() + "</td>"
                    + "<td>" + womenMonitor.getDiasBp() + "</td>"
                    + "</tr>";


        }
        double reg_weight = Double.parseDouble(sqliteHelper.GetOneData("weight", "pregnant_women", "pregnant_women_id = " + intWomenId));
        double weight_diff = latest_weight - reg_weight;
        if (weight_diff > 0) {
            txtPregWomenWeightGain.setText("Weight gain till last monitoring : " + weight_diff + " Kgs");
        }

        showWebView(strData);
    }

    private void showWebView(String data2) {

        String[] util = Utility.split(strWomenWeight);
        strWomenWeight = util[0];
        util = Utility.split(strHb);
        strHb = util[0];

        String myvar = "<html>" +
                "" +
                "" +
                "<script type=\"text/javascript\">" +
                "function altRows(id){" +
                "	if(document.getElementsByTagName){  " +
                "		" +
                "		var table = document.getElementById(id);  " +
                "		var rows = table.getElementsByTagName(\"tr\"); " +
                "		 " +
                "		for(i = 0; i < rows.length; i++){          " +
                "			if(i % 2 == 0){" +
                "				rows[i].className = \"evenrowcolor\";" +
                "			}else{" +
                "				rows[i].className = \"oddrowcolor\";" +
                "			}      " +
                "		}" +
                "	}" +
                "}" +
                "window.onload=function(){" +
                "	altRows('alternatecolor');" +
                "}" +
                "</script>" +
                "" +
                "" +
                "" +
                "" +
                "<style type=\"text/css\">" +
                "table.altrowstable {" +
                "	font-family: verdana,arial,sans-serif;" +
                "	font-size:16px;" +
                "	color:#333333;" +
                "	border-width: 1px;" +
                "	border-color: #a9c6c9;" +
                "	border-collapse: collapse;" +
                "}" +
                "table.altrowstable th {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "  text-align:center;" +
                "	border-color: #a9c6c9;" +
                "}" +
                "table.altrowstable td {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "	border-color: #a9c6c9;" +
                "}" +
                ".oddrowcolor{" +
                "	background-color:#d4e3e5;" +
                "}" +
                ".evenrowcolor{" +
                "	background-color:#c3dde0;" +
                "}" +
                "</style>" +
                "" +
                "" +
                "" +
                "" +
                "<body>" +
                "" +
                "" +
                "" +
                "" +
                "<table class=\"altrowstable\" id=\"alternatecolor\">" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "" +
                "<tr>"
                + "<th>" + strCurrentDate + "</th>" +
                "<th>" + strWomenWeight + "</th>" +
                "<th>" + strHb + "</th>" +
                "<th>" + strBMI + "</th>" +
                /*"<th>"+ strBp+"</th>"+*/
                "<th>" + strsysBp + "</th>" +
                "<th>" + strdiasBp + "</th>" +
                "</tr>" +

                data2 +
                "</table>" +
                "" +
                "" +
                "</body>" +
                "" +
                "</html>";

//        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);

        wvNutritionHistory.getSettings().setAllowContentAccess(true);

//        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);
        wvNutritionHistory.loadDataWithBaseURL("null", myvar, "text/html", "charset=UTF-8", null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_pregnant_women_monitoring,
                menu);
        return true;
    }

    void initialization() {

        sqliteHelper = new SqliteHelper(this);
        pregnantWomenMonitor = new PregnantWomenMonitor();
        sph = new SharedPrefHelper(this);

        txtPregWomenWeightGain = (TextView) findViewById(R.id.txtPregWomenWeightGain);
        txtPregnentWomenMonitor = (TextView) findViewById(R.id.txtPregnentWomenMonitor);
        txtPregnentWomen = (TextView) findViewById(R.id.txtPregnentWomen);
        txtWomenWeight = (TextView) findViewById(R.id.txtWomenWeight);
        txtHb = (TextView) findViewById(R.id.txtHb);
        /*txtBp = (TextView) findViewById(R.id.txtBp);*/
        systxtBp = (TextView) findViewById(R.id.systxtBp);
        diastxtBp = (TextView) findViewById(R.id.diastxtBp);
        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        txtPregnantNutritionHistory = (TextView) findViewById(R.id.txtPregnantNutritionHistory);
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        etxtWomenWeight = (EditText) findViewById(R.id.etxtWomenWeight);
        etxtHb = (EditText) findViewById(R.id.etxtHb);
        /*etxtBp = (EditText) findViewById(R.id.etxtBp);*/
        sysetxtBp = (EditText) findViewById(R.id.sysetxtBp);
        diasetxtBp = (EditText) findViewById(R.id.diasetxtBp);

        spnPregnantWomen = (TextView) findViewById(R.id.spnPregnantWomen);
        btnPreWomenMonitorSubmit = (Button) findViewById(R.id.btnPreWomenMonitorSubmit);


        imgPregWomenMon = (ImageView) findViewById(R.id.imgPregWomenMon);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn // spinner
                // class
                // method....always
                // use
                .getSelectedItemId());
        return data.getValue();
    }

    @SuppressLint("NewApi")

    public void btnSave(View vw) {

        try {

            pregnantWomenMonitor.setWeight(etxtWomenWeight.getText().toString().trim());

            if (etxtHb.getText().toString().trim().equals("")) {
                pregnantWomenMonitor.setHb("0");
            } else {
                pregnantWomenMonitor.setHb(etxtHb.getText().toString().trim());
            }

            /*pregnantWomenMonitor.setBp(etxtBp.getText().toString().trim());*/
            pregnantWomenMonitor.setSysbp(sysetxtBp.getText().toString().trim());
            pregnantWomenMonitor.setDiasbp(diasetxtBp.getText().toString().trim());

            Time time = new Time();
            time.setToNow();

            String strDate = time.hour + ":" + time.minute + ":" + time.second;
            pregnantWomenMonitor.setCurrent_date(fdate + " " + strDate);

            pregnantWomenMonitor.setDate_of_recording(fdate + " " + strDate);

            pregnantWomenMonitor.setPregnant_women_id(pregnant_women_id);
            pregnantWomenMonitor.setPregnant_women_name(name);

            ArrayList<Views> arr = sqliteHelper.monitoringDateSearch("pregnant_womem_monitor", "women_id", pregnant_women_id);

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

            if (pregnantWomenMonitor.getPregnant_women_name().equals("")
                    || etxtWomenWeight.getText().toString().equals("")
                    || Float.parseFloat(etxtWomenWeight.getText().toString()) < 30.000
                    || Float.parseFloat(etxtWomenWeight.getText().toString()) > 120.000
                    /*|| etxtBp.getText().toString().equals("")*/

                    || pregnantWomenMonitor.getSysbp().equals("")
                    || Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.00
                    || Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.00

                    || pregnantWomenMonitor.getDiasbp().equals("")
                    || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.00
                    || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.00) {

                if (pregnantWomenMonitor.getPregnant_women_name().equals("")) {
                    Toast.makeText(getApplicationContext(), strPregnentWomen + " " + strMandatory, Toast.LENGTH_LONG).show();
                }

                if (etxtWomenWeight.getText().toString().equals("")) {

                    String[] util = Utility.split(strWomenWeight);
                    String weight = util[0];
                    etxtWomenWeight.setError(weight + " " + strMandatory);
                }
                if (Float.parseFloat(etxtWomenWeight.getText().toString()) < 30.000) {

                    String[] util = Utility.split(strWomenWeight);
                    String weight = util[0];
                    etxtWomenWeight.setError(weight + " should be greater than 30 kg.");
                    etxtWomenWeight.setFocusable(true);
                }
                if (Float.parseFloat(etxtWomenWeight.getText().toString()) > 120.000) {

                    String[] util = Utility.split(strWomenWeight);
                    String weight = util[0];
                    etxtWomenWeight.setError(weight + " should be less than 120 kg.");
                    etxtWomenWeight.setFocusable(true);
                }

                if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.000) {
                    String[] util = Utility.split(strsysBp);
                    String sysbp = util[0];
                    sysetxtBp.setError(sysbp + " should be greater than 40 .");
                    sysetxtBp.setFocusable(true);
                }
                if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.000) {
                    String[] util = Utility.split(strsysBp);
                    String sysbp = util[0];
                    sysetxtBp.setError(sysbp + " should be less than 200 .");
                    sysetxtBp.setFocusable(true);
                }

                if (pregnantWomenMonitor.getDiasbp().equals(""))
                    diasetxtBp.setError(strdiasBp + " " + strMandatory);

                if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.000) {
                    String[] util = Utility.split(strdiasBp);
                    String diasbp = util[0];
                    diasetxtBp.setError(diasbp + " should be greater than 40 .");
                    diasetxtBp.setFocusable(true);
                }
                if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.000) {
                    String[] util = Utility.split(strdiasBp);
                    String diasbp = util[0];
                    diasetxtBp.setError(diasbp + " should be less than 250 .");
                    diasetxtBp.setFocusable(true);
                }
            } else {

                pregnantWomenMonitor.setImage(image64);
                long id = sqliteHelper.editPregnantWomenNutrition(pregnantWomenMonitor, current_date);
                if (id > 0) {
                    sqliteHelper.updateViewAbsentStatus(Integer.parseInt(pregnantWomenMonitor.getPregnant_women_id()), "pregnant_womem_monitor");
                    etxtWomenWeight.setText("");
                    etxtHb.setText("");
                    /*etxtBp.setText("");*/
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "This month data is already taken.", Toast.LENGTH_LONG).show();
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strCancel + " " + strPregnentWomenMonitor + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new ActivityPregnantWomenMonitoring.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }

    public void helpOnClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(strPhotoInsHead)
                .setMessage(strPhotoIns)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        private String formattedDate;

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
            formattedDate = sdf.format(c.getTime());
        }
    }
}
