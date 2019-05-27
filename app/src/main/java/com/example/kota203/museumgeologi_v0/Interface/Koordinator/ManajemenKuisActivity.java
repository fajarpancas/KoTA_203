package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
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

import java.util.ArrayList;
import java.util.List;

public class ManajemenKuisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String klasifikasi;
    int kelompok;
    String statuKuis= "belum mulai";

    Button btn_generate_kelompok, btn_keluar;
//    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
//    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference peserta = db.collection("peserta");
    private CollectionReference dbkoordinator = db.collection("koordinator");
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_kuis);

        Intent intent = getIntent();
        final String textNamaKoor = intent.getStringExtra("NAMA_KOOR");
        final String textIDKoor = intent.getStringExtra("ID_KOOR");
//        final String textKodeKoor = "h0y5";

        TextView textViewNama = (TextView)findViewById(R.id.nama_koordinator_db);
        TextView textViewKode = (TextView)findViewById(R.id.id_koordinator_db);
        final TextView textViewTotal = (TextView)findViewById(R.id.jumlah_peserta_masuk);

        getTotalPesertaMasuk(textIDKoor, textViewTotal);

        textViewNama.setText("Hi, "+ textNamaKoor);
        textViewKode.setText("Kode Permainan : "+ textIDKoor);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_klasifikasi);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,
                R.array.klasifikasi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Spinner spinnerkelompok = (Spinner) findViewById(R.id.spinner_kelompok);
        final ArrayAdapter<CharSequence> adapterkelompok = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,
                R.array.kelompok_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkelompok.setAdapter(adapterkelompok);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //menggunakan nilai posisi dari spinner
                switch (position)
                {
                    case 0:
                        klasifikasi = "TK";
                        break;

                    case 1:
                        klasifikasi = "SD";
                        break;

                    case 2:
                        klasifikasi = "SMP";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerkelompok.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //menggunakan nilai posisi dari spinner
                switch (position)
                {
                    case 0:
                        kelompok = 1;
                        break;

                    case 1:
                        kelompok = 2;
                        break;

                    case 2:
                        kelompok = 3;
                        break;

                    case 3:
                        kelompok = 4;
                        break;

                    case 4:
                        kelompok = 5;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_generate_kelompok = (Button) findViewById(R.id.btn_generate_kelompok);
        btn_generate_kelompok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityListKelompokdanMulaiKuis(textIDKoor, textNamaKoor);
            }
        });

        btn_keluar = (Button) findViewById(R.id.btn_keluar);

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    private void openActivityListKelompokdanMulaiKuis(String textIDKoor, String textNamaKoor) {
        pembagianKelompok(textIDKoor);
        createKuis(textIDKoor);
        Intent intent = new Intent(ManajemenKuisActivity.this, ListKelompokdanMulaiKuisActivity.class);
        intent.putExtra("ID_KOOR", textIDKoor);
        intent.putExtra("NAMA_KOOR", textNamaKoor);
        startActivity(intent);
    }

    private void createKuis(String textIDKoor) {
        Kuis kuis = new Kuis(textIDKoor, kelompok, klasifikasi, statuKuis);
        dbkuis.document().set(kuis);
    }

    private void pembagianKelompok(String textIDKoor){
        peserta.whereEqualTo("id_koordinator", textIDKoor)
        .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int i = 1;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if (i > kelompok) {
                        i = 1;
                        Peserta data_peserta = documentSnapshot.toObject(Peserta.class);
                        data_peserta.setDocumentId(documentSnapshot.getId());

                        String idDocumentPeserta= data_peserta.getDocumentId();
                        String idPeserta = data_peserta.getId_peserta();
                        String idKoordinator = data_peserta.getId_koordinator();
                        String namaPesesrta = data_peserta.getNama_peserta();

                        Peserta update_peserta_db = new Peserta(idKoordinator, idPeserta, namaPesesrta, i);
                        peserta.document(idDocumentPeserta).set(update_peserta_db);

                        i++;
                        //
                    }else {
                        Peserta data_peserta = documentSnapshot.toObject(Peserta.class);
                        data_peserta.setDocumentId(documentSnapshot.getId());

                        String idDocumentPeserta = data_peserta.getDocumentId();
                        String idPeserta = data_peserta.getId_peserta();
                        String idKoordinator = data_peserta.getId_koordinator();
                        String namaPesesrta = data_peserta.getNama_peserta();

                        Peserta update_peserta_db = new Peserta(idKoordinator, idPeserta, namaPesesrta, i);
                        peserta.document(idDocumentPeserta).set(update_peserta_db);

                        i++;
//                                textViewKlasifikasi.setText(klasifikasi);
                    }
                }
            }
        });
    }

    //deteksi realtime firestore
    private void getTotalPesertaMasuk(String kodeKoor, final TextView textViewTotal){
        peserta.whereEqualTo("id_koordinator", kodeKoor)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    textViewTotal.setText("Listen Failed");
                    return;
                }
                String data = "";
                int i = 0;
                for (QueryDocumentSnapshot doc : value) {
                    i++;
                }
                data += i;
                textViewTotal.setText(data);
             }
        });
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
            .setMessage("Keluar Permainan ?")
            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Toast.makeText(ManajemenKuisActivity.this, "Not Logout", Toast.LENGTH_SHORT).show();
                }
            })
            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Toast.makeText(ManajemenKuisActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
