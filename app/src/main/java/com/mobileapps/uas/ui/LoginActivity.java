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
import com.mobileapps.uas.R;



public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvRegister;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLoginStatus();

        initWidgets();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void checkLoginStatus() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String email = user.getEmail();
            checkRole(email);
        }

    }

    private void Login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("tag", "Login success");
                    Toast.makeText(LoginActivity.this, "Login Sukses",
                            Toast.LENGTH_SHORT).show();

                    // check role
                    checkRole(email);



                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("tag", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Login Gagal.",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });



    }

    private void checkRole(String email) {
        if(email.equals("admin@gmail.com")){
            startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
            finish();
        } else {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }


    private void initWidgets() {
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword =findViewById(R.id.etPassword);

    }
}