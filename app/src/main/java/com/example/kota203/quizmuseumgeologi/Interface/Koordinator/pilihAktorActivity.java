package com.example.kota203.quizmuseumgeologi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.Interface.Peserta.LoginPesertaActivity;
import com.example.kota203.quizmuseumgeologi.Model.Guru;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class pilihAktorActivity extends AppCompatActivity {

    GridLayout mainGridbermain;
//    MaterialEditText kodeGuru;
//
//    FirebaseDatabase database;
//    DatabaseReference guru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_aktor);

//        database = FirebaseDatabase.getInstance();
//        guru = database.getReference("Guru");

        mainGridbermain = (GridLayout)findViewById(R.id.mainGridbermain);

        //set event
        setSingleEvent(mainGridbermain);
    }

    private void setSingleEvent(GridLayout mainGridbermain) {
        //loop all child
        for(int i=0;i<mainGridbermain.getChildCount();i++){
            CardView cardView = (CardView)mainGridbermain.getChildAt(i);
            final int finalI = i;
            final int finalI1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        openLoginGuru();
                    } else if (finalI == 1) {
                        openLoginPeserta();
//                      Toast.makeText(pilihAktorActivity.this, "click : true" + finalI1, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void openLoginPeserta() {
        Intent i = new Intent(pilihAktorActivity.this, LoginPesertaActivity.class);
        startActivity(i);
    }

    private void openLoginGuru() {
        Intent i = new Intent(pilihAktorActivity.this, LoginGuruActivity.class);
        startActivity(i);
    }

//    private void showLoginDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(pilihAktorActivity.this);
//        alertDialog.setTitle("Sign In");
//        alertDialog.setMessage("Please fill");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        View sign_in = inflater.inflate(R.layout.sign_in,null);
//
//        kodeGuru = (MaterialEditText)sign_in.findViewById(R.id.kodeGuru);
//        alertDialog.setView(sign_in);
//        alertDialog.setIcon(R.drawable.ic_person_black_24dp);
//
////        alertDialog.setNegativeButton("batal", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                dialogInterface.dismiss();
////            }
////        });
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                final Guru kode_guru = new Guru(kodeGuru.getText().toString());
//
//                guru.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                            Guru login = dataSnapshot.child(String.valueOf(kode_guru)).getValue(Guru.class);
//                            if (login.getKodeGuru().equals(kode_guru)){
//                                Toast.makeText(pilihAktorActivity.this, "Success Login", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(pilihAktorActivity.this, "Password is Wrong", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                dialogInterface.dismiss();
//            }
//        });
//        alertDialog.show();
//    }

//    private void openLoginAdmin(){
//        Intent i = new Intent(pilihAktorActivity.this, LoginAdminActivity.class);
//        startActivity(i);
//    }

//    private void openSetParticipant(){
//        Intent i = new Intent(AktorActivity.this, SetParticipantActivity.class);
//        startActivity(i);
//    }
}
