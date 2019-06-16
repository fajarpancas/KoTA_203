package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Peserta.InformasiKoleksiActivity;
import com.example.kota203.museumgeologi_v0.Interface.Peserta.PetunjukActivity;
import com.example.kota203.museumgeologi_v0.Model.Alur;
import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListKelompokdanMulaiKuisActivity extends AppCompatActivity {
    Button btn_mulai_kuis, btn_hentikan_kuis, btn_kelompok1, btn_kelompok2, btn_kelompok3, btn_kelompok4, btn_kelompok5;
    String status_kuis_mulai = "mulai";
    String status_kuis_hentikan = "dihentikan";
    int jumlah_kelompok;
    int indexAlurKe = 1;

    private TextView countdownText, infoAlur;
    private CountDownTimer countDownTimer, countDownTimerPersiapan, countDownTimerPetunjukKuis;
    //    private long timeLeftInMiliseconds = 180000; //3 mins
    private long timeLeftInMiliseconds = 12000;
    private long timeLeftInMilisecondsPersiapan = 10000;
    private long timeLeftInMilisecondsPetunjukKuis = 10000;

    Boolean runningTimer;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference db_kuis = db.collection("kuis");
    private CollectionReference db_alur = db.collection("alur");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kelompok);

        Intent intent = getIntent();
        final String nama_koor = intent.getStringExtra("NAMA_KOOR");
        final String id_koor = intent.getStringExtra("ID_KOOR");
        jumlah_kelompok = getIntent().getExtras().getInt("KELOMPOK");

        TextView textViewNama = (TextView)findViewById(R.id.nama_koordinator_db);
        TextView textViewId = (TextView)findViewById(R.id.id_koordinator_db);

        textViewNama.setText("Hi, "+ nama_koor);
        textViewId.setText("Kode Permainan : "+ id_koor);

        checkStatusKuis(id_koor);
        dataPeserta(id_koor);

        countdownText = findViewById(R.id.countdown_pergantian_alur);
        infoAlur = findViewById(R.id.alur_saat_ini);

        btn_mulai_kuis = (Button) findViewById(R.id.mulai_kuis);
        btn_mulai_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusKuistoStart(id_koor);
            }
        });

        btn_hentikan_kuis = (Button) findViewById(R.id.hentikan_kuis);
        btn_hentikan_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogHentikanKuis(id_koor);
            }
        });

        btn_kelompok1 = (Button) findViewById((R.id.btn_kelompok1));
        btn_kelompok2 = (Button) findViewById((R.id.btn_kelompok2));
        btn_kelompok3 = (Button) findViewById((R.id.btn_kelompok3));
        btn_kelompok4 = (Button) findViewById((R.id.btn_kelompok4));
        btn_kelompok5 = (Button) findViewById((R.id.btn_kelompok5));

        btn_kelompok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(id_koor, nama_koor, 1);
            }
        });
        btn_kelompok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(id_koor, nama_koor, 2);
            }
        });
        btn_kelompok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(id_koor, nama_koor, 3);
            }
        });
        btn_kelompok4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(id_koor, nama_koor, 4);
            }
        });
        btn_kelompok5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(id_koor, nama_koor, 5);
            }
        });
    }

    public void openActivityListNama(String id_koor, String nama_koor, int i) {
        Intent intent = new Intent(ListKelompokdanMulaiKuisActivity.this, ListNamaSetiapKelompokKuisActivity.class);
        intent.putExtra("ID_KOOR", id_koor);
        intent.putExtra("NAMA_KOOR", nama_koor);
        intent.putExtra("KELOMPOK", i);
        startActivity(intent);
    }

    public void startTimer(final String id_koor) {
        if(indexAlurKe <= 5){
            countDownTimer = new CountDownTimer(timeLeftInMiliseconds, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftInMiliseconds = l;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    if(indexAlurKe < 5){
                        updateAlur(id_koor);
                        timeLeftInMiliseconds = 12000;
                        startTimer(id_koor);
                        indexAlurKe = indexAlurKe +1;
                    }else{
                        Toast.makeText(ListKelompokdanMulaiKuisActivity.this, "Kuis End", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        }
    }

    public void startTimerPersiapan(final String id_koor) {
        countDownTimerPersiapan = new CountDownTimer(timeLeftInMilisecondsPersiapan, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilisecondsPersiapan = l;
                updateTimerPersiapan();
            }

            @Override
            public void onFinish() {
                startTimerPetunjukKuis(id_koor);
            }
        }.start();
    }

    public void startTimerPetunjukKuis(final String id_koor) {
        countDownTimerPetunjukKuis = new CountDownTimer(timeLeftInMilisecondsPetunjukKuis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilisecondsPetunjukKuis = l;
                updateTimerPetunjukKuis();
            }

            @Override
            public void onFinish() {
                getAlurSaatIni(id_koor);
                startTimer(id_koor);
            }
        }.start();
    }

    private void updateTimerPetunjukKuis() {
        int minutes = (int) timeLeftInMilisecondsPetunjukKuis / 60000;
        int seconds = (int) timeLeftInMilisecondsPetunjukKuis % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "Informasi petunjuk kuis : " + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    private void updateTimerPersiapan() {
        int minutes = (int) timeLeftInMilisecondsPersiapan / 60000;
        int seconds = (int) timeLeftInMilisecondsPersiapan % 60000 / 1000;

        String timeLeftText;
        timeLeftText = "Kuis akan dimulai pada hitungan : " + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
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

    private void updateAlur(String id_koor) {
        int i;
        for (i = 1; i<=jumlah_kelompok; i++) {
            db_alur.whereEqualTo("id_koordinator", id_koor).whereEqualTo("kelompok", i)
            .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Alur dataAlur = documentSnapshot.toObject(Alur.class);
                            dataAlur.setDocumentId(documentSnapshot.getId());

                            String idDocumentPeserta = dataAlur.getDocumentId();
                            String idKoordinator = dataAlur.getId_koordinator();
                            int kel = dataAlur.getKelompok();
                            int alur = dataAlur.getAlur();
                            int alurKe = dataAlur.getAlur_ke();

                            alur = alur+1;
                            if(alur == 6){
                                alur = 1;
                            }
                            alurKe = alurKe+1;

                            Alur update_alur = new Alur(idKoordinator, kel, alur, alurKe);
                            db_alur.document(idDocumentPeserta).set(update_alur);
                        }
                    }
                });
            }
    }

    //deteksi realtime firestore
    private void getAlurSaatIni(String id_koor){
        db_alur.whereEqualTo("id_koordinator", id_koor).whereEqualTo("kelompok", 1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            infoAlur.setText("Listen Failed");
                            return;
                        }
                        String data = "";
                        for (QueryDocumentSnapshot doc : value) {
                            Alur dataAlur = doc.toObject(Alur.class);

                            int alurKe = dataAlur.getAlur_ke();
                            data += "Perpindahan alur ke " + alurKe;
                        }
                        infoAlur.setText(data);
                    }
                });
    }

    public void updateStatusKuistoStart(final String id_koor){
        db_kuis.whereEqualTo("id_koordinator", id_koor)
        .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
              @Override
              public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                  for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                      Kuis data = documentSnapshot.toObject(Kuis.class);
                      data.setDocumentId(documentSnapshot.getId());

                      String idDocumentKuis = data.getDocumentId();
                      String id_koor = data.getId_koordinator();
                      Integer jumlahKelompok = data.getJumlah_kelompok();
                      String jenisKlasifikasi = data.getJenis_klasifikasi();

                      Kuis update_status_kuis = new Kuis(id_koor, jumlahKelompok, jenisKlasifikasi, status_kuis_mulai);
                      db_kuis.document(idDocumentKuis).set(update_status_kuis);
                  }
              startTimerPersiapan(id_koor);
              }
          });
    }

    public void checkStatusKuis(String id_koor){
        db_kuis.whereEqualTo("id_koordinator", id_koor)
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
                    if (status_kuis.equals(status_kuis_mulai) && !status_kuis.equals("Belum mulai")) { //mulai
//                                btn_mulai_kuis.setVisibility(View.INVISIBLE);
//                                btn_hentikan_kuis.setVisibility(View.VISIBLE);
                        btn_mulai_kuis.setEnabled(false);
                        btn_hentikan_kuis.setEnabled(true);
                    } else if(status_kuis.equals("Belum mulai") && !status_kuis.equals(status_kuis_mulai)){ //belum mulai
//                                btn_mulai_kuis.setVisibility(View.VISIBLE);
//                                btn_hentikan_kuis.setVisibility(View.INVISIBLE);
                        btn_mulai_kuis.setEnabled(true);
                        btn_hentikan_kuis.setEnabled(false);
                    }
                }
            }
        });
    }

    public void setButton(int kelompok){
        if(kelompok == 1){
//                        btn_kelompok1.setEnabled(true);
            btn_kelompok2.setVisibility(View.INVISIBLE);
            btn_kelompok3.setVisibility(View.INVISIBLE);
            btn_kelompok4.setVisibility(View.INVISIBLE);
            btn_kelompok5.setVisibility(View.INVISIBLE);
        }else if(kelompok == 2){
//                        btn_kelompok1.setEnabled(true);
//                        btn_kelompok2.setEnabled(true);
            btn_kelompok3.setVisibility(View.INVISIBLE);
            btn_kelompok4.setVisibility(View.INVISIBLE);
            btn_kelompok5.setVisibility(View.INVISIBLE);
        }else if(kelompok == 3){
//                        btn_kelompok1.setEnabled(true);
//                        btn_kelompok2.setEnabled(true);
//                        btn_kelompok3.setEnabled(true);
            btn_kelompok4.setVisibility(View.INVISIBLE);
            btn_kelompok5.setVisibility(View.INVISIBLE);
        }else if(kelompok == 4){
//                        btn_kelompok1.setEnabled(true);
//                        btn_kelompok2.setEnabled(true);
//                        btn_kelompok3.setEnabled(true);
//                        btn_kelompok4.setEnabled(true);
            btn_kelompok5.setVisibility(View.INVISIBLE);
        }else if(kelompok == 5){
//                        btn_kelompok1.setEnabled(true);
//                        btn_kelompok2.setEnabled(true);
//                        btn_kelompok3.setEnabled(true);
//                        btn_kelompok4.setEnabled(true);
//                        btn_kelompok5.setEnabled(true);
        }
    }

    public void dataPeserta(String id_koor){
        db_kuis.whereEqualTo("id_koordinator", id_koor)
        .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i = 1;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Kuis kuis = documentSnapshot.toObject(Kuis.class);

                    int kelompok = kuis.getJumlah_kelompok();
                    setButton(kelompok);
                }
            }
        });
    }

    public void updateStatusKuistoStop(String id_koor){
        db_kuis.whereEqualTo("id_koordinator", id_koor)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int i = 1;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Kuis data_kuis = documentSnapshot.toObject(Kuis.class);
                            data_kuis.setDocumentId(documentSnapshot.getId());

                            String idDocumentKuis = data_kuis.getDocumentId();
                            String id_koor = data_kuis.getId_koordinator();
                            Integer jumlahKelompok = data_kuis.getJumlah_kelompok();
                            String jenisKlasifikasi = data_kuis.getJenis_klasifikasi();

                            Kuis update_status_kuis = new Kuis(id_koor, jumlahKelompok, jenisKlasifikasi, status_kuis_hentikan);
                            db_kuis.document(idDocumentKuis).set(update_status_kuis);
                        }
                    }
                });
    }

    private void alertDialogHentikanKuis(final String id_koor){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
        .setMessage("Hentikan Kuis?")
        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                updateStatusKuistoStop(id_koor);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
