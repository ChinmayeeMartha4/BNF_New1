package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GPSTracker;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ImageLoadingUtils;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Activity_Add_Worker extends Activity {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE1 = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE2 = 101;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE3 = 102;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE4 = 103;
    private static final int CAMERA_PIC_REQUEST = 1;
    static EditText etWhichAwc, etWorkerName, etWorkerDob, etWorkerDOJ, etWorkerQualification, etWorkerTraining,
            etWorkerAWCDistance, etWorkerMobile, etWorkerAlMobile, etHelperName, etHelperDob, etHelperDoj, etHelperTraining,
            etHelperDistance, etHelperMobile, etHelperAltMobile;
    static int calender_code = 0;
    Spinner spnWorkerPosition, spnWorkerReligion, spnWorkerCC, spnWorkerCast;
    ImageView WorkerImg1, WorkerImg2, HelperImg1, HelperImg2;
    RadioGroup rgTempInCharge, rgWorkerResidance, rgHelperResidance;
    RadioButton rbTempInChargeYes, rbTempInChargeNo, rbWorkerResidanceNative, rbWorkerResidanceOther,
            rbHelperResidanceNative, rbHelperResidanceOther;
    TextView tvWorkerPosition, tvWorkerTempCharge, tvWorkerWhichAwc, tvWorkerName, tvWorkerReligion, tvWorkerCC, tvWorkerCast,
            tvWorkerDOB, tvWorkerDOJ, tvWorkerEdu, tvWorkerTraining, tvWorkerResidance, tvWorkerAwcDistance, tvWorkerMobileNo,
            tvWorkerAltMob, tvWorkerFullPhoto, tvWorkerPPPhoto, tvHelperDetails, tvHelperName, tvHelperDOB, tvHelperDOJ, tvHelperTraining,
            tvHelperResidance, tvHelperAwcDistance, tvHelperMob, tvHelperAtlMob, tvHelperFullPhoto, tvHelperPPPhoto, txtFooter;
    Button tvWorkerSave;
    SqliteHelper sqlitehelper;
    SharedPrefHelper sph;
    int awc_id;
    String image1 = "", image2 = "", image3 = "", image4 = "";
    byte[] image;
    ArrayAdapter<String> adpWT, adpWR, adpWCC, adpWC;
    private Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
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
        setContentView(R.layout.activity_add_worker);

        sqlitehelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        awc_id = sph.getInt("user_id", 0);
        String languageId = sph.getString("Language", "1");

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Add Worker Details");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        spnWorkerPosition = (Spinner) findViewById(R.id.spnWorkerPosition);
        spnWorkerReligion = (Spinner) findViewById(R.id.spnWorkerReligion);
        spnWorkerCC = (Spinner) findViewById(R.id.spnWorkerCC);
        spnWorkerCast = (Spinner) findViewById(R.id.spnWorkerCast);

        WorkerImg1 = (ImageView) findViewById(R.id.WorkerImg1);
        WorkerImg2 = (ImageView) findViewById(R.id.WorkerImg2);
        HelperImg1 = (ImageView) findViewById(R.id.HelperImg1);
        HelperImg2 = (ImageView) findViewById(R.id.HelperImg2);

        rgTempInCharge = (RadioGroup) findViewById(R.id.rgTempInCharge);
        rgWorkerResidance = (RadioGroup) findViewById(R.id.rgWorkerResidance);
        rgHelperResidance = (RadioGroup) findViewById(R.id.rgHelperResidance);

        rbTempInChargeYes = (RadioButton) findViewById(R.id.rbTempInChargeYes);
        rbTempInChargeNo = (RadioButton) findViewById(R.id.rbTempInChargeNo);
        rbWorkerResidanceNative = (RadioButton) findViewById(R.id.rbWorkerResidanceNative);
        rbWorkerResidanceOther = (RadioButton) findViewById(R.id.rbWorkerResidanceOther);
        rbHelperResidanceNative = (RadioButton) findViewById(R.id.rbHelperResidanceNative);
        rbHelperResidanceOther = (RadioButton) findViewById(R.id.rbHelperResidanceOther);

        etWhichAwc = (EditText) findViewById(R.id.etWhichAwc);
        etWorkerName = (EditText) findViewById(R.id.etWorkerName);
        etWorkerDob = (EditText) findViewById(R.id.etWorkerDob);
        etWorkerDOJ = (EditText) findViewById(R.id.etWorkerDOJ);
        etWorkerQualification = (EditText) findViewById(R.id.etWorkerQualification);
        etWorkerTraining = (EditText) findViewById(R.id.etWorkerTraining);
        etWorkerAWCDistance = (EditText) findViewById(R.id.etWorkerAWCDistance);
        etWorkerMobile = (EditText) findViewById(R.id.etWorkerMobile);
        etWorkerAlMobile = (EditText) findViewById(R.id.etWorkerAlMobile);
        etHelperName = (EditText) findViewById(R.id.etHelperName);
        etHelperDob = (EditText) findViewById(R.id.etHelperDob);
        etHelperDoj = (EditText) findViewById(R.id.etHelperDoj);
        etHelperTraining = (EditText) findViewById(R.id.etHelperTraining);
        etHelperDistance = (EditText) findViewById(R.id.etHelperDistance);
        etHelperMobile = (EditText) findViewById(R.id.etHelperMobile);
        etHelperAltMobile = (EditText) findViewById(R.id.etHelperAltMobile);

        tvWorkerPosition = (TextView) findViewById(R.id.tvWorkerPosition);
        tvWorkerTempCharge = (TextView) findViewById(R.id.tvWorkerTempCharge);
        tvWorkerWhichAwc = (TextView) findViewById(R.id.tvWorkerWhichAwc);
        tvWorkerName = (TextView) findViewById(R.id.tvWorkerName);
        tvWorkerReligion = (TextView) findViewById(R.id.tvWorkerReligion);
        tvWorkerCC = (TextView) findViewById(R.id.tvWorkerCC);
        tvWorkerCast = (TextView) findViewById(R.id.tvWorkerCast);
        tvWorkerDOB = (TextView) findViewById(R.id.tvWorkerDOB);
        tvWorkerDOJ = (TextView) findViewById(R.id.tvWorkerDOJ);
        tvWorkerEdu = (TextView) findViewById(R.id.tvWorkerEdu);
        tvWorkerTraining = (TextView) findViewById(R.id.tvWorkerTraining);
        tvWorkerResidance = (TextView) findViewById(R.id.tvWorkerResidance);
        tvWorkerAwcDistance = (TextView) findViewById(R.id.tvWorkerAwcDistance);
        tvWorkerMobileNo = (TextView) findViewById(R.id.tvWorkerMobileNo);
        tvWorkerAltMob = (TextView) findViewById(R.id.tvWorkerAltMob);
        tvWorkerFullPhoto = (TextView) findViewById(R.id.tvWorkerFullPhoto);
        tvWorkerPPPhoto = (TextView) findViewById(R.id.tvWorkerPPPhoto);
        tvHelperDetails = (TextView) findViewById(R.id.tvHelperDetails);
        tvHelperName = (TextView) findViewById(R.id.tvHelperName);
        tvHelperDOB = (TextView) findViewById(R.id.tvHelperDOB);
        tvHelperDOJ = (TextView) findViewById(R.id.tvHelperDOJ);
        tvHelperTraining = (TextView) findViewById(R.id.tvHelperTraining);
        tvHelperResidance = (TextView) findViewById(R.id.tvHelperResidance);
        tvHelperAwcDistance = (TextView) findViewById(R.id.tvHelperAwcDistance);
        tvHelperMob = (TextView) findViewById(R.id.tvHelperMob);
        tvHelperAtlMob = (TextView) findViewById(R.id.tvHelperAtlMob);
        tvHelperFullPhoto = (TextView) findViewById(R.id.tvHelperFullPhoto);
        tvHelperPPPhoto = (TextView) findViewById(R.id.tvHelperPPPhoto);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        tvWorkerSave = (Button) findViewById(R.id.tvWorkerSave);

        tvWorkerPosition.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerPosition, languageId));
        tvWorkerTempCharge.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerTempCharge, languageId));
        tvWorkerWhichAwc.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerWhichAwc, languageId));
        tvWorkerName.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerName, languageId));
        tvWorkerReligion.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerReligion, languageId));
        tvWorkerCC.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerCC, languageId));
        tvWorkerCast.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerCast, languageId));
        tvWorkerDOB.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerDOB, languageId));
        tvWorkerDOJ.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerDOJ, languageId));
        tvWorkerEdu.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerEdu, languageId));
        tvWorkerTraining.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerTraining, languageId));
        tvWorkerResidance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerResidance, languageId));
        tvWorkerAwcDistance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAwcDistance, languageId));
        tvWorkerMobileNo.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerMobileNo, languageId));
        tvWorkerAltMob.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAltMob, languageId));
        tvWorkerFullPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerFullPhoto, languageId));
        tvWorkerPPPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerPPPhoto, languageId));
        tvHelperDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDetails, languageId));
        tvHelperName.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperName, languageId));
        tvHelperDOB.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDOB, languageId));
        tvHelperDOJ.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDOJ, languageId));
        tvHelperTraining.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperTraining, languageId));
        tvHelperResidance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperResidance, languageId));
        tvHelperAwcDistance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAwcDistance, languageId));
        tvHelperMob.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperMob, languageId));
        tvHelperAtlMob.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperAtlMob, languageId));
        tvHelperFullPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperFullPhoto, languageId));
        tvHelperPPPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperPPPhoto, languageId));

        String text = "<a href='http://indevjobs.org'>" + sqlitehelper.LanguageChanges(ConstantValue.LANTPIC, languageId) + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        tvWorkerSave.setText(sqlitehelper.LanguageChanges(ConstantValue.LANsv, languageId));

        String[] wp = {"Vacant", "Temporary in-charge", "Holding Charge"};
        adpWT = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wp);
        adpWT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWorkerPosition.setAdapter(adpWT);

        String[] wr = sqlitehelper.GetManyData("value", "religion", "religion_id > 0");
        adpWR = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wr);
        adpWR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWorkerReligion.setAdapter(adpWR);

        String[] wcc = sqlitehelper.GetManyData("category", "caste_category", "id > 0");
        adpWCC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wcc);
        adpWCC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWorkerCC.setAdapter(adpWCC);

        spnWorkerCC.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int hg = position + 1;
                PopulateCst(hg);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        SetDataIntoViews();
    }

    public void PopulateCst(int hg) {
        String[] wc = sqlitehelper.GetManyData("value", "cast", "cast_category_id = " + hg);
        adpWC = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wc);
        adpWC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnWorkerCast.setAdapter(adpWC);
    }

    public void SetDataIntoViews() {
        String worPosition = sqlitehelper.GetOneData("awc_worker_position", "anganwadi_center", "center_id = " + awc_id);
        if (!worPosition.equals(null)) {
            if (!worPosition.equals("")) {
                spnWorkerPosition.setSelection(adpWT.getPosition(worPosition));
            }
        }

        String worTemInCharge = sqlitehelper.GetOneData("temporary_in_charge", "anganwadi_center", "center_id = " + awc_id);
        if (worTemInCharge.equals("Yes")) {
            rbTempInChargeYes.setChecked(true);
        }
        if (worTemInCharge.equals("No")) {
            rbTempInChargeNo.setChecked(true);
        }

        String worWhichAwc = sqlitehelper.GetOneData("which_awc", "anganwadi_center", "center_id = " + awc_id);
        if (!worWhichAwc.equals(null)) {
            if (!worWhichAwc.equals("")) {
                etWhichAwc.setText(worWhichAwc);
            }
        }

        String worName = sqlitehelper.GetOneData("worker_name", "anganwadi_center", "center_id = " + awc_id);
        if (!worName.equals(null)) {
            if (!worName.equals("")) {
                etWorkerName.setText(worName);
            }
        }

        String worReligion = sqlitehelper.GetOneData("worker_religion", "anganwadi_center", "center_id = " + awc_id);
        if (!worReligion.equals(null)) {
            if (!worReligion.equals("")) {
                spnWorkerReligion.setSelection(adpWR.getPosition(worReligion));
            }
        }

        String worCastCat = sqlitehelper.GetOneData("worker_cc", "anganwadi_center", "center_id = " + awc_id);
        if (!worCastCat.equals(null)) {
            if (!worCastCat.equals("")) {
                spnWorkerCC.setSelection(adpWCC.getPosition(worCastCat));
            }
        }
		/*
		String worCast = sqlitehelper.GetOneData("worker_cast", "anganwadi_center", "center_id = "+awc_id);
		if(!worCast.equals(null))
		{
			if(!worCast.equals(""))
			{
				spnWorkerCast.setSelection(adpWC.getPosition(worCast));
			}
		}
		*/
        String worDOB = sqlitehelper.GetOneData("worker_dob", "anganwadi_center", "center_id = " + awc_id);
        if (!worDOB.equals(null)) {
            if (!worDOB.equals("-")) {
                String das = worDOB.substring(2, 3);
                if (das.equals("-")) {
                    etWorkerDob.setText(worDOB);
                } else {
                    String f_d = worDOB.substring(8, 10) + "-" + worDOB.substring(5, 7) + "-" + worDOB.substring(0, 4);
                    etWorkerDob.setText(f_d);
                }

            }
        }

        String worDOJ = sqlitehelper.GetOneData("worker_doj", "anganwadi_center", "center_id = " + awc_id);
        if (!worDOJ.equals(null)) {
            if (!worDOJ.equals("")) {
                String das1 = worDOJ.substring(2, 3);
                if (das1.equals("-")) {
                    etWorkerDOJ.setText(worDOJ);
                } else {
                    String f_d2 = worDOJ.substring(8, 10) + "-" + worDOJ.substring(5, 7) + "-" + worDOJ.substring(0, 4);
                    etWorkerDOJ.setText(f_d2);
                }
            }
        }

        String worQualification = sqlitehelper.GetOneData("worker_qualification", "anganwadi_center", "center_id = " + awc_id);
        if (!worQualification.equals(null)) {
            if (!worQualification.equals("")) {
                etWorkerQualification.setText(worQualification);
            }
        }

        String worTraining = sqlitehelper.GetOneData("worker_training", "anganwadi_center", "center_id = " + awc_id);
        if (!worTraining.equals(null)) {
            if (!worTraining.equals("")) {
                etWorkerTraining.setText(worTraining);
            }
        }

        String worResidance = sqlitehelper.GetOneData("worker_residance", "anganwadi_center", "center_id = " + awc_id);
        if (worResidance.equals("Native")) {
            rbWorkerResidanceNative.setChecked(true);
        }
        if (worResidance.equals("Other")) {
            rbWorkerResidanceOther.setChecked(true);
        }

        String worAwcDistance = sqlitehelper.GetOneData("worker_distance_awc", "anganwadi_center", "center_id = " + awc_id);
        if (!worAwcDistance.equals(null)) {
            if (!worAwcDistance.equals("")) {
                etWorkerAWCDistance.setText(worAwcDistance);
            }
        }

        String worMobNo = sqlitehelper.GetOneData("worker_mobile", "anganwadi_center", "center_id = " + awc_id);
        if (!worMobNo.equals(null)) {
            if (!worMobNo.equals("")) {
                etWorkerMobile.setText(worMobNo);
            }
        }

        String worAltMobNo = sqlitehelper.GetOneData("worker_alt_mobile", "anganwadi_center", "center_id = " + awc_id);
        if (!worAltMobNo.equals(null)) {
            if (!worAltMobNo.equals("")) {
                etWorkerAlMobile.setText(worAltMobNo);
            }
        }

        String helName = sqlitehelper.GetOneData("helper_name", "anganwadi_center", "center_id = " + awc_id);
        if (!helName.equals(null)) {
            if (!helName.equals("")) {
                etHelperName.setText(helName);
            }
        }
        String helDOB = sqlitehelper.GetOneData("helper_dob", "anganwadi_center", "center_id = " + awc_id);
        if (!helDOB.equals(null)) {
            if (!helDOB.equals("")) {
                String das2 = helDOB.substring(2, 3);
                if (das2.equals("-")) {
                    etHelperDob.setText(helDOB);
                } else {
                    String f_d2 = helDOB.substring(8, 10) + "-" + helDOB.substring(5, 7) + "-" + helDOB.substring(0, 4);
                    etHelperDob.setText(f_d2);
                }

            }
        }

        String helDOJ = sqlitehelper.GetOneData("helper_doj", "anganwadi_center", "center_id = " + awc_id);
        if (!helDOJ.equals(null)) {
            if (!helDOJ.equals("")) {
                String das3 = helDOJ.substring(2, 3);
                if (das3.equals("-")) {
                    etHelperDoj.setText(helDOJ);
                } else {
                    String f_d3 = helDOJ.substring(8, 10) + "-" + helDOJ.substring(5, 7) + "-" + helDOJ.substring(0, 4);
                    etHelperDoj.setText(f_d3);
                }

            }
        }

        String helTraining = sqlitehelper.GetOneData("helper_training", "anganwadi_center", "center_id = " + awc_id);
        if (!helTraining.equals(null)) {
            if (!helTraining.equals("")) {
                etHelperTraining.setText(helTraining);
            }
        }

        String helResidance = sqlitehelper.GetOneData("helper_residance", "anganwadi_center", "center_id = " + awc_id);
        if (helResidance.equals("Native")) {
            rbHelperResidanceNative.setChecked(true);
        }
        if (helResidance.equals("Other")) {
            rbHelperResidanceOther.setChecked(true);
        }

        String helAwcDistance = sqlitehelper.GetOneData("helper_distance_awc", "anganwadi_center", "center_id = " + awc_id);
        if (!helAwcDistance.equals(null)) {
            if (!helAwcDistance.equals("")) {
                etHelperDistance.setText(helAwcDistance);
            }
        }

        String helMobNo = sqlitehelper.GetOneData("helper_mobile", "anganwadi_center", "center_id = " + awc_id);
        if (!helMobNo.equals(null)) {
            if (!helMobNo.equals("")) {
                etHelperMobile.setText(helMobNo);
            }
        }

        String helAltMobNo = sqlitehelper.GetOneData("helper_alt_mobile", "anganwadi_center", "center_id = " + awc_id);
        if (!helAltMobNo.equals(null)) {
            if (!helAltMobNo.equals("")) {
                etHelperAltMobile.setText(helMobNo);
            }
        }
    }

    @SuppressLint("NewApi")
    public void show_callender(View vw) {
        switch (vw.getId()) {
            case R.id.ivWorkerDOB:
                calender_code = 1;
                break;
            case R.id.ivWorkerDOJ:
                calender_code = 2;
                break;
            case R.id.ivHelperDOB:
                calender_code = 3;
                break;
            case R.id.ivHelperDOJ:
                calender_code = 4;
                break;

            default:
                break;
        }

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void click_Worker_Image1(View v) {
        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 1);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }

