package com.example.tokokelontong1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Menu_Keranjang extends AppCompatActivity {
    TextView txtTotal;
    Button btnProses;
    BottomNavigationView nav_view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList idList;
    private ArrayList namaProdukList;
    private ArrayList hargaList;
    private ArrayList jumlahList;
    private ArrayList gambarList;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    int total = 0;
    int totalBelanja = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_keranjang);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        idList = new ArrayList<>();
        namaProdukList = new ArrayList<>();
        hargaList = new ArrayList<>();
        jumlahList = new ArrayList<>();
        gambarList = new ArrayList<>();
        txtTotal = findViewById(R.id.totalBelanja);
        btnProses = findViewById(R.id.btnBeli);
        recyclerView = findViewById(R.id.recycler1);
        nav_view = findViewById(R.id.nav_view);


        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        if(fAuth.getCurrentUser() != null) {

            userId = fAuth.getCurrentUser().getUid();

            CollectionReference collectionReference = fStore.collection("cart").document(userId).collection("Products");

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

                        adapter = new RecyclerViewAdapter1(idList, namaProdukList, hargaList, gambarList, jumlahList,"1");
                        recyclerView.setAdapter(adapter);


                        txtTotal.setText("Total : " + nf.format(totalBelanja));

                    }
                }
            });

            btnProses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(totalBelanja > 0){
                        startActivity(new Intent(getApplicationContext(), Proses.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Keranjang kosong!", Toast.LENGTH_LONG).show();
                    }

//                    DocumentReference documentReference = fStore.collection("Order").document(userId);
//
//                    Map<String, Object> product = new HashMap<>();
//                    for (int i = 0; i < idList.size(); i++) {
//                        product.put("Pesanan-" + (i + 1), namaProdukList.get(i) + " Jumlah : " + jumlahList.get(i));
//                    }
//
//                    product.put("Total", totalBelanja);
//                    documentReference.collection("Products").document().set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(getApplicationContext(), "Berhasil melakukan pemesanan", Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        }
//                    });
                }
            });
        }else {
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