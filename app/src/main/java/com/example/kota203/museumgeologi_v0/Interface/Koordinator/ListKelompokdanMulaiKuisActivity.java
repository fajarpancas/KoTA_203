package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
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
    Button btn_mulai_kuis, btn_hentikan_kuis, btn_kelompok1, btn_kelompok2, btn_kelompok3, btn_kelompok4, btn_kelompok5;
    String status_kuis_mulai = "mulai";
    String status_kuis_selesai = "selesai";
    String kelompok;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kelompok);

        Intent intent = getIntent();
        final String textNama = intent.getStringExtra("NAMA_KOOR");
        final String textIdKoor = intent.getStringExtra("ID_KOOR");

        TextView textViewNama = (TextView)findViewById(R.id.nama_koordinator_db);
        TextView textViewId = (TextView)findViewById(R.id.id_koordinator_db);

        textViewNama.setText("Hi, "+ textNama);
        textViewId.setText("Kode Permainan : "+ textIdKoor);

        checkStatusKuis(textIdKoor);
        dataPeserta(textIdKoor);

        btn_mulai_kuis = (Button) findViewById(R.id.mulai_kuis);
        btn_mulai_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusKuistoStart(textIdKoor);
            }
        });

        btn_hentikan_kuis = (Button) findViewById(R.id.hentikan_kuis);
        btn_hentikan_kuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setToDB(textKode, textNama);
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
                openActivityListNama(textIdKoor, textNama, 1);
            }
        });
        btn_kelompok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(textIdKoor, textNama, 2);
            }
        });
        btn_kelompok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(textIdKoor, textNama, 3);
            }
        });
        btn_kelompok4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(textIdKoor, textNama, 4);
            }
        });
        btn_kelompok5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListNama(textIdKoor, textNama, 5);
            }
        });
    }

    public void openActivityListNama(String idKoor, String namaKoor, int i) {
        Intent intent = new Intent(ListKelompokdanMulaiKuisActivity.this, ListNamaSetiapKelompokKuisActivity.class);
        intent.putExtra("ID_KOOR", idKoor);
        intent.putExtra("NAMA_KOOR", namaKoor);
        intent.putExtra("KELOMPOK", i);
        startActivity(intent);
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

    public void dataPeserta(String IdKoor){
        dbkuis.whereEqualTo("id_koordinator", IdKoor)
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
}
