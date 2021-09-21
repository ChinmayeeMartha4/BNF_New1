package com.example.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.InputStream;

public class Activity_View_Worker extends Activity {
    EditText etWVAwcPosition, etWVInCharge, etWVWhichAwc, etWVWorkerName, etWVReligion, etWVCC, etWVCast, etWVDOB,
            etWVDOJ, etWVEQ, etWVTraining, etWVResidance, etWVAwcDistance, etWVMobile, etWVAltMobile, etHVName, etHVDOB,
            etHVDOJ, etHVTraining, etHVResidance, etHVAwcDistance, etHVMobile, etHVAltMobileNo;
    ImageView wvImg1, wvImg2, hvImg1, hvImg2;

    TextView tvWVDetails, tvWVPosition, tvWVTempCharge, tvWVWhichAwc, tvWVName, tvWVReligion, tvWVCC, tvWVCast, tvWVDOB,
            tvWVDOJ, tvWVEdu, tvWVTraining, tvWVResidance, tvWVAwcDistance, tvWVMobile, tvWVAltMobile, tvWVFullImage, tvWVPPPhoto,
            tvHVDetails, tvHVName, tvHVDOB, tvHVDOJ, tvHVTraining, tvHVResidance, tvHVAwcDistance, tvHVMobile, tvHVAMobile,
            tvHVFullImage, tvHVPPPhoto, txtFooter;

    Button btnWVBack;

    SqliteHelper sqlitehelper;
    SharedPrefHelper sph;
    int awc_id;

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
        setContentView(R.layout.activity_view_worker);

        sqlitehelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        awc_id = sph.getInt("user_id", 0);
        String languageId = sph.getString("Language", "1");

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> View Worker Details");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        etWVAwcPosition = (EditText) findViewById(R.id.etWVAwcPosition);
        etWVInCharge = (EditText) findViewById(R.id.etWVInCharge);
        etWVWhichAwc = (EditText) findViewById(R.id.etWVWhichAwc);
        etWVWorkerName = (EditText) findViewById(R.id.etWVWorkerName);
        etWVReligion = (EditText) findViewById(R.id.etWVReligion);
        etWVCC = (EditText) findViewById(R.id.etWVCC);
        etWVCast = (EditText) findViewById(R.id.etWVCast);
        etWVDOB = (EditText) findViewById(R.id.etWVDOB);
        etWVDOJ = (EditText) findViewById(R.id.etWVDOJ);
        etWVEQ = (EditText) findViewById(R.id.etWVEQ);
        etWVTraining = (EditText) findViewById(R.id.etWVTraining);
        etWVResidance = (EditText) findViewById(R.id.etWVResidance);
        etWVAwcDistance = (EditText) findViewById(R.id.etWVAwcDistance);
        etWVMobile = (EditText) findViewById(R.id.etWVMobile);
        etWVAltMobile = (EditText) findViewById(R.id.etWVAltMobile);
        etHVName = (EditText) findViewById(R.id.etHVName);
        etHVDOB = (EditText) findViewById(R.id.etHVDOB);
        etHVDOJ = (EditText) findViewById(R.id.etHVDOJ);
        etHVTraining = (EditText) findViewById(R.id.etHVTraining);
        etHVResidance = (EditText) findViewById(R.id.etHVResidance);
        etHVAwcDistance = (EditText) findViewById(R.id.etHVAwcDistance);
        etHVMobile = (EditText) findViewById(R.id.etHVMobile);
        etHVAltMobileNo = (EditText) findViewById(R.id.etHVAltMobileNo);

        wvImg1 = (ImageView) findViewById(R.id.wvImg1);
        wvImg2 = (ImageView) findViewById(R.id.wvImg2);
        hvImg1 = (ImageView) findViewById(R.id.hvImg1);
        hvImg2 = (ImageView) findViewById(R.id.hvImg2);


