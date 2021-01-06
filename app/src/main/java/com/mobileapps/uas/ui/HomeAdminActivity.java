package com.mobileapps.uas.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.mobileapps.uas.R;
import com.mobileapps.uas.Util;

public class HomeAdminActivity extends AppCompatActivity {

    ImageButton btnLogout, btnOrderList, btnReport, btnCarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        Util.isAdmin = true;
        initWidget();
        initClickListener();
    }

    private void initWidget() {
        btnLogout = findViewById(R.id.btnLogout);
        btnOrderList = findViewById(R.id.btnOrderList);
        btnReport = findViewById(R.id.btnReport);
        btnCarList = findViewById(R.id.btnCarList);
    }

    private void initClickListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.logout();
                startActivity(new Intent(HomeAdminActivity.this, LoginActivity.class));
                finish();
            }
        });
        btnOrderList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.isFirst = false;
                startActivity(new Intent(HomeAdminActivity.this,OrderListActivity.class));
            }
        });
        btnCarList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(HomeAdminActivity.this,CarsListActivity.class));
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Util.isFirst = false;
                startActivity(new Intent(HomeAdminActivity.this,ReportActivity.class));
            }
        });
    }
}