package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Koordinator.ListKelompokdanMulaiKuisActivity;
import com.example.kota203.museumgeologi_v0.Interface.Koordinator.ManajemenKuisActivity;
import com.example.kota203.museumgeologi_v0.Model.Alur;
import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PetunjukActivity extends AppCompatActivity {
    private TextView countdownText;
    private CountDownTimer countDownTimer;
//    private long timeLeftInMiliseconds = 180000; //3 mins
private long timeLeftInMiliseconds = 6000;

    private TextView textViewPetunjuk, textNamaRuang, textInfoLokasiSalah;
    private ImageView imgPetunjuk;
    Button btn_lanjutkan;
    Boolean btn_lanjutkanKlik = false;
    Boolean runningTimer;

    boolean beaconDetect = false;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");
    private CollectionReference dbalur = db.collection("alur");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petunjuk);

        Intent intent = getIntent();
        final String textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
        final String textIdPeserta = intent.getStringExtra("ID_PESERTA");
//        final String textIdKoor = intent.getStringExtra("ID_KOOR");
        final String textIdKoor = "h0y5";
//        final int kelompok = getIntent().getExtras().getInt("KELOMPOK");
        final int kelompok = 1;

        TextView textViewKlasifikasi = (TextView) findViewById(R.id.klasifikasi);
        TextView textViewNamaPeserta = (TextView) findViewById(R.id.nama_peserta_db);
        textViewPetunjuk = (TextView) findViewById(R.id.petunjuk);
        textNamaRuang =(TextView) findViewById(R.id.nama_ruang_petunjuk);
        textInfoLokasiSalah = (TextView) findViewById(R.id.info_lokasi_salah);
        imgPetunjuk = (ImageView) findViewById(R.id.imgPetunjuk);

        countdownText = findViewById(R.id.countdown_petunjuk_ruang);

        getData(textIdKoor, textViewKlasifikasi);

        textViewNamaPeserta.setText(textNamaPeserta);

        btn_lanjutkan = (Button) findViewById(R.id.btn_lanjutkan);


        btn_lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_lanjutkanKlik){
                    cekLokasi();
                }else{
                    PetunjukRuang(kelompok, textIdKoor);
                    startTimer();
                }
            }
        });
    }

    private void cekLokasi() {
        beaconDetect = true;
        Toast.makeText(PetunjukActivity.this, "Cek Lokasi Procedure", Toast.LENGTH_SHORT).show();
    }

    private void PetunjukRuang(final int kelompok, final String idKoordinator) {
        Toast.makeText(PetunjukActivity.this, idKoordinator + kelompok, Toast.LENGTH_SHORT).show();
        dbalur.whereEqualTo("id_koordinator", idKoordinator).whereEqualTo("kelompok", kelompok)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                int imgIndex = 0;
                String infoRuang = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Alur get_alur = documentSnapshot.toObject(Alur.class);
                    get_alur.setDocumentId(documentSnapshot.getId());

                    String getDocumentId = get_alur.getDocumentId();
                    int alurfromdb = get_alur.getAlur();
                    int alurKe = get_alur.getAlur_ke();

                    alurKe = alurKe + 1;

                    if(alurKe <= 5){
                        String alur1 = "Lantai 1 sebelah kiri dari pintu masuk museum";
                        String alur2 = "Lantai 1 sebelah kiri dari pintu masuk museum";
                        String alur3 = "Lantai 2 sebelah kanan dari pintu masuk museum";
                        String alur4 = "Lantai 2 sebelah kiri dari pintu masuk museum";
                        String alur5 = "Lantai 1 sebelah kanan dari pintu masuk museum";

                        if(alurfromdb == 1){ infoRuang = alur1; }
                        if(alurfromdb == 2){ infoRuang = alur2; }
                        if(alurfromdb == 3){ infoRuang = alur3; }
                        if(alurfromdb == 4){ infoRuang = alur4; }
                        if(alurfromdb == 5){ infoRuang = alur5; }

                        imgIndex = alurfromdb;
                        data += "Anda harus menuju ke Ruang " + alurfromdb + ", yang berada di " + infoRuang;
                        if(imgIndex == 1){
                            textNamaRuang.setText("Ruang Geologi Indonesia Bagian 1");
                            imgPetunjuk.setImageResource(R.drawable.img_petunjuk_geologi_indonesia_bagian_1);
                        }else if(imgIndex == 2) {
                            textNamaRuang.setText("Ruang Geologi Indonesia Bagian 2");
                            imgPetunjuk.setImageResource(R.drawable.img_petunjuk_geologi_indonesia_bagian_2);
                        }else if(imgIndex == 3) {
                            textNamaRuang.setText("Ruang Sumber Daya Geologi");
                            imgPetunjuk.setImageResource(R.drawable.img_petunjuk_sumber_daya_geologi);
                        }else if(imgIndex == 4) {
                            textNamaRuang.setText("Ruang Manfaat dan Bencana Geologi");
                            imgPetunjuk.setImageResource(R.drawable.img_petunjuk_manfaat_dan_bencana);
                        }else if(imgIndex == 5) {
                            textNamaRuang.setText("Ruang Sejarah Kehidupan");
                            imgPetunjuk.setImageResource(R.drawable.img_petunjuk_sejarah_kehidupan);
                        }
                        textViewPetunjuk.setText(data);

                        alurfromdb = alurfromdb + 1;
                        if(alurfromdb == 6){
                            alurfromdb = 1;
                        }
                        Alur update_alur = new Alur(idKoordinator, kelompok, alurfromdb, alurKe);
                        dbalur.document(getDocumentId).set(update_alur);
                    }else if(alurKe > 5){
                        Toast.makeText(PetunjukActivity.this, "intent ke ranking", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PetunjukActivity.this, PeringkatActivity.class);
                        startActivity(intent);
                    }
                }
            }

        });

        btn_lanjutkan.setVisibility(View.INVISIBLE);

    }
//
//    public void Timer(){
//        if(runningTimer){
//            startTimer();
//        }
//    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMiliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(PetunjukActivity.this, "Times Up", Toast.LENGTH_SHORT).show();
                cekLokasi();
                if(beaconDetect){
                    Intent intent = new Intent(PetunjukActivity.this, InformasiKoleksiActivity.class);
                    startActivity(intent);
                }else{
                    btn_lanjutkanKlik = true;
                    btn_lanjutkan.setText("Cek ulang lokasi");
                    textInfoLokasiSalah.setText("Anda belum berada di lokasi yang sesuai");
                    btn_lanjutkan.setVisibility(View.VISIBLE);
                }
            }
        }.start();
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMiliseconds / 60000;
        int seconds = (int) timeLeftInMiliseconds % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    private void getData(String textIdKoor, final TextView textViewKlasifikasi) {
        dbkuis.whereEqualTo("id_koordinator", textIdKoor)
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
    }
}
