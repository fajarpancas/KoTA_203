package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InfoKelompokActivity extends AppCompatActivity {

    String status_kuis_mulai = "mulai";
    private int kelompok_info;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");
    private CollectionReference dbpeserta = db.collection("peserta");
    private TextView textCountdownPersiapan, textViewKlasifikasi, textViewKetKelompok, textViewNamaPeserta ;

    private CountDownTimer countDownTimerPersiapan;
    private long timeLeftInMilisecondsPersiapan = 10000;

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

        textViewKlasifikasi = (TextView) findViewById(R.id.klasifikasi);
        textViewKetKelompok = (TextView) findViewById(R.id.keterangan);
        textViewNamaPeserta = (TextView) findViewById(R.id.nama_peserta_db);
        textCountdownPersiapan = findViewById(R.id.countdown_persiapan_kuis_peserta);

        textViewNamaPeserta.setText(textNamaPeserta);

        getDataKuis(textIdKoor, textIdPeserta);
        checkStatusKuis(textNamaPeserta, textIdPeserta, textIdKoor);
    }

    private void openPetunjukKuisActivity(String namaPeserta, String idPeserta, String idKoor) {
        Intent intent = new Intent(InfoKelompokActivity.this, PetunjukActivity.class);
        intent.putExtra("NAMA_PESERTA", namaPeserta);
        intent.putExtra("ID_PESERTA", idPeserta);
        intent.putExtra("ID_KOOR", idKoor);
        intent.putExtra("KELOMPOK", kelompok_info);
        startActivity(intent);
    }

    public void startTimerPersiapan(final String NamaKoor, final String IdPeserta, final String IdKoor) {
        countDownTimerPersiapan = new CountDownTimer(timeLeftInMilisecondsPersiapan, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilisecondsPersiapan = l;
                updateTimerPersiapan();
            }

            @Override
            public void onFinish() {
                openPetunjukKuisActivity(NamaKoor, IdPeserta, IdKoor);
            }
        }.start();
    }

    private void updateTimerPersiapan() {
        int minutes = (int) timeLeftInMilisecondsPersiapan / 60000;
        int seconds = (int) timeLeftInMilisecondsPersiapan % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "Kuis akan dimulai pada hitungan : " + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        textCountdownPersiapan.setText(timeLeftText);
    }



    private void getDataKuis(String idKoor, String idPeserta){
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
        getDataPeserta(idPeserta, textViewKetKelompok);
    }

    private void getDataPeserta(String idPeserta, final TextView textViewKetKelompok) {
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

                                kelompok_info = data_peserta.getKelompok();
                                kelompok += "Kelompok : " +kelompok_info;
                                textViewKetKelompok.setText(kelompok);
                            }else {
                                textViewKetKelompok.setText("Tunggu Koordinator membagikan kelompok");
                            }
                        }
                    }
                });
    }

    public void checkStatusKuis(final String textNamaPeserta, final String textIdPeserta, final String textIdKoor ){
        dbkuis.whereEqualTo("id_koordinator", textIdKoor)
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
                    startTimerPersiapan(textNamaPeserta, textIdPeserta, textIdKoor);
                }
            }
            }
        });
    }
}
