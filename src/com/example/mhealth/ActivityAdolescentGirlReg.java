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
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.Adolescent;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActivityAdolescentGirlReg extends Activity implements OnItemSelectedListener {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etxtDateOfBirth, etxtDOB;
    byte[] image;
    String image64 = "";
    ImageView imgAdolscent;
    String gps;
    DatePickerDialog datePickerDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    public static String age_in_month;

    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    Adolescent adolescent;
    long server_pw_id = 0;
    ImageView ivDOB;

    TextView txtAdolescentGirlReg, txtHhId, txtNameOfTheGirl, txtGirlFatherName, txtDateOfBirth, txtGirlHeight, txtGirlWeight,
            txtGirlHb, txtGirlOsp, txtyear, txtmonth, txtFooter,  txtGps;

    EditText etxtNameOfTheGirl, etxtGirlFatherName, etxtGirlHeight, etxtGirlWeight, etxtGirlHb;
    ImageView imgHbStatus;
    CheckBox cbThyroid, cbAsthma, cbBronchiectasis, cbHeartdisease, cbDiabetes, cbGynic, cbOther;
    Spinner spnyear, spnmonth, spnHhId;
    Button btnSave;
    String year[] = {"0", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
    String month[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String strAdolescentGirlReg, strHhId, strNameOfTheGirl, strGirlFatherName, strDateOfBirth, strGirlHeight, strGirlWeight, strCancel,
            strGirlHb, strGirlOsp, stryear, strmonth, strGirlChronic, strSubmit, strlanguageId, strFooter, strYes, strNo,
            strTryagain, strRegDone, strOnlyAlpha, strMandatory, strDone, strThyroid, strAsthma, strBronchiectasis,
            strHeartdisease, strDiabetes, strGynic, strOther, strAge, strGender="", straccessICDS="",strrececiveIFA="",strtakenIFA="",
            stradolescentDewarming="",strhealthService="",strDob="", strLat, strLang , strDOB="";
    private Bitmap bitmap;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    TextView tvTitleText,txtMobileNumber;
    EditText etxtMobileNumber;
    ImageView ivTitleBack;
    RadioGroup rgGender, rgaccessICDS, rgllrececiveIFA, rgtakenIFA, rgadolescentDewarming, rghealthService, rgDOB;
    RadioButton rbMale,rbFemale, rbDobmonth;
    private Context context=this;
    public static android.app.Dialog submit_alert;

    public static EditText etxtDateOfScreening;
    LinearLayout lltakenIFA, lladolescentDewarming;

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
        setContentView(R.layout.activity_adolescent_girl);
        initialization();

        strlanguageId = sph.getString("Language", "1");// getting languageId

        strAdolescentGirlReg = sqliteHelper.LanguageChanges(ConstantValue.LANAdolscentReg, strlanguageId);
        strHhId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId, strlanguageId);
        strNameOfTheGirl = sqliteHelper.LanguageChanges(ConstantValue.LANGirlName, strlanguageId);
        strGirlFatherName = sqliteHelper.LanguageChanges(ConstantValue.LANFatherName, strlanguageId);
        strDateOfBirth = sqliteHelper.LanguageChanges(ConstantValue.LANDateOfBirth, strlanguageId);
        strGirlHeight = sqliteHelper.LanguageChanges(ConstantValue.LANHeight, strlanguageId);
        strGirlWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight, strlanguageId);
        strGirlHb = sqliteHelper.LanguageChanges(ConstantValue.LANHB, strlanguageId);
        strGirlOsp = sqliteHelper.LanguageChanges(ConstantValue.LANOSP, strlanguageId);
        stryear = sqliteHelper.LanguageChanges(ConstantValue.LANYear, strlanguageId);
        strmonth = sqliteHelper.LanguageChanges(ConstantValue.LANMonth, strlanguageId);
        strGirlChronic = sqliteHelper.LanguageChanges(ConstantValue.LANChronic, strlanguageId);
        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strlanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strlanguageId);
        strRegDone = sqliteHelper.LanguageChanges(ConstantValue.LANEFRegDone, strlanguageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, strlanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, strlanguageId);
        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone, strlanguageId);
        strThyroid = sqliteHelper.LanguageChanges(ConstantValue.LANThyroid, strlanguageId);
        strAsthma = sqliteHelper.LanguageChanges(ConstantValue.LANAsthma, strlanguageId);
        strBronchiectasis = sqliteHelper.LanguageChanges(ConstantValue.LANBronchiectasis, strlanguageId);
        strHeartdisease = sqliteHelper.LanguageChanges(ConstantValue.LANCardiac, strlanguageId);
        strDiabetes = sqliteHelper.LanguageChanges(ConstantValue.LANDiabetes, strlanguageId);
        strGynic = sqliteHelper.LanguageChanges(ConstantValue.LANGynic, strlanguageId);
        strOther = sqliteHelper.LanguageChanges(ConstantValue.LANAnyOther, strlanguageId);
        strAge = sqliteHelper.LanguageChanges(ConstantValue.LANAdolAge, strlanguageId);
        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strlanguageId);
        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strlanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strlanguageId);

        txtGps.setText(gps);
        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        txtAdolescentGirlReg.setText(R.string.adolescent_registration);
        txtHhId.setText(R.string.select_a_house_number);
        txtNameOfTheGirl.setText(R.string.name_of_adolescent);
        txtGirlFatherName.setText(strGirlFatherName);
        txtDateOfBirth.setText(strDateOfBirth);
        txtGirlHeight.setText(strGirlHeight);
        txtGirlWeight.setText(strGirlWeight);
        txtGirlHb.setText(R.string.hb_in_gm);
        txtGirlOsp.setText(strGirlOsp);
        txtyear.setText(stryear);
        txtmonth.setText(strmonth);
        cbThyroid.setText(strThyroid);
        cbAsthma.setText(strAsthma);
        cbBronchiectasis.setText(strBronchiectasis);
        cbHeartdisease.setText(strHeartdisease);
        cbDiabetes.setText(strDiabetes);
        cbGynic.setText(strGynic);
        cbOther.setText(strOther);
        tvTitleText.setText(R.string.adolescent_registration);
        txtMobileNumber.setText(R.string.mobile_number);
        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strAdolescentGirlReg + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivityAdolescentGirlReg.this,
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

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); //current Day.

        //txtFooter.setText(strFooter);
        btnSave.setText(strSubmit);
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



        spnyear.setOnItemSelectedListener(this);

        // Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, year);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spnyear.setPrompt("Select Year");
        spnyear.setAdapter(aa);


        spnmonth.setOnItemSelectedListener(this);

        // Creating the ArrayAdapter instance having the country list
        ArrayAdapter a = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, month);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spnmonth.setPrompt("Select month");
        spnmonth.setAdapter(a);


        //radio group for gender
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbMale:
                        strGender="M";
                        break;
                    case R.id.rbFemale:
                        strGender="F";
                        break;
                }
            }
        });

        rgaccessICDS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbaccessICDSYes:
                        straccessICDS="Yes";
                        break;
                    case R.id.rbaccessICDSNo:
                        straccessICDS="No";
                        break;
                }
            }
        });

        rgllrececiveIFA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbllrececiveIFAYes:
                        strrececiveIFA="Yes";
                        lltakenIFA.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbllrececiveIFANo:
                        strrececiveIFA="No";
                        lltakenIFA.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgtakenIFA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbtakenIFAYes:
                        strtakenIFA="Yes";
                        lladolescentDewarming.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbtakenIFANo:
                        strtakenIFA="No";
                        lladolescentDewarming.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgadolescentDewarming.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbadolescentDewarmingYes:
                        stradolescentDewarming="Yes";
                        break;
                    case R.id.rbadolescentDewarmingNo:
                        stradolescentDewarming="No";
                        break;
                }
            }
        });

        rghealthService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbhealthServiceYes:
                        strhealthService="Yes";
                        break;
                    case R.id.rbhealthServiceNo:
                        strhealthService="No";
                        break;
                    case R.id.rbhealthServiceNotNeeded:
                        strhealthService="Not Needed";
                        break;
                }
            }
        });

        ivDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });


        rgDOB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbDobmonth:
                        etxtDOB.setVisibility(View.VISIBLE);
                        etxtDateOfBirth.setVisibility(View.GONE);
                        ivDOB.setVisibility(View.GONE);
                        getDateOfBirthFromAge();
                        strDOB="Age";

                        break;
                    case R.id.rbDob:
                        etxtDateOfBirth.setVisibility(View.VISIBLE);
                        etxtDOB.setVisibility(View.GONE);
                        ivDOB.setVisibility(View.VISIBLE);
                        strDOB="DOB";
                        break;
                }
            }
        });

        etxtGirlHb.addTextChangedListener(new TextWatcher() {
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
                        if(Double.parseDouble(etxtGirlHb.getText().toString().trim()) < 8){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.sev);
                        }
                        if(Double.parseDouble(etxtGirlHb.getText().toString().trim()) >= 8 && Double.parseDouble(etxtGirlHb.getText().toString().trim()) <= 10.9){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.mod);
                        }if(Double.parseDouble(etxtGirlHb.getText().toString().trim()) >= 11 && Double.parseDouble(etxtGirlHb.getText().toString().trim()) <= 11.9){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.light_yellow);
                        }
                        if(Double.parseDouble(etxtGirlHb.getText().toString().trim()) >= 12){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.nor);
                        }

                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void selectDate() {
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mYear = i;
                mMonth = i1;
                mDay = i2;
                Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String dob = sdf1.format(c.getTime());
                String formattedDate = sdf.format(c.getTime());
                etxtDateOfBirth.setText(formattedDate);
                etxtDOB.setText(getAge(dob));
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public static String getAge(String date) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(date);
            dob.setTime(date1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        long d = dob.getTimeInMillis();
        int year = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        int month = 0, totalDaysDifference = 0;
        if (today.get(Calendar.MONTH) >= dob.get(Calendar.MONTH)) {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
        } else {
            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
            month = 12 + month;
            year--;
        }

        if (today.get(Calendar.DAY_OF_MONTH) >= dob.get(Calendar.DAY_OF_MONTH)) {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
        } else {
            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
            totalDaysDifference = 30 + totalDaysDifference;
            month--;
        }
        double age = (year * 12) + month;
        Integer ageInt = (int) age;

        age_in_month = ageInt.toString(); //for months return this.
        int calAge = (Integer.parseInt(age_in_month) / 12); //for years return this.
        return String.valueOf(calAge);
    }


    private void getDateOfBirthFromAge() {
        etxtDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                float aa = 0;
                int year = 0;
                int month = 0;
                String ageee = etxtDOB.getText().toString().trim();
                if (ageee.length() > 0) {
                    if (ageee.contains(".")) {
                        String[] dobDate = ageee.split("\\.");
                        year = Integer.parseInt(dobDate[0]);
                        if (dobDate.length > 1) {
                            month = Integer.parseInt(dobDate[1]);
                        }
                    } else {
                        year = Integer.parseInt(ageee);
                    }
                    if (etxtDOB.getText().toString().trim().equalsIgnoreCase("")) {
                        year = 0;
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - year);
                        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1 - month);
                        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) -30);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd", Locale.getDefault());
                        String formatted = dateFormat.format(calendar.getTime());
                        etxtDateOfBirth.setText(formatted);
                    }
                } else {
                    etxtDateOfBirth.setText("");
                }
            }
        });
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
//
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
//                imgAdolscent.setImageBitmap(bitmap);
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
//
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
                        imgAdolscent.setImageBitmap(bitmap);
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

                                imgAdolscent.setImageBitmap(bitmap);

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

    public void btnSave_Click(View v) {

        if(checkValidation()){

        String familyid="";
        try {
            String strHouseHoldId = (spnHhId.getSelectedItem().toString());
            String[] houseHoldId = strHouseHoldId.split("\\(");
            String hh = houseHoldId[0].trim();

            familyid = getSelectedValue(spnHhId);

             /*String year=spnyear.getSelectedItem().toString().trim();
             String month=spnmonth.getSelectedItem().toString().trim();
             String osp=year+month; */


            String condition = "";
            if (cbThyroid.isChecked()) {
                //condition = condition + "," + cbThyroid.getText();
                condition = cbThyroid.getText().toString();
            }
            if (cbAsthma.isChecked()) {
                condition = condition + "," +  cbAsthma.getText().toString();
            }
            if (cbHeartdisease.isChecked()) {
                condition = condition + "," + cbHeartdisease.getText().toString();
            }
            if (cbBronchiectasis.isChecked()) {
                condition = condition + "," + cbBronchiectasis.getText().toString();
            }
//            if (cbDiabetes.isChecked()) {
//                condition = condition + "," + cbAsthma.getText().toString();
//            }
            if (cbGynic.isChecked()) {
                condition = condition + "," + cbGynic.getText().toString();
            }
            if (cbOther.isChecked()) {
                condition = condition + "," + cbOther.getText().toString();
            }




            adolescent.setChronicDisease(condition);

            adolescent.setHhId(hh);
            adolescent.setParent_id(familyid);
            adolescent.setNameOfTheGirl(etxtNameOfTheGirl.getText().toString().trim());
            adolescent.setGirlFatherName(etxtGirlFatherName.getText().toString().trim());
            adolescent.setMobile_number(etxtMobileNumber.getText().toString().trim());
            adolescent.setGender(strGender);
            adolescent.setIs_adolescent_access_icds(straccessICDS);
            adolescent.setIs_adolescent_rececive_ifa(strrececiveIFA);
            adolescent.setIs_taken_ifa(strtakenIFA);
            adolescent.setIs_adolescent_dewarming_tablet(stradolescentDewarming);
            adolescent.setIs_health_service(strhealthService);
            adolescent.setAdolescent_age(etxtDOB.getText().toString().trim());
            adolescent.setImage(image64);
            adolescent.setDateOfBirth(etxtDateOfBirth.getText().toString().trim());
            adolescent.setGirlHeight(etxtGirlHeight.getText().toString().trim());
            adolescent.setGirlWeight(etxtGirlWeight.getText().toString().trim());
            if (etxtGirlHb.getText().toString().trim().equals("")) {
                adolescent.setGirlHb("0");
            } else {
                adolescent.setGirlHb(etxtGirlHb.getText().toString().trim());
            }
            adolescent.setospYear(spnyear.getSelectedItem().toString().trim());
            adolescent.setospMonth(spnmonth.getSelectedItem().toString().trim());
            adolescent.setOSP(spnyear.getSelectedItem().toString().trim() + " years " + spnmonth.getSelectedItem().toString().trim() + " months");
            adolescent.setMobile_unique_id(CommonMethods.getUUID());
            adolescent.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
            adolescent.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
//            adolescent.setChronic_disease(strThyroid + "," + str);

            int age_AG = 150;
            try {
                String cBirth = etxtDateOfBirth.getText().toString().trim();
                Calendar cal5 = Calendar.getInstance();
                int m1 = (Integer.parseInt(cBirth.substring(0, 4)) * 12) + Integer.parseInt(cBirth.substring(5, 7));
                int m2 = (cal5.get(Calendar.YEAR) * 12) + cal5.get(Calendar.MONTH) + 1;
                age_AG = m2 - m1;
            } catch (Exception e) {
                age_AG = 150;
            }

            if (adolescent.getNameOfTheGirl().equals("")
                    || adolescent.getGirlFatherName().equals("")
                    //|| adolescent.getDateOfBirth().equals("")
                    //|| age_AG < 120
                    //|| age_AG > 228
//                    || (!adolescent.getGirlHeight().equals("") && Float.parseFloat(adolescent.getGirlHeight()) > 185.00)
//                    || (!adolescent.getGirlWeight().equals("") && Float.parseFloat(adolescent.getGirlWeight()) > 90.000)
            )
            {

                /*if (age_AG < 120) {
                    etxtDateOfBirth.setError("" + strAge);
                    Toast.makeText(getApplicationContext(), strAge, Toast.LENGTH_LONG).show();
                }

                if (age_AG > 228) {
                    etxtDateOfBirth.setError("" + strAge);
                    Toast.makeText(getApplicationContext(), strAge, Toast.LENGTH_LONG).show();
                }*/

//                if (adolescent.getHhId().equals("")) {
//                    Toast.makeText(getApplicationContext(), strHhId + " " + strMandatory, 200).show();
//                }
                if (adolescent.getNameOfTheGirl().equals("")) {
                    etxtNameOfTheGirl.setError(strNameOfTheGirl + " " + strMandatory);
                }
                if (adolescent.getGirlFatherName().equals("")) {
                    etxtGirlFatherName.setError(strGirlFatherName + " " + strMandatory);
                }

//                if (adolescent.getDateOfBirth().equals("")) {
//                    etxtDateOfBirth.setError("Age" + " " + strMandatory);
//                }




//                if(strDateOfBirth.equals("")){
//                    Toast.makeText(context, "Please choose Age or DOB.", Toast.LENGTH_SHORT).show();
//                }


//                if (adolescent.getDateOfBirth().equals("")) {
//                    etxtDateOfBirth.setError(strDateOfBirth + " " + strMandatory);
//                }



//                if (rbDobmonth.isChecked()) {
//                    if (etxtDOB.getText().toString().trim().length() <=0) {
//                        EditText flagEditfield = etxtDOB;
//                        String msg = getString(R.string.Please_enter_valid_agemonth);
//                        etxtDOB.setError(msg);
//                        etxtDOB.requestFocus();
//
//                    }
//
//                } else {
//                    if (etxtDateOfBirth.getText().toString().trim().length() <= 0) {
//                        EditText flagEditfield = etxtDateOfBirth;
//                        String msg = getString(R.string.Please_enter_valid_dob);
//                        etxtDateOfBirth.setError(msg);
//                        etxtDateOfBirth.requestFocus();
//                    }
//                }
                /*if (adolescent.getGirlHeight().equals("")) {
                    String[] util = Utility.split(strGirlHeight);
                    String height = util[0];
                    etxtGirlHeight.setError(height + " " + strMandatory);
                }*/
//                if (!adolescent.getGirlHeight().equals("") && Float.parseFloat(adolescent.getGirlHeight()) > 185.00) {
//                    String[] util = Utility.split(strGirlHeight);
//                    String height = util[0];
//                    etxtGirlHeight.setError(height + " should be less than 185 cm.");
//                    etxtGirlHeight.setFocusable(true);
//                }

                /*if (adolescent.getGirlWeight().equals("")) {
                    String[] util = Utility.split(strGirlWeight);
                    String weight = util[0];
                    etxtGirlWeight.setError(weight + " " + strMandatory);
                }*/
//                if (!adolescent.getGirlWeight().equals("") && Float.parseFloat(adolescent.getGirlWeight()) > 90.000) {
//                    String[] util = Utility.split(strGirlWeight);
//                    String weight = util[0];
//                    etxtGirlWeight.setError(weight + " should be less than 90 kg.");
//                    etxtGirlWeight.setFocusable(true);
//                }
            } else if (!adolescent.getNameOfTheGirl().toString().trim().matches("[a-zA-Z ]+")) {
                etxtNameOfTheGirl.requestFocus();
                etxtNameOfTheGirl.setError(strOnlyAlpha);
            } else if (!adolescent.getGirlFatherName().toString().trim().matches("[a-zA-Z ]+")) {
                etxtGirlFatherName.requestFocus();
                etxtGirlFatherName.setError(strOnlyAlpha);
            }
            else if (spnHhId.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), R.string.select_a_house_number, Toast.LENGTH_LONG).show();
            }
            else {
                etxtNameOfTheGirl.setError(null);
                etxtGirlFatherName.setError(null);
                etxtGirlHb.setError(null);
                final long local_pw_id = sqliteHelper.AdolescentRegistration(adolescent, image64);
                if (local_pw_id > 0) {
                    showSubmitDialog(context, getString(R.string.adolescent_registration),
                            getString(R.string.adolescent_registration_success_message));
                } else {
                    Toast.makeText(getApplicationContext(), strTryagain, Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

    private boolean checkValidation() {
        if (!etxtMobileNumber.getText().toString().trim().equals("")) {
            if (etxtMobileNumber.getText().toString().trim().length() < 10) {
                etxtMobileNumber.requestFocus();
                etxtMobileNumber.setError(getString(R.string.please_enter_valid_mobile_number));
                return false;
            }
        }
        if (!etxtGirlHb.getText().toString().trim().equals("")) {
            if (Double.parseDouble(etxtGirlHb.getText().toString().trim()) < 2) {
                etxtGirlHb.requestFocus();
                etxtGirlHb.setError(getString(R.string.hb_should_greter_than_2));
                return false;
            }else if (Double.parseDouble(etxtGirlHb.getText().toString().trim()) > 20) {
                etxtGirlHb.requestFocus();
                etxtGirlHb.setError(getString(R.string.hb_should_less_20));
                return false;
            }
        }
        if(rgDOB.getCheckedRadioButtonId()==-1){
            Toast.makeText(context, R.string.please_choose_age_dob,Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (etxtDOB.getText().toString().trim().length() == 0) {
                String msg = getString(R.string.age_mandatory);
                Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
                etxtDOB.setError(msg);
                etxtDOB.requestFocus();
                return false;
            } else if (Integer.parseInt(etxtDOB.getText().toString().trim()) < 6) {
                String msg = getString(R.string.age_greater_10);
                Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
                etxtDOB.setError(msg);
                etxtDOB.requestFocus();
                return false;
            } else if (Integer.parseInt(etxtDOB.getText().toString().trim()) > 19) {
                String msg = getString(R.string.age_should_10_19);
                Toast.makeText(context, msg,Toast.LENGTH_LONG).show();
                etxtDOB.setError(msg);
                etxtDOB.requestFocus();
                return false;
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

    public void initialization() {

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);
        adolescent = new Adolescent();

        spnHhId = (Spinner) findViewById(R.id.spnHhId);
        spnyear = (Spinner) findViewById(R.id.spnyear);
        spnmonth = (Spinner) findViewById(R.id.spnmonth);
        etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
        txtAdolescentGirlReg = (TextView) findViewById(R.id.txtAdolescentGirlReg);
        txtHhId = (TextView) findViewById(R.id.txtHhId);
        txtNameOfTheGirl = (TextView) findViewById(R.id.txtNameOfTheGirl);
        txtGirlFatherName = (TextView) findViewById(R.id.txtGirlFatherName);
        txtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        txtGirlHeight = (TextView) findViewById(R.id.txtGirlHeight);
        txtGirlWeight = (TextView) findViewById(R.id.txtGirlWeight);
        txtGirlHb = (TextView) findViewById(R.id.txtGirlHb);
        txtGirlOsp = (TextView) findViewById(R.id.txtGirlOsp);
        txtyear = (TextView) findViewById(R.id.txtyear);
        txtmonth = (TextView) findViewById(R.id.txtmonth);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        txtGps = (TextView) findViewById(R.id.txtGps);

        lltakenIFA= findViewById(R.id.lltakenIFA);
        lladolescentDewarming= findViewById(R.id.lladolescentDewarming);


        etxtNameOfTheGirl = (EditText) findViewById(R.id.etxtNameOfTheGirl);
        etxtGirlFatherName = (EditText) findViewById(R.id.etxtGirlFatherName);
        etxtDateOfBirth = (EditText) findViewById(R.id.etxtDateOfBirth);
        etxtDOB = (EditText) findViewById(R.id.etxtDOB);
        etxtGirlHeight = (EditText) findViewById(R.id.etxtGirlHeight);
        etxtGirlWeight = (EditText) findViewById(R.id.etxtGirlWeight);
        etxtGirlHb = (EditText) findViewById(R.id.etxtGirlHb);
        imgHbStatus=findViewById(R.id.imgHbStatus);
        cbThyroid = (CheckBox) findViewById(R.id.cbThyroid);
        cbAsthma = (CheckBox) findViewById(R.id.cbAsthma);
        cbBronchiectasis = (CheckBox) findViewById(R.id.cbBronchiectasis);
        cbHeartdisease = (CheckBox) findViewById(R.id.cbHeartdisease);
        cbDiabetes = (CheckBox) findViewById(R.id.cbDiabetes);
        cbGynic = (CheckBox) findViewById(R.id.cbGynic);
        cbOther = (CheckBox) findViewById(R.id.cbOther);

        btnSave = (Button) findViewById(R.id.btnSave);

        imgAdolscent = (ImageView) findViewById(R.id.imgAdolscent);
        tvTitleText=findViewById(R.id.tvTitleText);
        txtMobileNumber=findViewById(R.id.txtMobileNumber);
        etxtMobileNumber=findViewById(R.id.etxtMobileNumber);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        ivDOB=findViewById(R.id.ivDOB);
        rgGender=findViewById(R.id.rgGender);
        rbMale=findViewById(R.id.rbMale);
        rbFemale=findViewById(R.id.rbFemale);
        rbDobmonth=findViewById(R.id.rbDobmonth);
        rgaccessICDS=findViewById(R.id.rgaccessICDS);
        rgllrececiveIFA=findViewById(R.id.rgllrececiveIFA);
        rgtakenIFA=findViewById(R.id.rgtakenIFA);
        rgadolescentDewarming=findViewById(R.id.rgadolescentDewarming);
        rghealthService=findViewById(R.id.rghealthService);
        rgDOB=findViewById(R.id.rgDOB);
    }

    public void populateHHList(Spinner spinner, String tableName, String col_id,
                               String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerHHID(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityAdolescentGirlReg.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(getString(R.string.house_hold));
        spinner.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @SuppressLint("NewApi")
    public void show_callender1(View vw) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adolescent_girl, menu);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strCancel + " " + strAdolescentGirlReg + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(
                        ActivityAdolescentGirlReg.this,
                        MainMenuRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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

                /*
                 * Calendar c = Calendar.getInstance(); c.set(year, month, day,
                 * 0, 0); SimpleDateFormat sdf = new
                 * SimpleDateFormat("dd/MM/yyyy");
                 */

                etxtDateOfBirth.setText(formattedDate);


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