//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE1);
    }

    public void click_Worker_Image2(View v) {
        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 2);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE2);
    }

    public void click_Helper_Image1(View v) {
        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 3);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }

//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE3);
    }

    public void click_Helper_Image2(View v) {

        Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            intentForCamera(getpic, 4);
        } catch (Exception e) {
            Log.d("exp_result:", e.getMessage().toString());
        }
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),	"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE4);
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

            //for camera intent
            if (requestCode == 1) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap1 = compressImage(file + "");
                    bitmap1 = getResizedBitmap(bitmap1, 350);
                    WorkerImg1.setImageBitmap(bitmap1);

                    image1 = encodeTobase64(bitmap1);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 2) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap2 = compressImage(file + "");
                    bitmap2 = getResizedBitmap(bitmap2, 350);
                    WorkerImg2.setImageBitmap(bitmap2);
                    image2 = encodeTobase64(bitmap2);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 3) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap3 = compressImage(file + "");
                    bitmap3 = getResizedBitmap(bitmap3, 350);
                    HelperImg1.setImageBitmap(bitmap3);
                    image3 = encodeTobase64(bitmap3);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (requestCode == 4) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());

                    bitmap4 = compressImage(file + "");
                    bitmap4 = getResizedBitmap(bitmap4, 350);
                    HelperImg2.setImageBitmap(bitmap4);

                    image4 = encodeTobase64(bitmap4);


                } catch (Exception ex) {
                    ex.printStackTrace();
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

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		try {
//			if (resultCode == RESULT_OK) {
//
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE1) {
//					
//					if (bitmap1 != null && !bitmap1.isRecycled()) {
//						bitmap1.recycle();
//						bitmap1 = null;
//					}
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap1 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap1 = getResizedBitmap(bitmap1, 350);
//
//						WorkerImg1.setImageBitmap(bitmap1);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap1.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image1 = encodeTobase64(bitmap1);	
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE2) {
//					
//					if (bitmap2 != null && !bitmap2.isRecycled()) {
//						bitmap2.recycle();
//						bitmap2 = null;
//					}
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap2 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap2 = getResizedBitmap(bitmap2, 350);
//
//						WorkerImg2.setImageBitmap(bitmap2);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap2.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image2 = encodeTobase64(bitmap2);	
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE3) {
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap3 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap3 = getResizedBitmap(bitmap3, 350);
//
//						HelperImg1.setImageBitmap(bitmap3);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap3.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image3 = encodeTobase64(bitmap3);	
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE4) {
//
//					File f = new File(Environment.getExternalStorageDirectory().toString());
//					for (File temp : f.listFiles()) {
//						if (temp.getName().equals("temp.jpg")) {
//							f = temp;
//							break;
//						}
//					}
//					String selectedImagePath = "";
//					try {
//						BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//						bitmap4 = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
//						//bitmap.compress(Bitmap.CompressFormat.PNG, 15, fOut);
//						bitmap4 = getResizedBitmap(bitmap4, 350);
//
//						HelperImg2.setImageBitmap(bitmap4);
//
//						OutputStream fOut = null;
//
//						try {
//							fOut = new FileOutputStream(f);
//							bitmap4.compress(Bitmap.CompressFormat.PNG, 5, fOut);
//							fOut.flush();
//							fOut.close();
//							image4 = encodeTobase64(bitmap4);	
//
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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

    public void Save_Worker_Details(View v) {

        String rb1 = "", rb2 = "", rb3 = "";
        try {
            rb1 = ((RadioButton) findViewById(rgTempInCharge.getCheckedRadioButtonId())).getText().toString();
            rb2 = ((RadioButton) findViewById(rgWorkerResidance.getCheckedRadioButtonId())).getText().toString();
            rb3 = ((RadioButton) findViewById(rgHelperResidance.getCheckedRadioButtonId())).getText().toString();
        } catch (Exception e) {
        }
        String sdob5 = etWorkerDob.getText().toString();
        String sdoj5 = etWorkerDOJ.getText().toString();
        String hdob5 = etHelperDob.getText().toString();
        String hdoj5 = etHelperDoj.getText().toString();

        String[] myarr = new String[27];
        myarr[0] = spnWorkerPosition.getSelectedItem().toString();
        myarr[1] = rb1;
        myarr[2] = etWhichAwc.getText().toString();
        myarr[3] = etWorkerName.getText().toString();
        myarr[4] = spnWorkerReligion.getSelectedItem().toString();
        myarr[5] = spnWorkerCC.getSelectedItem().toString();
        myarr[6] = spnWorkerCast.getSelectedItem().toString();
        myarr[7] = sdob5.substring(6, 10) + "-" + sdob5.substring(3, 5) + "-" + sdob5.substring(0, 2);
        myarr[8] = sdoj5.substring(6, 10) + "-" + sdoj5.substring(3, 5) + "-" + sdoj5.substring(0, 2);
        myarr[9] = etWorkerQualification.getText().toString();
        myarr[10] = etWorkerTraining.getText().toString();
        myarr[11] = rb2;
        myarr[12] = etWorkerAWCDistance.getText().toString();
        myarr[13] = etWorkerMobile.getText().toString();
        myarr[14] = etWorkerAlMobile.getText().toString();
        myarr[15] = image1;
        myarr[16] = image2;
        myarr[17] = etHelperName.getText().toString();
        myarr[18] = hdob5.substring(6, 10) + "-" + hdob5.substring(3, 5) + "-" + hdob5.substring(0, 2);
        myarr[19] = hdoj5.substring(6, 10) + "-" + hdoj5.substring(3, 5) + "-" + hdoj5.substring(0, 2);
        myarr[20] = etHelperTraining.getText().toString();
        myarr[21] = rb3;
        myarr[22] = etHelperDistance.getText().toString();
        myarr[23] = etHelperMobile.getText().toString();
        myarr[24] = etHelperAltMobile.getText().toString();
        myarr[25] = image3;
        myarr[26] = image4;
        sqlitehelper.SaveWorkerDetails(myarr);
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sdf.format(c.getTime());
            if (calender_code == 1) {
                etWorkerDob.setText(formattedDate);
            }
            if (calender_code == 2) {
                etWorkerDOJ.setText(formattedDate);
            }
            if (calender_code == 3) {
                etHelperDob.setText(formattedDate);
            }
            if (calender_code == 4) {
                etHelperDoj.setText(formattedDate);
            }

        }
    }


}
