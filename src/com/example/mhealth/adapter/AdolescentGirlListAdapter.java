package com.example.mhealth.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhealth.Activity_AdlGirlList;
import com.example.mhealth.R;
import com.example.mhealth.helper.Adolescent;

import java.util.ArrayList;

public class AdolescentGirlListAdapter extends ArrayAdapter {

    public ArrayList<Adolescent> GirlList;
    Context context;
    Activity activity;

    private ArrayList<Object> strindexData;

    public AdolescentGirlListAdapter(Activity_AdlGirlList context, ArrayList<Adolescent> list, ArrayList<Object> strindexData) {
        super(context, 0);
        this.context = context;
        this.strindexData = strindexData;
        this.GirlList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return GirlList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return GirlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Adolescent AdoGirlObj = this.GirlList.get(position);

        View rowView = null;

        rowView = inflater.inflate(R.layout.custom_listrow, parent, false);

        TextView textname = (TextView) rowView.findViewById(R.id.txtfield1);
        textname.setText(":" + " " + AdoGirlObj.getHhId());

        TextView textmothername = (TextView) rowView.findViewById(R.id.txtfield2);
        textmothername.setText(":" + " " + AdoGirlObj.getNameOfTheGirl().trim());

        TextView textaddress = (TextView) rowView.findViewById(R.id.txtfield3);
        textaddress.setText(":" + " " + AdoGirlObj.getGirlFatherName());

        TextView txtTop = (TextView) rowView.findViewById(R.id.txttop);
        txtTop.setText(strindexData.get(0).toString());


        TextView txtMiddle = (TextView) rowView.findViewById(R.id.txtmiddle);
        txtMiddle.setText(strindexData.get(1).toString());

        TextView txtBottom = (TextView) rowView.findViewById(R.id.txtbottom);
        txtBottom.setText(strindexData.get(2).toString());

        /*
         * TextView textview2 = (TextView) rowView.findViewById(R.id.textThird);
         * textview2.setText(parentObject.getMother());
         */

        if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#D8BFD8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#98FB98"));
        }

        return rowView;

    }


}
