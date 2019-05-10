package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
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
    Button btn_mulai_kuis, btn_hentikan_kuis;
    String status_kuis_mulai = "mulai";
    String status_kuis_selesai = "selesai";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kelompok);

        Intent intent = getIntent();
        final String textNama = intent.getStringExtra(ManajemenKuisActivity.EXTRA_TEXT_NAMA);
        final String textKode = intent.getStringExtra(ManajemenKuisActivity.EXTRA_TEXT_KODE);

        checkStatusKuis(textKode);

        btn_mulai_kuis = (Button) findViewById(R.id.mulai_kuis);
        btn_mulai_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusKuistoStart(textKode);
            }
        });

        btn_hentikan_kuis = (Button) findViewById(R.id.hentikan_kuis);
        btn_hentikan_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setToDB(textKode, textNama);
            }
        });
    }

    public void updateStatusKuistoStart(String IdKoor){
        dbkuis.whereEqualTo("id_koordinator", IdKoor)
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
                      dbkuis.document(idDocumentKuis).set(update_status_kuis);
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
                            if (status_kuis.equals(status_kuis_mulai) && !status_kuis.equals("belum mulai") && !status_kuis.equals(status_kuis_selesai)) { //mulai
//                                btn_mulai_kuis.setVisibility(View.INVISIBLE);
//                                btn_hentikan_kuis.setVisibility(View.VISIBLE);
                                btn_mulai_kuis.setEnabled(false);
                                btn_hentikan_kuis.setEnabled(true);
                            } else if(status_kuis.equals("belum mulai") && !status_kuis.equals(status_kuis_mulai) && !status_kuis.equals(status_kuis_selesai)){ //belum mulai
//                                btn_mulai_kuis.setVisibility(View.VISIBLE);
//                                btn_hentikan_kuis.setVisibility(View.INVISIBLE);
                                btn_mulai_kuis.setEnabled(true);
                                btn_hentikan_kuis.setEnabled(false);
                            }
                        }
                    }
                });
    }
}
