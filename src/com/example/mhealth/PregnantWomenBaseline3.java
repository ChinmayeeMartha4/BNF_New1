package com.example.mhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.PW_Baseline;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

public class PregnantWomenBaseline3 extends AppCompatActivity {
    TextView txtHandWashing,txtHandWashingWhen,txtHowDiarrheaPrevented,txtSourceOfWater,txtBoilWaterBeforeDrinking,txtHaveToiletAtHome;
    TextView txtKeepSurroundingClean,txtHeardAneamia,txtNutrientDeficientInAneamia,txtCauseOfAneamia,txtItemEatAtHome,txtConsumeVegOrNonVeg;
    TextView txtImportantOfChildNutrition,txtDifferentFoodImportant,txtVisitAnganwadiCenter,txtAttendMeeting,txtAwareOfGovtScheme;
    TextView txtHaveMotherCard,txtWhatToDoInInfectation;
    Button submitButton;

    RadioGroup rgHandWashing,rgHandWashingWhen,rgHowDiarrheaPrevented,rgSourceOfWater,rgBoilWaterBeforeDrinking,rgHaveToiletAtHome;
    RadioGroup rgKeepSurroundingClean,rgHeardAneamia,rgNutrientDeficientInAneamia,rgCauseOfAneamia,rgItemEatAtHome,rgConsumeVegOrNonVeg;
    RadioGroup rgImportantOfChildNutrition,rgDifferentFoodImportant,rgVisitAnganwadiCenter,rgAttendMeeting,rgAwareOfGovtScheme;
    RadioGroup rgHaveMotherCard,rgWhatToDoInInfectation;

    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    PW_Baseline pw_baseline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_women_baseline3);
        init();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((RadioButton)findViewById(rgHandWashing.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHandWashing(((RadioButton)findViewById(rgHandWashing.getCheckedRadioButtonId())).getText().toString());
                }
                if(((RadioButton)findViewById(rgHandWashingWhen.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHandWashingWhen(((RadioButton)findViewById(rgHandWashingWhen.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgHowDiarrheaPrevented.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHowDiarrheaPrevented(((RadioButton)findViewById(rgHowDiarrheaPrevented.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgSourceOfWater.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setSourceOfWater(((RadioButton)findViewById(rgSourceOfWater.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgBoilWaterBeforeDrinking.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setBoilWaterBeforeDrinking(((RadioButton)findViewById(rgBoilWaterBeforeDrinking.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgHaveToiletAtHome.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHaveToiletAtHome(((RadioButton)findViewById(rgHaveToiletAtHome.getCheckedRadioButtonId())).getText().toString());
                }
                if(((RadioButton)findViewById(rgKeepSurroundingClean.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setKeepSurroundingClean(((RadioButton)findViewById(rgKeepSurroundingClean.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgHeardAneamia.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHeardAneamia(((RadioButton)findViewById(rgHeardAneamia.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgNutrientDeficientInAneamia.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setNutrientDeficientInAneamia(((RadioButton)findViewById(rgNutrientDeficientInAneamia.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgCauseOfAneamia.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setCauseOfAneamia(((RadioButton)findViewById(rgCauseOfAneamia.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgItemEatAtHome.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setItemEatAtHome(((RadioButton)findViewById(rgItemEatAtHome.getCheckedRadioButtonId())).getText().toString());
                } if(((RadioButton)findViewById(rgConsumeVegOrNonVeg.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setConsumeVegOrNonVeg(((RadioButton)findViewById(rgConsumeVegOrNonVeg.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgImportantOfChildNutrition.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setImportantOfChildNutrition(((RadioButton)findViewById(rgImportantOfChildNutrition.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgDifferentFoodImportant.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setDifferentFoodImportant(((RadioButton)findViewById(rgDifferentFoodImportant.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgVisitAnganwadiCenter.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setVisitAnganwadiCenter(((RadioButton)findViewById(rgVisitAnganwadiCenter.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgAttendMeeting.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setAttendMeeting(((RadioButton)findViewById(rgAttendMeeting.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgAwareOfGovtScheme.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setAwareOfGovtScheme(((RadioButton)findViewById(rgAwareOfGovtScheme.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgHaveMotherCard.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHaveMotherCard(((RadioButton)findViewById(rgHaveMotherCard.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgWhatToDoInInfectation.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setWhatToDoInInfection(((RadioButton)findViewById(rgWhatToDoInInfectation.getCheckedRadioButtonId())).getText().toString());
                }

                String last_id = sqliteHelper.getLast_inserted_id("pregnant_women_baseline");


                long inserted_id = sqliteHelper.updatePregnantBaseline2(pw_baseline,last_id);
                if(inserted_id>0)
                {
                    Toast.makeText(getApplicationContext(),
                            "Data saved successfully", Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(PregnantWomenBaseline3.this,MainMenuActivity.class);

                    startActivity(intent);
                }
            }
        });

    }

    private void init() {
        //RadioGroup
        rgHandWashing = findViewById(R.id.rgHandWashing);
        rgHandWashingWhen = findViewById(R.id.rgHandWashingWhen);
        rgHowDiarrheaPrevented = findViewById(R.id.rgHowDiarrheaPrevented);
        rgSourceOfWater = findViewById(R.id.rgSourceOfWater);
        rgBoilWaterBeforeDrinking = findViewById(R.id.rgBoilWaterBeforeDrinking);
        rgHaveToiletAtHome = findViewById(R.id.rgHaveToiletAtHome);
        rgKeepSurroundingClean = findViewById(R.id.rgKeepSurroundingClean);
        rgHeardAneamia = findViewById(R.id.rgHeardAneamia);
        rgNutrientDeficientInAneamia = findViewById(R.id.rgNutrientDeficientInAneamia);
        rgCauseOfAneamia = findViewById(R.id.rgCauseOfAneamia);
        rgItemEatAtHome = findViewById(R.id.rgItemEatAtHome);
        rgConsumeVegOrNonVeg = findViewById(R.id.rgConsumeVegOrNonVeg);
        rgImportantOfChildNutrition = findViewById(R.id.rgImportantOfChildNutrition);
        rgDifferentFoodImportant = findViewById(R.id.rgDifferentFoodImportant);
        rgVisitAnganwadiCenter = findViewById(R.id.rgVisitAnganwadiCenter);
        rgAttendMeeting = findViewById(R.id.rgAttendMeeting);
        rgAwareOfGovtScheme = findViewById(R.id.rgAwareOfGovtScheme);
        rgHaveMotherCard = findViewById(R.id.rgHaveMotherCard);
        rgWhatToDoInInfectation = findViewById(R.id.rgWhatToDoInInfectation);
        //TextView
        txtHandWashing = findViewById(R.id.txtHandWashing);
        txtHandWashingWhen = findViewById(R.id.txtHandWashingWhen);
        txtHowDiarrheaPrevented = findViewById(R.id.txtHowDiarrheaPrevented);
        txtSourceOfWater = findViewById(R.id.txtSourceOfWater);
        txtBoilWaterBeforeDrinking = findViewById(R.id.txtBoilWaterBeforeDrinking);
        txtHaveToiletAtHome = findViewById(R.id.txtHaveToiletAtHome);
        txtKeepSurroundingClean = findViewById(R.id.txtKeepSurroundingClean);
        txtHeardAneamia = findViewById(R.id.txtHeardAneamia);
        txtNutrientDeficientInAneamia = findViewById(R.id.txtNutrientDeficientInAneamia);
        txtCauseOfAneamia = findViewById(R.id.txtCauseOfAneamia);
        txtItemEatAtHome = findViewById(R.id.txtItemEatAtHome);
        txtConsumeVegOrNonVeg = findViewById(R.id.txtConsumeVegOrNonVeg);
        txtImportantOfChildNutrition = findViewById(R.id.txtImportantOfChildNutrition);
        txtDifferentFoodImportant = findViewById(R.id.txtDifferentFoodImportant);
        txtVisitAnganwadiCenter = findViewById(R.id.txtVisitAnganwadiCenter);
        txtAttendMeeting = findViewById(R.id.txtAttendMeeting);
        txtAwareOfGovtScheme = findViewById(R.id.txtAwareOfGovtScheme);
        txtHaveMotherCard = findViewById(R.id.txtHaveMotherCard);
        txtWhatToDoInInfectation = findViewById(R.id.txtWhatToDoInInfectation);

        //Button
        submitButton = findViewById(R.id.submitButton);
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        pw_baseline = new PW_Baseline();

    }
}
