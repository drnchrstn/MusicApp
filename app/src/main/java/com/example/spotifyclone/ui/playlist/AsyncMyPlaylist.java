package com.example.spotifyclone.ui.playlist;

import android.content.Context;
import android.os.AsyncTask;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.objects.MusicPool;

import java.util.ArrayList;

public class AsyncMyPlaylist extends AsyncTask<Void, Void, ArrayList<MusicPool>> {
    private int Id;
    private Context context;
    MusicHelper musicHelper;
    MyPlaylistListener callback;

    public AsyncMyPlaylist(int id, Context context, MyPlaylistListener callback) {
        Id = id;
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected ArrayList<MusicPool> doInBackground(Void... voids) {
        musicHelper = new MusicHelper(context);
        ArrayList<MusicPool> musicPool = musicHelper.songFromPlaylist(Id);



        return musicPool;
    }

    @Override
    protected void onPostExecute(ArrayList<MusicPool> musicPool) {
        super.onPostExecute(musicPool);
        callback.MyPlaylistListener(musicPool);
    }
}