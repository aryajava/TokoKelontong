package com.example.tokokelontong1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView registerText;
    EditText mEmail,mPassword;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.registerText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogin);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Validasi jika ada form yang kosong
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    if(TextUtils.isEmpty(email)) mEmail.setError("Email tidak boleh kosong!");
                    if(TextUtils.isEmpty(password)) mPassword.setError("Password tidak boleh kosong!");
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(), "User tidak terdaftar", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        registerText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Register.class)));
    }
}