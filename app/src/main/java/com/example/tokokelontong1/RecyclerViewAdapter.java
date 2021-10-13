package com.example.tokokelontong1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.NumberFormat;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList idList;
    private ArrayList namaProdukList;
    private ArrayList hargaList;
    private ArrayList gambarList;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase fDatabase;
    FirebaseStorage fStorage;
    String uId;


    Context context;



    RecyclerViewAdapter(ArrayList idList, ArrayList namaProdukList, ArrayList hargaList,ArrayList gambarList){
        this.idList = idList;
        this.namaProdukList = namaProdukList;
        this.hargaList = hargaList;
        this.gambarList = gambarList;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView gambar;
        private TextView namaProduk,harga;
        private Button btnCart;
        private CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            gambar = itemView.findViewById(R.id.gambar);
            namaProduk = itemView.findViewById(R.id.namaProduk);
            harga = itemView.findViewById(R.id.harga);
            card = itemView.findViewById(R.id.card);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fStorage = FirebaseStorage.getInstance();
        NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
        nf.setGroupingUsed(true);



        final String Id = (String) idList.get(position);
        final String nama = (String) namaProdukList.get(position);
        final long harga = (long) hargaList.get(position);
        final String gambar = (String) gambarList.get(position);

        holder.namaProduk.setText(nama);
        holder.harga.setText("Rp. "+String.valueOf(nf.format(harga)));
        final long ONE_MEGABYTE = 1024 * 1024;

        fStorage.getReference(gambar).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.gambar.setImageBitmap(bm);
            }
        });


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(v.getContext(),Detail_Produk.class);
                detail.putExtra("ID",Id);
                context.startActivity(detail);
            }
        });


//        holder.btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uId = fAuth.getCurrentUser().getUid();
//                DocumentReference documentReference = fStore.collection("cart").document(uId);
//
//                Map<String,Object> cart = new HashMap<>();
//
//                cart.put("nama",nama);
//                cart.put("harga",harga);
//
//                documentReference.collection("Products").document().set(cart);
//
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return namaProdukList.size();
    }

}
