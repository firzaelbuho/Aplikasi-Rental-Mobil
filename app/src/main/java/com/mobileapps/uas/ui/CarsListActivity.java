package com.mobileapps.uas.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileapps.uas.adapter.CarAdapter;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.adapter.OrderAdapter;
import com.mobileapps.uas.model.Car;
import com.mobileapps.uas.model.Order;

import java.util.ArrayList;
import java.util.List;

public class CarsListActivity extends AppCompatActivity {
    List<Car>  mDataset;
    RecyclerView rvList;
    ProgressBar loader;
    DatabaseReference mDatabase;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    Button btnAddCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_list);




        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pilih Mobil");
        initWidgets();
        checkIsAdmin();
        showList();
    }

    private void checkIsAdmin() {
        if(Util.isAdmin){
            btnAddCar.setVisibility(View.VISIBLE);
            btnAddCar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Util.isFirst = true;
                    startActivity(new Intent(CarsListActivity.this, AddCarActivity.class));
                }
            });
        }
    }

    private void showList() {

        rvList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("CARS");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Car> myList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Car car = snapshot.getValue(Car.class);
                    myList.add(car);
                    Log.d("cek isi", "isinya"+ car.getId());

                }
                mDataset = myList;
                mAdapter = new CarAdapter(mDataset);
                rvList.setAdapter(mAdapter);
                loader.setVisibility(View.GONE);

                Log.d("cek list isi", "isinya"+ myList.toString());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




    }

    private void initWidgets() {
        btnAddCar = findViewById(R.id.btnAddCar);
        rvList = findViewById(R.id.rvCarList);
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