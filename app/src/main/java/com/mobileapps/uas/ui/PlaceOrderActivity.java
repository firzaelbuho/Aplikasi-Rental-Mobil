package com.mobileapps.uas.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.adapter.OrderAdapter;
import com.mobileapps.uas.model.Car;
import com.mobileapps.uas.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class PlaceOrderActivity extends AppCompatActivity {

    String status = "diproses";
    String msg = "Pesanan berhasil dibuat";
    long stamp0, stamp1;
    public static String EXTRA_CAR = "extra_car";
    public static String EXTRA_ORDER = "extra_order";
    boolean isCancel = false;
    EditText etName,etPhone,etAdress, etDateStart, etDateEnd, etPrice;
    TextView tvCarName, tvCheck;
    ProgressBar loader;
    boolean isSaveClicked = false;
    Car car;
    Order order;
    ImageView imgCar;
    Button btnCancel, btnSave;
    DatabaseReference mDatabase;

    // only accesed for edit

    long oldStamp0 = 0;
    long oldStamp1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        initWidget();
        initClickListener();
        checkIsFirst();
        setUp();
    }

    private void setUp() {

        tvCheck.setText("");
        loader.setVisibility(View.GONE);
        if(!Util.isAdmin){
            
            if(Util.isFirst){

                String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                etName.setText(name);
                Intent intent = getIntent();
                car = intent.getParcelableExtra(EXTRA_CAR);
                Log.d("cek isi car", car.toString());
                tvCarName.setText(car.getName());
                etDateStart.setText(Util.getCurrentDate());
                etDateEnd.setText(Util.getCurrentDate());
                etPrice.setText("");
                stamp0 = Util.toUnixTime(Util.getCurrentDate());
                stamp1 = Util.toUnixTime(Util.getCurrentDate());
                updatePrice();

                Glide.with(imgCar)
                        .load(car.getImgUrl())
                        .into(imgCar);



            } else {
                setupFromData();
                if(order.getStatus().equals("diproses")){
                    updatePrice();
                }
            }


            
        } else {
            disableEdit();
            setupFromData();
            btnSave.setVisibility(View.INVISIBLE);
        }


    }

    private void checkDateAvailability() {

        String currCarId = "";
        if(Util.isFirst){
            currCarId = car.getId();
        } else {
            currCarId = order.getCarId();
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ORDERS");
        String finalCurrCarId = currCarId;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                loader.setVisibility(View.VISIBLE);
                tvCheck.setText("mengecek..");
                boolean isAvailable = true;
                btnSave.setEnabled(false);
                ArrayList<Order> myList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order currOrder = snapshot.getValue(Order.class);

                    if(finalCurrCarId.equals(currOrder.getCarId())){
                        if(stamp0/86400 >= currOrder.getRendStartDate()/86400 && stamp0/86400 <= currOrder.getRendEndDate()/86400){
                           if(stamp0 != oldStamp0){
                               isAvailable = false;
                               String errorMsg = Util.unixTimeToString(currOrder.getRendStartDate())+"  -  "+Util.unixTimeToString(currOrder.getRendEndDate())+"\nsudah dibooking !";
                               if(!isSaveClicked){
                                   tvCheck.setText(errorMsg);
                               }
                               break;
                           }
                        } else if(stamp1/86400 >= currOrder.getRendStartDate()/86400 && stamp1/86400 <= currOrder.getRendEndDate()/86400){
                            if(stamp1 != oldStamp1){
                                isAvailable = false;
                                String errorMsg = Util.unixTimeToString(currOrder.getRendStartDate())+"-"+Util.unixTimeToString(currOrder.getRendEndDate())+"\nsudah dibooking !";
                                if(!isSaveClicked){
                                    tvCheck.setText(errorMsg);
                                }
                                break;
                            }
                        }
                    }

                }

                if(!isSaveClicked){
                    if(isAvailable){
                        loader.setVisibility(View.GONE);
                        tvCheck.setText("Jadwal tersedia !");
                        tvCheck.setTextColor(getResources().getColor(R.color.green));
                        btnSave.setEnabled(true);
                    } else {

                        tvCheck.setTextColor(getResources().getColor(R.color.red));
                        loader.setVisibility(View.GONE);
                        btnSave.setEnabled(false);
                    }
                }

                Log.d("cek list isi", "isinya"+ myList.toString());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }



    private void setupFromData() {

        order = Util.order;
        Glide.with(imgCar)
                .load(order.getCarImg())
                .into(imgCar);
        Intent intent = getIntent();
        // order = intent.getParcelableExtra(EXTRA_ORDER);
        tvCarName.setText(order.getCarName());
        Log.d("isi order",order.getPhone());
        etName.setText(order.getCustomerName());
        etAdress.setText(order.getAddress());
        etPhone.setText(order.getPhone());
        etDateStart.setText(Util.unixTimeToString(order.getRendStartDate()));
        etDateEnd.setText(Util.unixTimeToString(order.getRendEndDate()));
        etPrice.setText("Rp. "+order.getTotalPrice());

        stamp0 = order.getRendStartDate();
        stamp1 = order.getRendEndDate();
        oldStamp0 = order.getRendStartDate();
        oldStamp1 = order.getRendEndDate();

        // check kalau pesanan sudah dibatalkan

        if(order.getStatus().equals("dibatalkan")){
            disableEdit();
            btnSave.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            tvCarName.setText(order.getCarName() + "\n\n( Pesanan Dibatalkan )");

        } else if(order.getStatus().equals("selesai")){
            disableEdit();
            btnSave.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            tvCarName.setText(order.getCarName() + "\n\n( Pesanan Telah Selesai )");


        }

    }

    private void checkIsFirst() {
        boolean isFirst = Util.isFirst;
        if(isFirst){
            btnCancel.setVisibility(View.INVISIBLE);
        } else {
            btnSave.setText("Simpan");
        }
    }

    private void initClickListener() {

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCancelDialog();

            }
        });

        if(!Util.isAdmin){
            etDateStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(etDateStart, true);
                }
            });

            etDateEnd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showDialog(etDateEnd, false);
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                      isSaveClicked = true;
                      save();

                }
            });
        } 
           
        
    }

    private void showCancelDialog() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title

        alertDialogBuilder.setTitle("Batalkan Pesanan");

        // set dialog message
        alertDialogBuilder.setMessage("Apakah anda ingin membatalkan pesanan ini ?");
        alertDialogBuilder
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        isCancel = true;
                        save();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();



    }


    private void save() {

        String orderId = "";
        String carId = "";
        long orderDate = 0;
        long startDate = stamp0;
        long endDate = stamp1;
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String customerName = etName.getText().toString();;
        String address = etAdress.getText().toString();
        String phone = etPhone.getText().toString();
        String carName = "";
        int carprice = 0;
        int totalPrice = Integer.parseInt(etPrice.getText().toString());
        String carImg = "";

        if(Util.isAdmin){

        }

        if(Util.isFirst){
            carImg = car.getImgUrl();
            carName = car.getName();
            carprice = car.getCost();
            carId = car.getId();
            orderDate = Util.toUnixTime(Util.getCurrentDate());
            orderId = Util.generateId();
            customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        } else {

            carImg = order.getCarImg();
            msg = "Pesanan berhasil diubah";
            carName = order.getCarName();
            carprice = order.getCarprice();
            carId = order.getCarId();
            orderDate = order.getOrderDate();
            orderId = order.getOrderId();
            customerId = order.getCustomerId();

            if(isCancel){
                status = "dibatalkan";
                msg = "Pesanan berhasil dibatalkan";
            }

        }



        Order order = new Order(carImg,status, orderId,orderDate,startDate,endDate,customerId,customerName,address,phone,carName,carprice,totalPrice, carId);

        mDatabase = FirebaseDatabase.getInstance().getReference("ORDERS");
        mDatabase.child(orderId).setValue(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    msg, Toast.LENGTH_SHORT).show();

                            if(Util.isFirst){
                                startActivity(new Intent(PlaceOrderActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                finish();
                            }


                        }
                    }
                });



    }

    private void disableEdit() {
        etDateEnd.setEnabled(false);
        etDateStart.setEnabled(false);
        etPrice.setFocusable(false);
        etPhone.setFocusable(false);
        etAdress.setFocusable(false);
        etName.setFocusable(false);
    }

    private void showDialog(EditText et, boolean isStart) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Update
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(isStart){
                    stamp0 = myCalendar.getTimeInMillis()/1000;
                } else {
                    stamp1 = myCalendar.getTimeInMillis()/1000;
                }
                et.setText(sdf.format(myCalendar.getTime()));
                updatePrice();
            }

        };

        new DatePickerDialog(PlaceOrderActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updatePrice() {
        String strDate0 = etDateStart.getText().toString();
        String strDate1 = etDateEnd.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        long date0 = Util.toUnixTime(strDate0);
        long date1 = Util.toUnixTime(strDate1);
        long duration = date1-date0;
        int day = 1;



        // cek apakah tanggal valid
        if(duration<0){
            etDateStart.setError("Tanggal tidak valid");
            etDateEnd.setError("Tanggal tidak valid");
            btnSave.setEnabled(false);
        } else {
            // cek apa tanggal valid #2
            if(date0 < Util.toUnixTime(Util.getCurrentDate())){
                Log.d("cekkkk", "updatePrice: ");
                etDateStart.setError("Tanggal tidak valid");
                etDateEnd.setError("Tanggal tidak valid");
                btnSave.setEnabled(false);
            } else {
                etDateStart.setError(null);
                etDateEnd.setError(null);
                btnSave.setEnabled(true);
                checkDateAvailability();
            }

//            etDateStart.setError(null);
//            etDateEnd.setError(null);
//            btnSave.setEnabled(true);

        }
        if(duration>0){

            day = 1 + ((int)duration / 86400);

        }

        int currPrice = 0;
        if (Util.isFirst){
           currPrice = car.getCost();
        } else {
            currPrice = order.getCarprice();
        }

        etPrice.setText(""+day * currPrice);


    }

    private void initWidget() {
        etName = findViewById(R.id.etCustName);
        etAdress = findViewById(R.id.etAdress);
        etPhone = findViewById(R.id.etPhone);
        etDateEnd = findViewById(R.id.etDateEnd);
        etDateStart = findViewById(R.id.etDateStart);
        etPrice = findViewById(R.id.etPriceTotal);
        imgCar = findViewById(R.id.imgCarThumb);
        tvCarName = findViewById(R.id.tvCarName);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSubmit);
        loader = findViewById(R.id.progressBarCheck);
        tvCheck = findViewById(R.id.tvChecking);

        etDateStart.setFocusable(false);
        etDateEnd.setFocusable(false);

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