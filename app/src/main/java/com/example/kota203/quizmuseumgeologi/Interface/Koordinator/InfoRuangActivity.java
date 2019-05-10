package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class InfoRuangActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("klasifikasi");

    private TextView textViewData;
    String nama_ruang;
    Button btn_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_intro);

        Intent intent = getIntent();
        int nomor_ruang = getIntent().getExtras().getInt("Nomor_Ruang");
        cekNamaRuang(nomor_ruang);
        loadDataRuang(nomor_ruang);

        TextView textViewNomor = (TextView)findViewById(R.id.nomor_ruang);
        TextView textViewNama = (TextView)findViewById(R.id.nama_ruang);

        textViewNomor.setText("Nomor Ruang : "+ nomor_ruang);
        textViewNama.setText("Nama Ruang : "+ nama_ruang);
        textViewData = (TextView)findViewById(R.id.textViewData);

        btn_kembali = (Button)findViewById(R.id.btn_kembali);
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoIntroActivity();
            }
        });
    }

    private void loadDataRuang(int nomor_ruang) {
        notebookRef.whereEqualTo("id_ruang", nomor_ruang)
            .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        int i = 1;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Klasifikasi klasifikasi = documentSnapshot.toObject(Klasifikasi.class);
                            klasifikasi.setDocumentId(documentSnapshot.getId());

                            String documentId = klasifikasi.getDocumentId();
                            int ruang = klasifikasi.getId_ruang();
                            int klas = klasifikasi.getId_klasifikasi();
                            String namaKlas = klasifikasi.getNama_klasifikasi();

                            data += i + ". " + namaKlas + "\n\n";
                            i++;
                        }
                        textViewData.setText(data);
                    }
                });
    }

    private void backtoIntroActivity() {
        Intent i = new Intent(InfoRuangActivity.this, IntroActivity.class);
        startActivity(i);
    }

    private void cekNamaRuang(int nomor_ruang) {
        if (nomor_ruang == 1){
            nama_ruang = "Ruang Geologi Indonesia";
        } else if (nomor_ruang == 2){
            nama_ruang = "Ruang Sumber Daya Geologi";
        } else if (nomor_ruang == 3){
            nama_ruang = "Ruang Manfaat dan Bencana Geologi";
        } else if (nomor_ruang == 4){
            nama_ruang = "Ruang Sejarah Kehidupan";
        }
    }
}
