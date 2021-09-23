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
import android.webkit.WebView;
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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
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
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.helper.Views;
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

public class AdolescentMonitoringActivity extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etxtDateOfRecord;
    static EditText etxtDeathDate;
    static String death_date = "";
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    long server_pw_id = 0;
    AdolescentMonitoring adolescentMonitoring;
    ArrayList<AdolescentMonitoring> list;
    long local_pw_id;
    LinearLayout inr_death_date;
    int checkedStatus = 0;
    TextView txtAdolescentGirlMonitor, txtAdolscentName, txtAdolescentWeight,
            txtAdolescentHeight, txtAdolescentHb, txtDateOfRecord,
            txtPregnantNutritionHistory, txtFooter,  txtGps;
    EditText etxtSearchByHhid, etxtAdolescentWeight, etxtAdolescentHeight,
            etxtAdolescentHb;
    ImageView imgHbStatus;
    RadioGroup rgMGR, current_status,rgIFA, radiogroupPeriods, rgaccessICDS, rgllrececiveIFA, rgtakenIFA, rgadolescentDewarming, rghealthService;
    RadioButton rbMgrStatus, tempMigration, permMigration, adolescent, mother,
            married, rbPresent, rbAbsent, death,rbRegularly,rbNotRegularly,rbNotTaking, radioRegular, radioIrregular;
    CheckBox cbDewormingDone;
    Button btnAdolescentGo, btnAdlMonSubmit;
    Spinner spnAdolescentName;
    WebView wvAdlNutritionHistory;
    int intAdolescentId;

    public static android.app.Dialog submit_alert;

    String strAdolescentGirlMonitor, strAdolscentName, strAdolescentWeight,
            strAdolescentHeight, strAdolescentHb, strAdolescentBMI, strDateOfRecord,
            strAdolescentGo, strAdlMonSubmit, strlanguageId, strId, strName,
            strData, strHistory, strFooter, strMandatory, strYes, strNo,
            strCancel, strTryAgain, strDone, strDeath, strTempMigration,
            strPermMigration, strMarkedLabel, strMother, strMarried, strAdolescentGirl,
            strConsumptionOfIFA="",strDewormingDone="",strLat, strLang, straccessICDS="",strrececiveIFA="",strtakenIFA="",
            stradolescentDewarming="",strhealthService="";
    int intId;
    TextView marked_label;
    ImageView imgAdolscentMon;
    byte[] image;
    String image64 = "";
    int marked_status = 0;
    int already_monitored = 0;
    private int user_id;
    private int flag = 0, flag2 = 0;
    private int migrationflag = 0;
    private Bitmap bitmap;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    String gps;
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
        setContentView(R.layout.activity_adolescent_monitoring);

        /*Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthy_Monitoring/ Adolescent Monitoring");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());*/

        initialization();
        setLanguage();

        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        tvTitleText.setText(R.string.adolescent_girl_monitoring);
        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                                Intent intent = new Intent(
                                        AdolescentMonitoringActivity.this,
                                        MainMenuMonitoringActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        user_id = sph.getInt("user_id", 0);

        etxtSearchByHhid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populateList(spnAdolescentName, "adolescent", "adolescent_id",
                        "adolescent_name", getString(R.string.select_girl), "where anganwadi_center_id=" + user_id);
            }
        });
        populateList(spnAdolescentName, "adolescent", "adolescent_id",
                "adolescent_name", getString(R.string.select_girl), "where anganwadi_center_id=" + user_id);

        spnAdolescentName.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View arg1, int pos, long arg3) {
                if (!spnAdolescentName.getSelectedItem().toString().trim().equalsIgnoreCase("")) {
                    flag = 0;
                    flag2 = 0;
                    already_monitored = 0;
                    spnAdolescentName.getSelectedItem().toString();
                    String adolescent = getSelectedValue(spnAdolescentName);
                    intAdolescentId = Integer.parseInt(adolescent);

                    try {
                        list = new ArrayList<AdolescentMonitoring>();
                        list.clear();
                        wvAdlNutritionHistory.loadUrl("about:blank");
                        list = sqliteHelper
                                .getAdolescentMonitorDataBy(intAdolescentId);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        rgMGR.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tempMigration) {
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");
                    etxtAdolescentWeight.setEnabled(false);
                    etxtAdolescentHeight.setEnabled(false);
                    etxtAdolescentHb.setEnabled(false);
                    etxtAdolescentWeight.setFocusable(false);
                    etxtAdolescentHeight.setFocusable(false);
                    etxtAdolescentHb.setFocusable(false);
                    adolescentMonitoring.setMgrStatus("Temporary Migration");
//                    current_status.setFocusable(false);
                    wvAdlNutritionHistory.setVisibility(View.VISIBLE);
                    inr_death_date.setVisibility(View.GONE);
                  //  imgAdolscentMon.setVisibility(View.GONE);
                 /*   adolescent.setEnabled(true);
                    mother.setEnabled(true);
                    married.setEnabled(true);*/
                    checkedStatus = 1;
                    death_date = "";
//                    rgMGR.clearCheck();
                } else if (checkedId == R.id.permMigration) {
                  /*  tempMigration.setEnabled(true);
                    rbPresent.setEnabled(true);
                    rbAbsent.setEnabled(true);
*/
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");
                    etxtAdolescentWeight.setEnabled(false);
                    etxtAdolescentHeight.setEnabled(false);
                    etxtAdolescentHb.setEnabled(false);
                    etxtAdolescentWeight.setFocusable(false);
                    etxtAdolescentHeight.setFocusable(false);
                    etxtAdolescentHb.setFocusable(false);
//                    rgMGR.setFocusable(false);
//                    current_status.setFocusable(false);
                    imgAdolscentMon.setFocusable(false);
                    wvAdlNutritionHistory.setVisibility(View.INVISIBLE);
                    // current_status.setFocusable(false);
                    adolescentMonitoring.setMgrStatus("Permanent Migration");
                  //  imgAdolscentMon.setVisibility(View.GONE);
                   /* adolescent.setEnabled(true);
                    mother.setEnabled(true);
                    married.setEnabled(true);*/
                    checkedStatus = 1;
                    inr_death_date.setVisibility(View.GONE);
                    death_date = "";
                }

                if (checkedId == R.id.rbAbsent) {
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");
                    etxtAdolescentWeight.setEnabled(false);
                    etxtAdolescentHeight.setEnabled(false);
                    etxtAdolescentHb.setEnabled(false);
                    etxtAdolescentWeight.setFocusable(false);
                    etxtAdolescentHeight.setFocusable(false);
                    etxtAdolescentHb.setFocusable(false);
                    adolescentMonitoring.setMgrStatus("Absent");
                    wvAdlNutritionHistory.setVisibility(View.INVISIBLE);
                    checkedStatus = 1;
                    inr_death_date.setVisibility(View.GONE);
                    imgAdolscentMon.setVisibility(View.GONE);
                    death_date = "";
                    // current_status.setFocusable(false);
                 /*   adolescent.setEnabled(true);
                    mother.setEnabled(true);
                    married.setEnabled(true);*/
                }
                if (checkedId == R.id.rbPresent) {
                    etxtAdolescentWeight.setEnabled(true);
                    etxtAdolescentHeight.setEnabled(true);
                    etxtAdolescentHb.setEnabled(true);
                    etxtAdolescentWeight.setFocusable(true);
                    etxtAdolescentWeight.setFocusableInTouchMode(true);
                    etxtAdolescentHeight.setFocusable(true);
                    etxtAdolescentHeight.setFocusableInTouchMode(true);
                    etxtAdolescentHb.setFocusable(true);
                    etxtAdolescentHb.setFocusableInTouchMode(true);
                    adolescentMonitoring.setMgrStatus("");
                    wvAdlNutritionHistory.setVisibility(View.VISIBLE);
                    checkedStatus = 0;
                    inr_death_date.setVisibility(View.GONE);
                    death_date = "";
                   /* adolescent.setEnabled(true);
                    mother.setEnabled(true);
                    married.setEnabled(true);*/
                }

                if (checkedId == R.id.death) {
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");
                    etxtAdolescentWeight.setEnabled(false);
                    etxtAdolescentHeight.setEnabled(false);
                    etxtAdolescentHb.setEnabled(false);
                    etxtAdolescentWeight.setFocusable(false);
                    etxtAdolescentHeight.setFocusable(false);
                    etxtAdolescentHb.setFocusable(false);
                    adolescentMonitoring.setMgrStatus("Death");
                    wvAdlNutritionHistory.setVisibility(View.INVISIBLE);
                    checkedStatus = 1;
                    imgAdolscentMon.setVisibility(View.GONE);
                }
            }
        });

        current_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.adolescent) {
                    adolescentMonitoring.setMarked_as("Adolescent");
                    etxtAdolescentWeight.setEnabled(true);
                    etxtAdolescentHeight.setEnabled(true);
                    etxtAdolescentHb.setEnabled(true);
                    etxtAdolescentWeight.setFocusable(true);
                    etxtAdolescentWeight.setFocusableInTouchMode(true);
                    etxtAdolescentHeight.setFocusable(true);
                    etxtAdolescentHeight.setFocusableInTouchMode(true);
                    etxtAdolescentHb.setFocusable(true);
                    etxtAdolescentHb.setFocusableInTouchMode(true);
                    wvAdlNutritionHistory.setVisibility(View.VISIBLE);
                    tempMigration.setEnabled(true);
                    permMigration.setEnabled(true);
                    rbPresent.setEnabled(true);
                    rbAbsent.setEnabled(true);
                    rbAbsent.setChecked(false);

                }
                if (i == R.id.mother) {
                    adolescentMonitoring.setMarked_as("Mother");
                    etxtAdolescentWeight.setEnabled(true);
                    etxtAdolescentHeight.setEnabled(true);
                    etxtAdolescentHb.setEnabled(true);
                    etxtAdolescentWeight.setFocusable(true);
                    etxtAdolescentWeight.setFocusableInTouchMode(true);
                    etxtAdolescentHeight.setFocusable(true);
                    etxtAdolescentHeight.setFocusableInTouchMode(true);
                    etxtAdolescentHb.setFocusable(true);
                    etxtAdolescentHb.setFocusableInTouchMode(true);
                    wvAdlNutritionHistory.setVisibility(View.INVISIBLE);
                    tempMigration.setEnabled(true);
                    permMigration.setEnabled(true);
                    rbPresent.setEnabled(true);
                    rbAbsent.setEnabled(true);
                    rbAbsent.setChecked(false);

                }
                if (i == R.id.married) {
                    adolescentMonitoring.setMarked_as("Married");
                    etxtAdolescentWeight.setText("");
                    etxtAdolescentHeight.setText("");
                    etxtAdolescentHb.setText("");
                    etxtAdolescentWeight.setEnabled(false);
                    etxtAdolescentHeight.setEnabled(false);
                    etxtAdolescentHb.setEnabled(false);
                    etxtAdolescentWeight.setFocusable(false);
                    etxtAdolescentHeight.setFocusable(false);
                    etxtAdolescentHb.setFocusable(false);
                    adolescentMonitoring.setMgrStatus("Absent");
                    checkedStatus = 1;
                    current_status.setFocusable(false);
                    wvAdlNutritionHistory.setVisibility(View.INVISIBLE);
                    rgMGR.setEnabled(false);
                    rgMGR.clearCheck();
                    tempMigration.setEnabled(false);
                    permMigration.setEnabled(false);
                    rbPresent.setEnabled(false);
                    rbAbsent.setEnabled(true);
                    rbAbsent.setChecked(true);

                }
            }
        });


        radiogroupPeriods.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioRegular:
                        strConsumptionOfIFA = "Regular";
                        break;
                    case R.id.radioIrregular:
                        strConsumptionOfIFA = "Irregular";
                        break;

                }
            }
        });


        etxtAdolescentHb.addTextChangedListener(new TextWatcher() {
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
                        if(Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) < 8){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.sev);
                        }
                        if(Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 8 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <= 10.9){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.mod);
                        }
                        if(Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 11 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <= 11.9){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.light_yellow);
                        }
                        if(Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 12){
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.nor);
                        }

                    }
                } catch (Exception e) {
                }
            }
        });
        //consumption of IFA tablet
        rgIFA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbRegularly:
                        strConsumptionOfIFA = "Regularly";
                        break;
                    case R.id.rbNotRegularly:
                        strConsumptionOfIFA = "Taking but not Regularly";
                        break;
                    case R.id.rbNotTaking:
                        strConsumptionOfIFA = "Not Taking";
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
                }
            }
        });

        //deworming done
        cbDewormingDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbDewormingDone.isChecked()){
                    strDewormingDone="Yes";
                } else {
                    strDewormingDone="No";
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
                        bitmap = imageLatLogHeper.compressImage(file + "", "");
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
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", "");
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
                + "    if(document.getElementsByTagName){  " + "     "
                + "       var table = document.getElementById(id);  "
                + "       var rows = table.getElementsByTagName(\"tr\"); " + "       "
                + "       for(i = 0; i < rows.length; i++){          "
                + "          if(i % 2 == 0){"
                + "             rows[i].className = \"evenrowcolor\";" + "       }else{"
                + "             rows[i].className = \"oddrowcolor\";" + "        }      "
                + "       }" + " }" + "}" + "window.onload=function(){"
                + "    altRows('alternatecolor');" + "}" + "</script>" + "" + ""
                + "" + "" + "<style type=\"text/css\">"
                + "table.altrowstable {"
                + "    font-family: verdana,arial,sans-serif;"
                + "    font-size:16px;" + "   color:#333333;"
                + "    border-width: 1px;" + "    border-color: #a9c6c9;"
                + "    border-collapse: collapse;" + "}"
                + "table.altrowstable th {" + "    border-width: 1px;"
                + "    padding: 8px;" + " border-style: solid;"
                + "  text-align:center;" + "   border-color: #a9c6c9;" + "}"
                + "table.altrowstable td {" + "    border-width: 1px;"
                + "    padding: 8px;" + " border-style: solid;"
                + "    border-color: #a9c6c9;" + "}" + ".oddrowcolor{"
                + "    background-color:#d4e3e5;" + "}" + ".evenrowcolor{"
                + "    background-color:#c3dde0;" + "}" + "</style>" + "" + ""
                + "" + "" + "<body>" + "" + "" + "" + ""
                + "<table class=\"altrowstable\" id=\"alternatecolor\">" + ""
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + strDateOfRecord
                + "</th>" + "<th>" + strAdolescentWeight + "</th>" + "<th>"
                + strAdolescentHeight + "</th>" + "<th>" + strAdolescentHb
                + "</th>" + "</tr>" +

                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";


        //  wvAdlNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);
        wvAdlNutritionHistory.loadDataWithBaseURL("null", myvar, "text/html", "charset=UTF-8", null);

    }

    public void btnSave(View v) {
        if(checkValidation()) {
            adolescentMonitoring.setDeath_date(death_date);
            adolescentMonitoring.setMobile_unique_id(CommonMethods.getUUID());
            adolescentMonitoring.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
            adolescentMonitoring.setConsumption_of_ifa(strConsumptionOfIFA);
            adolescentMonitoring.setDeworming_done(strDewormingDone);
            adolescentMonitoring.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
            if (spnAdolescentName.getSelectedItemPosition() == -1) {
                Toast.makeText(getApplicationContext(), R.string.please_select_adolescent, Toast.LENGTH_LONG).show();
                return;
            } else {
//                String rb1 = "";
//
//                if (radiogroupPeriods.getCheckedRadioButtonId() == -1) {
//                    Toast.makeText(getApplicationContext(), R.string.please_select_periods, Toast.LENGTH_LONG).show();
//                    return;
//                } else {
//                    rb1 = ((RadioButton) findViewById(radiogroupPeriods.getCheckedRadioButtonId())).getText().toString().trim();
//                    adolescentMonitoring.setPeriods(rb1);
//                }
                try {
                    strName = spnAdolescentName.getSelectedItem().toString().trim();
                    intId = Integer.parseInt(getSelectedValue(spnAdolescentName));

                    ArrayList<Views> arr = new ArrayList<Views>();

                    arr = sqliteHelper.monitoringDateSearch("adolescent_monitoring", "adolescent_id", Integer.toString(intId));
                    Log.e("....................", Integer.toString(intId));

                    for (int i = 0; i < arr.size(); i++) {

                        final Views viewObj = arr.get(i);
                        String s = viewObj.getDate();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        //String dateOfRecording = form.format(c.getTime());
                        String dateOfRecording = etxtDateOfScreening.getText().toString().trim();

                        //String dateOfRecording = etxtDateOfRecord.getText().toString().trim();
                        // Calendar c = Calendar.getInstance();

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
                        int monthDb = c.get(Calendar.MONTH) + 1;
                        int yearDb = c.get(Calendar.YEAR);
                        c.setTime(dateInsert);
                        int monthInsert = c.get(Calendar.MONTH) + 1;
                        int yearInsert = c.get(Calendar.YEAR);

                        if (monthDb == monthInsert && yearDb == yearInsert) {

                   /*Toast.makeText(getApplicationContext(),
                            "This month data already monitored.", 200)
                            .show();*/

                            etxtSearchByHhid.setText("");
                            etxtAdolescentWeight.setText("");
                            etxtAdolescentHeight.setText("");
                            etxtAdolescentHb.setText("");
                            etxtDateOfRecord.setText("");
                            flag = 1;
                            flag2 = 1;
                            already_monitored = 1;
                        }
                    }
                    if (already_monitored == 0 && flag2 == 0) {
                        adolescentMonitoring.setAdolescentGirlID(intId);
                        adolescentMonitoring.setAdolscentName(strName);
                        adolescentMonitoring.setAdolescentWeight(etxtAdolescentWeight
                                .getText().toString().trim());
                        adolescentMonitoring.setAdolescentHeight(etxtAdolescentHeight
                                .getText().toString().trim());
                        adolescentMonitoring.setAdolescentHb(etxtAdolescentHb.getText().toString().trim());
                        adolescentMonitoring.setIs_adolescent_access_icds(straccessICDS);
                        adolescentMonitoring.setIs_adolescent_rececive_ifa(strrececiveIFA);
                        adolescentMonitoring.setIs_taken_ifa(strtakenIFA);
                        adolescentMonitoring.setIs_adolescent_dewarming_tablet(stradolescentDewarming);
                        adolescentMonitoring.setIs_health_service(strhealthService);

                        if (etxtAdolescentHb.getText().toString().trim().equals("")) {
                            adolescentMonitoring.setAdolescentHb("0");
                        } else {
                            adolescentMonitoring.setAdolescentHb(etxtAdolescentHb.getText().toString().trim());
                        }

          /*  Time time = new Time();
            time.setToNow();
            String strDate = etxtDateOfRecord.getText().toString().trim() + " " + time.hour + ":" + time.minute + ":" + time.second;*/
                        if (!etxtDateOfRecord.getText().toString().trim().equalsIgnoreCase(""))
                            adolescentMonitoring.setDateOfRecord(etxtDateOfRecord.getText().toString().trim());

                        if (checkedStatus == 0) {
                            if (adolescentMonitoring.getAdolscentName().equals("")
                                    || (!adolescentMonitoring.getAdolescentWeight().equals("") && Float.parseFloat(adolescentMonitoring.getAdolescentWeight()) > 90.000)
                                    || (!adolescentMonitoring.getAdolescentHeight().equals("") && Float.parseFloat(adolescentMonitoring.getAdolescentHeight()) > 185.00)
                                    || adolescentMonitoring.getDateOfRecord().equals("")) {

                                if (adolescentMonitoring.getAdolscentName().equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            strAdolscentName + " " + strMandatory, Toast.LENGTH_LONG).show();

                                }
                                if (adolescentMonitoring.getMarked_as() == null) {
                                    Toast.makeText(getApplicationContext(),
                                            strAdolscentName + " " + strMandatory, Toast.LENGTH_LONG).show();

                                }
                      /*  if (adolescentMonitoring.getAdolescentWeight().equals("")) {
                            String[] util = Utility.split(strAdolescentWeight);
                            String weight = util[0];
                            etxtAdolescentWeight.setError(weight + " " + strMandatory);

                        }*/
                       /* if (adolescentMonitoring.getAdolescentHeight().equals("")) {
                            String[] util = Utility.split(strAdolescentHeight);
                            String height = util[0];
                            etxtAdolescentHeight.setError(height + " " + strMandatory);

                        }*/

                                if (adolescentMonitoring.getDateOfRecord().equals("")) {
                                    etxtDateOfRecord.setError(strDateOfRecord + " "
                                            + strMandatory);
                                }

                            } else {

                                etxtAdolescentWeight.setError(null);
                                etxtAdolescentHeight.setError(null);
                                etxtAdolescentHb.setError(null);
                                etxtDateOfRecord.setError(null);

                                if (adolescentMonitoring.getMarked_as() == null) {
                                    adolescentMonitoring.setMarked_as("Adolescent");
                                }

                                if (flag == 0) {
                                    adolescentMonitoring.setImage(image64);
                                    final long local_pw_id = sqliteHelper.adolescentNutritionMonitoring(adolescentMonitoring);

                                    if (local_pw_id > 0) {

                                        etxtSearchByHhid.setText("");
                                        etxtAdolescentWeight.setText("");
                                        etxtAdolescentHeight.setText("");
                                        etxtAdolescentHb.setText("");
                                        etxtDateOfRecord.setText("");
//
//                                    Toast.makeText(getApplicationContext(),
//                                            strAdolescentGirlMonitor + " " + strDone, 200)
//                                            .show();
//
//                                    Intent intent2 = new Intent(this, MainMenuMonitoringActivity.class);
//                                    startActivity(intent2);

//                                    setSubmitPopUp();

                                        showSubmitDialog(context, getString(R.string.adolescent_monitoring), getString(R.string.adolescent_monitoring_success));


                                    } else {
                                        Toast.makeText(getApplicationContext(), strTryAgain,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            if (flag == 0) {

                                if (adolescentMonitoring.getMarked_as() == null) {
                                    adolescentMonitoring.setMarked_as("Adolescent");
                                }

                                adolescentMonitoring.setImage(image64);

                                final long local_pw_id = sqliteHelper
                                        .adolescentNutritionMonitoring(adolescentMonitoring);

                                if (local_pw_id > 0) {

                                    etxtSearchByHhid.setText("");
                                    etxtAdolescentWeight.setText("");
                                    etxtAdolescentHeight.setText("");
                                    etxtAdolescentHb.setText("");
                                    etxtDateOfRecord.setText("");

//                                Toast.makeText(getApplicationContext(),
//                                        strAdolescentGirlMonitor + " " + strDone, 200)
//                                        .show();

                                    showSubmitDialog(context, getString(R.string.adolescent_monitoring), getString(R.string.adolescent_monitoring_success_message));

                                } else {
                                    Toast.makeText(getApplicationContext(), strTryAgain,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.monitered_data, Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkValidation() {
        if(spnAdolescentName.getSelectedItem().toString().equals("Please Select")){
            Toast.makeText(context, R.string.please_select_adolescent, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!etxtAdolescentHb.getText().toString().trim().equals("")) {
            if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) < 2) {
                etxtAdolescentHb.requestFocus();
                etxtAdolescentHb.setError(getString(R.string.hb_should_greter_than_2));
                return  false;
            }else if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) > 20) {
                etxtAdolescentHb.requestFocus();
                etxtAdolescentHb.setError(getString(R.string.hb_should_less_20));
                return  false;
            }
        }
        if (!etxtAdolescentHeight.getText().toString().trim().equals("")) {
            if (Float.parseFloat(etxtAdolescentHeight.getText().toString()) > 240.00) {
                String[] util = Utility.split(strAdolescentHeight);
                String height = util[0];
                etxtAdolescentHeight.setError(height + getString(R.string.should_210));
                etxtAdolescentHeight.setFocusable(true);
                return false;
            }
            if (Float.parseFloat(etxtAdolescentHeight.getText().toString()) < 60.00) {
                String[] util = Utility.split(strAdolescentHeight);
                String height = util[0];
                etxtAdolescentHeight.setError(height + getString(R.string.should_80));
                etxtAdolescentHeight.setFocusable(true);
                return false;
            }
        }

        if (!etxtAdolescentWeight.getText().toString().trim().equals("")) {

            if (Float.parseFloat(etxtAdolescentWeight.getText().toString()) > 200.000) {
                String[] util = Utility.split(strAdolescentWeight);
                String weight = util[0];
                etxtAdolescentWeight.setError(weight + getString(R.string.should_120));
                etxtAdolescentWeight.setFocusable(true);
                return false;
            }
            if (Float.parseFloat(etxtAdolescentWeight.getText().toString()) < 10.000) {
                String[] util = Utility.split(strAdolescentWeight);
                String weight = util[0];
                etxtAdolescentWeight.setError(weight + getString(R.string.should_30));
                etxtAdolescentWeight.setFocusable(true);
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
                Intent intent2 = new Intent(context, MainMenuMonitoringActivity.class);
                context.startActivity(intent2);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }



    public void clickGo(View v) {
        strId = etxtSearchByHhid.getText().toString().trim();
        if (strId.equals("")) {
            populateList(spnAdolescentName, "adolescent", "adolescent_id",
                    "adolescent_name", getString(R.string.select_girl), "where anganwadi_center_id=" + user_id);
        } else {

            populateList(spnAdolescentName, "adolescent", "adolescent_id",
                    "adolescent_name", getString(R.string.select_girl), "where house_number='" + strId.toLowerCase() + "' and  anganwadi_center_id=" + user_id);
        }
    }

    void setLanguage() {

        strlanguageId = sph.getString("Language", "");// getting languageId

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

        strTempMigration = sqliteHelper.LanguageChanges(ConstantValue.LANTempMig,
                strlanguageId);
        strPermMigration = sqliteHelper.LanguageChanges(ConstantValue.LANPerMig,
                strlanguageId);

        strMarkedLabel = sqliteHelper.LanguageChanges(ConstantValue.LANADOLMARKED,
                strlanguageId);

        strMother = sqliteHelper.LanguageChanges(ConstantValue.LANMother,
                strlanguageId);
        strMarried = sqliteHelper.LanguageChanges(ConstantValue.LANMarried,
                strlanguageId);
        strAdolescentGirl = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescentGirl,
                strlanguageId);
        strDeath = sqliteHelper.LanguageChanges(ConstantValue.LANDeath,
                strlanguageId);
        adolescent.setText(strAdolescentGirl);
        mother.setText(strMother);
        married.setText(strMarried);
        marked_label.setText(strMarkedLabel);
        txtAdolescentGirlMonitor.setText(strAdolescentGirlMonitor);

        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strlanguageId);
        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strlanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strlanguageId);

        txtGps.setText(gps);

//        txtAdolscentName.setText(R.string.adolescent_name);
        txtAdolescentWeight.setText(strAdolescentWeight);
        txtAdolescentHeight.setText(strAdolescentHeight);
        txtAdolescentHb.setText(strAdolescentHb);
        txtDateOfRecord.setText(strDateOfRecord);
        txtPregnantNutritionHistory.setText(strAdolescentGirlMonitor + " "
                + strHistory);
        btnAdolescentGo.setText(strAdolescentGo);
        btnAdlMonSubmit.setText(strAdlMonSubmit);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());
        tempMigration.setText(strTempMigration);
        permMigration.setText(strPermMigration);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fdate = df.format(c.getTime());
        etxtDateOfRecord.setText(fdate);
        death.setText(strDeath);

    }

    void initialization() {

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);
        adolescentMonitoring = new AdolescentMonitoring();

        etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        txtAdolescentGirlMonitor = (TextView) findViewById(R.id.txtAdolescentGirlMonitor);
        txtAdolscentName = (TextView) findViewById(R.id.txtAdolscentName);
        txtAdolescentWeight = (TextView) findViewById(R.id.txtAdolescentWeight);
        txtAdolescentHeight = (TextView) findViewById(R.id.txtAdolescentHeight);
        txtAdolescentHb = (TextView) findViewById(R.id.txtAdolescentHb);
        txtDateOfRecord = (TextView) findViewById(R.id.txtDateOfRecord);
        txtPregnantNutritionHistory = (TextView) findViewById(R.id.txtPregnantNutritionHistory);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        txtGps = (TextView) findViewById(R.id.txtGps);
        tempMigration = (RadioButton) findViewById(R.id.tempMigration);
        permMigration = (RadioButton) findViewById(R.id.permMigration);
        marked_label = (TextView) findViewById(R.id.marked_label);

        lltakenIFA= findViewById(R.id.lltakenIFA);
        lladolescentDewarming= findViewById(R.id.lladolescentDewarming);

        adolescent = (RadioButton) findViewById(R.id.adolescent);
        married = (RadioButton) findViewById(R.id.married);
        mother = (RadioButton) findViewById(R.id.mother);
        rbAbsent = (RadioButton) findViewById(R.id.rbAbsent);
        rbPresent = (RadioButton) findViewById(R.id.rbPresent);
        death = (RadioButton) findViewById(R.id.death);
        etxtSearchByHhid = (EditText) findViewById(R.id.etxtSearchByHhid);
        etxtAdolescentWeight = (EditText) findViewById(R.id.etxtAdolescentWeight);
        etxtAdolescentHeight = (EditText) findViewById(R.id.etxtAdolescentHeight);
        etxtAdolescentHb = (EditText) findViewById(R.id.etxtAdolescentHb);
        imgHbStatus=findViewById(R.id.imgHbStatus);
        etxtDateOfRecord = (EditText) findViewById(R.id.etxtDateOfRecord);
        etxtDeathDate = (EditText) findViewById(R.id.etxtDeathDate);
        btnAdolescentGo = (Button) findViewById(R.id.btnAdolescentGo);
        btnAdlMonSubmit = (Button) findViewById(R.id.btnAdlMonSubmit);
        spnAdolescentName = (Spinner) findViewById(R.id.family_card_no_spinner);
        wvAdlNutritionHistory = (WebView) findViewById(R.id.wvAdlNutritionHistory);
        rgMGR = (RadioGroup) findViewById(R.id.rgMGR);
        current_status = (RadioGroup) findViewById(R.id.current_status);
        imgAdolscentMon = (ImageView) findViewById(R.id.imgAdolscentMon);
        inr_death_date = (LinearLayout) findViewById(R.id.inr_death_date);
        rgIFA=findViewById(R.id.rgIFA);
        radiogroupPeriods=findViewById(R.id.radiogroupPeriods);
        rgaccessICDS=findViewById(R.id.rgaccessICDS);
        rgllrececiveIFA=findViewById(R.id.rgllrececiveIFA);
        rgtakenIFA=findViewById(R.id.rgtakenIFA);
        rgadolescentDewarming=findViewById(R.id.rgadolescentDewarming);
        rghealthService=findViewById(R.id.rghealthService);
        rbRegularly=findViewById(R.id.rbRegularly);
        rbNotRegularly=findViewById(R.id.rbNotRegularly);
        rbNotTaking=findViewById(R.id.rbNotTaking);
        radioRegular=findViewById(R.id.radioRegular);
        radioIrregular=findViewById(R.id.radioIrregular);
        cbDewormingDone=findViewById(R.id.cbDewormingDone);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn // spinner
                // class
                // method....always
                // use
                .getSelectedItemId());
        return data.getValue();
    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinner(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                AdolescentMonitoringActivity.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
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

    public void deathDate(View view) {

        inr_death_date.setVisibility(View.VISIBLE);
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

                        Intent intent = new Intent(
                                AdolescentMonitoringActivity.this,
                                MainMenuMonitoringActivity.class);
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
                Calendar c1 = Calendar.getInstance();
                c1.set(year, month, day, 0, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(c1.getTime());


                etxtDeathDate.setText(formattedDate);
                death_date = formattedDate;
//                etxtDateOfRecord.setText(formattedDate);

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