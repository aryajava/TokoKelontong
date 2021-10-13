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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextView loginText;
    EditText mNamaLengkap,mEmail,mPassword,mNoHp,mAlamat;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNamaLengkap = findViewById(R.id.namaLengkap);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mNoHp = findViewById(R.id.noHp);
        mAlamat = findViewById(R.id.alamat);
        mRegisterBtn = findViewById(R.id.registerButton);

        loginText = findViewById(R.id.loginText);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(v -> {
            String namaLengkap = mNamaLengkap.getText().toString();
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String noHp = mNoHp.getText().toString();
            String alamat = mAlamat.getText().toString();

            // Validasi jika ada form yang kosong
            if (TextUtils.isEmpty(namaLengkap) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(noHp) || TextUtils.isEmpty(alamat)){
                if (TextUtils.isEmpty(namaLengkap)) mNamaLengkap.setError("Nama lengkap harus diisi!");
                if (TextUtils.isEmpty(email)) mEmail.setError("Email harus diisi!");
                if (TextUtils.isEmpty(password)) mPassword.setError("Password harus diisi");
                if (TextUtils.isEmpty(noHp)) mNoHp.setError("No Handphone harus diisi!");
                if (TextUtils.isEmpty(alamat)) mAlamat.setError("Alamat harus diisi!");
            }else{
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("nLengkap",namaLengkap);
                            user.put("email",email);
                            user.put("nohp",noHp);
                            user.put("alamat",alamat);
                            documentReference.set(user);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }
                });
            }


        });

        loginText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),Login.class)));
    }
}