package com.example.kota203.museumgeologi_v0.Interface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class InputKoleksi extends AppCompatActivity {

    MaterialEditText idRuang, idKlasifikasi, idKoleksi, namaKoleksi, deskripsiKoleksi, gambar;
    Button btn_sign_in_peserta;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkoleksi = db.collection("koleksi");
    private CollectionReference dbruang = db.collection("ruang");
    private CollectionReference dbklasifikasi = db.collection("klasifikasi");

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

    }

    private void setDataKoleksitoDB(int idRuang, int idKlasifikasi, int idKoleksi, String namaKoleksi, String deskripsiKoleksi, final String gambar) {
        final Koleksi peserta = new Peserta(idKoor, id_pesertaS, namaPeserta);
        dbpeserta.add(peserta);
    }
}
