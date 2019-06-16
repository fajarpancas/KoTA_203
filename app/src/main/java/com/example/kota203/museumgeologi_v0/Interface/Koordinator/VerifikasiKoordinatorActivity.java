package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Koordinator;
import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class VerifikasiKoordinatorActivity extends AppCompatActivity {

    MaterialEditText nama_koordinator, nama_sekolah, kode_verifikasi;   //deklarasi form edit untuk verifikasi
    String id_koordinator;                                              //variabel untuk menampung hasil generate id koordinator
    String status_kuis = "Belum manajemen";                             //variabel untuk menampung status kuis yang dikirim ke database
    Button btn_sign_in_koordinator;                                     //deklarasi button untuk verifikasi
    int i;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();                //menghubungkan ke firebase cloud firestore
    private CollectionReference db_permainan = db.collection("permainan");      //menghubungkan ke cloud firestore koleksi permainan
    private CollectionReference db_koordinator = db.collection("koordinator");  //menghubungkan ke cloud firestore koleksi koordinator
    private CollectionReference db_kuis = db.collection("kuis");                //menghubungkan ke cloud firestore koleksi koordinator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_koordinator);                       //menampilkan xml verifikasi koordinator

        nama_koordinator = (MaterialEditText)findViewById(R.id.form_nama_koordinator);  //deklarasi nama_koordinator diisi dengan form dengan id nama koordinator pada xml
        nama_sekolah = (MaterialEditText)findViewById(R.id.form_nama_sekolah);          //deklarasi nama_sekolah diisi dengan form dengan id nama sekolah pada xml
        kode_verifikasi = (MaterialEditText)findViewById(R.id.form_kode_verifikasi);    //deklarasi kode_verifikasi diisi dengan form dengan id kode verifikasi pada xml

        btn_sign_in_koordinator = (Button)findViewById(R.id.btn_sign_in_koordinator);   //menghubungkan variabel button dengan button xml id btn_sign_in_koordinator
        btn_sign_in_koordinator.setOnClickListener(new View.OnClickListener() {         //aksi ketika button btn_sign_in_koordinator di klik
            @Override
            public void onClick(View view) {
               signInKoordinator(nama_koordinator.getText().toString(), nama_sekolah.getText().toString(), kode_verifikasi.getText().toString()); //memanggil procedure signInKoordinator
            }
        });
    }

    //procedure untuk memproses verifikasi koordinator
    private void signInKoordinator(final String nama_koordinator, final String nama_sekolah, final String kode_verifikasi) {
        i = 0;
        if(!nama_koordinator.isEmpty()) {                                                                       //melakukan pengecekan nama koordinator pada form
            if(!nama_sekolah.isEmpty()){
                if (!kode_verifikasi.isEmpty()) {                                                               //melakukan pengecekan kode verifikasi pada form
                    db_permainan.whereEqualTo("kode_verifikasi", kode_verifikasi).get()                      //mengambil data dari koleksi permainan yang kode_verifikasi sama dengan yang diinputkan koordinator
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {              //jika terdapat data akan mengambil data
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {     //melakukan pengulangan sebanyak data yang ditemukan
                                        Permainan kodeVerifikasi = documentSnapshot.toObject(Permainan.class);  //membuat objek Permainan untuk mengatur proses deserialisasi data
                                        String kodeVer = kodeVerifikasi.getKode_verifikasi();                   //deklarasi varibel kodeVer diisi dengan kode verifikasi yang terdapat pada database
                                        if (kodeVer.equals(kode_verifikasi)) {                                  //pengecekan kode verifikasi yang terdapat pada database dan yang diinputkan koordinator
                                            i = i + 1;
                                            Toast.makeText(VerifikasiKoordinatorActivity.this,          //pesan jika kode sesuai
                                                    "Login Berhasil",
                                                    Toast.LENGTH_SHORT).show();
                                            id_koordinator = generate_id_koor(4);                        //memanggil fungsi generate id koordinator
                                            setNamaKoortoDB(nama_koordinator, nama_sekolah);                    //memanggil procedure setNamaKoortoDB
                                            openActivityManajemenKuis(nama_koordinator);                        //memanggil procedure openActivitiymanajemenKuis
                                        }else if(!kodeVer.equals(kode_verifikasi) && i == 0){
                                            Toast.makeText(VerifikasiKoordinatorActivity.this,          //pesan jika kode tidak sesuai
                                                    "Kode verifikasi yang anda masukkan salah",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else Toast.makeText(VerifikasiKoordinatorActivity.this,                               //pesan jika kode verifikasi tidak diisi
                        "Kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
            } else if(!kode_verifikasi.isEmpty()){                                                              //pengecekan
                Toast.makeText(VerifikasiKoordinatorActivity.this,                                      //pesan jika nama koordinator tidak diisi
                        "Nama sekolah harus diisi", Toast.LENGTH_SHORT).show();
            } else if(kode_verifikasi.isEmpty()){                                                               //pengecekan
                Toast.makeText(VerifikasiKoordinatorActivity.this,                                      //pesan jika nama koordinator tidak diisi
                        "Nama sekolah dan kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
            }
        } else if(!kode_verifikasi.isEmpty() && !nama_sekolah.isEmpty()){                                       //pengecekan
            Toast.makeText(VerifikasiKoordinatorActivity.this,                                          //pesan jika nama koordinator tidak diisi
                    "Nama koordinator harus diisi", Toast.LENGTH_SHORT).show();
        } else if(!kode_verifikasi.isEmpty() && nama_sekolah.isEmpty()){                                        //pengecekan
            Toast.makeText(VerifikasiKoordinatorActivity.this,                                          //pesan jika nama koordinator dan nama sekolah tidak diisi
                    "Nama koordinator dan nama sekolah harus diisi", Toast.LENGTH_SHORT).show();
        } else if(kode_verifikasi.isEmpty() && !nama_sekolah.isEmpty()){                                        //pengecekan
            Toast.makeText(VerifikasiKoordinatorActivity.this,                                          //pesan jika nama koordinator dan kode verifikasi tidak diisi
                    "Nama koordinator dan kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
        } else if(kode_verifikasi.isEmpty() && nama_sekolah.isEmpty()){
            Toast.makeText(VerifikasiKoordinatorActivity.this,                                          //pesan jika nama koordinator, nama sekolah dan kode verifikasi tidka diisi
                    "Nama koordinator, nama sekolah dan kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
        }
    }

    //procedure untuk pindah activity ke activity manajemen kuis
    private void openActivityManajemenKuis(String nama_koordinator) {
        Intent intent = new Intent(VerifikasiKoordinatorActivity.this, ManajemenKuisActivity.class);    //deklarasi intent dari verifikasi ke manajemen
        intent.putExtra("ID_KOOR", id_koordinator);                                                             //deklarasi variabel ID_KOOR diisi id_koordinator yang akan dikirim ke activity manajemen
        intent.putExtra("NAMA_KOOR", nama_koordinator);                                                         //deklarasi variabel NAMA_KOORD diisi nama_koordinator yang akan dikirim ke activity manajemen
        startActivity(intent);                                                                                        //memulai perpindahan activity
    }

    //function untuk men-generate id koordinator
    private String generate_id_koor(int lenght){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();    //deklarasi var dengan isi huruf a-z dan 0-9 untuk di random
        StringBuilder stringBuilder = new StringBuilder();                      //inisialisasi stringBuilder
        Random random = new Random();                                           //inisialisasi random
        for(int i = 0; i<lenght; i++){                                          //pengulangan sebanyak 4x
            char c = chars[random.nextInt(chars.length)];                       //pengisisan hasil huruf/angka random ke var c
            stringBuilder.append(c);
        }
        return stringBuilder.toString();                                        //convert ke string
    }
    //procedure untuk meng-insert data koor dan data kuis ke dalam database
    private void setNamaKoortoDB(String nama_koordinator, String nama_sekolah){
        Koordinator koordinator = new Koordinator(id_koordinator, nama_koordinator, nama_sekolah);      //inisialisasi koordinator menggunakan model koordinator
        db_koordinator.add(koordinator);                                                                //insert data koordinator ke database tabel koordinator
        Kuis kuis = new Kuis(id_koordinator, 0, "", status_kuis);          //inisialisasi kuis menggunakan model kuis
        db_kuis.add(kuis);                                                                              //insert data kuis ke database tabe kuis
    }
}
