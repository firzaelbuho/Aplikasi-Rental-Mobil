package com.mobileapps.uas.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.adapter.OrderAdapter;
import com.mobileapps.uas.model.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    RecyclerView rvList;
    List<Order>  mDataset;
    ProgressBar loader;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Pesanan");

        initWidgets();
        showList();
    }

    private void showList() {
        rvList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ORDERS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Order> myList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);

                    // update status order jika melewati batas, berarti sudah selesai

                    updateOrderstatus(order);

                    // set hanya order milik user yg ditampilkan

                    if(Util.isAdmin) {
                        myList.add(order);
                    } else{
                        if(order.getCustomerId().equals(userId)){
                            myList.add(order);
                        }
                    }
                }
                Collections.reverse(myList);
                mDataset = myList;
                mAdapter = new OrderAdapter(mDataset);
                rvList.setAdapter(mAdapter);
                loader.setVisibility(View.GONE);

                Log.d("cek list isi", "isinya"+ myList.toString());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        // mDataset = Util.getListOrder();
        // specify an adapter (see also next example)
        // mAdapter = new OrderAdapter(mDataset);
        // rvList.setAdapter(mAdapter);
    }

    private void updateOrderstatus(Order order) {

        if(order.getStatus().equals("diproses")){
            long currTime = Util.toUnixTime(Util.getCurrentDate());
            if(order.getRendEndDate() < currTime) {
                Log.d("tes", "updateOrderstatus: masuk h");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ORDERS");
                order.setStatus("selesai");
                ref.child(order.getOrderId()).setValue(order);
            }
            Log.d("tes", "updateOrderstatus: masuk g");
        }
    }

    private void initWidgets() {
        rvList = findViewById(R.id.rvOrderList);
        loader = findViewById(R.id.progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}