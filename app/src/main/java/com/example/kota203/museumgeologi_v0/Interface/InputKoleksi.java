package com.example.kota203.museumgeologi_v0.Interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Koordinator.LoginKoordinatorActivity;
import com.example.kota203.museumgeologi_v0.Interface.Koordinator.ManajemenKuisActivity;
import com.example.kota203.museumgeologi_v0.Interface.Peserta.LoginPesertaActivity;
import com.example.kota203.museumgeologi_v0.Model.Koleksi;
import com.example.kota203.museumgeologi_v0.Model.Koordinator;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class InputKoleksi extends AppCompatActivity {

    MaterialEditText idRuang, idKlasifikasi, idKoleksi, namaKoleksi, deskripsiKoleksi, gambar;
    Button btn_input_data;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkoleksi = db.collection("koleksi");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_koleksi);

        idRuang = (MaterialEditText)findViewById(R.id.idRuang);
        idKlasifikasi = (MaterialEditText)findViewById(R.id.idKlasifikasi);
        idKoleksi = (MaterialEditText)findViewById(R.id.idKoleksi);
        namaKoleksi = (MaterialEditText)findViewById(R.id.namaKoleksi);
        deskripsiKoleksi = (MaterialEditText)findViewById(R.id.deskripsiKoleksi);
        gambar = (MaterialEditText)findViewById(R.id.gambar);

        btn_input_data = (Button)findViewById(R.id.btn_input_data);
        btn_input_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData(idRuang.getText().toString(), idKlasifikasi.getText().toString(), idKoleksi.getText().toString(),
                            namaKoleksi.getText().toString(), deskripsiKoleksi.getText().toString(), gambar.getText().toString());
            }
        });

    }

    private void inputData(String idRuang, String idKlasifikasi, String idKoleksi, String namaKoleksi, String deskripsi, String gambar) {

        int IDruang = Integer.parseInt(idRuang);
        int IDklas = Integer.parseInt(idKlasifikasi);
        int IDkoleksi = Integer.parseInt(idKoleksi);
        if(!TextUtils.isEmpty(gambar)){
            Koleksi koleksi = new Koleksi(IDruang, IDklas, IDkoleksi, namaKoleksi, deskripsi, gambar);
            dbkoleksi.add(koleksi);
            Toast.makeText(this, "Input Data Berhasil", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Data Belum Terisi Semua", Toast.LENGTH_LONG).show();
        }
    }

}
