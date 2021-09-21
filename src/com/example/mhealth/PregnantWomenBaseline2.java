package com.example.mhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mhealth.helper.PW_Baseline;
import com.example.mhealth.helper.SqliteHelper;

public class PregnantWomenBaseline2 extends AppCompatActivity {
    TextView txtBreastFeedingInitiated,txtHowLongBreastFeeding,txtWhenStartFood,txtDecesionToStartFeeding,txtBreastFeedingGivenDuring;
    TextView txtFirstDoseOfTetnus,txtSecondDoseOfTetnus,txtBoosterTetnus,txtVaccinesToBeGiven,txtIronSupplementImportant,txtTrimesterOfDeworming;
    TextView txtOftenTakeIronTablet,txtIronRichFood,txtIronTabletFromGovt;

    RadioGroup rgBreastFeedingInitiated,rgHowLongBreastFeeding,rgWhenStartFood,rgDecesionToStartFeeding,rgBreastFeedingGivenDuring;
    RadioGroup rgFirstDoseOfTetnus,rgSecondDoseOfTetnus,rgBoosterTetnus,rgVaccinesToBeGiven,rgIronSupplementImportant,rgTrimesterOfDeworming;
    RadioGroup rgOftenTakeIronTablet,rgIronRichFood,rgIronTabletFromGovt;

    Button nextButton;
    PW_Baseline pw_baseline;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_women_baseline2);
        init();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((RadioButton)findViewById(rgBreastFeedingInitiated.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setBreastFeedingInitiated(((RadioButton)findViewById(rgBreastFeedingInitiated.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgHowLongBreastFeeding.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setHowLongBreastFeeding(((RadioButton)findViewById(rgHowLongBreastFeeding.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgWhenStartFood.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setWhenStartFood(((RadioButton)findViewById(rgWhenStartFood.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgDecesionToStartFeeding.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setDecesionToStartFeeding(((RadioButton)findViewById(rgDecesionToStartFeeding.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgBreastFeedingGivenDuring.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setBreastFeedingGivenDuring(((RadioButton)findViewById(rgBreastFeedingGivenDuring.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgFirstDoseOfTetnus.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setFirstDoseOfTetnus(((RadioButton)findViewById(rgFirstDoseOfTetnus.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgSecondDoseOfTetnus.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setSecondDoseOfTetnus(((RadioButton)findViewById(rgSecondDoseOfTetnus.getCheckedRadioButtonId())).getText().toString());
                }

                if(((RadioButton)findViewById(rgBoosterTetnus.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setBoosterTetnus(((RadioButton)findViewById(rgBoosterTetnus.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgVaccinesToBeGiven.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setVaccinesToBeGiven(((RadioButton)findViewById(rgVaccinesToBeGiven.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgIronSupplementImportant.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setIronSupplementImportant(((RadioButton)findViewById(rgIronSupplementImportant.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgTrimesterOfDeworming.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setTrimesterOfDeworming(((RadioButton)findViewById(rgTrimesterOfDeworming.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgOftenTakeIronTablet.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setOftenTakeIronTablet(((RadioButton)findViewById(rgOftenTakeIronTablet.getCheckedRadioButtonId())).getText().toString());
                }if(((RadioButton)findViewById(rgIronRichFood.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setIronRichFood(((RadioButton)findViewById(rgIronRichFood.getCheckedRadioButtonId())).getText().toString());
                }
                if(((RadioButton)findViewById(rgIronTabletFromGovt.getCheckedRadioButtonId()))!=null)
                {
                    pw_baseline.setIronTabletFromGovt(((RadioButton)findViewById(rgIronTabletFromGovt.getCheckedRadioButtonId())).getText().toString());
                }

                String last_id = sqliteHelper.getLast_inserted_id("pregnant_women_baseline");


                long inserted_id = sqliteHelper.updatePregnantBaseline(pw_baseline,last_id);
                if(inserted_id>0)
                {
                    Intent intent = new Intent(PregnantWomenBaseline2.this,PregnantWomenBaseline3.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        pw_baseline = new PW_Baseline();
        sqliteHelper = new SqliteHelper(this);
        nextButton = findViewById(R.id.nextButton);
        //RADIOGROUPS
        rgBreastFeedingInitiated = findViewById(R.id.rgBreastFeedingInitiated);
        rgHowLongBreastFeeding = findViewById(R.id.rgHowLongBreastFeeding);
        rgWhenStartFood = findViewById(R.id.rgWhenStartFood);
        rgDecesionToStartFeeding = findViewById(R.id.rgDecesionToStartFeeding);
        rgBreastFeedingGivenDuring = findViewById(R.id.rgBreastFeedingGivenDuring);
        rgFirstDoseOfTetnus = findViewById(R.id.rgFirstDoseOfTetnus);
        rgSecondDoseOfTetnus = findViewById(R.id.rgSecondDoseOfTetnus);
        rgBoosterTetnus = findViewById(R.id.rgBoosterTetnus);
        rgVaccinesToBeGiven = findViewById(R.id.rgVaccinesToBeGiven);
        rgIronSupplementImportant = findViewById(R.id.rgIronSupplementImportant);
        rgTrimesterOfDeworming = findViewById(R.id.rgTrimesterOfDeworming);
        rgOftenTakeIronTablet = findViewById(R.id.rgOftenTakeIronTablet);
        rgIronRichFood = findViewById(R.id.rgIronRichFood);
        rgIronTabletFromGovt = findViewById(R.id.rgIronTabletFromGovt);

        //TEXTVIEWS
        txtBreastFeedingInitiated = findViewById(R.id.txtBreastFeedingInitiated);
        txtHowLongBreastFeeding = findViewById(R.id.txtHowLongBreastFeeding);
        txtWhenStartFood = findViewById(R.id.txtWhenStartFood);
        txtDecesionToStartFeeding = findViewById(R.id.txtDecesionToStartFeeding);
        txtBreastFeedingGivenDuring = findViewById(R.id.txtBreastFeedingGivenDuring);
        txtFirstDoseOfTetnus = findViewById(R.id.txtFirstDoseOfTetnus);
        txtSecondDoseOfTetnus = findViewById(R.id.txtSecondDoseOfTetnus);
        txtBoosterTetnus = findViewById(R.id.txtBoosterTetnus);
        txtVaccinesToBeGiven = findViewById(R.id.txtVaccinesToBeGiven);
        txtIronSupplementImportant = findViewById(R.id.txtIronSupplementImportant);
        txtTrimesterOfDeworming = findViewById(R.id.txtTrimesterOfDeworming);
        txtOftenTakeIronTablet = findViewById(R.id.txtOftenTakeIronTablet);
        txtIronRichFood = findViewById(R.id.txtIronRichFood);
        txtIronTabletFromGovt = findViewById(R.id.txtIronTabletFromGovt);



    }

}
