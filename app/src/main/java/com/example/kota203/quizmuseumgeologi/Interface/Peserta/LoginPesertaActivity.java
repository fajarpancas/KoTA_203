package com.example.kota203.quizmuseumgeologi.Interface.Peserta;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.ViewGroupUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.Model.Guru;
import com.example.kota203.quizmuseumgeologi.Model.Permainan;
import com.example.kota203.quizmuseumgeologi.Model.Peserta;
import com.example.kota203.quizmuseumgeologi.Model.User;
import com.example.kota203.quizmuseumgeologi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class LoginPesertaActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    MaterialEditText entrnama_peserta, entr_kode_peserta;
    Button btn_sign_in_peserta;
    String id_pesertaS;

    FirebaseDatabase database;
    DatabaseReference permainan, peserta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_peserta);

        database = FirebaseDatabase.getInstance();
        peserta = database.getReference("Peserta");
        permainan = database.getReference("Permainan");

        entrnama_peserta = (MaterialEditText)findViewById(R.id.entrnama_peserta);
        entr_kode_peserta = (MaterialEditText)findViewById(R.id.entr_kode_peserta);

        btn_sign_in_peserta = (Button)findViewById(R.id.btn_sign_in_peserta);

        btn_sign_in_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_pesertaS = generateString(6);
                signIn(id_pesertaS, entrnama_peserta.getText().toString(), entr_kode_peserta.getText().toString());
            }
        });
    }

    private void signIn(final String id_pesertaS, final String namaPeserta, final String kodePermainan) {
        permainan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!kodePermainan.isEmpty()){
                    Permainan login = dataSnapshot.child(kodePermainan).getValue(Permainan.class);
                    if (login.getKodePermainan().equals(kodePermainan)) {
                        addNametoDB(id_pesertaS, namaPeserta, kodePermainan);
                        openInfoKelompok(kodePermainan, namaPeserta);
                    }
                    else {
                        Toast.makeText(LoginPesertaActivity.this, "Kode salah", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(LoginPesertaActivity.this, "Kode harus diisi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openInfoKelompok(String kodePeserta, String namaPeserta) {
        Intent intent = new Intent(LoginPesertaActivity.this, InfoKelompokActivity.class);
        intent.putExtra(EXTRA_TEXT_KODE, kodePeserta);
        intent.putExtra(EXTRA_TEXT_NAMA, namaPeserta);
        startActivity(intent);
    }

    private String generateString(int lenght){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void addNametoDB(final String idPeserta, final String namaPeserta, final String kodePermainan) {
        if (!namaPeserta.isEmpty()) {

            final Peserta peserta1 = new Peserta(idPeserta, namaPeserta);
            peserta.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    peserta.child(kodePermainan).child(id_pesertaS).setValue(peserta1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(LoginPesertaActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
        }
    }
}