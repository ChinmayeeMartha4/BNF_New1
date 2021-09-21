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
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.example.mhealth.ActivityLogin.MyLocationListener;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.Mother;
import com.example.mhealth.helper.Parent;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.utils.AlertDialogClass;
import com.example.mhealth.utils.CommonMethods;
import com.example.mhealth.utils.MyOperationLayoutAddMother;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ActivityEligibleFamilyRegistration extends Activity implements AdapterView.OnItemSelectedListener {
    public static final String Savingonserver = "savingonserver";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ImageView img, ivEdit;
    String image64 = "";
    String mCurrentPhotoPath;
    String lang = "", dateTime = "";
    private String houseNo = "", familyid = "";
    Spinner spnReligion, spnToilet, spnDrinkingWater, spnCast, spnCastecat, spnLanload1, spnWaterQuality, spnHhId;
    Button btnSave, btnAddMother;
    Button btnScan;
    TextView txtReligion, txtCastcat, txtCast, txtToilet, txtDrinkingWater, txtFooter,
            txtsesMigration, txtLanlod, tvImg, txtbplCard, txtHHID;
    int intGender, intReligion, intCast, intCastecat, intToilet, intDrinkingWater, intLandHold;
    String strwaterQuality;
    RadioGroup grpGroup;
    RadioButton rdbuttonyes, rdbuttonNo;
    // remove
    String savingonserver, strTryagain, strNo, strYes, strCancel, strsesMig = "No", strMig,
            strRegDone,strFooter, strEligFamily, strValidAadhar, strOnlyAlpha, strLanload,
            strBPL = "";
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    LocationManager locationManager;
    ServerHelper serverhelper;
    Parent parent;
    long local_parent_id = 0;
    ProgressDialog progressDialog;
    private Bitmap bitmap;
    private Spinner spnGender;
    private TextView txtEligibleFamilyReg;
    private TextView txtHouseNo;
    private TextView txtHeadOfHouse;
    private TextView txtHHAadhar;
    private TextView txtHHMobile;
    private TextView txtGender;
    private EditText etxtHousenumber;
    private EditText etxtHeadOfHH;
    private EditText etxtAadharCardHH;
    private EditText etxtMobileHH;
    private String strHouseNo;
    private String strHeadOfHH;
    private String strAadharHH;
    private String strMobileHH;
    private String strGender;
    private String strReligion;
    private String strCastcat;
    private String strCast;
    private String strToilet;
    private String strDrinkingWater;
    private String strBtnSave;
    private String strHouseNMandatory;
    private String strHHHMandatory;
    private String strAadhaarMandatory;
    private ImageLoadingUtils utils;
    private String lat;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context = this;
    LinearLayout llSeasonalMigrationReason, llHouseHoldDropDown, llMainLayout;
    TextView tvMigrationReason;
    EditText etMigrationReason, etxtNoOfFM;
    RadioGroup radiogroupbplCard;
    RadioButton radiobplCardYes, radiobplCardNo;
    public static EditText etxtDateOfScreening;
    Spinner spnMaritalStatus, spnEducation;
    EditText etxtMother, etxtAge;
    String[] maritalStatusAL = {"Please Select", "Single", "Married", "Widow"};
    String[] maritalStatusHindiAL = {"कृपया चयन कीजिए", "एकल", "विवाहित", "विधवा"};
    String[] educationAL = {"Please Select", "Never gone to school", "Primary 1 to 5", "Upper Primary 6 to 8",
            "Secondary 9 to 10", "Higher Secondary 11 to 12", "Graduate", "Post Graduate and Above"};
    String[] educationHindiAL = {"कृपया चयन कीजिए", "कभी स्कूल नहीं गया", "प्राथमिक 1 से 5", "उच्च प्राथमिक 6 से 8",
            "माध्यमिक 9 से 10", "हायर सेकेंडरी 11 से 12", "स्नातक", "स्नातकोत्तर और उससे ऊपर"};
    HashMap<String, ArrayList<String>> motherHM;
    private long local_m_id;
    Mother mother;
    public static android.app.Dialog submit_alert;
    ArrayList<Parent> parentList;
    int genderId = 0, religionId = 0, castCategoriesId = 0, occupationId = 0, toiletFacilityId = 0,
            sourceOfDrinkingWaterId = 0;
    String genderName = "", religionName = "", occupationName = "", casteCategoriesName = "",
            toiletFacilityName = "",
            sourceOfDrinkingWaterName = "", mobileUniqueId = "", motherMobileUniqueId = "";
    ArrayList<Mother> motherArrayList = new ArrayList<>();
    boolean isEditable = false;

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
        setContentView(R.layout.activity_activity_parent_registration);
        initialize();
        uploadAttendance();
        setMaritalStatusSpinner();
        setEducationSpinner();

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }

        String languageId = sph.getString("Language", "");// getting languageId

        strHouseNo = sqliteHelper.LanguageChanges(ConstantValue.LANHouseNo, languageId);
        strHeadOfHH = sqliteHelper.LanguageChanges(ConstantValue.LANHHH, languageId);
        strAadharHH = sqliteHelper.LanguageChanges(ConstantValue.LANAadhaarHH, languageId);
        strMobileHH = sqliteHelper.LanguageChanges(ConstantValue.LANMobileHH, languageId);
        strGender = sqliteHelper.LanguageChanges(ConstantValue.LANGender, languageId);
        strReligion = sqliteHelper.LanguageChanges(ConstantValue.LANReligion, languageId);
        strCastcat = sqliteHelper.LanguageChanges(ConstantValue.LANCastcat, languageId);
        strCast = sqliteHelper.LanguageChanges(ConstantValue.LANCast, languageId);//requirchange 14-6-18
        strToilet = sqliteHelper.LanguageChanges(ConstantValue.LANToilet, languageId);
        strDrinkingWater = sqliteHelper.LanguageChanges(ConstantValue.LANDrinkingWater, languageId);
        strBtnSave = sqliteHelper.LanguageChanges(ConstantValue.LANSave, languageId);
        strHouseNMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANHNMandatory, languageId);
        strHHHMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANHHHMandatory, languageId);
        strAadhaarMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANAadhaarMandatory, languageId);
        savingonserver = (String) sqliteHelper.LanguageChanges(Savingonserver, languageId);
        strRegDone = sqliteHelper.LanguageChanges(ConstantValue.LANEFRegDone, languageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strEligFamily = sqliteHelper.LanguageChanges(ConstantValue.LANElFamily, languageId);
        strValidAadhar = sqliteHelper.LanguageChanges(ConstantValue.LANValidAadhar, languageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, languageId);
        strMig = sqliteHelper.LanguageChanges(ConstantValue.LANSesmig, languageId);
        strLanload = sqliteHelper.LanguageChanges(ConstantValue.LANload, languageId);
        /* new code */
        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        txtEligibleFamilyReg.setText(strEligFamily);
        txtsesMigration.setText(strMig);
        txtHouseNo.setText(R.string.select_household_number);
        txtHeadOfHouse.setText(strHeadOfHH);
        txtHHAadhar.setText(strAadharHH);
        txtHHMobile.setText(strMobileHH);
        txtGender.setText(strGender);
        txtReligion.setText(strReligion);
        txtCastcat.setText(strCastcat);
        txtCast.setText(strCast);//input language
        txtToilet.setText(strToilet);
        txtDrinkingWater.setText(strDrinkingWater);
        txtLanlod.setText(strLanload);
        //txtFooter.setText(strFooter);
        btnSave.setText(strBtnSave);
        tvImg.setText(sqliteHelper.LanguageChanges(ConstantValue.LANPhoto, languageId));
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        tvMigrationReason.setText("Migration Reason");
        tvTitleText.setText(R.string.house_hold);
        ivTitleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(getString(R.string.house_hold_exit_message) + "?");
                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivityEligibleFamilyRegistration.this,
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
        //add BPL info click
        txtbplCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass.showDialog(context, getString(R.string.bpl_information), getString(R.string.bpl_content));
            }
        });
        //BPL card holder
        radiogroupbplCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radiobplCardYes:
                        strBPL = "Yes";
                        break;
                    case R.id.radiobplCardNo:
                        strBPL = "No";
                        break;
                }
            }
        });

        //add mother click
        btnAddMother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyOperationLayoutAddMother.add(ActivityEligibleFamilyRegistration.this, btnAddMother);
            }
        });

        try {
            populateList(spnReligion, "religion", "caption_id", "value",
                    getString(R.string.select_religion), "where language_id=" + languageId, isEditable);

            populateList(spnWaterQuality, "water_quality", "caption_id", "water_quality_name",
                    getString(R.string.select_water_quality), "where language_id=" + languageId, isEditable);

            populateList(spnLanload1, "family_land_hold", "caption_id", "value",
                    getString(R.string.main_source_of_income), "where language_id=" + languageId, isEditable);

            populateList(spnCastecat, "caste_category", "caption_id", "category",
                    getString(R.string.select_social_categories), "where language_id=" + languageId, isEditable);

            populateList(spnGender, "gender", "caption_id", "gender_value",
                    getString(R.string.select_gender), "where language_id=" + languageId, isEditable);

            populateList(spnDrinkingWater, "drinking_water_source",
                    "caption_id", "drinking_water_source",
                    getString(R.string.primary_source_of_drinking), "where language_id=" + languageId, isEditable);

            populateList(spnToilet, "toilet_avaibality",
                    "caption_id", "toilet_avaibality_source",
                    getString(R.string.access_to_toilet_facility), "where language_id=" + languageId, isEditable);

            spnReligion.setOnItemSelectedListener(ActivityEligibleFamilyRegistration.this);
            spnWaterQuality.setOnItemSelectedListener(ActivityEligibleFamilyRegistration.this);
            spnCastecat.setOnItemSelectedListener(ActivityEligibleFamilyRegistration.this);
            spnCast.setOnItemSelectedListener(ActivityEligibleFamilyRegistration.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        grpGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rdbuttonyes:
                        strsesMig = "Yes";
                        //llSeasonalMigrationReason.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rdbuttonNo:
                        strsesMig = "No";
                        //llSeasonalMigrationReason.setVisibility(View.GONE);
                        break;
                }
            }
        });


        /*--------------make code for edit household--------------*/
        int user_id = sph.getInt("user_id", 0);
        ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*show household dropdown here*/
                txtHHID.setText(R.string.select_household_number);
                llHouseHoldDropDown.setVisibility(View.VISIBLE);
                populateHHList(spnHhId, "eligible_family", "familyid", "house_number",
                        getString(R.string.select_household_number), "where anganwadi_center_id=" + user_id);
            }
        });
        spnHhId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String strHouseHoldId = (spnHhId.getSelectedItem().toString());
                    String[] houseHoldId = strHouseHoldId.split("\\(");
                    houseNo = houseHoldId[0].trim();
                    familyid = getSelectedValue(spnHhId);
                    /*---get all data of selected household according to family id---*/
                    parentList = sqliteHelper.getAllDataOfHouseHold(familyid);
                    if (parentList.size() > 0) {
                        setAllDataOfHH(parentList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                Locale current = getBaseContext().getResources().getConfiguration().locale;
                String lang1 = current.getLanguage();
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Locale currentn = getBaseContext().getResources().getConfiguration().locale;
                String lang = currentn.getLanguage();
                Log.e("Lang>>>", "lang1>>>" + lang1 + ">>>lang>>>" + lang);
            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }

    private void setAllDataOfHH(ArrayList<Parent> parentList) {
        String languageId = sph.getString("Language", "");// getting languageId

        isEditable = true;
        llMainLayout.setVisibility(View.GONE);
        etxtHousenumber.setText(parentList.get(0).getHouse_number());
        etxtHousenumber.setEnabled(false);
        etxtHeadOfHH.setText(parentList.get(0).getHead_family());
        etxtAadharCardHH.setText(parentList.get(0).getAdhaar_card());
        etxtMobileHH.setText(parentList.get(0).getMobile_number());
        etxtNoOfFM.setText(parentList.get(0).getNo_of_member_in_family());
        mobileUniqueId = parentList.get(0).getMobile_unique_id();
        String gender=parentList.get(0).getGender();
        genderId = Integer.parseInt(gender);
        genderName = sqliteHelper.getNameById("gender", "gender_value", "id", genderId);
        populateList(spnGender, "gender", "caption_id", "gender_value",
                getString(R.string.select_gender), "where language_id=" + languageId, isEditable);
        if (genderId != 0) {
            spnGender.setSelection(genderId - 1);//spinner set selected with item position
        }

        religionId = Integer.parseInt(parentList.get(0).getReligion_id());
        religionName = sqliteHelper.getNameById("religion", "value", "religion_id", religionId);
        populateList(spnReligion, "religion", "caption_id", "value",
                getString(R.string.select_religion), "where language_id=" + languageId, isEditable);
        if (religionId != 0) {
            spnReligion.setSelection(religionId - 1);//spinner set selected with item position
        }

        castCategoriesId = Integer.parseInt(parentList.get(0).getCast_category());
        casteCategoriesName = sqliteHelper.getNameById("caste_category", "category", "id", castCategoriesId);
        populateList(spnCastecat, "caste_category", "caption_id", "category",
                getString(R.string.select_social_categories), "where language_id=" + languageId, isEditable);
        if (castCategoriesId != 0) {
            spnCastecat.setSelection(castCategoriesId - 1);//spinner set selected with item position
        }

        String seasonalMigrationRG = parentList.get(0).getSeasonal_migration();
        if (seasonalMigrationRG.equalsIgnoreCase("Yes")) {
            rdbuttonyes.setChecked(true);
        } else if (seasonalMigrationRG.equalsIgnoreCase("No")) {
            rdbuttonNo.setChecked(true);
        }

        occupationId = Integer.parseInt(parentList.get(0).getFamily_land_hold());
        occupationName = sqliteHelper.getNameById("family_land_hold", "value", "id", occupationId);
        populateList(spnLanload1, "family_land_hold", "caption_id", "value",
                getString(R.string.main_source_of_income), "where language_id=" + languageId, isEditable);
        if (occupationId != 0) {
            spnLanload1.setSelection(occupationId - 1);//spinner set selected with item position
        }

        toiletFacilityId = Integer.parseInt(parentList.get(0).getToilet_type());
        toiletFacilityName = sqliteHelper.getNameById("toilet_avaibality", "toilet_avaibality_source", "toilet_avaibality_id", toiletFacilityId);
        populateList(spnToilet, "toilet_avaibality",
                "caption_id", "toilet_avaibality_source",
                getString(R.string.access_to_toilet_facility), "where language_id=" + languageId, isEditable);
        if (toiletFacilityId != 0) {
            spnToilet.setSelection(toiletFacilityId - 1);//spinner set selected with item position
        }

        sourceOfDrinkingWaterId = Integer.parseInt(parentList.get(0).getWater_source());
        sourceOfDrinkingWaterName = sqliteHelper.getNameById("drinking_water_source", "drinking_water_source", "drinking_water_source_id", sourceOfDrinkingWaterId);
        populateList(spnDrinkingWater, "drinking_water_source",
                "caption_id", "drinking_water_source",
                getString(R.string.primary_source_of_drinking), "where language_id=" + languageId, isEditable);
        if (sourceOfDrinkingWaterId != 0) {
            spnDrinkingWater.setSelection(sourceOfDrinkingWaterId - 1);//spinner set selected with item position
        }

        String bplCardRG = parentList.get(0).getBpl_card();
        if (bplCardRG.equalsIgnoreCase("Yes")) {
            radiobplCardYes.setChecked(true);
        } else if (bplCardRG.equalsIgnoreCase("No")) {
            radiobplCardNo.setChecked(true);
        }

        if (parentList.get(0).getImage() != null && parentList.get(0).getImage().length() > 200) {
            byte[] decodedString = Base64.decode(parentList.get(0).getImage(), Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            img.setImageBitmap(bitmap);
        }
        else {
            img.setImageResource(R.drawable.ic_photo_camera);
        }

        motherArrayList.clear();
        motherArrayList = sqliteHelper.getMotherDataForEdit(String.valueOf(familyid));
        motherMobileUniqueId = motherArrayList.get(0).getMobile_unique_id();
        MyOperationLayoutAddMother.addForEdit(ActivityEligibleFamilyRegistration.this, motherArrayList);
    }

    public void populateHHList(Spinner spinner, String tableName,
                               String col_id, String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerHHID(tableName, col_id, col_value,
                label, whr);

        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityEligibleFamilyRegistration.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(getString(R.string.household_number));
        spinner.setAdapter(adapter);
    }

    private void setEducationSpinner() {
        String languageId = sph.getString("Language", "");// getting languageId

        if (languageId.equals("1")) {
            ArrayAdapter adapterEducation = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, educationAL);
            adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnEducation.setPrompt("Select Education");
            spnEducation.setAdapter(adapterEducation);

            if (isEditable) {
                if (motherArrayList.get(0).getEducation() != null) {
                    if (motherArrayList.get(0).getEducation().equals("Never gone to school")) {
                        spnEducation.setSelection(0);
                    } else if (motherArrayList.get(0).getEducation().equals("Primary 1 to 5")) {
                        spnEducation.setSelection(1);
                    } else if (motherArrayList.get(0).getEducation().equals("Upper Primary 6 to 8")) {
                        spnEducation.setSelection(2);
                    } else if (motherArrayList.get(0).getEducation().equals("Secondary 9 to 10")) {
                        spnEducation.setSelection(3);
                    } else if (motherArrayList.get(0).getEducation().equals("Higher Secondary 11 to 12")) {
                        spnEducation.setSelection(4);
                    } else if (motherArrayList.get(0).getEducation().equals("Graduate")) {
                        spnEducation.setSelection(5);
                    } else if (motherArrayList.get(0).getEducation().equals("Post Graduate and Above")) {
                        spnEducation.setSelection(6);
                    }
                }
            }
        } else if (languageId.equals("2")) {
            ArrayAdapter adapterEducation = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, educationHindiAL);
            adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnEducation.setPrompt("शिक्षा का चयन करें");
            spnEducation.setAdapter(adapterEducation);

            if (isEditable) {
                if (motherArrayList.get(0).getEducation() != null) {
                    if (motherArrayList.get(0).getEducation().equals("कभी स्कूल नहीं गया")) {
                        spnEducation.setSelection(0);
                    } else if (motherArrayList.get(0).getEducation().equals("प्राथमिक 1 से 5")) {
                        spnEducation.setSelection(1);
                    } else if (motherArrayList.get(0).getEducation().equals("उच्च प्राथमिक 6 से 8")) {
                        spnEducation.setSelection(2);
                    } else if (motherArrayList.get(0).getEducation().equals("माध्यमिक 9 से 10")) {
                        spnEducation.setSelection(3);
                    } else if (motherArrayList.get(0).getEducation().equals("हायर सेकेंडरी 11 से 12")) {
                        spnEducation.setSelection(4);
                    } else if (motherArrayList.get(0).getEducation().equals("स्नातक")) {
                        spnEducation.setSelection(5);
                    } else if (motherArrayList.get(0).getEducation().equals("स्नातकोत्तर और उससे ऊपर")) {
                        spnEducation.setSelection(6);
                    }
                }
            }
        }
    }

    private void setMaritalStatusSpinner() {
        String languageId = sph.getString("Language", "");// getting languageId

        if (languageId.equals("1")) {
            ArrayAdapter adapterMS = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, maritalStatusAL);
            adapterMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMaritalStatus.setPrompt("Select Marital Status");
            spnMaritalStatus.setAdapter(adapterMS);

            if (isEditable) {
                if (motherArrayList.get(0).getMarital_status() != null) {
                    if (motherArrayList.get(0).getMarital_status().equals("Single")) {
                        spnMaritalStatus.setSelection(0);
                    } else if (motherArrayList.get(0).getMarital_status().equals("Married")) {
                        spnMaritalStatus.setSelection(1);
                    } else if (motherArrayList.get(0).getMarital_status().equals("Widow")) {
                        spnMaritalStatus.setSelection(2);
                    }
                }
            }
        } else if (languageId.equals("2")) {
            ArrayAdapter adapterMSs = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, maritalStatusHindiAL);
            adapterMSs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMaritalStatus.setPrompt("वैवाहिक स्थिति का चयन करें");
            spnMaritalStatus.setAdapter(adapterMSs);

            if (isEditable) {
                if (motherArrayList.get(0).getMarital_status() != null) {
                    if (motherArrayList.get(0).getMarital_status().equals("एकल")) {
                        spnMaritalStatus.setSelection(0);
                    } else if (motherArrayList.get(0).getMarital_status().equals("विवाहित")) {
                        spnMaritalStatus.setSelection(1);
                    } else if (motherArrayList.get(0).getMarital_status().equals("विधवा")) {
                        spnMaritalStatus.setSelection(2);
                    }
                }
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

        sqliteHelper = new SqliteHelper(this);
        serverhelper = new ServerHelper(this);

        parent = new Parent();
        mother = new Mother();
        sph = new SharedPrefHelper(this);
        motherHM = new HashMap<>();
        parentList = new ArrayList<>();
        llSeasonalMigrationReason = findViewById(R.id.llSeasonalMigrationReason);
        llHouseHoldDropDown = findViewById(R.id.llHouseHoldDropDown);
        llMainLayout = findViewById(R.id.llMainLayout);
        etMigrationReason = findViewById(R.id.etMigrationReason);
        etxtNoOfFM = findViewById(R.id.etxtNoOfFM);
        tvMigrationReason = findViewById(R.id.tvMigrationReason);
        etxtDateOfScreening = findViewById(R.id.etxtDateOfScreening);
        spnMaritalStatus = findViewById(R.id.spnMaritalStatus);
        spnEducation = findViewById(R.id.spnEducation);
        etxtMother = findViewById(R.id.etxtMother);
        etxtAge = findViewById(R.id.etxtAge);
        /* New Code */
        txtsesMigration = (TextView) findViewById(R.id.sesmigration);
        txtEligibleFamilyReg = (TextView) findViewById(R.id.txtEligibleReg);
        txtHouseNo = (TextView) findViewById(R.id.txtHouseNo);
        txtHeadOfHouse = (TextView) findViewById(R.id.txtHeadOfHh);
        txtHHAadhar = (TextView) findViewById(R.id.txtHHAadharCard);
        txtHHMobile = (TextView) findViewById(R.id.txtHHMobile);
        radiogroupbplCard = (RadioGroup) findViewById(R.id.radiogroupbplCard);
        radiobplCardYes = findViewById(R.id.radiobplCardYes);
        radiobplCardNo = findViewById(R.id.radiobplCardNo);

        btnScan = (Button) findViewById(R.id.scan_button);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtReligion = (TextView) findViewById(R.id.txtReligion);
        txtCastcat = (TextView) findViewById(R.id.txtCastcat);
        txtCast = (TextView) findViewById(R.id.txtCast);
        txtToilet = (TextView) findViewById(R.id.txtToilet);
        txtDrinkingWater = (TextView) findViewById(R.id.txtDrinkingWater);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnAddMother = findViewById(R.id.btnAddMother);
        grpGroup = (RadioGroup) findViewById(R.id.radiogrp);
        rdbuttonyes = findViewById(R.id.rdbuttonyes);
        rdbuttonNo = findViewById(R.id.rdbuttonNo);
        txtLanlod = (TextView) findViewById(R.id.txtLanLoad);
        tvImg = (TextView) findViewById(R.id.tvImg);
        img = (ImageView) findViewById(R.id.img);
        txtbplCard = findViewById(R.id.txtbplCard);
        ivEdit = findViewById(R.id.ivEdit);
        txtHHID = findViewById(R.id.txtHHID);

        etxtHousenumber = (EditText) findViewById(R.id.etxtHouseNo);
        etxtHeadOfHH = (EditText) findViewById(R.id.etxtHeadOfHh);
        etxtAadharCardHH = (EditText) findViewById(R.id.etxtHHAadharCard);
        etxtMobileHH = (EditText) findViewById(R.id.etxtHHMobile);
        spnReligion = (Spinner) findViewById(R.id.spnReligion);
        spnWaterQuality = (Spinner) findViewById(R.id.spnWaterQuality);
        spnToilet = (Spinner) findViewById(R.id.spnToilet);
        spnDrinkingWater = (Spinner) findViewById(R.id.spnDrinkingWater);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnCastecat = (Spinner) findViewById(R.id.spnCastecat);
        spnCast = (Spinner) findViewById(R.id.spnCast);
        spnLanload1 = (Spinner) findViewById(R.id.spnLanload);
        tvTitleText = findViewById(R.id.tvTitleText);
        ivTitleBack = findViewById(R.id.ivTitleBack);
        spnHhId = findViewById(R.id.spnHhId);
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(savingonserver);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.rotate_loading_360));
        btnScan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ActivityEligibleFamilyRegistration.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setResultDisplayDuration(500);
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_parent_registration, menu);
        return true;
    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr, boolean isEditable) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerEF(tableName, col_id, col_value,
                label, whr, isEditable);
        if (items != null) {

            ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                    ActivityEligibleFamilyRegistration.this,
                    android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setPrompt(label);
            spinner.setAdapter(adapter);
        }
    }

    @SuppressLint("NewApi")
    public void btnSave_Click(View vw) {
        if (checkValidation()) {
            sqliteHelper.deleteRowFromTable("mother", "parent_id", String.valueOf(sqliteHelper.getParentId(etxtHousenumber.getText().toString().trim())));

            strHouseNo = etxtHousenumber.getText().toString().trim().toLowerCase();
            strHeadOfHH = etxtHeadOfHH.getText().toString().trim();
            strAadharHH = etxtAadharCardHH.getText().toString().trim();
            strMobileHH = etxtMobileHH.getText().toString().trim();
            intReligion = Integer.parseInt(getSelectedValue(spnReligion));
            strwaterQuality = spnWaterQuality.getSelectedItem().toString();
            intGender = Integer.parseInt(getSelectedValue(spnGender));
            intToilet = Integer.parseInt(getSelectedValue(spnToilet));
            intDrinkingWater = Integer.parseInt(getSelectedValue(spnDrinkingWater));
            intCastecat = Integer.parseInt(getSelectedValue(spnCastecat));
            intCast = Integer.parseInt(getSelectedValue(spnCast));
            intLandHold = Integer.parseInt(getSelectedValue(spnLanload1));

            parent.setHouseNo(strHouseNo.replaceFirst("^0+(?!$)", ""));
            parent.setHeadofHH(strHeadOfHH);
            parent.setAadharCardHH(strAadharHH);
            parent.setMobileHH(strMobileHH);
            parent.setIntcastecat(intCastecat);
            parent.setCast_id(intCast);
            parent.setIntGender(intGender);
            parent.setReligion(intReligion);
            parent.setWater_quality(strwaterQuality);
            parent.setHas_toilet(intToilet);
            parent.setHave_water(intDrinkingWater);
            parent.setLatitude(GlobalVars.lattitude);
            parent.setLongitude(GlobalVars.longitude);
            parent.setSes_migration(strsesMig);
            parent.setLan_Value(Integer.toString(intLandHold));
            parent.setImage(image64);
            if (isEditable) {
                parent.setMobile_unique_id(mobileUniqueId);
            } else {
                parent.setMobile_unique_id(CommonMethods.getUUID());
            }
            parent.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
            parent.setNo_of_member_in_family(etxtNoOfFM.getText().toString().trim());
            parent.setDate_of_screening(etxtDateOfScreening.getText().toString().trim());
            parent.setBpl_card(strBPL);

            if (parent.getHouseNo().equals("")
                    || parent.getHeadofHH().equals("")
                    || !strHeadOfHH.matches("[a-zA-Z ]+")
            ) {

                if (parent.getHouseNo().equals("")) {
                    etxtHousenumber.setError(strHouseNMandatory);
                }

                if (parent.getHeadofHH().equals("")) {
                    etxtHeadOfHH.setError(strHHHMandatory);
                }
                if (!strHeadOfHH.matches("[a-zA-Z ]+")) {
                    etxtHeadOfHH.requestFocus();
                    etxtHeadOfHH.setError(strOnlyAlpha);
                }
            } else {
                etxtHeadOfHH.setError(null);
                etxtHousenumber.setError(null);
                etxtHousenumber.setText("");
                etxtHeadOfHH.setText("");
                try {
                    local_parent_id = sqliteHelper.EligibleFamilyRegistration(parent, 0, mobileUniqueId);
                    GlobalVars.familystatus = 0;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (local_parent_id > 0) {
                    //mother data
                    motherHM.clear();
                    motherHM = MyOperationLayoutAddMother.display(ActivityEligibleFamilyRegistration.this);
                    ArrayList<String> nameAl = new ArrayList<>();
                    ArrayList<String> ageAl = new ArrayList<>();
                    ArrayList<String> maritalStatusAl = new ArrayList<>();
                    ArrayList<String> educationAl = new ArrayList<>();

                    nameAl = motherHM.get("name");
                    ageAl = motherHM.get("age");
                    maritalStatusAl = motherHM.get("maritalStatus");
                    educationAl = motherHM.get("education");
                    if (nameAl.size() > 0 && ageAl.size() > 0 && educationAl.size() > 0 && maritalStatusAl.size() > 0
                    ) {
                        for (int i = 0; i < nameAl.size(); i++) {
                            Mother mother = new Mother();
                            mother.setMotherName(nameAl.get(i));
                            mother.setAge(ageAl.get(i));
                            if (maritalStatusAl.get(i).equals("Please Select")) {
                                mother.setMarital_status("");
                            } else {
                                mother.setMarital_status(maritalStatusAl.get(i));
                            }

                            if (educationAl.get(i).equals("Please Select")) {
                                mother.setEducation("");
                            } else {
                                mother.setEducation(educationAl.get(i));
                            }
                            mother.setHhId(parent.getHouseNo());
                            mother.setParent_id(sqliteHelper.getParentId(parent.getHouseNo()));
                            /*if (isEditable){
                                mother.setMobile_unique_id(motherMobileUniqueId);
                            }else {*/
                            mother.setMobile_unique_id(CommonMethods.getUUID());
                            //}
                            mother.setCreated_on_mobile(CommonMethods.getCurrentDateTime());

                            local_m_id = sqliteHelper.MotherRegistration(mother);
                        }
                    } /*else {
                        Toast.makeText(context, "Please add mother by clicking on add mother.", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                    /*mother.setMotherName(etxtMother.getText().toString().trim());
                    mother.setAge(etxtAge.getText().toString().trim());
                    mother.setMarital_status(spnMaritalStatus.getSelectedItem().toString().trim());
                    mother.setEducation(spnEducation.getSelectedItem().toString().trim());
                    mother.setHhId(parent.getHouseNo());
                    mother.setParent_id(sqliteHelper.getParentId(parent.getHouseNo()));
                    if (isEditable){
                        mother.setMobile_unique_id(motherMobileUniqueId);
                    }else {
                        mother.setMobile_unique_id(CommonMethods.getUUID());
                    }
                    mother.setCreated_on_mobile(CommonMethods.getCurrentDateTime());

                    local_m_id = sqliteHelper.MotherRegistration(mother,motherMobileUniqueId);*/

                /*Toast t = Toast.makeText(getApplicationContext(), strRegDone, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP, 0, 0);
                t.show();*/
                    GlobalVars.EFposition = 1;
                    showSubmitDialog(context, getString(R.string.household_registration),
                            getString(R.string.household_registration_success_message));
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), strTryagain, Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.TOP, 0, 0);
                    t.show();
                }

            }
        }
    }

    private boolean checkValidation() {
        if (isEditable == false) {
            int id = sqliteHelper.HHNoExists(etxtHousenumber.getText().toString().trim().toLowerCase());
            if (id > 0) {
                Toast t = Toast.makeText(getApplicationContext(), R.string.enter_unique_household_number, Toast.LENGTH_LONG);
                t.setGravity(Gravity.TOP, 0, 0);
                t.show();
                return false;
            }
        }
        if (!etxtAadharCardHH.getText().toString().trim().equals("")) {
            if (etxtAadharCardHH.getText().toString().trim().length() < 12) {
                etxtAadharCardHH.requestFocus();
                etxtAadharCardHH.setError(strValidAadhar);
                return false;
            }
        }
        if (!etxtMobileHH.getText().toString().trim().equals("")) {
            if (etxtMobileHH.getText().toString().trim().length() < 10) {
                etxtMobileHH.requestFocus();
                etxtMobileHH.setError(getString(R.string.please_enter_valid_mobile_number));
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
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn // spinner
                // class
                // method....always
                // use
                .getSelectedItemId());
        return data.getValue();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(getString(R.string.house_hold_exit_message) + "?");
        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(
                        ActivityEligibleFamilyRegistration.this,
                        MainMenuRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        String item = parent.getItemAtPosition(position).toString();
        String languageId = sph.getString("Language", "");// getting languageId


        switch (parent.getId()) {
            case R.id.spnReligion:
                /*populateList(spnCastecat, "caste_category", "caption_id", "category",
                        getString(R.string.select_social_categories), "where language_id=" + languageId, isEditable);*/

                break;
            case R.id.spnCastecat:
                int i = Integer.parseInt(getSelectedValue(spnCastecat));
                String selectedReligion = spnReligion.getSelectedItem().toString();

                if (selectedReligion.equalsIgnoreCase("Hinduism\n")) {
                    populateList(spnCast, "cast", "cast_id", "value",
                            "Select Caste", "where cast_category_id=" + i, isEditable);
                } else {
                    ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
                    SpinnerHelper spinnerHelper = new SpinnerHelper("N/A", "0");
                    items.add(spinnerHelper);
                    ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                            ActivityEligibleFamilyRegistration.this,
                            android.R.layout.simple_spinner_item, items);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCast.setPrompt("N/A");
                    spnCast.setAdapter(adapter);
                }
                break;
            case R.id.spnCast:
                break;
        }
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
            new sync_sevika_helper(this).execute("");
        }
    }

    @SuppressLint("ResourceAsColor")
    public Bitmap compressImage(String filePath) {
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
                        bitmap = imageLatLogHeper.compressImage(file + "", "");
                        img.setImageBitmap(bitmap);
                        image64 = encodeTobase64(bitmap);


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {

                    switch (requestCode) {
                        case IntentIntegrator.REQUEST_CODE:
                            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                            if (resultCode == RESULT_OK) {
                                if (scanningResult != null) {
                                    //we have a result
                                    String scanContent = scanningResult.getContents();
                                    String scanFormat = scanningResult.getFormatName();

                                    // process received data
                                    if (scanContent != null && !scanContent.isEmpty()) {
                                        processScannedData(scanContent);
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Scan Cancelled", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "No Scan Data Received", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }


                            break;
                        default:
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

                                bitmap = compressImage(f.getAbsolutePath() + "");
                                bitmap = getResizedBitmap(bitmap, 350);

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

            } else {

                switch (requestCode) {
                    case IntentIntegrator.REQUEST_CODE:
                        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                        if (resultCode == RESULT_OK) {
                            if (scanningResult != null) {
                                //we have a result
                                String scanContent = scanningResult.getContents();
                                String scanFormat = scanningResult.getFormatName();

                                // process received data
                                if (scanContent != null && !scanContent.isEmpty()) {
                                    processScannedData(scanContent);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Scan Cancelled", Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No Scan Data Received", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }


                        break;
                    default:
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void processScannedData(String scanData) {
        Log.d("Rajdeol", scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol", "Start document");
                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    String uid = parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR);
                    etxtAadharCardHH.setText(uid);

                } else if (eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol", "End tag " + parser.getName());

                } else if (eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol", "Text " + parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function

    public void getTimeAndLAtLang() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system
        try {
            Location location = getLatitudeLogitude();
            lat = "Lat.: " + location.getLatitude();
            lang = " Long.:" + location.getLongitude();
        } catch (Exception e) {
            e.printStackTrace();
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