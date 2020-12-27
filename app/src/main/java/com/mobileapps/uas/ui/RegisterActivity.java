package com.mobileapps.uas.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mobileapps.uas.R;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView tvLogin;
    EditText etName, etEmail, etPassword, etPasswordConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initWidgets();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Register();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void Register() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();

        if(password.equals(passwordConfirm)){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegisterActivity.this, "Berhasil Membuat akun.",
                                        Toast.LENGTH_SHORT).show();
                                updateUser();


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("tag", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Gagal Membuat akun.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }

    }

    private void updateUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString())
                // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();

                        }
                    }
                });



    }

    private void initWidgets() {
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword =findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPassword2);
    }
}