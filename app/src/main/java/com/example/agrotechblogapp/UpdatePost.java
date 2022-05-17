package com.example.agrotechblogapp;

import static com.example.agrotechblogapp.DBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdatePost extends AppCompatActivity {

    ImageView imgPostUp;
    AppCompatButton btnUpChoose, btnUpdate;
    EditText edtUpDesc;
    int id = 0;
    final int CODE_GALLERY_REQUEST =999;
    Bitmap bitmap;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        dbHelper = new DBHelper(this);
        imgPostUp = findViewById(R.id.imgPosts);
        btnUpdate = findViewById(R.id.btnPostUpload);
        btnUpChoose = findViewById(R.id.btnChooseUPPhoto);
        edtUpDesc = findViewById(R.id.edtPUpDescription);

        btnUpdate.setVisibility(View.VISIBLE);

        btnUpChoose.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(
                    UpdatePost.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_GALLERY_REQUEST
            );
        });
        edtData();

        btnUpdate.setOnClickListener(view -> {
            ContentValues cv = new ContentValues();
            cv.put("description", edtUpDesc.getText().toString());
            cv.put("postimage", imageToString(bitmap));
            sqLiteDatabase = dbHelper.getWritableDatabase();
            long updateRecord = sqLiteDatabase.update(TABLENAME,cv,"id2="+id,null);
            if (updateRecord!=-1){
                Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdatePost.this, Home.class));
            }else{
                Toast.makeText(this, "Error Please try again", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void edtData() {
        if (getIntent().getBundleExtra("post")!=null){
            Bundle bundle = getIntent().getBundleExtra("post");
            id = bundle.getInt("pID");
            edtUpDesc.setText(bundle.getString("pDesc"));
            byte[]bytes=bundle.getByteArray("pImg");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
            imgPostUp.setImageBitmap(bitmap);

        }
    }


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
                imgPostUp.setImageBitmap(bitmap);
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