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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
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
import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Views;
import com.example.mhealth.utils.AlertDialogClass;
import com.example.mhealth.utils.CommonMethods;

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
import java.util.Locale;

@SuppressLint("ShowToast")
public class ActivityChildNutritionMonitor extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    static String death_date = "";
    static EditText etxtDeathDate;
    int childAgeInMonth = 0;
    String child_gender = "M";
    private String searchData="";
    TextView tvchildmeal;
    public static String age_in_month="";

    ArrayList<Double> age_weight = new ArrayList<Double>();
    ArrayList<Double> age_height = new ArrayList<Double>();
    ArrayList<SpinnerHelper> spinnerchildData = new ArrayList<>();
    String[] deathReason;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    ChildNutrition childnutrition;
    SharedPrefHelper sph;
    LinearLayout inr_death_date, lnr_pre,llTypeOfEdema,llLinkedWithMedical;
    String sick_id, sick_id15,strHaveAdeama="";
    RadioButton radioYes, radioNo, tempMigration, permMigration, death, absent,
            rb_sick_no, rb_sick_yes, rb_sick_no15, rb_sick_yes15,rbPitting,rbBilateral,
            rbMedicalFacilityYes,rbMedicalFacilityNo,rb_medicaly_critical_yes,rb_medicaly_critical_no
            ,rb_sent_to_nrc_yes,rb_sent_to_nrc_no,rb_nutrition_by_bnf_yes,rb_nutrition_by_bnf_no,rb_garden_setup_yes
            ,rb_garden_setup_no,rb_nutrition_garden_yes,rb_nutrition_garden_no;
    CheckBox cbGrain, cbDal, cbMilkrelated, cbEggs, cbFleshfood, cbVitA, cbFruits;
    CheckBox cbDiarrhoea, cbCough, cbFever, cbDifficultybreathing, cbOther;
    CheckBox cbDiarrhoea_no15, cbCough_no15, cbFever_no15, cbDifficultybreathing_no15, cbOther_no15;
    RadioGroup radioGroup, radioGroupMigrate, rg_sick, rg_sick15,rgPOET,rgMedicalFacility, rgAgeVaccination,
            rgchildPremature, rgchildbreastfeeding,
            rgmotherbreastmilk, rgchildVitA, rgchilddeworming_6, rgchildIFA, rgchildVaccinationStatus
    ,rg_medicaly_critical,rg_sent_to_nrc,rg_nutrition_by_bnf,rg_garden_setup,rg_nutrition_garden;
    TextView txtChildNutrition, txtChildName, txtchildWeight, txtchildHeight,
            txtPregmentWomen, txtMuac, txtChildhbn, txtEdema, txtPhoto, txtCurrentDate,
            txtNutritionHistory, txtFooter, tvNotification, txtSick, txtSick15, txtGps, preheight, tvchildVaccinationStatus;
    EditText etxtSearchByHouseHoldId, etxtWeight, etxtHeight, etxtMuac,et_medicaly_critical,et_sent_to_nrc;
    TextView txtDeathReason;
    Button btnChildGo, btnSave;
    Spinner spnChildName, spnHB, spn_sickname, spn_sickname15, spnDeathReason;
    EditText et_searchchildname;
    WebView wvNutritionHistory;

    String image64 = "", strId="";
    int flag2 = 0;
    RadioButton rbPresent, private_school, on_farm;
    String[] latestData;
    float latestHeight;
    float latestWeight;
    ArrayList<Integer> absent_child;
    ArrayList<ChildNutrition> migrate_child;
    ArrayList<ChildNutrition> monitor_child;
    ArrayList<String> date_child;
    String gps;
    String fdate;
    private int child_id;
    private String childID;
    private String child_name="";
    private ChildNutrition childMonitor;
    private ArrayList<ChildNutrition> list;
    private ArrayList<ChildNutrition> prelist;
    private String strlanguageId, strChildNutrition, strEdema,
            strDone, strPhoto, strData="", strWeight, strHeight, strFiveYearMon, strMuac, strChildhbn,
            strCurrentDate, strMandatory, strHistory, strGo, strSubmit,
            strChildFullName, strFooter, strYes, strNo, strCancel, strTryAgain, strTempMigration,
            strPermMigration, strDeath, strAbsent, strNotification, strSick, strLat,
            strLang, strSick15, strPhotoInsHead, strPhotoIns, strOnfarm, strPrivateschool,
            strPOET,strMedicalFacility, strAgeVaccination, strchildPremature, strchildbreastfeeding,
            strmotherbreastmilk, strchildVitA, strchilddeworming_6, strchildIFA,
            strchildVaccinationStatus,strNutritionGarden,strGardenSetup,strNutritionBnf,strSentToNrc,strMedicaly;
    private long id;
    private Bitmap bitmap;
    private int user_id, flag = 0, migrationflag = 0, dateflag = 0, absentFlag = 0, temporaryMigrateFlag = 0,
            onFarmFlag = 0, privateSchoolFlag = 0, permanentMigrateFlag = 0, deathFlag = 0;
    private ImageView img, img1, imgCake, imgHbStatus, imgWeight, imgHeight, imgMuac;
    private String mCurrentPhotoPath = "";

    private String formattedDate;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    EditText etChildsick, etxtAdolescentHb, etxtchildmealnumber;
    public static EditText etxtDateOfScreening;
    android.app.Dialog info_alert;
    public static android.app.Dialog submit_alert;
    ImageView Iv_image;

    LinearLayout llAgeVaccination, llchildPremature, llchildbreastfeeding, llmotherbreastmilk, llchildmeal,
            llchildmealnumber, llchildVitA6Months,
            llchilddeworming_6, llchildIFA, llchildVaccinationStatus, llSick,llSick_no15, ll_muac, ll_muac_et;

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
        setContentView(R.layout.activity_activity_child_nutrition_monitor);

        initialization();
        setLanguages();
        uploadAttendance();
        populateHBspn();

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }

        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        tvTitleText.setText(R.string.child_nutrition_monitoring);
        //set GPS on button
        Button btnGps=findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);

        tvchildmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass.showIMageDialog(context);
            }
        });

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioYes:
                        strHaveAdeama="Yes";
                        llTypeOfEdema.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioNo:
                        strHaveAdeama="No";
                        llTypeOfEdema.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgPOET.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbPitting:
                        strPOET="Pitting";
                        break;
                    case R.id.rbBilateral:
                        strPOET="Bilateral";
                        break;
                }
            }
        });

        rgMedicalFacility.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbMedicalFacilityYes:
                        strMedicalFacility="Yes";
                        break;
                    case R.id.rbMedicalFacilityNo:
                        strMedicalFacility="No";
                        break;
                }
            }
        });

        rgAgeVaccination.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbAgeVaccinationRegular:
                        strAgeVaccination="Yes";
                        break;
                    case R.id.rbAgeVaccinationIrregular:
                        strAgeVaccination="No";
                        break;
                    case R.id.rbAgeVaccinationNotTaken:
                        strAgeVaccination="Not Taken";
                        break;
                }
            }
        });

        rgchildPremature.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchildPrematureYes:
                        strchildPremature="Yes";
                        break;
                    case R.id.rbchildPrematureNo:
                        strchildPremature="No";
                        break;
                    case R.id.rbchildPrematureDontremember:
                        strchildPremature="Don’t Remember";
                        break;

                }
            }
        });

        rgchildbreastfeeding.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchildbreastfeedingYes:
                        strchildbreastfeeding="Yes";
                        break;
                    case R.id.rbchildbreastfeedingNo:
                        strchildbreastfeeding="No";
                        break;

                }
            }
        });

        rgmotherbreastmilk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbmotherbreastmilkYes:
                        strmotherbreastmilk="Yes";
                        break;
                    case R.id.rbmotherbreastmilkNo:
                        strmotherbreastmilk="No";
                        break;

                }
            }
        });

        rgchildVitA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchildVitAYes:
                        strchildVitA="Yes";
                        break;
                    case R.id.rbchildVitANo:
                        strchildVitA="No";
                        break;

                }
            }
        });

        rgchilddeworming_6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchilddeworming_6Yes:
                        strchilddeworming_6="Yes";
                        break;
                    case R.id.rbchilddeworming_6No:
                        strchilddeworming_6="No";
                        break;

                }
            }
        });

        rgchildIFA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchildIFAYes:
                        strchildIFA="Yes";
                        break;
                    case R.id.rbchildIFANo:
                        strchildIFA="No";
                        break;

                }
            }
        });

        rgchildVaccinationStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbchildVaccinationStatusFullyVaccinated:
                        strchildVaccinationStatus="Fully vaccinated";
                        break;
                    case R.id.rbchildVaccinationStatusPartiallyVaccinated:
                        strchildVaccinationStatus="Partially vaccinated";
                        break;
                    case R.id.rbchildVaccinationStatusNotVaccinated:
                        strchildVaccinationStatus="Not vaccinated at all";
                        break;

                }
            }
        });

        rg_medicaly_critical.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_medicaly_critical_yes:
                        strMedicaly="Yes";
                        et_medicaly_critical.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_medicaly_critical_no:
                        strMedicaly="No";
                        et_medicaly_critical.setVisibility(View.GONE);
                        break;

                }
            }
        });

        rg_sent_to_nrc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_sent_to_nrc_yes:
                        strSentToNrc="Yes";
                        et_sent_to_nrc.setVisibility(View.VISIBLE);
                        Iv_image.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_sent_to_nrc_no:
                        strSentToNrc="No";
                        et_sent_to_nrc.setVisibility(View.GONE);
                        Iv_image.setVisibility(View.GONE);
                        break;

                }
            }
        });

        rg_nutrition_by_bnf.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_nutrition_by_bnf_yes:
                        strNutritionBnf="Yes";
                        break;
                    case R.id.rb_nutrition_by_bnf_no:
                        strNutritionBnf="No";
                        break;

                }
            }
        });

        rg_garden_setup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_garden_setup_yes:
                        strGardenSetup="Yes";
                        break;
                    case R.id.rb_garden_setup_no:
                        strGardenSetup="No";
                        break;

                }
            }
        });
        rg_nutrition_garden.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_nutrition_garden_yes:
                        strNutritionGarden="Yes";
                        break;
                    case R.id.rb_nutrition_garden_no:
                        strNutritionGarden="No";
                        break;

                }
            }
        });

        txtEdema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                                Intent intent = new Intent(
                                        ActivityChildNutritionMonitor.this,
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

        monitor_child = sqliteHelper.getMonitoringData(user_id);
        if (monitor_child != null) {
            for (int i = 0; i < monitor_child.size(); i++) {
                absent_child.add(monitor_child.get(i).getChild_id());
            }
            for (int i = 0; i < absent_child.size(); i++) {
                migrate_child.add(sqliteHelper.getChildStatusData(absent_child.get(i)));
            }
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        formattedDate = df.format(c.getTime());

        list = new ArrayList<ChildNutrition>();
        prelist = new ArrayList<ChildNutrition>();
        list.clear();
        prelist.clear();

        deathReason = getResources().getStringArray(R.array.death_reasons);
        ArrayAdapter<String> reasonOfDeath = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deathReason);
        reasonOfDeath.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnDeathReason.setPrompt("Death Reason");
        spnDeathReason.setAdapter(reasonOfDeath);

        et_searchchildname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                {

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData = et_searchchildname.getText().toString();
                strId="";
                //populatechildNam(spinnerchildData, "child_name", searchData);
                populatechildNam();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        etxtSearchByHouseHoldId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                strId=""; //when you remove the text from household search box
                setChildNameSpinner();
            }
        });

        //child spinner
        setChildNameSpinner();

        tvchildVaccinationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass.showDialog(context,getString(R.string.vaccination_status), getString(R.string.cross_check_mcp_card));
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
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) < 7) {
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.sev);
                        }
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 7 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <=9.9) {
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.mod);
                        }
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 10 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <= 10.9 ) {
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.light_yellow);
                        }
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 11) {
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.nor);
                        }

                    }
                } catch (Exception e) {
                }
            }
        });

        etxtDateOfScreening.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    String childBirth = sqliteHelper.getDOB(child_id);

                    if(etxtDateOfScreening.getText().toString().equals("")){
                    }else {
                        String dobString=childBirth;

                        getMonthsNumber(childBirth);
                    }
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayList<String> sickList = sqliteHelper.getSickList();
        ArrayAdapter<String> asdf = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sickList);
        asdf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spn_sickname.setAdapter(asdf);
        spn_sickname15.setAdapter(asdf);

        spn_sickname.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_item = spn_sickname.getSelectedItem().toString();
                if (!selected_item.equalsIgnoreCase("Select Sickness")) {
                    sick_id = String.valueOf(sqliteHelper.getIdOfSickList(selected_item));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spn_sickname15.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_item = spn_sickname15.getSelectedItem().toString();
                if (!selected_item.equalsIgnoreCase("Select Sickness")) {
                    sick_id15 = String.valueOf(sqliteHelper.getIdOfSickList(selected_item));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                            imgWeight.setVisibility(View.VISIBLE);
                            imgWeight.setImageResource(R.drawable.sev);
                        }
                        if (we > age_weight.get(0) & we <= age_weight.get(1)) {
                            imgWeight.setVisibility(View.VISIBLE);
                            imgWeight.setImageResource(R.drawable.mod);
                        }
                        if (we > age_weight.get(1)) {
                            imgWeight.setVisibility(View.VISIBLE);
                            imgWeight.setImageResource(R.drawable.nor);
                        }
                    } else {
                        imgWeight.setVisibility(View.VISIBLE);
                        imgWeight.setImageResource(R.drawable.na);
                    }
                } catch (Exception e) {
                }
            }
        });

        etxtMuac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    double muac= Double.parseDouble(etxtMuac.getText().toString().trim());
                        if (muac < 11.5) {
                            imgMuac.setVisibility(View.VISIBLE);
                            imgMuac.setImageResource(R.drawable.sev);
                        }else if (muac >= 11.5 & muac < 12.5) {
                            imgMuac.setVisibility(View.VISIBLE);
                            imgMuac.setImageResource(R.drawable.mod);
                        }else if (muac >=12.5) {
                            imgMuac.setVisibility(View.VISIBLE);
                            imgMuac.setImageResource(R.drawable.nor);
                        } else {
                            imgMuac.setVisibility(View.VISIBLE);
                            imgMuac.setImageResource(R.drawable.na);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
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
                            imgHeight.setVisibility(View.VISIBLE);
                            imgHeight.setImageResource(R.drawable.sev);
                        }
                        if (we > age_height.get(0) & we <= age_height.get(1)) {
                            imgHeight.setVisibility(View.VISIBLE);
                            imgHeight.setImageResource(R.drawable.mod);
                        }
                        if (we > age_height.get(1)) {
                            imgHeight.setVisibility(View.VISIBLE);
                            imgHeight.setImageResource(R.drawable.nor);
                        }
                    } else {
                        imgHeight.setVisibility(View.VISIBLE);
                        imgHeight.setImageResource(R.drawable.na);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }

    private void setChildNameSpinner() {

        spnChildName.setAdapter(new SpinnerAdapter1(getApplicationContext(), monitor_child, migrate_child, formattedDate));
        spnChildName.setPrompt(getString(R.string.select_child));

        spnChildName.setOnItemSelectedListener(new OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (strId.equals("") && searchData.equals("")) {
                    child_id = monitor_child.get(i).getChild_id();
                    child_name = monitor_child.get(i).getName_of_child();
                }else {
                    childID = getSelectedValue(spnChildName);
                    if(!childID.equals(""))
                    child_id= Integer.parseInt(childID);
                }
                if (child_id != 0) {
                    flag = 0;
                    flag2 = 0;
                    imgCake.setVisibility(View.GONE);
                    setCake(child_id, age_in_month);
                    getDate(child_id);
                }
                try {
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
                    preheight.setVisibility(View.GONE);
                    lnr_pre.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getMonthsNumber(String childBirth) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(childBirth);
            dob.setTime(date1);

            Date date2 = format.parse(etxtDateOfScreening.getText().toString());
            today.setTime(date2);

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
        long age = (year * 12) + month;
        getBirthDate(age);
        Log.e("TAG", "onTextChanged: months"+  age);
    }

    private int getDate(int child_id) {
        String childBirth = sqliteHelper.getDOB(child_id);

        String dobString=childBirth;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = format.parse(dobString);
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
        getBirthDate(Long.parseLong(age_in_month));
        setCake(child_id, age_in_month);

        int calAge = (Integer.parseInt(age_in_month)); //for years return this.
        return calAge;
    }


    private void getBirthDate(long months) {
        if(months<=6){
            llchildbreastfeeding.setVisibility(View.VISIBLE);
            ll_muac.setVisibility(View.GONE);
            ll_muac_et.setVisibility(View.GONE);
        }else{
            llchildbreastfeeding.setVisibility(View.GONE);
        }

        if(months<=12){
            llmotherbreastmilk.setVisibility(View.VISIBLE);
        }else{
            llmotherbreastmilk.setVisibility(View.GONE);
        }

        if(months>6){
            llchildmeal.setVisibility(View.VISIBLE);
            ll_muac.setVisibility(View.VISIBLE);
            ll_muac_et.setVisibility(View.VISIBLE);
        }else{
            llchildmeal.setVisibility(View.GONE);
        }

        if(months>1){
            llchildIFA.setVisibility(View.VISIBLE);
        }else{
            llchildIFA.setVisibility(View.GONE);
        }

        if(months>9){
            llchildVitA6Months.setVisibility(View.VISIBLE);
        }else{
            llchildVitA6Months.setVisibility(View.GONE);
        }

        if(months>12){
            llchilddeworming_6.setVisibility(View.VISIBLE);
            llchildVaccinationStatus.setVisibility(View.VISIBLE);
        }else{
            llchilddeworming_6.setVisibility(View.GONE);
            llchildVaccinationStatus.setVisibility(View.GONE);
        }


    }

    private void showDialog() {
        info_alert = new android.app.Dialog(this);

        info_alert.setContentView(R.layout.info_alert_dialog);
        info_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = info_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        Button btnOk = (Button) info_alert.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                info_alert.dismiss();
            }
        });

        info_alert.show();
        info_alert.setCanceledOnTouchOutside(false);
    }

    public void click_edemaYes(View a) {

        if (((RadioButton) a).isChecked()) {

            childMonitor.setEdema("1");
            radioNo.setChecked(false);
        }

    }

    public void ShowSicknessList(View v) {
//        spn_sickname.setVisibility(View.VISIBLE);
        childnutrition.setSickYesNo("Yes");
        txtSick15.setVisibility(View.GONE);
        rg_sick15.setVisibility(View.GONE);
        spn_sickname15.setVisibility(View.GONE);
        childnutrition.setSickYesNo15("");
        llLinkedWithMedical.setVisibility(View.VISIBLE);
        llSick.setVisibility(View.VISIBLE);
        llSick_no15.setVisibility(View.GONE);
    }

    public void HideSicknessList(View v) {
//        spn_sickname.setVisibility(View.GONE);
        childnutrition.setSickYesNo("No");
        childnutrition.setSickName("");
        txtSick15.setVisibility(View.VISIBLE);
        rg_sick15.setVisibility(View.VISIBLE);
        childnutrition.setSickYesNo15("");
        llLinkedWithMedical.setVisibility(View.GONE);
        llSick.setVisibility(View.GONE);
    }

    public void ShowSicknessList15(View v) {
//        spn_sickname15.setVisibility(View.VISIBLE);
        childnutrition.setSickYesNo15("Yes");
        llSick_no15.setVisibility(View.VISIBLE);
        llSick.setVisibility(View.GONE);
    }

    public void HideSicknessList15(View v) {
        childnutrition.setSickYesNo15("No");
        childnutrition.setSickName15("");
        llSick_no15.setVisibility(View.GONE);
    }

    public void click_edemaNo(View b) {
        if (((RadioButton) b).isChecked()) {
            childMonitor.setEdema("0");
            radioYes.setChecked(false);
        }
    }

    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

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


    void setLanguages() {
        strlanguageId = sph.getString("Language", "");// getting languageId

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
        strDeath = sqliteHelper.LanguageChanges(ConstantValue.LANDeath,
                strlanguageId);
        strAbsent = sqliteHelper.LanguageChanges(ConstantValue.LANAbsent,
                strlanguageId);
        strFiveYearMon = sqliteHelper.LanguageChanges(ConstantValue.LANFiveYearMon,
                strlanguageId);
        strNotification = sqliteHelper.LanguageChanges(ConstantValue.LANNotice,
                strlanguageId);

        strSick = sqliteHelper.LanguageChanges(ConstantValue.LANChildSick,
                strlanguageId);
        strSick15 = sqliteHelper.LanguageChanges(ConstantValue.LANChildSickIn15,
                strlanguageId);

        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, strlanguageId);

        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, strlanguageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, strlanguageId);

        strPhotoInsHead = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsHead, strlanguageId);
        strPhotoIns = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsChild, strlanguageId);

        strOnfarm = sqliteHelper.LanguageChanges(ConstantValue.LANONFIRM,
                strlanguageId);
        strPrivateschool = sqliteHelper.LanguageChanges(ConstantValue.LANPRIVATESCHOOL,
                strlanguageId);
        //, strPhotoIns


        txtGps.setText(gps);
        txtChildNutrition.setText(strChildNutrition);
