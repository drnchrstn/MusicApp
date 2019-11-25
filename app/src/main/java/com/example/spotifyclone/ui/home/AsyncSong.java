package com.example.spotifyclone.ui.home;

import android.content.Context;
import android.os.AsyncTask;

import com.example.spotifyclone.SingletonArraylist;

import java.io.File;
import java.util.ArrayList;

public class AsyncSong extends AsyncTask<Void, Void, ArrayList<File>> {

    Context context;
    ArrayList<File> songs;
    songListener callback;

    public AsyncSong(Context context, songListener callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected ArrayList<File> doInBackground(Void... voids) {
        songs = SingletonArraylist.getInstance(context).getdefaultlist();
        return songs;
    }

    @Override
    protected void onPostExecute(ArrayList<File> files) {
        super.onPostExecute(files);
        callback.songListener(files);
    }
}
