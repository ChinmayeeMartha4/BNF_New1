package com.example.mhealth.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mhealth.Adolscent_Baseline;
import com.example.mhealth.R;
import com.example.mhealth.helper.AdolBaseline;


import java.util.ArrayList;

public class AdolescentBaselineEditAdapter extends RecyclerView.Adapter<AdolescentBaselineEditAdapter.ViewHolder> {

    Context context;
    private ArrayList<AdolBaseline> adolBaselines;

    public AdolescentBaselineEditAdapter(@NonNull Context context, ArrayList editBaseline) {
        this.context = context;
        this.adolBaselines = editBaseline;

    }

    @NonNull
    @Override
    public AdolescentBaselineEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem = layoutInflater.inflate(R.layout.adolescent_baseline_edit, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AdolescentBaselineEditAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.adolescentName.setText(adolBaselines.get(i).getAdolName());
        viewHolder.villageName.setText(adolBaselines.get(i).getVillageName());
        viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Adolscent_Baseline.class);
             String id= adolBaselines.get(i).getId();
             intent.putExtra("id",id);
             context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return adolBaselines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView adolescentName,villageName;
        private Button btnView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.adolescentName = itemView.findViewById(R.id.txtAdolescentName);
            this.villageName = itemView.findViewById(R.id.txtvillageName);
            this.btnView = itemView.findViewById(R.id.btnView);



        }
    }
}
