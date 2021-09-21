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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.ImageLatLogHeper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
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

public class ActivityChildReg extends Activity implements AdapterView.OnItemSelectedListener {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    static EditText etxtDateOfBirth, etxtChildName, etxtBirthWeight, etxtBirthHeight,
            etxtBirthMuac, etxtAdolescentHb, etxtDOB;
    public Button btnSubmit;
    String strDone, strChildReg, strTryagain, strYes, strNo,
            strCancel, strFooter, strMandatory, strSubmit, strLat, strLang, strOnlyAlpha,
            strMuac, strChildHB, strEdema, strHaveEdema = "0", strNotification, strPhotoInsHead,
            strPhotoIns, strRegisteredICDS = "", strHealthFacility = "", strChildBreastfed = "",
            strComplementaryFoodNo = "";
    String mCurrentPhotoPath, strDobInMonth = "";
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    String please_select = "Please Select";
    public static android.app.Dialog submit_alert;
    int childAgeInMonth = 0;
    ArrayList<Double> age_weight = new ArrayList<Double>();
    ImageView imgWeight;

    DatePickerDialog datePickerDialog;
    private int mYear;
    private int mMonth;
    private int mDay;
    public static String age_in_month;

    TextView txtBirthHeight, txtGender, txtFooter, txtGps, txtPhotograph, txtSelectParent, txtBirthOrder,
            txtBirthWeight,
            txtDateOfBirth, txtChildRegistration, txtBirthMuac, txtChildHB, txtEdema, tvNotification;
    RadioGroup radioGroup, rgSelectWomen, rgICDS, rgHealthFacility, rgDOB, rgchildBreastfed, rgComplementaryFood;
    RadioButton radioYes, radioNo, rbPregnantWomen, rbExistingWomen,
            rbICDSYes, rbICDSNo, rbHealthFacilityYes, rbHealthFacilityNo, rbDobmonth, rbDob, rbchildBreastfedYes,
            rbchildBreastfedNo;
    Button btnGps;
    Spinner spnBirthOrder, spnSelectParent, spnGender, spnBreastfeedInitiated, spnDisability, spnHhId, spnHB;
    ImageView btnClicked;
    Child child;
    long etxtDOBdigit = 0;
    ImageView imgHbStatus, ivDOB;
    String strBPL = "";
    LinearLayout llchildbreastfed, llBreastfeedInitiated, llComplementaryFood, llMonthComplementaryFeeding;

    RadioGroup radiogroupbplCard, rgchildPremature;
    RadioButton radiobplCardYes, radiobplCardNo;
    long months;

    EditText etxtMonthComplementaryFeeding;

    private int day, month, year;

    CheckBox checkbox;
    String gender, strHHId, bweight, dob, parentid, birthWeight, selectparent, border, photograph, disablity,
            gps, birthHeight, dobinMonth,
            fullNameOfChild, str, strFiveYearReg, strSelectWomen = "", strBreastfeedInitiated = "",
            strComplementaryFeeding = "", strchildPremature = "";
    String[] birthStatus = {"Please Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String image64 = "";
    ServerHelper serverhelper;
    TextView txtSelectAdolescent, txtbplCard;
    Spinner spnSelectAdolescent;
    String strAdolscentName;
    private TextView txtHHID;
    private TextView txtChildFullName;
    private TextView txtDisablity;
    private Bitmap bitmap;
    private int user_id;
    private ImageLoadingUtils utils;
    TextView tvTitleText, txtchildBreastfed;
    ImageView ivTitleBack;
    private Context context = this;
    ArrayList<PregnantWomen> pregnantWomenList;
    private String HouseNo = "";
    public static EditText etxtDateOfScreening;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_child_reg);

        initialization();
        setLanguage();
        uploadAttendance();
        populateHBspn();

