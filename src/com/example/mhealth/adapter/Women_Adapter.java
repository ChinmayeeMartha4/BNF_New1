package com.example.mhealth.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhealth.Activity_womenListing;
import com.example.mhealth.R;
import com.example.mhealth.helper.PregnantWomen;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Women_Adapter extends ArrayAdapter {

    public ArrayList<PregnantWomen> pregnantWomenList;
    Context context;
    private ArrayList<Object> strindexData;

    public Women_Adapter(Activity_womenListing context,
                         ArrayList<PregnantWomen> list, ArrayList<Object> strindexData) {
        super(context, 0);
        this.strindexData = strindexData;
        this.context = context;
        this.pregnantWomenList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pregnantWomenList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return pregnantWomenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final PregnantWomen pregnantWomenObject = this.pregnantWomenList
                .get(position);

        View rowView = null;

        rowView = inflater.inflate(R.layout.custom_listrow, parent, false);

        TextView textview = (TextView) rowView.findViewById(R.id.txtfield1);
        textview.setText(":" + " " + pregnantWomenObject.getHhId());


        TextView textview2 = (TextView) rowView.findViewById(R.id.txtfield2);
        textview2.setText(":" + " " + pregnantWomenObject.getPreWomenName());


        TextView textview3 = (TextView) rowView.findViewById(R.id.txtfield3);
        textview3.setText(":" + " " + dateFormatConvert(pregnantWomenObject.getEdd()));

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


    public String dateFormatConvert(String dateOfBirth) {
        String convertedDate = "";
        try {
            String dateReceivedFromUser = dateOfBirth;
            DateFormat userDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-mm-yyyy");
            Date date = userDateFormat.parse(dateReceivedFromUser);
            convertedDate = dateFormatNeeded.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}