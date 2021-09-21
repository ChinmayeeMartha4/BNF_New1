package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ChildNutrition;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ShowToast")
public class Activity_EditAbsentChildNutrition extends Activity {


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etxtCurrentDate;
    int don = 0;
    int childID;
    String childNAME, currDate;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    ChildNutrition childnutrition;
    SharedPrefHelper sph;
    RadioButton radioYes, radioNo, tempMigration, permMigration, absent, private_school, on_farm;
    CheckBox chkMigrate;
    RadioGroup radioGroup, radioGroupMigrate;
    TextView txtChildNutrition, txtChildName, txtchildWeight, txtchildHeight,
            txtPregmentWomen, txtMuac, txtChildhbn, txtEdema, txtPhoto, txtCurrentDate,
            txtNutritionHistory, txtFooter;
    EditText etxtSearchByHouseHoldId, etxtWeight, etxtHeight, etxtMuac;
    Button btnChildGo, btnSave;
    Spinner spnChildName, spnHB;
    WebView wvNutritionHistory;
    byte[] image;
    int birth_order, birthOrder;
    ProgressDialog progressDialog;
    String image64 = "", strId;
    ArrayList<Double> age_weight = new ArrayList<Double>();
    ArrayList<Double> age_height = new ArrayList<Double>();
    LinearLayout lnr_pre;
    TextView preheight;
    ImageView imgWeight, imgHeight;
    ;
    int childAgeInMonth = 0;
    int child_gender = 1;
    private ChildNutrition childMonitor;
    private ArrayList<ChildNutrition> list;
    private String strlanguageId, strChildNutrition, strChildName, strEdema,
            strDone, strPhoto, strData, strWeight, strHeight, strFiveYearMon, strMuac, strChildhbn,
            strCurrentDate, strMandatory, strHistory, strGo, strSubmit,
            strChildFullName, strFooter, strYes, strNo, strCancel, strTryAgain, strTempMigration, strPermMigration, strAbsent, strOnfarm, strPrivateschool;
    private long id;
    private Bitmap bitmap;
    private int user_id, dateflag = 0;
    private ImageView img, img1, imgCake, imgHbStatus;
    private String mCurrentPhotoPath = "";
    private ImageLoadingUtils utils;
    private ArrayList<ChildNutrition> prelist;

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
        setContentView(R.layout.activity_absent_child_monitoring);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Edit/ Absent Child Nutrition");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        setLanguage();
        uploadAttendance();
        populateHBspn();

        Bundle b = getIntent().getExtras();
        childID = b.getInt("index");
        childNAME = b.getString("nameofchild");
        currDate = b.getString("currDate");
        user_id = sph.getInt("user_id", 0);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(childNAME);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChildName.setAdapter(adapter);

