package com.example.tokokelontong1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Detail_Produk extends AppCompatActivity {
    TextView test,test1;
    ImageView image;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseStorage fStorage;
    String id;
    String uId;
    String gambar;
    int num = 0;
    Button tambah;
    ElegantNumberButton jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        tambah = findViewById(R.id.tambah);
        jumlah = findViewById(R.id.jumlah);
        test = findViewById(R.id.test);
        test1 = findViewById(R.id.test1);
        image = findViewById(R.id.image);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        id = getIntent().getExtras().getString("ID");


        DocumentReference documentReference = fStore.collection("Products").document(id);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                test.setText(value.getString("Nama"));
                test1.setText(value.get("Harga").toString());
                gambar = value.getString("gambar");

                final long ONE_MEGABYTE = 1024 * 1024;

                fStorage.getReference(gambar).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.setImageBitmap(bm);
                    }
                });
            }
        });

        jumlah.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = Integer.parseInt(jumlah.getNumber());
            }
        });



        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fAuth.getCurrentUser() != null) {
                    uId = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("cart").document(uId);


                    Map<String, Object> cart = new HashMap<>();

                    cart.put("nama", test.getText().toString());
                    cart.put("harga", Integer.parseInt(test1.getText().toString()));
                    cart.put("gambar", gambar);
                    cart.put("jumlah", num);


                    if (num > 0) {
                        documentReference.collection("Products").document(id).set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Berhasil menambahkan data", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Jumlah tidak boleh kosong", Toast.LENGTH_LONG).show();
                    }

                }else{
                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Anda harus login terlebih dahulu")
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
            }
        });

    }
}