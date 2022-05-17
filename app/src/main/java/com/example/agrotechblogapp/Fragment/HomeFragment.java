package com.example.agrotechblogapp.Fragment;

import static com.example.agrotechblogapp.DBHelper.TABLECOM;
import static com.example.agrotechblogapp.DBHelper.TABLENAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.agrotechblogapp.Adapters.PostAdapter;
import com.example.agrotechblogapp.Adapters.PostsAdapter;
import com.example.agrotechblogapp.DBHelper;
import com.example.agrotechblogapp.Home;
import com.example.agrotechblogapp.Models.Post;
import com.example.agrotechblogapp.Models.Posts;
import com.example.agrotechblogapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    ArrayList<Posts>postsArrayList=new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private PostsAdapter postsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;
    private RecyclerView.LayoutManager manager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    PostAdapter postAdapter;


    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_home,container,false);

        dbHelper = new DBHelper(this.getContext());

        EditText edtSearch = view.findViewById(R.id.edtSearch);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        init();




        return view;
    }

    private void filter(String searchText){
        ArrayList<Posts> filterdList = new ArrayList<>();
        for (Posts posts : postsArrayList){
            if (posts.getPostDesc().toLowerCase().contains(searchText.toLowerCase())){
                filterdList.add(posts);
            }
        }

        postAdapter.filteredList(filterdList);

    }

    private void init(){
        recyclerView = view.findViewById(R.id.recyclerHome);
        manager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);
//        refreshLayout = view.findViewById(R.id.swipeHome);
        toolbar = view.findViewById(R.id.toolbarHome);
        ((Home)getContext()).setSupportActionBar(toolbar);



        getPosts();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false));

    }

    private void getPosts() {
//        arrayList = new ArrayList<>();
//        refreshLayout.setRefreshing(true);

        sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+ TABLENAME +" order by id2 desc",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            byte[]imgPost = cursor.getBlob(2);
            postsArrayList.add(new Posts(id,desc,imgPost));
              }
        cursor.close();
        postAdapter = new PostAdapter(this.getContext(), R.layout.post_layout,postsArrayList,sqLiteDatabase);
        recyclerView.setAdapter(postAdapter);


        //StringRequest request = new StringRequest(Request.Method.GET,Const)
    }


}

