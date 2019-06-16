package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class InfoJawabanActivity extends AppCompatActivity {

    private TextView textViewIcon, textViewCoundown, textViewPoin, textViewHasilJawaban;
    private String textNamaPeserta, textIdPeserta, textIdKoor, textHasilJawaban;
    private int textPoin;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbpeserta = db.collection("peserta");
    private CollectionReference db_kuis = db.collection("kuis");

    private CountDownTimer countDownTimer;
    private long timeLeftInMiliseconds = 3000; //1 mins
    boolean timerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_jawaban);

        Intent intent = getIntent();
        textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
        textIdPeserta = intent.getStringExtra("ID_PESERTA");
        textIdKoor = intent.getStringExtra("ID_KOOR");
        textHasilJawaban = intent.getStringExtra("JAWABAN");
        textPoin = getIntent().getExtras().getInt("POIN");

        textViewPoin = findViewById(R.id.poin_didapat);
        textViewIcon = findViewById(R.id.icon_jawaban);
        textViewCoundown = findViewById(R.id.countdown_kembali_ke_informasi_koleksi);
        textViewHasilJawaban = findViewById(R.id.keterangan_hasil_jawaban);

        checkStatusKuis();
        showPoin();
    }

    private void showPoin() {
        if(textPoin != 0){
            textViewIcon.setBackgroundResource(R.drawable.ic_sentiment_very_satisfied_black_24dp);
        }
        if(textPoin == 0){
            textViewIcon.setBackgroundResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
        }

        textViewHasilJawaban.setText(textHasilJawaban);
        String dataPoin = "Poin yang anda dapatkan : " + textPoin;
        textViewPoin.setText(dataPoin);
        startTimer();
    }

    public void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMiliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMiliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
//                Intent intent = new Intent(InfoJawabanActivity.this, InformasiKoleksiActivity.class);
//                startActivity(intent);
                Toast.makeText(InfoJawabanActivity.this, "yes", Toast.LENGTH_SHORT).show();
            }
        }.start();

        timerRunning = true;
    }

    public void updateTimer() {
        int minutes = (int) (timeLeftInMiliseconds / 1000) / 60;
        int seconds = (int) (timeLeftInMiliseconds / 1000) % 60;

        String timeLeft;
        timeLeft = ""+seconds;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        textViewCoundown.setText(timeLeftText);
    }

    public void checkStatusKuis(){
        db_kuis.whereEqualTo("id_koordinator", textIdKoor)
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
                        stopTimer();
                        Intent intent = new Intent(InfoJawabanActivity.this, KuisSelesaiActivity.class);
                        intent.putExtra("ID_PESERTA", textIdPeserta);
                        intent.putExtra("ID_KOOR", textIdKoor);
                        startActivity(intent);
                    }
                }
                }
            });
    }
}
