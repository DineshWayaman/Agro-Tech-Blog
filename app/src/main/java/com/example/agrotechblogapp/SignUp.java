package com.example.agrotechblogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    private TextInputEditText txtEmail, txtPassword, txtConfirm;
    private Button btnSignUp,btnSignInTab ;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

            txtPassword = findViewById(R.id.txtPasswordSignUp);
            txtConfirm = findViewById(R.id.txtConfirmPasswordSignUp);
            btnSignInTab = findViewById(R.id.btnSignInTab);
            txtEmail = findViewById(R.id.txtEmailSignUp);
            btnSignUp = findViewById(R.id.btnSignUp);
            db = new DBHelper(this);

         btnSignInTab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(SignUp.this, SignIn.class));
                 finish();
             }
         });

         btnSignUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String mail = txtEmail.getText().toString();
                 String pass = txtPassword.getText().toString();
                 String repass = txtConfirm.getText().toString();

                 if (mail.equals("")){
                     Toast.makeText(SignUp.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                 }else if (pass.equals("")){
                     Toast.makeText(SignUp.this, "Enter Password", Toast.LENGTH_SHORT).show();
                 }else if (repass.equals("")){
                     Toast.makeText(SignUp.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                 }else {
                     if (pass.equals(repass)){
                         Boolean checkuser = db.checkuseremail(mail);
                         if (checkuser==false){
                             Boolean insert = db.insertData(mail,pass);
                             if (insert==true){
                                 Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                 //startActivity(new Intent(SignUp.this, Profile.class));
                                 //finish();
                                 Intent intent= new Intent(SignUp.this,Profile.class);
                                 startActivity(intent);
                             }else {
                                 Toast.makeText(SignUp.this, "Registration failed!!!!", Toast.LENGTH_SHORT).show();
                             }
                         }
                         else {
                             Toast.makeText(SignUp.this, "User already exists! Please sign in..", Toast.LENGTH_SHORT).show();
                         }
                     }
                     else {
                         Toast.makeText(SignUp.this, "Password not matching", Toast.LENGTH_SHORT).show();
                     }
                 }

             }
         });

    }
}