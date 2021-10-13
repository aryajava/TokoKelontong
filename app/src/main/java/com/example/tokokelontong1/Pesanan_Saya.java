package com.example.tokokelontong1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Pesanan_Saya extends AppCompatActivity {

    BottomNavigationView nav_view;
    Button btnBeli;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList idList;
    private ArrayList namaProdukList;
    private ArrayList hargaList;
    private ArrayList jumlahList;
    private ArrayList gambarList;
    String userId;

    int total = 0;
    int totalBelanja = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_saya);

        idList = new ArrayList<>();
        namaProdukList = new ArrayList<>();
        hargaList = new ArrayList<>();
        jumlahList = new ArrayList<>();
        gambarList = new ArrayList<>();

        nav_view = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycler);
        btnBeli = findViewById(R.id.btnBeli);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        if(fAuth.getCurrentUser() == null){
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


            CollectionReference collectionReference = fStore.collection("pesananSaya").document(userId).collection("Products");

            collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            idList.add(document.getId());
                            namaProdukList.add(document.getString("nama"));
                            hargaList.add(document.get("harga"));
                            jumlahList.add(document.get("jumlah"));
                            gambarList.add(document.getString("gambar"));
                            total = Integer.parseInt(document.get("harga").toString()) * Integer.parseInt(document.get("jumlah").toString());
                            totalBelanja += total;
                        }

                        adapter = new RecyclerViewAdapter1(idList, namaProdukList, hargaList, gambarList, jumlahList,"2");
                        recyclerView.setAdapter(adapter);



                    }
                }
            });

        }



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