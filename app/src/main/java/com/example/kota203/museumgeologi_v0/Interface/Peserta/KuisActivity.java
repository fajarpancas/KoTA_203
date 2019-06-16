package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.Model.Soal;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class KuisActivity extends AppCompatActivity {

    int minutes, seconds, secondsRuang, minutesRuang;
    GridLayout mainGridAnswer;
    private TextView countdownTextKuis, txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD, txtQuestion;
    private ImageView imgAnswerA, imgAnswerB, imgAnswerC, imgAnswerD, imgQuestion;
    private CountDownTimer countDownTimer, countDownTimerRuang;
    private long timeLeftInMiliseconds = 60000; //1 mins
    private long TimeRuang;
    boolean timerRunning = true;
    String textNamaPeserta,textIdPeserta, textIdKoor;
    String hasilJawaban;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbsoal = db.collection("Soal");
    private CollectionReference dbpeserta = db.collection("peserta");
    private CollectionReference db_kuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuis);

        final String jenis_klasifikasi = "SD";
        final int klasifikasi = 11;
        final int soal = 1;

        Intent intent = getIntent();
        TimeRuang = getIntent().getLongExtra("TimeKunjungan", 0);
        textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
        textIdPeserta = intent.getStringExtra("ID_PESERTA");
        textIdKoor = intent.getStringExtra("ID_KOOR");

        TextView textViewNama = findViewById(R.id.nama_peserta_db);
        textViewNama.setText(textNamaPeserta);

        mainGridAnswer = (GridLayout) findViewById(R.id.mainGridAnswer);
        txtQuestion = (TextView) findViewById(R.id.question_text);
        txtAnswerA = (TextView) findViewById(R.id.txtAnswerA);
        txtAnswerB = (TextView) findViewById(R.id.txtAnswerB);
        txtAnswerC = (TextView) findViewById(R.id.txtAnswerC);
        txtAnswerD = (TextView) findViewById(R.id.txtAnswerD);
        imgQuestion = (ImageView) findViewById(R.id.question_image);

        countdownTextKuis = findViewById(R.id.countdown_kuis);

        checkStatusKuis();
        inputJawaban(mainGridAnswer, klasifikasi, jenis_klasifikasi, soal, textIdPeserta, textIdKoor);
        startTimerRuang();
        loadQuestion(jenis_klasifikasi, klasifikasi, soal);
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
                Toast.makeText(KuisActivity.this, "Times Up", Toast.LENGTH_SHORT).show();
                hasilJawaban = "Tidak Menjawab";
                int poin = 0;
                stopTimer();
                Toast.makeText(KuisActivity.this, "Tidak menjawab", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(KuisActivity.this, InfoJawabanActivity.class);
                intent.putExtra("NAMA_PESERTA", textNamaPeserta);
                intent.putExtra("ID_PESERTA", textIdPeserta);
                intent.putExtra("ID_KOOR", textIdKoor);
                intent.putExtra("JAWABAN", hasilJawaban);
                intent.putExtra("POIN", poin);
                startActivity(intent);
            }
        }.start();

        timerRunning = true;
    }

    public void updateTimer() {
        minutes = (int) (timeLeftInMiliseconds / 1000) / 60;
        seconds = (int) (timeLeftInMiliseconds / 1000) % 60;

        String timeLeft;
        timeLeft = ""+seconds;

        String timeLeftText;
        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownTextKuis.setText(timeLeftText);
    }

    public void stopTimerKunjungan() {
        countDownTimerRuang.cancel();
    }

    public void startTimerRuang() {
        countDownTimerRuang = new CountDownTimer(TimeRuang, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeRuang = millisUntilFinished;
                updateTimerRuang();
            }

            @Override
            public void onFinish() {
                Toast.makeText(KuisActivity.this, "Times Up", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(KuisActivity.this, InfoJawabanActivity.class);
//                intent.putExtra("NAMA_PESERTA", textNamaPeserta);
//                intent.putExtra("ID_PESERTA", textIdPeserta);
//                intent.putExtra("ID_KOOR", textIdKoor);
//                startActivity(intent);
            }
        }.start();

        timerRunning = true;
    }

    public void updateTimerRuang() {
        minutesRuang = (int) (TimeRuang / 1000) / 60;
        secondsRuang = (int) (TimeRuang / 1000) % 60;
    }

    private void loadQuestion(String jenis_klasifikasi, int klasifikasi, int soal) {
        dbsoal.whereEqualTo("jenis_klasifikasi", jenis_klasifikasi)
                .whereEqualTo("id_klasifikasi",klasifikasi)
                .whereEqualTo("id_soal", soal)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    String answerA = "";
                    String answerB = "";
                    String answerC = "";
                    String answerD = "";
                    String img_question = "R.drawable.";
                    String txt_question = "";

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Soal getSoal = documentSnapshot.toObject(Soal.class);

                        String var_img_question = getSoal.getImg_question();
                        String var_txt_question = getSoal.getTxt_question();
                        String var_answerA = getSoal.getAnswerA();
                        String var_answerB = getSoal.getAnswerB();
                        String var_answerC = getSoal.getAnswerC();
                        String var_answerD = getSoal.getAnswerD();

                        img_question += var_img_question;
                        txt_question += var_txt_question;
                        answerA += var_answerA;
                        answerB += var_answerB;
                        answerC += var_answerC;
                        answerD += var_answerD;
                    }

                    txtQuestion.setText(txt_question);
                    txtAnswerA.setText(answerA);
                    txtAnswerB.setText(answerB);
                    txtAnswerC.setText(answerC);
                    txtAnswerD.setText(answerD);
