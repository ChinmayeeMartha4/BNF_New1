package com.example.mhealth.NewCode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.MainMenuRegistrationActivity;
import com.example.mhealth.R;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.SuposhanSakhiPojo;

public class SuposhanSakhiRegistration extends AppCompatActivity {
    TextView tv_suposhan_submit;
    EditText et_name,et_mobile;
    ImageView image1;
    SuposhanSakhiPojo suposhanSakhiPojo;
    private Context context = this;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suposhan_sakhi);
        init();

//        tv_suposhan_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                suposhanSakhiPojo.setName(et_name.getText().toString());
//                suposhanSakhiPojo.setMobile_number(et_mobile.getText().toString());
//
//                    sqliteHelper.saveSuposhanSakhiRegistrationData(suposhanSakhiPojo);
//
//                    Intent intent = new Intent(SuposhanSakhiRegistration.this, MainMenuRegistrationActivity.class);
//                    startActivity(intent);
//                }
//
//
//        });

    }
    private void init() {
        tv_suposhan_submit=findViewById(R.id.tv_suposhan_submit);
        et_mobile=findViewById(R.id.et_mobile);
        et_name=findViewById(R.id.et_name);
        image1=findViewById(R.id.image1);
        sqliteHelper=new SqliteHelper(this);
        suposhanSakhiPojo = new SuposhanSakhiPojo();

    }
}