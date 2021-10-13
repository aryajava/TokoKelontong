package com.example.tokokelontong1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    TextView test;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button Logout;
    BottomNavigationView nav_view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList idList;
    private ArrayList namaProdukList;
    private ArrayList hargaList;
    private ArrayList gambarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav_view = findViewById(R.id.nav_view);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        idList = new ArrayList<>();
        namaProdukList = new ArrayList<>();
        hargaList = new ArrayList<>();
        gambarList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);




        CollectionReference collectionReference = fStore.collection("Products");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        idList.add(document.getId());
                        namaProdukList.add(document.getString("Nama"));
                        hargaList.add(document.get("Harga"));
                        gambarList.add(document.get("gambar"));
                    }

                    adapter = new RecyclerViewAdapter(idList,namaProdukList,hargaList,gambarList);
                    recyclerView.setAdapter(adapter);

                }
            }
        });



        nav_view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.navigation_home: startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
                case R.id.navigation_keranjang: startActivity(new Intent(getApplicationContext(),Menu_Keranjang.class));
                break;
                case R.id.navigation_akun: startActivity(new Intent(getApplicationContext(),Menu_Akun.class));
                break;
            }
            return false;
        });

    }
}