package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Views;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityMotherView extends Activity {


    SqliteHelper sqliteHelper;
    //private WebView wvNutritionHistory;
    //private PregnantWomenMonitor womenMonitor;
    //PregnantWomenMonitor pregnantWomenMonitor;
    SharedPrefHelper sph;
    String strData, strCurrentDate;
    TextView textName, textAddress, textNumberOfChild, textFatherName, textAdhaarCard, textReligion, textCastCat, textToilet, textWater;
    //private ArrayList<PregnantWomenMonitor> list;
    private ArrayList<Views> motherList;
    private int mother_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_mother_view);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/View/ Mother view");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        Bundle b = getIntent().getExtras();
        mother_id = b.getInt("index");

        motherList = sqliteHelper.getDataOfMotherByWomenId(mother_id);
        final Views motherObj = motherList.get(0);


        textName.setText(motherObj.getName());
        textAddress.setText(motherObj.getHhId());
        textNumberOfChild.setText(motherObj.getChildNumber());
        textFatherName.setText(motherObj.getHusbandName());
        textAdhaarCard.setText(motherObj.getAdhaar_card());
        textReligion.setText(motherObj.getReligion().trim());
        textCastCat.setText(motherObj.getCategory());
        textToilet.setText(motherObj.getToilet_avaibality_source());
        textWater.setText(motherObj.getDrinking_water_source());
		/*
		try {
			list = new ArrayList<PregnantWomenMonitor>();
			list.clear();
			wvNutritionHistory.loadUrl("about:blank");
			list = sqliteHelper.getPregnentWomenMonitorDataBy(women_id);
			 
			if(list.get(0)!=null)
				
			{
				wvNutritionHistory.setVisibility(View.VISIBLE);
				
			}
			
			getData(list);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			wvNutritionHistory.setVisibility(View.VISIBLE);
			e.printStackTrace();
		}   */
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

        //	list = new ArrayList<PregnantWomenMonitor>();
        motherList = new ArrayList<Views>();
        //	wvNutritionHistory = (WebView) findViewById(R.id.wvNutritionHistory);
        //	womenMonitor = new PregnantWomenMonitor();
        //	pregnantWomenMonitor = new PregnantWomenMonitor();


        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);

        String strlanguageId = sph.getString("Language", "1");// getting languageId

        //strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,	strlanguageId);

        //strHB = sqliteHelper.LanguageChanges(ConstantValue.LANHB,	strlanguageId);
        //strSysBp = sqliteHelper.LanguageChanges(ConstantValue.LANSystolicBp, strlanguageId);
        //strDiasBp = sqliteHelper.LanguageChanges(ConstantValue.LANDiastolicBp, strlanguageId);
        strCurrentDate = sqliteHelper.LanguageChanges(
                ConstantValue.LANRecordDate, strlanguageId);


        String strAdhaarCard = sqliteHelper.LanguageChanges(ConstantValue.LANAdhaarCard, strlanguageId);
        String strReligion = sqliteHelper.LanguageChanges(ConstantValue.LANReligion, strlanguageId);
        String strCastCat = sqliteHelper.LanguageChanges(ConstantValue.LANCastcat, strlanguageId);
        String strHaveToilet = sqliteHelper.LanguageChanges(ConstantValue.LANHaveToilet, strlanguageId);
        String strWater = sqliteHelper.LanguageChanges(ConstantValue.LANWater, strlanguageId);
        String strName = sqliteHelper.LanguageChanges(ConstantValue.LANName, strlanguageId);
        String strAddress = sqliteHelper.LanguageChanges(ConstantValue.LANAddress, strlanguageId);
        String strFamilyDetails = sqliteHelper.LanguageChanges(ConstantValue.LANFamilyDetails, strlanguageId);
        //String strGrowthHistory = sqliteHelper.LanguageChanges(ConstantValue.LANGrowthHistory, strlanguageId);
        String strHusband = sqliteHelper.LanguageChanges(ConstantValue.LANHusband, strlanguageId);
        String strMotherProfile = sqliteHelper.LanguageChanges(ConstantValue.LANMotherProfile, strlanguageId);


        textName = (TextView) findViewById(R.id.textName);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textNumberOfChild = (TextView) findViewById(R.id.txtNumberOfChild);
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
        TextView txtMotherProfile = (TextView) findViewById(R.id.txtMotherProfile);
        TextView txtFamilyProfile = (TextView) findViewById(R.id.txtFamilyProfile);
        //TextView txtGrowthHistory = (TextView) findViewById(R.id.txtGrowthHistory);

        textNameView.setText(strName + ":");
        textAddressView.setText(strAddress + ":");
        textFatherNameView.setText(strHusband + ":");
        textAdhaarCardView.setText(strAdhaarCard + ":");
        textReligionView.setText(strReligion + ":");
        textCastCatView.setText(strCastCat + ":");
        textToiletView.setText(strHaveToilet + ":");
        textWaterView.setText(strWater + ":");
        txtMotherProfile.setText(strMotherProfile);
        txtFamilyProfile.setText(strFamilyDetails);
        //txtGrowthHistory.setText(strGrowthHistory);

    }


}
