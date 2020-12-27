package com.mobileapps.uas.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.model.Car;
import com.mobileapps.uas.model.Report;
import com.mobileapps.uas.ui.PlaceOrderActivity;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    private List<Report> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date, orderCount,income;
        public MyViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.tvMonth);
            orderCount = v.findViewById(R.id.tvOrderCount);
            income = v.findViewById(R.id.tvIncome);
        }

        public void bind(Report report) {
           date.setText(report.getDate());
           orderCount.setText(""+report.getOrderCount());
           income.setText("Rp. "+report.getIncome());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReportAdapter(List<Report> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_report, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Report report = mDataset.get(position);
        holder.bind(report);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