//                    imgQuestion.setImageResource(img_question);
                    Toast.makeText(KuisActivity.this, img_question, Toast.LENGTH_SHORT).show();
                    if(timerRunning){
                        startTimer();
                    }else {
                        stopTimer();
                    }
                }
            });
    }

    private void inputJawaban(GridLayout mainGridAnswer, final int klasifikasi, final String jenis_klasifikasi, final int soal,
                                final String IdPeserta, final String IdKoor) {
        //loop all child
        for(int i=0;i<mainGridAnswer.getChildCount();i++){
            CardView cardView = (CardView)mainGridAnswer.getChildAt(i);
            final int Answer = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Answer == 0) {
                        cekJawaban("A", klasifikasi,jenis_klasifikasi, soal, IdPeserta, IdKoor);
                        Toast.makeText(KuisActivity.this, "A", Toast.LENGTH_SHORT).show();
                    } else if (Answer == 1) {
                        cekJawaban("B", klasifikasi,jenis_klasifikasi, soal, IdPeserta, IdKoor);
                        Toast.makeText(KuisActivity.this, "B", Toast.LENGTH_SHORT).show();
                    } else if (Answer == 2){
                        cekJawaban("C", klasifikasi,jenis_klasifikasi, soal, IdPeserta, IdKoor);
                        Toast.makeText(KuisActivity.this, "C", Toast.LENGTH_SHORT).show();
                    } else if (Answer == 3) {
                        cekJawaban("D", klasifikasi,jenis_klasifikasi, soal, IdPeserta, IdKoor);
                        Toast.makeText(KuisActivity.this, "D", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void cekJawaban(final String jawaban, int klasifikasi, String jenis_klasifikasi, int soal, final String IdPeserta, final String IdKoor) {
        dbsoal.whereEqualTo("jenis_klasifikasi", jenis_klasifikasi)
        .whereEqualTo("id_klasifikasi",klasifikasi)
        .whereEqualTo("id_soal", soal)
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Soal getSoal = documentSnapshot.toObject(Soal.class);

                    String var_answer = getSoal.getCorrectAnswer();

                    if(var_answer.equals(jawaban)){
                        hasilJawaban = "Jawaban Benar";
                        stopTimer();
                        updatePoin();
                        int poin = seconds * 10;
                        Intent intent = new Intent(KuisActivity.this, InfoJawabanActivity.class);
                        intent.putExtra("NAMA_PESERTA", textNamaPeserta);
                        intent.putExtra("ID_PESERTA", textIdPeserta);
                        intent.putExtra("ID_KOOR", textIdKoor);
                        intent.putExtra("JAWABAN", hasilJawaban);
                        intent.putExtra("POIN", poin);
                        startActivity(intent);
                    }else if(!var_answer.equals(jawaban)){
                        hasilJawaban = "Jawaban Salah";
                        stopTimer();
                        int poin = 0;
                        Toast.makeText(KuisActivity.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(KuisActivity.this, InfoJawabanActivity.class);
                        intent.putExtra("NAMA_PESERTA", textNamaPeserta);
                        intent.putExtra("ID_PESERTA", textIdPeserta);
                        intent.putExtra("ID_KOOR", textIdKoor);
                        intent.putExtra("JAWABAN", hasilJawaban);
                        intent.putExtra("POIN", poin);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void updatePoin() {
        dbpeserta.whereEqualTo("id_peserta", textIdPeserta).whereEqualTo("id_koordinator", textIdKoor)
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Peserta data_peserta = documentSnapshot.toObject(Peserta.class);
                    data_peserta.setDocumentId(documentSnapshot.getId());

                    String idDocumentPeserta = data_peserta.getDocumentId();
                    String idPeserta = data_peserta.getId_peserta();
                    String idKoordinator = data_peserta.getId_koordinator();
                    String namaPesesrta = data_peserta.getNama_peserta();
                    Integer kelompok = data_peserta.getKelompok();
                    Integer poin = data_peserta.getPoin();

                    int poinTotal = poin + (seconds* 10);

                    Peserta update_peserta_db = new Peserta(idKoordinator, idPeserta, namaPesesrta, kelompok, poinTotal);
                    dbpeserta.document(idDocumentPeserta).set(update_peserta_db);
                }
            }
        });
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
                            stopTimerKunjungan();
                            stopTimer();
                            Intent intent = new Intent(KuisActivity.this, KuisSelesaiActivity.class);
                            intent.putExtra("ID_PESERTA", textIdPeserta);
                            intent.putExtra("ID_KOOR", textIdKoor);
                            startActivity(intent);
                        }
                    }
                }
            });
    }
}
