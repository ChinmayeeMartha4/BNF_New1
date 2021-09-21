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

public class ActivityBMI_Calculator extends Activity {
    private TextView txtResult,tvTitleText;
    private EditText etxtHeight, etxtWeight;
    private Button btnCalculate;
    private ImageView ivTitleBack;
    private LinearLayout llResult;
    float height;
    double weight;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sph;
    private Context context=this;
    private String strYes="", strNo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_b_m_i__calculator);

        initialize();
        String languageId = sph.getString("Language", "1");// getting languageId
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);

        setText();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    height = Float.parseFloat(etxtHeight.getText().toString());
                    weight = Double.parseDouble(etxtWeight.getText().toString());
                    if (height > 120.0 || height < 45.0) {
                        etxtHeight.setError(getString(R.string.height_validation));
                        return;
                    }
                    if (weight < 1.0) {
                        etxtWeight.setError(getString(R.string.weight_validation));
                        return;
                    }
                    float heightValue = Float.parseFloat(String.valueOf(height)) / 100;
                    float weightValue = Float.parseFloat(String.valueOf(weight));
                    float bmi = weightValue / (heightValue * heightValue);
                    displayBMI(bmi);

                } else {
                    Toast.makeText(ActivityBMI_Calculator.this, R.string.proper_values_text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.bmi_calculator_message);
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
                                        ActivityBMI_Calculator.this,
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
        tvTitleText.setText(R.string.bmi_calculator);
    }

    public void initialize() {
        sqliteHelper=new SqliteHelper(this);
        sph=new SharedPrefHelper(this);

        txtResult = (TextView) findViewById(R.id.txtResult);
        etxtHeight = (EditText) findViewById(R.id.etxtHeight);
        etxtWeight = (EditText) findViewById(R.id.etxtWeight);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        llResult = (LinearLayout) findViewById(R.id.llResult);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        tvTitleText=findViewById(R.id.tvTitleText);
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
        return ret;
    }

    private void displayBMI(float bmi) {
        String bmiLabel = "";
        if (Float.compare(bmi, 18.0f) <= 0) {
            bmiLabel = getString(R.string.Red);
            llResult.setBackgroundColor(getResources().getColor(R.color.red));
        } else if (Float.compare(bmi, 18.0f) > 0 && Float.compare(bmi, 25.0f) <= 0) {
            bmiLabel = getString(R.string.green);
            llResult.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            bmiLabel = getString(R.string.obese);
            llResult.setBackgroundColor(getResources().getColor(R.color.yellow));

        }
        bmiLabel = bmi + "\n\n" + bmiLabel;
        txtResult.setText(bmiLabel);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.bmi_calculator_exit_mesage);
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
                                ActivityBMI_Calculator.this,
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
