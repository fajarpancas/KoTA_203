package com.example.kota203.quizmuseumgeologi.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.LoginGuruActivity;
import com.example.kota203.quizmuseumgeologi.ManajemenKuisActivity;
import com.example.kota203.quizmuseumgeologi.Model.Permainan;
import com.example.kota203.quizmuseumgeologi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class InfoKelompokActivity extends AppCompatActivity {

    Button button_mulai_permainan_peserta, btn_keluar;
    FirebaseDatabase database;
    DatabaseReference permainan;
    String Mulai = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_kelompok_peserta);

        database = FirebaseDatabase.getInstance();
        permainan = database.getReference("Permainan");

        Intent intent = getIntent();
        String textNamaPeserta = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_NAMA);
        final String textKodePeserta = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_KODE);

        btn_keluar = (Button) findViewById(R.id.btn_keluar);
        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        button_mulai_permainan_peserta = (Button)findViewById(R.id.mulai_permainan_peserta);
        button_mulai_permainan_peserta.setEnabled(false);
        button_mulai_permainan_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityPetunjuk();
            }
        });

        permainan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Permainan mulai = dataSnapshot.child(textKodePeserta).getValue(Permainan.class);
                if (mulai.getEnable().equals(Mulai)) {
                    button_mulai_permainan_peserta.setEnabled(true);
                    Toast.makeText(InfoKelompokActivity.this, mulai.getEnable(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView textViewNamaPeserta = (TextView)findViewById(R.id.nama_peserta_db);
//        TextView textViewKodePesesrta = (TextView)findViewById(R.id.kode_peserta_db);

        textViewNamaPeserta.setText(textNamaPeserta);
//        textViewKodePesesrta.setText("Kode Permainan : "+ textKodePeserta);

    }

    private void openActivityPetunjuk() {
        Intent intent = new Intent(InfoKelompokActivity.this, PetunjukActivity.class);
    //        intent.putExtra(EXTRA_TEXT_KODE, kodePeserta);
    //        intent.putExtra(EXTRA_TEXT_NAMA, namaPeserta);
        startActivity(intent);
    }

    @Override
    //untuk disabled fungsi back smartphone
    public void onBackPressed()
    {
        Toast.makeText(InfoKelompokActivity.this,"Tekan buton keluar untuk keluar dari permainan",Toast.LENGTH_SHORT).show();
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Keluar Permainan ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(InfoKelompokActivity.this, "Not Logout", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(InfoKelompokActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