//        txtChildName.setText(R.string.name_of_the_child);
        txtchildWeight.setText(strWeight);
        txtchildHeight.setText(strHeight);
        txtMuac.setText(strMuac);
        txtChildhbn.setText(strChildhbn);
        txtEdema.setText("Odeame");
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
        death.setText(strDeath);
        absent.setText(strAbsent);

        //added by arun after 3.9
        on_farm.setText(strOnfarm);
        private_school.setText(strPrivateschool);

        txtSick.setText("Is the child sick ?");
        rb_sick_no.setText(strNo);
        rb_sick_yes.setText(strYes);

//        txtSick15.setText(getString(R.string.child_suffered_30_days));
        rb_sick_no15.setText(strNo);
        rb_sick_yes15.setText(strYes);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        fdate = df.format(c.getTime());


        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());
        tvNotification.setText(strNotification);
        tvNotification.setVisibility(View.GONE);


    }

    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
    }

    private void populateList(Spinner spinner, String tableName, String col_id,
                              String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        ArrayAdapter<SpinnerHelper> adapter = null;
        items = sqliteHelper.populateChildSpinner(tableName, col_id, col_value,
                label, whr);
        if (items.size() == 0) {
            SpinnerHelper spinnerHelper = new SpinnerHelper("", "");
            items.add(spinnerHelper);
        }
        adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildNutritionMonitor.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    protected void getData(ArrayList<ChildNutrition> list2) {
        strData = "";
        Log.e("data", strData);
        for (int i = 0; i < list2.size(); i++) {
            childMonitor = list2.get(i);
            childMonitor.getMigration();
            String hdate = childMonitor.getDate_of_monitoring().substring(8, 10) + "-" +
                    childMonitor.getDate_of_monitoring().substring(5, 7) + "-" +
                    childMonitor.getDate_of_monitoring().substring(0, 4);


            if (childMonitor.getMigration() != null && !childMonitor.getMigration().equals("")) {
                switch (childMonitor.getMigration()) {
                    case "Absent":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "</tr>";
                        break;
                    case "Temporary Migrate":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "</tr>";
                        break;
                    case "On-farm":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "</tr>";
                        break;
                    case "Private school":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "</tr>";
                        break;
                    case "Permanent Migrate":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "</tr>";
                        break;
                    default:
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + childMonitor.getWeight() + "</td>" + "<td>"
                                + childMonitor.getHeight() + "</td>" + "<td>"
                                + childMonitor.getMuac() + "</td>" + "<td>"
                                + childMonitor.getHB() + "</td>" + "</tr>";
                }
            }
            else {
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
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + strCurrentDate
                + "</th>" + "<th>" + strWeight + "</th>" + "<th>" + strHeight
                + "</th>" + "<th>" + strMuac + "</th>" + "<th>" + strChildhbn + "</th>" + "</tr>" +
                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";


        wvNutritionHistory.getSettings().setAllowContentAccess(true);

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
        preheight = (TextView) findViewById(R.id.preheight);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroupMigrate = (RadioGroup) findViewById(R.id.radioGroupMigrate);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        tempMigration = (RadioButton) findViewById(R.id.tempMigration);
        permMigration = (RadioButton) findViewById(R.id.permMigration);
        death = (RadioButton) findViewById(R.id.death);
        absent = (RadioButton) findViewById(R.id.absent);
        tvTitleText=findViewById(R.id.tvTitleText);
        etChildsick= (EditText) findViewById(R.id.etChildsick);
        etxtAdolescentHb= (EditText) findViewById(R.id.etxtAdolescentHb);
        etxtchildmealnumber= (EditText) findViewById(R.id.etxtchildmealnumber);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        etxtDateOfScreening=findViewById(R.id.etxtDateOfScreening);
        llAgeVaccination=findViewById(R.id.llAgeVaccination);
        llchildPremature=findViewById(R.id.llchildPremature);
        llchildbreastfeeding=findViewById(R.id.llchildbreastfeeding);
        llmotherbreastmilk=findViewById(R.id.llmotherbreastmilk);
        llchildmeal=findViewById(R.id.llchildmeal);
        llchildmealnumber=findViewById(R.id.llchildmealnumber);
        llchildVitA6Months=findViewById(R.id.llchildVitA6Months);
        llchilddeworming_6=findViewById(R.id.llchilddeworming_6);
        llchildIFA=findViewById(R.id.llchildIFA);
        llchildVaccinationStatus=findViewById(R.id.llchildVaccinationStatus);
        llSick=findViewById(R.id.llSick);
        llSick_no15=findViewById(R.id.llSick_no15);
        ll_muac=findViewById(R.id.ll_muac);
        ll_muac_et=findViewById(R.id.ll_muac_et);

        tvchildmeal = (TextView) findViewById(R.id.tvchildmeal);
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
        tvNotification = (TextView) findViewById(R.id.tvNotification);
        lnr_pre = (LinearLayout) findViewById(R.id.lnr_pre);
        img1 = (ImageView) findViewById(R.id.calenders);
        img = (ImageView) findViewById(R.id.btnPhoto);
        imgCake = (ImageView) findViewById(R.id.imgCake);
        imgCake.setVisibility(View.GONE);
        imgHbStatus = (ImageView) findViewById(R.id.imgHbStatus);
        imgWeight = (ImageView) findViewById(R.id.imgWeight);
        imgHeight = (ImageView) findViewById(R.id.imgHeight);
        imgMuac = (ImageView) findViewById(R.id.imgMuac);

        spnChildName = (Spinner) findViewById(R.id.spnChildName);
        etxtSearchByHouseHoldId = (EditText) findViewById(R.id.etxtSearchByHouseHoldId);
        etxtWeight = (EditText) findViewById(R.id.etxtWeight);
        et_searchchildname = (EditText) findViewById(R.id.et_searchchildname);
        etxtHeight = (EditText) findViewById(R.id.etxtHeight);
        etxtMuac = (EditText) findViewById(R.id.etxtMuac);
        spnHB = (Spinner) findViewById(R.id.spnChildHBN);

        etxtDeathDate = (EditText) findViewById(R.id.etxtDeathDate);
        btnChildGo = (Button) findViewById(R.id.btnChildGo);
        btnSave = (Button) findViewById(R.id.btnSave);
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);

        txtSick = (TextView) findViewById(R.id.txtSick);
        txtGps = (TextView) findViewById(R.id.txtGps);
        txtDeathReason = (TextView) findViewById(R.id.txtDeathReason);
        rb_sick_no = (RadioButton) findViewById(R.id.rb_sick_no);
        rb_sick_yes = (RadioButton) findViewById(R.id.rb_sick_yes);
        spn_sickname = (Spinner) findViewById(R.id.spn_sickname);
        rg_sick = (RadioGroup) findViewById(R.id.rg_sick);

        tvchildVaccinationStatus = (TextView) findViewById(R.id.tvchildVaccinationStatus);
        txtSick15 = (TextView) findViewById(R.id.txtSick15);
        rb_sick_no15 = (RadioButton) findViewById(R.id.rb_sick_no15);
        rb_sick_yes15 = (RadioButton) findViewById(R.id.rb_sick_yes15);
        spn_sickname15 = (Spinner) findViewById(R.id.spn_sickname15);
        spnDeathReason = (Spinner) findViewById(R.id.spnDeathReason);
        rg_sick15 = (RadioGroup) findViewById(R.id.rg_sick15);
        // btnSave.setEnabled(false);




        inr_death_date = (LinearLayout) findViewById(R.id.inr_death_date);
        on_farm = (RadioButton) findViewById(R.id.on_farm);
        private_school = (RadioButton) findViewById(R.id.private_school);
        rbPresent = (RadioButton) findViewById(R.id.rbPresent);
        rgPOET=findViewById(R.id.rgPOET);
        rbPitting=findViewById(R.id.rbPitting);
        rbBilateral=findViewById(R.id.rbBilateral);
        rgMedicalFacility=findViewById(R.id.rgMedicalFacility);
        rgAgeVaccination=findViewById(R.id.rgAgeVaccination);
        rgchildPremature=findViewById(R.id.rgchildPremature);
        rgchildbreastfeeding=findViewById(R.id.rgchildbreastfeeding);
        rgmotherbreastmilk=findViewById(R.id.rgmotherbreastmilk);
        rgchildVitA=findViewById(R.id.rgchildVitA);
        rgchilddeworming_6=findViewById(R.id.rgchilddeworming_6);
        rgchildIFA=findViewById(R.id.rgchildIFA);
        rgchildVaccinationStatus=findViewById(R.id.rgchildVaccinationStatus);
        rbMedicalFacilityYes=findViewById(R.id.rbMedicalFacilityYes);
        rbMedicalFacilityNo=findViewById(R.id.rbMedicalFacilityNo);
        llTypeOfEdema=findViewById(R.id.llTypeOfEdema);
        llLinkedWithMedical=findViewById(R.id.llLinkedWithMedical);

        cbGrain = (CheckBox) findViewById(R.id.cbGrain);
        cbDal = (CheckBox) findViewById(R.id.cbDal);
        cbMilkrelated = (CheckBox) findViewById(R.id.cbMilkrelated);
        cbEggs = (CheckBox) findViewById(R.id.cbEggs);
        cbFleshfood = (CheckBox) findViewById(R.id.cbFleshfood);
        cbVitA = (CheckBox) findViewById(R.id.cbVitA);
        cbFruits = (CheckBox) findViewById(R.id.cbFruits);

        cbDiarrhoea = (CheckBox) findViewById(R.id.cbDiarrhoea);
        cbCough = (CheckBox) findViewById(R.id.cbCough);
        cbFever = (CheckBox) findViewById(R.id.cbFever);
        cbDifficultybreathing = (CheckBox) findViewById(R.id.cbDifficultybreathing);
        cbOther = (CheckBox) findViewById(R.id.cbOther);

        cbDiarrhoea_no15 = (CheckBox) findViewById(R.id.cbDiarrhoea_no15);
        cbCough_no15 = (CheckBox) findViewById(R.id.cbCough_no15);
        cbFever_no15 = (CheckBox) findViewById(R.id.cbFever_no15);
        cbDifficultybreathing_no15 = (CheckBox) findViewById(R.id.cbDifficultybreathing_no15);
        cbOther_no15 = (CheckBox) findViewById(R.id.cbOther_no15);

        rg_medicaly_critical=findViewById(R.id.rg_medicaly_critical);
        rb_medicaly_critical_yes=findViewById(R.id.rb_medicaly_critical_yes);
        rb_medicaly_critical_no=findViewById(R.id.rb_medicaly_critical_no);
        et_medicaly_critical=findViewById(R.id.et_medicaly_critical);

        rg_sent_to_nrc=findViewById(R.id.rg_sent_to_nrc);
        rb_sent_to_nrc_yes=findViewById(R.id.rb_sent_to_nrc_yes);
        rb_sent_to_nrc_no=findViewById(R.id.rb_sent_to_nrc_no);
        et_sent_to_nrc=findViewById(R.id.et_sent_to_nrc);
        Iv_image=findViewById(R.id.Iv_image);

        rg_nutrition_by_bnf=findViewById(R.id.rg_nutrition_by_bnf);
        rb_nutrition_by_bnf_yes=findViewById(R.id.rb_nutrition_by_bnf_yes);
        rb_nutrition_by_bnf_no=findViewById(R.id.rb_nutrition_by_bnf_no);

        rg_garden_setup=findViewById(R.id.rg_garden_setup);
        rb_garden_setup_yes=findViewById(R.id.rb_garden_setup_yes);
        rb_garden_setup_no=findViewById(R.id.rb_garden_setup_no);

        rg_nutrition_garden=findViewById(R.id.rg_nutrition_garden);
        rb_nutrition_garden_yes=findViewById(R.id.rb_nutrition_garden_yes);
        rb_nutrition_garden_no=findViewById(R.id.rb_nutrition_garden_no);

        childnutrition.setSickYesNo("");
        childnutrition.setSickName("");
        childnutrition.setSickYesNo15("");
        childnutrition.setSickName15("");
        inr_death_date.setVisibility(View.GONE);


        //arun
        absent_child = new ArrayList<Integer>();
        monitor_child = new ArrayList<>();
        migrate_child = new ArrayList<>();
        date_child = new ArrayList<>();
    }

    public void setCake(int cid, String age_in_months) {
        if(!age_in_months.equals("")) {
            child_gender = sqliteHelper.GetOneData("gender", "child", "child_id = " + cid);
            String cBirth = sqliteHelper.getDOB(cid);

            childAgeInMonth = Integer.parseInt(age_in_months);


            try {
                if (child_gender.equals("1") || child_gender.equals("3")) {
                    age_height = sqliteHelper.zScoreValue("IDEAL_HEIGHT_BOY", childAgeInMonth);
                    age_weight = sqliteHelper.zScoreValue("ideal_weight_boy", childAgeInMonth);
                } else {
                    age_height = sqliteHelper.zScoreValue("IDEAL_HEIGHT_GIRL", childAgeInMonth);
                    age_weight = sqliteHelper.zScoreValue("ideal_weight_girl", childAgeInMonth);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }


            Log.e("..........", "->" + childAgeInMonth);
            if (Integer.parseInt(age_in_months) == 6) {
                Display display = getWindowManager().getDefaultDisplay();
                imgCake.getLayoutParams().height = display.getWidth() - 10;
                imgCake.getLayoutParams().width = display.getWidth() - 10;
                imgCake.setVisibility(View.VISIBLE);


            }
        }
        else{

        }
    }

    @SuppressLint("ShowToast")
    public void clickGo(View v) {
        strId = etxtSearchByHouseHoldId.getText().toString().trim();

        if (strId.equals("")) {
            //populateList(spnChildName, "child_registration", "child_id",
            //      "name_of_child", "Select Child", "where anganwadi_center_id=" + user_id);
        } else {
            if(spnChildName.getSelectedItemPosition() < 0){

            }else {
                searchData="";
                et_searchchildname.setText("");
                strId = etxtSearchByHouseHoldId.getText().toString().trim();
                populateList(spnChildName, "child", "child_id",
                        "name_of_" +
                                "child", getString(R.string.select_name), "where house_number='"
                                + strId.toLowerCase() + "' and  anganwadi_center_id=" + user_id);
            }
        }
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = null;
        try {
            data = (SpinnerHelper) spn.getItemAtPosition((int) spn
                    .getSelectedItemId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getValue();
    }

    @SuppressLint("NewApi")
    public void btnSave_Click(View vw) {
        if(checkValidation()) {
            try {
                childnutrition.setDeathDate(death_date);
                childnutrition.setMobile_unique_id(CommonMethods.getUUID());
                childnutrition.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                childnutrition.setHave_adeama(strHaveAdeama);
                childnutrition.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
                childnutrition.setProvision_of_edima(strPOET);
                childnutrition.setLink_medical_facility(strMedicalFacility);

//                childnutrition.setMedical_critical(strMedicaly);
//                childnutrition.setMedical_critical_reason(et_medicaly_critical.getText().toString().trim());
//                childnutrition.setNrc_last_month(strSentToNrc);
//                childnutrition.setNrc_last_month_date(et_sent_to_nrc.getText().toString().trim());
//                childnutrition.setNutrition_bnf(strNutritionBnf);
//                childnutrition.setNutrition_garden(strGardenSetup);
//                childnutrition.setNutrition_garden_kit(strNutritionGarden);


                String spinner_value = String.valueOf(child_id);
                String cBirth = sqliteHelper.getDOB(child_id);

                if (spnChildName.getSelectedItemPosition() == -1) {
                    Toast.makeText(getApplicationContext(), R.string.select_child_name, Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    if (compareTwoDates(etxtDateOfScreening.getText().toString().trim(), cBirth).equals("Today Date Lesser")) {
                        Toast.makeText(getApplicationContext(), R.string.dob_greater_than_child, Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        if (!spinner_value.equalsIgnoreCase("")) {
                            String dob1 = sqliteHelper.getDOB(Integer.parseInt(spinner_value));
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dob1.substring(8, 10)));
                            cal.set(Calendar.MONTH, Integer.parseInt(dob1.substring(5, 7)) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(dob1.substring(0, 4)));
                            Date firstDate = cal.getTime();

                            Calendar cal2 = Calendar.getInstance();

                            cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fdate.substring(8, 10)));
                            cal2.set(Calendar.MONTH, Integer.parseInt(fdate.substring(5, 7)) - 1);
                            cal2.set(Calendar.YEAR, Integer.parseInt(fdate.substring(0, 4)));
                            Date secondDate = cal2.getTime();
                            long diff = (secondDate.getTime() - firstDate.getTime());
                            int diffday = (int) (diff / (1000 * 60 * 60 * 24));
                            if (diffday > 2190) {
                                AlertDialog alertDialog = new AlertDialog.Builder(ActivityChildNutritionMonitor.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage(strFiveYearMon);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ActivityChildNutritionMonitor.this, ActivityChildNutritionMonitor.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                                alertDialog.show();

                            } else {
                                try {

                                    String arrChildDOB;
                                    ArrayList<Views> arr = new ArrayList<Views>();
                                    String condition = "";
                                    if (cbGrain.isChecked()) {
                                        condition = "1";
                                    }
                                    if (cbDal.isChecked()) {
                                        condition = condition + "," + "2";
                                    }
                                    if (cbMilkrelated.isChecked()) {
                                        condition = condition + "," + "3";
                                    }
                                    if (cbEggs.isChecked()) {
                                        condition = condition + "," + "4";
                                    }
                                    if (cbFleshfood.isChecked()) {
                                        condition = condition + "," + "5";
                                    }
                                    if (cbVitA.isChecked()) {
                                        condition = condition + "," + "6";
                                    }
                                    if (cbFruits.isChecked()) {
                                        condition = condition + "," + "7";
                                    }


                                    String sickCondition = "";
                                    if (cbDiarrhoea.isChecked()) {
                                        sickCondition = cbDiarrhoea.getText().toString();
                                    }
                                    if (cbCough.isChecked()) {
                                        sickCondition = sickCondition + "," + cbCough.getText().toString();
                                    }
                                    if (cbFever.isChecked()) {
                                        sickCondition = sickCondition + "," + cbFever.getText().toString();
                                    }
                                    if (cbDifficultybreathing.isChecked()) {
                                        sickCondition = sickCondition + "," + cbDifficultybreathing.getText().toString();
                                    }
                                    if (cbOther.isChecked()) {
                                        sickCondition = sickCondition + "," + cbOther.getText().toString();
                                    }


                                    String sickCondition_015 = "";
                                    if (cbDiarrhoea_no15.isChecked()) {
                                        //condition = condition + "," + cbThyroid.getText();
                                        sickCondition_015 = cbDiarrhoea_no15.getText().toString();
                                    }
                                    if (cbCough_no15.isChecked()) {
                                        sickCondition_015 = sickCondition_015 + "," + cbCough_no15.getText().toString();
                                    }
                                    if (cbFever_no15.isChecked()) {
                                        sickCondition_015 = sickCondition_015 + "," + cbFever_no15.getText().toString();
                                    }
                                    if (cbDifficultybreathing_no15.isChecked()) {
                                        sickCondition_015 = sickCondition_015 + "," + cbDifficultybreathing_no15.getText().toString();
                                    }
                                    if (cbOther_no15.isChecked()) {
                                        sickCondition_015 = sickCondition_015 + "," + cbOther_no15.getText().toString();
                                    }


                                    childnutrition.setChild_id(child_id);
                                    latestData = sqliteHelper.getLatestHeight(child_id);

                                    if (latestData[1].equals("")) {
                                        latestHeight = 0;
                                    } else {
                                        latestHeight = Float.parseFloat(latestData[0]);
                                    }

                                    if (latestData[1].equals("")) {
                                        latestWeight = 0;
                                    } else {
                                        latestWeight = Float.parseFloat(latestData[1]);
                                    }

                                    childnutrition.setChild_name(child_name);
                                    childnutrition.setWeight(etxtWeight.getText().toString().trim());
                                    childnutrition.setHeight(etxtHeight.getText().toString().trim());
                                    childnutrition.setMuac(etxtMuac.getText().toString().trim());
                                    childnutrition.setHB(etxtAdolescentHb.getText().toString().trim());
                                    childnutrition.setMultimedia(image64);
                                    childnutrition.setIs_age_vaccination(strAgeVaccination);
                                    childnutrition.setIs_child_Premature(strchildPremature);
                                    childnutrition.setIs_child_breastfeeding(strchildbreastfeeding);
                                    childnutrition.setIs_mother_breastmilk(strmotherbreastmilk);
                                    childnutrition.setIs_child_VitA6_months(strchildVitA);
                                    childnutrition.setIs_child_deworming_6months(strchilddeworming_6);
                                    childnutrition.setIs_child_ifa(strchildIFA);
                                    childnutrition.setChild_Vaccination_Status(strchildVaccinationStatus);
                                    childnutrition.setChild_meal_number(etxtchildmealnumber.getText().toString().trim());
                                    childnutrition.setChild_meal(condition);
                                    childnutrition.setSick_reason(sickCondition);
                                    childnutrition.setSick_15_reason(sickCondition_015);
                                    childnutrition.setDreason("");

                                    childnutrition.setMedical_critical(strMedicaly);
                                    childnutrition.setMedical_critical_reason(et_medicaly_critical.getText().toString().trim());
                                    childnutrition.setNrc_last_month(strSentToNrc);
                                    childnutrition.setNrc_last_month_date(et_sent_to_nrc.getText().toString().trim());
                                    childnutrition.setNutrition_bnf(strNutritionBnf);
                                    childnutrition.setNutrition_garden(strGardenSetup);
                                    childnutrition.setNutrition_garden_kit(strNutritionGarden);


                                    Time time = new Time();
                                    time.setToNow();
                                    childnutrition.setDate_of_monitoring(fdate);


                                    arr = sqliteHelper.monitoringDateSearch("child_nutrition_monitoring", "child_id", String.valueOf(child_id));
                                    arrChildDOB = sqliteHelper.getBirthDate(String.valueOf(child_id));
                                    String dateOfRecording = etxtDateOfScreening.getText().toString().trim();
                                    SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar c = Calendar.getInstance();
                                    Date dateDb = null;
                                    Date dateInsert = null;
                                    Date dateDOB = null;

                                    try {
                                        dateDOB = form.parse(arrChildDOB);
                                        dateInsert = form.parse(dateOfRecording);
                                    } catch (java.text.ParseException e) {
                                        e.printStackTrace();
                                    }
                                    c.setTime(dateDOB);
                                    int monthDOB = c.get(Calendar.MONTH) + 1;
                                    int yearDOB = c.get(Calendar.YEAR);
                                    c.setTime(dateInsert);
                                    int monthInsert = c.get(Calendar.MONTH) + 1;
                                    int yearInsert = c.get(Calendar.YEAR);

                                    if (dateDOB.after(dateInsert)) {

                                        Toast.makeText(getApplicationContext(),
                                                "Invalid date", Toast.LENGTH_LONG)
                                                .show();
                                        etxtWeight.setText("");
                                        etxtHeight.setText("");
                                        etxtMuac.setText("");
                                        etxtAdolescentHb.setText("");
                                        etxtSearchByHouseHoldId.setText("");
                                        etxtDateOfScreening.setText("");
                                        flag = 1;
                                    }

                                    for (int i = 0; i < arr.size(); i++) {

                                        final Views viewObj = arr.get(i);
                                        String s = viewObj.getDate();

                                        try {
                                            dateDb = form.parse(s);

                                        } catch (java.text.ParseException e) {
                                            e.printStackTrace();
                                        }
                                        c.setTime(dateDb);
                                        int monthDb = c.get(Calendar.MONTH) + 1;
                                        int yearDb = c.get(Calendar.YEAR);

                                        if (monthDb == monthInsert && yearDb == yearInsert) {

                                            Toast.makeText(getApplicationContext(), R.string.month_data_already_monitoried, Toast.LENGTH_LONG).show();

                                            etxtWeight.setText("");
                                            etxtHeight.setText("");
                                            etxtMuac.setText("");
                                            etxtSearchByHouseHoldId.setText("");

                                            flag = 1;
                                            flag2 = 1;

//                                flag = 0;

                                        }

                                    }


                                    validation();


//                            if (migrationflag == 0 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 0 && deathFlag == 0 && permanentMigrateFlag == 0) {
//
//
//                            }
//
//                            } else if (migrationflag == 1 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 1 &&
//                                    absentFlag == 0 && deathFlag == 0 && permanentMigrateFlag == 0) {
//
//                                validationformigration();
//
//                            } else if (migrationflag == 1 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 0 && deathFlag == 0 && permanentMigrateFlag == 1) {
//
//                                validationformigration();
//
//                            } else if (migrationflag == 1 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 0 && deathFlag == 1 && permanentMigrateFlag == 0) {
//
//                                validationformigrationDeath();
//
//                            } else if (migrationflag == 1 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 1 && deathFlag == 0 && permanentMigrateFlag == 0) {
//
//                                validationformigration();
//
//                            } else if (migrationflag == 1 && onFarmFlag == 0 &&
//                                    privateSchoolFlag == 1 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 0 && deathFlag == 0 && permanentMigrateFlag == 0) {
//
//                                validationformigration();
//
//                            } else if (migrationflag == 1 && onFarmFlag == 1 &&
//                                    privateSchoolFlag == 0 && temporaryMigrateFlag == 0 &&
//                                    absentFlag == 0 && deathFlag == 0 && permanentMigrateFlag == 0) {
//
//                                validationformigration();
//
//                            }


                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.download_first, Toast.LENGTH_LONG).show();
                        }
                    }
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkValidation() {
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
            return true;
    }

    private void validationformigrationDeath() {


        childnutrition.setMultimedia("");
        childnutrition.setHB("0");
        childnutrition.setSickName("");
        childnutrition.setSickName15("");
        childnutrition.setSickYesNo("");
        childnutrition.setSickYesNo15("");
//if( !etxtDeathDate.getText().toString().trim().equals(""))
//{

        if (flag == 0) {
            migrationflag = 0;
//        if (death.isChecked() && spnDeathReason.getSelectedItem().toString().equalsIgnoreCase("Select Death Reason")) {
//            txtDeathReason.setError("Please select death reason");
//            txtDeathReason.requestFocus();
//            txtDeathReason.setFocusable(true);
//
//        } else {
            id = sqliteHelper.childNutitionMonitor(childnutrition);
            if (id > 0) {
                etxtWeight.setText("");
                etxtHeight.setText("");
                etxtMuac.setText("");
                etChildsick.setText("");
                etxtSearchByHouseHoldId.setText("");
//                Toast.makeText(getApplicationContext(),
//                        strChildNutrition + " " + strDone + "!", 200)
//                        .show();

//                setSubmitPopUp();
                showSubmitDialog(context, getString(R.string.child_nutrition_monitoring), getString(R.string.child_monitoring_success_message));


                finish();
            } else {
                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
            }
            //   }
        }
//}
//else {
//    etxtDeathDate.setError("Please select death date");
//    etxtDeathDate.setFocusable(true);
//    Toast.makeText(this, "Please select death date", Toast.LENGTH_SHORT).show();
//}

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

    private void validationformigration() {

//        childnutrition.setMultimedia("");
//        childnutrition.setHB("0");
//        childnutrition.setSickName("");
//        childnutrition.setSickName15("");
//        childnutrition.setSickYesNo("");
//        childnutrition.setSickYesNo15("");

//        if (flag == 0) {
//            migrationflag = 0;
//            if (death.isChecked() && spnDeathReason.getSelectedItem().toString().equalsIgnoreCase("Select Death Reason")) {
//                txtDeathReason.setError("Please select death reason");
//                txtDeathReason.requestFocus();
//                txtDeathReason.setFocusable(true);
//
//            } else {
            id = sqliteHelper.childNutitionMonitor(childnutrition);
            if (id > 0) {
//                etxtWeight.setText("");
//                etxtHeight.setText("");
//                etxtMuac.setText("");
//                etChildsick.setText("");
//                etxtSearchByHouseHoldId.setText("");
//                Toast.makeText(getApplicationContext(),
//                        strChildNutrition + " " + strDone + "!", 200)
//                        .show();
//                finish();

                showSubmitDialog(context, getString(R.string.child_nutrition_monitoring), getString(R.string.child_monitoring_success_message));

            } else {
                Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
            }
            //      }
//        }

    }

    private void validation() {
        if (childnutrition.getChild_name().equals(""))
//                || etxtWeight.getText().toString().equals("")
//                || Float.parseFloat(etxtWeight.getText().toString()) < 0.800
//                || Float.parseFloat(etxtWeight.getText().toString()) > 25.000
//                || etxtHeight.getText().toString().equals("")
//                || Float.parseFloat(etxtHeight.getText().toString()) < 40.00
//                || Float.parseFloat(etxtHeight.getText().toString()) > 130.00
//                || etxtMuac.getText().toString().equals(""))
        //  || spnHB.getSelectedItem().toString().equals("Select HB")
//                || Float.parseFloat(etxtMuac.getText().toString()) < 9.00
//                || Float.parseFloat(etxtMuac.getText().toString()) > 22.00)
        //   || image64.equals("")
        {

            if (childnutrition.getChild_name().equals("")) {
                Toast.makeText(getApplicationContext(),
                        strChildFullName + " " + strMandatory, Toast.LENGTH_LONG).show();
            }

//            if (etxtWeight.getText().toString().equals("")) {
//                String[] util = Utility.split(strWeight);
//                String weight = util[0];
//                etxtWeight.setError(weight + " " + strMandatory);
//
//            }
//
//
//            if (Float.parseFloat(etxtWeight.getText().toString()) < 0.800) {
//                String[] util = Utility.split(strWeight);
//                String weight = util[0];
//                etxtWeight.setError(weight + " should be greater than 800 gram.");
//                etxtWeight.setFocusable(true);
//            }
//
//            if (Float.parseFloat(etxtWeight.getText().toString()) > 25.000) {
//                String[] util = Utility.split(strWeight);
//                String weight = util[0];
//                etxtWeight.setError(weight + " should be less than 25 kg.");
//                etxtWeight.setFocusable(true);
//            }
//
//            if (etxtHeight.getText().toString().equals("")) {
//                String[] util = Utility.split(strHeight);
//                String height = util[0];
//                etxtHeight.setError(height + " " + strMandatory);
//
//            }
//
//            if (Float.parseFloat(etxtHeight.getText().toString()) < 40.00) {
//                String[] util = Utility.split(strHeight);
//                String height = util[0];
//                etxtHeight.setError(height + "should be greater than 40 cm.");
//                etxtHeight.setFocusable(true);
//            }
//
//            if (Float.parseFloat(etxtHeight.getText().toString()) > 130.00) {
//                String[] util = Utility.split(strHeight);
//                String height = util[0];
//                etxtHeight.setError(height + "should be less than 130 cm.");
//                etxtHeight.setFocusable(true);
//            }

        /*    if (spnHB.getSelectedItem().toString().equals("Select HB")) {
                Toast.makeText(getApplicationContext(), "Please select HB.", 200).show();
            }
*/
//            if (etxtMuac.getText().toString().equals("")) {
//                String[] util = Utility.split(strMuac);
//                String muac = util[0];
//                etxtMuac.setError(muac + " " + strMandatory);
//                etxtMuac.setFocusable(true);
//            }
//
//            if (Float.parseFloat(etxtMuac.getText().toString()) < 9.00) {
//                String[] util = Utility.split(strMuac);
//                String muac = util[0];
//                etxtMuac.setError(muac + " should be greater than 9 cm ");
//            }
//
//            if (Float.parseFloat(etxtMuac.getText().toString()) > 22.00) {
//                String[] util = Utility.split(strMuac);
//                String muac = util[0];
//                etxtMuac.setError(muac + " should be less than 22 cm ");
//            }
         /*   if (image64.equals("")) {

                Toast.makeText(getApplicationContext(), "Photo is mandatory.", 200).show();
            }
*/

        }
        else {

//            etxtWeight.setError(null);
//            etxtHeight.setError(null);
//            etxtMuac.setError(null);
//            etxtSearchByHouseHoldId.setError(null);
//            if (sick_id == null) {
//                childnutrition.setSickName("" + sick_id);
//            }
//            if (sick_id15 != null) {
//                childnutrition.setSickName15("" + sick_id15);
//            }
//
//            if (Float.parseFloat(etxtWeight.getText().toString()) < latestWeight) {
//
//                ArrayList<String> reas = new ArrayList<String>();
//                reas = sqliteHelper.getDreason();
//                String[] re = reas.toArray(new String[reas.size()]);
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//                builder1.setTitle("Select reason for weight loss.");
//                builder1.setSingleChoiceItems(re, -1, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        don = which + 1;
//                    }
//                });
//                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (flag == 0) {
//                            childnutrition.setDreason(Integer.toString(don));
//                            id = sqliteHelper.childNutitionMonitor(childnutrition);
//                            if (id > 0) {
////                                etxtWeight.setText("");
////                                etxtHeight.setText("");
////                                etxtMuac.setText("");
////                                etxtSearchByHouseHoldId.setText("");
////                                Toast.makeText(getApplicationContext(),
////                                        strChildNutrition + " " + strDone + "!", 200)
////                                        .show();
////                                finish();
//
//                                showSubmitDialog(context, "Child Nutrition Monitoring", "You have successfully completed Child Nutrition Monitoring monitoring.");
//
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), strTryAgain, 200);
//                            }
//                        }
//                        dialog.dismiss();
//                    }
//                });
//
//                builder1.setNegativeButton("Back",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                AlertDialog alert = builder1.create();
//                alert.show();
//            } else {
            if (flag == 0) {

                id = sqliteHelper.childNutitionMonitor(childnutrition);
                if (id > 0) {
//                        etxtWeight.setText("");
//                        etxtHeight.setText("");
//                        etxtMuac.setText("");
//                        etxtSearchByHouseHoldId.setText("");
//                        Toast.makeText(getApplicationContext(),
//                                strChildNutrition + " " + strDone + "!", 200)
//                                .show();
//                        finish();

                    showSubmitDialog(context, getString(R.string.child_nutrition_monitoring), getString(R.string.child_monitoring_success_message));

                } else {
                    Toast.makeText(getApplicationContext(), strTryAgain, Toast.LENGTH_LONG).show();
//                    }
//                }
            }
//                }
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
                        bitmap = imageLatLogHeper.compressImage(file + "", child_name);

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
                            try {


                                ImageLatLogHeper imageLatLogHeper = new ImageLatLogHeper(getApplicationContext());
                                bitmap = imageLatLogHeper.compressImage(f.getAbsolutePath() + "", child_name);

                                img.setImageBitmap(bitmap);

                                OutputStream fOut = null;

                                try {
                                    fOut = new FileOutputStream(f);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
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

                        Intent intent = new Intent(
                                ActivityChildNutritionMonitor.this,
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
        flag = 0;

    }

    @SuppressLint("NewApi")
    public void show_callender1(View vw) {
        DialogFragment newFragment = new DatePickerFragment1();
        newFragment.show(getFragmentManager(), "datePicker");
//        flag = 0;

    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
            new sync_sevika_helper(this).execute("");
        }
    }

    public void populateHBspn() {
        String hbs[] = {"Select HB", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12", "12.5", "13", "13.5", "14", "14.5", "15", "NA - Other issue", "NA-Child not Co-operating", "Machine not available"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hbs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnHB.setAdapter(spinnerArrayAdapter);
    }


    /**
     * search child by name
     **/
    public void  populatechildNam( ) {
        if (!searchData.equalsIgnoreCase("")) {
            spinnerchildData = sqliteHelper.getChildSpnnerforsearchdata(searchData, getString(R.string.select_name));
            ArrayAdapter<SpinnerHelper> adapter = null;

            adapter = new ArrayAdapter<SpinnerHelper>(
                    ActivityChildNutritionMonitor.this,
                    android.R.layout.simple_spinner_item, spinnerchildData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setPrompt(getString(R.string.select_name));
            spnChildName.setAdapter(adapter);
            /*ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerchildData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setAdapter(aa);*/
        }else {
            spinnerchildData = sqliteHelper.getChildSpnnerforsearchdata1(searchData, getString(R.string.select_name));
            ArrayAdapter<SpinnerHelper> adapter = null;

            adapter = new ArrayAdapter<SpinnerHelper>(
                    ActivityChildNutritionMonitor.this,
                    android.R.layout.simple_spinner_item, spinnerchildData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setPrompt(getString(R.string.select_name));
            spnChildName.setAdapter(adapter);
            /*ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerchildData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setAdapter(aa);*/
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

    public void deathDate(View view) {
        inr_death_date.setVisibility(View.VISIBLE);
    }

    public void show_callender_death(View v) {
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
            etxtDeathDate.setText(formattedDate);
            death_date = formattedDate;
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

    public String compareTwoDates(String dateToday, String dateDob){
        String compareDate = null;
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdformat.parse(dateToday);
            d2 = sdformat.parse(dateDob);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(d1.compareTo(d2) > 0) {
            compareDate="Today Date Greater";
            System.out.println("Date 1 occurs after Date 2");
        } else if(d1.compareTo(d2) < 0) {
            compareDate="Today Date Lesser";
            System.out.println("Date 1 occurs before Date 2");
        } else if(d1.compareTo(d2) == 0) {
            compareDate="Today Date Equal";
            System.out.println("Both dates are equal");
        }
        return compareDate;
    }
}