        spnChildName.setOnItemSelectedListener(new OnItemSelectedListener() {
            private int child_id;

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                child_id = childID;
                imgCake.setVisibility(View.GONE);
                setCake(child_id);
                try {
                    list = new ArrayList<ChildNutrition>();
                    prelist = new ArrayList<ChildNutrition>();
                    list.clear();
                    prelist.clear();
                    wvNutritionHistory.loadUrl("about:blank");
                    list = sqliteHelper.getchildNutrationMonitorDataBy(child_id);
                    prelist = sqliteHelper.getChildPrevious(child_id);
                    if (list.get(0) != null) {
                        wvNutritionHistory.setVisibility(View.VISIBLE);
                        getData(list);
                    }
                    if (prelist.get(0) != null) {
                        preheight.setVisibility(View.VISIBLE);
                        lnr_pre.setVisibility(View.VISIBLE);
                        setData(prelist);
                    }

                } catch (Exception e) {
                    wvNutritionHistory.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spnHB.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int hbvalue = spnHB.getSelectedItemPosition();

                if (hbvalue < 5) {
                    imgHbStatus.setImageResource(R.drawable.sev);
                }
                if (hbvalue > 4 && hbvalue < 15) {
                    imgHbStatus.setImageResource(R.drawable.mod);
                }
                if (hbvalue > 14 && hbvalue < 21) {
                    imgHbStatus.setImageResource(R.drawable.nor);
                }
                if (hbvalue > 20) {
                    imgHbStatus.setImageResource(R.drawable.na);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        etxtWeight.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!s.toString().equalsIgnoreCase("")) {
                        double we = Double.parseDouble(s.toString());
                        if (we <= age_weight.get(0)) {
                            imgWeight.setImageResource(R.drawable.sev);
                        }
                        if (we > age_weight.get(0) & we <= age_weight.get(1)) {
                            imgWeight.setImageResource(R.drawable.mod);
                        }
                        if (we > age_weight.get(1)) {
                            imgWeight.setImageResource(R.drawable.nor);
                        }
                    } else {
                        imgWeight.setImageResource(R.drawable.na);
                    }
                } catch (Exception e) {
                }
            }
        });

        etxtHeight.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!s.toString().equalsIgnoreCase("")) {
                        double we = Double.parseDouble(s.toString());
                        if (we <= age_height.get(0)) {
                            imgHeight.setImageResource(R.drawable.sev);
                        }
                        if (we > age_height.get(0) & we <= age_height.get(1)) {
                            imgHeight.setImageResource(R.drawable.mod);
                        }
                        if (we > age_height.get(1)) {
                            imgHeight.setImageResource(R.drawable.nor);
                        }
                    } else {
                        imgHeight.setImageResource(R.drawable.na);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });


    }

    public void click_edemaYes(View a) {

        if (((RadioButton) a).isChecked()) {

            childMonitor.setEdema("1");
            radioNo.setChecked(false);
        }

    }

    public void ShowSicknessList(View a) {

    }

    public void HideSicknessList(View a) {

    }

    public void ShowSicknessList15(View a) {

    }

    public void HideSicknessList15(View a) {

    }

    public void click_edemaNo(View b) {

        if (((RadioButton) b).isChecked()) {

            childMonitor.setEdema("0");
            radioYes.setChecked(false);
        }
    }

    void setLanguage() {
        strlanguageId = sph.getString("Language", "1");// getting languageId

        strChildNutrition = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildNut, strlanguageId);
        strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANCurrentWeight,
                strlanguageId);
        strHeight = sqliteHelper.LanguageChanges(ConstantValue.LANCurrentheight,
                strlanguageId);
        strMuac = sqliteHelper.LanguageChanges(ConstantValue.LANMuac,
                strlanguageId);
        strChildhbn = sqliteHelper.LanguageChanges(ConstantValue.LANChildHb,
                strlanguageId);
        strEdema = sqliteHelper.LanguageChanges(ConstantValue.LANEdema,
                strlanguageId);
        strGo = sqliteHelper
                .LanguageChanges(ConstantValue.LANGo, strlanguageId);
        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave,
                strlanguageId);
        strCurrentDate = sqliteHelper.LanguageChanges(
                ConstantValue.LANRecordDate, strlanguageId);
        strPhoto = sqliteHelper.LanguageChanges(ConstantValue.LANPhoto,
                strlanguageId);
        strHistory = sqliteHelper.LanguageChanges(ConstantValue.LANHistory,
                strlanguageId);
        strChildFullName = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildName, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                strlanguageId);
        strTryAgain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain,
                strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes,
                strlanguageId);
        strNo = sqliteHelper
                .LanguageChanges(ConstantValue.LANNo, strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel,
                strlanguageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory,
                strlanguageId);
        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone,
                strlanguageId);
        strTempMigration = sqliteHelper.LanguageChanges(ConstantValue.LANTempMig,
                strlanguageId);
        strPermMigration = sqliteHelper.LanguageChanges(ConstantValue.LANPerMig,
                strlanguageId);
        strAbsent = sqliteHelper.LanguageChanges(ConstantValue.LANAbsent,
                strlanguageId);
        strFiveYearMon = sqliteHelper.LanguageChanges(ConstantValue.LANFiveYearMon,
                strlanguageId);

        strOnfarm = sqliteHelper.LanguageChanges(ConstantValue.LANONFIRM,
                strlanguageId);
        strPrivateschool = sqliteHelper.LanguageChanges(ConstantValue.LANPRIVATESCHOOL,
                strlanguageId);
        txtChildNutrition.setText(strChildNutrition);
        txtChildName.setText(strChildFullName);
        txtchildWeight.setText(strWeight);
        txtchildHeight.setText(strHeight);
        txtMuac.setText(strMuac);
        txtChildhbn.setText(strChildhbn);
        txtEdema.setText(strEdema);
        txtCurrentDate.setText(strCurrentDate);
        txtPhoto.setText(strPhoto);
        txtNutritionHistory.setText(strChildNutrition + " " + strHistory);
        //txtFooter.setText(strFooter);
        btnChildGo.setText(strGo);
        btnSave.setText(strSubmit);
        radioYes.setText(strYes);
        radioNo.setText(strNo);
        tempMigration.setText(strTempMigration);
        permMigration.setText(strPermMigration);
        absent.setText(strAbsent);
        on_farm.setText(strOnfarm);
        private_school.setText(strPrivateschool);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fdate = df.format(c.getTime());

        etxtCurrentDate.setText(fdate);

        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void getData(ArrayList<ChildNutrition> list2) {
        strData = "";
        Log.e("data", strData);
        for (int i = 0; i < list2.size(); i++) {
            childMonitor = list2.get(i);

            childMonitor.getWeight();
            childMonitor.getHeight();
            childMonitor.getMuac();
            childMonitor.getDate_of_monitoring();
            childMonitor.getMigration();
            String hdate = childMonitor.getDate_of_monitoring().substring(8, 10) + "-" +
                    childMonitor.getDate_of_monitoring().substring(5, 7) + "-" +
                    childMonitor.getDate_of_monitoring().substring(0, 4);

            if (childMonitor.getMigration().equals("Absent")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "Absent" + "</td>" + "<td>"
                        + "Absent" + "</td>" + "<td>"
                        + "Absent" + "</td>" + "<td>"
                        + "Absent" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration().equals("Temporary Migrate")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "Temporary Migrate" + "</td>" + "<td>"
                        + "Temporary Migrate" + "</td>" + "<td>"
                        + "Temporary Migrate" + "</td>" + "<td>"
                        + "Temporary Migrate" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration().equals("On-farm")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "On-farm" + "</td>" + "<td>"
                        + "On-farm" + "</td>" + "<td>"
                        + "On-farm" + "</td>" + "<td>"
                        + "On-farm" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration().equals("Private school")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "Private school" + "</td>" + "<td>"
                        + "Private school" + "</td>" + "<td>"
                        + "Private school" + "</td>" + "<td>"
                        + "Private school" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration().equals("To Be Verified")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "Death" + "</td>" + "<td>"
                        + "Death" + "</td>" + "<td>"
                        + "Death" + "</td>" + "<td>"
                        + "Death" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration().equals("Permanent Migrate")) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + "Permanent Migrate" + "</td>" + "<td>"
                        + "Permanent Migrate" + "</td>" + "<td>"
                        + "Permanent Migrate" + "</td>" + "<td>"
                        + "Permanent Migrate" + "</td>" + "</tr>";
            } else if (childMonitor.getMigration() == null) {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + childMonitor.getWeight() + "</td>" + "<td>"
                        + childMonitor.getHeight() + "</td>" + "<td>"
                        + childMonitor.getMuac() + "</td>" + "<td>"
                        + childMonitor.getHB() + "</td>" + "</tr>";

            } else {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + childMonitor.getWeight() + "</td>" + "<td>"
                        + childMonitor.getHeight() + "</td>" + "<td>"
                        + childMonitor.getMuac() + "</td>" + "<td>"
                        + childMonitor.getHB() + "</td>" + "</tr>";
            }

        }
        showWebView(strData);
    }

    private void showWebView(String data2) {

        String[] util = Utility.split(strHeight);
        strHeight = util[0];
        util = Utility.split(strWeight);
        strWeight = util[0];
        util = Utility.split(strMuac);
        strMuac = util[0];
        util = Utility.split(strChildhbn);
        strChildhbn = util[0];

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
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + strCurrentDate
                + "</th>" + "<th>" + strWeight + "</th>" + "<th>" + strHeight
                + "</th>" + "<th>" + strMuac + "</th>" + "<th>" + strChildhbn + "</th>" + "</tr>" +
                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";

//        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);

        wvNutritionHistory.getSettings().setAllowContentAccess(true);

//        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);
        wvNutritionHistory.loadDataWithBaseURL("null", myvar, "text/html", "charset=UTF-8", null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater()
                .inflate(R.menu.activity_child_nutrition_monitor, menu);
        return true;
    }

    public void initialization() {

        sqliteHelper = new SqliteHelper(this);
        childnutrition = new ChildNutrition();
        sph = new SharedPrefHelper(this);
        list = new ArrayList<ChildNutrition>();
        serverhelper = new ServerHelper(this);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroupMigrate = (RadioGroup) findViewById(R.id.radioGroupMigrate);
        radioGroupMigrate.setVisibility(View.GONE);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        tempMigration = (RadioButton) findViewById(R.id.tempMigration);
        tempMigration.setVisibility(View.GONE);
        permMigration = (RadioButton) findViewById(R.id.permMigration);
        permMigration.setVisibility(View.GONE);
        absent = (RadioButton) findViewById(R.id.absent);
        absent.setVisibility(View.GONE);

        txtChildNutrition = (TextView) findViewById(R.id.txtChildNutrition);
        txtPregmentWomen = (TextView) findViewById(R.id.txtPregmentWomen);
        txtChildName = (TextView) findViewById(R.id.txtChildName);
        txtchildWeight = (TextView) findViewById(R.id.txtchildWeight);
        txtchildHeight = (TextView) findViewById(R.id.txtchildHeight);
        txtMuac = (TextView) findViewById(R.id.txtMuac);
        txtChildhbn = (TextView) findViewById(R.id.txtChildHBN);
        txtPhoto = (TextView) findViewById(R.id.txtPhoto);
        txtNutritionHistory = (TextView) findViewById(R.id.txtNutritionHistory);
        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        txtEdema = (TextView) findViewById(R.id.txtEdema);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        img1 = (ImageView) findViewById(R.id.calenders);
        img = (ImageView) findViewById(R.id.btnPhoto);
        imgCake = (ImageView) findViewById(R.id.imgCake);
        imgCake.setVisibility(View.GONE);
        imgHbStatus = (ImageView) findViewById(R.id.imgHbStatus);

        spnChildName = (Spinner) findViewById(R.id.spnChildName);
        etxtSearchByHouseHoldId = (EditText) findViewById(R.id.etxtSearchByHouseHoldId);
        etxtSearchByHouseHoldId.setVisibility(View.GONE);
        etxtWeight = (EditText) findViewById(R.id.etxtWeight);
        etxtHeight = (EditText) findViewById(R.id.etxtHeight);
        etxtMuac = (EditText) findViewById(R.id.etxtMuac);
        spnHB = (Spinner) findViewById(R.id.spnChildHBN);
        etxtCurrentDate = (EditText) findViewById(R.id.etxtCurrentDate);
        btnChildGo = (Button) findViewById(R.id.btnChildGo);
        btnChildGo.setVisibility(View.GONE);
        btnSave = (Button) findViewById(R.id.btnSave);
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);
        // btnSave.setEnabled(false);

        on_farm = (RadioButton) findViewById(R.id.on_farm);
        private_school = (RadioButton) findViewById(R.id.private_school);

        lnr_pre = (LinearLayout) findViewById(R.id.lnr_pre);
        preheight = (TextView) findViewById(R.id.preheight);
        lnr_pre.setVisibility(View.VISIBLE);
        preheight.setVisibility(View.VISIBLE);
        imgHeight = (ImageView) findViewById(R.id.imgHeight);
        imgWeight = (ImageView) findViewById(R.id.imgWeight);


    }


    public void setCake(int cid) {

        child_gender = Integer.parseInt(sqliteHelper.GetOneData("gender", "child", "child_id = " + cid));

        String cBirth = sqliteHelper.getDOB(cid);
        Calendar cal5 = Calendar.getInstance();
        int m1 = (Integer.parseInt(cBirth.substring(0, 4)) * 12) + Integer.parseInt(cBirth.substring(5, 7));
        int m2 = (cal5.get(Calendar.YEAR) * 12) + cal5.get(Calendar.MONTH) + 2;
        int diffMonth = m2 - m1;
        childAgeInMonth = diffMonth + 1;

        try {
            if (child_gender == 1) {
                age_height = sqliteHelper.zScoreValue("IDEAL_HEIGHT_BOY", childAgeInMonth);
                age_weight = sqliteHelper.zScoreValue("ideal_weight_boy", childAgeInMonth);
            } else {
                age_height = sqliteHelper.zScoreValue("IDEAL_HEIGHT_GIRL", childAgeInMonth);
                age_weight = sqliteHelper.zScoreValue("ideal_weight_girl", childAgeInMonth);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        if (diffMonth == 6) {
            Display display = getWindowManager().getDefaultDisplay();
            imgCake.getLayoutParams().height = display.getWidth() - 10;
            imgCake.getLayoutParams().width = display.getWidth() - 10;
            imgCake.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NewApi")
    public void btnSave_Click(View vw) {


        String dob1 = sqliteHelper.getDOB(childID);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dob1.substring(8, 10)));
        cal.set(Calendar.MONTH, Integer.parseInt(dob1.substring(5, 7)) - 1);
        cal.set(Calendar.YEAR, Integer.parseInt(dob1.substring(0, 4)));
        Date firstDate = cal.getTime();

        Calendar cal2 = Calendar.getInstance();
        String dob2 = etxtCurrentDate.getText().toString().trim();
        ;
        cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dob2.substring(8, 10)));
        cal2.set(Calendar.MONTH, Integer.parseInt(dob2.substring(5, 7)) - 1);
        cal2.set(Calendar.YEAR, Integer.parseInt(dob2.substring(0, 4)));
        Date secondDate = cal2.getTime();
        long diff = (secondDate.getTime() - firstDate.getTime());
        int diffday = (int) (diff / (1000 * 60 * 60 * 24));
        if (diffday > 1827) {
            Log.e("............", Integer.toString(diffday));
            AlertDialog alertDialog = new AlertDialog.Builder(Activity_EditAbsentChildNutrition.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage(strFiveYearMon);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Activity_EditAbsentChildNutrition.this, Activity_EditAbsentChildNutrition.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        } else {
            try {

                ArrayList<Views> arr = new ArrayList<Views>();
                String childname = childNAME;
                int intChild_id = childID;

                childnutrition.setChild_id(intChild_id);
                String[] latestData = sqliteHelper.getLatestHeight(intChild_id);
                int latestHeight = Integer.parseInt(latestData[0]);
                float latestWeight = Float.parseFloat(latestData[1]);
                childnutrition.setChild_name(childname);
                childnutrition.setWeight(etxtWeight.getText().toString().trim());
                childnutrition.setHeight(etxtHeight.getText().toString().trim());
                childnutrition.setMuac(etxtMuac.getText().toString().trim());
                String childhb = "";
                if (spnHB.getSelectedItem().toString().equals("NA - Other issue")) {
                    childhb = "NA - Other issue";
                } else if (spnHB.getSelectedItem().toString().equals("NA-Child not Co-operating")) {
                    childhb = "NA-Child not Co-operating";
                } else {
                    childhb = spnHB.getSelectedItem().toString();
                }
                childnutrition.setHB(childhb);
                childnutrition.setMultimedia(image64);
                childnutrition.setDreason("");

                Time time = new Time();
                time.setToNow();
                String strDate = etxtCurrentDate.getText().toString().trim() + " " + time.hour + ":" + time.minute + ":" + time.second;
                childnutrition.setDate_of_monitoring(strDate);

                if (childnutrition.getChild_name().equals("")
                        || etxtWeight.getText().toString().equals("")
                        || Float.parseFloat(etxtWeight.getText().toString()) < 0.800
                        || Float.parseFloat(etxtWeight.getText().toString()) > 25.000
                        || etxtHeight.getText().toString().equals("")

                        || Float.parseFloat(etxtHeight.getText().toString()) < 40.00
                        || Float.parseFloat(etxtHeight.getText().toString()) > 130.00
                        || etxtCurrentDate.getText().toString().equals("")
                        || etxtMuac.getText().toString().equals("")
                        || Float.parseFloat(etxtMuac.getText().toString()) < 9.00
                        || Float.parseFloat(etxtMuac.getText().toString()) > 22.00
                        || image64.equals("")) {

                    if (childnutrition.getChild_name().equals("")) {
                        Toast.makeText(getApplicationContext(),
                                strChildFullName + " " + strMandatory, Toast.LENGTH_LONG).show();
                    }
                    if (etxtWeight.getText().toString().equals("")) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + " " + strMandatory);
                    }
                    if (Float.parseFloat(etxtWeight.getText().toString()) < 0.800) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + " should be greater than 800 gram.");
                        etxtWeight.setFocusable(true);
                    }

                    if (Float.parseFloat(etxtWeight.getText().toString()) > 25.000) {
                        String[] util = Utility.split(strWeight);
                        String weight = util[0];
                        etxtWeight.setError(weight + " should be less than 25 kg.");
                        etxtWeight.setFocusable(true);
                    }


                    //if (etxtHeight.getText().toString().equals("")) {
                    //String[] util = Utility.split(strHeight);
                    //String height = util[0];
                    //etxtHeight.setError(height + " " + strMandatory);
                    //}

                    if (Float.parseFloat(etxtHeight.getText().toString()) < 40.00) {
                        String[] util = Utility.split(strHeight);
                        String height = util[0];
                        etxtHeight.setError(height + "should be greater than 40 cm.");
                        etxtHeight.setFocusable(true);
                    }

                    if (Float.parseFloat(etxtHeight.getText().toString()) > 130.00) {
                        String[] util = Utility.split(strHeight);
                        String height = util[0];
                        etxtHeight.setError(height + "should be less than 130 cm.");
                        etxtHeight.setFocusable(true);
                    }
                    if (etxtCurrentDate.getText().toString().equals("")) {
                        String[] util = Utility.splitBySpace(strCurrentDate);
                        String date = util[0];
                        etxtCurrentDate.setError(date + " " + strMandatory);
                    }
                    if (etxtMuac.getText().toString().equals("")) {
                        String[] util = Utility.split(strMuac);
                        String muac = util[0];
                        etxtMuac.setError(muac + " " + strMandatory);
                    }

                    if (Float.parseFloat(etxtMuac.getText().toString()) < 9.00) {
                        String[] util = Utility.split(strMuac);
                        String muac = util[0];
                        etxtMuac.setError(muac + " should be greater than 9 cm ");
                    }

                    if (Float.parseFloat(etxtMuac.getText().toString()) > 22.00) {
                        String[] util = Utility.split(strMuac);
                        String muac = util[0];
                        etxtMuac.setError(muac + " should be less than 22 cm ");
                    }
                    if (image64.equals("")) {

                        Toast.makeText(getApplicationContext(), "Photo is mandatory.", Toast.LENGTH_LONG).show();
                    }


                } else {

                    etxtWeight.setError(null);
                    etxtHeight.setError(null);
                    etxtMuac.setError(null);
                    etxtSearchByHouseHoldId.setError(null);
                    etxtCurrentDate.setError(null);

                    if (Float.parseFloat(etxtWeight.getText().toString()) < latestWeight) {

                        ArrayList<String> reas = new ArrayList<String>();
                        reas = sqliteHelper.getDreason();
                        String[] re = reas.toArray(new String[reas.size()]);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                        builder1.setTitle("Select reason for weight loss.");
                        builder1.setSingleChoiceItems(re, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                don = which + 1;
                            }
                        });
                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                childnutrition.setDreason(Integer.toString(don));
                                id = sqliteHelper.editAbsentChildNutrition(childnutrition, currDate);
                                if (id > 0) {
                                    etxtWeight.setText("");
                                    etxtHeight.setText("");
                                    etxtMuac.setText("");
                                    etxtCurrentDate.setText("");
                                    Toast.makeText(getApplicationContext(),
                                            strChildNutrition + " " + strDone + "!", Toast.LENGTH_LONG)
                                            .show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG);
                                }
                                dialog.dismiss();
                            }
                        });

                        builder1.setNegativeButton("Back",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder1.create();
                        alert.show();
                    } else {
                        id = sqliteHelper.editAbsentChildNutrition(childnutrition, currDate);
                        if (id > 0) {
                            sqliteHelper.updateViewAbsentStatus(childnutrition.getChild_id(), "child_nutrition_monitoring");
                            etxtWeight.setText("");
                            etxtHeight.setText("");
                            etxtMuac.setText("");
                            etxtCurrentDate.setText("");
                            Toast.makeText(getApplicationContext(),
                                    strChildNutrition + " " + strDone + "!", Toast.LENGTH_LONG)
                                    .show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG);
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                        bitmap = imageLatLogHeper.compressImage(file + "", childNAME);
                        img.setImageBitmap(bitmap);
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
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", childNAME);
                                img.setImageBitmap(bitmap);

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
//                        img.setImageBitmap(bitmap);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strChildNutrition + "?");
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

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }

    public void populateHBspn() {
        String hbs[] = {"5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12", "12.5", "13", "13.5", "14", "14.5", "15", "NA - Other issue", "NA-Child not Co-operating", "Machine not available"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hbs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnHB.setAdapter(spinnerArrayAdapter);
    }

    private void setData(ArrayList<ChildNutrition> prelist) {
        String height = prelist.get(0).getHeight();
        String start_dt = prelist.get(0).getDate_of_monitoring();
        String date = "";
        try {
            date = new SimpleDateFormat("MMM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(start_dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        preheight.setText(date + " = " + height + " cm");
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
            etxtCurrentDate.setText(formattedDate);
        }
    }
}
