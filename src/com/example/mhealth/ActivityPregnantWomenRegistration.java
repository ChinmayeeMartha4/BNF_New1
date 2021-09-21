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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.utils.AlertDialogClass;
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
import java.util.Locale;

public class ActivityPregnantWomenRegistration extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etxtLmpDate, etxtEdd;
    ImageView imgPW;
    byte[] image;
    String image64 = "";
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    PregnantWomen pregnantWomen;
    long server_pw_id = 0;
    String gps;
    RadioGroup radiogroupregisteredICDS, radiogroupsupplementsICDS, rgSelectWhatYouKnow;
    RadioButton radioregisteredICDSYes,radioregisteredICDSNo,radioRegular,radioIrregular,radioNottaken,
            rbCompletedPregnancyMonth,rbLMPEDDDate;
    LinearLayout llCompletedPregnancyMonths, llLMPDate, llEDDDate;
    private String strRegisteredICDS="",strSupplementsICDS="";
    Spinner spnHhId;
    Button btnWomenRegSubmit;
    TextView txtPregnantWomenReg, txtHHID, tv_complete_month_pregnanacy, txtPreWomenName, txtHusbandName, txtPWAge,
            txtLmpDate, txtEdd, txtHeight, txtWeight, txtHb, txtBp, systxtBp, diastxtBp, txtFooter, txtGps, txtEducation, txtmuac;
    EditText etxtPreWomenName, etxtHusbandName, etxtPWAge;

    EditText etxtHeight;
    EditText etxtWeight;
    EditText etxtHB;
    ImageView imgHbStatus;
    EditText etxtBp, sysetxtBp, diasetxtBp,etxtMUAC;
    String strpregWomenReg, strHhId, strPreWomenName, strHusbandName, strPWAge,
            strLmpDate, strEdd, strHeight, strWeight, strHb, strBp, strsysBp, strdiasBp, strSubmit,
            strlanguageId, strYes, strNo, strTryagain, strFooter, strCancel,
            strRegDone, strMandatory, strPregWomReg, strValidAadhar, strOnlyAlpha, strPhotoInsHead,streducation,
            strPhotoIns, strLat, strLang, strPregnancyMonths="";
    private Bitmap bitmap;
    private long local_pw_id;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    private Context context=this;
    public static EditText etxtDateOfScreening;
    Spinner spnMonthsOfPregnancy,spnEducation;

    String[] monthsOfPregnancyAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] educationAL = {"Please Select","Never gone to school", "Primary 1 to 5", "Upper Primary 6 to 8",
            "Secondary 9 to 10", "Higher Secondary 11 to 12", "Graduate", "Post Graduate and Above"};
    String[] educationHindiAL = {"कृपया चयन कीजिए", "कभी स्कूल नहीं गया", "प्राथमिक 1 से 5", "उच्च प्राथमिक 6 से 8",
            "माध्यमिक 9 से 10", "हायर सेकेंडरी 11 से 12", "स्नातक", "स्नातकोत्तर और उससे ऊपर"};
    public static android.app.Dialog submit_alert;

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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_pregnant_women_registration);

        initialize();
        uploadAttendance();
        setMonthsOfPregnancySpinner();
        setEducationSpinner();

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }

        strlanguageId = sph.getString("Language", "");// getting languageId

        txtmuac.setText(R.string.muac_mid_upper_arm_circumference_in_cms);
            rbCompletedPregnancyMonth.setText(R.string.completed_pregnancy_months);
            rbLMPEDDDate.setText(R.string.lmp_edd_date);
            txtHHID.setText(R.string.select_a_house_number);
            txtPreWomenName.setText(R.string.name_of_pregnant_wamen);
            tv_complete_month_pregnanacy.setText(R.string.completed_months_of_pregnancy);


        strpregWomenReg = sqliteHelper.LanguageChanges(
                ConstantValue.LANPregWomenRes, strlanguageId);
        strHhId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId,
                strlanguageId);
        strPreWomenName = sqliteHelper.LanguageChanges(
                ConstantValue.LANpreWomenName, strlanguageId);
        strHusbandName = sqliteHelper.LanguageChanges(
                ConstantValue.LANHusbandName, strlanguageId);
        strPWAge = sqliteHelper.LanguageChanges(
                ConstantValue.LANAge, strlanguageId);
        strLmpDate = sqliteHelper.LanguageChanges(ConstantValue.LANLMP,
                strlanguageId);
        strEdd = sqliteHelper.LanguageChanges(ConstantValue.LANEDD,
                strlanguageId);
        strHeight = sqliteHelper.LanguageChanges(ConstantValue.LANHeight,
                strlanguageId);
        strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,
                strlanguageId);
        strHb = sqliteHelper
                .LanguageChanges(ConstantValue.LANHB, strlanguageId);
      /*strBp = sqliteHelper.LanguageChanges(ConstantValue.LANBloddPressure,
            strlanguageId);*/
        strsysBp = sqliteHelper.LanguageChanges(ConstantValue.LANSystolicBpreg, strlanguageId);
        strdiasBp = sqliteHelper.LanguageChanges(ConstantValue.LANDiastolicBpreg, strlanguageId);


        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave,
                strlanguageId);
        strPregWomReg = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomen, strlanguageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strlanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        strRegDone = sqliteHelper.LanguageChanges(ConstantValue.LANPWRegDone, strlanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, strlanguageId);
        streducation = sqliteHelper.LanguageChanges(ConstantValue.LANEducation, strlanguageId);
        //strValidAadhar = sqliteHelper.LanguageChanges(ConstantValue.LANValidAadhar, strlanguageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, strlanguageId);

        strPhotoInsHead = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsHead, strlanguageId);
        strPhotoIns = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsPW, strlanguageId);

        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strlanguageId);
        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strlanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strlanguageId);

        txtGps.setText(gps);
        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        txtPregnantWomenReg.setText(strpregWomenReg);
        txtHusbandName.setText(strHusbandName);
        txtPWAge.setText(strPWAge);
        txtLmpDate.setText(strLmpDate);
        txtEdd.setText(strEdd);
        txtHeight.setText(strHeight);
        txtWeight.setText(strWeight);
        txtEducation.setText(streducation);
        txtHb.setText(strHb);
        /*txtBp.setText(strBp);*/
        systxtBp.setText(strsysBp);
        diastxtBp.setText(strdiasBp);

        //txtFooter.setText(strFooter);
        btnWomenRegSubmit.setText(strSubmit);

        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);

        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());


        int user_id = sph.getInt("user_id", 0);

        populateHHList(spnHhId, "eligible_family", "familyid", "house_number",
                getString(R.string.select_household_number), "where anganwadi_center_id=" + user_id);


        //lmp info click
        txtLmpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass.showDialog(context,"LMP Information", "Content to be provide...");
            }
        });

        //Radio what you know click
        rgSelectWhatYouKnow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbCompletedPregnancyMonth:
                        llCompletedPregnancyMonths.setVisibility(View.VISIBLE);
                        llLMPDate.setVisibility(View.GONE);
                        llEDDDate.setVisibility(View.GONE);
                        strPregnancyMonths="Completed Pregnancy Months";
                        break;
                    case R.id.rbLMPEDDDate:
                        llCompletedPregnancyMonths.setVisibility(View.GONE);
                        llLMPDate.setVisibility(View.VISIBLE);
                        llEDDDate.setVisibility(View.VISIBLE);
                        strPregnancyMonths="LMP-EDD";
                        break;
                }
            }
        });

        etxtHB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!editable.toString().equalsIgnoreCase("")) {
                        if(Double.parseDouble(etxtHB.getText().toString().trim()) < 7){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.sev);
                        }
                        if(Double.parseDouble(etxtHB.getText().toString().trim()) >= 7/* || Integer.parseInt(etxtHB.getText().toString().trim()) <= 9.9*/){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.mod);
                        }
                        if(Double.parseDouble(etxtHB.getText().toString().trim()) >= 10/* || Integer.parseInt(etxtHB.getText().toString().trim()) <=10.9*/){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.light_yellow);
                        }
                        if(Double.parseDouble(etxtHB.getText().toString().trim()) >= 11){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.nor);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        //registred ICDS click
        radiogroupregisteredICDS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioregisteredICDSYes:
                        strRegisteredICDS="Yes";
                        break;
                    case R.id.radioregisteredICDSNo:
                        strRegisteredICDS="No";
                        break;
                }
            }
        });
        radiogroupsupplementsICDS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioRegular:
                        strSupplementsICDS="Regular";
                        break;
                    case R.id.radioIrregular:
                        strSupplementsICDS="Irregular";
                        break;
                    case R.id.radioNottaken:
                        strSupplementsICDS="Not taken";
                        break;
                }
            }
        });



    }
    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                /*Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Resources resources = getBaseContext().getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());*/

                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                Locale current = getBaseContext().getResources().getConfiguration().locale;
                String lang1=current.getLanguage();
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Locale currentn = getBaseContext().getResources().getConfiguration().locale;
                String lang=currentn.getLanguage();
                Log.e("Lang>>>","lang1>>>"+lang1+">>>lang>>>"+lang);
            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }

    private void setEducationSpinner() {
        String languageId = sph.getString("Language", "");// getting languageId

        if(languageId.equals("1")) {

        ArrayAdapter adapterEducation = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, educationAL);
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEducation.setPrompt("Select Education");
        spnEducation.setAdapter(adapterEducation);

    }else if(languageId.equals("2")) {

        ArrayAdapter adapterEducation = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, educationHindiAL);
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEducation.setPrompt("शिक्षा का चयन करें");
        spnEducation.setAdapter(adapterEducation);
            if(spnEducation.getSelectedItem().toString().trim().equals("Please Select")){
                int spinnerPosition = adapterEducation.getPosition("");
                spnEducation.setSelection(spinnerPosition);
            }
         }
    }

    private void setMonthsOfPregnancySpinner() {
        ArrayAdapter adapterMOP = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, monthsOfPregnancyAL);
        adapterMOP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonthsOfPregnancy.setPrompt(getString(R.string.select_month_of_pregnancy));
        spnMonthsOfPregnancy.setAdapter(adapterMOP);

        spnMonthsOfPregnancy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //calculate date here according to selected months
                int months= Integer.parseInt(spnMonthsOfPregnancy.getSelectedItem().toString().trim());


                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());

                c.add(Calendar.MONTH, -months);
                etxtLmpDate.setText(sdf.format(c.getTime()));
                etxtLmpDate.setError(null);

                String lmp=etxtLmpDate.getText().toString().trim();//2021-05-04
                String year=lmp.substring(0, 4);
                String month=lmp.substring(5, 7);
                String day=lmp.substring(8, 10);
                int year1= Integer.parseInt(year);
                int month1= Integer.parseInt(month);
                int day1= Integer.parseInt(day);

                Calendar c1 = Calendar.getInstance();
                c1.set(year1, month1, day1, 0, 0,0);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate1 = sdf1.format(c.getTime());

                c1.add(Calendar.MONTH, 9);
                etxtEdd.setText(sdf.format(c1.getTime()));
                etxtEdd.setError(null);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
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

    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
    }

    public Location getLatitudeLogitude() {
        GPSTracker gpsTracker = new GPSTracker(this);
        return gpsTracker.getLocation();
    }

// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//    // TODO Auto-generated method stub
//    super.onActivityResult(requestCode, resultCode, data);
//    if (bitmap != null && !bitmap.isRecycled()) {
//       bitmap.recycle();
//       bitmap = null;
//    }
//    try {
//       if (resultCode == RESULT_OK) {
//
//          if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//
//             File f = new File(Environment.getExternalStorageDirectory().toString());
//             Bundle extras = data.getExtras();
//             // Get the returned image from extra
//             bitmap = (Bitmap) extras.get("data");
////               for (File temp : f.listFiles()) {
////                  if (temp.getName().equals("temp.jpg")) {
////                     f = temp;
////                     break;
////                  }
////               }
//             String selectedImagePath = "";
//             try {
////                  BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
////                  bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//                //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//                bitmap = getResizedBitmap(bitmap, 350);
//
//                imgPW.setImageBitmap(bitmap);
//
//                OutputStream fOut = null;
//
//                try {
////                     fOut = new FileOutputStream(f);
////                     bitmap.compress(Bitmap.CompressFormat.PNG, 5, fOut);
////                     fOut.flush();
////                     fOut.close();
//                   image64 = encodeTobase64(bitmap);
//
//                }
////                  catch (FileNotFoundException e) {
////                     e.printStackTrace();
////                  } catch (IOException e) {
////                     e.printStackTrace();
////                  }
//                catch (Exception e) {
//                   e.printStackTrace();
//                }
//             } catch (Exception e) {
//                e.printStackTrace();
//             }
//          }
//       }
//    } catch (Exception e) {
//       e.printStackTrace();
//    }
// }

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
                        imgPW.setImageBitmap(bitmap);
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

                                imgPW.setImageBitmap(bitmap);

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

    public void initialize() {
        try {
            sqliteHelper = new SqliteHelper(this);
            sph = new SharedPrefHelper(this);
            serverhelper = new ServerHelper(this);
            pregnantWomen = new PregnantWomen();

            spnMonthsOfPregnancy=findViewById(R.id.spnMonthsOfPregnancy);
            spnEducation=findViewById(R.id.spnEducation);
            etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
            txtPregnantWomenReg = (TextView) findViewById(R.id.txtPregnantWomenReg);
            txtHHID = (TextView) findViewById(R.id.txtHHID);
            tv_complete_month_pregnanacy = (TextView) findViewById(R.id.tv_complete_month_pregnanacy);
            txtPreWomenName = (TextView) findViewById(R.id.txtPreWomenName);
            txtHusbandName = (TextView) findViewById(R.id.txtHusbandName);
            txtPWAge = (TextView) findViewById(R.id.txtPWAge);
            txtLmpDate = (TextView) findViewById(R.id.txtLmpDate);
            txtEdd = (TextView) findViewById(R.id.txtEdd);
            txtHeight = (TextView) findViewById(R.id.txtHeight);
            txtWeight = (TextView) findViewById(R.id.txtWeight);
            txtEducation = (TextView) findViewById(R.id.txtEducation);
            txtmuac = (TextView) findViewById(R.id.txtmuac);
            txtHb = (TextView) findViewById(R.id.txtHb);
            /*txtBp = (TextView) findViewById(R.id.txtBp);*/
            systxtBp = (TextView) findViewById(R.id.systxtBp);
            diastxtBp = (TextView) findViewById(R.id.diastxtBp);

            txtFooter = (TextView) findViewById(R.id.txtFooter);
            txtGps = (TextView) findViewById(R.id.txtGps);

            etxtPreWomenName = (EditText) findViewById(R.id.etxtPreWomenName);
            etxtHusbandName = (EditText) findViewById(R.id.etxtHusbandName);
            etxtPWAge = (EditText) findViewById(R.id.etxtPWAge);
            etxtLmpDate = (EditText) findViewById(R.id.etxtLmpDate);
            etxtEdd = (EditText) findViewById(R.id.etxtEdd);
            etxtHeight = (EditText) findViewById(R.id.etxtHeight);
            etxtWeight = (EditText) findViewById(R.id.etxtWeight);
            etxtHB = (EditText) findViewById(R.id.etxtHB);
            imgHbStatus=findViewById(R.id.imgHbStatus);
            /*etxtBp = (EditText) findViewById(R.id.etxtBp);*/
            sysetxtBp = (EditText) findViewById(R.id.sysetxtBp);
            diasetxtBp = (EditText) findViewById(R.id.diasetxtBp);
            etxtMUAC=findViewById(R.id.etxtMUAC);
            spnHhId = (Spinner) findViewById(R.id.spnHhId);
            rgSelectWhatYouKnow=findViewById(R.id.rgSelectWhatYouKnow);
            rbCompletedPregnancyMonth=findViewById(R.id.rbCompletedPregnancyMonth);
            rbLMPEDDDate=findViewById(R.id.rbLMPEDDDate);
            llCompletedPregnancyMonths=findViewById(R.id.llCompletedPregnancyMonths);
            llLMPDate=findViewById(R.id.llLMPDate);
            llEDDDate=findViewById(R.id.llEDDDate);
            btnWomenRegSubmit = (Button) findViewById(R.id.btnWomenRegSubmit);
            radiogroupregisteredICDS = (RadioGroup) findViewById(R.id.radiogroupregisteredICDS);
            radiogroupsupplementsICDS = (RadioGroup) findViewById(R.id.radiogroupsupplementsICDS);
            radioregisteredICDSYes=findViewById(R.id.radioregisteredICDSYes);
            radioregisteredICDSNo=findViewById(R.id.radioregisteredICDSNo);
            radioRegular=findViewById(R.id.radioRegular);
            radioIrregular=findViewById(R.id.radioIrregular);
            radioNottaken=findViewById(R.id.radioNottaken);
            imgPW = (ImageView) findViewById(R.id.imgPW);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @SuppressLint("WrongConstant")
    public void btnSave_Click(View v) {
        if (checkValidation()) {
            try {
                pregnantWomen.setPreWomenName(etxtPreWomenName.getText().toString().trim());
                pregnantWomen.setHusbandName(etxtHusbandName.getText().toString().trim());
                pregnantWomen.setAgeofPW(etxtPWAge.getText().toString().trim());
                pregnantWomen.setLmp_date(etxtLmpDate.getText().toString().trim() + " 00:00:00");
                pregnantWomen.setEdd(etxtEdd.getText().toString().trim() + " 00:00:00");
                pregnantWomen.setHeight(etxtHeight.getText().toString().trim());
                pregnantWomen.setWeight(etxtWeight.getText().toString().trim());

            /*String rb1 = "", rb2="";

            rb1 = ((RadioButton) findViewById(radiogroupregisteredICDS.getCheckedRadioButtonId())).getText().toString().trim();
            rb2 = ((RadioButton) findViewById(radiogroupsupplementsICDS.getCheckedRadioButtonId())).getText().toString().trim();*/

                pregnantWomen.setRegistered_icds(strRegisteredICDS);
                pregnantWomen.setSupplements_icds(strSupplementsICDS);

                if (etxtHB.getText().toString().trim().equals("")) {
                    pregnantWomen.setHb("0");
                } else {
                    pregnantWomen.setHb(etxtHB.getText().toString().trim());
                }
                /*pregnantWomen.setBp(etxtBp.getText().toString().trim());*/
                pregnantWomen.setSysbp(sysetxtBp.getText().toString().trim());
                pregnantWomen.setDiasbp(diasetxtBp.getText().toString().trim());
                pregnantWomen.setMobile_unique_id(CommonMethods.getUUID());
                pregnantWomen.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                pregnantWomen.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
                pregnantWomen.setMuac(etxtMUAC.getText().toString().trim());
                if(spnEducation.getSelectedItem().toString().trim().equals("Please Select")){
                    pregnantWomen.setEducation("");

                }else{
                    pregnantWomen.setEducation(spnEducation.getSelectedItem().toString().trim());

                }
                pregnantWomen.setMonths_of_pregnancy(spnMonthsOfPregnancy.getSelectedItem().toString().trim());
                pregnantWomen.setImage(image64);

                String strWomenName = etxtPreWomenName.getText().toString().trim();


                if (spnHhId.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.select_household_number), Toast.LENGTH_LONG).show();
                } else if (strPregnancyMonths.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.LMP_pregnanacy_month, Toast.LENGTH_LONG).show();
                    return;
                } else if (pregnantWomen.getPreWomenName().equals("")
                        || !pregnantWomen.getPreWomenName().matches("[a-zA-Z ]+")
                        //|| pregnantWomen.getLmp_date().equals("")
                        || pregnantWomen.getEdd().equals(" 00:00:00")
                        || pregnantWomen.getHeight().equals("")
                        || pregnantWomen.getAgeofPW().equals("")
                        || Float.parseFloat(pregnantWomen.getAgeofPW()) > 60.00
                        || Float.parseFloat(pregnantWomen.getAgeofPW()) < 10.00
                        || Float.parseFloat(pregnantWomen.getHeight()) < 60.00
                        || Float.parseFloat(pregnantWomen.getHeight()) > 240.00
                        || pregnantWomen.getWeight().equals("")
                        || Float.parseFloat(pregnantWomen.getWeight()) < 10.000
                        || Float.parseFloat(pregnantWomen.getWeight()) > 200.000
                    //|| pregnantWomen.getSysbp().equals("")
                    //|| Float.parseFloat(pregnantWomen.getSysbp()) < 40.00
                    //|| Float.parseFloat(pregnantWomen.getSysbp()) > 200.00
                    //|| pregnantWomen.getDiasbp().equals("")
                    //|| Float.parseFloat(pregnantWomen.getDiasbp()) < 40.00
                    //|| Float.parseFloat(pregnantWomen.getDiasbp()) > 250.00
                    //|| pregnantWomen.getHb().equals("")
                    //|| pregnantWomen.getBp().equals("")
                ) {
                    if (pregnantWomen.getPreWomenName().equals("")) {
                        etxtPreWomenName.setError(strPregWomReg + " " + strMandatory);
                        etxtPreWomenName.setFocusable(true);
                    }
                    if (!pregnantWomen.getPreWomenName().matches("[a-zA-Z ]+")) {
                        etxtPreWomenName.requestFocus();
                        etxtPreWomenName.setError(strOnlyAlpha);
                    }

                /*if (pregnantWomen.getLmp_date().equals(""))
                    etxtLmpDate.setError(strLmpDate + " " + strMandatory);*/

                    if (pregnantWomen.getEdd().equals(" 00:00:00")) {
                    /*etxtEdd.setError(strEdd + " " + strMandatory);
                    etxtEdd.setFocusable(true);*/
                        Toast.makeText(context, strEdd + " " + strMandatory, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pregnantWomen.getAgeofPW().equals("")) {
                        etxtPWAge.setError(strPWAge + " " + strMandatory);
                        etxtPWAge.setFocusable(true);
                    }

                /*if(!pregnantWomen.getAgeofPW().equals("")) {
                    if (Float.parseFloat(pregnantWomen.getAgeofPW()) > 60.00) {
                        etxtPWAge.setError("Please enter valid age");
                        etxtPWAge.setFocusable(true);
                    }

                    if (Float.parseFloat(pregnantWomen.getAgeofPW()) < 10.00) {
                        etxtPWAge.setError("Please enter valid age");
                        etxtPWAge.setFocusable(true);
                    }
                }*/

                    if (pregnantWomen.getHeight().equals("")) {
                        String[] util = Utility.split(strHeight);
                        String height = util[0];
                        etxtHeight.setError(height + " " + strMandatory);
                    }

                    if (Float.parseFloat(pregnantWomen.getHeight()) < 80.00) {
                        String[] util = Utility.split(strHeight);
                        String height = util[0];
                        etxtHeight.setError(height + getString(R.string.should_80));
                        etxtHeight.setFocusable(true);
                    }
                    if (Float.parseFloat(pregnantWomen.getHeight()) > 210.00) {
                        String[] util = Utility.split(strHeight);
                        String height = util[0];
                        etxtHeight.setError(height + getString(R.string.should_210));
                        etxtHeight.setFocusable(true);
                    }

                    if (pregnantWomen.getWeight().equals("")) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + " " + strMandatory);
                    }

                    if (Float.parseFloat(pregnantWomen.getWeight()) < 30.000) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + getString(R.string.should_30));
                        etxtWeight.setFocusable(true);
                    }
                    if (Float.parseFloat(pregnantWomen.getWeight()) > 120.000) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + getString(R.string.should_120));
                        etxtWeight.setFocusable(true);
                    }
                /*if (pregnantWomen.getSysbp().equals(""))
                    sysetxtBp.setError(strsysBp + " " + strMandatory);*/

                /*if (Float.parseFloat(pregnantWomen.getSysbp()) < 40.000) {
                    String[] util = Utility.split(strsysBp);
                    String sysbp = util[0];
                    sysetxtBp.setError(sysbp + " should be greater than 40 .");
                    sysetxtBp.setFocusable(true);
                }
                if (Float.parseFloat(pregnantWomen.getSysbp()) > 200.000) {
                    String[] util = Utility.split(strsysBp);
                    String sysbp = util[0];
                    sysetxtBp.setError(sysbp + " should be less than 200 .");
                    sysetxtBp.setFocusable(true);
                }*/

                 /*if (pregnantWomen.getDiasbp().equals(""))
                    diasetxtBp.setError(strdiasBp+" "+strMandatory);
                 */
                /*if (Float.parseFloat(pregnantWomen.getDiasbp()) < 40.000) {
                    String[] util = Utility.split(strdiasBp);
                    String diasbp = util[0];
                    diasetxtBp.setError(diasbp + " should be greater than 40 .");
                    diasetxtBp.setFocusable(true);
                }
                if (Float.parseFloat(pregnantWomen.getDiasbp()) > 250.000) {
                    String[] util = Utility.split(strdiasBp);
                    String diasbp = util[0];
                    diasetxtBp.setError(diasbp + " should be less than 250 .");
                    diasetxtBp.setFocusable(true);
                }*/

                } else if (!strWomenName.matches("[a-zA-Z ]+")) {
                    etxtPreWomenName.requestFocus();
                    etxtPreWomenName.setError(strOnlyAlpha);
                } else {
                    String familyid = "";
                    String strHouseHoldId = (spnHhId.getSelectedItem().toString());
                    String[] houseHoldId = strHouseHoldId.split("\\(");
                    String hh = houseHoldId[0].trim();

                    familyid = getSelectedValue(spnHhId);
                    pregnantWomen.setHhId(hh);
                    pregnantWomen.setParent_id(Integer.parseInt(familyid));

                    etxtPreWomenName.setError(null);
                    /*etxtBp.setError(null);*/
                    etxtWeight.setError(null);
                    etxtPWAge.setError(null);
                    etxtHeight.setError(null);
                    etxtEdd.setError(null);
                    //etxtLmpDate.setError(null);
                    sysetxtBp.setError(null);
                    diasetxtBp.setError(null);


                    try {
                        pregnantWomen.setFlag("");
                        local_pw_id = sqliteHelper.PregnantWomenRegistration(pregnantWomen, image64);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }

                    etxtPreWomenName.setText("");
                    /*etxtBp.setText("");*/
                    etxtHB.setText("");
                    etxtPWAge.setText("");
                    etxtWeight.setText("");
                    etxtHeight.setText("");
                    etxtEdd.setText("");
                    //etxtLmpDate.setText("");
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    if (local_pw_id > 0) {
                    /*Toast.makeText(getApplicationContext(), strRegDone,
                            200).show();*/

                        showSubmitDialog(context, getString(R.string.pregnant_women_registration),
                                getString(R.string.pregnanat_registration_message));

                    } else {
                        Toast.makeText(getApplicationContext(), strTryagain,
                                Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkValidation(){
        if (!etxtHusbandName.getText().toString().trim().equals("")) {
            if (!etxtHusbandName.getText().toString().trim().matches("[a-zA-Z ]+")) {
                etxtHusbandName.requestFocus();
                etxtHusbandName.setError(strOnlyAlpha);
                return false;
            }
        }

        if (!etxtHB.getText().toString().trim().equals("")) {
            if (Double.parseDouble(etxtHB.getText().toString().trim()) < 2) {
                etxtHB.requestFocus();
                etxtHB.setError(getString(R.string.hb_should_greter_than_2));
                return  false;
            }else if (Double.parseDouble(etxtHB.getText().toString().trim()) > 20) {
                etxtHB.requestFocus();
                etxtHB.setError(getString(R.string.hb_should_less_20));
                return  false;
            }
        }

        return true;
    }

    public static void showSubmitDialog(Context context, String infoTitle, String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submit_alert_dialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvInfoTitle = (TextView) submit_alert.findViewById(R.id.tv_info_title);
        TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvInfoTitle.setText(infoTitle);
        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
                Intent intent1 = new Intent(context, MainMenuRegistrationActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data;
        data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        return data.getValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    public void populateHHList(Spinner spinner, String tableName,
                               String col_id, String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerHHID(tableName, col_id, col_value,
                label, whr);

        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityPregnantWomenRegistration.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setPrompt(label);
        spinner.setPrompt(getString(R.string.household_number));
        spinner.setAdapter(adapter);
    }

    //LMP
    @SuppressLint("NewApi")
    public void show_callender2(View vw) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //Edd
    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //date of screening
    @SuppressLint("NewApi")
    public void show_callender1(View vw) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void ShowMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strpregWomenReg + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(
                        ActivityPregnantWomenRegistration.this,
                        MainMenuRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

            Date today = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(today);
            c1.add( Calendar.MONTH, 9);
            datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                String dt = day + "-" + month + "-" + year;

                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0,0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());

                /*
                 * Calendar c = Calendar.getInstance(); c.set(year, month, day,
                 * 0, 0); SimpleDateFormat sdf = new
                 * SimpleDateFormat("dd/MM/yyyy");
                 */

                etxtEdd.setText(formattedDate);
                etxtLmpDate.setText(formattedDate);
                etxtLmpDate.setError(null);
                c.add(Calendar.MONTH, -9);
                etxtLmpDate.setText(sdf.format(c.getTime()));

                etxtEdd.setError(null);

            } catch (Exception e) {
                Log.d("", "");
            }
        }
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment2 extends DialogFragment implements
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
            /*Date today = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(today);
            c1.add( Calendar.MONTH, 9);
            datePickerDialog.getDatePicker().setMaxDate(c1.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());*/
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                String dt = day + "-" + month + "-" + year;

                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0,0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c.getTime());

                /*
                 * Calendar c = Calendar.getInstance(); c.set(year, month, day,
                 * 0, 0); SimpleDateFormat sdf = new
                 * SimpleDateFormat("dd/MM/yyyy");
                 */

                etxtLmpDate.setText(formattedDate);
                etxtLmpDate.setError(null);
                c.add(Calendar.MONTH, 9);
                etxtEdd.setText(sdf.format(c.getTime()));
                etxtEdd.setError(null);

            } catch (Exception e) {
                Log.d("", "");
            }
        }
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment1 extends DialogFragment implements
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