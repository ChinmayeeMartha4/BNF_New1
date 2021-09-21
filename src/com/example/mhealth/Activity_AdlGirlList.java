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

import com.example.mhealth.adapter.AdolescentGirlListAdapter;
import com.example.mhealth.helper.Adolescent;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class Activity_AdlGirlList extends Activity implements OnItemClickListener {

    Adolescent adoGirl;
    TextView txtFooter;
    String strYes, strNo, strCancel, strAdolescentListing;
    private ListView listView;
    private Button btnMainMenu;
    private SharedPrefHelper sph;
    private SqliteHelper helper;
    private ArrayList<Adolescent> list;
    private AdolescentGirlListAdapter Adapter;
    private String getHouseNo;
    private String getGirlName;
    private int intAdoGirl_id;

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

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/view/ Adolescent List");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        setContentView(R.layout.activity_showlist);
        listView = (ListView) findViewById(R.id.listview_parent);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        adoGirl = new Adolescent();

        list = new ArrayList<Adolescent>();
        helper = new SqliteHelper(this);
        list = helper.allAdolescentGirl();
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId

        String strHouseNo = helper.LanguageChanges(ConstantValue.LANHouseNo,
                languageId);
        String strAdoGirl = helper.LanguageChanges(ConstantValue.LANGirlName,
                languageId);
        String strFather = helper.LanguageChanges(ConstantValue.LANFatherName,
                languageId);
        String strFooter = helper.LanguageChanges(ConstantValue.LANTPIC,
                languageId);
        String strMainMenu = helper.LanguageChanges(ConstantValue.LANBactToMM,
                languageId);
        strYes = helper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = helper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = helper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strAdolescentListing = helper.LanguageChanges(ConstantValue.LANAdolescentListing, languageId);

        //txtFooter.setText(strFooter);
        btnMainMenu.setText(strMainMenu);
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
        strindexData.add(strAdoGirl);
        strindexData.add(strFather);

        Adapter = new AdolescentGirlListAdapter(this, list, strindexData);
        listView.setAdapter(Adapter);
        listView.setOnItemClickListener(this);

    }

    public void btn_click(View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        // TODO Auto-generated method stub

        TextView tvhouseNo = (TextView) view.findViewById(R.id.txtfield1);
        getHouseNo = tvhouseNo.getText().toString().replaceAll(":", "").trim();
        TextView tvPreWomen = (TextView) view.findViewById(R.id.txtfield2);
        getGirlName = tvPreWomen.getText().toString().replaceAll(":", "").trim();


        intAdoGirl_id = helper.getAdoGirlIdbyNameNHHID(getHouseNo, getGirlName);
        Log.e("AdoGirl id", String.valueOf(intAdoGirl_id));

     /*   Intent WomenData = new Intent(this,
                ActivityAdlView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("index", intAdoGirl_id);
        WomenData.putExtras(bundle);
        startActivity(WomenData);*/


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strAdolescentListing + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_AdlGirlList.this,
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
