package com.example.hackintosh.forcethemdoit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hackintosh on 4/7/17.
 */

public class DeliveringListAdapter extends RecyclerView.Adapter<DeliveringListAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private List<String[]> receivers;

    public DeliveringListAdapter(List<String[]> receivers) {
        this.receivers = receivers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView mMessage;
        public TextView mNumber;
        public ViewHolder(View v) {
            super(v);
            mMessage = (TextView) v.findViewById(R.id.message);
            mNumber = (TextView) v.findViewById(R.id.number);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receivers_model,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        this.viewHolder = viewHolder;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMessage.setText(receivers.get(position)[1]);
        holder.mNumber.setText(receivers.get(position)[0]);
    }


    @Override
    public int getItemCount() {
        return receivers.size();
    }
}
