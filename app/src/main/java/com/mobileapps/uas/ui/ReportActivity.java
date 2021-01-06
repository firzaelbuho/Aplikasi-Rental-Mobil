package com.mobileapps.uas.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.adapter.CarAdapter;
import com.mobileapps.uas.adapter.ReportAdapter;
import com.mobileapps.uas.model.Car;
import com.mobileapps.uas.model.Order;
import com.mobileapps.uas.model.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    List<Report>  mDataset;
    RecyclerView rvList;
    ProgressBar loader;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laporan Pemasukan");

        initWidget();
        showList();


    }

    private void showList() {
        rvList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ORDERS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long currentTime = Util.toUnixTime(Util.getCurrentDate());
                ArrayList<Report> myList = new ArrayList<>();
                for(int i = 0 ; i < 30 ; i++ ){
                    long stamp = currentTime - (86400 * i);
                    myList.add(new Report(Util.unixTimeToString(stamp),0,0,stamp));
                }

                // mengelompokkan order berdasarkan tanggal order

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);

                    if(order.getStatus().equals("selesai")){
                        for (int i = 0 ; i<30 ; i++){
                            if(Util.unixTimeToString(order.getRendEndDate()).equals(myList.get(i).getDate())){
                                int orderCount = myList.get(i).getOrderCount() + 1;
                                int newPrice = order.getTotalPrice();
                                int income = myList.get(i).getIncome() + newPrice;
                                Log.d("newPrice =", ""+newPrice);
                                Log.d("newPrice setelah ditamb", ""+income);
                                myList.get(i).setOrderCount(orderCount);
                                myList.get(i).setIncome(income);
                                break;
                            }
                        }
                    }
                }
               // mDataset = myList;
                Log.d("cek list isi", "isinya"+ myList.toString());
                mAdapter = new ReportAdapter(myList);
                rvList.setAdapter(mAdapter);
                loader.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }



    private void initWidget() {
        rvList = findViewById(R.id.rvReport);
        loader = findViewById(R.id.progressBarReport);
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