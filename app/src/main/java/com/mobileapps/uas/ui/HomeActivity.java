package com.mobileapps.uas.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;

public class HomeActivity extends AppCompatActivity {

    TextView tvname;
    ImageView imgAvatar;
    ImageButton btnCar, btnMyOrder, btnProfile, btnAbout, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Util.isAdmin = false;
        initWidgets();
        updateUi();
        initClickListener();


    }

    private void initClickListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.logout();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnCar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CarsListActivity.class));
            }
        });
        btnMyOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.isFirst = false;
                startActivity(new Intent(HomeActivity.this,OrderListActivity.class));
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AboutActivity.class));
            }
        });
    }

    private void updateUi() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            tvname.setText(user.getDisplayName());
            if(user.getPhotoUrl() != null){
                Glide.with(imgAvatar)
                        .load(user.getPhotoUrl())
                        .into(imgAvatar);
            }
            Log.d("uri photo", user.getPhotoUrl()+"") ;
        }
    }

    private void initWidgets() {
        imgAvatar = findViewById(R.id.circleImageView);
        btnLogout = findViewById(R.id.btnLogout);
        tvname = findViewById(R.id.name);
        btnCar = findViewById(R.id.btnCar);
        btnMyOrder = findViewById(R.id.btnMyOrder);
        btnProfile = findViewById(R.id.btnProfile);
        btnAbout = findViewById(R.id.btnAbout);


    }
}