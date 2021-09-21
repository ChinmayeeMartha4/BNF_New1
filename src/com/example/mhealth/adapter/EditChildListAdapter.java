package com.example.mhealth.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mhealth.Activity_EditChildListing;
import com.example.mhealth.R;
import com.example.mhealth.helper.Child;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditChildListAdapter extends ArrayAdapter {
    public ArrayList<Child> childList;

    Context context;

    private ArrayList<Object> strindexData;

    public EditChildListAdapter(Activity_EditChildListing context, ArrayList<Child> list, ArrayList<Object> strindexData) {
        // TODO Auto-generated constructor stub
        super(context, 0);
        this.context = context;
        this.childList = list;
        this.strindexData = strindexData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return childList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup child) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Child childObject = this.childList.get(position);
        View rowView = null;

        rowView = inflater.inflate(R.layout.custom_listrow, child, false);

        RelativeLayout rel = (RelativeLayout) rowView
                .findViewById(R.id.rel_imgview);

        rel.setVisibility(convertView.GONE);
        ImageView img = (ImageView) rowView.findViewById(R.id.img_view);
        Bitmap bmp = StringToBitmap(childObject.getMultimedia());
        img.setImageBitmap(bmp);

        TextView textview1 = (TextView) rowView.findViewById(R.id.txtfield1);
        textview1.setText(":" + " " + childObject.getChild_name());

        TextView textview2 = (TextView) rowView.findViewById(R.id.txtfield2);
        textview2.setText(":" + " " + childObject.getMother_name());

        TextView textview3 = (TextView) rowView.findViewById(R.id.txtfield3);
        textview3.setVisibility(View.GONE);

        TextView textview4 = (TextView) rowView.findViewById(R.id.txtfield4);
        textview4.setText(":" + " " + childObject.getChild_id());


        TextView txtTop = (TextView) rowView.findViewById(R.id.txttop);
        txtTop.setText(strindexData.get(0).toString());


        TextView txtMiddle = (TextView) rowView.findViewById(R.id.txtmiddle);
        txtMiddle.setText(strindexData.get(1).toString());

        TextView txtBottom = (TextView) rowView.findViewById(R.id.txtbottom);
        txtBottom.setVisibility(View.GONE);

        if (childObject.getChild_name().equals(null) || childObject.getChild_weight().equals(null) || childObject.getHeight().equals(null) || childObject.getLatitude()==null || childObject.getLatitude().contains("00000") || childObject.getLongitude()==null || childObject.getLongitude().contains("000000")  || childObject.getHHID()==null ||childObject.getHb().equals(null)) {
            rowView.setBackgroundColor(Color.parseColor("#FF0000"));
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

    public Bitmap StringToBitmap(String multimedia) {
        // TODO Auto-generated method stub
        try {
            byte[] encodeByte = Base64.decode(multimedia, Base64.DEFAULT);


            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }
}
