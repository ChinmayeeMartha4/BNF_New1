package com.example.mhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mhealth.helper.AdolBaseline;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class Adolscent_Baseline extends AppCompatActivity {
    AdolBaseline adolBaseline;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sharedPrefHelper;
    LinearLayout sourceofInfo;
    ArrayList<AdolBaseline> adolBaselines;

    TextView txtAdolName,txtAddress,txtParentName,txtContactNumber,txtHeight,txtWeight,txtAge,txtMUAC,txtSchoolName;
    TextView txtClass,txtUniqueId,txtVillageName,txtBlockName,txtDistrictName;
    TextView txtHeardAneamia,txtSourceOfInfo,txtWhatIsAneamia,txtWhichNutrientDef,txtCauseOfAneamia,txtSignsOfAneamia,txtEffectOfAneamia,txtMeasuresOfAneamia;

    EditText etxtAdolName,etxtAddress,etxtParentName,etxtContactNumber,etxtHeight,etxtWeight,etxtAge,etxtMUAC,etxtSchoolName;
    EditText etxtClass,etxtUniqueId,etxtVillageName,etxtBlockName,etxtDistrictName;

    RadioGroup rgHeardAneamia,rgSourceOfInfo,rgWhatIsAneamia,rgWhichNutrientDef,rgCauseOfAneamia,rgSignsOfAneamia,rgEffectOfAneamia,rgMeasuresOfAneamia;

    RadioButton rbHeardAneamiaYes,rbHeardAneamiaNo,rbSchoolTeacher,rbDoctor,rbFamily,rbFriends,rbOther;
    RadioButton rbIncRBC,rbDecRBC,rbDontKnow,rbIOdine,rbIron,rbCalcium,rbNotKnow,rbWorm,rbPoorNutrition,rbIncBloodLoss,rbAllTheAbove,rbKnownNot;
    RadioButton rbShortnessBreath,rbTiredness,rbAll,rbKnownDont,rbSlowRateGrowth,rbLowerConcStudies,rbGettingTiredEasily,rbAllAbove,rbKnownNo;
    RadioButton rbConsumeNutriFood,rbMaintainigPersonnelHygine,rbTFATablets,rbAllTheAboveMeasures,rbDontKnowMeasures;

    Button nextButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adolscent__baseline);

        init();
        String id = null;
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            id = bundle.getString("id","");
            sharedPrefHelper.setString("id",id);

            adolBaselines = sqliteHelper.getBaselineDataEdit(id);
            if (adolBaselines.size() > 0) {

                for (int i = 0; i < adolBaselines.size(); i++) {
                    etxtAdolName.setText(adolBaselines.get(i).getAdolName());
                    etxtAddress.setText(adolBaselines.get(i).getAddress());
                    etxtParentName.setText(adolBaselines.get(i).getParentName());
                    etxtContactNumber.setText(adolBaselines.get(i).getContactNumber());
                    etxtHeight.setText(adolBaselines.get(i).getHeight());
                    etxtWeight.setText(adolBaselines.get(i).getWeight());
                    etxtAge.setText(adolBaselines.get(i).getAge());
                    etxtMUAC.setText(adolBaselines.get(i).getMUAC());
                    etxtSchoolName.setText(adolBaselines.get(i).getSchoolName());
                    etxtClass.setText(adolBaselines.get(i).getClassName());
                    etxtUniqueId.setText(adolBaselines.get(i).getUniqueId());
                    etxtVillageName.setText(adolBaselines.get(i).getVillageName());
                    etxtBlockName.setText(adolBaselines.get(i).getBlockName());
                    etxtDistrictName.setText(adolBaselines.get(i).getDistrictName());

                    String heardAnemaia=adolBaselines.get(i).getHeardOfAneamia();
                    if (heardAnemaia.contains("Yes") && heardAnemaia!=null){

                        rbHeardAneamiaYes.setChecked(true);
                    }else if (heardAnemaia.contains("No") && heardAnemaia!=null){

                        rbHeardAneamiaNo.setChecked(true);

                    }

                    String sourceOfInfo=adolBaselines.get(i).getSourceOfInfo();
                    if (sourceOfInfo.contains("School Teacher") && sourceOfInfo!=null) {

                        rbSchoolTeacher.setChecked(true);
                    }else if (sourceOfInfo.contains("Doctor/Health Professional") && sourceOfInfo != null) {

                            rbDoctor.setChecked(true);
                        } else if (sourceOfInfo.contains("Family Members") && sourceOfInfo != null) {

                            rbFamily.setChecked(true);
                        } else if (sourceOfInfo.contains("Friends/Neighbours") && sourceOfInfo != null) {

                            rbFriends.setChecked(true);
                        } else if (sourceOfInfo.contains("Other") && sourceOfInfo != null) {

                            rbOther.setChecked(true);
                        }

                    String whatisAnemia = adolBaselines.get(i).getWhatIsAneamia();
                    if (whatisAnemia.contains("Increased red blood cells in blood") && whatisAnemia!=null){
                        rbIncRBC.setChecked(true);

                    }else if (whatisAnemia.contains("Decreased red blood cells in blood") && whatisAnemia!=null){

                        rbDecRBC.setChecked(true);
                    }else if (whatisAnemia.contains("Dont know") && whatisAnemia!=null){

                        rbDontKnow.setChecked(true);
                    }

                    String deficeintAnemia = adolBaselines.get(i).getWhichNutrientDef();
                    if (deficeintAnemia.contains("Iodine") && deficeintAnemia!=null){
                        rbIOdine.setChecked(true);
                    }else if (deficeintAnemia.contains("Iron") && deficeintAnemia!=null){
                        rbIron.setChecked(true);

                    }else if (deficeintAnemia.contains("Calcium") && deficeintAnemia!=null){
                        rbCalcium.setChecked(true);

                    }else if (deficeintAnemia.contains("Dont know") && deficeintAnemia!=null){
                        rbKnownNot.setChecked(true);

                    }

                    String CauseAnemia = adolBaselines.get(i).getCauseOfAneamia();
                    if (CauseAnemia.contains("Worm infestation") && CauseAnemia!=null){
                        rbWorm.setChecked(true);
                    }else if (CauseAnemia.contains("Poor Nutrition") && CauseAnemia!=null){
                        rbPoorNutrition.setChecked(true);
                    }else if (CauseAnemia.contains("All the above") && CauseAnemia!=null){
                        rbAllTheAbove.setChecked(true);
                    }else if (CauseAnemia.contains("Dont know") && CauseAnemia!=null){
                        rbKnownNot.setChecked(true);
                    }else if (CauseAnemia.contains("Increased blood loss") && CauseAnemia!=null){
                        rbIncBloodLoss.setChecked(true);
                    }


                    String signAnemaia = adolBaselines.get(i).getSignsOfAneamia();
                    if (signAnemaia.contains("Shortness of breath") && signAnemaia!=null){

                        rbShortnessBreath.setChecked(true);
                    }else  if (signAnemaia.contains("Tiredness pale skin") && signAnemaia!=null){

                        rbTiredness.setChecked(true);
                    } else  if (signAnemaia.contains("All the above") && signAnemaia!=null){

                        rbAll.setChecked(true);
                    }else  if (signAnemaia.contains("Dnot know") && signAnemaia!=null){

                        rbKnownNo.setChecked(true);
                    }

                    String effectAnemaia = adolBaselines.get(i).getEffectsOfAneamia();
                    if (effectAnemaia.contains("Slow rate of growth") && effectAnemaia!=null){

                        rbSlowRateGrowth.setChecked(true);
                    }else  if (effectAnemaia.contains("Lower concentration in studies") && effectAnemaia!=null){

                        rbLowerConcStudies.setChecked(true);
                    }else  if (effectAnemaia.contains("Getting tired easily") && effectAnemaia!=null){

                        rbGettingTiredEasily.setChecked(true);
                    }else  if (effectAnemaia.contains("All the above") && effectAnemaia!=null){

                        rbAllAbove.setChecked(true);
                    }else  if (effectAnemaia.contains("Dont know") && effectAnemaia!=null){

                        rbKnownNot.setChecked(true);
                    }

                    String preventiveMeasure =adolBaselines.get(i).getMeasuresOfAneamia();
                    if (preventiveMeasure.contains("Consuming nutrition food") && preventiveMeasure!=null){

                        rbConsumeNutriFood.setChecked(true);
                    }else  if (preventiveMeasure.contains("Maintaining personnel hygiene") && preventiveMeasure!=null){

                        rbMaintainigPersonnelHygine.setChecked(true);
                    }else  if (preventiveMeasure.contains("Taking weekly IFA Tablets") && preventiveMeasure!=null){

                        rbTFATablets.setChecked(true);
                    }else  if (preventiveMeasure.contains("All the above") && preventiveMeasure!=null){

                        rbAllAbove.setChecked(true);
                    }else  if (preventiveMeasure.contains("Dont know") && preventiveMeasure!=null){

                        rbKnownNot.setChecked(true);
                    }
                }
            }

       }


        rgHeardAneamia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbHeardAneamiaNo.isChecked()){

                    sourceofInfo.setVisibility(View.GONE);
                }else if (rbHeardAneamiaYes.isChecked()){

                    sourceofInfo.setVisibility(View.VISIBLE);
                }
            }
        });



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkValidation()) {
                    adolBaseline.setAdolName(etxtAdolName.getText().toString());
                    adolBaseline.setAddress(etxtAddress.getText().toString());
                    adolBaseline.setParentName(etxtParentName.getText().toString());
                    adolBaseline.setContactNumber(etxtContactNumber.getText().toString());
                    adolBaseline.setHeight(etxtHeight.getText().toString());
                    adolBaseline.setWeight(etxtWeight.getText().toString());
                    adolBaseline.setAge(etxtAge.getText().toString());
                    adolBaseline.setMUAC(etxtMUAC.getText().toString());
                    adolBaseline.setSchoolName(etxtSchoolName.getText().toString());
                    adolBaseline.setClassName(etxtClass.getText().toString());
                    adolBaseline.setUniqueId(etxtUniqueId.getText().toString());
                    adolBaseline.setVillageName(etxtVillageName.getText().toString());
                    adolBaseline.setBlockName(etxtBlockName.getText().toString());
                    adolBaseline.setDistrictName(etxtDistrictName.getText().toString());
                    if (((RadioButton) findViewById(rgHeardAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setHeardOfAneamia(((RadioButton) findViewById(rgHeardAneamia.getCheckedRadioButtonId())).getText().toString());

                    }else {
                        adolBaseline.setHeardOfAneamia("");
                    }
                    if (((RadioButton) findViewById(rgSourceOfInfo.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setSourceOfInfo(((RadioButton) findViewById(rgSourceOfInfo.getCheckedRadioButtonId())).getText().toString());

                    }else {
                        adolBaseline.setSourceOfInfo("");
                    }
                    if (((RadioButton) findViewById(rgWhatIsAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setWhatIsAneamia(((RadioButton) findViewById(rgWhatIsAneamia.getCheckedRadioButtonId())).getText().toString());

                    }else {
                        adolBaseline.setWhatIsAneamia("");
                    }
                    if (((RadioButton) findViewById(rgWhichNutrientDef.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setWhichNutrientDef(((RadioButton) findViewById(rgWhichNutrientDef.getCheckedRadioButtonId())).getText().toString());

                    }else {

                        adolBaseline.setWhichNutrientDef("");
                    }
                    if (((RadioButton) findViewById(rgCauseOfAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setCauseOfAneamia(((RadioButton) findViewById(rgCauseOfAneamia.getCheckedRadioButtonId())).getText().toString());

                    }else {

                        adolBaseline.setCauseOfAneamia("");
                    }
                    if (((RadioButton) findViewById(rgSignsOfAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setSignsOfAneamia(((RadioButton) findViewById(rgSignsOfAneamia.getCheckedRadioButtonId())).getText().toString());

                    }else {

                        adolBaseline.setSignsOfAneamia("");
                    }
                    if (((RadioButton) findViewById(rgEffectOfAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setEffectsOfAneamia(((RadioButton) findViewById(rgEffectOfAneamia.getCheckedRadioButtonId())).getText().toString());

                    }else {

                        adolBaseline.setEffectsOfAneamia("");
                    }
                    if (((RadioButton) findViewById(rgMeasuresOfAneamia.getCheckedRadioButtonId())) != null) {
                        adolBaseline.setMeasuresOfAneamia(((RadioButton) findViewById(rgMeasuresOfAneamia.getCheckedRadioButtonId())).getText().toString());
                    }else {

                        adolBaseline.setMeasuresOfAneamia("");
                    }


                   // long inserted_id = sqliteHelper.setAdolscentBaseline(adolBaseline);

                   // if (inserted_id > 0) {



                    Intent intent = new Intent(Adolscent_Baseline.this, Adolscent_Baseline2.class);
                    intent.putExtra("page1to2", (Serializable) adolBaseline);
                    startActivity(intent);
                    //}
                }
            }
        });
    }
    private boolean checkValidation() {
        Boolean ret = true;
        if(etxtAdolName.getText().toString().equalsIgnoreCase(""))
        {
            etxtAdolName.setError("Adolescent Name Mandatory");
            ret = false;
        }
        if(etxtParentName.getText().toString().equalsIgnoreCase(""))
        {
            etxtParentName.setError("Parent Name Mandatory");
            ret = false;
        }if(etxtHeight.getText().toString().equalsIgnoreCase(""))
        {
            etxtHeight.setError("Height Mandatory");
            ret = false;
        }if(etxtWeight.getText().toString().equalsIgnoreCase(""))
        {
            etxtWeight.setError("Weight Mandatory");
            ret = false;
        }if(etxtAge.getText().toString().equalsIgnoreCase(""))
        {
            etxtAge.setError("Age Mandatory");
            ret = false;
        }
        return ret;
    }

    private void init() {
        adolBaseline = new AdolBaseline();
        sharedPrefHelper=new SharedPrefHelper(this);
        adolBaselines=new ArrayList<>();
        sqliteHelper = new SqliteHelper(this);
        sourceofInfo=findViewById(R.id.llSourceOfInfo);
        //RadioButtons
        rbHeardAneamiaYes = findViewById(R.id.rbHeardAneamiaYes);
        rbHeardAneamiaNo = findViewById(R.id.rbHeardAneamiaNo);
        rbSchoolTeacher = findViewById(R.id.rbSchoolTeacher);
        rbDoctor = findViewById(R.id.rbDoctor);
        rbFamily = findViewById(R.id.rbFamily);
        rbFriends = findViewById(R.id.rbFriends);
        rbOther = findViewById(R.id.rbOther);
        rbIncRBC = findViewById(R.id.rbIncRBC);
        rbDecRBC = findViewById(R.id.rbDecRBC);
        rbDontKnow = findViewById(R.id.rbDontKnow);
        rbIOdine = findViewById(R.id.rbIOdine);
        rbIron = findViewById(R.id.rbIron);
        rbCalcium = findViewById(R.id.rbCalcium);
        rbNotKnow = findViewById(R.id.rbNotKnow);
        rbPoorNutrition = findViewById(R.id.rbPoorNutrition);
        rbWorm = findViewById(R.id.rbWorm);
        rbIncBloodLoss = findViewById(R.id.rbIncBloodLoss);
        rbAllTheAbove = findViewById(R.id.rbAllTheAbove);
        rbKnownNot = findViewById(R.id.rbKnownNot);
        rbShortnessBreath = findViewById(R.id.rbShortnessBreath);
        rbTiredness = findViewById(R.id.rbTiredness);
        rbAll = findViewById(R.id.rbAll);
        rbKnownDont = findViewById(R.id.rbKnownDont);
        rbSlowRateGrowth = findViewById(R.id.rbSlowRateGrowth);
        rbLowerConcStudies = findViewById(R.id.rbLowerConcStudies);
        rbGettingTiredEasily = findViewById(R.id.rbGettingTiredEasily);
        rbAllAbove = findViewById(R.id.rbAllAbove);
        rbConsumeNutriFood = findViewById(R.id.rbConsumeNutriFood);
        rbMaintainigPersonnelHygine = findViewById(R.id.rbMaintainigPersonnelHygine);
        rbTFATablets = findViewById(R.id.rbTFATablets);
        rbAllTheAboveMeasures = findViewById(R.id.rbAllTheAboveMeasures);
        rbAllTheAboveMeasures = findViewById(R.id.rbAllTheAboveMeasures);
        rbDontKnowMeasures = findViewById(R.id.rbDontKnowMeasures);

        //Radio Groups
        rgHeardAneamia = findViewById(R.id.rgHeardAneamia);
        rgSourceOfInfo = findViewById(R.id.rgSourceOfInfo);
        rbKnownNo = findViewById(R.id.rbKnownNo);
        rgWhichNutrientDef = findViewById(R.id.rgWhichNutrientDef);
        rgCauseOfAneamia = findViewById(R.id.rgCauseOfAneamia);
        rgSignsOfAneamia = findViewById(R.id.rgSignsOfAneamia);
        rgWhatIsAneamia = findViewById(R.id.rgWhatIsAneamia);
        rgEffectOfAneamia = findViewById(R.id.rgEffectOfAneamia);
        rgMeasuresOfAneamia = findViewById(R.id.rgMeasuresOfAneamia);

        //EditTexts
        etxtAdolName = findViewById(R.id.etxtAdolName);
        etxtAddress = findViewById(R.id.etxtAddress);
        etxtParentName = findViewById(R.id.etxtParentName);
        etxtContactNumber = findViewById(R.id.etxtContactNumber);
        etxtHeight = findViewById(R.id.etxtHeight);
        etxtWeight = findViewById(R.id.etxtWeight);
        etxtMUAC = findViewById(R.id.etxtMUAC);
        etxtAge = findViewById(R.id.etxtAge);
        etxtSchoolName = findViewById(R.id.etxtSchoolName);
        etxtClass = findViewById(R.id.etxtClass);
        etxtUniqueId = findViewById(R.id.etxtUniqueId);
        etxtVillageName = findViewById(R.id.etxtVillageName);
        etxtBlockName = findViewById(R.id.etxtBlockName);
        etxtDistrictName = findViewById(R.id.etxtDistrictName);

        //TextViews
        txtAdolName = findViewById(R.id.txtAdolName);
        txtAddress = findViewById(R.id.txtAddress);
        txtParentName = findViewById(R.id.txtParentName);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtHeight = findViewById(R.id.txtHeight);
        txtWeight = findViewById(R.id.txtWeight);
        txtAge = findViewById(R.id.txtAge);
        txtMUAC = findViewById(R.id.txtMUAC);
        txtSchoolName = findViewById(R.id.txtSchoolName);
        txtClass = findViewById(R.id.txtClass);
        txtUniqueId = findViewById(R.id.txtUniqueId);
        txtVillageName = findViewById(R.id.txtVillageName);
        txtBlockName = findViewById(R.id.txtBlockName);
        txtDistrictName = findViewById(R.id.txtDistrictName);
        txtHeardAneamia = findViewById(R.id.txtHeardAneamia);
        txtSourceOfInfo = findViewById(R.id.txtSourceOfInfo);
        txtWhatIsAneamia = findViewById(R.id.txtWhatIsAneamia);
        txtWhichNutrientDef = findViewById(R.id.txtWhichNutrientDef);
        txtCauseOfAneamia = findViewById(R.id.txtCauseOfAneamia);
        txtSignsOfAneamia = findViewById(R.id.txtSignsOfAneamia);
        txtEffectOfAneamia = findViewById(R.id.txtEffectOfAneamia);
        txtMeasuresOfAneamia = findViewById(R.id.txtMeasuresOfAneamia);

        nextButton = findViewById(R.id.nextButton);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sharedPrefHelper.setString("id","");
    }
}
