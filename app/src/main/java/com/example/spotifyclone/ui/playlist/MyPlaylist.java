package com.example.spotifyclone.ui.playlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.SingletonArraylist;
import com.example.spotifyclone.objects.MusicPool;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MyPlaylist extends AppCompatActivity {
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    int play_Id;
    MyPlaylistAdapter myPlaylistAdapter;
    ArrayList<File> songs;
    String playlistName = "";
    MusicHelper musicHelper;
    ArrayList<MusicPool> myPool;
    FloatingActionButton AddPlaylist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songs = SingletonArraylist.getInstance(MyPlaylist.this).getdefaultlist();
        musicHelper = new MusicHelper(MyPlaylist.this);
        setContentView(R.layout.activity_myplaylist);
        Intent intent = getIntent();
        play_Id = Integer.parseInt(intent.getStringExtra("play_Id"));
        playlistName = intent.getStringExtra("playlist_Name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#232F34")));
        actionBar.setTitle(playlistName);
        RecyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(MyPlaylist.this));
        AddPlaylist = findViewById(R.id.AddPlaylist);
        AddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPlaylist.this, Addsong.class);
                intent.putExtra("play_Id", play_Id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AsyncMyPlaylist asyncMyPlaylist = new AsyncMyPlaylist(play_Id, MyPlaylist.this, new MyPlaylistListener() {
            @Override
            public void MyPlaylistListener(ArrayList<MusicPool> musicPool) {
                for (int i =0; i<musicPool.size(); i++){
                    myPool = musicPool;
                }
                SingletonArraylist.getInstance(MyPlaylist.this).getPlaylistSong(myPool);
                myPlaylistAdapter = new MyPlaylistAdapter(MyPlaylist.this, musicPool, songs);
                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(RecyclerView);
                RecyclerView.setAdapter(myPlaylistAdapter);
                RecyclerView.setHasFixedSize(true);
                myPlaylistAdapter.notifyDataSetChanged();
            }
        });
        asyncMyPlaylist.execute();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            final int Id = myPool.get(pos).getId();
            final String name = myPool.get(pos).getName();



            AlertDialog.Builder builder = new AlertDialog.Builder(MyPlaylist.this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure do you want to delete this from playlist");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    musicHelper.deleteSongFromPlaylist(Id);
                    myPool.clear();
                    myPool.addAll(musicHelper.songFromPlaylist(play_Id));

                    myPlaylistAdapter.notifyDataSetChanged();
                    Toast.makeText(MyPlaylist.this, "" + name +" was removed from this playlist", Toast.LENGTH_SHORT).show();

                    dialogInterface.dismiss();

                }
            });
            builder.setNegativeButton("Cancel",     new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    myPlaylistAdapter.notifyDataSetChanged();
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
            myPlaylistAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
