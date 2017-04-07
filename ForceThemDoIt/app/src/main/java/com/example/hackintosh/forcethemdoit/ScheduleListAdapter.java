package com.example.hackintosh.forcethemdoit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by strainu on 01.04.2017.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    List<String> scheduleList;
    ViewHolder viewHolder;

    public ScheduleListAdapter(List<String> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView mText;
        public ViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.myText);
            mText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),DeliveringActivity.class);
                    intent.putExtra("schedule",mText.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        this.viewHolder = viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("myTag", String.valueOf(position));
        holder.mText.setText(scheduleList.get(position));
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

}
