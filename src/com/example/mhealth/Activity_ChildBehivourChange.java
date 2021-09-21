package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.adapter.SpinnerAdapter1;
import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Utility;
import com.example.mhealth.model.ChildBehaviourChange;
import com.example.mhealth.utils.CommonMethods;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_ChildBehivourChange extends Activity {

    TextView tvTitleText, txtFooter;
    RadioGroup rgregisteredICDS, rgSupplementsICDS, rgMotherEducated, rgVaccination, rgchildPremature, rgBreastfeeding, rgchildBreastfeeding, rgDisability, rgEdemaOdema;
    RadioButton rbICDSYes, rbICDSNo, rbSupplementsICDSRegular, rbSupplementsICDSIrregular, rbSupplementsICDSNotTaken, rbMotherEducatedYes, rbMotherEducatedNo,
            rbVaccinationRegular, rbVaccinationIrregular, rbVaccinationNotTaken, rbchildPrematureYes, rbchildPrematureNo, rbBreastfeedingYes, rbBreastfeedingeNo, rbchildBreastfeedingYes,
            rbchildBreastfeedingNo, rbDisabilityYes, rbDisabilityNo, rbEdemaOdemaYes, rbEdemaOdemaNo;
    EditText etBirthOrder;

    SqliteHelper sqlitehelper;
    EditText etxtSearchByHouseHoldId;
    String strId;
    Spinner spnChildName;
    private int user_id, flag = 0, migrationflag = 0, dateflag = 0, absentFlag = 0, temporaryMigrateFlag = 0, onFarmFlag = 0, privateSchoolFlag = 0, permanentMigrateFlag = 0, deathFlag = 0;
    ArrayList<Integer> absent_child;
    ArrayList<ChildNutrition> migrate_child;
    ArrayList<ChildNutrition> monitor_child;
    private String formattedDate;
    int flag2 = 0;
    private int child_id;
    private String child_name;
    private ChildNutrition childMonitor;
    private ArrayList<ChildNutrition> list;
    private ArrayList<ChildNutrition> prelist;
    String strData;
    LinearLayout lnr_pre;
    SharedPrefHelper sph;
    Button btnChildGo;
    EditText et_searchchildname;
    private String searchData;
    ArrayList<String> spinnerchildData = new ArrayList<>();
    ChildBehaviourChange childBehaviourChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__child_behivour_change);
        init();


        childBehaviourChange=new ChildBehaviourChange();

        tvTitleText.setText("Child Behaviour Change");

        txtFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    intent = new Intent().parseUri("http://indevjobs.org", Intent.URI_INTENT_SCHEME);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }

        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        formattedDate = df.format(c.getTime());

        user_id = sph.getInt("user_id", 0);


        monitor_child = sqlitehelper.getMonitoringData(user_id);

        if (monitor_child != null) {
            for (int i = 0; i < monitor_child.size(); i++) {
                absent_child.add(monitor_child.get(i).getChild_id());
            }
            for (int i = 0; i < absent_child.size(); i++) {
                migrate_child.add(sqlitehelper.getChildStatusData(absent_child.get(i)));
            }
        }

        spnChildName.setAdapter(new SpinnerAdapter1(getApplicationContext(), monitor_child, migrate_child, formattedDate));
        spnChildName.setPrompt("Select Child");

        spnChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                child_id = monitor_child.get(i).getChild_id();
                child_name = monitor_child.get(i).getName_of_child();
                if (child_id != 0) {
                    flag = 0;
                    flag2 = 0;
//                    imgCake.setVisibility(View.GONE);
//                    setCake(child_id);
                }

                try {

//                    wvNutritionHistory.loadUrl("about:blank");
                    list = sqlitehelper.getchildNutrationMonitorDataBy(child_id);
                    prelist = sqlitehelper.getChildPrevious(child_id);
                    if (list.get(0) != null) {
//                        wvNutritionHistory.setVisibility(View.VISIBLE);
                        getData(list);
                    }
                    if (prelist.get(0) != null) {
//                        preheight.setVisibility(View.VISIBLE);
//                        lnr_pre.setVisibility(View.VISIBLE);
                        setData(prelist);
                    }

                } catch (Exception e) {
//                    wvNutritionHistory.setVisibility(View.GONE);
//                    preheight.setVisibility(View.GONE);
//                    lnr_pre.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        et_searchchildname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                {

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData = et_searchchildname.getText().toString();
                // populatechildNam(spinnerchildData, "child_name", searchData);
                populatechildNam ();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });





    }

    protected void getData(ArrayList<ChildNutrition> list2) {
        strData = "";
        Log.e("data", strData);
        for (int i = 0; i < list2.size(); i++) {
            childMonitor = list2.get(i);
            childMonitor.getMigration();
            String hdate = childMonitor.getDate_of_monitoring().substring(8, 10) + "-" +
                    childMonitor.getDate_of_monitoring().substring(5, 7) + "-" +
                    childMonitor.getDate_of_monitoring().substring(0, 4);

            if (childMonitor.getMigration() != null) {
                switch (childMonitor.getMigration()) {
                    case "Absent":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "<td>"
                                + "Absent" + "</td>" + "</tr>";

                        break;

                    case "Temporary Migrate":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "<td>"
                                + "Temporary Migrate" + "</td>" + "</tr>";

                        break;

                    case "On-farm":

                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "<td>"
                                + "On-farm" + "</td>" + "</tr>";
                        break;

                    case "Private school":
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "<td>"
                                + "Private school" + "</td>" + "</tr>";
                        break;

                    case "Permanent Migrate":

                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "<td>"
                                + "Permanent Migrate" + "</td>" + "</tr>";
                        break;

                    default:
                        strData = strData + "<tr>" + "<td>"
                                + hdate + "</td>" + "<td>"
                                + childMonitor.getWeight() + "</td>" + "<td>"
                                + childMonitor.getHeight() + "</td>" + "<td>"
                                + childMonitor.getMuac() + "</td>" + "<td>"
                                + childMonitor.getHB() + "</td>" + "</tr>";


                }
            } else {
                strData = strData + "<tr>" + "<td>"
                        + hdate + "</td>" + "<td>"
                        + childMonitor.getWeight() + "</td>" + "<td>"
                        + childMonitor.getHeight() + "</td>" + "<td>"
                        + childMonitor.getMuac() + "</td>" + "<td>"
                        + childMonitor.getHB() + "</td>" + "</tr>";

            }

        }
//        showWebView(strData);
    }


    @SuppressLint("ShowToast")
    public void clickGo(View v) {
        strId = etxtSearchByHouseHoldId.getText().toString();
        if (strId.equals("")) {
            //populateList(spnChildName, "child_registration", "child_id",
            //      "name_of_child", "Select Child", "where anganwadi_center_id=" + user_id);
        } else {
            populateList(spnChildName, "child", "child_id",
                    "name_of_" +
                            "child", "Select Child", "where house_number="
                            + strId + " and  anganwadi_center_id=" + user_id);
        }
    }

    public void  populatechildNam( ) {
        /*if (!searchData.equalsIgnoreCase("")) {
            this.spinnerchildData = sqlitehelper.getChildSpnnerforsearchdata(searchData);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.spinnerchildData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setAdapter(aa);
        }else {
            this.spinnerchildData = sqlitehelper.getChildSpnnerforsearchdata1(searchData);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, this.spinnerchildData);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnChildName.setAdapter(aa);
        }*/
    }

    private void populateList(Spinner spinner, String tableName, String col_id,
                              String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        ArrayAdapter<SpinnerHelper> adapter = null;
        items = sqlitehelper.populateChildSpinner(tableName, col_id, col_value,
                label, whr);
        if (items.size() == 0) {
            SpinnerHelper spinnerHelper = new SpinnerHelper("", "");
            items.add(spinnerHelper);
        }
        adapter = new ArrayAdapter<SpinnerHelper>(
                Activity_ChildBehivourChange.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    private void setData(ArrayList<ChildNutrition> prelist) {
        String height = prelist.get(0).getHeight();
        String start_dt = prelist.get(0).getDate_of_monitoring();
        String date = "";
        try {
            date = new SimpleDateFormat("MMM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(start_dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        preheight.setText(date + " = " + height + " cm");
    }

    private void init() {
        tvTitleText= findViewById(R.id.tvTitleText);
        txtFooter= findViewById(R.id.txtFooter);
        etBirthOrder= findViewById(R.id.etBirthOrder);
        sqlitehelper= new SqliteHelper(this);
        etxtSearchByHouseHoldId = (EditText) findViewById(R.id.etxtSearchByHouseHoldId);
        lnr_pre= findViewById(R.id.lnr_pre);
        spnChildName= findViewById(R.id.spnChildName);
        sph = new SharedPrefHelper(this);
        list = new ArrayList<ChildNutrition>();
        prelist = new ArrayList<ChildNutrition>();
        btnChildGo = (Button) findViewById(R.id.btnChildGo);
        et_searchchildname = (EditText) findViewById(R.id.et_searchchildname);


        rgregisteredICDS = (RadioGroup) findViewById(R.id.rgregisteredICDS);
        rgSupplementsICDS = (RadioGroup) findViewById(R.id.rgSupplementsICDS);
        rgMotherEducated = (RadioGroup) findViewById(R.id.rgMotherEducated);
        rgVaccination = (RadioGroup) findViewById(R.id.rgVaccination);
        rgchildPremature = (RadioGroup) findViewById(R.id.rgchildPremature);
        rgBreastfeeding = (RadioGroup) findViewById(R.id.rgBreastfeeding);
        rgchildBreastfeeding = (RadioGroup) findViewById(R.id.rgchildBreastfeeding);
        rgDisability = (RadioGroup) findViewById(R.id.rgDisability);
        rgEdemaOdema = (RadioGroup) findViewById(R.id.rgEdemaOdema);

        rbICDSYes = (RadioButton) findViewById(R.id.rbICDSYes);
        rbICDSNo = (RadioButton) findViewById(R.id.rbICDSNo);
        rbSupplementsICDSRegular = (RadioButton) findViewById(R.id.rbSupplementsICDSRegular);
        rbSupplementsICDSIrregular = (RadioButton) findViewById(R.id.rbSupplementsICDSIrregular);
        rbSupplementsICDSNotTaken = (RadioButton) findViewById(R.id.rbSupplementsICDSNotTaken);
        rbMotherEducatedYes = (RadioButton) findViewById(R.id.rbMotherEducatedYes);
        rbMotherEducatedNo = (RadioButton) findViewById(R.id.rbMotherEducatedNo);
        rbVaccinationRegular = (RadioButton) findViewById(R.id.rbVaccinationRegular);
        rbVaccinationIrregular = (RadioButton) findViewById(R.id.rbVaccinationIrregular);
        rbVaccinationNotTaken = (RadioButton) findViewById(R.id.rbVaccinationNotTaken);
        rbchildPrematureYes = (RadioButton) findViewById(R.id.rbchildPrematureYes);
        rbchildPrematureNo = (RadioButton) findViewById(R.id.rbchildPrematureNo);
        rbBreastfeedingYes = (RadioButton) findViewById(R.id.rbBreastfeedingYes);
        rbBreastfeedingeNo = (RadioButton) findViewById(R.id.rbBreastfeedingeNo);
        rbchildBreastfeedingYes = (RadioButton) findViewById(R.id.rbchildBreastfeedingYes);
        rbchildBreastfeedingNo = (RadioButton) findViewById(R.id.rbchildBreastfeedingNo);
        rbDisabilityYes = (RadioButton) findViewById(R.id.rbDisabilityYes);
        rbDisabilityNo = (RadioButton) findViewById(R.id.rbDisabilityNo);
        rbEdemaOdemaYes = (RadioButton) findViewById(R.id.rbEdemaOdemaYes);
        rbEdemaOdemaNo = (RadioButton) findViewById(R.id.rbEdemaOdemaNo);


        absent_child = new ArrayList<Integer>();
        monitor_child = new ArrayList<>();
        migrate_child = new ArrayList<>();
    }


    public void Submit_Details(View v){
        String rb1 = "", rb2 = "", rb3 = "", rb4 = "", rb5="", rb6="", rb7="", rb8="", rb9="";
        if(validation()) {
//        if(rb1.equals("") || rb2.equals("") || rb3.equals("") || rb4.equals("") || rb5.equals("") || rb6.equals("") || rb7.equals("") || rb8.equals("") || rb9.equals("")){
//            Toast.makeText(this, "Please fill the all details", Toast.LENGTH_SHORT).show();
//        }else {

            rb1 = ((RadioButton) findViewById(rgregisteredICDS.getCheckedRadioButtonId())).getText().toString();
            rb2 = ((RadioButton) findViewById(rgSupplementsICDS.getCheckedRadioButtonId())).getText().toString();
            rb3 = ((RadioButton) findViewById(rgMotherEducated.getCheckedRadioButtonId())).getText().toString();
            rb4 = ((RadioButton) findViewById(rgVaccination.getCheckedRadioButtonId())).getText().toString();
            rb5 = ((RadioButton) findViewById(rgchildPremature.getCheckedRadioButtonId())).getText().toString();
            rb6 = ((RadioButton) findViewById(rgBreastfeeding.getCheckedRadioButtonId())).getText().toString();
            rb7 = ((RadioButton) findViewById(rgchildBreastfeeding.getCheckedRadioButtonId())).getText().toString();
            rb8 = ((RadioButton) findViewById(rgDisability.getCheckedRadioButtonId())).getText().toString();
            //rb9 = ((RadioButton) findViewById(rgEdemaOdema.getCheckedRadioButtonId())).getText().toString();
//        }


            childBehaviourChange.setRegistered_icds(rb1);
            childBehaviourChange.setSupplements_icds(rb2);
            childBehaviourChange.setMother_educated(rb3);
            childBehaviourChange.setVaccination(rb4);
            childBehaviourChange.setChild_premature(rb5);
            childBehaviourChange.setBirth_breast_feeding(rb6);
            childBehaviourChange.setChild_breast_feeding(rb7);
            childBehaviourChange.setDisability(rb8);
            childBehaviourChange.setEdema_Odema(rb9);
            childBehaviourChange.setBirth_order(etBirthOrder.getText().toString().trim());
            childBehaviourChange.setMobile_unique_id(CommonMethods.getUUID());
            childBehaviourChange.setCreated_on_mobile(CommonMethods.getCurrentDateTime());
            childBehaviourChange.setLatitude(GlobalVars.lattitude);
            childBehaviourChange.setLongitude(GlobalVars.longitude);
            childBehaviourChange.setChild_id(String.valueOf(child_id));

            long local_cb_id = sqlitehelper.SaveChildBehaviourChangeDetails(childBehaviourChange);
            if (local_cb_id > 0) {
                Toast.makeText(this, "Child Behaviour Monitoring Done", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainMenuMonitoringActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean validation() {

        boolean ret = true;

        if (etBirthOrder.getText().toString().equalsIgnoreCase("")){
            etBirthOrder.setError("Please enter Birth Order");
            ret=false;
        }

        return ret;
    }
}