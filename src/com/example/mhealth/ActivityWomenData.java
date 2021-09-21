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

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivityWomenData extends Activity implements OnClickListener {

    public static final String TxtBackToMainMenu = "txtBackToMainMenu";
    TextView txtFooter;
    Button btnUpdate, btnPreviewExit;
    String strFooter, strUpdate;
    private ArrayList<String> strTitle = new ArrayList<String>();
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sph;
    private int women_id;
    private ArrayList<PregnantWomen> list;
    private String strHhId;
    private String strPreWomenName;
    private String strHusbandName;
    private String strLmpDate;
    private String strEdd;
    private String strHeight;
    private String strWeight;
    private String strHb;
    private String strsysBp;
    private String strdiasBp;
    private String BackToMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);

        WebView ww = (WebView) findViewById(R.id.webview);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnPreviewExit = (Button) findViewById(R.id.btnPreviewExit);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        list = new ArrayList<PregnantWomen>();

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        Bundle b = getIntent().getExtras();
        women_id = b.getInt("index");
        Log.e("index", String.valueOf(women_id));


        String strlanguageId = sph.getString("Language", "1");// getting languageId

        strHhId = sqliteHelper.LanguageChanges(ConstantValue.LANHouseNo,
                strlanguageId);
        strPreWomenName = sqliteHelper.LanguageChanges(
                ConstantValue.LANPregWomen, strlanguageId);

        strLmpDate = sqliteHelper.LanguageChanges(ConstantValue.LANLMP,
                strlanguageId);
        strEdd = sqliteHelper.LanguageChanges(ConstantValue.LANEDD,
                strlanguageId);
        strHeight = sqliteHelper.LanguageChanges(ConstantValue.LANHeight,
                strlanguageId);
        strWeight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,
                strlanguageId);
        strHb = sqliteHelper
                .LanguageChanges(ConstantValue.LANHB, strlanguageId);
		/*strBp = sqliteHelper.LanguageChanges(ConstantValue.LANBloddPressure,
				strlanguageId);*/
        strsysBp = sqliteHelper.LanguageChanges(ConstantValue.LANSystolicBpreg,
                strlanguageId);
        strdiasBp = sqliteHelper.LanguageChanges(ConstantValue.LANDiastolicBpreg,
                strlanguageId);
        BackToMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANBactToMM, strlanguageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        strUpdate = sqliteHelper.LanguageChanges(ConstantValue.LANUpdate, strlanguageId);

        btnPreviewExit.setText(BackToMainMenu);
        txtFooter.setText(strFooter);
        btnUpdate.setText(strUpdate);
        strTitle.add(strHhId);
        strTitle.add(strPreWomenName);
        strTitle.add(strLmpDate);
        strTitle.add(strEdd);
        strTitle.add(strHeight);
        strTitle.add(strWeight);
        strTitle.add(strHb);
        strTitle.add(strsysBp);
        strTitle.add(strdiasBp);


        //list = sqliteHelper.getDataOfMotherByWomenId(women_id);
        final PregnantWomen pregnantWomenObject = list.get(0);


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
                + pregnantWomenObject.getHhId() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(1) + "</td>" + "<td>"
                + pregnantWomenObject.getPreWomenName() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(2) + "</td>" + "<td>"
                + pregnantWomenObject.getLmp_date() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(3) + "</td>" + "<td>"
                + pregnantWomenObject.getEdd() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(4) + "</td>" + "<td>"
                + pregnantWomenObject.getHeight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(5) + "</td>" + "<td>"
                + pregnantWomenObject.getWeight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(6) + "</td>" + "<td>"
                + pregnantWomenObject.getHb() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(7) + "</td>" + "<td>"
                + pregnantWomenObject.getSysBp() + "</td>" + "</tr>"
                + "<td>" + strTitle.get(8) + "</td>" + "<td>"
                + pregnantWomenObject.getDiasBp() + "</td>" + "</tr>" +
                "</table>" +
                "" +
                "" +
                "</body>" +
                "" +
                "</html>";


        ww.loadData(myvar, "text/html; charset=UTF-8", null);


//						Button btnExit=(Button)findViewById(R.id.btnPreviewExit);
//						btnExit.setText(BackToMainMenu);
//						btnExit.setOnClickListener(this);

        RelativeLayout LayoutUpdate = (RelativeLayout) findViewById(R.id.layoutUpdate);
        Button Update = (Button) findViewById(R.id.btnUpdate);
//						Update.setText(BackToMainMenu);
        Update.setOnClickListener(this);

        //Button btnExit = (Button) findViewById(R.id.btnPreviewExit);
        btnPreviewExit.setText(BackToMainMenu);
        btnPreviewExit.setOnClickListener(this);
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


}
