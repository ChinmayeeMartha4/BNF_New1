package com.example.mhealth;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.AdolBaseline;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class Adolscent_Baseline2 extends AppCompatActivity {

    TextView txtIronRichFood,txtMoreIronNeeds,txtYouAreAneamic,txtHowSeriousAneamia,txtGetIronTablet,txtHowTakeIronTablet,txtLikeIronTablet;
    TextView txtFoodTypeConsume,txtConsumePastWeek,txtFrequencyOfConsumption,txtPeas,txtMeat,txtSeafood,txtEgg,txtGreenleaf,txtAlmonds;
    TextView txtIncludeLemonInDiet,txtDewormingTablet,txtFrequentlyAlbandazoleTablet,txtHadCheckedHBBefore,txtHB;

    EditText etxtPeas,etxtSeafood,etxtMeat,etxtGreenleaf,etxtEgg,etxtAlmonds,etxtHb;

    RadioGroup rgIronRichFood,rgMoreIronNeeds,rgYouAreAneamic,rgHowSeriousAneamia,rgGetIronTablet,rgHowTakeIronTablet,rgLikeIronTablet;
    RadioGroup rgFoodTypeConsume,rgConsumePastWeek,rgIncludeLemonInDiet,rgDewormingTablet,rgFrequentlyAlbandazoleTablet,rgHadCheckedHBBefore;

    RadioButton rbGreenVeg,rbSproutedPulses,rbMeat,rbAllCorrect,rbnoKnow,rbIronYes,rbIronNo,rbIronDontNo,rbanemicYes,rbanemicNo,rbAnemiaDontNo,rbVerySerious,rbNotSoSerious,rbNotSoSeriousAtall,rbGettingTabYes,rbGettingTabNo,rbOnceInWeek,rbOnceInFortnight,rbOnceInMonth,rbNotApplicable,rbVeruMuch,rbNotSoMuch,rbDontLike,rbnotApplicable,rbVegitarian,rbNonVegitarian,rbBoth,rbYesLemon,rbNoLemon,rbDewormingYes,rbDewormingNo,rbtakingAlbenzoYearly,rbtakingAlbenzoTwiceYearly,rbtakingAlbenzoDontKnow,rbHbYes,rbHbNo;
    Button submitButton;
    CheckBox pulses,almonds,meat,eggs,other,greenleaf,peas,seafood;
    SharedPrefHelper sharedPrefHelper;
    SqliteHelper sqliteHelper;
    ArrayList<AdolBaseline> adolBaselines;
    String ids;

    AdolBaseline adolBaseline,adolBaselinePage1;
    ArrayList<String> consumingFood=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adolscent__baseline2);


        init();

         ids= sharedPrefHelper.getString("id","");
       // Bundle bundle=getIntent().getExtras();
        if (ids!=null){
            //id = bundle.getString("id","");
            adolBaselines = sqliteHelper.getBaselineDataEdit(ids);
            if (adolBaselines.size() > 0) {

                for (int i = 0; i < adolBaselines.size(); i++) {
                  etxtPeas.setText(adolBaselines.get(i).getPeas());
                  etxtSeafood.setText(adolBaselines.get(i).getSeafood());
                  etxtMeat.setText(adolBaselines.get(i).getMeat());
                  etxtEgg.setText(adolBaselines.get(i).getEgg());
                  etxtGreenleaf.setText(adolBaselines.get(i).getGreenleaf());
                  etxtAlmonds.setText(adolBaselines.get(i).getAlmonds());
                  etxtHb.setText(adolBaselines.get(i).getHB());

                  String ironrichfood=adolBaselines.get(i).getIronRichFood();
                  if (ironrichfood.contains("Green leafy vegitables") && ironrichfood!=null){
                      rbGreenVeg.setChecked(true);
                  }else   if (ironrichfood.contains("Sprouted pulses") && ironrichfood!=null){
                      rbSproutedPulses.setChecked(true);
                  }else   if (ironrichfood.contains("Meat/Poultry") && ironrichfood!=null){
                      rbMeat.setChecked(true);
                  }else   if (ironrichfood.contains("All are correct") && ironrichfood!=null){
                      rbAllCorrect.setChecked(true);
                  }else   if (ironrichfood.contains("Dnot know") && ironrichfood!=null){
                      rbDontLike.setChecked(true);
                  }
                  String ironneeds = adolBaselines.get(i).getMoreIronNeeds();
                  if (ironneeds.contains("Yes") && ironneeds!=null){

                      rbIronYes.setChecked(true);
                  }else if (ironneeds.contains("No") && ironneeds!=null){

                      rbIronNo.setChecked(true);
                  }else if (ironneeds.contains("Dnot Know") && ironneeds!=null){

                      rbIronNo.setChecked(true);
                  }

                  String thinkAnemic=adolBaselines.get(i).getYouAreAneamic();
                  if (thinkAnemic.contains("Yes") && thinkAnemic!=null){

                      rbanemicYes.setChecked(true);
                  }else  if (thinkAnemic.contains("No") && thinkAnemic!=null){

                      rbanemicNo.setChecked(true);
                  }else  if (thinkAnemic.contains("Dnot Know") && thinkAnemic!=null){

                      rbAnemiaDontNo.setChecked(true);
                  }

                  String  seriousAnemia=adolBaselines.get(i).getHowSeriousAneamia();

                  if (seriousAnemia.contains("Very serious") && seriousAnemia!=null){

                      rbVerySerious.setChecked(true);
                  }else if (seriousAnemia.contains("Not so serious") && seriousAnemia!=null){

                      rbNotSoSerious.setChecked(true);
                  }else if (seriousAnemia.contains("Not Serious at all") && seriousAnemia!=null){

                      rbNotSoSeriousAtall.setChecked(true);
                  }

                  String ironTablets=adolBaselines.get(i).getGetIronTablet();
                  if (ironTablets.contains("Yes") && ironTablets!=null){
                      rbGettingTabYes.setChecked(true);
                  }else  if (ironTablets.contains("No") && ironTablets!=null){
                      rbGettingTabNo.setChecked(true);
                  }
                  String takeIronTab=adolBaselines.get(i).getHowTakeIronTablet();
                  if (takeIronTab.contains("Once in a week") && takeIronTab!=null){

                      rbOnceInWeek.setChecked(true);
                  }else  if (takeIronTab.contains("Once in a fortnight") && takeIronTab!=null){

                      rbOnceInFortnight.setChecked(true);
                  }else  if (takeIronTab.contains("Once in a month") && takeIronTab!=null){

                      rbOnceInMonth.setChecked(true);
                  }else  if (takeIronTab.contains("Not Applicable") && takeIronTab!=null){

                      rbNotApplicable.setChecked(true);
                  }
                  String likeIrontab=adolBaselines.get(i).getLikeIronTablet();
                  if (likeIrontab.contains("Very much") && likeIrontab!=null){
                      rbVeruMuch.setChecked(true);
                  }else  if (likeIrontab.contains("Not so much") && likeIrontab!=null){
                      rbNotSoMuch.setChecked(true);
                  }else  if (likeIrontab.contains("I dnot like") && likeIrontab!=null){
                      rbDontLike.setChecked(true);
                  }else  if (likeIrontab.contains("Not Applicable") && likeIrontab!=null){
                      rbNotApplicable.setChecked(true);
                  }

                  String foodConsume=adolBaselines.get(i).getFoodTypeConsume();
                  if (foodConsume.contains("Vegetarian") && foodConsume!=null){
                      rbVegitarian.setChecked(true);

                  }else if (foodConsume.contains("Non vegetarian") && foodConsume!=null){
                      rbNonVegitarian.setChecked(true);

                  }else if (foodConsume.contains("Both") && foodConsume!=null){
                      rbBoth.setChecked(true);

                  }
                  String lemon=adolBaselines.get(i).getIncludeLemonInDiet();
                  if (lemon.contains("Yes") && lemon!=null){

                      rbYesLemon.setChecked(true);
                  }else  if (lemon.contains("No") && lemon!=null){

                      rbNoLemon.setChecked(true);
                  }
                    String deworming=adolBaselines.get(i).getDewormingTablet();
                    if (deworming.contains("Yes") && deworming!=null){

                        rbDewormingYes.setChecked(true);
                    }else  if (deworming.contains("No") && deworming!=null){

                        rbDewormingNo.setChecked(true);
                    }



                    String albendazole=adolBaselines.get(i).getFrequentlyAlbandazoleTablet();
                  if (albendazole.contains("Once yearly") && albendazole!=null){

                      rbtakingAlbenzoYearly.setChecked(true);
                  }else  if (albendazole.contains("Twice yearly") && albendazole!=null){

                      rbtakingAlbenzoTwiceYearly.setChecked(true);
                  }else  if (albendazole.contains("Dont know") && albendazole!=null){

                      rbtakingAlbenzoDontKnow.setChecked(true);
                  }

                  String hb=adolBaselines.get(i).getHadCheckedHBBefore();
                  if (hb.contains("Yes") && hb!=null){

                      rbHbYes.setChecked(true);
                  }else  if (hb.contains("No") && hb!=null){

                      rbHbNo.setChecked(true);
                  }

                  String peasBeans=adolBaselines.get(i).getConsumePastWeek();
                  if (peasBeans.contains("Peas/Beans and Pulses") && peasBeans!=null){
                      peas.setChecked(true);

                  }
                //  String seafoodcheck =adolBaselines.get(i).getSeafood();
                  if (peasBeans.contains("Seafood") && peasBeans!=null){

                      seafood.setChecked(true);
                  }

                  //String meatcheck=adolBaselines.get(i).getMeat();
                  if (peasBeans.contains("Meat") && peasBeans!=null){

                      meat.setChecked(true);
                  }

              //    String eggcheck =adolBaselines.get(i).getEgg();

                  if (peasBeans.contains("Eggs") && peasBeans!=null){

                      eggs.setChecked(true);
                  }

              //    String greenLeaf=adolBaselines.get(i).getGreenleaf();
                  if (peasBeans.contains("Green Leaf vegetables") && peasBeans!=null){

                      greenleaf.setChecked(true);
                  }

                //  String alomonds=adolBaselines.get(i).getAlmonds();
                  if (peasBeans.contains("Almonds and Nuts") && peasBeans!=null){

                      almonds.setChecked(true);
                  }




                }
            }

        }






        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (((RadioButton) findViewById(rgIronRichFood.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setIronRichFood(((RadioButton) findViewById(rgIronRichFood.getCheckedRadioButtonId())).getText().toString());

                } else {
                    adolBaseline.setIronRichFood("");
                }

                if (((RadioButton) findViewById(rgMoreIronNeeds.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setMoreIronNeeds(((RadioButton) findViewById(rgMoreIronNeeds.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setMoreIronNeeds("");
                }
                if (((RadioButton) findViewById(rgYouAreAneamic.getCheckedRadioButtonId())) != null) {


                    adolBaseline.setYouAreAneamic(((RadioButton) findViewById(rgYouAreAneamic.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setYouAreAneamic("");
                }

                if (((RadioButton) findViewById(rgHowSeriousAneamia.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setHowSeriousAneamia(((RadioButton) findViewById(rgHowSeriousAneamia.getCheckedRadioButtonId())).getText().toString());

                } else {
                    adolBaseline.setHowSeriousAneamia("");
                }
                if (((RadioButton) findViewById(rgGetIronTablet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setGetIronTablet(((RadioButton) findViewById(rgGetIronTablet.getCheckedRadioButtonId())).getText().toString());

                } else {


                    adolBaseline.setGetIronTablet("");
                }

                if (((RadioButton) findViewById(rgHowTakeIronTablet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setHowTakeIronTablet(((RadioButton) findViewById(rgHowTakeIronTablet.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setHowTakeIronTablet("");
                }
                if (((RadioButton) findViewById(rgLikeIronTablet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setLikeIronTablet(((RadioButton) findViewById(rgLikeIronTablet.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setLikeIronTablet("");
                }

                if (((RadioButton) findViewById(rgFoodTypeConsume.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setFoodTypeConsume(((RadioButton) findViewById(rgFoodTypeConsume.getCheckedRadioButtonId())).getText().toString());
                } else {

                    adolBaseline.setFoodTypeConsume("");
                }
                if (peas.isChecked()) {
                    consumingFood.add(peas.getText().toString());

                } else {
                    consumingFood.remove(peas.getText().toString());

                }

                if (almonds.isChecked()) {

                    consumingFood.add(almonds.getText().toString());
                } else {
                    consumingFood.remove(almonds.getText().toString());

                }

                if (meat.isChecked()) {

                    consumingFood.add(meat.getText().toString());
                } else {
                    consumingFood.remove(meat.getText().toString());


                }

                if (eggs.isChecked()) {
                    consumingFood.add(eggs.getText().toString());

                } else {

                    consumingFood.remove(eggs.getText().toString());
                }

                if (other.isChecked()) {

                    consumingFood.add(other.getText().toString());
                } else {

                    consumingFood.remove(other.getText().toString());
                }

                if (greenleaf.isChecked()) {

                    consumingFood.add(greenleaf.getText().toString());
                } else {

                    consumingFood.remove(greenleaf.getText().toString());
                }

                if (seafood.isChecked()) {

                    consumingFood.add(seafood.getText().toString());
                } else {
                    consumingFood.remove(seafood.getText().toString());

                }

                String listString = "";

                for (String s : consumingFood) {
                    listString += s + ",";
                }
                adolBaseline.setConsumePastWeek(listString);


//                }if(((RadioButton)findViewById(rgConsumePastWeek.getCheckedRadioButtonId()))!=null)
//                {
//                    adolBaseline.setConsumePastWeek(((RadioButton)findViewById(rgConsumePastWeek.getCheckedRadioButtonId())).getText().toString());
//
//                }
                if (((RadioButton) findViewById(rgIncludeLemonInDiet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setIncludeLemonInDiet(((RadioButton) findViewById(rgIncludeLemonInDiet.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setIncludeLemonInDiet("");
                }
                if (((RadioButton) findViewById(rgDewormingTablet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setDewormingTablet(((RadioButton) findViewById(rgDewormingTablet.getCheckedRadioButtonId())).getText().toString());

                } else {
                    adolBaseline.setDewormingTablet("");
                }
                if (((RadioButton) findViewById(rgFrequentlyAlbandazoleTablet.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setFrequentlyAlbandazoleTablet(((RadioButton) findViewById(rgFrequentlyAlbandazoleTablet.getCheckedRadioButtonId())).getText().toString());

                } else {

                    adolBaseline.setFrequentlyAlbandazoleTablet("");
                }
                if (((RadioButton) findViewById(rgHadCheckedHBBefore.getCheckedRadioButtonId())) != null) {
                    adolBaseline.setHadCheckedHBBefore(((RadioButton) findViewById(rgHadCheckedHBBefore.getCheckedRadioButtonId())).getText().toString());

                } else {
                    adolBaseline.setHadCheckedHBBefore("");
                }
                adolBaseline.setHB(etxtHb.getText().toString());
                adolBaseline.setPeas(etxtPeas.getText().toString());
                adolBaseline.setMeat(etxtMeat.getText().toString());
                adolBaseline.setSeafood(etxtSeafood.getText().toString());
                adolBaseline.setEgg(etxtEgg.getText().toString());
                adolBaseline.setGreenleaf(etxtGreenleaf.getText().toString());
                adolBaseline.setAlmonds(etxtAlmonds.getText().toString());

                adolBaselinePage1 = (AdolBaseline) getIntent().getSerializableExtra("page1to2");

                //  String last_id = sqliteHelper.getLast_inserted_id("adolscent_baseline");

                if (!ids.equals("")) {

                    long insertedd_id = sqliteHelper.updateAdolBaselinee(adolBaseline, adolBaselinePage1, ids);
                    if (insertedd_id > 0) {
                        Toast.makeText(getApplicationContext(),
                                "Data saved successfully", Toast.LENGTH_LONG)
                                .show();

                        Intent intent = new Intent(Adolscent_Baseline2.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                } else {
                    long inserted_id = sqliteHelper.updateAdolBaseline(adolBaseline, adolBaselinePage1);
                    if (inserted_id > 0) {
                        Toast.makeText(getApplicationContext(),
                                "Data saved successfully", Toast.LENGTH_LONG)
                                .show();
                        sharedPrefHelper.setString("id", "");
                        Intent intent = new Intent(Adolscent_Baseline2.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void init() {
        //BUTTON
        submitButton = findViewById(R.id.submitButton);

        //RADIO GROUPS
        rgIronRichFood = findViewById(R.id.rgIronRichFood);
        adolBaselines=new ArrayList<>();
        rgMoreIronNeeds = findViewById(R.id.rgMoreIronNeeds);
        rgYouAreAneamic = findViewById(R.id.rgYouAreAneamic);
        rgHowSeriousAneamia = findViewById(R.id.rgHowSeriousAneamia);
        rgGetIronTablet = findViewById(R.id.rgGetIronTablet);
        rgHowTakeIronTablet = findViewById(R.id.rgHowTakeIronTablet);
        rgLikeIronTablet = findViewById(R.id.rgLikeIronTablet);
        rgFoodTypeConsume = findViewById(R.id.rgFoodTypeConsume);
        peas=findViewById(R.id.cb_beansandpulses);
        other=findViewById(R.id.cb_other);
        almonds=findViewById(R.id.cb_almonds);
        meat=findViewById(R.id.cb_meat);
        eggs=findViewById(R.id.cb_eggs);
        seafood=findViewById(R.id.cb_seafood);
        greenleaf=findViewById(R.id.cb_greenleaf);

      //  rgConsumePastWeek = findViewById(R.id.rgConsumePastWeek);
        rgIncludeLemonInDiet = findViewById(R.id.rgIncludeLemonInDiet);
        rgDewormingTablet = findViewById(R.id.rgDewormingTablet);
        rgFrequentlyAlbandazoleTablet = findViewById(R.id.rgFrequentlyAlbandazoleTablet);
        rgHadCheckedHBBefore = findViewById(R.id.rgHadCheckedHBBefore);


       //TEXTVIEWS
        txtIronRichFood = findViewById(R.id.txtIronRichFood);
        txtMoreIronNeeds = findViewById(R.id.txtMoreIronNeeds);
        txtYouAreAneamic = findViewById(R.id.txtYouAreAneamic);
        txtHowSeriousAneamia = findViewById(R.id.txtHowSeriousAneamia);
        txtGetIronTablet = findViewById(R.id.txtGetIronTablet);
        txtHowTakeIronTablet = findViewById(R.id.txtHowTakeIronTablet);
        txtLikeIronTablet = findViewById(R.id.txtLikeIronTablet);
        txtFoodTypeConsume = findViewById(R.id.txtFoodTypeConsume);
        txtConsumePastWeek = findViewById(R.id.txtConsumePastWeek);
        txtFrequencyOfConsumption = findViewById(R.id.txtFrequencyOfConsumption);
        txtPeas = findViewById(R.id.txtPeas);
        txtMeat = findViewById(R.id.txtMeat);
        txtEgg = findViewById(R.id.txtEgg);
        txtGreenleaf = findViewById(R.id.txtGreenleaf);
        txtSeafood = findViewById(R.id.txtSeafood);
        txtAlmonds = findViewById(R.id.txtAlmonds);
        txtIncludeLemonInDiet = findViewById(R.id.txtIncludeLemonInDiet);
        txtDewormingTablet = findViewById(R.id.txtDewormingTablet);
        txtFrequentlyAlbandazoleTablet = findViewById(R.id.txtFrequentlyAlbandazoleTablet);
        txtHadCheckedHBBefore = findViewById(R.id.txtHadCheckedHBBefore);
        txtHB = findViewById(R.id.txtHB);

        //EDITTEXTS
        etxtPeas = findViewById(R.id.etxtPeas);
        etxtSeafood = findViewById(R.id.etxtSeafood);
        etxtMeat = findViewById(R.id.etxtMeat);
        etxtEgg = findViewById(R.id.etxtEgg);
        etxtGreenleaf = findViewById(R.id.etxtGreenleaf);
        etxtAlmonds = findViewById(R.id.etxtAlmonds);
        etxtHb = findViewById(R.id.etxtHB);

        //RadioButton
        rbGreenVeg =findViewById(R.id.rbGreenVeg);
        rbSproutedPulses =findViewById(R.id.rbSproutedPulses);
        rbMeat=findViewById(R.id.rbMeat);
        rbAllCorrect=findViewById(R.id.rbAllCorrect);
        rbnoKnow=findViewById(R.id.rbnoKnow);
        rbIronYes=findViewById(R.id.rbIronYes);
        rbIronNo=findViewById(R.id.rbIronNo);
        rbIronDontNo=findViewById(R.id.rbIronDontNo);
        rbanemicYes=findViewById(R.id.rbanemicYes);
        rbanemicNo=findViewById(R.id.rbanemicNo);
        rbAnemiaDontNo=findViewById(R.id.rbAnemiaDontNo);
        rbVerySerious=findViewById(R.id.rbVerySerious);
        rbNotSoSerious=findViewById(R.id.rbNotSoSerious);
        rbNotSoSeriousAtall=findViewById(R.id.rbNotSoSeriousAtall);
        rbGettingTabYes=findViewById(R.id.rbGettingTabYes);
        rbGettingTabNo=findViewById(R.id.rbGettingTabNo);
        rbOnceInWeek=findViewById(R.id.rbOnceInWeek);
        rbOnceInFortnight=findViewById(R.id.rbOnceInFortnight);
        rbOnceInMonth=findViewById(R.id.rbOnceInMonth);
        rbNotApplicable=findViewById(R.id.rbNotApplicable);
        rbVeruMuch=findViewById(R.id.rbVeruMuch);
        rbNotSoMuch=findViewById(R.id.rbNotSoMuch);
        rbDontLike=findViewById(R.id.rbDontLike);
        rbnotApplicable=findViewById(R.id.rbnotApplicable);
        rbVegitarian=findViewById(R.id.rbVegitarian);
        rbNonVegitarian=findViewById(R.id.rbNonVegitarian);
        rbBoth=findViewById(R.id.rbBoth);
        rbYesLemon=findViewById(R.id.rbYesLemon);
        rbNoLemon=findViewById(R.id.rbNoLemon);
        rbDewormingYes=findViewById(R.id.rbDewormingYes);
        rbDewormingNo=findViewById(R.id.rbDewormingNo);
        rbtakingAlbenzoYearly=findViewById(R.id.rbtakingAlbenzoYearly);
        rbtakingAlbenzoTwiceYearly=findViewById(R.id.rbtakingAlbenzoTwiceYearly);
        rbtakingAlbenzoDontKnow=findViewById(R.id.rbtakingAlbenzoDontKnow);
        rbHbYes=findViewById(R.id.rbHbYes);
        rbHbNo=findViewById(R.id.rbHbNo);


       sqliteHelper = new SqliteHelper(this);
       sharedPrefHelper = new SharedPrefHelper(this);
       adolBaseline = new AdolBaseline();
       adolBaselinePage1 = new AdolBaseline();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // sharedPrefHelper.setString("id","");
    }
}
