package com.example.spotifyclone.ui.playlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;

import java.io.File;
import java.util.ArrayList;

public class Addsong extends AppCompatActivity {
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    public static int play_Id;
    ProgressDialog progressDialog;
    AddsongAdapter addsongAdapter;
    ArrayList<File> ListOfSongs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        ListOfSongs = SingletonArraylist.getInstance(Addsong.this).getdefaultlist();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#232F34")));
        actionBar.setTitle("Add Song");
        Intent intent = getIntent();
        play_Id = intent.getIntExtra("play_Id", 0);

        RecyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(Addsong.this));
        progressDialog = new ProgressDialog(Addsong.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        addsongAdapter = new AddsongAdapter(Addsong.this, ListOfSongs, play_Id);
        RecyclerView.setAdapter(addsongAdapter);
        RecyclerView.setHasFixedSize(true);
        progressDialog.dismiss();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
