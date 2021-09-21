package com.example.mhealth;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.helper.Views;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ActivityChildView extends Activity {

    Date d;
    WebView wvNutritionHistory;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    ChildNutrition childnutrition;
    String strData, strlanguageId, strWeight, strHeight, strMuac, strCurrentDate, birthDate, strChildName, strDob, strGender, strAge, strMother, strAdhaarCard, strReligion, strCastCat, strHaveToilet, strWater, strChildProfile, strFamilyDetails, strNutritionHistory;
    TextView textChildName, txtAgeVsWeight, textGender, textDob, textAge, textMotherName, textReligion, textCastCat, textToilet, textWater, textAdhaarCard;
    private SimpleDateFormat mSimpleDateFormat;
    private ArrayList<ChildNutrition> list;
    private int child_id;
    private ChildNutrition childMonitor;
    private ArrayList<Views> childList;

    private static String calAge(String strStartDate, String strEndDate) {

        String totalAge = "";
        Log.e("Start date", strStartDate);
        Log.e("End date", strEndDate);
        SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        try {
            startDate = form.parse(strStartDate);
            endDate = form.parse(strEndDate);
            Log.e("Start Parse", String.valueOf(startDate));
            Log.e("End Parse", String.valueOf(endDate));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.setTime(startDate);
        int startMonth = c.get(Calendar.MONTH) + 1;
        int startYear = c.get(Calendar.YEAR);

        Log.e("Start Month", String.valueOf(startMonth));
        Log.e("Start Year", String.valueOf(startYear));
        c.setTime(endDate);
        int endMonth = c.get(Calendar.MONTH) + 1;
        int endYear = c.get(Calendar.YEAR);
        Log.e("End Month", String.valueOf(endMonth));
        Log.e("End Year", String.valueOf(endYear));
        int diffYear = endYear - startYear;
        int diffMonth = endMonth - startMonth;
        Log.e("Diff Month", String.valueOf(diffMonth));
        Log.e("Diff Year", String.valueOf(diffYear));
        int totalDiff = diffYear + diffMonth;
        Log.e("totalDiff", String.valueOf(totalDiff));

        if (diffYear == 0) {
            totalAge = diffMonth + " Month";
        } else {
            totalAge = diffYear + " Year, " + diffMonth + " Month";
        }
        return totalAge;

    }

    private static int startCalendar(int year) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_child_view);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/View/Child_List/ Child View");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();


        childList = sqliteHelper.getDataByChildId(child_id);
        final Views childObj = childList.get(0);


        birthDate = dateFormatConvert(childObj.getDate_of_birth());


        textChildName.setText(childObj.getName());
        textGender.setText(childObj.getGender());
        textDob.setText(birthDate);

        String DATE_FORMAT = "dd-mm-yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String strDate = sdf.format(date);

        Calendar calendar = Calendar.getInstance();
        String sDate = sdf.format(calendar.getTime());


        //Period dateDiff = calc(birthDate,strDate);
        //mPeriodFormat = new PeriodFormatterBuilder().appendYears().appendSuffix(" year ").appendMonths().appendSuffix(" month ").appendDays().appendSuffix(" day ").printZeroNever().toFormatter();

        //int intAge = calc(birthDate,sDate);
        //textAge.setText(intAge);
        //int age = calc(birthDate,strDate)+"";
        //Log.e("Age",String.valueOf(age));
        textAge.setText(calAge(birthDate, strDate));

        textMotherName.setText(childObj.getMother_name());
        textAdhaarCard.setText(childObj.getAdhaar_card());
        textReligion.setText(childObj.getReligion().trim());
        //textCastCat.setText(childObj.getCategory());
        textCastCat.setText("");
        textToilet.setText(childObj.getToilet_avaibality_source());
        textWater.setText(childObj.getDrinking_water_source());

        try {
            list = new ArrayList<ChildNutrition>();
            list.clear();
            //wvNutritionHistory.loadUrl("about:blank");
            list = sqliteHelper
                    .getchildNutrationMonitorDataBy(child_id);
            Log.e("list", String.valueOf(list.size()));
            if (list.get(0) != null) {

                getData(list);
            } else {

                getDataBlank();
            }


        } catch (Exception e) {

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

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        childnutrition = new ChildNutrition();
        childList = new ArrayList<Views>();
        wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);

        Bundle b = getIntent().getExtras();
        child_id = b.getInt("index");
        Log.e("index", String.valueOf(child_id));
        strlanguageId = sph.getString("Language", "1");// getting languageId

        strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,
                strlanguageId);
        strHeight = sqliteHelper.LanguageChanges(ConstantValue.LANHeight,
                strlanguageId);
        strMuac = sqliteHelper.LanguageChanges(ConstantValue.LANMuac,
                strlanguageId);
        strCurrentDate = sqliteHelper.LanguageChanges(
                ConstantValue.LANRecordDate, strlanguageId);
        strChildName = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildName, strlanguageId);

        strGender = sqliteHelper.LanguageChanges(
                ConstantValue.LANGender, strlanguageId);
        strDob = sqliteHelper.LanguageChanges(
                ConstantValue.LANDateOfBirth, strlanguageId);
        strAge = sqliteHelper.LanguageChanges(
                ConstantValue.LANAge, strlanguageId);
        strMother = sqliteHelper.LanguageChanges(
                ConstantValue.LANMotherName, strlanguageId);
        strAdhaarCard = sqliteHelper.LanguageChanges(
                ConstantValue.LANAdhaarCard, strlanguageId);
        strReligion = sqliteHelper.LanguageChanges(
                ConstantValue.LANReligion, strlanguageId);
        strCastCat = sqliteHelper.LanguageChanges(
                ConstantValue.LANCastcat, strlanguageId);
        strHaveToilet = sqliteHelper.LanguageChanges(
                ConstantValue.LANHaveToilet, strlanguageId);
        strWater = sqliteHelper.LanguageChanges(
                ConstantValue.LANWater, strlanguageId);
        strChildProfile = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildPro, strlanguageId);
        strFamilyDetails = sqliteHelper.LanguageChanges(
                ConstantValue.LANFamilyDetails, strlanguageId);
        strNutritionHistory = sqliteHelper.LanguageChanges(
                ConstantValue.LANNutHistory, strlanguageId);

        //txtAgeVsWeight =(TextView) findViewById(R.id.txtAgeVsWeight);
        textChildName = (TextView) findViewById(R.id.textChildName);
        textGender = (TextView) findViewById(R.id.textGender);
        textDob = (TextView) findViewById(R.id.textDob);
        textAge = (TextView) findViewById(R.id.textAge);

        textMotherName = (TextView) findViewById(R.id.textMotherName);
        textAdhaarCard = (TextView) findViewById(R.id.textAdhaarCard);
        textReligion = (TextView) findViewById(R.id.textReligion);
        textCastCat = (TextView) findViewById(R.id.textCastCat);
        textToilet = (TextView) findViewById(R.id.textToilet);
        textWater = (TextView) findViewById(R.id.textWater);
        TextView txtChildProfile = (TextView) findViewById(R.id.txtChildProfile);
        TextView txtFamilyProfile = (TextView) findViewById(R.id.txtFamilyProfile);
        TextView txtNutHistory = (TextView) findViewById(R.id.txtNutHistory);

        TextView textChildNameView = (TextView) findViewById(R.id.textChildNameView);
        TextView textGenderView = (TextView) findViewById(R.id.textGenderView);
        TextView textDobView = (TextView) findViewById(R.id.textDobView);
        TextView textAgeView = (TextView) findViewById(R.id.textAgeView);

        TextView textMotherNameView = (TextView) findViewById(R.id.textMotherNameView);
        TextView textAdhaarCardView = (TextView) findViewById(R.id.textAdhaarCardView);
        TextView textReligionView = (TextView) findViewById(R.id.textReligionView);
        TextView textCastCatView = (TextView) findViewById(R.id.textCastCatView);
        TextView textToiletView = (TextView) findViewById(R.id.textToiletView);
        TextView textWaterView = (TextView) findViewById(R.id.textWaterView);

        textChildNameView.setText(strChildName + ":");
        textGenderView.setText(strGender + ":");
        textDobView.setText(strDob + ":");
        textAgeView.setText(strAge + ":");

        textMotherNameView.setText(strMother + ":");
        textAdhaarCardView.setText(strAdhaarCard + ":");
        textReligionView.setText(strReligion + ":");
        textCastCatView.setText(strCastCat + ":");
        textToiletView.setText(strHaveToilet + ":");
        textWaterView.setText(strWater + ":");
        txtChildProfile.setText(strChildProfile);
        txtFamilyProfile.setText(strFamilyDetails);
        txtNutHistory.setText(strNutritionHistory);
    }

    protected void getData(ArrayList<ChildNutrition> list2) {
        // TODO Auto-generated method stub
        strData = "";
        Log.e("data", strData);
        for (int i = 0; i < list2.size(); i++) {
            childMonitor = list2.get(i);

            childMonitor.getHeight();
            childMonitor.getWeight();
            childMonitor.getMuac();
            childMonitor.getDate_of_monitoring();

            strData = strData + "<tr>" + "<td>"
                    + dateFormatConvert(childMonitor.getDate_of_monitoring()) + "</td>" + "<td>"
                    + calAge(birthDate, dateFormatConvert(childMonitor.getDate_of_monitoring())) + "</td>" + "<td>"
                    + childMonitor.getWeight() + "</td>" + "<td>"
                    + childMonitor.getHeight() + "</td>" + "<td>"
                    + childMonitor.getMuac() + "</td>" + "</tr>";

        }
        showWebView(strData);

    }

    protected void getDataBlank() {
        // TODO Auto-generated method stub
        strData = "";
        //Log.e("data", strData);


        strData = strData + "<tr>" + "<td>"
                + "" + "</td>" + "<td>"
                + "" + "</td>" + "<td>"
                + "" + "</td>" + "<td>"
                + "" + "</td>" + "<td>"
                + "" + "</td>" + "</tr>";


        showWebView(strData);

    }


    private void showWebView(String data2) {
        // TODO Auto-generated method stub
        String[] util = Utility.split(strHeight);
        strHeight = util[0];
        util = Utility.split(strWeight);
        strWeight = util[0];
        util = Utility.split(strMuac);
        strMuac = util[0];

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
                + "	font-size:14px;" + "	color:#333333;"
                + "	border-width: 1px;" + "	border-color: #000000;"
                + "	border-collapse: collapse;" + "}"
                + "table.altrowstable th {" + "	border-width: 1px;"
                + "	padding: 8px;" + "	border-style: solid;"
                + "  text-align:center;" + "	border-color: #000000;" + "}"
                + "table.altrowstable td {" + "	border-width: 1px;"
                + "	padding: 8px;" + "	border-style: solid;"
                + "	border-color: #000000;" + "}" + ".oddrowcolor{"
                + "	background-color:#d4e3e5;" + "}" + ".evenrowcolor{"
                + "	background-color:#c3dde0;" + "}" + "</style>" + "" + ""
                + "" + "" + "<body>" + "" + "" + "" + ""
                + "<table class=\"altrowstable\" id=\"alternatecolor\">" + ""
                + "" + "" + "" + "" + "" + "<tr>" + "<th>" + strCurrentDate
                + "</th>" + "<th>" + strAge
                + "</th>" + "<th>" + strWeight + "</th>" + "<th>" + strHeight
                + "</th>" + "<th>" + strMuac + "</th>" + "</tr>" +

                data2 + "</table>" + "" + "" + "</body>" + "" + "</html>";

        wvNutritionHistory.loadData(myvar, "text/html; charset=UTF-8", null);
    }

    public void weightClick(View v) {

        Intent i = new Intent(this, ShowWebViewChart.class);
        startActivity(i);

    }

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_child_view, menu);
		return true;
	}*/


}