        /**
         * select mother make editable false until not choose the house hold
         **/
        rbPregnantWomen.setEnabled(false);
        rbExistingWomen.setEnabled(false);

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguages("en");
        } else if (lngTypt.equals("2")) {
            setLanguages("hi");
        }

        String languageId = sph.getString("Language", "");// getting languageId

        spnBirthOrder.setOnItemSelectedListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); //current Day.

        txtbplCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogClass.showDialog(context, getString(R.string.bpl_information), getString(R.string.bpl_content));
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
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 7 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <= 9.9) {
                            imgHbStatus.setVisibility(View.VISIBLE);
                            imgHbStatus.setImageResource(R.drawable.mod);
                        }
                        if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) >= 10 && Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) <= 10.9) {
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

        @SuppressWarnings("rawtypes")
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, birthStatus);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spnBirthOrder.setPrompt(getString(R.string.select_birth_order));
        spnBirthOrder.setAdapter(aa);

        user_id = sph.getInt("user_id", 0);

        try {
            populateHHList(spnHhId, "eligible_family", "familyid", "house_number",
                    getString(R.string.select_a_house_number), "where anganwadi_center_id=" + user_id);
            populateWomenList2(spnSelectParent, "pregnant_women", "mother", "Pregnant_women_id",
                    "mother_id", "name_of_pregnant_women", "name_of_mother", "Select Mother ", "where user_id=" + user_id);
            populateList(spnSelectAdolescent, "adolescent", "adolescent_id",
                    "adolescent_name", "Select Girl", "where anganwadi_center_id=" + user_id);
            populateList(spnGender, "gender", "id", "gender_value",
                    getString(R.string.select_gender), "where language_id=" + languageId);
            populateList(spnBreastfeedInitiated, "breastfeed_initiated", "id", "breastfeed_value",
                    getString(R.string.select_breastfeeding), "where language_id=" + languageId);
            populateList(spnDisability, "disability", "disability_id", "disability_type",
                    "Select Disability", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        spnHhId.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (spnHhId.getSelectedItemPosition() == 0) {
                    /**
                     * select mother make editable false until not choose the house hold
                     **/
                    rbPregnantWomen.setEnabled(false);
                    rbExistingWomen.setEnabled(false);
                    populateWomenList2(spnSelectParent, "pregnant_women", "mother", "Pregnant_women_id",
                            "mother_id", "name_of_pregnant_women", "name_of_mother", "Select Mother ", "where user_id=" + user_id);
                    populateList(spnSelectAdolescent, "adolescent", "adolescent_id",
                            "adolescent_name", "Select Girl", "where anganwadi_center_id=" + user_id);
                } else {
                    try {
                        String strHouseHoldId = (spnHhId.getSelectedItem().toString());
                        String[] houseHoldId = strHouseHoldId.split("\\(");
                        HouseNo = houseHoldId[0].trim();
                        /**
                         * select mother make editable true when choose the house hold
                         **/
                        rbPregnantWomen.setEnabled(true);
                        rbExistingWomen.setEnabled(true);
                        if (child.getCheck_adol() != 1)
                            populateListAccordingHN(spnSelectParent, "pregnant_women", "mother", "Pregnant_women_id", "mother_id",
                                    "name_of_pregnant_women", "name_of_mother", "Select Parent", HouseNo);
                        else
                            populateAdolListAccordingHN(spnSelectAdolescent, HouseNo);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        /**
                         * select mother make editable false until not choose the house hold
                         **/
                        rbPregnantWomen.setEnabled(false);
                        rbExistingWomen.setEnabled(false);
                        populateWomenList2(spnSelectParent, "pregnant_women", "mother", "Pregnant_women_id",
                                "mother_id", "name_of_pregnant_women", "name_of_mother", "Select Mother ", "where user_id=" + user_id);
                        populateList(spnSelectAdolescent, "adolescent", "adolescent_id",
                                "adolescent_name", "Select Girl", "where anganwadi_center_id=" + user_id);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

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


        rgchildPremature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbchildPrematureYes:
                        strchildPremature = "Yes";
                        break;
                    case R.id.rbchildPrematureNo:
                        strchildPremature = "No";
                        break;
                    case R.id.rbchildPrematureDontremember:
                        strchildPremature = "Don't remember";
                        break;

                }
            }
        });

        tvTitleText.setText(R.string.child_registration);
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strChildReg + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ActivityChildReg.this,
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

        rgSelectWomen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbPregnantWomen:
                        strSelectWomen = "PW";
                        spnSelectParent.setVisibility(View.VISIBLE);
                        populateListAccordingHNOfPW(spnSelectParent, "pregnant_women", "Pregnant_women_id",
                                "name_of_pregnant_women", "Select Parent", HouseNo);
                        break;
                    case R.id.rbExistingWomen:
                        strSelectWomen = "EW";
                        spnSelectParent.setVisibility(View.VISIBLE);
                        populateListAccordingHNOfEM(spnSelectParent, "mother", "mother_id",
                                "name_of_mother", "Select Parent", HouseNo);
                        break;
                }
            }
        });

        //registered ICDS
        rgICDS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbICDSYes:
                        strRegisteredICDS = "Yes";
                        break;
                    case R.id.rbICDSNo:
                        strRegisteredICDS = "No";
                        break;
                }
            }
        });

        rgDOB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbDobmonth:
                        etxtDOB.setVisibility(View.VISIBLE);
                        etxtDateOfBirth.setVisibility(View.GONE);
                        ivDOB.setVisibility(View.GONE);
                        getDateOfBirthFromAge();
                        strDobInMonth = "DOBInMonth";
                        break;

                    case R.id.rbDob:
                        etxtDateOfBirth.setVisibility(View.VISIBLE);
                        etxtDOB.setVisibility(View.GONE);
                        ivDOB.setVisibility(View.VISIBLE);
                        strDobInMonth = "DOB";
                        break;
                }
            }
        });

        try {
            if (gender.equals("Male")) {
                age_weight = sqliteHelper.zScoreValue("ideal_weight_boy", childAgeInMonth);
            } else {
                age_weight = sqliteHelper.zScoreValue("ideal_weight_girl", childAgeInMonth);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        etxtBirthWeight.addTextChangedListener(new TextWatcher() {

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

        etxtDateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    if (etxtDateOfBirth.getText().toString().equals("")) {
                    } else {

                        String dobString = etxtDateOfBirth.getText().toString();

                        Date date = null;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            date = sdf.parse(dobString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (date == null) {

                        } else {
                            Calendar dob = Calendar.getInstance();
                            Calendar today = Calendar.getInstance();

                            dob.setTime(date);

                            int year = dob.get(Calendar.YEAR);
                            int month = dob.get(Calendar.MONTH);
                            int day = dob.get(Calendar.DAY_OF_MONTH);

                            dob.set(year, month + 1, day);

                            months = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);

                            if (!dobString.equalsIgnoreCase("")) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dobString.substring(8, 10)));
                                cal.set(Calendar.MONTH, Integer.parseInt(dobString.substring(5, 7)) - 1);
                                cal.set(Calendar.YEAR, Integer.parseInt(dobString.substring(0, 4)));
                                Date firstDate = cal.getTime();
                                Calendar cal1 = Calendar.getInstance();
                                Date secondDate = cal1.getTime();
                                long diff = (secondDate.getTime() - firstDate.getTime());
                                int diffday = (int) (diff / (1000 * 60 * 60 * 24));
                                if (diffday > 2190) {
                                    Log.e("............", Integer.toString(diffday));
                                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityChildReg.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage(strFiveYearReg);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(ActivityChildReg.this, ActivityChildReg.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        etxtDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    etxtDOBdigit = Integer.parseInt(etxtDOB.getText().toString());
                    if (etxtDOBdigit >= 6) {
                        llchildbreastfed.setVisibility(View.VISIBLE);
                    }
                    if (etxtDOBdigit <= 12) {
                        llComplementaryFood.setVisibility(View.VISIBLE);
                    } else {
                        llchildbreastfed.setVisibility(View.GONE);
                        llComplementaryFood.setVisibility(View.GONE);
                        llMonthComplementaryFeeding.setVisibility(View.GONE);
                    }

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        rgchildBreastfed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbchildBreastfedYes:
                        strChildBreastfed = "Yes";
                        break;

                    case R.id.rbchildBreastfedNo:
                        strChildBreastfed = "No";
                        break;
                }
            }
        });

        rgComplementaryFood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbComplementaryFoodYes:
                        strComplementaryFoodNo = "Yes";
                        llMonthComplementaryFeeding.setVisibility(View.VISIBLE);
                        break;

                    case R.id.rbComplementaryFoodNo:
                        strComplementaryFoodNo = "No";
                        llMonthComplementaryFeeding.setVisibility(View.GONE);
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
    }

    private void setLanguages(String languageToLoad) {
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
                String dobinMonth = sdf1.format(c.getTime());
                String formattedDate = sdf.format(c.getTime());
                etxtDateOfBirth.setText(formattedDate);
                etxtDOB.setText(getMonthfromDOB(dobinMonth));
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

    public static String getMonthfromDOB(String date) {
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
        int calAge = (Integer.parseInt(age_in_month)); //for years return this.
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
                        month = Integer.parseInt(dobDate[0]);
                        if (dobDate.length > 1) {
                            month = Integer.parseInt(dobDate[1]);
                        }
                    } else {
                        month = Integer.parseInt(ageee);
                    }
                    if (etxtDOB.getText().toString().trim().equalsIgnoreCase("")) {
                        month = 0;
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - year);
                        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) + 1 - month);
                        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 30);
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

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                etxtDateOfBirth.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(context, listener, year, month, day);
        dpDialog.show();
    }

    public void click_edemaYes(View a) {
        if (((RadioButton) a).isChecked()) {
            strHaveEdema = "1";
            radioNo.setChecked(false);
        }
    }

    public void click_edemaNo(View b) {
        if (((RadioButton) b).isChecked()) {
            strHaveEdema = "0";
            radioYes.setChecked(false);
        }
    }

    public void populateHHList(Spinner spinner, String tableName,
                               String col_id, String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinnerHHID(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(getString(R.string.house_hold));
        spinner.setAdapter(adapter);
    }

    public void initialization() {
        child = new Child();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        serverhelper = new ServerHelper(this);
        pregnantWomenList = new ArrayList<>();

        txtChildRegistration = (TextView) findViewById(R.id.txtChildRegistration);
        txtHHID = (TextView) findViewById(R.id.txtHholdId);
        txtChildFullName = (TextView) findViewById(R.id.txtChildName);
        txtSelectParent = (TextView) findViewById(R.id.txtSelectParent);
        txtDateOfBirth = (TextView) findViewById(R.id.txtDateOfBirth);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtBirthHeight = (TextView) findViewById(R.id.txtBirthHeight);
        txtBirthWeight = (TextView) findViewById(R.id.txtBirthWeight);
        txtBirthMuac = (TextView) findViewById(R.id.txtBirthMuac);
        txtChildHB = (TextView) findViewById(R.id.txtChildHB);
        txtBirthOrder = (TextView) findViewById(R.id.txtBirthOrder);
        txtDisablity = (TextView) findViewById(R.id.txtDisability);
        txtEdema = (TextView) findViewById(R.id.txtEdema);
        txtPhotograph = (TextView) findViewById(R.id.txtPhotograph);
        txtGps = (TextView) findViewById(R.id.txtGps);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        tvNotification = (TextView) findViewById(R.id.tvNotification);
        imgHbStatus = (ImageView) findViewById(R.id.imgHbStatus);
        ivDOB = (ImageView) findViewById(R.id.ivDOB);
        llchildbreastfed = (LinearLayout) findViewById(R.id.llchildbreastfed);
        llBreastfeedInitiated = (LinearLayout) findViewById(R.id.llBreastfeedInitiated);
        llComplementaryFood = (LinearLayout) findViewById(R.id.llComplementaryFood);
        llMonthComplementaryFeeding = (LinearLayout) findViewById(R.id.llMonthComplementaryFeeding);

        rgchildPremature = (RadioGroup) findViewById(R.id.rgchildPremature);
        radiogroupbplCard = (RadioGroup) findViewById(R.id.radiogroupbplCard);
        radiobplCardYes = findViewById(R.id.radiobplCardYes);
        radiobplCardNo = findViewById(R.id.radiobplCardNo);

        etxtBirthHeight = (EditText) findViewById(R.id.etxtBirthHeight);
        etxtBirthWeight = (EditText) findViewById(R.id.etxtBirthWeight);
        etxtDateOfBirth = (EditText) findViewById(R.id.etxtDateOfBirth);
        etxtChildName = (EditText) findViewById(R.id.etxtChildName);
        etxtBirthMuac = (EditText) findViewById(R.id.etxtBirthMuac);
        etxtMonthComplementaryFeeding = (EditText) findViewById(R.id.etxtMonthComplementaryFeeding);
        etxtDOB = (EditText) findViewById(R.id.etxtDOB);
        etxtAdolescentHb = (EditText) findViewById(R.id.etxtAdolescentHb);
        spnHB = (Spinner) findViewById(R.id.spnHBN);
        spnBirthOrder = (Spinner) findViewById(R.id.spnBirthOrder);
        spnSelectParent = (Spinner) findViewById(R.id.spnSelectParent);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnBreastfeedInitiated = (Spinner) findViewById(R.id.spnBreastfeedInitiated);
        spnDisability = (Spinner) findViewById(R.id.spnDisability);
        spnHhId = (Spinner) findViewById(R.id.spnHhId);

        btnClicked = (ImageView) findViewById(R.id.btnClicked);
        imgWeight = (ImageView) findViewById(R.id.imgWeight);
        btnGps = (Button) findViewById(R.id.btnGps);
        btnSubmit = (Button) findViewById(R.id.btnChildReg);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rgSelectWomen = (RadioGroup) findViewById(R.id.rgSelectWomen);
        radioYes = (RadioButton) findViewById(R.id.radioYes);
        radioNo = (RadioButton) findViewById(R.id.radioNo);
        rbPregnantWomen = (RadioButton) findViewById(R.id.rbPregnantWomen);
        rbExistingWomen = (RadioButton) findViewById(R.id.rbExistingWomen);

        txtSelectAdolescent = (TextView) findViewById(R.id.txtSelectAdolescent);
        txtbplCard = (TextView) findViewById(R.id.txtbplCard);
        spnSelectAdolescent = (Spinner) findViewById(R.id.spnSelectAdolescent);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        tvTitleText = findViewById(R.id.tvTitleText);
        txtchildBreastfed = findViewById(R.id.txtchildBreastfed);
        ivTitleBack = findViewById(R.id.ivTitleBack);
        etxtDateOfScreening = findViewById(R.id.etxtDateOfScreening);
        rgICDS = findViewById(R.id.rgICDS);
        rgDOB = findViewById(R.id.rgDOB);
        rbDobmonth = findViewById(R.id.rbDobmonth);
        rbDob = findViewById(R.id.rbDob);
        rbICDSYes = findViewById(R.id.rbICDSYes);
        rbICDSNo = findViewById(R.id.rbICDSNo);
        rgHealthFacility = findViewById(R.id.rgHealthFacility);
        rgchildBreastfed = findViewById(R.id.rgchildBreastfed);
        rgComplementaryFood = findViewById(R.id.rgComplementaryFood);
        rbchildBreastfedYes = findViewById(R.id.rbchildBreastfedYes);
        rbchildBreastfedNo = findViewById(R.id.rbchildBreastfedNo);
        rbHealthFacilityYes = findViewById(R.id.rbHealthFacilityYes);
        rbHealthFacilityNo = findViewById(R.id.rbHealthFacilityNo);
    }

    private void setLanguage() {
        btnGps.setText("Lat: " + GlobalVars.lattitude + ", Long: " + GlobalVars.longitude);
        String languageId = sph.getString("Language", "1");// getting languageId
        strChildReg = sqliteHelper.LanguageChanges(ConstantValue.LANChildReg, languageId);
        strHHId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId, languageId);
        dob = sqliteHelper.LanguageChanges(ConstantValue.LANDateOfBirth, languageId);
        bweight = sqliteHelper.LanguageChanges(ConstantValue.LANBirthWeight, languageId);
        border = sqliteHelper.LanguageChanges(ConstantValue.LANBirthOrder, languageId);
        selectparent = sqliteHelper.LanguageChanges(ConstantValue.LANSelectParent, languageId);
        disablity = sqliteHelper.LanguageChanges(ConstantValue.LANDisability, languageId);
        photograph = sqliteHelper.LanguageChanges(ConstantValue.LANPhoto, languageId);
        gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS, languageId);
        birthHeight = sqliteHelper.LanguageChanges(ConstantValue.LANBirthHeight, languageId);
        strMuac = sqliteHelper.LanguageChanges(ConstantValue.LANMuac, languageId);
        strEdema = sqliteHelper.LanguageChanges(ConstantValue.LANEdema, languageId);
        fullNameOfChild = sqliteHelper.LanguageChanges(ConstantValue.LANChildName, languageId);
        gender = sqliteHelper.LanguageChanges(ConstantValue.LANGender, languageId);
        strSubmit = sqliteHelper.LanguageChanges(ConstantValue.LANSave, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strNotification = sqliteHelper.LanguageChanges(ConstantValue.LANNotice, languageId);

        strDone = sqliteHelper.LanguageChanges(ConstantValue.LANDone, languageId);
        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strOnlyAlpha = sqliteHelper.LanguageChanges(ConstantValue.LANOnlyAlpha, languageId);
        strMandatory = sqliteHelper.LanguageChanges(ConstantValue.LANMandatory, languageId);
        strChildHB = sqliteHelper.LanguageChanges(ConstantValue.LANChildHb, languageId);
        strFiveYearReg = sqliteHelper.LanguageChanges(ConstantValue.LANFiveYearReg, languageId);

        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, languageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, languageId);

        strPhotoInsHead = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsHead, languageId);
        strPhotoIns = sqliteHelper.LanguageChanges(ConstantValue.LANPhotoInsChild, languageId);
        strAdolscentName = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescent, languageId);

        // setting text
        etxtDateOfScreening.setText(CommonMethods.getCurrentDate());
        txtChildRegistration.setText(strChildReg);
        txtDateOfBirth.setText(dob);
        txtBirthWeight.setText(bweight);
        txtBirthOrder.setText(border);
        txtSelectParent.setText(R.string.select_mother);
        txtPhotograph.setText(photograph);
        txtGps.setText(gps);
        txtBirthHeight.setText(birthHeight);
        etxtDOB.setText(dobinMonth);
        txtBirthMuac.setText(strMuac);
        txtChildHB.setText(strChildHB);
        txtEdema.setText(strEdema);
        txtChildFullName.setText(R.string.name_of_the_child);
        txtGender.setText(R.string.sex);
        txtHHID.setText(R.string.select_a_house_number);
        txtDisablity.setText(disablity);
        btnSubmit.setText(strSubmit);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        radioYes.setText(strYes);
        radioNo.setText(strNo);
        tvNotification.setText(strNotification);
        txtSelectAdolescent.setText(strAdolscentName);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data;
        data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        return data.getValue();
    }

    @SuppressLint("NewApi")
    public void click_save(View v) {
        if (checkValidation()) {
            if (spnHhId.getSelectedItemPosition() == 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.select_a_house_number), Toast.LENGTH_LONG).show();
            } else {
                String dob1 = etxtDateOfBirth.getText().toString().trim();
                if (!dob1.equalsIgnoreCase("")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dob1.substring(8, 10)));
                    cal.set(Calendar.MONTH, Integer.parseInt(dob1.substring(5, 7)) - 1);
                    cal.set(Calendar.YEAR, Integer.parseInt(dob1.substring(0, 4)));
                    Date firstDate = cal.getTime();
                    Calendar cal1 = Calendar.getInstance();
                    Date secondDate = cal1.getTime();
                    long diff = (secondDate.getTime() - firstDate.getTime());
                    int diffday = (int) (diff / (1000 * 60 * 60 * 24));
                    if (diffday > 2190) {
                        Log.e("............", Integer.toString(diffday));
                        AlertDialog alertDialog = new AlertDialog.Builder(ActivityChildReg.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage(strFiveYearReg);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ActivityChildReg.this, ActivityChildReg.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();

                    } else {
                        try {

                            String familyid = "";
                            String houseNo = "";

                            String strHouseHoldId = (spnHhId.getSelectedItem().toString());
                            String[] houseHoldId = strHouseHoldId.split("\\(");
                            houseNo = houseHoldId[0].trim();

                            fullNameOfChild = etxtChildName.getText().toString().trim();

                            dob = etxtDateOfBirth.getText().toString().trim();
                            birthWeight = etxtBirthWeight.getText().toString();
                            birthHeight = etxtBirthHeight.getText().toString();
                            dobinMonth = etxtDOB.getText().toString().trim();
                            border = spnBirthOrder.getSelectedItem().toString();
                            gender = getSelectedValue(spnGender);
                            strBreastfeedInitiated = getSelectedValue(spnBreastfeedInitiated);
                            disablity = getSelectedValue(spnDisability);
                            strComplementaryFeeding = etxtMonthComplementaryFeeding.getText().toString().trim();

                            try {
                                familyid = getSelectedValue(spnHhId);
                                int p = Integer.parseInt(familyid);
                                child.setParent_id(p);
                                if (child.getCheck_adol() != 1) {
                                    parentid = getSelectedValue(spnSelectParent);
                                } else {
                                    parentid = getSelectedValue(spnSelectAdolescent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            child.setChild_name(fullNameOfChild);
                            child.setDate_of_birth(dob);
                            child.setDob_month(dobinMonth);

                            child.setHeight(birthHeight);
                            child.setGender(gender);
                            if (border.equals(please_select)) {
                                child.setBirth_order(0);
                            } else {
                                child.setBirth_order(Integer.parseInt(border));
                            }
                            child.setDisablity(disablity);
                            child.setChild_weight(birthWeight);
                            child.setBpl_card(strBPL);
                            child.setMuac(etxtBirthMuac.getText().toString());

                            if (etxtAdolescentHb.getText().toString().trim().equals("")) {
                                child.setHb("0");
                            } else {
                                child.setHb(etxtAdolescentHb.getText().toString().trim());
                            }
                            child.setHHID(houseNo);
                            child.setMultimedia(image64);
                            if (strSelectWomen.equals("PW")) {
                                child.setPregnent_women_id(parentid);
                                child.setSelected_women_type(strSelectWomen);
                            } else {
                                child.setPregnent_women_id(parentid);
                                child.setSelected_women_type(strSelectWomen);
                            }
                            child.setEdema(strHaveEdema);
                            child.setMobile_unique_id(CommonMethods.getUUID() + sph.getInt("user_id", 0));
                            child.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
                            child.setRegistred_icds(strRegisteredICDS);
                            child.setHealth_facility(strHealthFacility);
                            child.setIs_child_breastfed(strChildBreastfed);
                            child.setIs_complementary_food(strComplementaryFoodNo);
                            child.setSelect_breastfeed_initiated(strBreastfeedInitiated);
                            child.setMonth_complementary_feeding(strComplementaryFeeding);
                            child.setIs_child_Premature(strchildPremature);

                            if (spnHhId.getSelectedItemPosition() == 0) {
                                Toast.makeText(getApplicationContext(), getString(R.string.select_household_number), Toast.LENGTH_LONG).show();
                            }

                            if (child.getChild_name().equals("")) {
                                etxtChildName.setError(getString(R.string.name_of_the_child) + " " + strMandatory);
                                etxtChildName.setFocusable(true);
                            } else if (child.getChild_name().equals("")
                                    || child.getDate_of_birth().equals("")
                            ) {
                                if (child.getChild_name().equals("")) {
                                    etxtChildName.setError(getString(R.string.name_of_the_child) + " " + strMandatory);
                                    etxtChildName.setFocusable(true);
                                }
                                if (child.getDate_of_birth().equals("")) {
                                    etxtDateOfBirth.setError(dob + " " + strMandatory);
                                }

                            } else if (!fullNameOfChild.matches("[a-zA-Z ]+")) {
                                etxtChildName.requestFocus();
                                etxtChildName.setError("Error");
                                etxtChildName.setError(strOnlyAlpha);
                            } else {
                                long id = sqliteHelper.ChildRegistration(child);
                                if (id > 0) {
                                    if (strSelectWomen.equals("PW")) {
                                        sqliteHelper.makeMother(child.getDate_of_birth(), parentid);
                                        //get all data if pregnant women convert into mother and insert to mother table
                                        pregnantWomenList = sqliteHelper.getPregnantWomenToMother();
                                        if (pregnantWomenList.size() > 0) {
                                            //insert data to mother table
                                            long idd = sqliteHelper.MotherRegistration2(pregnantWomenList);
                                            if (idd > 0) {
                                                Log.e("Child>>>", "insert pregnant data to mother ");
                                            }
                                        }
                                    }
                                }
                                showSubmitDialog(context, getString(R.string.child_registration),
                                        getString(R.string.child_registration_success_message));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (strDobInMonth.equals("")) {
                        Toast.makeText(context, R.string.choose_age_or_dob, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (rbDobmonth.isChecked()) {
                        if (etxtDOB.getText().toString().trim().length() <= 0) {
                            EditText flagEditfield = etxtDOB;
                            String msg = getString(R.string.Please_enter_valid_agemonth);
                            etxtDOB.setError(msg);
                            etxtDOB.requestFocus();
                        }

                    } else {
                        if (etxtDateOfBirth.getText().toString().trim().length() <= 0) {
                            etxtDateOfBirth.setError("" + "Error");
                            Toast.makeText(getApplicationContext(), R.string.date_birth_mandatory, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    private boolean checkValidation() {
        if (rbPregnantWomen.isChecked() || rbExistingWomen.isChecked()) {
        } else {
            Toast.makeText(getApplicationContext(), R.string.select_mother_type, Toast.LENGTH_LONG).show();
            return false;
        }

        if (spnSelectParent.getSelectedItem().toString().trim().equals("Please Select")) {
            Toast.makeText(context, R.string.please_select_mother_first, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etxtAdolescentHb.getText().toString().trim().equals("")) {
            if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) < 2) {
                etxtAdolescentHb.requestFocus();
                etxtAdolescentHb.setError(getString(R.string.hb_should_greter_than_2));
                return false;
            } else if (Double.parseDouble(etxtAdolescentHb.getText().toString().trim()) > 20) {
                etxtAdolescentHb.requestFocus();
                etxtAdolescentHb.setError(getString(R.string.hb_should_less_20));
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
                Intent intent = new Intent(context, MainMenuRegistrationActivity.class);
                context.startActivity(intent);
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    public String compareTwoDates(String dateToday, String dateDob) {
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
        System.out.println("The date 1 is: " + sdformat.format(d1));
        System.out.println("The date 2 is: " + sdformat.format(d2));
        if (d1.compareTo(d2) > 0) {
            compareDate = "Today Date Greater";
            System.out.println("Date 1 occurs after Date 2");
        } else if (d1.compareTo(d2) < 0) {
            compareDate = "Today Date Lesser";
            System.out.println("Date 1 occurs before Date 2");
        } else if (d1.compareTo(d2) == 0) {
            compareDate = "Today Date Equal";
            System.out.println("Both dates are equal");
        }
        return compareDate;
    }

    private int parseInt(Object selectedItem) {
        return 0;
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

    private void populateListAccordingHNOfPW(Spinner spinner, String tableName,
                                             String col_id, String col_value,
                                             String label, String whr) {

        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

        items = sqliteHelper.populateSpinnerbyHouseNoOfPW(tableName, col_id,
                col_value, label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);

    }

    private void populateListAccordingHNOfEM(Spinner spinner, String tableName,
                                             String col_id, String col_value,
                                             String label, String whr) {

        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

        items = sqliteHelper.populateSpinnerbyHouseNoOfEM(tableName, col_id,
                col_value, label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);

    }

    private void populateListAccordingHN(Spinner spinner, String tableName, String tableName2,
                                         String col_id, String col_id2, String col_value, String col_value2,
                                         String label, String whr) {

        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

        items = sqliteHelper.populateSpinnerbyHouseNo(tableName, tableName2, col_id,
                col_id2, col_value, col_value2, label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);

    }

    private void populateAdolListAccordingHN(Spinner spinner, String whr) {

        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

        items = sqliteHelper.populateAdolescentSpinner(whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Girl");
        spinner.setAdapter(adapter);

    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSpinner(tableName, col_id, col_value,
                label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    public void populateWomenList2(Spinner spinner, String tableName, String tableName2, String col_id,
                                   String col_id2, String col_value, String col_value2, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateWomenSpinnerFromTwoTables(tableName, tableName2, col_id, col_id2, col_value,
                col_value2, label, whr);
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivityChildReg.this, android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
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
                        bitmap = imageLatLogHeper.compressImage(file + "", "");
                        btnClicked.setImageBitmap(bitmap);
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

                                btnClicked.setImageBitmap(bitmap);

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

    public void click_getgps(View vw) {
        Button btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setText(strLat + ": " + GlobalVars.lattitude + "," + strLang + ": "
                + GlobalVars.longitude);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strChildReg + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ActivityChildReg.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_child_reg, menu);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            etxtDateOfBirth.setText(formattedDate);
            etxtDateOfBirth.setError(null);
        }
    }

    @SuppressLint("NewApi")
    public static class DatePickerFragment1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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