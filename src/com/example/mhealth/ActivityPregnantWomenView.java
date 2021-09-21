package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.PregnantWomenMonitor;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.helper.Views;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityPregnantWomenView extends Activity {


    PregnantWomenMonitor pregnantWomenMonitor;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    String strData, strCurrentDate, strWeight, strHB, strSysBp, strDiasBp;
    TextView textName, textAddress, textFatherName, textAdhaarCard, textReligion, textCastCat, textToilet, textWater;
    private ArrayList<PregnantWomenMonitor> list;
    private ArrayList<Views> womenList;
    private WebView wvNutritionHistory;
    private PregnantWomenMonitor womenMonitor;
    private int women_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_pregnant_women_view);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/View/ Pregnant Women View");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        Bundle b = getIntent().getExtras();
        women_id = b.getInt("index");

        womenList = sqliteHelper.getDataOfMotherByWomenId(women_id);
        final Views womenObj = womenList.get(0);

        textName.setText(womenObj.getName());
        textAddress.setText(womenObj.getHhId());
        textFatherName.setText(womenObj.getHusbandName());
        textAdhaarCard.setText(womenObj.getAdhaar_card());
        textReligion.setText(womenObj.getReligion().trim());
        textCastCat.setText(womenObj.getCategory());
        textToilet.setText(womenObj.getToilet_avaibality_source());
        textWater.setText(womenObj.getDrinking_water_source());

        try {
            list = new ArrayList<PregnantWomenMonitor>();
            list.clear();
            wvNutritionHistory.loadUrl("about:blank");
            list = sqliteHelper.getPregnentWomenMonitorDataBy(women_id);

            if (list.get(0) != null) {
                wvNutritionHistory.setVisibility(View.VISIBLE);

            }

            getData(list);


        } catch (Exception e) {
            // TODO Auto-generated catch block

            wvNutritionHistory.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    public String dateFormatConvert(String dateOfBirth) {
        String convertedDate = "";
        try {
            String dateReceivedFromUser = dateOfBirth;
            DateFormat userDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-mm-yyyy");
            Date date = userDateFormat.parse(dateReceivedFromUser);
            convertedDate = dateFormatNeeded.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }


    public void initialization() {

        list = new ArrayList<PregnantWomenMonitor>();
        womenList = new ArrayList<Views>();
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);
        womenMonitor = new PregnantWomenMonitor();
        pregnantWomenMonitor = new PregnantWomenMonitor();


        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);

        String strlanguageId = sph.getString("Language", "1");// getting languageId

        strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,
                strlanguageId);

        strHB = sqliteHelper.LanguageChanges(ConstantValue.LANHB,
                strlanguageId);
        strSysBp = sqliteHelper.LanguageChanges(ConstantValue.LANSystolicBp,
                strlanguageId);
        strDiasBp = sqliteHelper.LanguageChanges(ConstantValue.LANDiastolicBp,
                strlanguageId);
        strCurrentDate = sqliteHelper.LanguageChanges(
                ConstantValue.LANRecordDate, strlanguageId);


        String strAdhaarCard = sqliteHelper.LanguageChanges(
                ConstantValue.LANAdhaarCard, strlanguageId);
        String strReligion = sqliteHelper.LanguageChanges(
                ConstantValue.LANReligion, strlanguageId);
        String strCastCat = sqliteHelper.LanguageChanges(
                ConstantValue.LANCastcat, strlanguageId);
        String strHaveToilet = sqliteHelper.LanguageChanges(
                ConstantValue.LANHaveToilet, strlanguageId);
        String strWater = sqliteHelper.LanguageChanges(
                ConstantValue.LANWater, strlanguageId);
        String strName = sqliteHelper.LanguageChanges(
                ConstantValue.LANName, strlanguageId);
        String strAddress = sqliteHelper.LanguageChanges(
                ConstantValue.LANAddress, strlanguageId);
        String strFamilyDetails = sqliteHelper.LanguageChanges(
                ConstantValue.LANFamilyDetails, strlanguageId);
        String strGrowthHistory = sqliteHelper.LanguageChanges(
                ConstantValue.LANGrowthHistory, strlanguageId);
        String strHusband = sqliteHelper.LanguageChanges(
                ConstantValue.LANHusband, strlanguageId);
        String strPreProfile = sqliteHelper.LanguageChanges(
                ConstantValue.LANPregWomenProfile, strlanguageId);


        textName = (TextView) findViewById(R.id.textName);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textFatherName = (TextView) findViewById(R.id.textFatherName);
        textAdhaarCard = (TextView) findViewById(R.id.textAdhaarCard);
        textReligion = (TextView) findViewById(R.id.textReligion);
        textCastCat = (TextView) findViewById(R.id.textCastCat);
        textToilet = (TextView) findViewById(R.id.textToilet);
        textWater = (TextView) findViewById(R.id.textWater);

        TextView textNameView = (TextView) findViewById(R.id.textNameView);
        TextView textAddressView = (TextView) findViewById(R.id.textAddressView);
        TextView textFatherNameView = (TextView) findViewById(R.id.textFatherNameView);
        TextView textAdhaarCardView = (TextView) findViewById(R.id.textAdhaarCardView);
        TextView textReligionView = (TextView) findViewById(R.id.textReligionView);
        TextView textCastCatView = (TextView) findViewById(R.id.textCastCatView);
        TextView textToiletView = (TextView) findViewById(R.id.textToiletView);
        TextView textWaterView = (TextView) findViewById(R.id.textWaterView);
        TextView txtPregWomanProfile = (TextView) findViewById(R.id.txtPregWomanProfile);
        TextView txtFamilyProfile = (TextView) findViewById(R.id.txtFamilyProfile);
        TextView txtGrowthHistory = (TextView) findViewById(R.id.txtGrowthHistory);

        textNameView.setText(strName + ":");
        textAddressView.setText(strAddress + ":");
        textFatherNameView.setText(strHusband + ":");
        textAdhaarCardView.setText(strAdhaarCard + ":");
        textReligionView.setText(strReligion + ":");
        textCastCatView.setText(strCastCat + ":");
        textToiletView.setText(strHaveToilet + ":");
        textWaterView.setText(strWater + ":");
        txtPregWomanProfile.setText(strPreProfile);
        txtFamilyProfile.setText(strFamilyDetails);
        txtGrowthHistory.setText(strGrowthHistory);

    }

    private void getData(ArrayList<PregnantWomenMonitor> list2) {
        // TODO Auto-generated method stub
        strData = " ";
        for (int i = 0; i < list.size(); i++) {
            womenMonitor = list.get(i);

            womenMonitor.getWeight();
            womenMonitor.getHb();
            womenMonitor.getSysBp();
            womenMonitor.getDiasbp();
            womenMonitor.getCurrent_date();


            strData = strData + "<tr>" + "<td>" + dateFormatConvert(womenMonitor.getCurrent_date()) + "</td>" +
                    "<td>" + womenMonitor.getWeight() + "</td>"
                    + "<td>" + womenMonitor.getHb() + "</td>"
                    + "<td>" + womenMonitor.getSysBp() + "</td>"
                    + "<td>" + womenMonitor.getDiasBp() + "</td>"
                    + "</tr>";


        }
        showWebView(strData);
    }

    private void showWebView(String data2) {
        String[] util = Utility.split(strWeight);
        strWeight = util[0];
        util = Utility.split(strHB);
        strHB = util[0];


        String myvar = "<html>" +
                "" +
                "" +
                "<script type=\"text/javascript\">" +
                "function altRows(id){" +
                "	if(document.getElementsByTagName){  " +
                "		" +
                "		var table = document.getElementById(id);  " +
                "		var rows = table.getElementsByTagName(\"tr\"); " +
                "		 " +
                "		for(i = 0; i < rows.length; i++){          " +
                "			if(i % 2 == 0){" +
                "				rows[i].className = \"evenrowcolor\";" +
                "			}else{" +
                "				rows[i].className = \"oddrowcolor\";" +
                "			}      " +
                "		}" +
                "	}" +
                "}" +
                "window.onload=function(){" +
                "	altRows('alternatecolor');" +
                "}" +
                "</script>" +
                "" +
                "" +
                "" +
                "" +
                "<style type=\"text/css\">" +
                "table.altrowstable {" +
                "	font-family: verdana,arial,sans-serif;" +
                "	font-size:14px;" +
                "	color:#000000;" +
                "	border-width: 1px;" +
                "	border-color: #000000;" +
                "	border-collapse: collapse;" +
                "}" +
                "table.altrowstable th {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "  text-align:center;" +
                "	border-color: #000000;" +
                "}" +
                "table.altrowstable td {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "	border-color: #000000;" +
                "}" +
                ".oddrowcolor{" +
                "	background-color:#000000;" +
                "}" +
                ".evenrowcolor{" +
                "	background-color:#000000;" +
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
                "<th>" + strWeight + "</th>" +
                "<th>" + strHB + "</th>" +
                "<th>" + strSysBp + "</th>" +
                "<th>" + strDiasBp + "</th>" +
                "</tr>" +

                data2 +
                "</table>" +
                "" +
                "" +
                "</body>" +
                "" +
                "</html>";

        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);


    }


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pregnant_women_view, menu);
		return true;
	}*/

}
