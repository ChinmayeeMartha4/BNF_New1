package com.example.mhealth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import com.example.mhealth.R;

public class EditMenu extends AppCompatActivity {
    LinearLayout lnrBcktoMenu,lnrChildNutrn,lnrEditAbsent,lnrChild,lnrEligibleFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        init();
        lnrBcktoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditMenu.this, MainMenuActivity.class);
                startActivity(intent);

            }
        });
    }
    private void init(){
        lnrEligibleFamily=findViewById(R.id.lnrEligibleFamily);
        lnrChild=findViewById(R.id.lnrChild);
        lnrEditAbsent=findViewById(R.id.lnrEditAbsent);
        lnrChildNutrn=findViewById(R.id.lnrChildNutrn);
        lnrBcktoMenu=findViewById(R.id.lnrBcktoMenu);
        //sqliteHelper=new SqliteHelper(this);

    }
    @Override
    public void onBackPressed() {
        Intent new_intent = new Intent(this, MainMenuActivity.class);
        this.startActivity(new_intent);
    }
}