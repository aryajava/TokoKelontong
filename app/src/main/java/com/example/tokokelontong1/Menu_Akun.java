package com.example.tokokelontong1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Menu_Akun extends AppCompatActivity {
    BottomNavigationView nav_view;
    TextView namaLengkap,email,noHp,alamat;
    Button logoutBtn,pesananSaya;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_akun);

        namaLengkap = findViewById(R.id.namaLengkap);
        email = findViewById(R.id.email);
        noHp = findViewById(R.id.noHp);
        alamat = findViewById(R.id.alamat);
        logoutBtn = findViewById(R.id.logout);
        nav_view = findViewById(R.id.nav_view);

        pesananSaya = findViewById(R.id.pesananSaya);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


            if(fAuth.getCurrentUser() == null){
                logoutBtn.setVisibility(View.INVISIBLE);
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Anda harus login terlebih dahulu")
                        .setConfirmText("Login")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                startActivity(new Intent(getApplicationContext(),Login.class));
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }else {
                userId = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection("users").document(userId);

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        namaLengkap.setText(value.getString("nLengkap"));
                        email.setText(value.getString("email"));
                        noHp.setText(value.getString("nohp"));
                        alamat.setText(value.getString("alamat"));
                    }
                });

                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }

         pesananSaya.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),Pesanan_Saya.class));
             }
         });

        nav_view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home: startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    break;
                case R.id.navigation_akun: startActivity(new Intent(getApplicationContext(),Menu_Akun.class));
                    break;
                case R.id.navigation_keranjang: startActivity(new Intent(getApplicationContext(),Menu_Keranjang.class));
                    break;
            }
            return false;
        });

    }
}