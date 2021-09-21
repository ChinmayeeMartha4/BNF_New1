package com.example.mhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhealth.Activity_AddEvent;
import com.example.mhealth.R;
import com.example.mhealth.helper.EventMeetingModel;
import com.example.mhealth.utils.EventMeetingsViewActivity;

import java.util.ArrayList;

public class EventMeetingAdapter extends RecyclerView.Adapter<EventMeetingAdapter.ViewHolder> {
    private Context context;
    ArrayList<EventMeetingModel> arrayList;

    public EventMeetingAdapter(Context context, ArrayList<EventMeetingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_event_meeting, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.TVeventName.setText(arrayList.get(position).getEvents_title().trim());
//        holder.TVeventfromTime.setText(arrayList.get(position).getEvents_from().trim());
//        holder.TVeventtoTime.setText(arrayList.get(position).getEvents_to().trim());
        holder.LlEventMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, EventMeetingsViewActivity.class);
                intent.putExtra("eventId",arrayList.get(position).getEvents_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout LlEventMeeting;
        private TextView TVeventName, TVeventfromTime,TVeventtoTime;

        public ViewHolder(View itemView) {
            super(itemView);
            this.TVeventName = itemView.findViewById(R.id.TVeventName);
            this.TVeventfromTime = itemView.findViewById(R.id.TVeventFromTime);
            this.TVeventtoTime = itemView.findViewById(R.id.TVeventToTime);
            this.LlEventMeeting =(LinearLayout) itemView.findViewById(R.id.LlEventMeeting);
        }
    }
}
