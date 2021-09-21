package com.example.mhealth;

import android.app.Activity;
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

import com.example.mhealth.adapter.EditEF_Adapter;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.EditEF;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class Activity_EditEligibleFamilyListing extends Activity implements OnItemClickListener {
    TextView txtFooter;
    String strYes, strNo, strCancel, streefListing;
    private ListView listView;
    private EditEF eefObj;
    private ArrayList<EditEF> list;
    private SqliteHelper helper;
    private EditEF_Adapter adapter;
    private SharedPrefHelper sph;
    private Button btnMainMenu;
    private String getHouseNo;
    private String getHHName;
    private int HH_id;

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
        setContentView(R.layout.activity_showlist);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Edit/ Edit Eligible Family List");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        listView = (ListView) findViewById(R.id.listview_parent);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        eefObj = new EditEF();
        list = new ArrayList<EditEF>();
        helper = new SqliteHelper(this);

        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        String strHouseNo = helper.LanguageChanges(ConstantValue.LANHouseNo,
                languageId);
        String strHHName = helper.LanguageChanges(ConstantValue.LANHHH, languageId);
        String strFooter = helper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        String strBackToMM = helper.LanguageChanges(ConstantValue.LANBactToMM, languageId);
        strYes = helper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = helper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = helper.LanguageChanges(ConstantValue.LANCancel, languageId);
        streefListing = helper.LanguageChanges(ConstantValue.LANElFamily, languageId);

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
        strindexData.add(strHHName);

        list = helper.getEFList();
        adapter = new EditEF_Adapter(this, list, strindexData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tvhouseNo = (TextView) arg1.findViewById(R.id.txtfield1);
        getHouseNo = tvhouseNo.getText().toString().replaceAll(":", "").trim();
        ;

        try {

//            Intent WomenData = new Intent(this, Activity_EditEF.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("index", getHouseNo);
//            WomenData.putExtras(bundle);
//            startActivity(WomenData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("error", "on index women");
            e.printStackTrace();
        }


    }

    public void btn_click(View v) {


        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();

    }
}
