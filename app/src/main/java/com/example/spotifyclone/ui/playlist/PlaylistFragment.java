package com.example.spotifyclone.ui.playlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.objects.Playlist;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<Cursor>

{
    FloatingActionButton AddPlaylist;
    private String MyPlaylist = "";
    MusicHelper musicHelper;
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    ProgressDialog progressDialog;
    PlaylistAdapter playlistAdapter;
    ArrayList<Playlist> myPlaylist;
    MyPlaylistCursorAdapter myPlaylistCursorAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_playlist, container, false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(1, null, this);
        musicHelper = new MusicHelper(getActivity());
        AddPlaylist = view.findViewById(R.id.AddPlaylist);
        RecyclerView = view.findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myPlaylistCursorAdapter = new MyPlaylistCursorAdapter(getActivity());
        RecyclerView.setAdapter(myPlaylistCursorAdapter);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        AddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlaylist();
            }
        });


//        AsyncPlaylist asyncPlaylist = new AsyncPlaylist(getActivity(), new PlaylistListener() {
//            @Override
//            public void PlaylistListener(final ArrayList<Playlist> playlist) {
//                for (int i =0; i<playlist.size(); i++){
//                    myPlaylist = playlist;
//                }
//                playlistAdapter = new PlaylistAdapter(getActivity(), playlist, new playlistcallback() {
//                    @Override
//                    public void DeletePlaylist(int position) {
//                        final Playlist MyPlaylist = myPlaylist.get(position);
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setTitle("Confirm");
//                        builder.setMessage("Are you sure do you want to delete this from playlist?");
//
//                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                int play_Id =  MyPlaylist.getId();
//                                boolean delete = musicHelper.deletePlaylist(play_Id);
//                                musicHelper.massDelete(play_Id);
//                                if (delete){
//                                    playlist.clear();
//                                    playlist.addAll(musicHelper.ListOfPlaylist());
//                                    playlistAdapter.notifyDataSetChanged();
//                                    Toast.makeText(getActivity(), "Playlist successfully deleted", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    Toast.makeText(getActivity(), "Failed to do operation", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                        builder.setCancelable(false);
//                        AlertDialog alert = builder.create();
//                        alert.setCancelable(false);
//                        alert.show();
//                    }
//                });
//                RecyclerView.setItemAnimator(new DefaultItemAnimator());
//                RecyclerView.setAdapter(playlistAdapter);
////                new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(RecyclerView);
//                RecyclerView.setHasFixedSize(true);
//                playlistAdapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//        });
//        asyncPlaylist.execute();
    }


    public void addPlaylist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Playlist");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = input.getText().toString();
                Uri newUri;
                ContentValues newValues = new ContentValues();
                newValues.put(PlaylistDb.KEY_NAME, name);


                newUri = getActivity().getContentResolver().insert(PlaylistProvider.CONTENT_URI, newValues);


                if (!newUri.equals(true)){
                    Toast.makeText(getActivity(), name + " was inserted" , Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Failed to add to playlist", Toast.LENGTH_SHORT).show();
                    return;
                }
//                boolean inserted = musicHelper.insertPlaylist(MyPlaylist);
//
//                if (inserted){
//                    if (!(myPlaylist == null)){
//                        myPlaylist.clear();
//                        myPlaylist.addAll(musicHelper.ListOfPlaylist());
//                    }else{
//                        playlistAdapter.notifyDataSetChanged();
//                        return;
//
//                    }
//
//
////                    musicHelper.ListOfPlaylist();
//                    playlistAdapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity(), "Playlist successfully made", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getActivity(), "Failed to make playlist", Toast.LENGTH_SHORT).show();
//                }

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

//    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//        @Override
//        public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
//            int pos = viewHolder.getAdapterPosition();
//            final int Id = myPlaylist.get(pos).getId();
//            final String name = myPlaylist.get(pos).getName();
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("Confirm");
//            builder.setMessage("Are you sure do you want to delete this playlist");
//
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    musicHelper.massDelete(Id);
//                    musicHelper.deletePlaylist(Id);
////                    musicHelper.deleteSongFromPlaylist(Id);
//                    myPlaylist.clear();
//                    myPlaylist.addAll(musicHelper.ListOfPlaylist());
//                    playlistAdapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity(), "" + name +" was removed from the list",  Toast.LENGTH_SHORT).show();
//
//                    dialogInterface.dismiss();
//
//                }
//            });
//            builder.setNegativeButton("Cancel",     new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                    playlistAdapter.notifyDataSetChanged();
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    };


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
        myPlaylistCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        myPlaylistCursorAdapter.swapCursor(null);

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
                getActivity(),
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);

    }
}
