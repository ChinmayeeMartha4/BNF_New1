package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
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
import android.text.format.Time;
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

import com.example.mhealth.adapter.SpinnerAdapter1;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.PregnantWomenMonitor;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.helper.Views;
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

public class ActivityPregnantWomenMonitoring extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etxtDateOfRecord;
    static EditText etxtAbortionDate, etxt_unusual_birth;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    RadioGroup rgMgr, rgVisitSequence,rgNightBlindness,rgIodineDeficiency,rgFluorosis,rgFeverWithChild,
            rgCoughChild,rgBloodSputum,rgUrinary,rgProlongedIllness,rgSugarAlbumin,
            rg_IFA,rg_Calcuim,rg_garden_setup,rg_Consumption_garden,rg_Registered_ICDS,rg_Hb_Level,
            rg_Newborn_Delivery,rg_Mortality;
    RadioButton rbMgrStatus, rbFirstVisit, rbSecondVisit, rbRandom,rbNBYes,rbNBNo,rbNBNot,
            rbIDYes,rbIDNo,rbIDNot,rbFluorosisYes,rbFluorosisNo,rbFluorosisNot,rbFeverWithChildYes,
            rbFeverWithChildNo,rbFeverWithChildNot,rbCoughChildYes,rbCoughChildNo,rbBloodSputumYes,rbBloodSputumNo,
            rbUrinaryYes,rbUrinaryNo,rbProlongedIllnessYes,rbProlongedIllnessNo,rbSugarAlbuminYes,rbSugarAlbuminNo,
            rb_IFA_YES,rb_IFA_No,rb_Calcuim_YES,rb_Calcuim_No,rb_garden_YES,rb_garden_No,rb_Consumption_garden_YES
            ,rb_Consumption_garden_No,rb_Registered_ICDS_YES,rb_Registered_ICDS_No,rb_Hb_Level_YES,rb_Hb_Level_No,
            rb_Newborn_Delivery_Institutional,rb_Newborn_Delivery_Home,rb_maternal,rb_child;
    CheckBox cbHeightBP, cbConvulsions, cbVaginalBleeding, cbFSVD,
            cbSevereAnaemia, cbDiabetes, cbTwins;
    int checkedstatus = 0;
    PregnantWomenMonitor pregnantWomenMonitor;
    EditText etxtSearchByHhid, etxtWomenWeight, etxtHb, etxtBp,etxtBMI,
            sysetxtBp, diasetxtBp, etxtBirthMuac,etxtWeekOfPregnancy;
    ImageView imgHbStatus;
    Spinner spnPregnantWomen,spnVisitSequence, spnPregnancyStatus;
    String[] visitSequenceAL = {"Select Visit Sequence","1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    String[] pregnancyStatusAL={"Select Pregnancy Status", "Still continue", "Live birth", "Still birth", "Abortion / Misscarriage"};
    String[] pregnancyStatusHindiAL={"गर्भावस्था की स्थिति का चयन करें", "अभी भी जारी रखें", "जीवित पैदाइश", "अभी भी जन्म", "गर्भपात / गर्भपात"};
    TextView txtPregnentWomenMonitor, txtPregnentWomen, txtFooter, txtPregWomenWeightGain,
            txtWomenWeight, txtHb, txtBp,  txtGps,txtCurrentDate, txtPregnantNutritionHistory, systxtBp, diastxtBp, txtBirthMuac;
    Button btnWomenNameGo, btnPreWomenMonitorSubmit;
    /*new code*/
    String strLanguageId, strPregnentWomenMonitor, strPregnentWomen, strPregnantWomenName,
            strWomenWeight, strHb, strBMI, strBp, strCurrentDate,
            strAdolescentGo, strAdlMonSubmit, strPregnantNutritionHistory, strId, strData,
            strFooter, strMandatory, strDone, strTryAgain, strCancel, strYes, strNo,
            strsysBp, strdiasBp, strPhotoInsHead, strPhotoIns, strAbortion,  strChildNo,strLat,strLang,
            strUnusalBirth,strFluorosis="",strFeverWithChild="",
            strInstitutionalBirth, strNonInstitutionalBirth, strDelivery, strChildDeliveryType,
            strVisitSequence = "", strHeightBP = "", strConvulsions = "", strVaginalBleeding = "",
            strFSVD = "", strSevereAnaemia = "", strDiabetes = "", strTwins = "", strNightBlindness="",
            strIodineDeficiency="", strPregnancyStatus="",strCoughChild="",strCoughChildNo="",strBloodSputum="",
            strBloodSputumNo="",strUrinary="",strUrinaryNo="",strProlongedIllness="",strProlongedIllnessNo="",
            strSugarAlbumin="",strSugarAlbuminNo="",strMortality,strNewborn,strRegistered,strConsumption,
            strGarden,strCalcuim,strIFA;
    int intWomenId;
    double height;
    ImageView imgPregWomenMon;
    byte[] image;
    String image64 = "";
    LinearLayout abortion_date, unusual_birth_date,llMainView;
    RadioButton rbAbortion, rbunusual_birth;
    String deliveryTypeData = "";
    double weight_diff = 0.0;
    RadioButton delivery, rbinstitutional, rbnoninstitutional;
    RadioGroup rgdeliverytype;
    LinearLayout lldelivery,llBirthWeightOfChild,llPlaceOfDelivery,llDateOfAbortion,llDateOfDelivery;
    TextView childDeliveryType;
    private ArrayList<PregnantWomenMonitor> list;
    private WebView wvNutritionHistory;
    private PregnantWomenMonitor womenMonitor;
    private int user_id;
    private int flag = 0;
    private int abortion = 0, unusual_birth = 0, child_delivery = 0;
    private Bitmap bitmap;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context = this;
    String gps;
    public static EditText etxtDateOfScreening;
    public static EditText etxtDateOfDelevery,etxtDateOfAbortion;
    public static EditText etxtPlaceOfDelivery;
    public static EditText etxtBirthWeightOfChild;
    public static android.app.Dialog submit_alert;
    ArrayList<PregnantWomen> pregnantWomenList;

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
        setContentView(R.layout.activity_activity_pregnant_women_monitoring);

//        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
//        t.setScreenName(GlobalVars.username + "-> Monthly_Monitoring/ Pregnant Women Monitoring");
//        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        setLanguage();
        uploadAttendance();
        setVisitSequenceSpinner();
        setPregnancyStatusSpinner();



        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguages("en");
        } else if (lngTypt.equals("2")) {
            setLanguages("hi");
        }

        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        tvTitleText.setText(getString(R.string.pregnant_women_monitoring));
        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strPregnentWomenMonitor + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivityPregnantWomenMonitoring.this,
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

        Calendar c = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //etxtDateOfRecord.setText(sdf.format(c.getTime()));
        //etxtDateOfScreening.setText(sdf.format(c.getTime()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(c.getTime());


        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String strDay = Integer.toString(day);
        String strMonth = Integer.toString(month);
        String strYear = Integer.toString(year);

        etxtSearchByHhid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populateList(spnPregnantWomen, "pregnant_women", "pregnant_women_id",
                        "name_of_pregnant_women", getString(R.string.select_name), "where flag != 'M' and anganwadi_center_id=" + user_id + " and date('now','start of month','-1 month')< edd");
            }
        });

        Log.e("date", date);
        populateList(spnPregnantWomen, "pregnant_women", "pregnant_women_id",
                "name_of_pregnant_women", getString(R.string.select_name), "where flag != 'M' and anganwadi_center_id=" + user_id + " and date('now','start of month','-1 month')< edd");


        spnPregnantWomen.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                if (!spnPregnantWomen.getSelectedItem().toString().trim().equalsIgnoreCase("")) {
                    flag = 0;
                    txtPregWomenWeightGain.setText(R.string.zero_monitoring);
                    spnPregnantWomen.getSelectedItem().toString();

                    String women = getSelectedValue(spnPregnantWomen);

                    intWomenId = Integer.parseInt(women);

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        rgMgr.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbAbsent) {
                    etxtWomenWeight.setText("");
                    etxtHb.setText("");
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    etxtWomenWeight.setEnabled(false);
                    etxtHb.setEnabled(false);
                    sysetxtBp.setEnabled(false);
                    diasetxtBp.setEnabled(false);
                    etxtWomenWeight.setFocusable(false);
                    etxtHb.setFocusable(false);
                    sysetxtBp.setFocusable(false);
                    diasetxtBp.setFocusable(false);
                    checkedstatus = 1;
                    pregnantWomenMonitor.setMgrStatus("Absent");
                    abortion_date.setVisibility(View.GONE);
                    unusual_birth_date.setVisibility(View.GONE);
                    imgPregWomenMon.setVisibility(View.GONE);
                    lldelivery.setVisibility(View.GONE);
                    etxtAbortionDate.setText("");
                    etxt_unusual_birth.setText("");

                }
                if (checkedId == R.id.rbPresent) {
                    etxtWomenWeight.setEnabled(true);
                    etxtHb.setEnabled(true);
                    sysetxtBp.setEnabled(true);
                    diasetxtBp.setEnabled(true);
                    etxtWomenWeight.setFocusable(true);
                    etxtWomenWeight.setFocusableInTouchMode(true);
                    etxtHb.setFocusable(true);
                    etxtHb.setFocusableInTouchMode(true);
                    sysetxtBp.setFocusable(true);
                    sysetxtBp.setFocusableInTouchMode(true);
                    diasetxtBp.setFocusable(true);
                    diasetxtBp.setFocusableInTouchMode(true);
                    checkedstatus = 0;
                    imgPregWomenMon.setVisibility(View.VISIBLE);
                    pregnantWomenMonitor.setMgrStatus("Present");
                    abortion_date.setVisibility(View.GONE);
                    unusual_birth_date.setVisibility(View.GONE);
                    lldelivery.setVisibility(View.GONE);
                    etxtAbortionDate.setText("");
                    etxt_unusual_birth.setText("");
                }
                if (checkedId == R.id.rbAbortion) {
                    checkedstatus = 1;
                    etxtWomenWeight.setText("");
                    etxtHb.setText("");
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    etxtWomenWeight.setEnabled(false);
                    etxtHb.setEnabled(false);
                    sysetxtBp.setEnabled(false);
                    diasetxtBp.setEnabled(false);
                    etxtWomenWeight.setFocusable(false);
                    etxtHb.setFocusable(false);
                    sysetxtBp.setFocusable(false);
                    diasetxtBp.setFocusable(false);
                    abortion = 1;
                    pregnantWomenMonitor.setMgrStatus("Abortion");
                    abortion_date.setVisibility(View.VISIBLE);
                    imgPregWomenMon.setVisibility(View.GONE);
                    unusual_birth_date.setVisibility(View.GONE);
                    etxtAbortionDate.setText("");
                    lldelivery.setVisibility(View.GONE);
                }
                if (checkedId == R.id.unusual_birth) {
                    checkedstatus = 1;
                    etxtWomenWeight.setText("");
                    etxtHb.setText("");
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    etxtWomenWeight.setEnabled(false);
                    etxtHb.setEnabled(false);
                    sysetxtBp.setEnabled(false);
                    diasetxtBp.setEnabled(false);
                    etxtWomenWeight.setFocusable(false);
                    etxtHb.setFocusable(false);
                    sysetxtBp.setFocusable(false);
                    diasetxtBp.setFocusable(false);
                    unusual_birth = 1;
                    imgPregWomenMon.setVisibility(View.GONE);
                    pregnantWomenMonitor.setMgrStatus("Miscarriage");
                    abortion_date.setVisibility(View.GONE);
                    unusual_birth_date.setVisibility(View.VISIBLE);
                    etxt_unusual_birth.setText("");
                    lldelivery.setVisibility(View.GONE);
                }
                if (checkedId == R.id.delivery) {
                    etxtWomenWeight.setText("");
                    etxtHb.setText("");
                    sysetxtBp.setText("");
                    diasetxtBp.setText("");
                    etxtWomenWeight.setEnabled(false);
                    etxtHb.setEnabled(false);
                    sysetxtBp.setEnabled(false);
                    diasetxtBp.setEnabled(false);
                    etxtWomenWeight.setFocusable(false);
                    etxtHb.setFocusable(false);
                    sysetxtBp.setFocusable(false);
                    diasetxtBp.setFocusable(false);
                    checkedstatus = 1;
                    child_delivery = 1;
                    pregnantWomenMonitor.setMgrStatus("Delivery");
                    imgPregWomenMon.setVisibility(View.GONE);
                    abortion_date.setVisibility(View.GONE);
                    unusual_birth_date.setVisibility(View.GONE);
                    lldelivery.setVisibility(View.VISIBLE);
                    etxtAbortionDate.setText("");
                    etxt_unusual_birth.setText("");
                }
            }
        });


        rgdeliverytype.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                deliveryTypeData = rb.getText().toString();
                Log.e("selected ", deliveryTypeData);
                pregnantWomenMonitor.setDelivery_type(deliveryTypeData);
            }
        });
        etxtHb.addTextChangedListener(new TextWatcher() {
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
                        if (!editable.toString().equalsIgnoreCase("")) {
                            if(Double.parseDouble(etxtHb.getText().toString().trim()) < 7){
                                imgHbStatus.setVisibility(View.VISIBLE);
                                imgHbStatus.setImageResource(R.drawable.sev);
                            }
                            if(Double.parseDouble(etxtHb.getText().toString().trim()) >= 7/* || Integer.parseInt(etxtHb.getText().toString().trim()) <= 9.9*/){
                                imgHbStatus.setVisibility(View.VISIBLE);
                                imgHbStatus.setImageResource(R.drawable.mod);
                            }
                            if(Double.parseDouble(etxtHb.getText().toString().trim()) >= 10/* || Integer.parseInt(etxtHb.getText().toString().trim()) <=10.9*/){
                                imgHbStatus.setVisibility(View.VISIBLE);
                                imgHbStatus.setImageResource(R.drawable.light_yellow);
                            }
                            if(Double.parseDouble(etxtHb.getText().toString().trim()) >= 11){
                                imgHbStatus.setVisibility(View.VISIBLE);
                                imgHbStatus.setImageResource(R.drawable.nor);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        });

        //visit sequence
        rgVisitSequence.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbFirstVisit:
                        strVisitSequence = "First visit";
                        break;
                    case R.id.rbSecondVisit:
                        strVisitSequence = "Second visit";
                        break;
                    case R.id.rbRandom:
                        strVisitSequence = "Random";
                        break;
                }
            }
        });
        //night blindness
        rgNightBlindness.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbNBYes:
                        strNightBlindness = "Yes";
                        break;
                    case R.id.rbNBNo:
                        strNightBlindness = "No";
                        break;
                    case R.id.rbNBNot:
                        strNightBlindness = "Could Not Assist";
                        break;
                }
            }
        });
        //Iodine Deficiency
        rgIodineDeficiency.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbIDYes:
                        strIodineDeficiency = "Yes";
                        break;
                    case R.id.rbIDNo:
                        strIodineDeficiency = "No";
                        break;
                    case R.id.rbIDNot:
                        strIodineDeficiency = "Could Not Assist";
                        break;
                }
            }
        });
        //Fluorosis click
        rgFluorosis.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbFluorosisYes:
                        strFluorosis = "Yes";
                        break;
                    case R.id.rbFluorosisNo:
                        strFluorosis = "No";
                        break;
                    case R.id.rbFluorosisNot:
                        strFluorosis = "Could Not Assist";
                        break;
                }
            }
        });

        //Fever With Child click
        rgFeverWithChild.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbFeverWithChildYes:
                        strFeverWithChild = "Yes";
                        break;
                    case R.id.rbFeverWithChildNo:
                        strFeverWithChild = "No";
                        break;
                    case R.id.rbFeverWithChildNot:
                        strFeverWithChild = "Could Not Assist";
                        break;
                }
            }
        });

        //Cough > 2 weeks
        rgCoughChild.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbCoughChildYes:
                        strCoughChild = "Yes";
                        break;
                    case R.id.rbCoughChildNo:
                        strCoughChild = "No";
                        break;
                }
            }
        });

        //Blood in sputum/Cough
        rgBloodSputum.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbBloodSputumYes:
                        strBloodSputum = "Yes";
                        break;
                    case R.id.rbBloodSputumNo:
                        strBloodSputum = "No";
                        break;
                }
            }
        });

        //Any Urinary complaints
        rgUrinary.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbUrinaryYes:
                        strUrinary = "Yes";
                        break;
                    case R.id.rbUrinaryNo:
                        strUrinary = "No";
                        break;
                }
            }
        });

        //Any Recurrent or prolonged illness
        rgProlongedIllness.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbProlongedIllnessYes:
                        strProlongedIllness = "Yes";
                        break;
                    case R.id.rbProlongedIllnessNo:
                        strProlongedIllness = "No";
                        break;
                }
            }
        });

        //Impaired sugar and albumin
        rgSugarAlbumin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbSugarAlbuminYes:
                        strSugarAlbumin = "Yes";
                        break;
                    case R.id.rbSugarAlbuminNo:
                        strSugarAlbumin = "No";
                        break;
                }
            }
        });

        //all check box for pregnant women monitoring
        cbHeightBP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbHeightBP.isChecked()){
                    strHeightBP="Yes";
                }else {
                    strHeightBP="No";
                }
            }
        });
        cbConvulsions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbConvulsions.isChecked()){
                    strConvulsions="Yes";
                }else {
                    strConvulsions="No";
                }
            }
        });
        cbVaginalBleeding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbVaginalBleeding.isChecked()){
                    strVaginalBleeding="Yes";
                }else {
                    strVaginalBleeding="No";
                }
            }
        });
        cbFSVD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbFSVD.isChecked()){
                    strFSVD="Yes";
                }else {
                    strFSVD="No";
                }
            }
        });
        cbSevereAnaemia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbSevereAnaemia.isChecked()){
                    strSevereAnaemia="Yes";
                }else {
                    strSevereAnaemia="No";
                }
            }
        });
        cbDiabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbDiabetes.isChecked()){
                    strDiabetes="Yes";
                }else {
                    strDiabetes="No";
                }
            }
        });
        cbTwins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbTwins.isChecked()){
                    strTwins="Yes";
                }else {
                    strTwins="No";
                }
            }
        });

        etxtWomenWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //calculate BMI here
                double heightt = sqliteHelper.getHeightOFPW(intWomenId);
                String height = String.valueOf(heightt);
                String weight = etxtWomenWeight.getText().toString().trim();
                String a = getBMI(weight, height);
                etxtBMI.setText(a);
            }
        });

        //new code start

        rg_IFA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_IFA_YES:
                        strIFA = "Yes";
                        break;
                    case R.id.rb_IFA_No:
                        strIFA = "No";
                        break;
                }
            }
        });

        rg_Calcuim.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_Calcuim_YES:
                        strCalcuim = "Yes";
                        break;
                    case R.id.rb_Calcuim_No:
                        strCalcuim = "No";
                        break;
                }
            }
        });

        rg_garden_setup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_garden_YES:
                        strGarden = "Yes";
                        break;
                    case R.id.rb_garden_No:
                        strGarden = "No";
                        break;
                }
            }
        });

        rg_Consumption_garden.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_Consumption_garden_YES:
                        strConsumption = "Yes";
                        break;
                    case R.id.rb_Consumption_garden_No:
                        strConsumption = "No";
                        break;
                }
            }
        });

        rg_Registered_ICDS.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_Registered_ICDS_YES:
                        strRegistered = "Yes";
                        break;
                    case R.id.rb_Registered_ICDS_No:
                        strRegistered = "No";
                        break;
                }
            }
        });

        rg_Newborn_Delivery.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_Newborn_Delivery_Institutional:
                        strNewborn = "Institutional";
                        break;
                    case R.id.rb_Newborn_Delivery_Home:
                        strNewborn = "Home";
                        break;
                }
            }
        });

        rg_Mortality.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_maternal:
                        strMortality = "Maternal";
                        break;
                    case R.id.rb_child:
                        strMortality = "Child";
                        break;
                }
            }
        });
    }


    private void setLanguages(String languageToLoad) {
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


    private void setPregnancyStatusSpinner() {
        String languageId = sph.getString("Language", "");// getting languageId

        if(languageId.equals("1")) {

            ArrayAdapter adapterPregnancyStatus = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, pregnancyStatusAL);
            adapterPregnancyStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPregnancyStatus.setPrompt("Select Pregnancy Status");
            spnPregnancyStatus.setAdapter(adapterPregnancyStatus);

            spnPregnancyStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!spnPregnancyStatus.getSelectedItem().toString().trim().equals(getString(R.string.select_pregnancy_status))){
                        strPregnancyStatus=spnPregnancyStatus.getSelectedItem().toString().trim();
                        if (strPregnancyStatus.equals("Still continue")){
                            llMainView.setVisibility(View.VISIBLE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        } else if (strPregnancyStatus.equals("Live birth")){
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.VISIBLE);
                            llPlaceOfDelivery.setVisibility(View.VISIBLE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.VISIBLE);
                        } else if (strPregnancyStatus.equals("Still birth")){
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        }else {
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.VISIBLE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        }
                    } else {
                    llMainView.setVisibility(View.GONE);
                    llBirthWeightOfChild.setVisibility(View.GONE);
                    llPlaceOfDelivery.setVisibility(View.GONE);
                    llDateOfAbortion.setVisibility(View.GONE);
                    llDateOfDelivery.setVisibility(View.GONE);
                }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }else if(languageId.equals("2")) {
            ArrayAdapter adapterPregnancyStatus = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, pregnancyStatusHindiAL);
            adapterPregnancyStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPregnancyStatus.setPrompt("गर्भावस्था की स्थिति का चयन करें");
            spnPregnancyStatus.setAdapter(adapterPregnancyStatus);

            spnPregnancyStatus.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!spnPregnancyStatus.getSelectedItem().toString().trim().equals(getString(R.string.select_pregnancy_status))){
                        strPregnancyStatus=spnPregnancyStatus.getSelectedItem().toString().trim();
                        if (strPregnancyStatus.equals("अभी भी जारी रखें")){
                            llMainView.setVisibility(View.VISIBLE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        } else if (strPregnancyStatus.equals("जीवित पैदाइश")){
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.VISIBLE);
                            llPlaceOfDelivery.setVisibility(View.VISIBLE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.VISIBLE);
                        } else if (strPregnancyStatus.equals("अभी भी जन्म")){
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.GONE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        }else {
                            llMainView.setVisibility(View.GONE);
                            llBirthWeightOfChild.setVisibility(View.GONE);
                            llPlaceOfDelivery.setVisibility(View.GONE);
                            llDateOfAbortion.setVisibility(View.VISIBLE);
                            llDateOfDelivery.setVisibility(View.GONE);
                        }
                    }else {
                        llMainView.setVisibility(View.GONE);
                        llBirthWeightOfChild.setVisibility(View.GONE);
                        llPlaceOfDelivery.setVisibility(View.GONE);
                        llDateOfAbortion.setVisibility(View.GONE);
                        llDateOfDelivery.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }

    private String getBMI(String weight, String height) {
        String bmi = "";
        if (!weight.equalsIgnoreCase("") && !height.equalsIgnoreCase("")) {
            float h, w;
            h = Float.parseFloat(height);
            w = Float.parseFloat(weight);
            int a = (int) ((w / h / h) * 10000);
            bmi = String.valueOf(a);
        }

        return bmi;
    }

    private void setVisitSequenceSpinner() {
        ArrayAdapter adapterVisitSequence = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, visitSequenceAL);
        adapterVisitSequence.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVisitSequence.setPrompt(getString(R.string.select_visit_sequence));
        spnVisitSequence.setAdapter(adapterVisitSequence);
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
//                                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);

                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", "");
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
        /*strBp = sqliteHelper.LanguageChanges(ConstantValue.LANBloddPressure,strLanguageId);*/
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

        strAbortion = sqliteHelper.LanguageChanges(ConstantValue.LANAbortion, strLanguageId);
        strUnusalBirth = sqliteHelper.LanguageChanges(ConstantValue.LANUnusalBirth, strLanguageId);

        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strLanguageId);

        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strLanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strLanguageId);


        strInstitutionalBirth = sqliteHelper.LanguageChanges(ConstantValue.LANINSTITUTIONALBIRTH, strLanguageId);
        strNonInstitutionalBirth = sqliteHelper.LanguageChanges(ConstantValue.LANNONINSTITUTIONALBIRTH, strLanguageId);
        strDelivery = sqliteHelper.LanguageChanges(ConstantValue.LANDELIVERY, strLanguageId);
        strChildDeliveryType = sqliteHelper.LanguageChanges(ConstantValue.LANCHILDDELIVERYTYPE, strLanguageId);

        txtPregnentWomenMonitor.setText(strPregnentWomenMonitor);
        txtPregnentWomen.setText(strPregnentWomen);
        txtWomenWeight.setText(strWomenWeight);
        txtHb.setText(strHb);
        /*txtBp.setText(strBp);*/
        systxtBp.setText(strsysBp);
        diastxtBp.setText(strdiasBp);
        txtGps.setText(gps);

        txtCurrentDate.setText(strCurrentDate);
        btnWomenNameGo.setText(strAdolescentGo);
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
        String fdate = df.format(c.getTime());
        //etxtDateOfRecord.setText(fdate);
        etxtDateOfScreening.setText(fdate);

        rbAbortion.setText(strAbortion);
        rbunusual_birth.setText(strUnusalBirth);
        rbinstitutional.setText(strInstitutionalBirth);
        rbnoninstitutional.setText(strNonInstitutionalBirth);
        childDeliveryType.setText(strChildDeliveryType);
        delivery.setText(strDelivery);


    }

    private void getData(ArrayList<PregnantWomenMonitor> list2) {
        // TODO Auto-generated method stub
        strData = " ";
        double latest_weight = 0;
        for (int i = 0; i < list.size(); i++) {
            womenMonitor = list.get(i);

            womenMonitor.getWeight();
            Log.e("womenMonitogetWeight()", womenMonitor.getWeight());

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
        weight_diff = latest_weight - reg_weight;
        if (weight_diff > 0) {
            txtPregWomenWeightGain.setText(getString(R.string.weight_gain_till) + weight_diff + getString(R.string.kgs));
        } else if (weight_diff == 0) {
            txtPregWomenWeightGain.setText(getString(R.string.weight_gain_till)+ weight_diff + getString(R.string.kgs));

        } else if (weight_diff < 0 && weight_diff != (-reg_weight)) {
            txtPregWomenWeightGain.setText(getString(R.string.weight_gain_till) + weight_diff + getString(R.string.kgs));

        } else if (weight_diff == (-reg_weight)) {
            txtPregWomenWeightGain.setText(getString(R.string.weight_gain_till) + 0.0 + getString(R.string.kgs));

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
                "  if(document.getElementsByTagName){  " +
                "     " +
                "     var table = document.getElementById(id);  " +
                "     var rows = table.getElementsByTagName(\"tr\"); " +
                "      " +
                "     for(i = 0; i < rows.length; i++){          " +
                "        if(i % 2 == 0){" +
                "           rows[i].className = \"evenrowcolor\";" +
                "        }else{" +
                "           rows[i].className = \"oddrowcolor\";" +
                "        }      " +
                "     }" +
                "  }" +
                "}" +
                "window.onload=function(){" +
                "  altRows('alternatecolor');" +
                "}" +
                "</script>" +
                "" +
                "" +
                "" +
                "" +
                "<style type=\"text/css\">" +
                "table.altrowstable {" +
                "  font-family: verdana,arial,sans-serif;" +
                "  font-size:16px;" +
                "  color:#333333;" +
                "  border-width: 1px;" +
                "  border-color: #a9c6c9;" +
                "  border-collapse: collapse;" +
                "}" +
                "table.altrowstable th {" +
                "  border-width: 1px;" +
                "  padding: 8px;" +
                "  border-style: solid;" +
                "  text-align:center;" +
                "  border-color: #a9c6c9;" +
                "}" +
                "table.altrowstable td {" +
                "  border-width: 1px;" +
                "  padding: 8px;" +
                "  border-style: solid;" +
                "  border-color: #a9c6c9;" +
                "}" +
                ".oddrowcolor{" +
                "  background-color:#d4e3e5;" +
                "}" +
                ".evenrowcolor{" +
                "  background-color:#c3dde0;" +
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
        pregnantWomenList=new ArrayList<>();

        etxtDateOfDelevery=findViewById(R.id.etxtDateOfDelevery);
        etxtDateOfAbortion=findViewById(R.id.etxtDateOfAbortion);
        etxtPlaceOfDelivery=findViewById(R.id.etxtPlaceOfDelivery);
        etxtBirthWeightOfChild=findViewById(R.id.etxtBirthWeightOfChild);
        etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
        tvTitleText = findViewById(R.id.tvTitleText);
        ivTitleBack = findViewById(R.id.ivTitleBack);
        txtPregWomenWeightGain = (TextView) findViewById(R.id.txtPregWomenWeightGain);
        txtPregnentWomenMonitor = (TextView) findViewById(R.id.txtPregnentWomenMonitor);
        txtPregnentWomen = (TextView) findViewById(R.id.txtPregnentWomen);
        txtWomenWeight = (TextView) findViewById(R.id.txtWomenWeight);
        txtHb = (TextView) findViewById(R.id.txtHb);
        txtGps = (TextView) findViewById(R.id. txtGps);
        /*txtBp = (TextView) findViewById(R.id.txtBp);*/
        systxtBp = (TextView) findViewById(R.id.systxtBp);
        diastxtBp = (TextView) findViewById(R.id.diastxtBp);
        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        txtPregnantNutritionHistory = (TextView) findViewById(R.id.txtPregnantNutritionHistory);
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        txtBirthMuac = (TextView) findViewById(R.id.txtBirthMuac);

        etxtBirthMuac = (EditText) findViewById(R.id.etxtBirthMuac);
        etxtWeekOfPregnancy=findViewById(R.id.etxtWeekOfPregnancy);
        etxtBMI=findViewById(R.id.etxtBMI);
        etxtSearchByHhid = (EditText) findViewById(R.id.etxtSearchByHhid);
        etxtWomenWeight = (EditText) findViewById(R.id.etxtWomenWeight);
        etxtHb = (EditText) findViewById(R.id.etxtHb);
        imgHbStatus = findViewById(R.id.imgHbStatus);
        etxtDateOfRecord = (EditText) findViewById(R.id.etxtDateOfRecord);
        /*etxtBp = (EditText) findViewById(R.id.etxtBp);*/
        sysetxtBp = (EditText) findViewById(R.id.sysetxtBp);
        diasetxtBp = (EditText) findViewById(R.id.diasetxtBp);
        spnVisitSequence=findViewById(R.id.spnVisitSequence);
        spnPregnancyStatus=findViewById(R.id.spnPregnancyStatus);

        spnPregnantWomen = (Spinner) findViewById(R.id.spnPregnantWomen);
        btnWomenNameGo = (Button) findViewById(R.id.btnWomenNameGo);
        btnPreWomenMonitorSubmit = (Button) findViewById(R.id.btnPreWomenMonitorSubmit);

        rgMgr = (RadioGroup) findViewById(R.id.rgMGR);
        rbAbortion = (RadioButton) findViewById(R.id.rbAbortion);
        rbunusual_birth = (RadioButton) findViewById(R.id.unusual_birth);
        llMainView=findViewById(R.id.llMainView);

        imgPregWomenMon = (ImageView) findViewById(R.id.imgPregWomenMon);
        unusual_birth_date = (LinearLayout) findViewById(R.id.unusual_birth_date);
        abortion_date = (LinearLayout) findViewById(R.id.abortion_date);
        etxtAbortionDate = (EditText) findViewById(R.id.etxtAbortionDate);
        etxt_unusual_birth = (EditText) findViewById(R.id.etxt_unusual_birth);

        delivery = (RadioButton) findViewById(R.id.delivery);
        rbinstitutional = (RadioButton) findViewById(R.id.rbinstitutional);
        rbnoninstitutional = (RadioButton) findViewById(R.id.rbnoninstitutional);
        rgdeliverytype = (RadioGroup) findViewById(R.id.rgdeliverytype);
        lldelivery = (LinearLayout) findViewById(R.id.lldelivery);
        lldelivery.setVisibility(View.GONE);
        llBirthWeightOfChild=findViewById(R.id.llBirthWeightOfChild);
        llPlaceOfDelivery=findViewById(R.id.llPlaceOfDelivery);
        llDateOfAbortion=findViewById(R.id.llDateOfAbortion);
        llDateOfDelivery=findViewById(R.id.llDateOfDelivery);
        childDeliveryType = (TextView) findViewById(R.id.childDeliveryType);
        rgVisitSequence = findViewById(R.id.rgVisitSequence);
        rbFirstVisit = findViewById(R.id.rbFirstVisit);
        rbSecondVisit = findViewById(R.id.rbSecondVisit);
        rbRandom = findViewById(R.id.rbRandom);
        rgNightBlindness=findViewById(R.id.rgNightBlindness);
        rbNBYes=findViewById(R.id.rbNBYes);
        rbNBNo=findViewById(R.id.rbNBNo);
        rbNBNot=findViewById(R.id.rbNBNot);
        rgIodineDeficiency=findViewById(R.id.rgIodineDeficiency);
        rgFluorosis=findViewById(R.id.rgFluorosis);
        rbFluorosisYes=findViewById(R.id.rbFluorosisYes);
        rbFluorosisNo=findViewById(R.id.rbFluorosisNo);
        rbFluorosisNot=findViewById(R.id.rbFluorosisNot);
        rgFeverWithChild=findViewById(R.id.rgFeverWithChild);
        rbFeverWithChildYes=findViewById(R.id.rbFeverWithChildYes);
        rbFeverWithChildNo=findViewById(R.id.rbFeverWithChildNo);
        rbFeverWithChildNot=findViewById(R.id.rbFeverWithChildNot);
        rbIDYes=findViewById(R.id.rbIDYes);
        rbIDNo=findViewById(R.id.rbIDNo);
        rbIDNot=findViewById(R.id.rbIDNot);
        cbHeightBP = findViewById(R.id.cbHeightBP);
        cbConvulsions = findViewById(R.id.cbConvulsions);
        cbVaginalBleeding = findViewById(R.id.cbVaginalBleeding);
        cbFSVD = findViewById(R.id.cbFSVD);
        cbSevereAnaemia = findViewById(R.id.cbSevereAnaemia);
        cbDiabetes = findViewById(R.id.cbDiabetes);
        cbTwins = findViewById(R.id.cbTwins);

        rgCoughChild=findViewById(R.id.rgCoughChild);
        rbCoughChildYes=findViewById(R.id.rbCoughChildYes);
        rbCoughChildNo=findViewById(R.id.rbCoughChildNo);

        rgBloodSputum=findViewById(R.id.rgBloodSputum);
        rbBloodSputumYes=findViewById(R.id.rbBloodSputumYes);
        rbBloodSputumNo=findViewById(R.id.rbBloodSputumNo);

        rgUrinary=findViewById(R.id.rgUrinary);
        rbUrinaryYes=findViewById(R.id.rbUrinaryYes);
        rbUrinaryNo=findViewById(R.id.rbUrinaryNo);

        rgProlongedIllness=findViewById(R.id.rgProlongedIllness);
        rbProlongedIllnessYes=findViewById(R.id.rbProlongedIllnessYes);
        rbProlongedIllnessNo=findViewById(R.id.rbProlongedIllnessNo);

        rgSugarAlbumin=findViewById(R.id.rgSugarAlbumin);
        rbSugarAlbuminYes=findViewById(R.id.rbSugarAlbuminYes);
        rbSugarAlbuminNo=findViewById(R.id.rbSugarAlbuminNo);

        rg_IFA=findViewById(R.id.rg_IFA);
        rb_IFA_YES=findViewById(R.id.rb_IFA_YES);
        rb_IFA_No=findViewById(R.id.rb_IFA_No);

        rg_Calcuim=findViewById(R.id.rg_Calcuim);
        rb_Calcuim_YES=findViewById(R.id.rb_Calcuim_YES);
        rb_Calcuim_No=findViewById(R.id.rb_Calcuim_No);

        rg_garden_setup=findViewById(R.id.rg_garden_setup);
        rb_garden_YES=findViewById(R.id.rb_garden_YES);
        rb_garden_No=findViewById(R.id.rb_garden_No);

        rg_Consumption_garden=findViewById(R.id.rg_Consumption_garden);
        rb_Consumption_garden_YES=findViewById(R.id.rb_Consumption_garden_YES);
        rb_Consumption_garden_No=findViewById(R.id.rb_Consumption_garden_No);

        rg_Registered_ICDS=findViewById(R.id.rg_Registered_ICDS);
        rb_Registered_ICDS_YES=findViewById(R.id.rb_Registered_ICDS_YES);
        rb_Registered_ICDS_No=findViewById(R.id.rb_Registered_ICDS_No);

        rg_Newborn_Delivery=findViewById(R.id.rg_Newborn_Delivery);
        rb_Newborn_Delivery_Institutional=findViewById(R.id.rb_Newborn_Delivery_Institutional);
        rb_Newborn_Delivery_Home=findViewById(R.id.rb_Newborn_Delivery_Home);

        rg_Mortality=findViewById(R.id.rg_Mortality);
        rb_maternal=findViewById(R.id.rb_maternal);
        rb_child=findViewById(R.id.rb_child);
    }

    public void clickGo(View v) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String strDay = Integer.toString(day);
        String strMonth = Integer.toString(month);
        String strYear = Integer.toString(year);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(c.getTime());
        Log.e("date", date);
        strId = etxtSearchByHhid.getText().toString().trim();
        if (strId.equals("")) {

            populateList(spnPregnantWomen, "pregnant_women", "pregnant_women_id",
                    "name_of_pregnant_women", getString(R.string.select_name), "where flag != 'M' and anganwadi_center_id=" + user_id + " and date('now','start of month','-1 month')< edd");
            Log.e("......................", strId);
        } else {

            populateList(spnPregnantWomen, "pregnant_women",
                    "pregnant_women_id", "name_of_pregnant_women",
                    String.valueOf(R.string.select_name),
                    "where house_number='" + strId.toLowerCase() + "' and flag != 'M' and anganwadi_center_id=" + user_id + " and date('now','start of month','-1 month')< edd");
        }

    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = null;
        try {
            data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        } catch (Exception e) {
            data.setValue("null");
            e.printStackTrace();
        }
        return data.getValue();
    }

    @SuppressLint("NewApi")

    public void btnSave(View vw) {


        String languageId = sph.getString("Language", "");// getting languageId

        if(languageId.equals("1")) {
            if(spnPregnantWomen.getSelectedItem().toString().equals("Please Select")){
                Toast.makeText(context, R.string.please_select_pregnant_women_first, Toast.LENGTH_SHORT).show();
                return;
            }

            if (strPregnancyStatus.equals("Still continue")) {
                if (checkValidation()) {
                    try {
                        if (spnPregnantWomen.getSelectedItemPosition() == -1) {
                            Toast.makeText(getApplicationContext(), R.string.please_select_pregnant_women_first, Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            pregnantWomenMonitor.setWeight(etxtWomenWeight.getText().toString().trim());

                            if (etxtHb.getText().toString().trim().equals("")) {
                                pregnantWomenMonitor.setHb("0");
                            } else {
                                pregnantWomenMonitor.setHb(etxtHb.getText().toString().trim());
                            }

                            /*pregnantWomenMonitor.setBp(etxtBp.getText().toString().trim());*/
                            pregnantWomenMonitor.setSysbp(sysetxtBp.getText().toString().trim());
                            pregnantWomenMonitor.setDiasbp(diasetxtBp.getText().toString().trim());
                            pregnantWomenMonitor.setMuac_status(etxtBirthMuac.getText().toString().trim());
                            pregnantWomenMonitor.setMobile_unique_id(CommonMethods.getUUID());
                            pregnantWomenMonitor.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                            pregnantWomenMonitor.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
                            pregnantWomenMonitor.setVisit_sequence(spnVisitSequence.getSelectedItem().toString().trim());
                            pregnantWomenMonitor.setHigh_bp(strHeightBP);
                            pregnantWomenMonitor.setConvulsions(strConvulsions);
                            pregnantWomenMonitor.setVaginal_bleeding(strVaginalBleeding);
                            pregnantWomenMonitor.setFoul_smelling_vaginal_discharge(strFSVD);
                            pregnantWomenMonitor.setSevere_anaemia(strSevereAnaemia);
                            pregnantWomenMonitor.setDiabetes(strDiabetes);
                            pregnantWomenMonitor.setTwins(strTwins);
                            pregnantWomenMonitor.setWeek_of_pregnancy(etxtWeekOfPregnancy.getText().toString().trim());
                            pregnantWomenMonitor.setNight_blindness(strNightBlindness);
                            pregnantWomenMonitor.setIodine_deficiency(strIodineDeficiency);
                            pregnantWomenMonitor.setFluorosis(strFluorosis);
                            pregnantWomenMonitor.setBmi(etxtBMI.getText().toString().trim());
                            pregnantWomenMonitor.setFever_with_chilled(strFeverWithChild);
                            pregnantWomenMonitor.setCough(strCoughChild);
                            pregnantWomenMonitor.setSputum_cough(strBloodSputum);
                            pregnantWomenMonitor.setUrinary_complaints(strUrinary);
                            pregnantWomenMonitor.setProlonged_illness(strProlongedIllness);
                            pregnantWomenMonitor.setSugar_albumin(strSugarAlbumin);

                            pregnantWomenMonitor.setIFA_tablet(strIFA);
                            pregnantWomenMonitor.setCalcuim_tablet(strCalcuim);
                            pregnantWomenMonitor.setNutrition_garden(strGarden);
                            pregnantWomenMonitor.setConsumption_garden(strConsumption);
                            pregnantWomenMonitor.setRegistered_ICDS(strRegistered);
                            pregnantWomenMonitor.setNewborn_delivery(strNewborn);
                            pregnantWomenMonitor.setMortality(strMortality);

                            Time time = new Time();
                            time.setToNow();

                            String strDate = time.hour + ":" + time.minute + ":" + time.second;
                            //pregnantWomenMonitor.setCurrent_date(etxtDateOfRecord.getText().toString().trim() + " " + strDate);
                            pregnantWomenMonitor.setCurrent_date(etxtDateOfScreening.getText().toString().trim() + " " + strDate);

                            //pregnantWomenMonitor.setDate_of_recording(etxtDateOfRecord.getText().toString().trim() + " " + strDate);
                            pregnantWomenMonitor.setDate_of_recording(etxtDateOfScreening.getText().toString().trim() + " " + strDate);
                            String name = getSelectedValue(spnPregnantWomen).trim();

                            if (!name.equalsIgnoreCase("null")) {
                                pregnantWomenMonitor.setPregnant_women_id(name);
                                pregnantWomenMonitor.setPregnant_women_name(spnPregnantWomen.getSelectedItem().toString().trim());

                                ArrayList<Views> arr = new ArrayList<Views>();

                                arr = sqliteHelper.monitoringDateSearch("pregnant_womem_monitor", "women_id", String.valueOf(getSelectedValue(spnPregnantWomen).trim()));

                                for (int i = 0; i < arr.size(); i++) {

                                    final Views viewObj = arr.get(i);
                                    String s = viewObj.getDate();
                                    //String dateOfRecording = etxtDateOfRecord.getText().toString().trim();
                                    String dateOfRecording = etxtDateOfScreening.getText().toString().trim();
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
                                    int monthDb = c.get(Calendar.MONTH) + 1;
                                    int yearDb = c.get(Calendar.YEAR);
                                    c.setTime(dateInsert);
                                    int monthInsert = c.get(Calendar.MONTH) + 1;
                                    int yearInsert = c.get(Calendar.YEAR);

                                    if (monthDb == monthInsert && yearDb == yearInsert) {

                                        Toast.makeText(getApplicationContext(),
                                                "This month data already monitored.", Toast.LENGTH_LONG)
                                                .show();

                                        etxtSearchByHhid.setText("");
                                        etxtWomenWeight.setText("");
                                        etxtHb.setText("");
                                        etxtBp.setText("");
                                        sysetxtBp.setText("");
                                        diasetxtBp.setText("");
                                        //etxtDateOfRecord.setText("");
                                        etxtDateOfScreening.setText("");
                                        flag = 1;
                                    }
                                }

                                if (checkedstatus == 0) {
                                    if (pregnantWomenMonitor.getPregnant_women_name().equals("")
                                            || etxtWomenWeight.getText().toString().equals("")
                                            || Float.parseFloat(etxtWomenWeight.getText().toString()) < 10.000
                                            || Float.parseFloat(etxtWomenWeight.getText().toString()) > 200.000
                                            //|| etxtDateOfRecord.getText().toString().equals("")
                                            || etxtDateOfScreening.getText().toString().equals("")

//                                            || pregnantWomenMonitor.getSysbp().equals("")
//                                            || Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.00
//                                            || Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.00

//                                            || pregnantWomenMonitor.getDiasbp().equals("")
//                                            || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.00
//                                            || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.00

                                    ) {

                                        if (pregnantWomenMonitor.getPregnant_women_name().equals("")) {
                                            Toast.makeText(getApplicationContext(), strPregnentWomen + " " + strMandatory, Toast.LENGTH_LONG).show();
                                        }

                                        if (etxtWomenWeight.getText().toString().equals("")) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + " " + strMandatory);
                                        }
                                        if (Float.parseFloat(etxtWomenWeight.getText().toString()) < 10.000) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + R.string.should_30);
                                            etxtWomenWeight.setFocusable(true);
                                        }
                                        if (Float.parseFloat(etxtWomenWeight.getText().toString()) > 200.000) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + R.string.should_120);
                                            etxtWomenWeight.setFocusable(true);
                                        }
//                                        if (pregnantWomenMonitor.getSysbp().equals(""))
//                                            sysetxtBp.setError(strsysBp + " " + strMandatory);
//
//                                        if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.000) {
//                                            String[] util = Utility.split(strsysBp);
//                                            String sysbp = util[0];
//                                            sysetxtBp.setError(sysbp + getString(R.string.should_40));
//                                            sysetxtBp.setFocusable(true);
//                                        }
//                                        if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.000) {
//                                            String[] util = Utility.split(strsysBp);
//                                            String sysbp = util[0];
//                                            sysetxtBp.setError(sysbp + getString(R.string.should_200));
//                                            sysetxtBp.setFocusable(true);
//                                        }

//                                        if (pregnantWomenMonitor.getDiasbp().equals(""))
//                                            diasetxtBp.setError(strdiasBp + " " + strMandatory);
//
//                                        if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.000) {
//                                            String[] util = Utility.split(strdiasBp);
//                                            String diasbp = util[0];
//                                            diasetxtBp.setError(diasbp + getString(R.string.should_greater_40));
//                                            diasetxtBp.setFocusable(true);
//                                        }
//                                        if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.000) {
//                                            String[] util = Utility.split(strdiasBp);
//                                            String diasbp = util[0];
//                                            diasetxtBp.setError(diasbp + getString(R.string.should_250));
//                                            diasetxtBp.setFocusable(true);
//                                        }
                                        if (etxtBp.getText().toString().equals("")) {

                                            etxtBp.setError(strBp + " " + strMandatory);
                                        }

                                /*if (etxtDateOfRecord.getText().toString().equals("")) {
                                    etxtDateOfRecord.setError(strCurrentDate + " " + strMandatory);
                                }*/
                                        if (etxtDateOfScreening.getText().toString().equals("")) {
                                            etxtDateOfScreening.setError(getString(R.string.date_of_screen) + " " + strMandatory);
                                        }

                                    } else {

                                        etxtSearchByHhid.setError(null);
                                        etxtWomenWeight.setError(null);
                                        etxtHb.setError(null);
//                                        sysetxtBp.setError(null);
//                                        diasetxtBp.setError(null);
                                        //etxtDateOfRecord.setError(null);
                                        etxtDateOfScreening.setError(null);

                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                                /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();*/
                                        /*Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_success_message));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.month_taken_message));
                                        }
                                    }
                                } else if (checkedstatus == 1) {
                                    if (abortion == 1) {
                                        pregnantWomenMonitor.setCause_date(etxtAbortionDate.getText().toString().trim());
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_success_message));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.month_taken_message));
                                        }
                                    } else if (unusual_birth == 1) {
                                        pregnantWomenMonitor.setCause_date(etxt_unusual_birth.getText().toString().trim());
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    } else if (child_delivery == 1) {
                                        pregnantWomenMonitor.setDelivery_type(deliveryTypeData);
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    } else {
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    }

                                }
                            }

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (strPregnancyStatus.equals("Live birth")) {
                if (checkLiveBirthValidation()) {
                    //update data in pregnant women table
                    String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                    String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                    String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                    String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                    long id = sqliteHelper.updatePregnantWomenTable(
                            "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                    + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                    Log.e("updateId>>>", "onUpdate: " + id);
                    if (id > 0) {
                        //get all data if pregnant women convert into mother and insert to mother table
                        pregnantWomenList = sqliteHelper.getPregnantWomenToMother2(intWomenId);
                        if (pregnantWomenList.size() > 0) {
                            //insert data to mother table
                            long idd = sqliteHelper.MotherRegistration2(pregnantWomenList);
                            if (idd > 0) {
                                Log.e("Child>>>", "insert pregnant data to mother ");
                            }
                        }
                        showSubmitDialogForChild(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_message));
                    }
                }
            } else if (strPregnancyStatus.equals("Still birth")) {
                //update data in pregnant women table
                String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                long id = sqliteHelper.updatePregnantWomenTable(
                        "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                Log.e("updateId>>>", "onUpdate: " + id);
                if (id > 0) {
                    showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_message));
                }
            } else {
                if (checkAbortionValidation()) {
                    //update data in pregnant women table
                    String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                    String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                    String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                    String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                    long id = sqliteHelper.updatePregnantWomenTable(
                            "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                    + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                    Log.e("updateId>>>", "onUpdate: " + id);
                    if (id > 0) {
                        showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_message));
                    }
                }
            }
        }else if(languageId.equals("2")) {
            if (strPregnancyStatus.equals("अभी भी जारी रखें")) {
                if (checkValidation()) {
                    try {
                        if (spnPregnantWomen.getSelectedItemPosition() == -1) {
                            Toast.makeText(getApplicationContext(), R.string.please_select_pregnant_women_first, Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            pregnantWomenMonitor.setWeight(etxtWomenWeight.getText().toString().trim());

                            if (etxtHb.getText().toString().trim().equals("")) {
                                pregnantWomenMonitor.setHb("0");
                            } else {
                                pregnantWomenMonitor.setHb(etxtHb.getText().toString().trim());
                            }

                            /*pregnantWomenMonitor.setBp(etxtBp.getText().toString().trim());*/
                            pregnantWomenMonitor.setSysbp(sysetxtBp.getText().toString().trim());
                            pregnantWomenMonitor.setDiasbp(diasetxtBp.getText().toString().trim());
                            pregnantWomenMonitor.setMuac_status(etxtBirthMuac.getText().toString().trim());
                            pregnantWomenMonitor.setMobile_unique_id(CommonMethods.getUUID());
                            pregnantWomenMonitor.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                            pregnantWomenMonitor.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
                            pregnantWomenMonitor.setVisit_sequence(spnVisitSequence.getSelectedItem().toString().trim());
                            pregnantWomenMonitor.setHigh_bp(strHeightBP);
                            pregnantWomenMonitor.setConvulsions(strConvulsions);
                            pregnantWomenMonitor.setVaginal_bleeding(strVaginalBleeding);
                            pregnantWomenMonitor.setFoul_smelling_vaginal_discharge(strFSVD);
                            pregnantWomenMonitor.setSevere_anaemia(strSevereAnaemia);
                            pregnantWomenMonitor.setDiabetes(strDiabetes);
                            pregnantWomenMonitor.setTwins(strTwins);
                            pregnantWomenMonitor.setWeek_of_pregnancy(etxtWeekOfPregnancy.getText().toString().trim());
                            pregnantWomenMonitor.setNight_blindness(strNightBlindness);
                            pregnantWomenMonitor.setIodine_deficiency(strIodineDeficiency);
                            pregnantWomenMonitor.setFluorosis(strFluorosis);
                            pregnantWomenMonitor.setBmi(etxtBMI.getText().toString().trim());
                            pregnantWomenMonitor.setFever_with_chilled(strFeverWithChild);
                            pregnantWomenMonitor.setCough(strCoughChild);
                            pregnantWomenMonitor.setSputum_cough(strBloodSputum);
                            pregnantWomenMonitor.setUrinary_complaints(strUrinary);
                            pregnantWomenMonitor.setProlonged_illness(strProlongedIllness);
                            pregnantWomenMonitor.setSugar_albumin(strSugarAlbumin);

                            Time time = new Time();
                            time.setToNow();

                            String strDate = time.hour + ":" + time.minute + ":" + time.second;
                            //pregnantWomenMonitor.setCurrent_date(etxtDateOfRecord.getText().toString().trim() + " " + strDate);
                            pregnantWomenMonitor.setCurrent_date(etxtDateOfScreening.getText().toString().trim() + " " + strDate);

                            //pregnantWomenMonitor.setDate_of_recording(etxtDateOfRecord.getText().toString().trim() + " " + strDate);
                            pregnantWomenMonitor.setDate_of_recording(etxtDateOfScreening.getText().toString().trim() + " " + strDate);
                            String name = getSelectedValue(spnPregnantWomen).trim();

                            if (!name.equalsIgnoreCase("null")) {
                                pregnantWomenMonitor.setPregnant_women_id(name);
                                pregnantWomenMonitor.setPregnant_women_name(spnPregnantWomen.getSelectedItem().toString().trim());

                                ArrayList<Views> arr = new ArrayList<Views>();

                                arr = sqliteHelper.monitoringDateSearch("pregnant_womem_monitor", "women_id", String.valueOf(getSelectedValue(spnPregnantWomen).trim()));

                                for (int i = 0; i < arr.size(); i++) {

                                    final Views viewObj = arr.get(i);
                                    String s = viewObj.getDate();
                                    //String dateOfRecording = etxtDateOfRecord.getText().toString().trim();
                                    String dateOfRecording = etxtDateOfScreening.getText().toString().trim();
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
                                    int monthDb = c.get(Calendar.MONTH) + 1;
                                    int yearDb = c.get(Calendar.YEAR);
                                    c.setTime(dateInsert);
                                    int monthInsert = c.get(Calendar.MONTH) + 1;
                                    int yearInsert = c.get(Calendar.YEAR);

                                    if (monthDb == monthInsert && yearDb == yearInsert) {

                                        Toast.makeText(getApplicationContext(),
                                                "This month data already monitored.", Toast.LENGTH_LONG)
                                                .show();

                                        etxtSearchByHhid.setText("");
                                        etxtWomenWeight.setText("");
                                        etxtHb.setText("");
                                        etxtBp.setText("");
                                        sysetxtBp.setText("");
                                        diasetxtBp.setText("");
                                        //etxtDateOfRecord.setText("");
                                        etxtDateOfScreening.setText("");
                                        flag = 1;
                                    }
                                }

                                if (checkedstatus == 0) {
                                    if (pregnantWomenMonitor.getPregnant_women_name().equals("")
                                            || etxtWomenWeight.getText().toString().equals("")
                                            || Float.parseFloat(etxtWomenWeight.getText().toString()) < 10.000
                                            || Float.parseFloat(etxtWomenWeight.getText().toString()) > 200.000
                                            //|| etxtDateOfRecord.getText().toString().equals("")
                                            || etxtDateOfScreening.getText().toString().equals("")

//                                            || pregnantWomenMonitor.getSysbp().equals("")
//                                            || Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.00
//                                            || Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.00
//
//                                            || pregnantWomenMonitor.getDiasbp().equals("")
//                                            || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.00
//                                            || Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.00

                                    ) {

                                        if (pregnantWomenMonitor.getPregnant_women_name().equals("")) {
                                            Toast.makeText(getApplicationContext(), strPregnentWomen + " " + strMandatory, Toast.LENGTH_LONG).show();
                                        }

                                        if (etxtWomenWeight.getText().toString().equals("")) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + " " + strMandatory);
                                        }
                                        if (Float.parseFloat(etxtWomenWeight.getText().toString()) < 10.000) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + getString(R.string.should_30));
                                            etxtWomenWeight.setFocusable(true);
                                        }
                                        if (Float.parseFloat(etxtWomenWeight.getText().toString()) > 200.000) {

                                            String[] util = Utility.split(strWomenWeight);
                                            String weight = util[0];
                                            etxtWomenWeight.setError(weight + getString(R.string.should_120));
                                            etxtWomenWeight.setFocusable(true);
                                        }
//                                        if (pregnantWomenMonitor.getSysbp().equals(""))
//                                            sysetxtBp.setError(strsysBp + " " + strMandatory);
//
//                                        if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) < 40.000) {
//                                            String[] util = Utility.split(strsysBp);
//                                            String sysbp = util[0];
//                                            sysetxtBp.setError(sysbp + getString(R.string.should_40));
//                                            sysetxtBp.setFocusable(true);
//                                        }
//                                        if (Float.parseFloat(pregnantWomenMonitor.getSysbp()) > 200.000) {
//                                            String[] util = Utility.split(strsysBp);
//                                            String sysbp = util[0];
//                                            sysetxtBp.setError(sysbp + getString(R.string.should_200));
//                                            sysetxtBp.setFocusable(true);
//                                        }
//
//                                        if (pregnantWomenMonitor.getDiasbp().equals(""))
//                                            diasetxtBp.setError(strdiasBp + " " + strMandatory);
//
//                                        if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) < 40.000) {
//                                            String[] util = Utility.split(strdiasBp);
//                                            String diasbp = util[0];
//                                            diasetxtBp.setError(diasbp + getString(R.string.should_greater_40));
//                                            diasetxtBp.setFocusable(true);
//                                        }
//                                        if (Float.parseFloat(pregnantWomenMonitor.getDiasbp()) > 250.000) {
//                                            String[] util = Utility.split(strdiasBp);
//                                            String diasbp = util[0];
//                                            diasetxtBp.setError(diasbp + getString(R.string.should_250));
//                                            diasetxtBp.setFocusable(true);
//                                        }
                                        if (etxtBp.getText().toString().equals("")) {

                                            etxtBp.setError(strBp + " " + strMandatory);
                                        }

                                /*if (etxtDateOfRecord.getText().toString().equals("")) {
                                    etxtDateOfRecord.setError(strCurrentDate + " " + strMandatory);
                                }*/
                                        if (etxtDateOfScreening.getText().toString().equals("")) {
                                            etxtDateOfScreening.setError(getString(R.string.date_of_screen) + " " + strMandatory);
                                        }

                                    } else {

                                        etxtSearchByHhid.setError(null);
                                        etxtWomenWeight.setError(null);
                                        etxtHb.setError(null);
//                                        sysetxtBp.setError(null);
//                                        diasetxtBp.setError(null);
                                        //etxtDateOfRecord.setError(null);
                                        etxtDateOfScreening.setError(null);

                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                                /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();*/
                                        /*Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_success_message));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.month_taken_message));
                                        }
                                    }
                                } else if (checkedstatus == 1) {
                                    if (abortion == 1) {
                                        pregnantWomenMonitor.setCause_date(etxtAbortionDate.getText().toString().trim());
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_success_message));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.month_taken_message));
                                        }
                                    } else if (unusual_birth == 1) {
                                        pregnantWomenMonitor.setCause_date(etxt_unusual_birth.getText().toString().trim());
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    } else if (child_delivery == 1) {
                                        pregnantWomenMonitor.setDelivery_type(deliveryTypeData);
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    } else {
                                        if (flag == 0) {
                                            pregnantWomenMonitor.setImage(image64);
                                            long id = sqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                                            if (id > 0) {
                                                etxtSearchByHhid.setText("");
                                                etxtWomenWeight.setText("");
                                                etxtHb.setText("");
                                                sysetxtBp.setText("");
                                                diasetxtBp.setText("");
                                                //etxtDateOfRecord.setText("");
                                                etxtDateOfScreening.setText("");
                                        /*Toast.makeText(getApplicationContext(), strPregnentWomenMonitor + " " + strDone, 200).show();

                                        Intent intent1 = new Intent(ActivityPregnantWomenMonitoring.this, MainMenuMonitoringActivity.class);
                                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                                                .toBundle();
                                        startActivity(intent1, bndlanimation4);*/
                                                showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_monitoring));
                                            } else {
                                                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            //Toast.makeText(getApplicationContext(), "This month data is already taken.", 200).show();
                                            AlertDialogClass.showDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.this_month_is_already_taken));
                                        }
                                    }

                                }
                            }

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (strPregnancyStatus.equals("जीवित पैदाइश")) {
                if (checkLiveBirthValidation()) {
                    //update data in pregnant women table
                    String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                    String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                    String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                    String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                    long id = sqliteHelper.updatePregnantWomenTable(
                            "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                    + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                    Log.e("updateId>>>", "onUpdate: " + id);
                    if (id > 0) {
                        //get all data if pregnant women convert into mother and insert to mother table
                        pregnantWomenList = sqliteHelper.getPregnantWomenToMother2(intWomenId);
                        if (pregnantWomenList.size() > 0) {
                            //insert data to mother table
                            long idd = sqliteHelper.MotherRegistration2(pregnantWomenList);
                            if (idd > 0) {
                                Log.e("Child>>>", "insert pregnant data to mother ");
                            }
                        }
                        showSubmitDialogForChild(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnant_monitoring_success_message));
                    }
                }
            } else if (strPregnancyStatus.equals("अभी भी जन्म")) {
                //update data in pregnant women table
                String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                long id = sqliteHelper.updatePregnantWomenTable(
                        "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                Log.e("updateId>>>", "onUpdate: " + id);
                if (id > 0) {
                    showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_message));
                }
            } else {
                if (checkAbortionValidation()) {
                    //update data in pregnant women table
                    String dateOfDelivery = etxtDateOfDelevery.getText().toString().trim();
                    String dateOfAbortion = etxtDateOfAbortion.getText().toString().trim();
                    String placeOfDelivery = etxtPlaceOfDelivery.getText().toString().trim();
                    String birthWeightOfChild = etxtBirthWeightOfChild.getText().toString().trim();
                    long id = sqliteHelper.updatePregnantWomenTable(
                            "pregnant_women", strPregnancyStatus, dateOfDelivery, dateOfAbortion, placeOfDelivery, birthWeightOfChild, "anganwadi_center_id="
                                    + user_id + " and pregnant_women_id ='" + intWomenId + "'");
                    Log.e("updateId>>>", "onUpdate: " + id);
                    if (id > 0) {
                        showSubmitDialog(context, getString(R.string.pregnant_women_monitoring), getString(R.string.pregnanat_monitoring_message));
                    }
                }
            }
        }
    }

    private boolean checkLiveBirthValidation() {
        if (etxtDateOfDelevery.getText().toString().trim().length()==0){
            etxtDateOfDelevery.setFocusable(true);
            etxtDateOfDelevery.setError(getString(R.string.please_choose_date_of_delivery));
            Toast.makeText(context, getString(R.string.please_choose_date_of_delivery), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etxtPlaceOfDelivery.getText().toString().trim().length()==0){
            etxtPlaceOfDelivery.setFocusable(true);
            etxtPlaceOfDelivery.setError(getString(R.string.please_enter_the_place_delivery));
            return false;
        }
        if (etxtBirthWeightOfChild.getText().toString().trim().length()==0){
            etxtBirthWeightOfChild.setFocusable(true);
            etxtBirthWeightOfChild.setError(getString(R.string.please_enter_weight_of_child));
            return false;
        }
        if (Float.parseFloat(etxtBirthWeightOfChild.getText().toString().trim()) < 0.800){
            etxtBirthWeightOfChild.setFocusable(true);
            etxtBirthWeightOfChild.setError(getString(R.string.weight_should_800));
            return false;
        }
        if (Float.parseFloat(etxtBirthWeightOfChild.getText().toString().trim()) > 4.000){
            etxtBirthWeightOfChild.setFocusable(true);
            etxtBirthWeightOfChild.setError(getString(R.string.weight_should_4));
            return false;
        }
        return true;
    }

    private boolean checkAbortionValidation() {
        if (etxtDateOfAbortion.getText().toString().trim().length()==0){
            etxtDateOfAbortion.setFocusable(true);
            etxtDateOfAbortion.setError(getString(R.string.please_choose_date_abortion));
            Toast.makeText(context, getString(R.string.please_choose_date_abortion), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static void showSubmitDialogForChild(Context context, String infoTitle, String message) {
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
                Intent intent1 = new Intent(context, ActivityChildReg.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent1);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
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
                Intent intent1 = new Intent(context, MainMenuMonitoringActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent1);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }

    private boolean checkValidation() {



//        if (etxtWeekOfPregnancy.getText().toString().trim().length()==0){
//            etxtWeekOfPregnancy.setFocusable(true);
//            etxtWeekOfPregnancy.setError(getString(R.string.please_enter_week_of_pregnancy));
//            return false;
//        }


//        if (Integer.parseInt(etxtWeekOfPregnancy.getText().toString().trim())>42){
//            etxtWeekOfPregnancy.setFocusable(true);
//            etxtWeekOfPregnancy.setError(getString(R.string.pregnanacy_shuld_42));
//            return false;
//        }
//        String weekOfPregnant = sqliteHelper.GetOneData("week_of_pregnancy", "pregnant_womem_monitor", String.valueOf(intWomenId));
//        if (!weekOfPregnant.equals("")){
//            if (Integer.parseInt(etxtWeekOfPregnancy.getText().toString().trim()) <= Integer.parseInt(weekOfPregnant)){
//                etxtWeekOfPregnancy.setFocusable(true);
//                etxtWeekOfPregnancy.setError(getString(R.string.previous_pregnanacy));
//                return false;
//            }
//        }
//        if (etxtBMI.getText().toString().trim().length()==0){
//            etxtBMI.setFocusable(true);
//            etxtBMI.setError(getString(R.string.please_enter_BMI));
//            return false;
//        }
        /*if (Integer.parseInt(etxtBMI.getText().toString().trim())>20){
            etxtBMI.setFocusable(true);
            etxtBMI.setError("BMI should not be greater then 20.");
            return false;
        }*/


        if (!etxtHb.getText().toString().trim().equals("")) {
            if (Double.parseDouble(etxtHb.getText().toString().trim()) < 2) {
                etxtHb.requestFocus();
                etxtHb.setError(getString(R.string.hb_should_greter_than_2));
                return  false;
            }else if (Double.parseDouble(etxtHb.getText().toString().trim()) > 20) {
                etxtHb.requestFocus();
                etxtHb.setError(getString(R.string.hb_should_less_20));
                return  false;
            }
        }
        return true;
    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

        items = sqliteHelper.populateWomenSpinner(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityPregnantWomenMonitoring.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strPregnentWomenMonitor + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(
                        ActivityPregnantWomenMonitoring.this,
                        MainMenuMonitoringActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

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
    //date of delivery
    @SuppressLint("NewApi")
    public void show_callender2(View vw) {
        DialogFragment newFragment = new DatePickerFragment2();
        newFragment.show(getFragmentManager(), "datePicker");

    }
    //date of abortion
    @SuppressLint("NewApi")
    public void show_callender3(View vw) {
        DialogFragment newFragment = new DatePickerFragment3();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }


    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
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
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dt = day + "-" + month + "-" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //etxtDateOfRecord.setText(sdf.format(c.getTime()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            etxtAbortionDate.setText(formattedDate);
//            etxtDateOfRecord.setText(formattedDate);
            etxt_unusual_birth.setText(formattedDate);
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
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String dt = day + "-" + month + "-" + year;
            Calendar c = Calendar.getInstance();
            c.set(year, month, day, 0, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            etxtDateOfDelevery.setText(formattedDate);
            etxtDateOfDelevery.setError(null);
        }
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment3 extends DialogFragment implements
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
            etxtDateOfAbortion.setText(formattedDate);
            etxtDateOfAbortion.setError(null);
        }
    }
}