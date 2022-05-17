package com.example.agrotechblogapp.Adapters;

import static com.example.agrotechblogapp.DBHelper.TABLECOM;
import static com.example.agrotechblogapp.DBHelper.TABLENAME;
import static com.example.agrotechblogapp.DBHelper.TABLEUSER;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agrotechblogapp.DBHelper;
import com.example.agrotechblogapp.Home;
import com.example.agrotechblogapp.Models.Comments;
import com.example.agrotechblogapp.Models.Posts;
import com.example.agrotechblogapp.R;
import com.example.agrotechblogapp.UpdatePost;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    int data;
    ArrayList<Posts> postsArrayList;
    SQLiteDatabase sqLiteDatabase,sqLiteDatabase2;
    DBHelper dbHelper;
    Dialog dialog;
    CommentAdapter commentAdapter;
    RecyclerView.LayoutManager manager;



    public PostAdapter(Context context, int data, ArrayList<Posts> postsArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.data = data;
        this.postsArrayList = postsArrayList;
        this.sqLiteDatabase = sqLiteDatabase;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_layout, null);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Posts posts = postsArrayList.get(position);
        holder.txtPostDesc.setText(posts.getPostDesc());
//        Glide.with(context).load(posts.getI_img()).into(holder.imgPostPhoto);
        byte[]image=posts.getI_img();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0, image.length);
        holder.imgPostPhoto.setImageBitmap(bitmap);


        dbHelper = new DBHelper(this.context);



        sqLiteDatabase2 = dbHelper.getReadableDatabase();
        Cursor cursor2 = sqLiteDatabase2.rawQuery("select * from "+ TABLEUSER +" order by id1 desc limit 1",null);
        while(cursor2.moveToNext()){
            int Uid = cursor2.getInt(0);
            byte[] uImg = cursor2.getBlob(1);
            String uName = cursor2.getString(2);

            holder.txtPostName.setText(uName);
            byte[]imageProf=uImg;
            Bitmap bitmapProf = BitmapFactory.decodeByteArray(imageProf,0, imageProf.length);
            holder.imgPostProfile.setImageBitmap(bitmapProf);
        }

        cursor2.close();









        holder.imbPostMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.imbPostMenu);
            popupMenu.inflate(R.menu.post_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.postEditMenu:
                            Bundle bundle = new Bundle();
                            bundle.putInt("pID", posts.getId());
                            bundle.putString("pDesc", posts.getPostDesc());
                            bundle.putByteArray("pImg", posts.getI_img());
                            Intent i = new Intent(context, UpdatePost.class);
                            i.putExtra("post", bundle);
                            context.startActivity(i);
                            break;

                        case R.id.deletePostMenu:
                            dbHelper = new DBHelper(context);
                            sqLiteDatabase = dbHelper.getReadableDatabase();
                            long deletePost = sqLiteDatabase.delete(TABLENAME, "id2="+posts.getId(),null);
                            if (deletePost!=-1){
                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                                postsArrayList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Error please try again", Toast.LENGTH_SHORT).show();

                            }
                            break;
                        case R.id.sharePost:
                            byte[]imageNew=posts.getI_img();
                            Bitmap bitmapNew = BitmapFactory.decodeByteArray(imageNew,0, imageNew.length);
                            String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmapNew, "title",null);
                            Uri uri = Uri.parse(bitmapPath);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/png");
                            intent.putExtra(Intent.EXTRA_STREAM,uri);
                            intent.putExtra(Intent.EXTRA_TEXT,posts.getPostDesc());
                            context.startActivity(Intent.createChooser(intent,"Share"));
                            break;
                        default:
                            return false;
                    }
                    return false;
                }
            });
            popupMenu.show();
        });



        ArrayList<Comments>commentArrayList=new ArrayList<>();

        holder.imbComment.setOnClickListener(view -> {


            dialog = new Dialog(this.context);
            dialog.setContentView(R.layout.comments_layout);


            EditText edtComment = dialog.findViewById(R.id.edtComment);
            ImageButton btnComment = dialog.findViewById(R.id.btnComment);
            RecyclerView commentRecycler = dialog.findViewById(R.id.recyclerComment);



            btnComment.setOnClickListener(view2 -> {
                ContentValues cv = new ContentValues();
                cv.put("postid", posts.getId());
                cv.put("postcom", edtComment.getText().toString());
                sqLiteDatabase = dbHelper.getWritableDatabase();
                Long insertData = sqLiteDatabase.insert(TABLECOM, null, cv);

                if (insertData!=null){
                    edtComment.setText("");
                    dialog.dismiss();
                }else{
                    Toast.makeText(this.context, "Error please try again", Toast.LENGTH_SHORT).show();
                }
            });

//            Toast.makeText(context, String.valueOf(posts.getId()), Toast.LENGTH_SHORT).show();

            commentArrayList.clear();

//        get all
            sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from "+ TABLECOM +" where postid="+posts.getId()+"",null);

            while(cursor.moveToNext()){
                int comid = cursor.getInt(0);
                int pID = cursor.getInt(1);
                String com = cursor.getString(2);
                commentArrayList.add(new Comments(comid,pID,com));
            }
            cursor.close();
            commentAdapter = new CommentAdapter(this.context, R.layout.comment_item,commentArrayList,sqLiteDatabase);
            commentRecycler.setAdapter(commentAdapter);

            manager = new LinearLayoutManager(this.context);
            commentRecycler.setLayoutManager(manager);
            commentRecycler.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.VERTICAL,false));


            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        });




    }

    private void getAllComment(){

    }


    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public void filteredList(ArrayList<Posts> filteredPosts){
        postsArrayList = filteredPosts;
        notifyDataSetChanged();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPostDesc,txtPostName;
        ImageView imgPostPhoto;
        ImageButton imbComment;
        ImageButton imbPostMenu;
        CircleImageView imgPostProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPostPhoto = (ImageView) itemView.findViewById(R.id.imgPostPhoto);
            txtPostDesc = (TextView) itemView.findViewById(R.id.txtPostDesc);
            imbComment = (ImageButton) itemView.findViewById(R.id.imbComment);
            imbPostMenu = (ImageButton) itemView.findViewById(R.id.postMenu);
            imgPostProfile = (CircleImageView) itemView.findViewById(R.id.imgPostProfile);
            txtPostName = (TextView) itemView.findViewById(R.id.txtPostName);
        }
    }

}

