package com.example.adekurniawan.androideatit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adekurniawan.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //binding View
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait . . .");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user already exists
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            //get User Info
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Sign In Succes !", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Wrong Password !", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User Doesn't Exist in Database !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                        Toast.makeText(SignIn.this, "Please Check Your Internet Connection !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
