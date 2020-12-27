package com.mobileapps.uas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mobileapps.uas.R;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgAvatar;
    EditText etName;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profil");

        initWidget();
        initClickListener();
    }

    private void initClickListener() {
       btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              updateUser();
            }
        });
    }

    private void updateUser() {
        String name = etName.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                            finish();

                        }
                    }
                });


    }

    private void initWidget() {
        imgAvatar = findViewById(R.id.imgAvatar);
        etName = findViewById(R.id.etName);
        // etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);

        String currentName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        etName.setText(currentName);
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