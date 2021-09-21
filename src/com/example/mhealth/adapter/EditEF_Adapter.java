package com.example.mhealth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhealth.Activity_EditEligibleFamilyListing;
import com.example.mhealth.R;
import com.example.mhealth.helper.EditEF;

import java.util.ArrayList;

public class EditEF_Adapter extends ArrayAdapter {
    public ArrayList<EditEF> efList;
    Context context;
    private ArrayList<Object> strindexData;

    public EditEF_Adapter(Activity_EditEligibleFamilyListing context, ArrayList<EditEF> list, ArrayList<Object> strindexData) {
        super(context, 0);
        this.strindexData = strindexData;
        this.context = context;
        this.efList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return efList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return efList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final EditEF eefObject = this.efList.get(position);

        View rowView = null;

        rowView = inflater.inflate(R.layout.custom_listrow, parent, false);

        TextView textview = (TextView) rowView.findViewById(R.id.txtfield1);
        textview.setText(":" + " " + eefObject.getHhId());
        TextView textview2 = (TextView) rowView.findViewById(R.id.txtfield2);
        textview2.setText(":" + " " + eefObject.getHHName());
        TextView textview3 = (TextView) rowView.findViewById(R.id.txtfield3);
        textview3.setVisibility(View.GONE);

        TextView txtTop = (TextView) rowView.findViewById(R.id.txttop);
        txtTop.setText(strindexData.get(0).toString());
        TextView txtMiddle = (TextView) rowView.findViewById(R.id.txtmiddle);
        txtMiddle.setText(strindexData.get(1).toString());
        TextView txtBottom = (TextView) rowView.findViewById(R.id.txtbottom);
        txtBottom.setVisibility(View.GONE);

        if ( eefObject.getAadhaar().contains("000000000000") || eefObject.getAadhaar().equalsIgnoreCase("") || eefObject.getAadhaar().equalsIgnoreCase("0")  || eefObject.getLatitude().equalsIgnoreCase("") || eefObject.getLatitude().contains("000000") && eefObject.getLongitude().equalsIgnoreCase("") || eefObject.getLongitude().contains("000000") ) {

            rowView.setBackgroundColor(Color.parseColor("#FF0000"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#98FB98"));
        }

        return rowView;
    }


}
