package com.example.mhealth.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.ActivityChildReg;
import com.example.mhealth.Activity_AddEvent;
import com.example.mhealth.EventMeetingActivity;
import com.example.mhealth.MainMenuActivity;
import com.example.mhealth.MainMenuRegistrationActivity;
import com.example.mhealth.R;
import com.example.mhealth.adapter.EventListAdapter;
import com.example.mhealth.helper.EventDetailsModel;
import com.example.mhealth.helper.EventMeetingModel;
import com.example.mhealth.helper.SqliteHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class EventMeetingsViewActivity extends Activity {

    TextView tvTitleText, txtEventTitle, txtEventDescription, txtEventLocation, TxtEventFromTime, TxtEventToTime, TxtAttendees, TxtAbsence;
    EventMeetingModel eventMeetingModel;
    ArrayList<EventDetailsModel> eventMeetingModelArrayList;
    SqliteHelper sqliteHelper;
    int event_id=0;
    String attendeesStatus="";
    Button btnAttendees;
    ImageView image1, ivTitleBack;
    private Context context=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_meetings_view);

        tvTitleText= findViewById(R.id.tvTitleText);
        txtEventTitle= findViewById(R.id.TxtEventTitle);
        txtEventDescription= findViewById(R.id.TxtEventDescription);
        txtEventLocation= findViewById(R.id.TxtEventLocation);
        TxtEventToTime= findViewById(R.id.TxtEventToTime);
        TxtEventFromTime= findViewById(R.id.TxtEventFromTime);
        btnAttendees= findViewById(R.id.btnAttendees);
        TxtAttendees= findViewById(R.id.TxtAttendees);
        TxtAbsence= findViewById(R.id.TxtAbsence);
        ivTitleBack= findViewById(R.id.ivTitleBack);
        eventMeetingModelArrayList= new ArrayList<EventDetailsModel>();

        image1= findViewById(R.id.image1);
        tvTitleText.setText(R.string.event_meeting_details);
        sqliteHelper= new SqliteHelper(this);
        eventMeetingModel= new EventMeetingModel();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            event_id = bundle.getInt("eventId", 0);
            attendeesStatus = bundle.getString("attendeesStatus", "");
        }

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(getString(R.string.event_exit_message)+ "?");

                builder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(EventMeetingsViewActivity.this, EventMeetingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        if(attendeesStatus.equals("1")){
            btnAttendees.setVisibility(View.VISIBLE);
        }

        eventMeetingModel= sqliteHelper.getEventsData(event_id);

        txtEventTitle.setText(": " + eventMeetingModel.getEvents_title().trim());
        txtEventDescription.setText(": " +eventMeetingModel.getEvents_description().trim());
        txtEventLocation.setText(": " +eventMeetingModel.getEvens_location().trim());
        TxtEventToTime.setText(": " +eventMeetingModel.getEvents_to().trim());

    if(eventMeetingModel.getImage1() != null) {
        if (eventMeetingModel.getImage1() != null && eventMeetingModel.getImage1().length() > 200) {
        byte[] decodedString = Base64.decode(eventMeetingModel.getImage1(), Base64.NO_WRAP);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        image1.setImageBitmap(bitmap);
    } else if (eventMeetingModel.getImage1().length() <= 200) {
        try {
                /*String base64 = APIClient.IMAGE_PROFILE_URL + parentList.get(0).getImage();
                Picasso.with(this).load(base64).placeholder(R.drawable.ic_photo_camera).into(img);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        image1.setImageResource(R.drawable.ic_photo_camera);

    }
}

        if(eventMeetingModel.getEvents_from().trim().equals("")){
            TxtEventFromTime.setText(": N/A");
        }else {
            TxtEventFromTime.setText(": " +eventMeetingModel.getEvents_from().trim());
        }


        eventMeetingModelArrayList= sqliteHelper.getEventList(String.valueOf(event_id));
        RecyclerView recyclerView = findViewById(R.id.rvEventList);
        EventListAdapter adapter = new EventListAdapter(context, eventMeetingModelArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }



    public void click_save(View view) {
        Intent intent=new Intent(this, Activity_AddEvent.class);
        intent.putExtra("event_id",event_id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage("Are you sure to exit from Events or Meetings"  + "?");

        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(EventMeetingsViewActivity.this,
                        EventMeetingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
