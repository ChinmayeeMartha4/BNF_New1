package com.example.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mhealth.adapter.Parent_Adapter;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.Parent;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class Activity_parentList extends Activity implements
        OnItemClickListener {

    //	private String getParentName;
    int intparentId;
    TextView txtFooter, txtnodata;
    RelativeLayout laynodata;
    private ListView listView;
    private Parent parent;
    private ArrayList<Parent> list;
    private SqliteHelper helper;
    private Parent_Adapter Adapter;
    private SharedPrefHelper sph;
    private Button btnMainMenu;
    private String getAadhaarNo;
    private int intFamilyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showlist);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/View/ Eligible Family List");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        txtnodata = (TextView) findViewById(R.id.nodatafound);
        listView = (ListView) findViewById(R.id.listview_parent);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        laynodata = (RelativeLayout) findViewById(R.id.listview_layout);
        parent = new Parent();
        list = new ArrayList<Parent>();
        helper = new SqliteHelper(this);

        list = helper.allEligibleFamilySearch(GlobalVars.familystatus);


        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId

        if (list.size() > 0)
            txtnodata.setVisibility(View.GONE);
        else
            laynodata.setVisibility(View.GONE);

        String strHouseNo = helper.LanguageChanges(ConstantValue.LANHouseNo, languageId);
        String strHouseHH = helper.LanguageChanges(ConstantValue.LANHHH, languageId);
        String strAadharNo = helper.LanguageChanges(ConstantValue.LANAadhaarHH, languageId);
        String strBackToMM = helper.LanguageChanges(ConstantValue.LANBactToMM, languageId);
        String strUpdate = helper.LanguageChanges(ConstantValue.LANUpdate, languageId);
        String strFooter = helper.LanguageChanges(ConstantValue.LANTPIC, languageId);

        txtFooter.setText(strFooter);
        btnMainMenu.setText(strBackToMM);

        ArrayList<Object> strindexData = new ArrayList<Object>();
        strindexData.add(strHouseNo);
        strindexData.add(strHouseHH);
        strindexData.add(strAadharNo);


        Adapter = new Parent_Adapter(this, list, strindexData);
        listView.setAdapter(Adapter);
        listView.setOnItemClickListener(this);

    }

    public void btn_click(View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() { // TODO Auto-generated method


        // super.onBackPressed();
        if (GlobalVars.familystatus == 0) {
            Intent intent = new Intent(this, MainMenuRegistrationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SyncActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        Adapter.notifyDataSetChanged();
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        // TODO Auto-generated method stub
        TextView tvAadhaarNo = (TextView) view.findViewById(R.id.txtfield3);
        getAadhaarNo = tvAadhaarNo.getText().toString().replaceAll(":", "").trim();
        intFamilyId = helper.getFamilyId(getAadhaarNo);
        Intent ParentData = new Intent(this, ActivityParentData.class);
        Bundle bundle = new Bundle();
        bundle.putInt("index", intFamilyId);
        ParentData.putExtras(bundle);
        startActivity(ParentData);
    }


}
