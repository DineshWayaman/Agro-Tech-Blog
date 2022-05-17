package com.example.agrotechblogapp;

import static com.example.agrotechblogapp.DBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.agrotechblogapp.Fragment.HomeFragment;
import com.example.agrotechblogapp.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Home extends AppCompatActivity {

    private FragmentManager fragmentManager;
    FloatingActionButton fab;
    Dialog dialog;
    EditText edtPostDesc;
    ImageView imgPost;
    AppCompatButton btnPost, btnChoose;
    final int CODE_GALLERY_REQUEST =999;
    Bitmap bitmap;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.upload_posts);

        edtPostDesc = dialog.findViewById(R.id.edtPDescription);
        imgPost = dialog.findViewById(R.id.imgPosts);
        btnPost = dialog.findViewById(R.id.btnPostUpload);
        btnChoose = dialog.findViewById(R.id.btnChoosePhoto);

        btnChoose.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(
                    Home.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_GALLERY_REQUEST
            );
        });


        btnPost.setOnClickListener(view -> {
            ContentValues cv = new ContentValues();
            cv.put("description", edtPostDesc.getText().toString());
            cv.put("postimage", imageToString(bitmap));
            sqLiteDatabase = dbHelper.getWritableDatabase();
            Long insertData = sqLiteDatabase.insert(TABLENAME, null, cv);

            if (insertData!=null){
                Toast.makeText(this, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                recreate();
                dialog.dismiss();
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListner);


        //fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment()).commit();
         // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.frameHomeContainer, new HomeFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

        fab =  findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.item_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.item_account:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer, selectedFragment).commit();
            return true;

        }
    };


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
                imgPost.setImageBitmap(bitmap);
                btnPost.setVisibility(View.VISIBLE);
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


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}