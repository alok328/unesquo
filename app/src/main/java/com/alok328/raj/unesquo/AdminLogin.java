package com.alok328.raj.unesquo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alok328.raj.unesquo.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class AdminLogin extends AppCompatActivity {

    MaterialEditText edtUsername,edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;
    SpotsDialog dialog;

    public void signIn(final View view){

        dialog = (SpotsDialog) new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();

        if(edtPassword.getText().length()==0 || edtUsername.getText().length()==0){
            dialog.dismiss();
            Snackbar.make(view, "Fields should not be empty", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }

        else {
            table_user.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.show();
                            if (dataSnapshot.child(edtUsername.getText().toString()).exists()) {

                                dialog.dismiss();
                                User user = dataSnapshot.child(edtUsername.getText().toString()).getValue(User.class);
                                user.setName(edtUsername.getText().toString());
                                if (user.getPassword().equals(edtPassword.getText().toString())) {

                                    Toast.makeText(AdminLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AdminLogin.this, CurrentDataList.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(AdminLogin.this, "Invalid username/password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(AdminLogin.this, "Admin not registered!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        edtUsername =  (MaterialEditText)findViewById(R.id.edtUsername);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Admin");
    }
}
