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
import com.mobileapps.uas.ui.PlaceOrderActivity;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    private List<Car> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name,price;
        ImageView img;
        View view;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tvName);
            price = v.findViewById(R.id.tvPrice);
            img = v.findViewById(R.id.imgCar);
            view = v;
        }

        public void bind(Car car) {
            name.setText(car.getName()+" "+car.getYear());
            price.setText("Rp. "+car.getCost());
            img.setImageResource(R.drawable.avanza);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Util.isFirst = true;
                    Intent intent = new Intent(view.getContext(), PlaceOrderActivity.class);
                    intent.putExtra(PlaceOrderActivity.EXTRA_CAR, car);
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CarAdapter(List<Car> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_car, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Car car = mDataset.get(position);
        holder.bind(car);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

