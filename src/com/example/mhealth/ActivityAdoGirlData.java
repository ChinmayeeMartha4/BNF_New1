package com.example.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mhealth.helper.Adolescent;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivityAdoGirlData extends Activity implements OnClickListener {

    TextView txtFooter;
    Button btnPreviewExit, btnUpdate;
    private ArrayList<String> strTitle = new ArrayList<String>();
    private ArrayList<Adolescent> list;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sph;
    private int adoGirl_id;
    private String strlanguageId;
    private String strHhId;
    private String strNameOfTheGirl;
    private String strGirlFatherName;
    private String strDateOfBirth;
    private String strGirlHeight;
    private String strGirlWeight;
    private String strGirlHb;
    private String strGirlOsp;
    private String stryear;
    private String strmonth, strFooter;
    private String strGirlChronic;
    private String BackToMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);

        WebView ww = (WebView) findViewById(R.id.webview);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        btnPreviewExit = (Button) findViewById(R.id.btnPreviewExit);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        list = new ArrayList<Adolescent>();
        sqliteHelper = new SqliteHelper(this);

        sph = new SharedPrefHelper(this);
        Bundle b = getIntent().getExtras();
        adoGirl_id = b.getInt("index");
        Log.e("index", String.valueOf(adoGirl_id));


        getLanguage();


        //list = sqliteHelper.getDataByADOGirlId(adoGirl_id);
        final Adolescent adoGirlObject = list.get(0);


        String strOSP = strTitle.get(8) + " " + ":" + " " + adoGirlObject.getospYear() + " " + "|" + " " + strTitle.get(9) + " " + ":" + " " + adoGirlObject.getospMonth();

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
                "	font-size:16px;" +
                "	color:#333333;" +
                "	border-width: 1px;" +
                "	border-color: #a9c6c9;" +
                "	border-collapse: collapse;" +
                "}" +
                "table.altrowstable th {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "	border-color: #a9c6c9;" +
                "}" +
                "table.altrowstable td {" +
                "	border-width: 1px;" +
                "	padding: 8px;" +
                "	border-style: solid;" +
                "	border-color: #a9c6c9;" +
                "}" +
                ".oddrowcolor{" +
                "	background-color:#d4e3e5;" +
                "}" +
                ".evenrowcolor{" +
                "	background-color:#c3dde0;" +
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
                + "<td>" + strTitle.get(0) + "</td>" + "<td>"
                + adoGirlObject.getHhId() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(1) + "</td>" + "<td>"
                + adoGirlObject.getNameOfTheGirl() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(2) + "</td>" + "<td>"
                + adoGirlObject.getGirlFatherName() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(3) + "</td>" + "<td>"
                + adoGirlObject.getDateOfBirth() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(4) + "</td>" + "<td>"
                + adoGirlObject.getGirlHeight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(5) + "</td>" + "<td>"
                + adoGirlObject.getGirlWeight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(6) + "</td>" + "<td>"
                + adoGirlObject.getGirlHb() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(7) + "</td>" + "<td>"
                + strOSP + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(10) + "</td>" + "<td>"
                + adoGirlObject.getChronicDisease() + "</td>" + "</tr>" +
                "</table>" +
                "" +
                "" +
                "</body>" +
                "" +
                "</html>";


        ww.loadData(myvar, "text/html; charset=UTF-8", null);
//		Button btnExit=(Button)findViewById(R.id.btnPreviewExit);
//		btnExit.setText(BackToMainMenu);
//		btnExit.setOnClickListener(this);

        RelativeLayout LayoutUpdate = (RelativeLayout) findViewById(R.id.layoutUpdate);
        Button Update = (Button) findViewById(R.id.btnUpdate);
//		Update.setText(BackToMainMenu);
        Update.setOnClickListener(this);

        Button btnExit = (Button) findViewById(R.id.btnPreviewExit);
        btnExit.setText(BackToMainMenu);
        btnExit.setOnClickListener(this);
        if (GlobalVars.EFposition == 0) {
            LayoutUpdate.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getLanguage() {
        // TODO Auto-generated method stub
        strlanguageId = sph.getString("Language", "1");// getting languageId

        strHhId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId, strlanguageId);
        strNameOfTheGirl = sqliteHelper.LanguageChanges(ConstantValue.LANGirlName, strlanguageId);
        strGirlFatherName = sqliteHelper.LanguageChanges(ConstantValue.LANFatherName, strlanguageId);
        strDateOfBirth = sqliteHelper.LanguageChanges(ConstantValue.LANDateOfBirth, strlanguageId);
        strGirlHeight = sqliteHelper.LanguageChanges(ConstantValue.LANHeight, strlanguageId);
        strGirlWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight, strlanguageId);
        strGirlHb = sqliteHelper.LanguageChanges(ConstantValue.LANHB, strlanguageId);
        strGirlOsp = sqliteHelper.LanguageChanges(ConstantValue.LANOSP, strlanguageId);
        stryear = sqliteHelper.LanguageChanges(ConstantValue.LANYear, strlanguageId);
        strmonth = sqliteHelper.LanguageChanges(ConstantValue.LANMonth, strlanguageId);
        strGirlChronic = sqliteHelper.LanguageChanges(ConstantValue.LANChronic, strlanguageId);
        BackToMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANBactToMM, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        String strUpdate = sqliteHelper.LanguageChanges(ConstantValue.LANUpdate, strlanguageId);

        txtFooter.setText(strFooter);
        btnPreviewExit.setText(BackToMainMenu);
        btnUpdate.setText(strUpdate);

        strTitle.add(strHhId);
        strTitle.add(strNameOfTheGirl);
        strTitle.add(strGirlFatherName);
        strTitle.add(strDateOfBirth);
        strTitle.add(strGirlHeight);
        strTitle.add(strGirlWeight);
        strTitle.add(strGirlHb);
        strTitle.add(strGirlOsp);
        strTitle.add(stryear);
        strTitle.add(strmonth);
        strTitle.add(strGirlChronic);
    }

}
