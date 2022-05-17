package com.example.agrotechblogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";
    public static final String TABLENAME = "posts";
    public static final String TABLECOM = "comments";
    public static final String TABLEUSER = "profile";
    private SQLiteDatabase db;
    byte[] imageInByte;

    public DBHelper(Context context) {
        super(context, "Login.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("create Table users(id integer primary key autoincrement, email TEXT, password TEXT)");
        db.execSQL("create Table "+ TABLEUSER +"(id1 integer primary key autoincrement, image Blob, name TEXT)");
        db.execSQL("create Table "+ TABLENAME +"(id2 integer primary key autoincrement, description TEXT, postimage Blob)");
        db.execSQL("create Table "+ TABLECOM +"(idcom integer primary key autoincrement, postid integer, postcom TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists profile");
        db.execSQL("drop Table if exists posts");
        db.execSQL("drop Table if exists comments");

    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",password);
        long results = db.insert("users",null,contentValues);
        if (results==-1)return false;
        else
            return true;
    }
    public Boolean checkuseremail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ?",new String[] {email});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkuserpassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ? and password = ?",new String[] {email,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public boolean insertDetails(Bitmap image, String name){
        SQLiteDatabase db = getReadableDatabase();
        ByteArrayOutputStream objectByteOutputStream = new ByteArrayOutputStream();
        imageInByte= objectByteOutputStream.toByteArray();
        ContentValues values = new ContentValues();

        values.put("image",imageInByte);
        values.put("name",name);
        long id1 = db.insert("profile",null,values);
        if (id1<=0){
            return false;
        }else {
            return true;
        }
    }
    public boolean uploadPosts(String description, Bitmap image){
        SQLiteDatabase db = getReadableDatabase();
        ByteArrayOutputStream objectByteOutputStream = new ByteArrayOutputStream();
        imageInByte= objectByteOutputStream.toByteArray();
        ContentValues values = new ContentValues();

        values.put("description",description);
        values.put("image",imageInByte);

        long id2 = db.insert("profile",null,values);
        if (id2<=0){
            return false;
        }else {
            return true;
        }
    }
}
