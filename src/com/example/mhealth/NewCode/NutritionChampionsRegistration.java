package com.example.mhealth.NewCode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.MainMenuRegistrationActivity;
import com.example.mhealth.R;
import com.example.mhealth.helper.NutritionChampionPojo;
import com.example.mhealth.helper.SqliteHelper;

public class NutritionChampionsRegistration extends AppCompatActivity {

    TextView tv_nutrition_submit;
    EditText et_name,et_mobile;
    ImageView image1;
    private Context context = this;
    SqliteHelper sqliteHelper;
    NutritionChampionPojo nutritionChampionsPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_champions_registration);

        init();

//        tv_nutrition_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                nutritionChampionsPojo.setName(et_name.getText().toString());
//                nutritionChampionsPojo.setMobile_number(et_mobile.getText().toString());
//
//                    sqliteHelper.saveNutritionChampionsRegistrationData(nutritionChampionsPojo);
//
//                    Intent intent = new Intent(NutritionChampionsRegistration.this, MainMenuRegistrationActivity.class);
//                    startActivity(intent);
//                }
//
//
//        });


    }
    private void init() {
        tv_nutrition_submit=findViewById(R.id.tv_nutrition_submit);
        et_mobile=findViewById(R.id.et_mobile);
        et_name=findViewById(R.id.et_name);
        image1=findViewById(R.id.image1);
        sqliteHelper=new SqliteHelper(this);
        nutritionChampionsPojo=new NutritionChampionPojo();
    }
}