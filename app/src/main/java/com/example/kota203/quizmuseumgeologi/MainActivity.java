package com.example.kota203.quizmuseumgeologi;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    MaterialEditText entr_password,entrnama_peserta;
    MaterialEditText entr_pesertanew, entr_passwordnew;

    Button btn_sign_in_peserta;
    Button btn_sign_up;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        entrnama_peserta = (MaterialEditText)findViewById(R.id.entrnama_peserta);
        entr_password = (MaterialEditText)findViewById(R.id.entr_password);

        btn_sign_in_peserta = (Button)findViewById(R.id.btn_sign_in_peserta);
        btn_sign_up = (Button)findViewById(R.id.btn_sign_up);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btn_sign_in_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(entrnama_peserta.getText().toString(), entr_password.getText().toString());
            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up = inflater.inflate(R.layout.sign_up,null);

        entr_pesertanew = (MaterialEditText)sign_up.findViewById(R.id.entrnama_pesertanew);
        entr_passwordnew= (MaterialEditText)sign_up.findViewById(R.id.entr_passwordnew);

        alertDialog.setView(sign_up);
        alertDialog.setIcon(R.drawable.ic_person_black_24dp);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User username = new User(entr_pesertanew.getText().toString(),entr_passwordnew.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(username.getUsername()).exists())
                            Toast.makeText(MainActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                        else{
                            users.child(username.getUsername()).setValue(username);
                            Toast.makeText(MainActivity.this, "User reg success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void signIn(final String username, final String password) {
//        final User user = new User(entrnama_peserta.getText().toString());
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()){
                    if (!username.isEmpty()){
                        User login = dataSnapshot.child(username).getValue(User.class);
                        if (login.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Success Login", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Password is Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(MainActivity.this, "Please Enter your username", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Username is not Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
