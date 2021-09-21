package com.example.mhealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivitySAM_Calculator extends Activity {
    private TextView txtResult,tvTitleText;
    private EditText etxtHeight, etxtWeight;
    private Button btnCalculate;
    private RadioGroup rgSelectType;
    private RadioButton rbMale, rbFemale;
    private LinearLayout llResult;
    private ImageView ivTitleBack;
    int gender;
    float height;
    double weight;
    ArrayList<Double> age_weight = new ArrayList<Double>();
    ArrayList<Double> age_height = new ArrayList<Double>();
    private SharedPrefHelper sph;
    private SqliteHelper sqliteHelper;
    private Context context=this;
    private String strYes="", strNo="";

    public void initialize() {
        txtResult = (TextView) findViewById(R.id.txtResult);
        tvTitleText = (TextView) findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        etxtHeight = (EditText) findViewById(R.id.etxtHeight);
        etxtWeight = (EditText) findViewById(R.id.etxtWeight);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        rgSelectType = (RadioGroup) findViewById(R.id.rgSelectType);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        llResult = (LinearLayout) findViewById(R.id.llResult);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        age_height = new ArrayList<>();
        age_weight = new ArrayList<>();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sam__calculator);

        initialize();
        String languageId = sph.getString("Language", "1");// getting languageId
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);

        setText();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (rbMale.isChecked()) {
                        gender = 1;
                    } else if (rbFemale.isChecked()) {
                        gender = 2;
                    }

                    height = Float.parseFloat(etxtHeight.getText().toString());
                    weight = Double.parseDouble(etxtWeight.getText().toString());

                    if (height > 120.0 || height < 45.0) {
                        etxtHeight.setError(getString(R.string.should_between_45_120));
                        return;
                    }

                    if (weight > 25.0 || weight < 1.0) {
                        etxtWeight.setError(getString(R.string.should_between_1_25));
                        return;
                    }

                    if (gender == 1) {
                        age_weight = sqliteHelper.CompareHeightWeight("IDEAl_weight_height_boys", height);

                    } else if (gender == 2) {
                        age_weight = sqliteHelper.CompareHeightWeight("IDEAl_weight_height_girls", height);
                    }

                    if (age_weight.size() > 0) {
                        if (weight <= age_weight.get(1)) {
                            txtResult.setText("SAM");
                            llResult.setBackgroundColor(getResources().getColor(R.color.red));
                        } else if (weight > age_weight.get(1) && weight <= age_weight.get(0)) {
                            txtResult.setText("MAM");
                            llResult.setBackgroundColor(getResources().getColor(R.color.yellow));
                        } else if (weight > age_weight.get(0)) {
                            txtResult.setText("Normal");
                            llResult.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    } else {
                        Toast.makeText(ActivitySAM_Calculator.this, R.string.proper_values_text, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.mal_nutrition_calculator_exit);
                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(strYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(
                                        ActivitySAM_Calculator.this,
                                        MainMenuUtilityActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void setText() {
        tvTitleText.setText(R.string.mal_nutrition_calculator);
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (etxtHeight.getText().toString().equalsIgnoreCase("")) {
            etxtHeight.setError(getString(R.string.please_enter_height));
            ret = false;
        }
        if (etxtWeight.getText().toString().equalsIgnoreCase("")) {
            etxtWeight.setError(getString(R.string.please_enter_weight));
            ret = false;
        }
        if (rgSelectType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.please_select_gender_type, Toast.LENGTH_SHORT).show();
            ret = false;
        }
        return ret;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.mal_nutrition_calculator_exit);
        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(strYes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivitySAM_Calculator.this,
                                MainMenuUtilityActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
