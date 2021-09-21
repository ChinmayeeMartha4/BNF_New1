package com.example.mhealth.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhealth.Activity_parentList;
import com.example.mhealth.R;
import com.example.mhealth.helper.Parent;

import java.util.ArrayList;

public class Parent_Adapter extends ArrayAdapter {

    public ArrayList<Parent> parentList;
    Context context;
    Activity activity;

    private ArrayList<Object> strindexData;

    public Parent_Adapter(Activity_parentList context, ArrayList<Parent> list, ArrayList<Object> strindexData) {
        super(context, 0);
        this.context = context;
        this.strindexData = strindexData;
        this.parentList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return parentList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return parentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Parent parentObject = this.parentList.get(position);

        View rowView = null;

        rowView = inflater.inflate(R.layout.custom_listrow, parent, false);

        TextView textname = (TextView) rowView.findViewById(R.id.txtfield1);
        textname.setText(":" + " " + parentObject.getHouseNo());

        TextView textmothername = (TextView) rowView.findViewById(R.id.txtfield2);
        textmothername.setText(":" + " " + parentObject.getHeadofHH().trim());

        TextView textaddress = (TextView) rowView.findViewById(R.id.txtfield3);
        textaddress.setText(":" + " " + parentObject.getAadharCardHH());

        TextView txtTop = (TextView) rowView.findViewById(R.id.txttop);
        txtTop.setText(strindexData.get(0).toString());


        TextView txtMiddle = (TextView) rowView.findViewById(R.id.txtmiddle);
        txtMiddle.setText(strindexData.get(1).toString());

        TextView txtBottom = (TextView) rowView.findViewById(R.id.txtbottom);
        txtBottom.setText(strindexData.get(2).toString());

        if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#D8BFD8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#98FB98"));
        }

        return rowView;

    }

}
