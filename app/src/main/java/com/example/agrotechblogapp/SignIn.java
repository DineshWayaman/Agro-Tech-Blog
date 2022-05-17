package com.example.agrotechblogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignIn extends AppCompatActivity {
    private TextInputEditText txtEmail, txtPassword;
    private Button btnSignIn,btnSignUpTab;
    DBHelper db;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        if (email.length()!=0){
            startActivity(new Intent(SignIn.this, Home.class));
        }

        txtPassword = findViewById(R.id.txtPasswordSignIn);
        btnSignUpTab = findViewById(R.id.btnSignUpTab);
        txtEmail = findViewById(R.id.txtEmailSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        db = new DBHelper(this);


        btnSignUpTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = txtEmail.getText().toString();
                String pass = txtPassword.getText().toString();

                if (mail.equals("")){
                    Toast.makeText(SignIn.this, "Enter Email..", Toast.LENGTH_SHORT).show();
                }else if (pass.equals("")){
                    Toast.makeText(SignIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkuserpass = db.checkuserpassword(mail,pass);
                    if (checkuserpass==true){
                        SharedPreferences.Editor spEditor = sharedPreferences.edit();
                        spEditor.putString("email", mail);
                        spEditor.commit();
                        Toast.makeText(SignIn.this, "Sign In successfully...", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignIn.this,Home.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SignIn.this, "Invalid credentials..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

}