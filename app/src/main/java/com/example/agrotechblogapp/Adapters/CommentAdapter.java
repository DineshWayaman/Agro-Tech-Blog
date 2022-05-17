package com.example.agrotechblogapp.Adapters;

import static com.example.agrotechblogapp.DBHelper.TABLECOM;
import static com.example.agrotechblogapp.DBHelper.TABLENAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrotechblogapp.DBHelper;
import com.example.agrotechblogapp.Models.Comments;
import com.example.agrotechblogapp.Models.Posts;
import com.example.agrotechblogapp.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    int data;
    ArrayList<Comments> commentsArrayList;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    public CommentAdapter(Context context, int data, ArrayList<Comments> commentsArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.data = data;
        this.commentsArrayList = commentsArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_item, null);


        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Comments comments = commentsArrayList.get(position);
        holder.txtPostComment.setText(comments.getCom());

        holder.txtDeleteCom.setOnClickListener(view -> {
            dbHelper = new DBHelper(context);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            long deleteCom = sqLiteDatabase.delete(TABLECOM, "idcom="+comments.getId(),null);
            if (deleteCom!=-1){
                Toast.makeText(context, "Comment Deleted", Toast.LENGTH_SHORT).show();
                commentsArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }else{
                Toast.makeText(context, "Error please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPostComment,txtDeleteCom;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPostComment = itemView.findViewById(R.id.txtPostComment);
            txtDeleteCom = itemView.findViewById(R.id.txtDeleteCom);
        }
    }
}
