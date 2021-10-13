package com.example.tokokelontong1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder>{

    private ArrayList idList;
    private ArrayList namaProdukList;
    private ArrayList hargaList;
    private ArrayList gambarList;
    private ArrayList jumlahList;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase fDatabase;
    FirebaseStorage fStorage;
    String uId,view;

    Context context;



    RecyclerViewAdapter1(ArrayList idList, ArrayList namaProdukList, ArrayList hargaList,ArrayList gambarList,ArrayList jumlahList,String view){
        this.idList = idList;
        this.namaProdukList = namaProdukList;
        this.hargaList = hargaList;
        this.gambarList = gambarList;
        this.jumlahList = jumlahList;
        this.view = view;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView gambar,remove;
        private TextView namaProduk,harga,jumlah;


        ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            gambar = itemView.findViewById(R.id.gambar);
            namaProduk = itemView.findViewById(R.id.namaProduk);
            harga = itemView.findViewById(R.id.harga);
            jumlah = itemView.findViewById(R.id.jumlah);
            remove = itemView.findViewById(R.id.remove);

        }
    }

    @Override
    public RecyclerViewAdapter1.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View V;

        if(view.equals("1")){
            V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design1, parent, false);
        }else{
            V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design2, parent, false);
        }

        return new RecyclerViewAdapter1.ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter1.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fStorage = FirebaseStorage.getInstance();



        final String Id = (String) idList.get(position);
        final String nama = (String) namaProdukList.get(position);
        final long harga = (long) hargaList.get(position);
        final long jumlah = (long) jumlahList.get(position);
        final String gambar = (String) gambarList.get(position);

        holder.namaProduk.setText(nama);
        holder.harga.setText(String.valueOf(harga));
        holder.jumlah.setText("Jumlah : "+String.valueOf(jumlah));


        final long ONE_MEGABYTE = 1024 * 1024;

        fStorage.getReference(gambar).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.gambar.setImageBitmap(bm);
            }
        });

        if(view.equals("1")){
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uId = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("cart").document(uId).collection("Products").document(Id);;

                    documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Data dihapus", Toast.LENGTH_LONG).show();
                            Intent main = new Intent(v.getContext(),MainActivity.class);
                            context.startActivity(main);
                        }
                    });
                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return namaProdukList.size();
    }
}
