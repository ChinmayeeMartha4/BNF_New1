package com.example.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Activity_EditChildNutritionHB extends Activity implements OnItemSelectedListener {
    TextView tvName, tvMotherName, tvHouseNo, tvSelectYearMonth, tvSelectYear, tvSelectMonth, tvHB, tvFooter;
    Button btnUpdate;
    Spinner spnYear, spnMonth, spnHB;

    int childID = 0;
    String nutritionID = "";

    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;

    String strname, strmothername, strhouseno, stryearmonth, stryear, strmonth, strhb, strfooter, strbtnupdate, strYes, strNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child_nutrition_hb);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Edit/Edit_Child_Nutrition_HB_List/ Edit Child Nutrition HB");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        populateHBspn();

        String languageId = sph.getString("Language", "1");
        strname = sqliteHelper.LanguageChanges(ConstantValue.LANChildName, languageId);
        strmothername = sqliteHelper.LanguageChanges(ConstantValue.LANMotherName, languageId);
        strhouseno = sqliteHelper.LanguageChanges(ConstantValue.LANHouseNo, languageId);
        stryearmonth = sqliteHelper.LanguageChanges(ConstantValue.LANSelectYearMonth, languageId);
        stryear = sqliteHelper.LanguageChanges(ConstantValue.LANYear, languageId);
        strmonth = sqliteHelper.LanguageChanges(ConstantValue.LANMonth, languageId);
        strhb = sqliteHelper.LanguageChanges(ConstantValue.LANChildHb, languageId);
        strfooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strbtnupdate = sqliteHelper.LanguageChanges(ConstantValue.LANsv, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);

        Bundle b = getIntent().getExtras();
        childID = b.getInt("index");

        String[] childInfo = sqliteHelper.getChildInfo(childID);
        String gethhid = childInfo[0];
        String getname = childInfo[1];
        String getMother = childInfo[2];

        tvName.setText(strname + "  :  " + getname);
        tvMotherName.setText(strmothername + "  :  " + getMother);
        tvHouseNo.setText(strhouseno + "  :  " + gethhid);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sqliteHelper.getYear(childID));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setPrompt("Select Year");
        spnYear.setAdapter(aa);

        tvSelectYearMonth.setText(stryearmonth);
        tvSelectYear.setText(stryear);
        tvSelectMonth.setText(strmonth);
        tvHB.setText(strhb);
        tvFooter.setText(strfooter);
        btnUpdate.setText(strbtnupdate);
    }

    public void initialization() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvMotherName = (TextView) findViewById(R.id.tvMotherName);
        tvHouseNo = (TextView) findViewById(R.id.tvHouseNo);
        tvSelectYearMonth = (TextView) findViewById(R.id.txtSelectYearMonth);
        tvSelectYear = (TextView) findViewById(R.id.txtSelectYear);
        tvSelectMonth = (TextView) findViewById(R.id.txtSelectMonth);
        tvHB = (TextView) findViewById(R.id.txtUpdateHB);
        tvFooter = (TextView) findViewById(R.id.txtFooter);

        btnUpdate = (Button) findViewById(R.id.btnUpdateHB);

        spnHB = (Spinner) findViewById(R.id.spnUpdateHB);

        spnMonth = (Spinner) findViewById(R.id.spnSelectMonth);
        spnMonth.setOnItemSelectedListener(this);
        spnYear = (Spinner) findViewById(R.id.spnSelectYear);
        spnYear.setOnItemSelectedListener(this);

        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        switch (arg0.getId()) {

            case R.id.spnSelectYear:
                ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sqliteHelper.getMonth(childID, spnYear.getSelectedItem().toString()));
                aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Setting the ArrayAdapter data on the Spinner
                spnMonth.setPrompt("Select Month");
                spnMonth.setAdapter(aa1);

                break;

            case R.id.spnSelectMonth:
                String halfDate = spnYear.getSelectedItem().toString() + "-" + spnMonth.getSelectedItem().toString();

                nutritionID = sqliteHelper.getHBbyDate(childID, halfDate);
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void updateChildNutrition(View v) {
        String childhb = "";
        if (spnHB.getSelectedItem().toString().equals("NA - Other issue")) {
            childhb = "NA - Other issue";
        } else if (spnHB.getSelectedItem().toString().equals("NA - Child not Co-operating")) {
            childhb = "NA - Child not Co-operating";
        } else {
            childhb = spnHB.getSelectedItem().toString();
        }
        sqliteHelper.setChildHB(nutritionID, childhb);

        Toast.makeText(getApplicationContext(), "Child nutrition HB updated.", Toast.LENGTH_LONG).show();// child registration done!!

        Intent intent = new Intent(this, ActivityEdit.class);
        startActivity(intent);
    }

    public void populateHBspn() {
        String hbs[] = {"5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12", "12.5", "13", "13.5", "14", "14.5", "15", "NA - Other issue", "NA - Child not Co-operating", "Machine not available"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hbs);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnHB.setAdapter(spinnerArrayAdapter);
    }
}