        tvWVDetails = (TextView) findViewById(R.id.tvWVDetails);
        tvWVPosition = (TextView) findViewById(R.id.tvWVPosition);
        tvWVTempCharge = (TextView) findViewById(R.id.tvWVTempCharge);
        tvWVWhichAwc = (TextView) findViewById(R.id.tvWVWhichAwc);
        tvWVName = (TextView) findViewById(R.id.tvWVName);
        tvWVReligion = (TextView) findViewById(R.id.tvWVReligion);
        tvWVCC = (TextView) findViewById(R.id.tvWVCC);
        tvWVCast = (TextView) findViewById(R.id.tvWVCast);
        tvWVDOB = (TextView) findViewById(R.id.tvWVDOB);
        tvWVDOJ = (TextView) findViewById(R.id.tvWVDOJ);
        tvWVEdu = (TextView) findViewById(R.id.tvWVEdu);
        tvWVTraining = (TextView) findViewById(R.id.tvWVTraining);
        tvWVResidance = (TextView) findViewById(R.id.tvWVResidance);
        tvWVAwcDistance = (TextView) findViewById(R.id.tvWVAwcDistance);
        tvWVMobile = (TextView) findViewById(R.id.tvWVMobile);
        tvWVAltMobile = (TextView) findViewById(R.id.tvWVAltMobile);
        tvWVFullImage = (TextView) findViewById(R.id.tvWVFullImage);
        tvWVPPPhoto = (TextView) findViewById(R.id.tvWVPPPhoto);
        tvHVDetails = (TextView) findViewById(R.id.tvHVDetails);
        tvHVName = (TextView) findViewById(R.id.tvHVName);
        tvHVDOB = (TextView) findViewById(R.id.tvHVDOB);
        tvHVDOJ = (TextView) findViewById(R.id.tvHVDOJ);
        tvHVTraining = (TextView) findViewById(R.id.tvHVTraining);
        tvHVResidance = (TextView) findViewById(R.id.tvHVResidance);
        tvHVAwcDistance = (TextView) findViewById(R.id.tvHVAwcDistance);
        tvHVMobile = (TextView) findViewById(R.id.tvHVMobile);
        tvHVAMobile = (TextView) findViewById(R.id.tvHVAMobile);
        tvHVFullImage = (TextView) findViewById(R.id.tvHVFullImage);
        tvHVPPPhoto = (TextView) findViewById(R.id.tvHVPPPhoto);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        btnWVBack = (Button) findViewById(R.id.btnWVBack);

        tvWVDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWVDetails, languageId));
        tvWVPosition.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerPosition, languageId));
        tvWVTempCharge.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerTempCharge, languageId));
        tvWVWhichAwc.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerWhichAwc, languageId));
        tvWVName.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerName, languageId));
        tvWVReligion.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerReligion, languageId));
        tvWVCC.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerCC, languageId));
        tvWVCast.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerCast, languageId));
        tvWVDOB.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerDOB, languageId));
        tvWVDOJ.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerDOJ, languageId));
        tvWVEdu.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerEdu, languageId));
        tvWVTraining.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerTraining, languageId));
        tvWVResidance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerResidance, languageId));
        tvWVAwcDistance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAwcDistance, languageId));
        tvWVMobile.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerMobileNo, languageId));
        tvWVAltMobile.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAltMob, languageId));
        tvWVFullImage.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerFullPhoto, languageId));
        tvWVPPPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerPPPhoto, languageId));
        tvHVDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDetails, languageId));
        tvHVName.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperName, languageId));
        tvHVDOB.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDOB, languageId));
        tvHVDOJ.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperDOJ, languageId));
        tvHVTraining.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperTraining, languageId));
        tvHVResidance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperResidance, languageId));
        tvHVAwcDistance.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWorkerAwcDistance, languageId));
        tvHVMobile.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperMob, languageId));
        tvHVAMobile.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperAtlMob, languageId));
        tvHVFullImage.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperFullPhoto, languageId));
        tvHVPPPhoto.setText(sqlitehelper.LanguageChanges(ConstantValue.LANHelperPPPhoto, languageId));
        btnWVBack.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWVBack, languageId));

        String text = "<a href='http://indevjobs.org'>" + sqlitehelper.LanguageChanges(ConstantValue.LANTPIC, languageId) + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());


        getAndSetData();
    }

    public void getAndSetData() {
        String strAwc_position = sqlitehelper.GetOneData("awc_worker_position", "anganwadi_center", "center_id = " + awc_id);
        String temporary_in_charge = sqlitehelper.GetOneData("temporary_in_charge", "anganwadi_center", "center_id = " + awc_id);
        String which_awc = sqlitehelper.GetOneData("which_awc", "anganwadi_center", "center_id = " + awc_id);
        String worker_name = sqlitehelper.GetOneData("worker_name", "anganwadi_center", "center_id = " + awc_id);
        String worker_religion = sqlitehelper.GetOneData("worker_religion", "anganwadi_center", "center_id = " + awc_id);
        String worker_cc = sqlitehelper.GetOneData("worker_cc", "anganwadi_center", "center_id = " + awc_id);
        String worker_cast = sqlitehelper.GetOneData("worker_cast", "anganwadi_center", "center_id = " + awc_id);
        String worker_dob = sqlitehelper.GetOneData("worker_dob", "anganwadi_center", "center_id = " + awc_id);
        String worker_doj = sqlitehelper.GetOneData("worker_doj", "anganwadi_center", "center_id = " + awc_id);
        String worker_qualification = sqlitehelper.GetOneData("worker_qualification", "anganwadi_center", "center_id = " + awc_id);
        String worker_training = sqlitehelper.GetOneData("worker_training", "anganwadi_center", "center_id = " + awc_id);
        String worker_residance = sqlitehelper.GetOneData("worker_residance", "anganwadi_center", "center_id = " + awc_id);
        String worker_distance_awc = sqlitehelper.GetOneData("worker_distance_awc", "anganwadi_center", "center_id = " + awc_id);
        String worker_mobile = sqlitehelper.GetOneData("worker_mobile", "anganwadi_center", "center_id = " + awc_id);
        String worker_alt_mobile = sqlitehelper.GetOneData("worker_alt_mobile", "anganwadi_center", "center_id = " + awc_id);
        String worker_img1 = sqlitehelper.GetOneData("worker_img1", "anganwadi_center", "center_id = " + awc_id);
        String worker_img2 = sqlitehelper.GetOneData("worker_img2", "anganwadi_center", "center_id = " + awc_id);
        String helper_name = sqlitehelper.GetOneData("helper_name", "anganwadi_center", "center_id = " + awc_id);
        String helper_dob = sqlitehelper.GetOneData("helper_dob", "anganwadi_center", "center_id = " + awc_id);
        String helper_doj = sqlitehelper.GetOneData("helper_doj", "anganwadi_center", "center_id = " + awc_id);
        String helper_qualification = sqlitehelper.GetOneData("helper_qualification", "anganwadi_center", "center_id = " + awc_id);
        String helper_training = sqlitehelper.GetOneData("helper_training", "anganwadi_center", "center_id = " + awc_id);
        String helper_residance = sqlitehelper.GetOneData("helper_residance", "anganwadi_center", "center_id = " + awc_id);
        String helper_distance_awc = sqlitehelper.GetOneData("helper_distance_awc", "anganwadi_center", "center_id = " + awc_id);
        String helper_mobile = sqlitehelper.GetOneData("helper_mobile", "anganwadi_center", "center_id = " + awc_id);
        String helper_alt_mobile = sqlitehelper.GetOneData("helper_alt_mobile", "anganwadi_center", "center_id = " + awc_id);
        String helper_img1 = sqlitehelper.GetOneData("helper_img1", "anganwadi_center", "center_id = " + awc_id);
        String helper_img2 = sqlitehelper.GetOneData("helper_img2", "anganwadi_center", "center_id = " + awc_id);


        if (strAwc_position != null) {
            etWVAwcPosition.setText(strAwc_position);
            Log.e(".........................", strAwc_position);
        }
        if (temporary_in_charge != null) {
            etWVInCharge.setText(temporary_in_charge);
        }
        if (which_awc != null) {
            etWVWhichAwc.setText(which_awc);
        }
        if (worker_name != null) {
            etWVWorkerName.setText(worker_name);
        }
        if (worker_religion != null) {
            etWVReligion.setText(worker_religion);
        }
        if (worker_cc != null) {
            etWVCC.setText(worker_cc);
        }
        if (worker_cast != null) {
            etWVCast.setText(worker_cast);
        }
        if (worker_dob != null) {
            etWVDOB.setText(worker_dob);
        }
        if (worker_doj != null) {
            etWVDOJ.setText(worker_doj);
        }
        if (worker_qualification != null) {
            etWVEQ.setText(worker_qualification);
        }
        if (worker_training != null) {
            etWVTraining.setText(worker_training);
        }
        if (worker_residance != null) {
            etWVResidance.setText(worker_residance);
        }
        if (worker_distance_awc != null) {
            etWVAwcDistance.setText(worker_distance_awc);
        }
        if (worker_mobile != null) {
            etWVMobile.setText(worker_mobile);
        }
        if (worker_alt_mobile != null) {
            etWVAltMobile.setText(worker_alt_mobile);
        }
        if (helper_name != null) {
            etHVName.setText(helper_name);
        }
        if (helper_dob != null) {
            etHVDOB.setText(helper_dob);
        }
        if (helper_doj != null) {
            etHVDOJ.setText(helper_doj);
        }
        if (helper_training != null) {
            etHVTraining.setText(helper_training);
        }
        if (helper_residance != null) {
            etHVResidance.setText(helper_residance);
        }
        if (helper_distance_awc != null) {
            etHVAwcDistance.setText(helper_distance_awc);
        }
        if (helper_mobile != null) {
            etHVMobile.setText(helper_mobile);
        }
        if (helper_alt_mobile != null) {
            etHVAltMobileNo.setText(helper_alt_mobile);
        }

        new DownloadImage1().execute("", "", "");
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
    }

    public void View_My_Awc_Details_Back(View v) {
        Intent i = new Intent(this, awc_main_menu.class);
        startActivity(i);
        finish();
    }

    private class DownloadImage1 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_sevika_full_img" + awc_id + ".png";

            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                wvImg1.setImageBitmap(result);
                new DownloadImage2().execute("", "", "");
            }
        }
    }

    private class DownloadImage2 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_sevika_pp_img" + awc_id + ".png";

            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                wvImg2.setImageBitmap(result);
                new DownloadImage3().execute("", "", "");
            }
        }
    }

    private class DownloadImage3 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_helper_full_img" + awc_id + ".png";

            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                hvImg1.setImageBitmap(result);
                new DownloadImage4().execute("", "", "");
            }
        }
    }

    private class DownloadImage4 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_helper_pp_img" + awc_id + ".png";

            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                hvImg2.setImageBitmap(result);
            }
        }
    }
}
