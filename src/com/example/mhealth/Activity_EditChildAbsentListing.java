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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mhealth.adapter.EditAdolescentGirlAbsentListAdapter;
import com.example.mhealth.adapter.EditChildAbsentListAdapter;
import com.example.mhealth.adapter.EditPregnantWomenAbsentListAdapter;
import com.example.mhealth.helper.Adolescent;
import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_EditChildAbsentListing extends Activity {

    TextView txtFooter, strMainMenu;
    String strFooter, strBackToMM, strYes, strNo, strCancel, strChildListing, strMother;
    EditPregnantWomenAbsentListAdapter pregnant_adapter;
    EditAdolescentGirlAbsentListAdapter adolescent_adapter;
    String select_option = null;
    private ListView listView;
    private Child child;
    private ArrayList<Child> list;
    private ArrayList<PregnantWomen> pregnantWomens;
    private ArrayList<Adolescent> adolescents;
    private SqliteHelper helper;
    private EditChildAbsentListAdapter adapter;
    private String getChildName;
    private int intChildId;
    private SharedPrefHelper sph;
    private Button btnMainMenu;
    private String getChildId;

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
        t.setScreenName(GlobalVars.username + "-> Edit/ Edit Child Absent Status List");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                select_option = bundle.getString("option");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.listview_parent);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        child = new Child();
        list = new ArrayList<Child>();
        pregnantWomens = new ArrayList<PregnantWomen>();
        adolescents = new ArrayList<Adolescent>();

        helper = new SqliteHelper(this);

        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        String ChildName = helper.LanguageChanges(ConstantValue.LANChildName, languageId);
        String HouseNo = helper.LanguageChanges(ConstantValue.LANHouseNo, languageId);
        String DoB = helper.LanguageChanges(ConstantValue.LANDateOfBirth, languageId);
        strMother = helper.LanguageChanges(ConstantValue.LANMotherName, languageId);
        strFooter = helper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strBackToMM = helper.LanguageChanges(ConstantValue.LANBactToMM, languageId);
        strYes = helper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = helper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = helper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strChildListing = helper.LanguageChanges(ConstantValue.LANChildListing, languageId);
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

        strindexData.add(ChildName);
        strindexData.add(strMother);

        if (select_option.equalsIgnoreCase("child")) {
            list = helper.getAllAbsentChild(getCurrDate());
            adapter = new EditChildAbsentListAdapter(this, list);
            listView.setAdapter(adapter);
        } else if (select_option.equalsIgnoreCase("pregnant_women")) {
            pregnantWomens = helper.getAllAbsentPregnantWomen(getCurrDate());
            pregnant_adapter = new EditPregnantWomenAbsentListAdapter(this, pregnantWomens);
            listView.setAdapter(pregnant_adapter);
        } else if (select_option.equalsIgnoreCase("adolescent_girl")) {
            adolescents = helper.getAllAbsentAdolescentGirl(getCurrDate());
            adolescent_adapter = new EditAdolescentGirlAbsentListAdapter(this, adolescents);
            listView.setAdapter(adolescent_adapter);
        }
        //listView.setOnItemClickListener(this);
    }

    public String getCurrDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fdate = df.format(c.getTime());
        String curDate = fdate.substring(0, 7) + "%";
        return curDate;
    }

    public void btn_click(View v) {

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (select_option.equalsIgnoreCase("child")) {
            list = helper.getAllAbsentChild(getCurrDate());
            adapter = new EditChildAbsentListAdapter(this, list);
            listView.setAdapter(adapter);
        } else if (select_option.equalsIgnoreCase("pregnant_women")) {
            pregnantWomens = helper.getAllAbsentPregnantWomen(getCurrDate());
            pregnant_adapter = new EditPregnantWomenAbsentListAdapter(this, pregnantWomens);
            listView.setAdapter(pregnant_adapter);
        } else if (select_option.equalsIgnoreCase("adolescent_girl")) {
            adolescents = helper.getAllAbsentAdolescentGirl(getCurrDate());
            adolescent_adapter = new EditAdolescentGirlAbsentListAdapter(this, adolescents);
            listView.setAdapter(adolescent_adapter);
        }
        super.onResume();
    }

 /*  public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        // TODO Auto-generated method stub

      *//*  TextView tvchildId = (TextView) view.findViewById(R.id.txtfield4);
        String ici = tvchildId.getText().toString();
        intChildId = Integer.parseInt(ici);*//*

     *//* TextView tvchildName = (TextView) view.findViewById(R.id.txtfield1);
        String child_name = tvchildName.getText().toString().replace(" : ", "");
        Log.e("............", "----" + child_name);*//*

        try {
            //intChildId = helper.getChildId(getChildName,getChildId);

            Intent ChildData = new Intent(this, Activity_EditAbsentChildNutrition.class);
            Bundle bundle = new Bundle();
            bundle.putInt("index", list.get(position).getChild_id());
            bundle.putString("nameofchild", list.get(position).getChild_name());
            bundle.putString("currDate", getCurrDate());

            GlobalVars.ChildViewID = intChildId;
            ChildData.putExtras(bundle);
            startActivity(ChildData);
            finish();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("error", e.toString());
            e.printStackTrace();
        }

    }
*/

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strChildListing + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_EditChildAbsentListing.this, ActivityEdit.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
