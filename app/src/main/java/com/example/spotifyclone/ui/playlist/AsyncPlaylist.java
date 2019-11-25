package com.example.spotifyclone.ui.playlist;

import android.content.Context;
import android.os.AsyncTask;

import com.example.spotifyclone.MusicHelper;
import com.example.spotifyclone.objects.Playlist;

import java.util.ArrayList;

public class AsyncPlaylist  extends AsyncTask<Void, Void, ArrayList<Playlist>> {
    Context context;
    PlaylistListener listener;
    MusicHelper musicHelper = null;

    public AsyncPlaylist(Context context, PlaylistListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        musicHelper = new MusicHelper(context);
    }

    @Override
    protected ArrayList<Playlist> doInBackground(Void... voids) {
        ArrayList<Playlist> playlist = musicHelper.ListOfPlaylist();
        return playlist;
    }

    @Override
    protected void onPostExecute(ArrayList<Playlist> playlist) {
        super.onPostExecute(playlist);
        listener.PlaylistListener(playlist);
    }
}
