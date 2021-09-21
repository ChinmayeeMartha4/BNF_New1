package com.example.mhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mhealth.Activity_AddEvent;
import com.example.mhealth.R;
import com.example.mhealth.helper.EventDetailsModel;
import com.example.mhealth.helper.EventMeetingModel;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private final Context context;
    ArrayList<EventDetailsModel> arrayList;

    public EventListAdapter(Context context, ArrayList<EventDetailsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_event_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int Total= Integer.parseInt(arrayList.get(position).getMale()) + Integer.parseInt(arrayList.get(position).getFemale()) + Integer.parseInt(arrayList.get(position).getBoys()) + Integer.parseInt(arrayList.get(position).getGirls());

        holder.tv_event_date.setText( ": "  +arrayList.get(position).getEvent_date());
        holder.tv_total_attendees.setText( ": "  + Total);
        holder.tv_event.setText( ": "  +arrayList.get(position).getEvents_title());
        holder.tv_event_type.setText( ": "  +arrayList.get(position).getEvent_type());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_event_date, tv_total_attendees, tv_event, tv_event_type;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_event_date = itemView.findViewById(R.id.tv_event_date);
            this.tv_total_attendees = itemView.findViewById(R.id.tv_total_attendees);
            this.tv_event = itemView.findViewById(R.id.tv_event);
            this.tv_event_type = itemView.findViewById(R.id.tv_event_type);
        }
    }
}
