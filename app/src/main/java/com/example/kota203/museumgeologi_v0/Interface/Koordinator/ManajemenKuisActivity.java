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

import com.example.kota203.museumgeologi_v0.Model.Alur;
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
    String statuKuis= "Belum mulai";
    TextView textViewTotal;

    Button btn_generate_kelompok, btn_keluar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();         //menghubungkan ke firebase cloud firestore
    private CollectionReference db_peserta = db.collection("peserta");   //menghubungkan ke cloud firestore koleksi peserta
    private CollectionReference db_kuis = db.collection("kuis");         //menghubungkan ke cloud firestore koleksi kuis
    private CollectionReference db_alur = db.collection("alur");         //menghubungkan ke cloud firestore koleksi alur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_kuis);                   //menampilkan xml manajemen kuis

        Intent intent = getIntent();                                        //mengambil intent dari activity verifikasi koordinator
        final String nama_koor = intent.getStringExtra("NAMA_KOOR");  //mengisi var nama_koor dengan var NAMA_KOOR yang dikirim dari activity sebelumnya
        final String id_koor = intent.getStringExtra("ID_KOOR");      //mengisi var id_koor dengan var ID_KOOR yang dikirim dari activity sebelumnya

        TextView textViewNamaKoor = (TextView)findViewById(R.id.nama_koordinator_db);   //deklarasi textViewNamaKoor diisi dengan form dengan id -> nama koordinator pada xml
        TextView textViewIdKoor = (TextView)findViewById(R.id.id_koordinator_db);       //deklarasi textViewIdKoor diisi dengan form dengan id -> id koordinator pada xml
        textViewTotal = (TextView)findViewById(R.id.jumlah_peserta_masuk);              //deklarasi textViewTotal diisi dengan form dengan id -> jumlah_peserta_masuk pada xml

        getTotalPesertaMasuk(id_koor);           //memanggil procedure getTotalPesertaMasuk

        textViewNamaKoor.setText("Hi, "+ nama_koor);                                    //menampilkan nama koor pada textViewNamaKoor
        textViewIdKoor.setText("Kode verifikasi untuk gabung kuis : "+ id_koor);        //menampilkan id koor pada textViewIDKoor

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_klasifikasi);                                           //deklarasi spinner diisi dengan id spinner_klasifikasi pada xml
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,      //deklarasi array adapter untuk menampilkan spinner klasifikasi
                R.array.klasifikasi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                                     //menampilkan dropdown/spinner jenis klasifikasi
        spinner.setAdapter(adapter);

        final Spinner spinnerkelompok = (Spinner) findViewById(R.id.spinner_kelompok);                                          //deklarasi spinner diisi dengan id spinner_kelompok pada xml
        final ArrayAdapter<CharSequence> adapterkelompok = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,  //deklarasi array adapter untuk menampilkan spinner kelompok
                R.array.kelompok_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                                         //menampilkan dropdown/spinner jumlah kelompok
        spinnerkelompok.setAdapter(adapterkelompok);

        //Adapter view untuk menampilkan spinner/dropdown
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //menggunakan nilai posisi dari spinner
                switch (position)
                {
                    case 0:
                        klasifikasi = "TK";             //dropdown klasifikasi TK
                        break;
                    case 1:
                        klasifikasi = "SD";             //dropdown klasifikasi SD
                        break;
                    case 2:
                        klasifikasi = "SMP";            //dropdown klasifikasi SMP
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
                        kelompok = 1;                   //dropdown kelompok 1
                        break;
                    case 1:
                        kelompok = 2;                   //dropdown kelompok 2
                        break;
                    case 2:
                        kelompok = 3;                   //dropdown kelompok 3
                        break;
                    case 3:
                        kelompok = 4;                   //dropdown kelompok 4
                        break;
                    case 4:
                        kelompok = 5;                   //dropdown kelompok 5
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {                         //jika tidak ada yang dipilih

            }
        });

        btn_generate_kelompok = (Button) findViewById(R.id.btn_generate_kelompok);              //deklarasi button untuk melakukan proses manajemen
        btn_generate_kelompok.setEnabled(false);                                                //set button disable / tidak dapat di klik
        btn_generate_kelompok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                    //jika button diklik
                openActivityListKelompokdanMulaiKuis(id_koor, nama_koor);                       //memanggil procedure open activity list kelompok dan mulai kuis
            }
        });

        btn_keluar = (Button) findViewById(R.id.btn_keluar);                                    //deklarasi button untuk logout atau keluar dari peran koordinator

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                                    //jika button di klik
                showAlertDialog();                                                              //menampilkan pop up konfirmasi logout
            }
        });
    }

    //procedure untuk pindah activity ke activity Lisk kelompok dan mulai kuis
    private void openActivityListKelompokdanMulaiKuis(String id_koor, String nama_koor) {
        pembagianKelompok(id_koor);                                                                                     //memnaggil procedure pembagian kelompok
        penentuanAlur(id_koor);                                                                                         //memanggil procedure penentuan alur
        updateKuis(id_koor);                                                                                            //memanggil procedure update kuis
        Intent intent = new Intent(ManajemenKuisActivity.this, ListKelompokdanMulaiKuisActivity.class);   //deklarasi intent untuk perpindah activity dari manajemen kelompok ke list kelompok dan mulai kuis
        intent.putExtra("ID_KOOR", id_koor);                                                                      //deklarasi variabel ID_KOOR diisi id_koor yang akan dikirim ke activity selanjutnya
        intent.putExtra("NAMA_KOOR", nama_koor);                                                                  //deklarasi variabel NAMA_KOOR diisi nama_koor yang akan dikirim ke activity selanjutnya
        intent.putExtra("KELOMPOK", kelompok);                                                                    //deklarasi variabel KELOMPOK diisi kelompok yang akan dikirim ke activity manajemen
        startActivity(intent);                                                                                           //memulai activity
    }

    //procedure untuk menentukan alur setiap kelompok
    private void penentuanAlur(final String Id_Koor) {
    db_alur.get()                                                                                   //mengambil data dari koleksi/tabel alur pada database
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int alurRuang1 = 0;                                                         //deklarasi alurRuang1 untuk menampung jumlah kelompok yang sedang berkunjung pada ruang 1
                        int alurRuang2 = 0;                                                         //deklarasi alurRuang2 untuk menampung jumlah kelompok yang sedang berkunjung pada ruang 2
                        int alurRuang3 = 0;                                                         //deklarasi alurRuang3 untuk menampung jumlah kelompok yang sedang berkunjung pada ruang 3
                        int alurRuang4 = 0;                                                         //deklarasi alurRuang4 untuk menampung jumlah kelompok yang sedang berkunjung pada ruang 4
                        int alurRuang5 = 0;                                                         //deklarasi alurRuang5 untuk menampung jumlah kelompok yang sedang berkunjung pada ruang 5
                        int alurMulai = 0;                                                          //deklarasi alurMulai untuk menampung data ruang yang sedang dikunjungi paling sedikit
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {     //melakukan pengulangan sebanyak data yang ada
                            Alur get_alur = documentSnapshot.toObject(Alur.class);                  //deklarasi variabel get_alur menggunakan model Alur

                            int indexAlur = get_alur.getAlur();                                     //deklarasi variabel indexAlur dan diisi dengan mengambil data alur pada database

                            if (indexAlur == 1){alurRuang1 = alurRuang1 + 1;}                       //kondisi jika data yang didapat dan dimasukkan ke variable indexAlur adalah 1, maka variabel alurRuang1 ditambah 1
                            if (indexAlur == 2){alurRuang2 = alurRuang2 + 1;}                       //kondisi jika data yang didapat dan dimasukkan ke variable indexAlur adalah 2, maka variabel alurRuang2 ditambah 1
                            if (indexAlur == 3){alurRuang3 = alurRuang3 + 1;}                       //kondisi jika data yang didapat dan dimasukkan ke variable indexAlur adalah 3, maka variabel alurRuang3 ditambah 1
                            if (indexAlur == 4){alurRuang4 = alurRuang4 + 1;}                       //kondisi jika data yang didapat dan dimasukkan ke variable indexAlur adalah 4, maka variabel alurRuang4 ditambah 1
                            if (indexAlur == 5){alurRuang5 = alurRuang5 + 1;}                       //kondisi jika data yang didapat dan dimasukkan ke variable indexAlur adalah 5, maka variabel alurRuang5 ditambah 1
                        }
                        int[] Alur = {alurRuang1,alurRuang2,alurRuang3,alurRuang4,alurRuang5};      //deklarasi variabel array of integer 1 dimensi untuk menampung total kelompok yang berkunjung dalam setiap ruangan
                        int[] AlurMulai = {1,2,3,4,5};                                              //deklarasi variabel array of integer 1 dimensi untuk menampung ruang 1,2,3,4,dan 5

                        int tempSort;                                                               //deklarasi variabel untuk menampung data alur pada saat bubble sort
                        int tempAlurMulai;                                                          //deklarasi variabel untuk menampung data ruang untuk alur mulai pada saat bubble sort

                        //bubble sort ascending
                        for(int i=0; i<4; i++){                                                     //pengulangan sebanyak 4x dari array ke 0-3
                            for(int j=i; j<5; j++){                                                 //pengulangan sebanyak 4x dari array ke 1-4
                                if(Alur[i] > Alur[j]){                                              //kondisi jika array[i] lebih besar dari array[j]
                                    tempSort = Alur[i];                                             //isi data array[i] dipindahkan ke variabel tempSort
                                    Alur[i] = Alur[j];                                              //isi data array[j] dipindahkan ke variabel alur[i]
                                    Alur[j] = tempSort;                                             //isi data variabel tempSort dipindahkan ke array[j]

                                    tempAlurMulai = AlurMulai[i];                                   //isi data array alurMulai[i] dipindahkan ke variabel tempAlurMulai
                                    AlurMulai[i] = AlurMulai[j];                                    //isi data array[i] dipindahkan ke variabel tempSort
                                    AlurMulai[j] = tempAlurMulai;                                   //isi data array[i] dipindahkan ke variabel tempSort
                                }
                            }
                        }

                        alurMulai = AlurMulai[0];                                                   //variabel alurMulai diisi dengan data pada variabel AlurMulai[0] karena paling sedikit

//                        String data = "Alur 1 : " +alurRuang1+ "\nAlur 2 : " +alurRuang2 +"\nAlur 3 : " +alurRuang3
//                                +"\nAlur 4 : " +alurRuang4+ "\nAlur 5 : " +alurRuang5+ "\nAlurmulai : " + alurMulai;
//
//                        textViewTotal.setText(data);

                        for(int i=1; i <= kelompok; i++){                                           //pengulangan sebanyak jumlah kelompok yang dipilih
                            if(alurMulai <=5){                                                      //pengecekan kondisi jika isi data variabel alurMulai kurang dari atau sama dengan 5
                                Alur setAlur = new Alur(Id_Koor, i, alurMulai, 1);
                                db_alur.document().set(setAlur);                                    //meng-insertkan data ke database tabel alur
                                alurMulai = alurMulai + 1;                                          //alurMulai ditambah 1
                            }else if(alurMulai == 6){                                               //jika alurMulai sama dengan 6
                                alurMulai = 1;                                                      //deklarai ulang alur mulai menjadi 1, dikarenakan total ruangan hanya ada 5
                                Alur setAlur = new Alur(Id_Koor, i, alurMulai, 1);
                                db_alur.document().set(setAlur);                                    //meng-insertkan data ke database tabel alur
                                alurMulai = alurMulai + 1;                                          //alurMulai ditambah 1
                            }
                        }
                    }
                });
    }

    //procedure untuk pembagian kelompok peserta kuis
    private void pembagianKelompok(String id_koor){
        db_peserta.whereEqualTo("id_koordinator", id_koor)                                       //mengambil data dari database tabel peserta yang id_koordinator = id_koor
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
                        String namaPeserta = data_peserta.getNama_peserta();

                        Peserta update_peserta_db = new Peserta(idKoordinator, idPeserta, namaPeserta, i, 0);
                        db_peserta.document(idDocumentPeserta).set(update_peserta_db);

                        i++;
                        //
                    }else {
                        Peserta data_peserta = documentSnapshot.toObject(Peserta.class);
                        data_peserta.setDocumentId(documentSnapshot.getId());

                        String idDocumentPeserta = data_peserta.getDocumentId();
                        String idPeserta = data_peserta.getId_peserta();
                        String idKoordinator = data_peserta.getId_koordinator();
                        String namaPeserta = data_peserta.getNama_peserta();

                        Peserta update_peserta_db = new Peserta(idKoordinator, idPeserta, namaPeserta, i, 0);
                        db_peserta.document(idDocumentPeserta).set(update_peserta_db);

                        i++;
                    }
                }
            }
        });
    }

    //deteksi realtime firestore untuk mengetahui jumlah peserta yang masuk
    private void getTotalPesertaMasuk(String id_koor){
        db_peserta.whereEqualTo("id_koordinator", id_koor)
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
                if(i > 0){
                    btn_generate_kelompok.setEnabled(true);
                }
             }
        });
    }

    //procedure untuk meng-update data kuis pada database
    private void updateKuis(String id_koor) {
        db_kuis.whereEqualTo("id_koordinator", id_koor)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Kuis data_kuis = documentSnapshot.toObject(Kuis.class);
                            data_kuis.setDocumentId(documentSnapshot.getId());

                            String idDocumentKuis = data_kuis.getDocumentId();
                            String idKoordinator = data_kuis.getId_koordinator();

                            Kuis update_kuis = new Kuis(idKoordinator, kelompok, klasifikasi, statuKuis);
                            db_kuis.document(idDocumentKuis).set(update_kuis);
                        }
                    }

                });
    }

    //procedure untuk menampilkan pop up konfirmasi ketika akan logout
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
