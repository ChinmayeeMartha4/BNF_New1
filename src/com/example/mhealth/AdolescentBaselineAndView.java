package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhealth.adapter.AdolescentBaselineEditAdapter;
import com.example.mhealth.helper.AdolBaseline;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;

public class AdolescentBaselineAndView extends Activity {

    RecyclerView recyclerView;
    ArrayList<AdolBaseline> baselines;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adolescent_baseline_and_view);
        recyclerView=findViewById(R.id.recylce);
        setTitle("Edit Adolescent Baseline");
        baselines=new ArrayList<>();
        sqliteHelper=new SqliteHelper(this);


      baselines=sqliteHelper.getBaselineDataEdit();
        AdolescentBaselineEditAdapter adapter = new AdolescentBaselineEditAdapter(this,baselines);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



    }
}
