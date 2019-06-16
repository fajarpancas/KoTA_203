package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InformasiKoleksiActivity extends AppCompatActivity {
    private TextView countdownTextRuang, countdownTextToKuis;

    private CountDownTimer countDownTimerRuang, countDownTimerToKuis;
    private long timeLeftInMilisecondsRuang = 180000; //3 mins
    private long timeLeftInMilisecondsToKuis = 10000;
    boolean timerRuang = true;
    boolean timerKuis = true;
    boolean KuisAvailable = false;
    String textNamaPeserta, textIdPeserta, textIdKoor;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_koleksi);

        Intent intent = getIntent();
        textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
        textIdPeserta = intent.getStringExtra("ID_PESERTA");
        textIdKoor = intent.getStringExtra("ID_KOOR");

        TextView textViewKlasifikasi = (TextView) findViewById(R.id.klasifikasi);
        TextView textViewNamaPeserta = (TextView) findViewById(R.id.nama_peserta_db);

        countdownTextRuang = findViewById(R.id.timer_kunjungan);
        countdownTextToKuis = findViewById(R.id.timer_to_kuis);
        if(timerRuang){
            startTimerRuang();
        }
        if(timerKuis){
            startTimerToKuis();
        }

        textViewNamaPeserta.setText(textNamaPeserta);

        checkStatusKuis();
        getDataKuis(textIdKoor, textViewKlasifikasi);
    }

    public void stopTimerKunjungan() {
        countDownTimerRuang.cancel();
    }

    public void startTimerRuang() {
        countDownTimerRuang = new CountDownTimer(timeLeftInMilisecondsRuang, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilisecondsRuang = l;
                updateTimerRuang();
            }

            @Override
            public void onFinish() {
                Toast.makeText(InformasiKoleksiActivity.this, "Times Up", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRuang = true;
    }

    public void updateTimerRuang() {
        int minutesRuang = (int) (timeLeftInMilisecondsRuang / 1000) / 60;
        int secondsRuang = (int) (timeLeftInMilisecondsRuang / 1000) % 60;

        String timeLeftText;
        timeLeftText = "" + minutesRuang;
        timeLeftText += ":";
        if (secondsRuang < 10) timeLeftText += "0";
        timeLeftText += secondsRuang;

        countdownTextRuang.setText(timeLeftText);
    }

    public void stopTimerToKuis() {
        countDownTimerToKuis.cancel();
    }

    public void startTimerToKuis() {
        countDownTimerToKuis = new CountDownTimer(timeLeftInMilisecondsToKuis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilisecondsToKuis = l;
                updateTimerToKuis();
            }

            @Override
            public void onFinish() {
                cekKuis();
                Toast.makeText(InformasiKoleksiActivity.this, "Times Up (to Kuis)", Toast.LENGTH_SHORT).show();
                if(KuisAvailable == true){
                    KuisAvailable = false;
                    Intent i = new Intent(InformasiKoleksiActivity.this, KuisActivity.class);
                    i.putExtra("NAMA_PESERTA", textNamaPeserta);
                    i.putExtra("ID_PESERTA", textIdPeserta);
                    i.putExtra("ID_KOOR", textIdKoor);
                    i.putExtra("TimeKunjungan", timeLeftInMilisecondsRuang);
                    startActivity(i);
                }
            }
        }.start();

        timerKuis = true;
    }

    private void cekKuis() {
        //cek ke databse
        Toast.makeText(InformasiKoleksiActivity.this, "Ada Kuis", Toast.LENGTH_SHORT).show();
        KuisAvailable = true;
    }

    public void updateTimerToKuis() {
        int minutesToKuis = (int) (timeLeftInMilisecondsToKuis / 1000) / 60;
        int secondsToKuis = (int) (timeLeftInMilisecondsToKuis / 1000) % 60;

        String timeLeftText;
        timeLeftText = "" + minutesToKuis;
        timeLeftText += ":";
        if (secondsToKuis < 10) timeLeftText += "0";
        timeLeftText += secondsToKuis;

        countdownTextToKuis.setText(timeLeftText);
    }

    private void getDataKuis(String textIdKoor, final TextView textViewKlasifikasi) {
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

    public void checkStatusKuis(){
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
                            if(status_kuis.equals("dihentikan")){
                                stopTimerKunjungan();
                                stopTimerToKuis();
                                Intent intent = new Intent(InformasiKoleksiActivity.this, KuisSelesaiActivity.class);
                                intent.putExtra("ID_PESERTA", textIdPeserta);
                                intent.putExtra("ID_KOOR", textIdKoor);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }
}

