package com.example.spotifyclone.ui.home;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.R;
import com.example.spotifyclone.ui.favorites.FavoriteCursorAdapter;

import java.io.File;
import java.util.ArrayList;

public class HomeFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<Cursor>
{
    RecyclerView RecyclerView;
    ProgressDialog progressDialog;
    AsyncSong asyncSong;
    SongAdapter songAdapter;
    HomeCursorAdapter homeCursorAdapter;
    private static final int SONG_LOADER = 1;
    Cursor mCursor;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView = v.findViewById(R.id.RecyclerView);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

//        getLoaderManager().initLoader(SONG_LOADER, null, this);

//        homeCursorAdapter = new HomeCursorAdapter(getActivity());
//        RecyclerView.setAdapter(homeCursorAdapter);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

//        LoaderManager.getInstance(getActivity()).initLoader(SONG_LOADER, null, (LoaderManager.LoaderCallbacks<Object>) getActivity());

        asyncSong = new AsyncSong(getActivity(), new songListener() {
            @Override
            public void songListener(ArrayList<File> songs) {
                songAdapter = new SongAdapter(getActivity(), songs);
                RecyclerView.setAdapter(songAdapter);
                RecyclerView.setHasFixedSize(true);
                progressDialog.dismiss();
            }
        });
        asyncSong.execute();

    }

//    @NonNull
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
//        if (id == SONG_LOADER){
//            return songLoader();
//        }
//
//        return null;
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
////        if(loader.getId() == SONG_LOADER) {
//            homeCursorAdapter.swapCursor(data);
////        }
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        homeCursorAdapter.swapCursor(null);
//    }

    private Loader<Cursor> songLoader() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {

                MediaStore.Audio.Media.DISPLAY_NAME
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC;
        String[] selectionArgs = {};
        String sortOrder = null;

        return new CursorLoader(
                getActivity(),
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }


    @Override
    public void onPause() {
        super.onPause();

        //homeCursorAdapter.swapCursor(null);
    }

}