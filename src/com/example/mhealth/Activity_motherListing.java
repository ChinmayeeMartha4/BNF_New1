package com.example.mhealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mhealth.adapter.Mother_Adapter;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.Mother;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity_motherListing extends Activity implements OnItemClickListener {

    TextView txtFooter;
    String strYes, strNo, strCancel, strMotherListing;
    private ListView listView;
    private Mother motherObj;
    private ArrayList<Mother> list;
    private SqliteHelper helper;
    private Mother_Adapter adapter;
    private SharedPrefHelper sph;
    private Button btnMainMenu;
    private String getHouseNo;
    private String getMotherName;
    private int intMother_id;

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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showlist);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/View/ Mother List");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        listView = (ListView) findViewById(R.id.listview_parent);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        motherObj = new Mother();
        list = new ArrayList<Mother>();
        helper = new SqliteHelper(this);


        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        String strHouseNo = helper.LanguageChanges(ConstantValue.LANHouseNo,
                languageId);
        String strMother = helper.LanguageChanges(ConstantValue.LANMother, languageId);
        String strChild = helper.LanguageChanges(ConstantValue.LANNumberOfChild, languageId);
        String strFooter = helper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        String strBackToMM = helper.LanguageChanges(ConstantValue.LANBactToMM, languageId);
        strYes = helper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = helper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = helper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strMotherListing = helper.LanguageChanges(ConstantValue.LANMotherListing, languageId);

        //txtFooter.setText(strFooter);
        btnMainMenu.setText(strBackToMM);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());


        ArrayList<Object> strindexData = new ArrayList<Object>();

        strindexData.add(strHouseNo);
        strindexData.add(strMother);
        strindexData.add(strChild);


        list = helper.getAllMotherList();
        adapter = new Mother_Adapter(this, list, strindexData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

    }

    public void btn_click(View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        // TODO Auto-generated method stub

        TextView tvhouseNo = (TextView) view.findViewById(R.id.txtfield1);
        getHouseNo = tvhouseNo.getText().toString().replaceAll(":", "").trim();
        TextView tvMother = (TextView) view.findViewById(R.id.txtfield2);
        getMotherName = tvMother.getText().toString().replaceAll(":", "").trim();


        try {

            intMother_id = helper.getMotherIdbyNameNHHID(getHouseNo, getMotherName);
            Log.e("women id", String.valueOf(intMother_id));

           /* Intent WomenData = new Intent(this, ActivityMotherView.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", intMother_id);
            WomenData.putExtras(bundle);
            startActivity(WomenData);*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("error", "on index women");
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strMotherListing + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_motherListing.this,
                        ActivityViews.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
