package com.example.mhealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.Parent;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivityParentData extends Activity implements OnClickListener {

    public static final String TxtBackToMainMenu = "txtBackToMainMenu";
    Button btnPreviewExit, btnUpdate;
    String strPreviewExit, strUpdate, StrUpdateGPSData, strUpdated, strFooter;
    SharedPrefHelper sph;
    ServerHelper serverhelper;
    private int family_id;
    private int parent_id;
    private ArrayList<String> strTitle = new ArrayList<String>();
    private ArrayList<Parent> list;
    private SqliteHelper sqliteHelper;
    private Parent parent;
    private ProgressDialog progressDialog;
    private String latitude, longitude, house_no;

    //	Parent parentObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        WebView ww = (WebView) findViewById(R.id.webview);
        TextView txtFooter = (TextView) findViewById(R.id.txtFooter);
        btnPreviewExit = (Button) findViewById(R.id.btnPreviewExit);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        sqliteHelper = new SqliteHelper(this);
        parent = new Parent();
        list = new ArrayList<Parent>();
        serverhelper = new ServerHelper(this);
        sph = new SharedPrefHelper(this);
        Bundle b = getIntent().getExtras();
        family_id = b.getInt("index");
        Log.e("index", String.valueOf(family_id));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(StrUpdateGPSData);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.rotate_loading_360));


        String languageId = sph.getString("Language", "1");// getting languageId

        String strHouseNo = sqliteHelper.LanguageChanges(ConstantValue.LANHouseNo, languageId);
        String strHeadOfHH = sqliteHelper.LanguageChanges(ConstantValue.LANHHH, languageId);
        String strAadharHH = sqliteHelper.LanguageChanges(ConstantValue.LANAadhaarHH, languageId);
        String strGender = sqliteHelper.LanguageChanges(ConstantValue.LANGender, languageId);

        String strReligion = sqliteHelper.LanguageChanges(ConstantValue.LANReligion, languageId);
        String strCastcat = sqliteHelper.LanguageChanges(ConstantValue.LANCastcat, languageId);
        String strToilet = sqliteHelper.LanguageChanges(ConstantValue.LANToilet, languageId);
        String strLatitude = sqliteHelper.LanguageChanges(ConstantValue.LANLat, languageId);
        String strLongitude = sqliteHelper.LanguageChanges(ConstantValue.LANLong, languageId);
        String strDrinkingWater = sqliteHelper.LanguageChanges(ConstantValue.LANDrinkingWater, languageId);
        strPreviewExit = sqliteHelper.LanguageChanges(ConstantValue.LANBactToMM, languageId);
        strUpdate = sqliteHelper.LanguageChanges(ConstantValue.LANUpdate, languageId);
        StrUpdateGPSData = sqliteHelper.LanguageChanges(ConstantValue.LANUpdateGPSData, languageId);
        strUpdated = sqliteHelper.LanguageChanges(ConstantValue.LANUpdated, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        StrUpdateGPSData = sqliteHelper.LanguageChanges(ConstantValue.LANUpdateGPSData, languageId);

        btnPreviewExit.setText(" " + strPreviewExit + " ");
        btnUpdate.setText(strUpdate);
        txtFooter.setText(strFooter);

        strTitle.add(strHouseNo);
        strTitle.add(strHeadOfHH);
        strTitle.add(strAadharHH);
        strTitle.add(strGender);
        strTitle.add(strReligion);
        strTitle.add(strCastcat);
        strTitle.add(strToilet);
        strTitle.add(strDrinkingWater);
        strTitle.add(strLatitude);
        strTitle.add(strLongitude);

        /*
         * // list = sqliteHelper.getDataOfParentId(parent_id);
         */
        list = sqliteHelper.getDataOfFamilyId(family_id);

        final Parent parentObject = list.get(0);


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
                + "	border-color: #a9c6c9;" + "}" + "table.altrowstable td {"
                + "	border-width: 1px;" + "	padding: 8px;"
                + "	border-style: solid;" + "	border-color: #a9c6c9;" + "}"
                + ".oddrowcolor{" + "	background-color:#d4e3e5;" + "}"
                + ".evenrowcolor{" + "	background-color:#c3dde0;" + "}"
                + "</style>" + "" + "" + "" + "" + "<body>" + "" + "" + "" + ""
                + "<table class=\"altrowstable\" id=\"alternatecolor\">" + ""
                + "" + "" + "" + "" + "" + "<tr>" + "<td>"
                + strTitle.get(0)
                + "</td>"
                + "<td>"
                + parentObject.getHouseNo()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(1)
                + "</td>"
                + "<td>"
                + parentObject.getHeadofHH()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(2)
                + "</td>"
                + "<td>"
                + parentObject.getAadharCardHH()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(3)
                + "</td>"
                + "<td>"
                + parentObject.getGender()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(4)
                + "</td>"
                + "<td>"
                + parentObject.getTxtReligion()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(5)
                + "</td>"
                + "<td>"
                + parentObject.getCaste()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(6)
                + "</td>"
                + "<td>"
                + parentObject.getTxthave_toilet()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(7)
                + "</td>"
                + "<td>"
                + parentObject.getTxthave_water() + "</td>" + "</tr>" + "<tr>" +
                "<tr>"
                + "<td>"
                + strTitle.get(8)
                + "</td>"
                + "<td>"
                + parentObject.getLatitude()
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td>"
                + strTitle.get(9)
                + "</td>"
                + "<td>"
                + parentObject.getLongitude()
                + "</td>"
                + "</tr>"
                +

                "</table>" + "" + "" + "</body>" + "" + "</html>";

        try {
            parent.setHouseNo(list.get(0).getHouseNo());
            parent.setHeadofHH(list.get(0).getHeadofHH());
            parent.setAadharCardHH(list.get(0).getAadharCardHH());
            parent.setIntcastecat(list.get(0).getIntcastecat());
            parent.setCast_id(list.get(0).getCast_id());
            parent.setIntGender(valueGender());
            parent.setReligion(valueReligion());
            parent.setHas_toilet(valueToilet());
            parent.setHave_water(valueWater());
            parent.setLatitude(GlobalVars.lattitude);
            parent.setLongitude(GlobalVars.longitude);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        latitude = GlobalVars.lattitude;
        longitude = GlobalVars.longitude;
        house_no = parentObject.getHouseNo();
        ww.loadData(myvar, "text/html; charset=UTF-8", null);

        RelativeLayout LayoutUpdate = (RelativeLayout) findViewById(R.id.layoutUpdate);
        Button Update = (Button) findViewById(R.id.btnUpdate);
//		Update.setText(BackToMainMenu);
        Update.setOnClickListener(this);

        //Button btnExit = (Button) findViewById(R.id.btnPreviewExit);
        btnPreviewExit.setText(strPreviewExit);
        btnPreviewExit.setOnClickListener(this);
        if (GlobalVars.EFposition == 0) {
            LayoutUpdate.setVisibility(View.GONE);
            GlobalVars.EFposition = 0;
        } else {

        }
    }

//	private int valueGender(String gender) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

    private int valueWater() {
        // TODO Auto-generated method stub
        int Value = 0;
        if ((list.get(0).getTxthave_water()).equalsIgnoreCase("Piped Water")) {
            Value = 0;
        } else if ((list.get(0).getTxthave_water()).contains("Closed Water Source(Tube")) {
            Value = 1;
        } else if ((list.get(0).getTxthave_water()).contains("Open Water Source(Open")) {
            Value = 2;
        }
        return Value;
    }

    private int valueToilet() {
        // TODO Auto-generated method stub
        int Value = 0;
        if ((list.get(0).getTxthave_toilet()).equalsIgnoreCase("Toilet with Wall/cover")) {
            Value = 0;
        } else if ((list.get(0).getTxthave_toilet()).equalsIgnoreCase("Toilet without a Wall/cover")) {
            Value = 1;
        } else if ((list.get(0).getTxthave_toilet()).equalsIgnoreCase("No Toilet")) {
            Value = 2;
        }
        return Value;
    }

    private int valueReligion() {
        // TODO Auto-generated method stub
        int Value = 0;
        if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Hinduism")) {
            Value = 0;
        } else if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Buddhism")) {
            Value = 1;
        } else if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Islam")) {
            Value = 2;
        } else if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Christianity")) {
            Value = 3;
        } else if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Sikhism")) {
            Value = 4;
        } else if ((list.get(0).getTxtReligion()).equalsIgnoreCase("Jainism")) {
            Value = 5;
        }
        return Value;
    }

    private int valueGender() {
        // TODO Auto-generated method stub
        int Value = 0;
        if ((list.get(0).getGender()).equalsIgnoreCase("Male")) {
            Value = 0;
        } else if ((list.get(0).getGender()).equalsIgnoreCase("Female")) {
            Value = 1;
        } else {
            Value = 2;
        }
        return Value;
    }

    @Override
    public void onBackPressed() { // TODO Auto-generated method

        // super.onBackPressed();

        Intent intent = new Intent(this, Activity_parentList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.btnPreviewExit:
                Intent intent = new Intent(this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            case R.id.btnUpdate:
//			progressDialog.setMessage(StrUpdateGPSData);
//			progressDialog.show();
                //sqliteHelper.EligibleFamilyRegistration(parent,2);

                sqliteHelper.UpdateFamilyGPS(latitude, longitude, house_no, 2);
                //SendEFamilyDataOnServer();

                finish();
                break;
        }
    }

    private void SendEFamilyDataOnServer() {
        Log.e("json", "");

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {


                serverhelper.EFamilyDataUpdate(latitude, longitude, house_no);

                return "1";
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result != null) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), strUpdated, Toast.LENGTH_LONG).show();
                    finish();

                }
                progressDialog.dismiss();
            }
        }.execute((String) null);

    }
}
