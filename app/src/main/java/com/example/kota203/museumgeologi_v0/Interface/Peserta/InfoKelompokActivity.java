package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Koordinator.LoginKoordinatorActivity;
import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InfoKelompokActivity extends AppCompatActivity {

    String status_kuis_mulai = "mulai";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");
    private CollectionReference dbpeserta = db.collection("peserta");

    Button btn_mulai_kuis_peserta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kelompok);

        Intent intent = getIntent();
        final String textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
//        Toast.makeText(InfoKelompokActivity.this, textNamaPeserta, Toast.LENGTH_SHORT).show();
        final String textIdPeserta = intent.getStringExtra("ID_PESERTA");
//        Toast.makeText(InfoKelompokActivity.this, textIdPeserta, Toast.LENGTH_SHORT).show();
        final String textIdKoor = intent.getStringExtra("ID_KOOR");
//        Toast.makeText(InfoKelompokActivity.this, textIdKoor, Toast.LENGTH_SHORT).show();

        TextView textViewKlasifikasi = (TextView) findViewById(R.id.klasifikasi);
        TextView textViewKetKelompok = (TextView) findViewById(R.id.keterangan);
        TextView textViewNamaPeserta = (TextView) findViewById(R.id.nama_peserta_db);

        textViewNamaPeserta.setText(textNamaPeserta);

        getData(textIdKoor, textIdPeserta, textViewKlasifikasi, textViewKetKelompok);
        checkStatusKuis(textIdKoor);

        btn_mulai_kuis_peserta = (Button)findViewById(R.id.btn_mulai_kuis_peserta);
        btn_mulai_kuis_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPetunjukKuisActivity(textNamaPeserta, textIdPeserta, textIdKoor);
            }
        });
    }

    private void openPetunjukKuisActivity(String namaPeserta, String idPeserta, String idKoor) {
        Intent intent = new Intent(InfoKelompokActivity.this, PetunjukKuisActivity.class);
        intent.putExtra("NAMA_PESERTA", namaPeserta);
        intent.putExtra("ID_PESERTA", idPeserta);
        intent.putExtra("ID_KOOR", idKoor);
        startActivity(intent);
    }

    private void getData(String idKoor, String idPeserta, final TextView textViewKlasifikasi, final TextView textViewKetKelompok){
        dbkuis.whereEqualTo("id_koordinator", idKoor)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    textViewKlasifikasi.setText("Listen Failed");
                    return;
                }
                String klasifikasi = "";
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("jenis_klasifikasi") != null) {
                        Kuis data_kuis = doc.toObject(Kuis.class);

                        String jenis_klasifikasi = data_kuis.getJenis_klasifikasi();
                        klasifikasi += "Jenis Klasifikasi Kuis : " +jenis_klasifikasi;
                        textViewKlasifikasi.setText(klasifikasi);
                    }else {
                        textViewKlasifikasi.setText("Belum ditentukan");
                    }
                }
            }
        });
        dbpeserta.whereEqualTo("id_peserta", idPeserta)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    textViewKetKelompok.setText("Listen Failed");
                    return;
                }
                String kelompok = "";
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("kelompok") != null) {
                        Peserta data_peserta = doc.toObject(Peserta.class);

                        Integer kelompok_nomor = data_peserta.getKelompok();
                        kelompok += "Kelompok : " +kelompok_nomor;
                        textViewKetKelompok.setText(kelompok);
                    }else {
                        textViewKetKelompok.setText("Tunggu Koordinator membagikan kelompok");
                    }
                }
            }
        });
    }

    public void checkStatusKuis(String textKode){
        dbkuis.whereEqualTo("id_koordinator", textKode)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
            if (e != null){
//                            textViewTotal.setText("Listen Failed");
                return;
            }
            for (QueryDocumentSnapshot doc : value) {
                Kuis status = doc.toObject(Kuis.class);
                String status_kuis = status.getStatus_kuis();
                if (status_kuis.equals(status_kuis_mulai)) { //mulai
                    btn_mulai_kuis_peserta.setEnabled(true);
                } else{  //belum mulai
                    btn_mulai_kuis_peserta.setEnabled(false);
                }
            }
            }
        });
    }
}
