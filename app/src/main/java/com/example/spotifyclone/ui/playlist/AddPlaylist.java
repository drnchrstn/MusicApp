package com.example.spotifyclone.ui.playlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.objects.Playlist;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddPlaylist extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor>
{
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    private String MyPlaylist = "";
    MusicHelper musicHelper;
    AddToPlaylistAdapter addToPlaylistAdapter;
    ProgressDialog progressDialog;
    FloatingActionButton AddPlaylist;
    ArrayList<Playlist> myPlaylist;
    AddsongCursorAdapter addsongCursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        musicHelper = new MusicHelper(AddPlaylist.this);
        LoaderManager.getInstance(AddPlaylist.this).initLoader(1, null, this);
        AddPlaylist = findViewById(R.id.AddPlaylist);
        RecyclerView = findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(AddPlaylist.this));
        addsongCursorAdapter = new AddsongCursorAdapter(AddPlaylist.this);
        RecyclerView.setAdapter(addsongCursorAdapter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#232F34")));
        actionBar.setTitle("Add to Playlist");
        progressDialog = new ProgressDialog(AddPlaylist.this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();


        AddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylist();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        AsyncPlaylist asyncPlaylist = new AsyncPlaylist(AddPlaylist.this, new PlaylistListener() {
//            @Override
//            public void PlaylistListener(ArrayList<Playlist> playlist) {
//                for (int i =0; i<playlist.size(); i++){
//                    myPlaylist = playlist;
//                }
//                addToPlaylistAdapter = new AddToPlaylistAdapter(AddPlaylist.this, playlist);
//                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(RecyclerView);
//                RecyclerView.setAdapter(addToPlaylistAdapter);
//                RecyclerView.setHasFixedSize(true);
//                addToPlaylistAdapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//        });
//        asyncPlaylist.execute();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void addPlaylist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaylist.this);
        builder.setTitle("Add New Playlist");
        final EditText input = new EditText(AddPlaylist.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = input.getText().toString();

                Uri newUri;
                ContentValues newValues = new ContentValues();
                newValues.put(PlaylistDb.KEY_NAME, name);


                newUri = getContentResolver().insert(PlaylistProvider.CONTENT_URI, newValues);


                if (!newUri.equals(true)){
                    Toast.makeText(AddPlaylist.this, name + " was inserted" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddPlaylist.this, "Failed to add to playlist", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            final int Id = myPlaylist.get(pos).getId();


            AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaylist.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure do you want to delete this playlist?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {



                    boolean deleted = musicHelper.deletePlaylist(Id);
                    musicHelper.massDelete(Id);


                    if (deleted){
                        myPlaylist.clear();
                        myPlaylist.addAll(musicHelper.ListOfPlaylist());
                        addToPlaylistAdapter.notifyDataSetChanged();
                        Toast.makeText(AddPlaylist.this, "Playlist successfully deleted", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddPlaylist.this, "Failed to make playlist", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    addToPlaylistAdapter.notifyDataSetChanged();
                    dialogInterface.cancel();
                }
            });
            builder.show();

        }
    };

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        if (id == 1){
            return playlistLoader();
        }

        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        addsongCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        addsongCursorAdapter.swapCursor(null);
    }

    public Loader<Cursor> playlistLoader(){
        Uri uri = PlaylistProvider.CONTENT_URI;
        String[] projection = {
                PlaylistDb.KEY_ROWID,
                PlaylistDb.KEY_NAME
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = PlaylistDb.KEY_ROWID + " DESC";

        return new CursorLoader(
                AddPlaylist.this,
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);

    }
}