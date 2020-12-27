package com.mobileapps.uas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;
import com.mobileapps.uas.model.Car;

public class MainActivity extends AppCompatActivity {
    private int time = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        // insertData();
        setSplash();


    }

    private void setSplash() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(MainActivity.this, LoginActivity.class);
                startActivity(home);
                finish();

            }
        },time);
    }


    void insertData(){
        String idStr = "car"+ Util.generateId();
        Car car = new Car(idStr,"Honda Jazz", 2015,500000, "firzaelbuho.github.io/assets/img/rental",true);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CARS");
        mDatabase.child(idStr).setValue(car)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }
}