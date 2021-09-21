package com.example.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivityChildData extends Activity implements OnClickListener {

    TextView txtFooter;
    String strFooter, strBackToMainMenu, strUpdate;
    Button btnExit, btnUpdate;
    //	private Child child;
    private ArrayList<Child> list;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sph;
    private int child_id;
    private ArrayList<String> strTitle = new ArrayList<String>();
    private String childregistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);
        WebView ww = (WebView) findViewById(R.id.webview);
        btnExit = (Button) findViewById(R.id.btnPreviewExit);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        //	child = new Child();
        list = new ArrayList<Child>();

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        Bundle b = getIntent().getExtras();
        child_id = b.getInt("index");
        //	Log.e("index", String.valueOf(child_id));

        String languageId = sph.getString("Language", "1");// getting languageId
        childregistration = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildReg, languageId);
        String strHHId = sqliteHelper.LanguageChanges(ConstantValue.LANHHId,
                languageId);
        String dob = sqliteHelper.LanguageChanges(ConstantValue.LANDateOfBirth,
                languageId);
        String bweight = sqliteHelper.LanguageChanges(ConstantValue.LANWeight,
                languageId);
        String border = sqliteHelper.LanguageChanges(
                ConstantValue.LANBirthOrder, languageId);
        String selectparent = sqliteHelper.LanguageChanges(
                ConstantValue.LANSelectParent, languageId);
        String Disablity = sqliteHelper.LanguageChanges(
                ConstantValue.LANDisability, languageId);
        String Photograph = sqliteHelper.LanguageChanges(
                ConstantValue.LANPhoto, languageId);
        String gps = sqliteHelper.LanguageChanges(ConstantValue.LANGPS,
                languageId);
        String BirthHeight = sqliteHelper.LanguageChanges(
                ConstantValue.LANBirthHeight, languageId);
        String FullNameOfChild = sqliteHelper.LanguageChanges(
                ConstantValue.LANChildName, languageId);
        String Gender = sqliteHelper.LanguageChanges(ConstantValue.LANGender,
                languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                languageId);
        strUpdate = sqliteHelper.LanguageChanges(ConstantValue.LANUpdate,
                languageId);
        strBackToMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANBactToMM, languageId);

        txtFooter.setText(strFooter);
        btnExit.setText(strBackToMainMenu);
        btnUpdate.setText(strUpdate);


        strTitle.add(strHHId);
        strTitle.add(FullNameOfChild);
        strTitle.add(selectparent);
        strTitle.add(dob);
        strTitle.add(Gender);
        strTitle.add(bweight);
        strTitle.add(BirthHeight);
        strTitle.add(Disablity);
        strTitle.add(border);

        //list = sqliteHelper.getDataByChildId(child_id);

        final Child childObject = list.get(0);


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
                + childObject.getHHID() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(1) + "</td>" + "<td>"
                + childObject.getChild_name() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(2) + "</td>" + "<td>"
                + childObject.getParent_name() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(3) + "</td>" + "<td>"
                + childObject.getDate_of_birth() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(4) + "</td>" + "<td>"
                + childObject.getGender() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(5) + "</td>" + "<td>"
                + childObject.getChild_weight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(6) + "</td>" + "<td>"
                + childObject.getHeight() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(7) + "</td>" + "<td>"
                + childObject.getDisablity() + "</td>" + "</tr>" + "<tr>"
                + "<td>" + strTitle.get(8) + "</td>" + "<td>"
                + childObject.getBirth_order() + "</td>" + "</tr>" +
                "</table>" +
                "" +
                "" +
                "</body>" +
                "" +
                "</html>";


        RelativeLayout rel = (RelativeLayout) findViewById(R.id.relouter);
        rel.setVisibility(View.VISIBLE);
        ImageView img = (ImageView) findViewById(R.id.img_view_child);


        Bitmap bmp = StringToBitmap(childObject.getMultimedia());

        img.setImageBitmap(bmp);


        ww.loadData(myvar, "text/html; charset=UTF-8", null);
//						Button btnExit=(Button)findViewById(R.id.btnPreviewExit);
//						btnExit.setText(BackToMainMenu);
//						btnExit.setOnClickListener(this);

        RelativeLayout LayoutUpdate = (RelativeLayout) findViewById(R.id.layoutUpdate);
        Button Update = (Button) findViewById(R.id.btnUpdate);
//						Update.setText(BackToMainMenu);
        Update.setOnClickListener(this);

        //btnExit = (Button) findViewById(R.id.btnPreviewExit);
        btnExit.setText(strBackToMainMenu);
        btnExit.setOnClickListener(this);
        if (GlobalVars.EFposition == 0) {
            LayoutUpdate.setVisibility(View.GONE);
        }


    }

    private Bitmap StringToBitmap(String multimedia) {
        // TODO Auto-generated method stub
        try {
            byte[] encodeByte = Base64.decode(multimedia, Base64.DEFAULT);


            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
