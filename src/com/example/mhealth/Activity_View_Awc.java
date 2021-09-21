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
import android.view.View;
import android.webkit.WebView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_View_Awc extends Activity {
    EditText etAVAwcType, etAVPukkaKuchha, etAVOwnedRented, etAVWaterType, etAVWaterSafety, etAVToilet, etAVWaterAvailability, etAVHWF, etAVAOTALA, etAVAOE;
    ImageView imgAV1, imgAV2, imgAV3, imgAV4;
    WebView wvAwcViewChild, wvAwcViewAG, wvAwcViewLW;

    TextView tvAVAwcHead, tvAVAwcType, tvAVAwcSB, tvAVAwcBT, tvAVAwcWaterType, tvAVAwcWaterSafety,
            tvAVAwcToilet, tvAVAwcWA, tvAVAwcHWF, tvAVAwcTL, tvAVAwcAE, tvAVAwcOutsideImg, tvAVAwcKitchenImg,
            tvAVAwcToiletImg, tvAVAwcHandWashImg, tvChildrenDetails, tvAGirlDetails, tvPregWomenDetails, txtFooter;
    Button btnBack;


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
        setContentView(R.layout.activity_view_awc);

        sqlitehelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> View Anganwadi Details");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        tvAVAwcHead = (TextView) findViewById(R.id.tvAVAwcHead);
        tvAVAwcType = (TextView) findViewById(R.id.tvAVAwcType);
        tvAVAwcSB = (TextView) findViewById(R.id.tvAVAwcSB);
        tvAVAwcBT = (TextView) findViewById(R.id.tvAVAwcBT);
        tvAVAwcWaterType = (TextView) findViewById(R.id.tvAVAwcWaterType);
        tvAVAwcWaterSafety = (TextView) findViewById(R.id.tvAVAwcWaterSafety);
        tvAVAwcToilet = (TextView) findViewById(R.id.tvAVAwcToilet);
        tvAVAwcWA = (TextView) findViewById(R.id.tvAVAwcWA);
        tvAVAwcHWF = (TextView) findViewById(R.id.tvAVAwcHWF);
        tvAVAwcTL = (TextView) findViewById(R.id.tvAVAwcTL);
        tvAVAwcAE = (TextView) findViewById(R.id.tvAVAwcAE);
        tvAVAwcOutsideImg = (TextView) findViewById(R.id.tvAVAwcOutsideImg);
        tvAVAwcKitchenImg = (TextView) findViewById(R.id.tvAVAwcKitchenImg);
        tvAVAwcToiletImg = (TextView) findViewById(R.id.tvAVAwcToiletImg);
        tvAVAwcHandWashImg = (TextView) findViewById(R.id.tvAVAwcHandWashImg);
        tvChildrenDetails = (TextView) findViewById(R.id.tvChildrenDetails);
        tvAGirlDetails = (TextView) findViewById(R.id.tvAGirlDetails);
        tvPregWomenDetails = (TextView) findViewById(R.id.tvPregWomenDetails);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        btnBack = (Button) findViewById(R.id.btnBack);

        tvAVAwcHead.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHead, languageId));
        tvAVAwcType.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcType, languageId));
        tvAVAwcSB.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcBuildingStatus, languageId));
        tvAVAwcBT.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcBuildingType, languageId));
        tvAVAwcWaterType.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterType, languageId));
        tvAVAwcWaterSafety.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterSafety, languageId));
        tvAVAwcToilet.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcToilet, languageId));
        tvAVAwcWA.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcWaterAvailability, languageId));
        tvAVAwcHWF.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHwf, languageId));
        tvAVAwcTL.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcTL, languageId));
        tvAVAwcAE.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcEquipment, languageId));
        tvAVAwcOutsideImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcOutsideImg, languageId));
        tvAVAwcKitchenImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcKitchenImg, languageId));
        tvAVAwcToiletImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcToiletImg, languageId));
        tvAVAwcHandWashImg.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcHandWashImg, languageId));
        tvChildrenDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcChildDetails, languageId));
        tvAGirlDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcAdolDetails, languageId));
        tvPregWomenDetails.setText(sqlitehelper.LanguageChanges(ConstantValue.LANAwcPregWomen, languageId));
        btnBack.setText(sqlitehelper.LanguageChanges(ConstantValue.LANWVBack, languageId));

        String text = "<a href='http://indevjobs.org'>" + sqlitehelper.LanguageChanges(ConstantValue.LANTPIC, languageId) + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        etAVAwcType = (EditText) findViewById(R.id.etAVAwcType);
        etAVPukkaKuchha = (EditText) findViewById(R.id.etAVPukkaKuchha);
        etAVOwnedRented = (EditText) findViewById(R.id.etAVOwnedRented);
        etAVWaterType = (EditText) findViewById(R.id.etAVWaterType);
        etAVWaterSafety = (EditText) findViewById(R.id.etAVWaterSafety);
        etAVToilet = (EditText) findViewById(R.id.etAVToilet);
        etAVWaterAvailability = (EditText) findViewById(R.id.etAVWaterAvailability);
        etAVHWF = (EditText) findViewById(R.id.etAVHWF);
        etAVAOTALA = (EditText) findViewById(R.id.etAVAOTALA);
        etAVAOE = (EditText) findViewById(R.id.etAVAOE);

        imgAV1 = (ImageView) findViewById(R.id.imgAV1);
        imgAV2 = (ImageView) findViewById(R.id.imgAV2);
        imgAV3 = (ImageView) findViewById(R.id.imgAV3);
        imgAV4 = (ImageView) findViewById(R.id.imgAV4);

        wvAwcViewChild = (WebView) findViewById(R.id.wvAwcViewChild);
        wvAwcViewAG = (WebView) findViewById(R.id.wvAwcViewAG);
        wvAwcViewLW = (WebView) findViewById(R.id.wvAwcViewLW);

        SetDataIntoViews();
        new DownloadImage1().execute("", "", "");
    }

    public void SetDataIntoViews() {
        awc_id = sph.getInt("user_id", 0);
        String strAwcType = sqlitehelper.GetOneData("main_mini", "anganwadi_center", "center_id = " + awc_id);
        String strkuchha_pukka = sqlitehelper.GetOneData("kuchha_pukka", "anganwadi_center", "center_id = " + awc_id);
        String strOwnedRented = sqlitehelper.GetOneData("owned_rented", "anganwadi_center", "center_id = " + awc_id);
        String strWaterSource = sqlitehelper.GetOneData("water_source", "anganwadi_center", "center_id = " + awc_id);
        String strWaterSafety = sqlitehelper.GetOneData("water_safety", "anganwadi_center", "center_id = " + awc_id);
        String strToilet = sqlitehelper.GetOneData("toilet", "anganwadi_center", "center_id = " + awc_id);
        String strWater = sqlitehelper.GetOneData("water", "anganwadi_center", "center_id = " + awc_id);
        String strHWF = sqlitehelper.GetOneData("hand_washing_facility", "anganwadi_center", "center_id = " + awc_id);
        String strlt = sqlitehelper.GetOneData("learning_teaching", "anganwadi_center", "center_id = " + awc_id);
        String strEquipment = sqlitehelper.GetOneData("equipment", "anganwadi_center", "center_id = " + awc_id);
        String strAwcImg = sqlitehelper.GetOneData("outside_awc_img", "anganwadi_center", "center_id = " + awc_id);
        String strKitchenImg = sqlitehelper.GetOneData("kitchen_img", "anganwadi_center", "center_id = " + awc_id);
        String strToiletImg = sqlitehelper.GetOneData("toilet_img", "anganwadi_center", "center_id = " + awc_id);
        String strHWImg = sqlitehelper.GetOneData("hand_washing_img", "anganwadi_center", "center_id = " + awc_id);

        if (strAwcType != null) {
            etAVAwcType.setText(strAwcType);
        }
        if (strkuchha_pukka != null) {
            etAVPukkaKuchha.setText(strkuchha_pukka);
        }
        if (strOwnedRented != null) {
            etAVOwnedRented.setText(strOwnedRented);
        }
        if (strWaterSource != null) {
            etAVWaterType.setText(strWaterSource);
        }
        if (strWaterSafety != null) {
            etAVWaterSafety.setText(strWaterSafety);
        }
        if (strToilet != null) {
            etAVToilet.setText(strToilet);
        }
        if (strWater != null) {
            etAVWaterAvailability.setText(strWater);
        }
        if (strHWF != null) {
            etAVHWF.setText(strHWF);
        }
        if (strlt != null) {
            etAVAOTALA.setText(strlt);
        }
        if (strEquipment != null) {
            etAVAOE.setText(strEquipment);
        }

        wvAwcViewChild.loadUrl("about:blank");
        wvAwcViewAG.loadUrl("about:blank");
        wvAwcViewLW.loadUrl("about:blank");

        getData();
    }

    protected void getData() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String currDate = df.format(c.getTime());
        int i1 = Integer.parseInt(currDate.substring(0, 4));
        String oneYear = Integer.toString(i1 - 1) + currDate.substring(4, 10);
        String threeYear = Integer.toString(i1 - 3) + currDate.substring(4, 10);
        String fiveYear = Integer.toString(i1 - 5) + currDate.substring(4, 10);
        String elevenYear = Integer.toString(i1 - 11) + currDate.substring(4, 10);
        String eighteenYear = Integer.toString(i1 - 18) + currDate.substring(4, 10);

        String f_0_1 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth > '" + oneYear + "' and gender = 2 and anganwadi_center_id = " + awc_id);
        String m_0_1 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth > '" + oneYear + "' and gender = 1 and anganwadi_center_id = " + awc_id);
        String o_0_1 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth > '" + oneYear + "' and gender = 3 and anganwadi_center_id = " + awc_id);
        String t_0_1 = Integer.toString(Integer.parseInt(f_0_1) + Integer.parseInt(m_0_1) + Integer.parseInt(o_0_1));
        String f_1_3 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + oneYear + "' and date_of_birth > '" + threeYear + "' and gender = 2 and anganwadi_center_id = " + awc_id);
        String m_1_3 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + oneYear + "' and date_of_birth > '" + threeYear + "' and gender = 1 and anganwadi_center_id = " + awc_id);
        String o_1_3 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + oneYear + "' and date_of_birth > '" + threeYear + "' and gender = 3 and anganwadi_center_id = " + awc_id);
        String t_1_3 = Integer.toString(Integer.parseInt(f_1_3) + Integer.parseInt(m_1_3) + Integer.parseInt(o_1_3));
        String f_3_5 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + threeYear + "' and date_of_birth > '" + fiveYear + "' and gender = 2 and anganwadi_center_id = " + awc_id);
        String m_3_5 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + threeYear + "' and date_of_birth > '" + fiveYear + "' and gender = 1 and anganwadi_center_id = " + awc_id);
        String o_3_5 = sqlitehelper.GetOneData("count(*)", "child", "date_of_birth < '" + threeYear + "' and date_of_birth > '" + fiveYear + "' and gender = 3 and anganwadi_center_id = " + awc_id);
        String t_3_5 = Integer.toString(Integer.parseInt(f_3_5) + Integer.parseInt(m_3_5) + Integer.parseInt(o_3_5));
        String f_t = Integer.toString(Integer.parseInt(f_0_1) + Integer.parseInt(f_1_3) + Integer.parseInt(f_3_5));
        String m_t = Integer.toString(Integer.parseInt(m_0_1) + Integer.parseInt(m_1_3) + Integer.parseInt(m_3_5));
        String o_t = Integer.toString(Integer.parseInt(o_0_1) + Integer.parseInt(o_1_3) + Integer.parseInt(o_3_5));
        String t_t = Integer.toString(Integer.parseInt(t_0_1) + Integer.parseInt(t_1_3) + Integer.parseInt(t_3_5));
        String strData = "<tr>" + "<td>" + "0-1 Year" + "</td>" + "<td>" + f_0_1 + "</td>" + "<td>" + m_0_1 + "</td>" + "<td>" + o_0_1 + "</td>" + "<td>" + t_0_1 + "</td>" + "</tr>" +
                "<tr>" + "<td>" + "1-3 Year" + "</td>" + "<td>" + f_1_3 + "</td>" + "<td>" + m_1_3 + "</td>" + "<td>" + o_1_3 + "</td>" + "<td>" + t_1_3 + "</td>" + "</tr>" +
                "<tr>" + "<td>" + "3-5 Year" + "</td>" + "<td>" + f_3_5 + "</td>" + "<td>" + m_3_5 + "</td>" + "<td>" + o_3_5 + "</td>" + "<td>" + t_3_5 + "</td>" + "</tr>" +
                "<tr>" + "<td>" + "Total" + "</td>" + "<td>" + f_t + "</td>" + "<td>" + m_t + "</td>" + "<td>" + o_t + "</td>" + "<td>" + t_t + "</td>" + "</tr>";
        showWebViewChild(strData);

        String a_data = sqlitehelper.GetOneData("count(*)", "adolescent", "date_of_birth < '" + elevenYear + "' and date_of_birth > '" + eighteenYear + "' and anganwadi_center_id = " + awc_id);
        showWebViewAG(a_data);

        String p_data = sqlitehelper.GetOneData("count(*)", "pregnant_women", "edd > '" + currDate + "' and flag = '' and anganwadi_center_id = " + awc_id);
        showWebViewLW(p_data, "0");
    }

    private void showWebViewChild(String data2) {

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
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + "Age in years"
                + "</th>" + "<th>" + "Female" + "</th>" + "<th>" + "Male"
                + "</th>" + "<th>" + "Other" + "</th>" + "<th>" + "Total" + "</th>" + "</tr>" +
                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";

        wvAwcViewChild.loadData(myvar, "text/html; charset=UTF-8", null);
    }

    private void showWebViewAG(String data2) {

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
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + "Adolescent Girls (11-18 Years)"
                + "</th>" + "<th>" + data2 + "</th>" + "</tr>" + "</table>" + "" + "" + "</body>" + "" + "</html>";

        wvAwcViewAG.loadData(myvar, "text/html; charset=UTF-8", null);
    }

    private void showWebViewLW(String data2, String data1) {

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
                + "" + "" + "" + "" + "" + "<tr>" + "<td>" + "Pregnant Women"
                + "</td>" + "<td>" + data2 + "</td>" + "</tr>" +
                "<tr>" + "<td>" + "Lactating Women"
                + "</td>" + "<td>" + data1 + "</td>" + "</tr>" + "</table>" + "" + "" + "</body>" + "" + "</html>";

        wvAwcViewLW.loadData(myvar, "text/html; charset=UTF-8", null);
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

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_img" + awc_id + ".png";

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
                imgAV1.setImageBitmap(result);
                new DownloadImage2().execute("", "", "");
            }
        }
    }

    private class DownloadImage2 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_kitchen_img" + awc_id + ".png";

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
                imgAV2.setImageBitmap(result);
                new DownloadImage3().execute("", "", "");
            }
        }
    }

    private class DownloadImage3 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_toilet_img" + awc_id + ".png";

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
                imgAV3.setImageBitmap(result);
                new DownloadImage4().execute("", "", "");
            }
        }
    }

    private class DownloadImage4 extends AsyncTask<String, Void, Bitmap> {
        private boolean running = true;

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = "http://jsw-np.in/anganwadi/app_v1.2/awc_details/awc_hand_washing_img" + awc_id + ".png";

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
                imgAV4.setImageBitmap(result);
            }
        }
    }
}
