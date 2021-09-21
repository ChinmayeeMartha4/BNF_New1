package com.example.mhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mhealth.helper.PW_Baseline;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

public class PregnantWomenBaseline extends AppCompatActivity {

    TextView txtName,txtHusbandName,txtVillageName,txtBlockName,txtDistrictName,txtContact,txtAge,txtEducation,txtOccupation;
    TextView txtHusbandAlive,txtNoOfChildren,txtNoOfGirls,txtNoOfBoys,txtMotherInLaw,txtFatherInLaw,txtFamilyIncome,txtSourceOfIncome;

    EditText etxtName,etxtHusbandName,etxtVillaageName,etxtBlockName,etxtDistrictName,etxtContact,etxtNoOfChildren,etxtNoOfGirls;
    EditText etxtNoOfBoys,etxtMotherInLaw,etxtFatherInLaw;

    Spinner spnAge,spnOccupation,spnEducation,spnFamilyIncome;

    RadioGroup rgHusbandAlive,rgSourceOfIncome;

    Button nextButton;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    PW_Baseline pw_baseline;
    String[] ageList;
    String[] incomeList;
    String[] occupationList;
    String[] educationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_women_baseline);
        init();

        ageList = getResources().getStringArray(R.array.age_array);
        incomeList = getResources().getStringArray(R.array.family_income);
        occupationList = getResources().getStringArray(R.array.occupation);
        educationList = getResources().getStringArray(R.array.education);

        ArrayAdapter<String> ageAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,ageList);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnAge.setPrompt("Select Age");
        spnAge.setAdapter(ageAdapter);

        ArrayAdapter<String> educationAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,educationList);
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnEducation.setPrompt("Select Education");
        spnEducation.setAdapter(educationAdapter);

        ArrayAdapter<String> incomeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,incomeList);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnFamilyIncome.setPrompt("Select Family Income");
        spnFamilyIncome.setAdapter(incomeAdapter);

        ArrayAdapter<String> occupationAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,occupationList);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnOccupation.setPrompt("Select Occupation");
        spnOccupation.setAdapter(occupationAdapter);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()) {
                    pw_baseline.setName(etxtName.getText().toString());
                    pw_baseline.setHusbandName(etxtHusbandName.getText().toString());
                    pw_baseline.setVillageName(etxtVillaageName.getText().toString());
                    pw_baseline.setBlockName(etxtBlockName.getText().toString());
                    pw_baseline.setDistrictName(etxtDistrictName.getText().toString());
                    pw_baseline.setContact(etxtContact.getText().toString());
                    pw_baseline.setAge(spnAge.getSelectedItem().toString());
                    pw_baseline.setEducation(spnEducation.getSelectedItem().toString());
                    pw_baseline.setOccupation(spnOccupation.getSelectedItem().toString());
                    if (((RadioButton) findViewById(rgHusbandAlive.getCheckedRadioButtonId())) != null) {
                        pw_baseline.setHusbandAlive(((RadioButton) findViewById(rgHusbandAlive.getCheckedRadioButtonId())).getText().toString());
                    }
                    pw_baseline.setNoOfChildren(etxtNoOfChildren.getText().toString());
                    pw_baseline.setNoOfBoys(etxtNoOfBoys.getText().toString());
                    pw_baseline.setNoOfGirls(etxtNoOfGirls.getText().toString());
                    if (((RadioButton) findViewById(rgSourceOfIncome.getCheckedRadioButtonId())) != null) {
                        pw_baseline.setSourceOfIncome(((RadioButton) findViewById(rgSourceOfIncome.getCheckedRadioButtonId())).getText().toString());
                    }
                    pw_baseline.setMotherInLaw(etxtMotherInLaw.getText().toString());
                    pw_baseline.setFatherInLaw(etxtFatherInLaw.getText().toString());
                    pw_baseline.setFamilyIncome(spnFamilyIncome.getSelectedItem().toString());

                    long inserted_id = sqliteHelper.setPregnantWomenBaseline(pw_baseline);
                    if (inserted_id > 0) {
                        Intent intent = new Intent(PregnantWomenBaseline.this, PregnantWomenBaseline2.class);
                        startActivity(intent);
                    }

                }

            }
        });
    }

    private boolean checkValidation() {
        Boolean ret = true;
        if(etxtName.getText().toString().equalsIgnoreCase(""))
        {
            etxtName.setError("Name Mandatory");
            ret = false;
        }
        if(etxtHusbandName.getText().toString().equalsIgnoreCase(""))
        {
            etxtHusbandName.setError("Husband Name Mandatory");
            ret = false;
        }
        return ret;
    }

    private void init() {
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        pw_baseline = new PW_Baseline();
        //RADIOGROUPS
        rgHusbandAlive = findViewById(R.id.rgHusbandAlive);
        rgSourceOfIncome = findViewById(R.id.rgSourceOfIncome);

        nextButton = findViewById(R.id.nextButton);

        //EDITTEXTS
        etxtName = findViewById(R.id.etxtName);
        etxtHusbandName = findViewById(R.id.etxtHusbandName);
        etxtVillaageName = findViewById(R.id.etxtVillageName);
        etxtBlockName = findViewById(R.id.etxtBlockName);
        etxtDistrictName = findViewById(R.id.etxtDistrictName);
        etxtContact = findViewById(R.id.etxtContact);
        etxtNoOfChildren = findViewById(R.id.etxtNoOfChildren);
        etxtNoOfGirls = findViewById(R.id.etxtNoOfGirls);
        etxtNoOfBoys = findViewById(R.id.etxtNoOfBoys);
        etxtMotherInLaw = findViewById(R.id.etxtMotherInLaw);
        etxtFatherInLaw = findViewById(R.id.etxtFatherInLaw);

        //SPINNER
        spnAge = findViewById(R.id.spnAge);
        spnOccupation = findViewById(R.id.spnOccupation);
        spnEducation = findViewById(R.id.spnEducation);
        spnFamilyIncome = findViewById(R.id.spnFamilyIncome);


        //TEXTVIEWS
        txtName = findViewById(R.id.txtName);
        txtHusbandName = findViewById(R.id.txtHusbandName);
        txtVillageName = findViewById(R.id.txtVillageName);
        txtBlockName = findViewById(R.id.txtBlockName);
        txtDistrictName = findViewById(R.id.txtDistrictName);
        txtContact = findViewById(R.id.txtContact);
        txtAge = findViewById(R.id.txtAge);
        txtEducation = findViewById(R.id.txtEducation);
        txtOccupation = findViewById(R.id.txtOccupation);
        txtHusbandAlive = findViewById(R.id.txtHusbandAlive);
        txtNoOfChildren = findViewById(R.id.txtNoOfChildren);
        txtNoOfGirls = findViewById(R.id.txtNoOfGirls);
        txtNoOfBoys = findViewById(R.id.txtNoOfBoys);
        txtMotherInLaw = findViewById(R.id.txtMotherInLaw);
        txtFatherInLaw = findViewById(R.id.txtFatherInLaw);
        txtFamilyIncome = findViewById(R.id.txtFamilyIncome);
        txtSourceOfIncome = findViewById(R.id.txtSourceOfIncome);

    }
}
