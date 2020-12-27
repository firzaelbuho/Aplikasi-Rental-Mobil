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
import com.mobileapps.uas.model.Order;
import com.mobileapps.uas.ui.PlaceOrderActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<Order> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView custName,price,carName, dateRent, status, orderDate;
        View view;
        public MyViewHolder(View v) {
            super(v);
            custName = v.findViewById(R.id.tvName);
            price = v.findViewById(R.id.tvPrice);
            carName = v.findViewById(R.id.tvCarName);
            dateRent = v.findViewById(R.id.tvDate);
            status = v.findViewById(R.id.tvStatus);
            view = v;
            orderDate = v.findViewById(R.id.tvOrderDate);
        }

        public void bind(Order order) {
             custName.setText(order.getCustomerName());
             price.setText("Rp. "+order.getTotalPrice());
             carName.setText(order.getCarName());
             String dateConcat = Util.unixTimeToString(order.getRendStartDate()) + " " + Util.unixTimeToString(order.getRendEndDate());
             dateRent.setText(dateConcat);
             status.setText(order.getStatus());

             if(order.getStatus().equals("selesai")){
                 status.setTextColor(view.getResources().getColor(R.color.green));
             } else if(order.getStatus().equals("dibatalkan")){
                 status.setTextColor(view.getResources().getColor(R.color.red));
             }

             orderDate.setText(Util.unixTimeToString(order.getOrderDate()));

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Util.isFirst = false;
                    Util.order = order;
                    Intent intent = new Intent(view.getContext(), PlaceOrderActivity.class);
                    intent.putExtra(PlaceOrderActivity.EXTRA_ORDER, order);
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(List<Order> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_order, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Order order = mDataset.get(position);
        holder.bind(order);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

