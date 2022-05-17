package com.example.agrotechblogapp;

import static com.example.agrotechblogapp.DBHelper.TABLENAME;
import static com.example.agrotechblogapp.DBHelper.TABLEUSER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    private EditText txtName;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE = 1;
    final int CODE_GALLERY_REQUEST =999;
    Bitmap bitmap;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       dbHelper = new DBHelper(this);
        init();
    }

    private void init(){
       txtName = findViewById(R.id.txtName);
       txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
       btnContinue = findViewById(R.id.btnContinue);
       circleImageView = findViewById(R.id.imageUserProfile);

       //pick photo from gallery
        txtSelectPhoto.setOnClickListener(v->{
            ActivityCompat.requestPermissions(
                    Profile.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_GALLERY_REQUEST
            );
        });

        btnContinue.setOnClickListener(v -> {
            //validate fields
//            if (validate()){
                ContentValues cv = new ContentValues();
                cv.put("image", imageToString(bitmap));
                cv.put("name", txtName.getText().toString());
                sqLiteDatabase = dbHelper.getWritableDatabase();
                Long insertData = sqLiteDatabase.insert(TABLEUSER, null, cv);
                if (insertData!=null){
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Profile.this, SignIn.class));

                }else{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }

//            }
        });

    }





    private boolean validate(){
       if (txtName.getText().toString().isEmpty()){
           Toast.makeText(this, "Name is Required!!!", Toast.LENGTH_SHORT).show();
            return false;
       }
       return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length>0 && PackageManager.PERMISSION_GRANTED == grantResults[0]){
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                circleImageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }



    private byte[] imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
//        String encodedString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageBytes;
    }

}