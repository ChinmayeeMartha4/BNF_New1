package com.example.mhealth;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class ActivityUnderWeight_Calculator extends AppCompatActivity {
    TextView txtSelectType, txtAge, txtWeight, txtResult;
    EditText etxtAge, etxtWeight;
    Button btnCalculate;
    RadioGroup rgSelectType;
    RadioButton rbMale, rbFemale;
    LinearLayout llResult;
    int gender;
    int age;
    double weight;
    ArrayList<Double> age_weight = new ArrayList<Double>();
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_under_weight__calculator);

        initViews();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (rbMale.isChecked()) {
                        gender = 1;
                    } else if (rbFemale.isChecked()) {
                        gender = 2;
                    }

                    age = Integer.parseInt(etxtAge.getText().toString());
                    weight = Double.parseDouble(etxtWeight.getText().toString());

                    if (age > 60 || age < 0) {
                        etxtAge.setError("Should between 0 to 60");
                        return;
                    }

                    if (weight > 25.0 || weight < 1.0) {
                        etxtWeight.setError("Should between 1 to 25 kg");
                        return;
                    }

                    if (gender == 1) {
                        age_weight = sqliteHelper.CompareAgeWeight("ideal_weight_boy", age);

                    } else if (gender == 2) {
                        age_weight = sqliteHelper.CompareAgeWeight("ideal_weight_girl", age);
                    }

                    if (age_weight.size() > 0) {
                        if (weight <= age_weight.get(1)) {
                            txtResult.setText("SUW");
                            //imgColor.setImageResource(R.drawable.sev);
                            llResult.setBackgroundColor(getResources().getColor(R.color.red));

                        } else if (weight > age_weight.get(1) && weight <= age_weight.get(0)) {
                            txtResult.setText("MUM");
                            // imgColor.setImageResource(R.drawable.mod);
                            llResult.setBackgroundColor(getResources().getColor(R.color.yellow));


                        } else if (weight > age_weight.get(0)) {
                            txtResult.setText("Normal");
                            // imgColor.setImageResource(R.drawable.nor);
                            llResult.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                        }
                    } else {
                        Toast.makeText(ActivityUnderWeight_Calculator.this, "Enter Proper Values", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initViews() {
        sqliteHelper = new SqliteHelper(this);
        txtSelectType = (TextView) findViewById(R.id.txtSelectType);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtWeight = (TextView) findViewById(R.id.txtWeight);
        txtResult = (TextView) findViewById(R.id.txtResult);
        etxtAge = (EditText) findViewById(R.id.etxtAge);
        etxtWeight = (EditText) findViewById(R.id.etxtWeight);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        rgSelectType = (RadioGroup) findViewById(R.id.rgSelectType);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        llResult = (LinearLayout) findViewById(R.id.llResult);
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (etxtAge.getText().toString().equalsIgnoreCase("")) {
            etxtAge.setError("Please enter Age");
            ret = false;
        }
        if (etxtWeight.getText().toString().equalsIgnoreCase("")) {
            etxtWeight.setError("Please enter weight");
            ret = false;
        }
        if (rgSelectType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender type", Toast.LENGTH_SHORT).show();
            ret = false;
        }
        return ret;
    }
}